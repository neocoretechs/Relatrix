package com.neocoretechs.relatrix.client.json;

import java.io.IOException;

import java.lang.reflect.Constructor;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import com.neocoretechs.relatrix.server.json.RelatrixJsonServer;

/**
 * This class functions as client to the {@link RelatrixJsonServer} Worker threads located on a remote node.
 * a worker thread that handles traffic back from the server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixJsonClient extends RelatrixJsonClientInterfaceImpl implements ClientNonTransactionInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	private boolean SHOWDUPEKEYEXCEPTION = true;
	
	private String bootNode, remoteNode;
	private int remotePort;
	
	protected SocketChannel workerSocket = null; // socket assigned to slave port
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	protected ConcurrentHashMap<String, RelatrixJsonStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixJsonStatement>();

	/**
	 * Start a Relatrix client to a remote server. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode Name of local master socket to coonect back to
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixJsonClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		IndexResolver.setRemote(this);
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		SynchronizedThreadManager.getInstance().spin(this);
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
	public Object next(RelatrixJsonStatement rii) throws Exception {
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
	public boolean hasNext(RelatrixJsonStatement rii) throws Exception {
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return (boolean) sendCommand(rii);
	}
	
	public void close() {
		shutdown();
		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException ie) {}
		}
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}
	
	protected void shutdown() {
		if( workerSocket != null ) {
			try {
				workerSocket.close();
			} catch (IOException e) {}
		}
		shouldRun = false;
	}
	
	
	public String getLocalNode() {
		return bootNode;
	}
	
	public String getRemoteNode() {
		return remoteNode;
	}
	
	public int getRemotePort( ) {
		return remotePort;
	}

	
	public void closeDb(Class clazz) throws Exception {
		RelatrixJsonStatement rs = new RelatrixJsonStatement("close", clazz);
		sendCommand(rs);
	}
	
	public void closeDb(String alias, Class clazz) throws Exception {
		RelatrixJsonStatement rs = new RelatrixJsonStatement("close", alias, clazz);
		sendCommand(rs);
	}
	
	/**
	* Set up the socket 
	*/
	@Override
	public void run() {
  	    try {
		  while(shouldRun ) {
				if(DEBUG)
					System.out.println(this.getClass().getName()+" connected:"+workerSocket.isConnected());
				String s = new String(RelatrixJsonServer.readUntil(workerSocket, (byte)'\n'));
				JSONObject jobj = new JSONObject(s);
				RemoteResponseInterface iori = (RemoteResponseInterface) jobj.toObject();//, RemoteResponseInterface.class);
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" remote Node:"+remoteNode+" slave:"+remotePort);
				Object o = iori.getObjectReturn();
				if( DEBUG )
					 System.out.println("FROM Remote, returned object from response:"+o+" remote Node:"+remoteNode+" slave:"+remotePort);
				if( o instanceof Throwable ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixJsonClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					 o = ((Throwable)o).getCause();
				} else {
		    		Class<?> returnClass = Class.forName(iori.getReturnClass());
  	    			if(returnClass != o.getClass()) {
 	    				// if exception was thrown, returnClass should be throwable
  	    				if(Throwable.class.isAssignableFrom(returnClass)) {
  	    					try {
  	    						Constructor co = returnClass.getConstructor(o.getClass());
  	    						o = co.newInstance(o);
  	    						throw new Exception((String)((Throwable)o).getMessage());
  	    					} catch(Exception oe) {
  	    						System.out.println("RelatrixJsonClient: ******** REMOTE EXCEPTION ******** "+oe);
  	    						o = oe;
  	    					}
  	    				} else {
  	    					// class mismatch of non Throwable variety, we my have a hashmap of values
  	    					if(o instanceof HashMap) {
  	    						JSONObject jo = (JSONObject) JSONObject.wrap(o);
  	    						o = jo.toObject(returnClass);
  	    					}
  	    				}
  	    			}
  	    		}
				RelatrixStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
				} else {
					if(o instanceof Iterator)
						((RemoteCompletionInterface)o).process();
					// We have the request after its session round trip, get it from outstanding waiters and signal
					// set it with the response object
					rs.setObjectReturn(o);
					// and signal the latch we have finished
					rs.signalCompletion(o);
				}
		  }
		} catch(Exception e) {
			//if(!(e instanceof SocketException)) {
				// we lost the remote master, try to close worker and wait for reconnect
				e.printStackTrace();
				System.out.println(this.getClass().getName()+": receive IO error "+e+" remoteNode:"+remoteNode+" slave:"+remotePort);
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
		outstandingRequests.put(iori.getSession(), (RelatrixJsonStatement) iori);
		String iorij = JSONObject.toJson(iori);
		RelatrixJsonServer.writeLineBlocking(workerSocket, iorij, null);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	static int i = 0;
	/**
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixJsonClient rc = new RelatrixJsonClient(args[0],args[1],Integer.parseInt(args[2]));
		RelatrixStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);
			case 5:
				rs = new RelatrixJsonStatement(args[3],args[4]);
				break;
			case 6:
				rs = new RelatrixJsonStatement(args[3],args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixJsonStatement(args[3],args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixJsonStatement(args[3],args[4],args[5],args[6],args[7]);
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
