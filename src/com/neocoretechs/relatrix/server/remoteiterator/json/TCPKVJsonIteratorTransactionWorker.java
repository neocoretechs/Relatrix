package com.neocoretechs.relatrix.server.remoteiterator.json;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatementInterface;
import com.neocoretechs.relatrix.client.json.RemoteIteratorKVJsonClientTransaction;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.server.ThreadPoolManager;

/**
 * This TCPWorker is spawned for servicing traffic from clients after an initial CommandPacketInterface
 * has been sent from client to support remote iterators. It processes requests directly, invoking the proper iterator
 * methods on instances of server side iterators.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 *
 */
public class TCPKVJsonIteratorTransactionWorker implements Runnable {
	private static final boolean DEBUG = false;
	
	public volatile boolean shouldRun = true;
	protected Object waitHalt = new Object();
	
	public int MASTERPORT = 9876;

	protected InetAddress IPAddress = null;
	private SocketAddress masterSocketAddress;
	
	protected Socket workerSocket;
	protected Socket masterSocket;
	
	public static ConcurrentHashMap<String,ServerInvokeMethod> relatrixKVIteratorMethods = new ConcurrentHashMap<String,ServerInvokeMethod>(); // hasNext and next iterator methods
	private ServerInvokeMethod relatrixKVIteratorMethod = null;
	
	// ByteBuffer for NIO socket read/write, currently broken under arm 5/2015
	//private ByteBuffer b = ByteBuffer.allocate(LogToFile.DEFAULT_LOG_BUFFER_SIZE);
	private static boolean TEST = false;
	
