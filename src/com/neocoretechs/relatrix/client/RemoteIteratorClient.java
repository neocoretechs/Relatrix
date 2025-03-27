package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.UUID;

import java.util.concurrent.CountDownLatch;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.ThreadPoolManager;
/**
 * Manages remote iterators via client that is serialized to remote iterator servers and returned as payload.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorClient implements Runnable, RelatrixStatementInterface, Serializable, Iterator {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	public static final boolean LOCALTEST = false; // use localhost as remote node
	public static final boolean TEST = true; // timing
	private long tim;
	
	private String remoteNode;
	private int remotePort;
	
	protected int MASTERPORT = 9876; // master port, accepts connection from remote server
	protected int SLAVEPORT = 9877; // slave port, conects to remote, sends outbound requests to master port of remote
	
	protected transient InetAddress IPAddress = null; // remote server address
	private transient InetAddress localIPAddress = null; // local server address

	protected transient Socket workerSocket = null; // socket assigned to slave port
	protected transient ServerSocket masterSocket; // master socket connected back to via server
	protected transient Socket sock; // socket of mastersocket
	//private SocketAddress masterSocketAddress; // address of master
	
	private volatile boolean shouldRun = true; // master service thread control
	private transient Object waitHalt;
	private transient Object waitPayload;
	private transient Object waitSocket;
	private transient CountDownLatch countDownLatch = null;
	
	private String session;
	private Object objectReturn;
	
	private String methodName;
	private Object[] paramArray = new Object[0];
	private Class<?>[] params = new Class<?>[0];
	private String returnClass;
	
	private transient RemoteIteratorClient returnPayload;

	/**
	 * Start a client to a remote server. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode Name of local master socket to coonect back to
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RemoteIteratorClient(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		session = UUID.randomUUID().toString();
	}
	
	public RemoteIteratorClient() {}
	
	/**
	 * When we deserialize this from the server as a result of remote method call, we get back the serialized
	 * object with remote server info. Here, we want to do the actual connection to remote.
	 */
	@Override
	public void process() throws Exception {
		waitHalt = new Object();
		waitPayload = new Object();
		waitSocket = new Object();
		if( LOCALTEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			IPAddress = InetAddress.getByName(remoteNode);
		}
		if( DEBUG ) {
			System.out.println("RemoteIteratorClient constructed with remote:"+IPAddress);
		}
		//localIPAddress = InetAddress.getByName(bootNode);
		localIPAddress = InetAddress.getLocalHost();
		//
		// Wait for master server node to connect back to here for return channel communication
		//
		//masterSocketAddress = new InetSocketAddress(MASTERPORT);
		masterSocket = new ServerSocket(0, 1000, localIPAddress);
		MASTERPORT = masterSocket.getLocalPort();
		SLAVEPORT = remotePort;
		// send message to spin connection
		workerSocket = Fopen(localIPAddress.getHostName());
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		//SocketChannel sock;
		try {
			sock = masterSocket.accept();
			sock.setKeepAlive(true);
			//sock.setTcpNoDelay(true);
			sock.setSendBufferSize(32767);
			sock.setReceiveBufferSize(32767);
			// At this point we have a connection back from 'slave'
		} catch (IOException e1) {
			System.out.println("RemoteIteratorClient server socket accept failed with "+e1);
			shutdown();
			return;
		}
		if( DEBUG ) {
			System.out.println("RemoteIteratorClient got connection "+sock);
		}
		ThreadPoolManager.getInstance().spin(this);
	}
	
	@Override
	public void run() {
		synchronized(waitSocket) {
			waitSocket.notify();
		}
		try {
			while(shouldRun) {
				InputStream ins = sock.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(ins);
				returnPayload = (RemoteIteratorClient) ois.readObject();
				synchronized(waitPayload) {
					objectReturn = returnPayload.getObjectReturn();
					if(objectReturn == TransportMorphism.class)
						objectReturn = TransportMorphism.createMorphism((TransportMorphism) objectReturn);
					else
						if(objectReturn instanceof Result)
							((Result)objectReturn).unpackFromTransport();
					if( DEBUG )
						System.out.println("FROM Remote, returned object from response:"+objectReturn+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
					if( objectReturn instanceof Exception ) {
						System.out.println("RemoteIteratorClient: ******** REMOTE EXCEPTION ******** "+((Throwable)objectReturn).getCause());
						objectReturn = ((Throwable)objectReturn).getCause();
					}
					synchronized(waitPayload) {
						waitPayload.notify();
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(this.getClass().getName()+": receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
			shutdown();
		}
		synchronized(waitHalt) {
			waitHalt.notifyAll();
		}

	}

	public void sendCommand() throws Exception {
		if(sock == null) {
			synchronized(waitSocket) {
				waitSocket.wait();
			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
		oos.writeObject(this);
		oos.flush();
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
		try {
			synchronized(waitPayload) {
				waitPayload.wait();
			}
		} catch (InterruptedException e) {}
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
		synchronized(waitPayload) {
			try {
				waitPayload.wait();
			} catch (InterruptedException e) {}
		}
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
			} catch (InterruptedException ie) {}
		}
		shutdown();
	}

	private void shutdown() {
		if(DEBUG)
			System.out.println("Calling shutdown for RemoteIteratorClient");
		if(sock != null) {
			try {
				sock.close();
			} catch (IOException e) {}
			sock = null;
		}
		if( workerSocket != null ) {
			try {
				workerSocket.close();
			} catch (IOException e2) {}
			workerSocket = null;
		}
		if( masterSocket != null ) {
			try {
				masterSocket.close();
			} catch (IOException e2) {}
			masterSocket = null;
		}
		ThreadPoolManager.getInstance().shutdown(); // client threads
	}


	public String getRemoteNode() {
		return remoteNode;
	}

	public int getRemotePort( ) {
		return remotePort;
	}


	/**
	 * Open a socket to the remote worker located at IPAddress and SLAVEPORT using {@link CommandPacket} bootNode and MASTERPORT
	 * @param bootNode local MASTER node name to connect back to
	 * @return Opened socket
	 * @throws IOException
	 */
	public Socket Fopen(String bootNode) throws IOException {
		Socket s = new Socket(IPAddress, SLAVEPORT);
		s.setKeepAlive(true);
		s.setReceiveBufferSize(32767);
		s.setSendBufferSize(32767);
		System.out.println("Socket created to "+s);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		CommandPacketInterface cpi = new CommandPacket(bootNode, MASTERPORT);
		os.writeObject(cpi);
		os.flush();
		return s;
	}

	@Override
	public String toString() {
		return String.format("RemoteIteratorClient BootNode:%s RemoteNode:%s RemotePort:%d workerSocket out socket:%s, in socket:%s session:%s method:%s return:%s%n",localIPAddress, remoteNode, remotePort, workerSocket, sock, session, methodName, objectReturn);
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
	public CountDownLatch getCountDownLatch() {
		return null;
	}

	@Override
	public void setCountDownLatch(CountDownLatch cdl) {
	}


	@Override
	public void setObjectReturn(Object o) {
		objectReturn = o;
	}

}
