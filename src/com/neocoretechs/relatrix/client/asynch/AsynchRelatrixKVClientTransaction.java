package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.client.ConnectionHandler;

import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement;
import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatement;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.CircularBlockingDeque;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;


/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixKVTransactionServer} 
 * Worker threads located on a remote node. It carries the transaction identifier to maintain transaction context.
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the {@link RelatrixTransactionServer}. this client has a worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.<p/>
 *
 * In a transaction context, we must obtain a transaction Id from the server for the lifecycle of the transaction.<p/>
 * The transaction Id may outlive the session, as the session is transitory for communication purposes.
 * The {@link RelatrixTransactionStatement} contains the transaction Id.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class AsynchRelatrixKVClientTransaction extends AsynchRelatrixKVClientTransactionInterfaceImpl implements AsynchRelatrixKVClientTransactionInterface, ClientTransactionInterface,Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static final int REQUEST_QUEUE = 1024;
	
	protected CircularBlockingDeque<RelatrixKVTransactionStatementInterface> queuedRequests = new CircularBlockingDeque<RelatrixKVTransactionStatementInterface>(REQUEST_QUEUE);
	private String bootNode, remoteNode;
	private int remotePort;
	
	protected SocketChannel workerSocket = null; // socket assigned to slave port
	protected ConnectionHandler workerHandler;
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 

	public AsynchRelatrixKVClientTransaction() { }
	
	/**
	 * Start a Relatrix client to a remote server. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public AsynchRelatrixKVClientTransaction(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		IndexResolver.setRemoteTransaction((AsynchRelatrixClientTransactionInterface) this);
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
  	    try {
  	    	while(shouldRun ) {
  	    		RelatrixKVTransactionStatementInterface rs = queuedRequests.takeFirstNotify();
  	    		CompletableFuture<Object> cf = (CompletableFuture<Object>) rs.getCompletionObject();
  	    		workerHandler.sendObject(rs);
  	    		RemoteResponseInterface iori = (RemoteResponseInterface) workerHandler.readObject();
  	    		// get the original request from the stored table
  	    		if( DEBUG )
  	    			System.out.println("Asynch FROM Remote, response:"+iori+" remote Node:"+remoteNode+" slave:"+remotePort);
  	    		Object o = iori.getObjectReturn();
  	    		if( o instanceof Throwable ) {
  	    			System.out.println("AsynchRelatrixKVClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
  	    			o = ((Throwable)o).getCause();
  	    			cf.completeExceptionally((Throwable) o);
  	    		} else {
  	    			if(o instanceof Iterator)
  	    				((RemoteCompletionInterface)o).process();
  		    		cf.complete(o);
  	    		}
  	    		// We have the request after its session round trip, get it from outstanding waiters and signal
  	    		// set it with the response object
  	    		rs.setObjectReturn(o);
  	    		// and signal the latch we have finished
  	    		rs.signalCompletion(o);
  	    	}
		} catch(Exception e) {
			if(!(e instanceof SocketException) && !(e instanceof InterruptedException)) {
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
	 * Queue a command to the blocking deque. Its a circular deque, so once capacity is reach, oldest requests are overwritten
	 */
	//@Override
	public CompletableFuture<Object> queueCommand(RelatrixKVTransactionStatementInterface rs) {
		CompletableFuture<Object> cf = new CompletableFuture<>();
		rs.setCompletionObject(cf);
		try {
			queuedRequests.addLastWait(rs);
		} catch (InterruptedException e) {}
		return cf;
	}

	public void close() {
		shutdown();
		queuedRequests = null;
		Thread.currentThread().interrupt();
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}
	
	protected void shutdown() {
		if( workerHandler != null ) {
			workerHandler.close();
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
	public CompletableFuture<Object> next(RelatrixKVTransactionStatement rii) throws Exception {
		rii.setMethodName("next");
		rii.setParamArray(new Object[0]);
		return queueCommand(rii);
	}
	/**
	 * Called from the {@link RemoteIteratorTransaction} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param xid Transaction Id
	 * @param rii RelatrixTransactionStatement
	 * @return The boolean result of hasNext on server
	 */	
	public CompletableFuture<Object> hasNext(RelatrixKVTransactionStatement rii) throws Exception {
		rii.setMethodName("hasNext");
		rii.setParamArray(new Object[0]);
		return queueCommand(rii);
	}

	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	public void close(RelatrixKVTransactionStatement rii) throws Exception {
		rii.setMethodName("close");
		rii.setParamArray(new Object[0]);
		queueCommand(rii);
	}
	
	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d output socket%s%n",this.getClass().getName(), remoteNode, remotePort, workerSocket);
	}
	
	/**
	 * This method is for compatibility with remote Relation resolution through the RelatrixKV,
	 * which doesnt have the required to methods resolve a relation index, but we can jigger it 
	 * such that it can be done like this
	 */
	@Override
	public void storekv(TransactionId transactionId, Comparable index, Object instance) throws IOException {
		store(transactionId, index, instance);	
	}
	/**
	 * This method is for compatibility with remote Relation resolution through the RelatrixKV,
	 * which doesnt have the required to methods resolve a relation index, but we can jigger it 
	 * such that it can be done like this
	 */
	@Override
	public void storekv(Alias alias, TransactionId transactionId, Comparable instance, Object index) throws IOException {
		store(alias, transactionId, instance, index);
	}
	/**
	 * This method is for compatibility with remote Relation resolution through the RelatrixKV,
	 * which doesnt have the required to methods resolve a relation index, but we can jigger it 
	 * such that it can be done like this
	 */
	@Override
	public Object getByIndex(Alias alias, TransactionId transactionId, Comparable index) throws IOException {
		return getByIndex(alias, transactionId, index);
	}
	/**
	 * This method is for compatibility with remote Relation resolution through the RelatrixKV,
	 * which doesnt have the required to methods resolve a relation index, but we can jigger it 
	 * such that it can be done like this
	 */
	@Override
	public Object getByIndex(TransactionId transactionId, Comparable index) throws IOException {
		return getByIndex(transactionId, index);
	}

	static int i = 0;
	/**
	 * Generic call to server localaddr, remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args local node, remote server, remote server port, className for entrySet or (method, argument, argument, argument...) 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AsynchRelatrixKVClientTransaction rc = new AsynchRelatrixKVClientTransaction(args[0],args[1],Integer.parseInt(args[2]));
		TransactionId xid = rc.getTransactionId();
		RelatrixKVTransactionStatement rs = null;
		switch(args.length) {
			case 4:
				System.out.println("queueing..");
				CompletableFuture<Iterator> cit = rc.entrySet(xid,Class.forName(args[3]));
				long tim = System.nanoTime();
				Iterator<?> it = cit.get();
				System.out.println("Iterator return from future took:"+(System.nanoTime()-tim)+"ns.");
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);				
				break;
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
		System.out.println("queueing "+rs);
		CompletableFuture<?> cf = rc.queueCommand(rs);
		System.out.println("Command queued...");
		long tim = System.nanoTime();
		System.out.println("Return from future:"+cf.get()+" took:"+(System.nanoTime()-tim)+"ns.");
		rc.endTransaction(xid);
		rc.close();
	}


}
