package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.Serializable;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * Manages remote iterators via client that is serialized to remote transaction servers and returned as payload.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorClientTransaction implements Runnable, RelatrixTransactionStatementInterface, Serializable, Iterator {
	private static final long serialVersionUID = 1L;
	public static final boolean DEBUG = true;
	public static final boolean LOCALTEST = false; // use localhost as remote node
	public static final boolean TEST = false; // timing
	private long tim;
	
	private String remoteNode;
	private int remotePort;

	protected transient SocketChannel workerSocket = null; // socket assigned to slave port
	protected transient ConnectionHandler workerHandler;
	
	private volatile boolean shouldRun = true; // master service thread control
	private transient Object waitHalt;
	private transient Object waitPayload;
	private transient Object waitSocket;
	
	private String session;
	private TransactionId transactionId;
	
	private Object objectReturn;
	
	private String methodName;
	private Object[] paramArray = new Object[0];
	private Class<?>[] params = new Class<?>[0];
	private String returnClass;
	
	private transient RemoteIteratorClientTransaction returnPayload;

	/**
	 * Start a client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode Name of local master socket
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RemoteIteratorClientTransaction(TransactionId transactionId, String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		session = UUID.randomUUID().toString();
		this.transactionId = transactionId;
	}
	
	public RemoteIteratorClientTransaction() {
	}
	
	/**
	 * When we deserialize this from the server as a result of remote method call, we get back the serialized
	 * object with remote server info. Here, we want to do the actual connection to remote.
	 */
	@Override
	public void process() throws Exception {
		waitHalt = new Object();
		waitPayload = new Object();
		waitSocket = new Object();
	
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		try {
			workerHandler = new ConnectionHandler(workerSocket);
			System.out.println("Channel created to "+workerHandler);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		SynchronizedThreadManager.getInstance().spin(this);
	}
	
	@Override
	public void run() {
		synchronized(waitSocket) {
			waitSocket.notify();
		}
		try {
			while(shouldRun) {
				returnPayload = (RemoteIteratorClientTransaction) workerHandler.readObject();
				synchronized(waitPayload) {
					objectReturn = returnPayload.getObjectReturn();
					if(objectReturn == TransportMorphism.class)
						objectReturn = TransportMorphism.createMorphism((TransportMorphism) objectReturn);
					else
						if(objectReturn instanceof Result)
							((Result)objectReturn).unpackFromTransport();
					if( DEBUG )
						System.out.println("FROM Remote, returned object from response:"+objectReturn+" handler:"+workerHandler);
					if( objectReturn instanceof Exception ) {
						System.out.println("RemoteIteratorClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)objectReturn).getCause());
						objectReturn = ((Throwable)objectReturn).getCause();
					}
					waitPayload.notify();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(this.getClass().getName()+": receive IO error "+e+" handler:"+workerHandler);
			shutdown();
		}
		synchronized(waitHalt) {
			waitHalt.notifyAll();
		}

	}
	/**
	 * Send 'this' via workersocket
	 * @throws Exception
	 */
	public void sendCommand() throws Exception {
		if(workerHandler == null) {
			synchronized(waitSocket) {
				waitSocket.wait();
			}
		}
		workerHandler.sendObject(this);
	}
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @return The next iterated object or null
	 */
	@Override
	public Object next() {
		this.methodName = "next";
		synchronized(waitPayload) {
			try {
				sendCommand();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				if(TEST)
					tim = System.nanoTime();
				waitPayload.wait();
				if(TEST)
					System.out.println("next waited:"+(System.nanoTime()-tim)+" nanos.");
			} catch (InterruptedException e) {}
		}
		return objectReturn;
	}
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @return The boolean result of hasNext on server
	 */
	@Override
	public boolean hasNext() {
		methodName = "hasNext";
		synchronized(waitPayload) {
			try {
				sendCommand();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				if(TEST)
					tim = System.nanoTime();
				waitPayload.wait();
				if(TEST)
					System.out.println("hasNext waited:"+(System.nanoTime()-tim)+" nanos.");
			} catch (InterruptedException e) {}
		}
		return (boolean) objectReturn;
	}
	/**
	 * set shouldRun to false to stop run loop, wait for loop to end, then call shutdown()
	 */
	public void close() {
		if(DEBUG)
			System.out.println("Calling close for RemoteIteratorClientTransaction");
		shouldRun = false;
		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException ie) {}
		}
		shutdown();
	}

	private void shutdown() {
		if(DEBUG)
			System.out.println("Calling shutdown for RemoteIteratorClientTransaction");
		if( workerHandler != null ) {
			workerHandler.close();
		}
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}

	public String getRemoteNode() {
		return remoteNode;
	}

	public int getRemotePort( ) {
		return remotePort;
	}

	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d workerSocket socket:%s session:%s method:%s return:%s%n",this.getClass().getName(), remoteNode, remotePort, workerSocket, session, methodName, objectReturn);
	}

	@Override
	public String getSession() {
		return session;
	}

	@Override
	public String getMethodName() {
		return methodName;
	}

	@Override
	public Object[] getParamArray() {
		return paramArray;
	}

	@Override
	public String getReturnClass() {
		return returnClass;
	}

	@Override
	public void setReturnClass(String className) {
		returnClass = className;
	}
	
	@Override
	public Class<?>[] getParams() {
		return params;
	}

	@Override
	public Object getObjectReturn() {
		return objectReturn;
	}

	@Override
	public Object getCompletionObject() {
		return null;
	}

	@Override
	public void setCompletionObject(Object cdl) {
	}
	
	@Override
	public synchronized void signalCompletion(Object o) {
	}
	
	@Override
	public void setObjectReturn(Object o) {
		objectReturn = o;
	}

	@Override
	public TransactionId getTransactionId() {
		return transactionId;
	}
	/**
	 * Generic call to server localaddr, remote addr, port, server method, arg1 to method, arg2 to method...
	 * java RemoteIteratorClientTransaction VOLVATRON VOLVATRON 9010 java.lang.String
	 * @param args
	 * @throws Exception
	 */
	static int i = 0;
	public static void main(String[] args) throws Exception {
		RelatrixClientTransaction rc = new RelatrixClientTransaction(args[1],Integer.parseInt(args[2]));
		TransactionId xid = rc.getTransactionId();
		RelatrixTransactionStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(xid,Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);				
				break;
			case 5:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4]);
				break;
			case 6:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		System.out.println(rc.sendCommand(rs));
		rc.endTransaction(xid);
		rc.close();
	}
}