    public TCPKVJsonIteratorTransactionWorker(Socket datasocket, String remoteMaster, int masterPort, String iteratorClass) throws IOException, ClassNotFoundException {
    	workerSocket = datasocket;
    	MASTERPORT= masterPort;
       	relatrixKVIteratorMethod = relatrixKVIteratorMethods.get(iteratorClass);
    	if(relatrixKVIteratorMethod == null) {
    		relatrixKVIteratorMethod = new ServerInvokeMethod(iteratorClass,0);
    		relatrixKVIteratorMethods.put(iteratorClass,relatrixKVIteratorMethod);
    	}
		try {
			if(TEST ) {
				IPAddress = InetAddress.getLocalHost();
			} else {
				IPAddress = InetAddress.getByName(remoteMaster);
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException("Bad remote master address:"+remoteMaster);
		}
		if(DEBUG) {
			System.out.printf("%s with params datasocket:%s, remoteMaster:%s masterPort:%d connection to masterPort at IPAddress:%s%n", this.getClass().getName(), datasocket.toString(), remoteMaster, masterPort, IPAddress.toString()); 
		}
		masterSocketAddress = new InetSocketAddress(IPAddress, MASTERPORT);
		masterSocket = new Socket();
		if(DEBUG)
			System.out.printf("%s about to connect socket to masterSocketAddress IPAddress:%s%n", this.getClass().getName(), masterSocketAddress.toString());
		masterSocket.connect(masterSocketAddress);
		masterSocket.setKeepAlive(true);
		//masterSocket.setTcpNoDelay(true);
		masterSocket.setReceiveBufferSize(32767);
		masterSocket.setSendBufferSize(32767);
		// spin the request processor thread for the worker
		if( DEBUG ) {
			System.out.println("Worker on port with master "+MASTERPORT+
					" address:"+IPAddress);
		}
	}
	
	/**
	 * Send a request on this worker,
	 * Instead of queuing to a running thread request queue, queue this for outbound message
	 * The type is RemoteCompletionInterface and contains the Id and the payload
	 * back to master
	 * @param irf
	 */
	public void sendResponse(RemoteResponseInterface irf) {
	
		if( DEBUG ) {
			System.out.println("Adding response "+irf+" to outbound from "+this.getClass().getName()+" to "+IPAddress+" port:"+MASTERPORT);
		}
		try {
			// Write response to master for forwarding to client
			String jirf = JSONObject.toJson(irf);
			if(DEBUG)
				System.out.println("Sending "+jirf+" to "+masterSocket);
			OutputStream os = masterSocket.getOutputStream();
			PrintWriter out = new PrintWriter(os, true);
			out.println(jirf);
		} catch (SocketException e) {
				//System.out.println("Exception setting up socket to remote master port "+MASTERPORT+e);
				//throw new RuntimeException(e);
		} catch (IOException e) {
				System.out.println("Socket send error "+e+" to address "+IPAddress+" on port "+MASTERPORT);
				throw new RuntimeException(e);
		}
	}
	/**
	 * Client (Slave port) sends data to our master in the following loop
	 */
	@Override
	public void run() {
		try {
			while(shouldRun) {
				if(DEBUG)
					System.out.println("TCPKVJsonIteratorTransactionWorker waiting getInputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				InputStream ins = workerSocket.getInputStream();
				if(DEBUG)
					System.out.println("TCPKVJsonIteratorTransactionWorker attempt readObject "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				BufferedReader in = new BufferedReader(new InputStreamReader(ins));
				JSONObject inJson = new JSONObject(in.readLine());
				if(DEBUG)
					System.out.println("TCPKVJsonIteratorTransactionWorker read "+inJson+" from "+workerSocket);
				RemoteIteratorKVJsonClientTransaction iori = (RemoteIteratorKVJsonClientTransaction) inJson.toObject();//RemoteIteratorKVJsonClientTransaction.class);	
				if( iori.getMethodName().equals("close") ) {
					RelatrixKVTransactionServer.sessionToObject.remove(iori.getSession());
				} else {
					// Get the iterator linked to this session
					Object itInst = RelatrixKVTransactionServer.sessionToObject.get(iori.getSession());
					if( itInst == null ) {
						in.close();
						throw new IOException("Requested iterator instance does not exist for session "+iori.getSession());
					}
					// invoke the desired method on this concrete server side iterator, let boxing take result
					//System.out.println(itInst+" class:"+itInst.getClass());
					Object result = relatrixKVIteratorMethod.invokeMethod(iori, itInst);
					if(result instanceof AbstractRelation) {
						((AbstractRelation)result).setTransactionId(((RelatrixKVTransactionStatementInterface)iori).getTransactionId());
						result = TransportMorphism.createTransport((Relation)result);
					}
					iori.setObjectReturn(result);
				}
				// notify latch waiters
				if( DEBUG ) {
					System.out.println("TCPKVJsonIteratorTransactionWorker FROM REMOTE on port:"+workerSocket+" "+iori);
				}
				// put the received request on the processing stack
				sendResponse((RemoteResponseInterface) iori);
			}
		// Call to shut down has been received from stopWorker
		} catch (Exception ie) {
			if(!(ie instanceof SocketException) && !(ie instanceof EOFException)) {
				ie.printStackTrace();
				System.out.println("Remote client disconnect with exception "+ie);
			}
		}
		finally {
			shouldRun = false;
			try {
				workerSocket.close();
			} catch (IOException e) {}
			try {
				masterSocket.close();
			} catch (IOException e) {}
			synchronized(waitHalt) {
				waitHalt.notify();
			}
		}
	}

	public String getMasterPort() {
		return String.valueOf(MASTERPORT);
	}

	public String getSlavePort() {
		return String.valueOf(workerSocket.getPort());
	}

	public void stopWorker() {
		// thread has been stopped by executor
		synchronized(waitHalt) {
			shouldRun = false;
			try {
				workerSocket.close(); // if we get a socket close error we probably dont want to wait anyway
				waitHalt.wait();
			} catch (InterruptedException | IOException e) {}
		}
	}
	/**
     * Spin the worker from command line
     * @param args
     * @throws Exception
     */
	public static void main(String args[]) throws Exception {
		if( args.length != 2 ) {
			System.out.println("Usage: java com.neocoretechs.relatrix.server.TCPKVJsonIteratorTransactionWorker [remote master node] [remote master port] [iterator class]");
		}
		ThreadPoolManager.getInstance().spin(new TCPKVJsonIteratorTransactionWorker(new Socket(),
				args[0], // remote master node
				Integer.valueOf(args[1]),args[2])); // master port, class
	}
}
