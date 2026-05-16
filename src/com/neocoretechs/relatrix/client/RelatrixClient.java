package com.neocoretechs.relatrix.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.RelatrixServer;


/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
 * that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.<p/>
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixClient extends RelatrixClientInterfaceImpl implements ClientInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static boolean SHOWDUPEKEYEXCEPTION = true;

	private String remoteNode;
	private int remotePort;

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	private ConnectionHandler workerHandler;

	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 

	protected ConcurrentHashMap<String, RelatrixStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixStatement>();

	/**
	 * Start a Relatrix client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode The remote Node
	 * @param remotePort The remote Port
	 * @throws IOException if connect fail
	 */
	public RelatrixClient(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		IndexResolver.setRemote((RelatrixClientInterface) this);
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

	/**
	 * Set up the socket 
	 */
	@Override
	public void run() {
		//SocketChannel sock;
		try {
			while(shouldRun) {
				RemoteResponseInterface iori = (RemoteResponseInterface) workerHandler.readObject();
				// get the original request from the stored table
				if( DEBUG )
					System.out.println("FROM Remote, response:"+iori+" remote Node:"+remoteNode+" slave:"+remotePort);
				Object o = iori.getObjectReturn();
				if( DEBUG )
					System.out.println("FROM Remote, returned object from response:"+o+" remote Node:"+remoteNode+" slave:"+remotePort);
				if( o instanceof Throwable ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					o = ((Throwable)o).getCause();
				}
				RelatrixStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					throw new RuntimeException("REQUEST/RESPONSE MISMATCH, statement:"+iori);
				} else {
					if(o instanceof Iterator)
						try {
							((RemoteCompletionInterface)o).process();
						} catch (Exception e) {
							e.printStackTrace();
							o = e;
						}
					// We have the request after its session round trip, get it from outstanding waiters and signal
					// set it with the response object
					rs.setObjectReturn(o);
					// and signal the latch we have finished
					rs.signalCompletion(null);
					if( DEBUG ) {
						System.out.println(this.getClass().getName()+" got connection "+workerHandler);
					}  
				}
			}
		} catch(Exception e) {
			// we lost the remote master, try to close worker and wait for reconnect
			e.printStackTrace();
			System.out.println(this.getClass().getName()+": receive IO error "+e+" remote Node:"+remoteNode+" slave:"+remotePort);
			//}
		} finally {
			shutdown();
		}
		synchronized(waitHalt) {
			waitHalt.notifyAll();
		}
	}

	/**
	 * Send request to remote worker
	 * @param iori
	 */
	public void send(RemoteRequestInterface iori) throws Exception {
		outstandingRequests.put(iori.getSession(), (RelatrixStatement) iori);
		workerHandler.sendObject(iori);
	}

	public Object sendCommand(RelatrixStatementInterface rs) throws Exception {
		IndexResolver.setRemote((RelatrixClientInterface) this);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCompletionObject(cdl);
		send(rs);
		cdl.await();
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof Exception)
			throw (Exception)o;
		return o;
	}
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The next iterated object or null
	 */
	public Object next(RelatrixStatement rii) throws Exception {
		rii.methodName = "next";
		rii.paramArray = new Object[0];
		return sendCommand(rii);
	}

	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The boolean result of hasNext on server
	 */	
	public boolean hasNext(RelatrixStatement rii) throws Exception {
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return (boolean) sendCommand(rii);
	}

	public void close() {
		shouldRun = false;

		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException ie) {}
		}
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}

	protected void shutdown() {
		if( workerHandler != null ) {
			workerHandler.close();
		}
		shouldRun = false;
	}

	public String getRemoteNode() {
		return remoteNode;
	}

	public int getRemotePort( ) {
		return remotePort;
	}

	public void closeDb(Class clazz) throws Exception {
		RelatrixStatement rs = new RelatrixStatement("close", clazz);
		sendCommand(rs);
	}

	public void closeDb(String alias, Class clazz) throws Exception {
		RelatrixStatement rs = new RelatrixStatement("close", alias, clazz);
		sendCommand(rs);
	}

	@Override
	public String toString() {
		return String.format("%s handler:%s%n",this.getClass().getName(),workerHandler);
	}

	static int i = 0;
	/**
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixClient rc = new RelatrixClient(args[1],Integer.parseInt(args[2]));
		RelatrixStatement rs = null;
		switch(args.length) {
		case 4:
			Iterator it = rc.entrySet(Class.forName(args[3]));
			it.forEachRemaining(e ->{	
				System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
			});
			System.exit(0);
		case 5:
			rs = new RelatrixStatement(args[3],args[4]);
			break;
		case 6:
			rs = new RelatrixStatement(args[3],args[4],args[5]);
			break;
		case 7:
			rs = new RelatrixStatement(args[3],args[4],args[5],args[6]);
			break;
		case 8:
			rs = new RelatrixStatement(args[3],args[4],args[5],args[6],args[7]);
			break;
		default:
			System.out.println("Cant process argument list of length:"+args.length);
			return;
		}
		System.out.println(rc.sendCommand(rs));
		//rc.send(rs);
		rc.close();
	}

}
