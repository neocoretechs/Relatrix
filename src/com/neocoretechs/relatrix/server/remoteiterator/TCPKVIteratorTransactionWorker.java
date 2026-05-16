package com.neocoretechs.relatrix.server.remoteiterator;

import java.io.EOFException;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.TransportMorphism;

import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * This TCPWorker is spawned for servicing traffic from clients 
 * to support remote iterators. It processes requests directly, invoking the proper iterator
 * methods on instances of server side iterators.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 *
 */
public class TCPKVIteratorTransactionWorker implements Runnable {
	private static final boolean DEBUG = false;
	private static boolean TEST = false;
	
	public volatile boolean shouldRun = true;
	protected Object waitHalt = new Object();
	
	protected SocketChannel workerSocket;
	protected ConnectionHandler workerHandler;
	
	public static ConcurrentHashMap<String,ServerInvokeMethod> relatrixKVIteratorMethods = new ConcurrentHashMap<String,ServerInvokeMethod>(); // hasNext and next iterator methods
	private ServerInvokeMethod relatrixKVIteratorMethod = null;
	
    public TCPKVIteratorTransactionWorker(SocketChannel datasocket, String iteratorClass) throws IOException, ClassNotFoundException {
    	workerSocket = datasocket;
    	workerHandler = new ConnectionHandler(datasocket);
       	relatrixKVIteratorMethod = relatrixKVIteratorMethods.get(iteratorClass);
    	if(relatrixKVIteratorMethod == null) {
    		relatrixKVIteratorMethod = new ServerInvokeMethod(iteratorClass,0);
    		relatrixKVIteratorMethods.put(iteratorClass,relatrixKVIteratorMethod);
    	}
		if(DEBUG) {
			System.out.printf("%s with params handler:%s%n", this.getClass().getName(), workerHandler.toString()); 
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
			System.out.println("Adding response "+irf+" to outbound from "+this.getClass().getName()+" to "+workerHandler);
		}
		try {
			// Write response to master for forwarding to client
			workerHandler.sendObject(irf);
		} catch (SocketException e) {
				//System.out.println("Exception setting up socket to remote master port "+MASTERPORT+e);
				//throw new RuntimeException(e);
		} catch (IOException e) {
				System.out.println("Channel send error "+e+" to "+workerHandler);
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
					System.out.println(this.getClass().getName()+" connected:"+workerSocket.isConnected());
				RemoteCompletionInterface iori = (RemoteCompletionInterface)workerHandler.readObject();
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
		synchronized(waitHalt) {
			shouldRun = false;
			try {
				workerHandler.close(); // if we get a socket close error we probably dont want to wait anyway
				waitHalt.wait();
			} catch (InterruptedException e) {}
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
			System.out.println("Usage: java com.neocoretechs.relatrix.server.TCPKVIteratorTransactionWorker [remote master node] [remote master port] [iterator class]");
		}
		SynchronizedThreadManager.getInstance().spin(new TCPKVIteratorTransactionWorker(SocketChannel.open(new InetSocketAddress(args[0],Integer.parseInt(args[1]))),args[2])); // master port, class
	}
}
