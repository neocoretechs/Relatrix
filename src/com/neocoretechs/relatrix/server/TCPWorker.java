package com.neocoretechs.relatrix.server;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;

import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * This TCPWorker is spawned for servicing traffic from clients. A {@link WorkerRequestProcessor} handles the actual processing of the
 * request after it has been acquired and extracted here. Creates a {@link ConnectionHandler}
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 *
 */
public class TCPWorker implements Runnable {
	private static final boolean DEBUG = true;
	
	public volatile boolean shouldRun = true;
	protected Object waitHalt = new Object();
	
	protected SocketChannel workerSocket;

	protected ConnectionHandler workerHandler;
	
	protected WorkerRequestProcessor workerRequestProcessor;
	
	public TCPWorker() {}
	
    public TCPWorker(SocketChannel workerSocket) throws IOException {
    	this.workerSocket = workerSocket;
		workerHandler = new ConnectionHandler(workerSocket);
		// spin the request processor thread for the worker
		workerRequestProcessor = new WorkerRequestProcessor(this);
		SynchronizedThreadManager.getInstance().spin(workerRequestProcessor);
		if( DEBUG ) {
			System.out.printf("%s%n",this);
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
    		System.out.println(this+" Adding response "+irf);
    	}
    	try {
    		// Write response to master for forwarding to client
    		workerHandler.sendObject(irf);
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
					System.out.println(this+" waiting getInputStream ");
				RemoteCompletionInterface iori = (RemoteCompletionInterface)workerHandler.readObject();
				if(iori == null)
					break;
				if( DEBUG ) {
					System.out.println(this+" FROM REMOTE got:"+iori);
				}
				// put the received request on the processing stack
				workerRequestProcessor.getQueue().put(iori);
			}
		// Call to shut down has been received from stopWorker
		} catch (InterruptedException | ClassNotFoundException | IOException ie) {
			ie.printStackTrace();
		}
		finally {
			shouldRun = false;
			workerHandler.close();
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
		shouldRun = false;
		workerHandler.close();
		synchronized(waitHalt) {
			waitHalt.notify();
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s worker=%s%n",this.getClass().getName(),workerHandler);
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
		SynchronizedThreadManager.getInstance().spin(new TCPWorker(SocketChannel.open(new InetSocketAddress(args[0],Integer.parseInt(args[1]))))); // master port
	}
}
