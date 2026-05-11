package com.neocoretechs.relatrix.server;

import java.io.EOFException;
import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import com.neocoretechs.relatrix.client.RelatrixClient;
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
	private static final boolean DEBUG = true;
	
	public volatile boolean shouldRun = true;
	protected Object waitHalt = new Object();
	
	public int MASTERPORT = 9876;

	protected InetAddress IPAddress = null;
	private SocketAddress masterSocketAddress;
	
	protected SocketChannel workerSocket;
	protected SocketChannel masterSocket;
	
	protected WorkerRequestProcessor workerRequestProcessor;
	private static boolean TEST = false;
	
    public TCPWorker(SocketChannel datasocket, String remoteMaster, int masterPort) throws IOException {
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
		masterSocket = SocketChannel.open();
		masterSocket.configureBlocking(true);
		if(DEBUG)
			System.out.printf("%s about to connect socket to masterSocketAddress IPAddress:%s%n", this.getClass().getName(), masterSocketAddress.toString());
		if(!masterSocket.connect(masterSocketAddress)) {
			while(!masterSocket.finishConnect()) {
				if(DEBUG)
					System.out.printf("%s RETRY connect socket to masterSocketAddress IPAddress:%s%n", this.getClass().getName(), masterSocketAddress.toString());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
		}
		masterSocket.setOption(StandardSocketOptions.SO_KEEPALIVE,true);
		masterSocket.setOption(StandardSocketOptions.SO_RCVBUF,32767);
		masterSocket.setOption(StandardSocketOptions.SO_SNDBUF,32767);
		// spin the request processor thread for the worker
		workerRequestProcessor = new WorkerRequestProcessor(this);
		SynchronizedThreadManager.getInstance().spin(workerRequestProcessor);
		if( DEBUG ) {
			System.out.println("Worker on port with master "+MASTERPORT+
					" address:"+IPAddress+" channel:"+masterSocket+" wroker:"+workerSocket+" connected:"+workerSocket.isConnected());
			if(!workerSocket.isConnected())
				System.out.println("Worker Not connected, pending:"+workerSocket.isConnectionPending());
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
    		RelatrixClient.sendObject(workerSocket, irf);
    	} catch (IOException e) {
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
					System.out.println("TCPWorker waiting getInputStream "+workerSocket+" connected:"+workerSocket.isConnected());
				RemoteCompletionInterface iori = (RemoteCompletionInterface)RelatrixClient.receiveObject(workerSocket);
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
			System.out.println("Usage: java com.neocoretechs.relatrix.server.TCPWorker [remote master node] [remote master port]");
		}
		SynchronizedThreadManager.getInstance().spin(new TCPWorker(SocketChannel.open(),
				args[0], // remote master node
				Integer.valueOf(args[1]))); // master port
	}
}
