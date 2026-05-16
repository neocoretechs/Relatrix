package com.neocoretechs.relatrix.client.json;

import java.io.IOException;

import java.lang.reflect.Constructor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;

import com.neocoretechs.relatrix.client.RemoteRequestInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.json.RelatrixJsonServer;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixJsonTransactionServer} 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the {@link RelatrixJsonTransactionServer}. the server thread uses to service the traffic
 * from this client.<p/>

 * In a transaction context, we must obtain a transaction Id from the server for the lifecycle of the transaction.<p/>
 * The transaction Id may outlive the session, as the session is transitory for communication purposes.
 * The {@link RelatrixJsonTransactionStatement} contains the transaction Id.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixJsonClientTransaction extends RelatrixJsonClientTransactionInterfaceImpl implements ClientTransactionInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private String bootNode, remoteNode;
	private int remotePort;

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	protected ConcurrentHashMap<String, RelatrixJsonTransactionStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixJsonTransactionStatement>();

	/**
	 * Start a Relatrix client to a remote server. . A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixJsonClientTransaction(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		IndexResolver.setRemoteTransaction(this);
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		SynchronizedThreadManager.getInstance().spin(this);
	}

	@Override
	public Object sendCommand(RelatrixTransactionStatementInterface rs) throws Exception {
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

	/**
	 * Called from the {@link RemoteIteratorTransaction} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param xid Transaction Id
	 * @param rii RelatrixTransactionStatement
	 * @return The next iterated object or null
	 */
	public Object next(RelatrixJsonTransactionStatement rii) throws Exception {
		rii.methodName = "next";
		rii.paramArray = new Object[0];
		return sendCommand(rii);
	}
	/**
	 * Called from the {@link RemoteIteratorTransaction} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param xid Transaction Id
	 * @param rii RelatrixTransactionStatement
	 * @return The boolean result of hasNext on server
	 */	
	public boolean hasNext(RelatrixJsonTransactionStatement rii) throws Exception {
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return (boolean) sendCommand(rii);
	}

	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	public void close(RelatrixJsonTransactionStatement rii) throws Exception {
		rii.methodName = "close";
		rii.paramArray = new Object[0];
		sendCommand(rii);
	}
	/**
	* Set up the socket to receive the queued response from TCPJsonTransactionWorker on server
	 */
	@Override
	public void run() {
  	    try {
  	    	while(shouldRun) {
  	    		if(DEBUG)
  	    			System.out.println(this.getClass().getName()+" "+workerSocket+" connected:"+workerSocket.isConnected());
  	    		String inLine = new String(RelatrixJsonServer.readUntil(workerSocket, (byte)'\n'));
  	    		if(DEBUG) {
  	    			System.out.println(this.getClass().getName()+" "+workerSocket+" raw data:"+inLine);
  	    		}
  	    		JSONObject jobj = new JSONObject(inLine);
  	    		RemoteResponseInterface iori = (RemoteResponseInterface) jobj.toObject();//,RelatrixTransactionStatement.class);
  	    		// get the original request from the stored table
  	    		if( DEBUG )
  	    			System.out.println("FROM Remote, response:"+iori+" remote Node:"+remoteNode+" slave:"+remotePort);
  	    		// unpack from TransportMorphism
  	    		Object o = iori.getObjectReturn();
  	    		// check for impedance mismatch in JSON return
  	    		// intercept remote stream, returned as a server side remote iterator and session info to communicate with remotely
  	    		// mostly, we just need the session and the fact its a type of stream with encapsulated iterator	
  	    		if( o instanceof Throwable ) {
  	    			System.out.println("RelatrixJsonClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
  	    			o = ((Throwable)o).getCause();
  	    		} else {
  		    		Class<?> returnClass = Class.forName(iori.getReturnClass());
  	    			if(returnClass != o.getClass()) {
  	    				if(DEBUG)
  	    					System.out.println(this.getClass().getName()+" class mismatch expected:"+returnClass+" got:"+o.getClass());
  	    				// one way to correct mismatch - provide ctor with type of returnClass designated by method call return type
  	    				// if exception was thrown, returnClass should be throwable
  	    				if(Throwable.class.isAssignableFrom(returnClass)) {
  	    					try {
  	    						Constructor co = returnClass.getConstructor(o.getClass());
  	    						o = co.newInstance(o);
  	    						throw new Exception((String)((Throwable)o).getMessage());
  	    					} catch(Exception oe) {
  	    						System.out.println("RelatrixJsonClientTransaction: ******** REMOTE EXCEPTION ******** "+oe);
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
  	    		RelatrixJsonTransactionStatement rs = (RelatrixJsonTransactionStatement) outstandingRequests.get(iori.getSession());
  	    		if( rs == null ) {
  	    			throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
  	    		} else {
  	    			if(DEBUG) {
  	    				System.out.printf("%s run response loop recieved class:%s %s%n", this.getClass().getName(),o.getClass().getName(),o);
  	    			}
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
  	    	if(!(e instanceof SocketException)) {
  	    		// we lost the remote master, try to close worker and wait for reconnect
  	    		e.printStackTrace();
  	    		System.out.println(this.getClass().getName()+": receive IO error "+e+" remote Node:"+remoteNode+" slave:"+remotePort);
  	    	}
  	    } finally {
  	    	shutdown();
  	    }
  	    synchronized(waitHalt) {
  	    	waitHalt.notifyAll();
  	    }
	}

	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	public void send(RemoteRequestInterface iori) throws Exception {
		outstandingRequests.put(iori.getSession(), (RelatrixJsonTransactionStatement) iori);
		String iorij = JSONObject.toJson(iori);
		RelatrixJsonServer.writeLineBlocking(workerSocket, iorij, null);
		if(DEBUG)
			System.out.println("Sent "+iorij+" to "+workerSocket);
	}

	
	@Override
	public String toString() {
		return super.toString();
	}
	static int i = 0;
	/**
	 * Generic call to server localaddr, remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixJsonClientTransaction rc = new RelatrixJsonClientTransaction(args[0],args[1],Integer.parseInt(args[2]));
		TransactionId xid = rc.getTransactionId();
		RelatrixJsonTransactionStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(xid,Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);				
				break;
			case 5:
				rs = new RelatrixJsonTransactionStatement(args[3],xid,args[4]);
				break;
			case 6:
				rs = new RelatrixJsonTransactionStatement(args[3],xid,args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixJsonTransactionStatement(args[3],xid,args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixJsonTransactionStatement(args[3],xid,args[4],args[5],args[6],args[7]);
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
