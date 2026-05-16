package com.neocoretechs.relatrix.server.remoteiterator.json;

import java.io.EOFException;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatementInterface;
import com.neocoretechs.relatrix.client.json.RemoteIteratorKVJsonClientTransaction;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.server.json.RelatrixJsonServer;

/**
 * This TCPWorker is spawned for servicing traffic from clients 
 * to support remote iterators. It processes requests directly, invoking the proper iterator
 * methods on instances of server side iterators.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 *
 */
public class TCPKVJsonIteratorTransactionWorker implements Runnable {
	private static final boolean DEBUG = false;
	private static boolean TEST = false;
	
	public volatile boolean shouldRun = true;
	protected Object waitHalt = new Object();
	
	protected SocketChannel workerSocket;
	
	public static ConcurrentHashMap<String,ServerInvokeMethod> relatrixKVIteratorMethods = new ConcurrentHashMap<String,ServerInvokeMethod>(); // hasNext and next iterator methods
	private ServerInvokeMethod relatrixKVIteratorMethod = null;
	
    public TCPKVJsonIteratorTransactionWorker(SocketChannel datasocket, String iteratorClass) throws IOException, ClassNotFoundException {
    	workerSocket = datasocket;
       	relatrixKVIteratorMethod = relatrixKVIteratorMethods.get(iteratorClass);
    	if(relatrixKVIteratorMethod == null) {
    		relatrixKVIteratorMethod = new ServerInvokeMethod(iteratorClass,0);
    		relatrixKVIteratorMethods.put(iteratorClass,relatrixKVIteratorMethod);
    	}
		// spin the request processor thread for the worker
		if( DEBUG ) {
			System.out.println(this.getClass().getName()+" with address: "+datasocket.toString());
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
			System.out.println("Adding response "+irf+" to outbound from "+this.getClass().getName()+" to "+workerSocket.toString());
		}
		try {
			// Write response to master for forwarding to client
			String jirf = JSONObject.toJson(irf);
			if(DEBUG)
				System.out.println("Sending "+jirf+" to "+workerSocket);
			RelatrixJsonServer.writeLineBlocking(workerSocket, jirf, null);
		} catch (SocketException e) {
				//System.out.println("Exception setting up socket to remote master port "+MASTERPORT+e);
				//throw new RuntimeException(e);
		} catch (IOException e) {
				System.out.println("Channel send error "+e+" to address "+workerSocket.toString());
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
					System.out.println(this.getClass().getName()+" waiting getInputStream "+workerSocket+" connected:"+workerSocket.isConnected());
				String s = new String(RelatrixJsonServer.readUntil(workerSocket, (byte)'\n'));
				JSONObject inJson = new JSONObject(s);
				if(DEBUG)
					System.out.println(this.getClass().getName()+" read "+inJson+" from "+workerSocket);
				RemoteIteratorKVJsonClientTransaction iori = (RemoteIteratorKVJsonClientTransaction) inJson.toObject();//RemoteIteratorKVJsonClientTransaction.class);	
				if( iori.getMethodName().equals("close") ) {
					RelatrixKVTransactionServer.sessionToObject.remove(iori.getSession());
				} else {
					// Get the iterator linked to this session
					Object itInst = RelatrixKVTransactionServer.sessionToObject.get(iori.getSession());
					if( itInst == null ) {
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
					System.out.println(this.getClass().getName()+" FROM REMOTE on port:"+workerSocket+" "+iori);
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
			synchronized(waitHalt) {
				waitHalt.notify();
			}
		}
	}

	public String getSlavePort() {
		return String.valueOf(workerSocket);
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
		SynchronizedThreadManager.getInstance().spin(new TCPKVJsonIteratorTransactionWorker(SocketChannel.open(new InetSocketAddress(args[0],Integer.parseInt(args[1]))),args[2])); // master port, class
	}
}
