package com.neocoretechs.relatrix.client;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixKVTransactionServer}
 * Worker threads located on a remote node.<p/>
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is on the RelatrixKVTransactionServer that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 */
public class RelatrixKVClientTransaction extends RelatrixKVClientTransactionInterfaceImpl implements ClientInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private String remoteNode;
	private int remotePort;
	
	protected SocketChannel workerSocket = null; // socket assigned to slave port
	protected ConnectionHandler workerHandler;

	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	protected ConcurrentHashMap<String, RelatrixKVStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixKVStatement>();

	/**
	 * Start a Relatrix client to a remote server.  A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixKVClientTransaction(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		try {
			workerHandler = new ConnectionHandler(workerSocket);
			System.out.println("Channel created to "+workerHandler);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(DEBUG)
			System.out.printf("%s about to connect socket to masterSocketAddress:%s%n", this.getClass().getName(), workerSocket.toString());
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		SynchronizedThreadManager.getInstance().spin(this);
	}
	
	public String getRemoteNode() {
		return remoteNode;
	}
	
	public int getRemotePort( ) {
		return remotePort;
	}
	/**
	* Set up the socket 
	 */
	@Override
	public void run() {
  	    try {
		  while(shouldRun ) {
				RemoteResponseInterface iori = (RemoteResponseInterface) workerHandler.readObject();
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" remote Node:"+remoteNode+" slave:"+remotePort);
				Object o = iori.getObjectReturn();
				if( o instanceof Throwable ) {
					System.out.println("RelatrixKVClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					o = ((Throwable)o).getCause();
				}
				RelatrixKVStatement rs = outstandingRequests.get(iori.getSession());
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
		  if(DEBUG)
			  System.out.printf("%s Exiting run loop shouldRun:%b%n", this.getClass().getName(),shouldRun);
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
	 * Send request to remote worker via workerSocket
	 * @param iori
	 */
	public void send(RemoteRequestInterface iori) throws Exception {
		outstandingRequests.put(iori.getSession(), (RelatrixKVStatement) iori);
		workerHandler.sendObject(iori);
	}
	
	public void close() {
		if(DEBUG) {
			System.out.println(this.getClass().getName()+" remote Node:"+remoteNode+" slave:"+remotePort);
		}
		shouldRun = false;
		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException ie) {}
		}
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}
	
	protected void shutdown() {
		if(DEBUG) {
			System.out.println(this.getClass().getName()+" shutdown remote Node:"+remoteNode+" slave:"+remotePort);
		}
		if( workerHandler != null ) {
			workerHandler.close();
		}
		shouldRun = false;
	}
	
	/**
	 * Call the remote server method to send a manually constructed command
	 * @param rs The RelatrixKvStatement manually constructed
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	@Override
	public Object sendCommand(RelatrixStatementInterface rs) throws Exception {
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
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return Object of iteration, depends on iterator being used, typically, Map.Entry derived serializable instance of next element
	 * @throws Exception 
	 */
	public Object next(TransactionId xid, RelatrixKVTransactionStatement rii)  throws Exception {
		rii.xid = xid;
		rii.methodName = "next";
		rii.paramArray = new Object[0];
		return sendCommand(rii);
	}
	
	public boolean hasNext(TransactionId xid, RelatrixKVTransactionStatement rii) throws Exception {
		rii.xid = xid;
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return (boolean) sendCommand(rii);
	}
	
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 * @throws Exception 
	 */
	public void close(TransactionId xid, RelatrixKVTransactionStatement rii) throws Exception {
		rii.xid = xid;
		rii.methodName = "close";
		rii.paramArray = new Object[] {xid};
		sendCommand(rii);
	}
	
	@Override
	public String toString() {
		return String.format("%s handler:%s%n",this.getClass().getName(),workerHandler);
	}
	
	static int i = 0;
	/**
	 * case 4:
	 * <dd>Generic call to server: localaddr, remote addr, port, class
	 * <dd>Displays entry set stream of class from database running on addr and port
	 * <dd>case 5-8:
	 * <dd>Call to server method: localaddr, remote addr, port, server_method <arg1> <arg2> ... 
	 * <dd>Invokes named method on the server at host and port using the given string arguments.<p/>
	 * Note that method must accept the number of string arguments provided, such as loadClassFromJar <jar>
	 * and loadClassFromPath <package> <path> and removePackageFromRepository <package>.<p/>
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixKVTransactionStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		i = 0;
		RelatrixKVClientTransaction rc = new RelatrixKVClientTransaction(args[1],Integer.parseInt(args[2]));
		TransactionId xid = null;
		switch(args.length) {
			case 4:
				/*
				Stream stream = rc.entrySetStream(xid, Class.forName(args[3]));
				stream.forEach(e ->{	
					System.out.println(++i+"="+((Map.Entry) (e)).getKey()+" / "+((Map.Entry) (e)).getValue());
				});
				*/
				xid = rc.getTransactionId();
				Iterator it = rc.entrySet(xid,Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				rc.endTransaction(xid);
				System.exit(0);
			case 5:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4]);
				break;
			case 6:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		rc.sendCommand(rs);
		rc.endTransaction(xid);
		rc.close();
	}

}
