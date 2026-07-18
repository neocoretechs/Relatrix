package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.Serializable;

import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.UUID;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.TransportMorphism;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * Manages remote iterators via client that is serialized to remote iterator servers and returned as payload.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorClient implements Runnable, RelatrixStatementInterface, Serializable, Iterator {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
	public static final boolean LOCALTEST = false; // use localhost as remote node
	public static final boolean TEST = false; // timing
	private long tim;
	
	private String remoteNode;
	private int remotePort;

	protected transient SocketChannel workerSocket = null; // socket assigned to slave port
	protected transient ConnectionHandler workerHandler;
	
	private volatile boolean shouldRun = true; // master service thread control
	private transient Object waitHalt = new Object();
	
	private String session;
	private Object objectReturn;
	
	private String methodName;
	private Object[] paramArray = new Object[0];
	private Class<?>[] params = new Class<?>[0];
	private String returnClass;
	
	private transient RemoteIteratorClient returnPayload;
	private transient Object waitPayload = new Object();

	/**
	 * Start a client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RemoteIteratorClient(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		session = UUID.randomUUID().toString();
		if(DEBUG)
			System.out.printf("%s ctor %s%n",this.getClass().getName(), this.toString());
	}
	
	public RemoteIteratorClient()  {
		if(DEBUG)
			System.out.printf("%s default ctor %s%n",this.getClass().getName(), this.toString());
	}
	
	/**
	 * When we deserialize this from the server as a result of remote method call, we get back the serialized
	 * object with remote server info. Here, we want to do the actual connection to remote.
	 */
	@Override
	public void process() throws Exception {
		if(workerSocket == null) {
			workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
			workerHandler = new ConnectionHandler(workerSocket, Thread.currentThread().getContextClassLoader());
			waitPayload = new Object();
			SynchronizedThreadManager.getInstance().spin(this);
			if(DEBUG)
				System.out.printf("%s process() called for %s%n",this.getClass().getName(), this.toString());
		} else {
			throw new IOException(String.format("%s process() called for existing workerSocket %s%n",this.getClass().getName(), this.toString()));
		}
	}
	
	@Override
	public void run() {
		try {
			while(shouldRun) {
				returnPayload = (RemoteIteratorClient) workerHandler.readObject();
				objectReturn = returnPayload.getObjectReturn();
				if( DEBUG )
					System.out.printf("%s FROM Remote, from remote node:%s remote port:%s return object:%s%n",this.getClass().getName(),remoteNode,String.valueOf(remotePort),objectReturn);
				if(objectReturn == TransportMorphism.class)
					objectReturn = TransportMorphism.createMorphism((TransportMorphism) objectReturn);
				else
					if(objectReturn instanceof Result)
						((Result)objectReturn).unpackFromTransport();
					else
						if(objectReturn instanceof Exception ) {
							System.out.println(this.getClass().getName()+" ******** REMOTE EXCEPTION ******** "+((Throwable)objectReturn).getCause());
							objectReturn = ((Throwable)objectReturn).getCause();
						}
				synchronized(waitPayload) {
					waitPayload.notifyAll();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(this.getClass().getName()+": receive IO error "+e+" remote node:"+remoteNode+" port:"+remotePort);
			shutdown();
		}
		synchronized(waitHalt) {
			waitHalt.notifyAll();
		}

	}
	/**
	 * Send 'this' via workerSocket
	 * @throws Exception
	 */
	public void sendCommand() throws Exception {
		workerHandler.sendObject(this);
		synchronized(waitPayload) {
			waitPayload.wait();
		}
	}
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The next iterated object or null
	 */
	@Override
	public Object next() {
		this.methodName = "next";
		try {
			sendCommand();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(DEBUG)
			System.out.printf("%s waitsocket return next %s%n",this.getClass().getName(), this.toString());
		return objectReturn;
	}
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The boolean result of hasNext on server
	 */
	@Override
	public boolean hasNext() {
		methodName = "hasNext";
		try {
			sendCommand();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(DEBUG)
			System.out.printf("%s waitsocket return hasNext %s%n",this.getClass().getName(), this.toString());
		return (boolean) objectReturn;
	}
	/**
	 * set shouldRun to false to stop run loop, wait for loop to end, then call shutdown()
	 */
	public void close() {
		if(DEBUG)
			System.out.println("Calling close for RemoteIteratorClient");
		shouldRun = false;
		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException e) {}
		}
		shutdown();
	}

	private void shutdown() {
		if(DEBUG)
			System.out.println("Calling shutdown for RemoteIteratorClient");
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
		return String.format("%s RemoteNode:%s RemotePort:%d workerSocket:%s session:%s method:%s handler:%s return:%s%n", this.getClass().getName(), remoteNode, remotePort, workerSocket, session, methodName, workerHandler, objectReturn);
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
	public void setMethodName(String methodName) {
		this.methodName = methodName;	
	}

	@Override
	public void setParamArray(Object[] params) {
		this.paramArray = params;	
	}

}
