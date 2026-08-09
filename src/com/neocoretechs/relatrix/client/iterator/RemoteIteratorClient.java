package com.neocoretechs.relatrix.client.iterator;

import java.io.IOException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClient;

/**
 * Manages remote iterators via client that is serialized to remote iterator servers and returned as payload.
 * Unlike the other client/server contracts, we are not using a statement, but sending this as a statement
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorClient extends RemoteIteratorInterfaceImpl implements RelatrixStatementInterface {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	public static final boolean LOCALTEST = false; // use localhost as remote node
	public static final boolean TEST = false; // timing
	private transient long tim;
	
	private String remoteNode;
	private int remotePort;
	private int mainPort;
	private UUID session;
	private UUID iteratorId;
	
	private String methodName;
	private Object[] paramArray = new Object[0];
	private Class<?>[] params = new Class<?>[0];
	private String returnClass;

	private Object objectReturn;

	private transient CompletableFuture<Object> completionObject;
	private transient CountDownLatch completionLatch;

	private transient AsynchRelatrixClient asynchClient;

	public RemoteIteratorClient()  {
		if(DEBUG)
			System.out.printf("%s default ctor %s%n",this.getClass().getName(), this.toString());
	}
	/**
	 * Start a  client to a remote server.
	 * @param remoteNode The remote Node
	 * @param remotePort The remote port of the iterator server
	 * @param mainPort The main server port for resolver
	 * @throws IOException if connect fail
	 */
	public RemoteIteratorClient(UUID session, String remoteNode, int remotePort, int mainPort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		this.mainPort = mainPort;
		this.session = session;
		if(DEBUG)
			System.out.printf("%s ctor %s%n",this.getClass().getName(), this.toString());
	}
	
	@Override
	public UUID getSession() {
		return session;
	}
	
	@Override
	public String getRemoteNode() {
		return remoteNode;
	}
	@Override
	public int getRemotePort() {
		return mainPort;
	}
	
	/**
	 * When we deserialize this from the server as a result of remote method call, we get back the serialized
	 * object with remote server info. Here, we want to do the actual connection to remote.
	 */
	@Override
	public void process() throws Exception {
		if(asynchClient == null) {
			asynchClient = new AsynchRelatrixClient(remoteNode, remotePort);
			if(DEBUG)
				System.out.printf("%s process() resolver and handler created %s%n",this.getClass().getName(), this.toString());
		} else {
			throw new IOException(String.format("%s process() called for existing workerSocket %s%n",this.getClass().getName(), this.toString()));
		}
	}

	@Override
	/**
	 * Send 'this' via workerSocket
	 * @throws Exception
	 */
	public Object sendCommand(String command) throws Exception {
		this.methodName = command;
		CompletableFuture<Object> cf = asynchClient.queueCommand(this);
		if(DEBUG)
			System.out.printf("%s sendCommand(%s) wait with cf %x%n",this.getClass().getName(),command,System.identityHashCode(cf));
		return cf.orTimeout(30, TimeUnit.SECONDS).get();
	}

	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d MainPort:%d session:%s method:%s handler:%s return:%s%n", this.getClass().getName(), remoteNode, remotePort, mainPort, session, methodName, asynchClient, objectReturn);
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
		return completionLatch;
	}
    @Override 
    public CompletableFuture<Object> getCompletionFuture() {
    	return completionObject;
    }
	@Override
	public void setCompletionObject() {
		completionLatch = new CountDownLatch(1);
		completionObject = new CompletableFuture<Object>();
	}

	@Override
	public void signalCompletion(Object o) {
	    // Capture state under lock, but do not complete the CF while holding the lock
	    completionLatch.countDown();
	    // Log before attempting to complete
	    if (DEBUG)
	        System.out.printf("%s.SIGNAL before complete rs=%x cf=%x oClass=%s%n",this.getClass().getName(),System.identityHashCode(this), completionObject == null ? 0 : System.identityHashCode(completionObject), o == null ? "null" : o.getClass().getName());
	    // Complete the CompletableFuture outside the synchronized block
	    if (completionObject != null) {
	        try {
	            if (o instanceof Throwable) 
	            	completionObject.completeExceptionally((Throwable) o);
	            else 
	            	completionObject.complete(o);
	        } catch (Throwable t) {
	            // Ensure caller doesn't hang if complete throws
	            try { 
	            	completionObject.completeExceptionally(t); 
	            } catch (Throwable ignore) {}
	            System.err.printf("%s.signalCompletion: cf.complete threw for rs=%x: %s%n",this.getClass().getName(), System.identityHashCode(this), t);
	        }
	    } else {
	        if (DEBUG) 
	            System.out.printf("%s.SIGNAL no CompletableFuture present for rs=%x%n",this.getClass().getName(), System.identityHashCode(this));
	    }
	    if(DEBUG)
	        System.out.printf("%s.SIGNAL after complete rs=%x cf=%x done=%b%n",this.getClass().getName(), System.identityHashCode(this),(completionObject != null ? System.identityHashCode(completionObject) : 0), (completionObject != null ? completionObject.isDone() : false));
	}

	@Override
	public void setObjectReturn(Object o) {
		objectReturn = o;
		if( DEBUG )
			System.out.printf("%s.setObjectReturn FROM Remote, from remote node:%s remote port:%s return object:%s%n",this.getClass().getName(),remoteNode,String.valueOf(remotePort),objectReturn);
		if(objectReturn == TransportMorphism.class)
			objectReturn = TransportMorphism.createMorphism((TransportMorphism) objectReturn);
		else
			if(objectReturn instanceof Result)
				((Result)objectReturn).unpackFromTransport();
			else
				if(objectReturn instanceof Exception ) {
					System.out.println(this.getClass().getName()+" ******** REMOTE EXCEPTION ******** "+o);
					objectReturn = ((Throwable)objectReturn).getCause();
				}
	}
	@Override
	public void setServerObjectReturn(Object o) {
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
	public void close() {
		shutdown();
	}
	
	public void shutdown() {
		asynchClient.close();
	}
	@Override
	public UUID getIteratorId() {
		return iteratorId;
	}
	@Override
	public void setIteratorId(UUID itid) {
		this.iteratorId = itid;
	}
	public AsynchRelatrixClient getClient() {
		return asynchClient;
	}
	@Override
	public void setClient(RemoteIteratorClient iteratorClient) {
		asynchClient = iteratorClient.asynchClient;
	}
	
}
