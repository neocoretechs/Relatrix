package com.neocoretechs.relatrix.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;


/**
 * This TCPWorker is spawned for servicing traffic from clients after an initial CommandPacketInterface
 * has been sent from client to WORKBOOTPORT. A WorkerRequestProcessor handles the actual processing of the
 * request after it has been acquired and extracted here.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 *
 */
public class TCPWorker implements Runnable {
	private static final boolean DEBUG = false;
	
	public volatile boolean shouldRun = true;
	protected Object waitHalt = new Object();
	
	public int MASTERPORT = 9876;

    //private byte[] sendData;
	protected InetAddress IPAddress = null;
	//private ServerSocketChannel workerSocketChannel;
	//private SocketAddress workerSocketAddress;
	//private SocketChannel masterSocketChannel;
	private SocketAddress masterSocketAddress;
	
	protected Socket workerSocket;
	protected Socket masterSocket;
	
	protected WorkerRequestProcessor workerRequestProcessor;
	// ByteBuffer for NIO socket read/write, currently broken under arm 5/2015
	//private ByteBuffer b = ByteBuffer.allocate(LogToFile.DEFAULT_LOG_BUFFER_SIZE);
	private static boolean TEST = false;
	
    public TCPWorker(Socket datasocket, String remoteMaster, int masterPort) throws IOException {
    	workerSocket = datasocket;
    	MASTERPORT= masterPort;
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
		workerRequestProcessor = new WorkerRequestProcessor(this);
		SynchronizedThreadManager.getInstance().spin(workerRequestProcessor);
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
	 * @param irf the remote response to be sent back to masterSocket
	 */
	public void sendResponse(RemoteResponseInterface irf) {
	
		if( DEBUG ) {
			System.out.println("Adding response "+irf+" to outbound from "+this.getClass().getName()+" to "+IPAddress+" port:"+MASTERPORT);
		}
		try {
			// Write response to master for forwarding to client
			OutputStream os = masterSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(irf);
			oos.flush();
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
					System.out.println("TCPWorker waiting getInputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				InputStream ins = workerSocket.getInputStream();
				if(DEBUG)
					System.out.println("TCPWorker ObjectInputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				ObjectInputStream ois = new ObjectInputStream(ins);
				if(DEBUG)
					System.out.println("TCPWorker attempt readObject "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				RemoteCompletionInterface iori = (RemoteCompletionInterface)ois.readObject();
				if( DEBUG ) {
					System.out.println("TCPWorker FROM REMOTE on port:"+workerSocket+" "+iori);
				}
				// put the received request on the processing stack
				workerRequestProcessor.getQueue().put(iori);
			}
		// Call to shut down has been received from stopWorker
		} catch (IOException | ClassNotFoundException | InterruptedException ie) {
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
			System.out.println("Usage: java com.neocoretechs.relatrix.server.TCPWorker [remote master node] [remote master port]");
		}
		SynchronizedThreadManager.getInstance().spin(new TCPWorker(new Socket(),
				args[0], // remote master node
				Integer.valueOf(args[1]))); // master port
	}
}
