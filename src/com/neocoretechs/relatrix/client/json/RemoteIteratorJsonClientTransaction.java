package com.neocoretechs.relatrix.client.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONObject;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.json.RelatrixJsonServer;
import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.client.json.RemoteIteratorJsonClientTransaction;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
/**
 * Manages remote iterators via client that is serialized to remote transaction servers and returned as payload.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorJsonClientTransaction implements Runnable, RelatrixTransactionStatementInterface, Serializable, Iterator {
	private static final long serialVersionUID = 1L;
	public static final boolean DEBUG = true;
	public static final boolean LOCALTEST = false; // use localhost as remote node
	public static final boolean TEST = false; // timing
	private long tim;
	
	private String remoteNode;
	private int remotePort;

	protected transient SocketChannel workerSocket = null; // socket assigned to slave port

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
	
	private transient RemoteIteratorJsonClientTransaction returnPayload;

	/**
	 * A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param transactionId the TransactionId
	 * @param remoteNode The remote node
	 * @param remotePort The remote port
	 * @throws IOException if connection fails
	 */
	public RemoteIteratorJsonClientTransaction(TransactionId transactionId, String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		session = UUID.randomUUID().toString();
		this.transactionId = transactionId;
	}
	
	public RemoteIteratorJsonClientTransaction() {
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
		if( DEBUG ) {
			System.out.println(this.getClass().getName()+" got connection "+workerSocket);
		}
		SynchronizedThreadManager.getInstance().spin(this);
	}
	
	@Override
	public void run() {
		synchronized(waitSocket) {
			waitSocket.notify();
		}
		try {
			while(shouldRun) {
				String s = new String(RelatrixJsonServer.readUntil(workerSocket, (byte)'\n'));
				JSONObject inJson = new JSONObject(s);
				if(DEBUG)
					System.out.println(this.getClass().getName()+" read "+inJson+" from "+workerSocket);
				returnPayload =  (RemoteIteratorJsonClientTransaction) inJson.toObject();//RemoteIteratorJsonClientTransaction.class);
				synchronized(waitPayload) {
					objectReturn = returnPayload.getObjectReturn();
					if(objectReturn.getClass() == HashMap.class) {
						if(DEBUG)
							System.out.println(this.getClass().getName()+" attempt to instantiate returnPayload class "+returnPayload.getReturnClass()+" for "+objectReturn);
						JSONObject jobj = (JSONObject) JSONObject.wrap(objectReturn);
						objectReturn = jobj.toObject(Class.forName(returnPayload.getReturnClass()));
					}
					if(objectReturn == TransportMorphism.class)
						objectReturn = TransportMorphism.createMorphism((TransportMorphism) objectReturn);
					else
						if(objectReturn instanceof Result)
							((Result)objectReturn).unpackFromTransport();
					if( DEBUG )
						System.out.println("FROM Remote, returned object from response:"+objectReturn+" remote Node:"+remoteNode+" slave:"+remotePort);
					if( objectReturn instanceof Exception ) {
						System.out.println("RemoteIteratorJsonClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)objectReturn).getCause());
						objectReturn = ((Throwable)objectReturn).getCause();
					}
					waitPayload.notify();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(this.getClass().getName()+": receive IO error "+e+" remote Node:"+remoteNode+" slave:"+remotePort);
			shutdown();
		}
		synchronized(waitHalt) {
			waitHalt.notifyAll();
		}

	}

	public void sendCommand() throws Exception {
		if(workerSocket == null) {
			synchronized(waitSocket) {
				waitSocket.wait();
			}
		}
		String jirf = JSONObject.toJson(this);
		if(DEBUG)
			System.out.println("Sending "+jirf+" to "+workerSocket);
		RelatrixJsonServer.writeLineBlocking(workerSocket, jirf, null);
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
			System.out.println("Calling close for RemoteIteratorJsonClientTransaction");
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
			System.out.println("Calling shutdown for RemoteIteratorJsonClientTransaction");
		if( workerSocket != null ) {
			try {
				workerSocket.close();
			} catch (IOException e) {}
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
		return String.format("%s RemoteNode:%s RemotePort:%d workerSocket:%s, session:%s method:%s return:%s%n",this.getClass().getName(), remoteNode, remotePort, workerSocket, session, methodName, objectReturn);
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

}
