package com.neocoretechs.relatrix.client.iterator;

import java.io.IOException;
import java.io.Serializable;

import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * Manages remote iterators via client that is serialized to remote iterator servers and returned as payload.
 * Unlike the other client/server contracts, we are not using a statement, but sending this as a statement
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorClient extends RemoteIteratorInterfaceImpl implements Runnable, RelatrixStatementInterface {
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
	
	private UUID session;
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
	 * @param session the session UUID
	 * @param remoteNode remote node of server
	 * @param remotePort port of remote node
	 * @throws IOException
	 */
	public RemoteIteratorClient(UUID session, String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		this.session = session;
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
			IndexResolver indexResolver = new IndexResolver();
			indexResolver.setRemote(this);
			ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());
			workerHandler = new ConnectionHandler(workerSocket, Thread.currentThread().getContextClassLoader(), pec);
			waitPayload = new Object();
			waitHalt = new Object();
			if(DEBUG)
				System.out.printf("%s process() resolver and handler created, ready to spin with context for %s%n",this.getClass().getName(), this.toString());
			SynchronizedThreadManager.getInstance().spinWithContext(this, pec);
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
	@Override
	/**
	 * Send 'this' via workerSocket
	 * @throws Exception
	 */
	public Object sendCommand(String command) throws Exception {
		this.methodName = command;
		workerHandler.sendObject(this);
		synchronized(waitPayload) {
			waitPayload.wait();
		}
		return objectReturn;
	}
	@Override
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
	@Override
	public void shutdown() {
		if(DEBUG)
			System.out.println("Calling shutdown for RemoteIteratorClient");
		if( workerHandler != null ) {
			workerHandler.close();
		}
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}

	@Override
	public String getRemoteNode() {
		return remoteNode;
	}
	@Override
	public int getRemotePort( ) {
		return remotePort;
	}

	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d workerSocket:%s session:%s method:%s handler:%s return:%s%n", this.getClass().getName(), remoteNode, remotePort, workerSocket, session, methodName, workerHandler, objectReturn);
	}

	@Override
	public UUID getSession() {
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
	public CountDownLatch getCompletionObject() {
		return null;
	}

	@Override
	public void setCompletionObject() {
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

	@Override
	public CompletableFuture<Object> getCompletionFuture() {
		return null;
	}

}
