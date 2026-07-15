package com.neocoretechs.relatrix.client.asynch.json;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.neocoretechs.relatrix.RelatrixKVJson;

import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.client.json.ConnectionHandlerJson;
import com.neocoretechs.relatrix.client.json.RelatrixKVStatementJson;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.CircularBlockingDeque;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.rocksack.Alias;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixKVServerJson} 
 * Worker threads located on a remote node. 
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the {@link RelatrixServer}. This client has a worker thread that handles traffic back from the server.
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class AsynchRelatrixKVClientJson extends AsynchRelatrixKVClientInterfaceJsonImpl implements AsynchRelatrixKVClientInterfaceJson, ClientNonTransactionInterface, Runnable {
	private static final boolean DEBUG = true;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static final int REQUEST_QUEUE = 1024;
	
	protected CircularBlockingDeque<RelatrixStatementInterface> queuedRequests = new CircularBlockingDeque<RelatrixStatementInterface>(REQUEST_QUEUE);
	private String remoteNode;
	private int remotePort;

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	protected ConnectionHandlerJson workerHandler;
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 

	public AsynchRelatrixKVClientJson() { }
	
	/**
	 * Start a Relatrix K/V client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public AsynchRelatrixKVClientJson(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		workerHandler = new ConnectionHandlerJson(workerSocket);
		if(DEBUG)
			System.out.println(this.getClass().getName()+" Channel created to "+workerHandler);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		IndexResolver indexResolver = new IndexResolver();
		indexResolver.setRemote(this);
		ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());
		SynchronizedThreadManager.getInstance().spinWithContext(this, pec);
	}

	/**
	* Set up the socket 
	 */
	@Override
	public void run() {
  	    try {
  	    	while(shouldRun ) {
  	    		if( DEBUG )
  	    			System.out.printf("%s wait for queue in %s%n",this.getClass().getName(),this);
  	    		RelatrixStatementInterface rs = queuedRequests.takeFirstNotify();
  	    		if( DEBUG )
  	    			System.out.printf("%s %s queue take %s%n",this.getClass().getName(),this,rs);
  	    		CompletableFuture<Object> cf = (CompletableFuture<Object>) rs.getCompletionObject();
	    		if( DEBUG )
  	    			System.out.printf("%s %s send using %s%n",this.getClass().getName(),this,workerHandler);
  	    		workerHandler.sendObject(rs);
  	    		if( DEBUG )
  	    			System.out.printf("%s %s awaiting response from %s%n",this.getClass().getName(),this,workerHandler);
  	    		RemoteResponseInterface iori = (RemoteResponseInterface) workerHandler.readObject();
  	    		if( DEBUG )
  	    			System.out.printf("%s %s got response %s%n",this.getClass().getName(),this,iori);
  	    		// get the original request from the stored table
  	    		Object o = iori.getObjectReturn();
  	    		if( o instanceof Throwable ) {
  	    			System.out.println(this.getClass().getName()+" ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
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
	    		if( DEBUG )
  	    			System.out.printf("%s %s signal completion %s%n",this.getClass().getName(),this,o);
  	    		// get the original request from the stored table
  	    		rs.signalCompletion(o);
  	    	}
		} catch(Exception e) {
			if(!(e instanceof SocketException) && !(e instanceof InterruptedException)) {
				// we lost the remote master, try to close worker and wait for reconnect
				e.printStackTrace();
				System.out.println(this.getClass().getName()+": receive IO error remote Node:"+remoteNode+" slave:"+remotePort);
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
	public CompletableFuture<Object> queueCommand(RelatrixStatementInterface rs) {
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
	
	public String getRemoteNode() {
		return remoteNode;
	}
	
	public int getRemotePort( ) {
		return remotePort;
	}

	/**
	 * Called from the {@link RemoteIterator} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixKVStatementJson
	 * @return The next iterated object or null
	 */
	public CompletableFuture<Object> next(RelatrixStatementInterface rii) throws Exception {
		rii.setMethodName("next");
		rii.setParamArray(new Object[0]);
		return queueCommand(rii);
	}
	/**
	 * Called from the {@link RemoteIteratorTransaction} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixTransactionStatement
	 * @return The boolean result of hasNext on server
	 */	
	public CompletableFuture<Object> hasNext(RelatrixStatementInterface rii) throws Exception {
		rii.setMethodName("hasNext");
		rii.setParamArray(new Object[0]);
		return queueCommand(rii);
	}
	@Override
	public Object remove(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("remove", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object remove(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("remove", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	public void close(RelatrixStatementInterface rii) throws Exception {
		rii.setMethodName("close");
		rii.setParamArray(new Object[0]);
		queueCommand(rii);
	}
	
	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d output socket:%s handler:%s queue:%d%n",this.getClass().getName(), remoteNode, remotePort, workerSocket, workerHandler, queuedRequests.length());
	}
	/**
	 * This method is for compatibility with Relation types were resolution of index is necessary so we can conform to the 
	 * NonTransactionClient interface on the back end.
	 */
	@Override
	public void storekv(Comparable index, Object instance) {
		store(index, instance);
	}
	/**
	 * This method is for compatibility with Relation types were resolution of index is necessary so we can conform to the 
	 * NonTransactionClient interface on the back end.
	 */
	@Override
	public void storekv(Alias alias, Comparable index, Object instance) {
		store(alias, index, instance);
	}
	/**
	 * This method is for compatibility with Relation types were resolution of index is necessary so we can conform to the 
	 * NonTransactionClient interface on the back end.
	 */
	@Override
	public Object getByIndex(DBKey index) {
		return get(index);
	}
	/**
	 * This method is for compatibility with Relation types were resolution of index is necessary so we can conform to the 
	 * NonTransactionClient interface on the back end.
	 */
	@Override
	public Object getByIndex(Alias alias, DBKey index) {
		return get(alias, index);
	}
	
	static int i = 0;
	/**
	 * Generic call to server remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args remote server, remote server port, className for entrySet or (method, argument, argument, argument...) 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AsynchRelatrixKVClientJson rc = new AsynchRelatrixKVClientJson(args[0],Integer.parseInt(args[1]));
		RelatrixKVStatementJson rs = null;
		switch(args.length) {
			case 4:
				System.out.println("queueing..");
				CompletableFuture<Iterator> cit = rc.entrySet(Class.forName(args[2]));
				long tim = System.nanoTime();
				Iterator<?> it = cit.get();
				System.out.println("Iterator return from future took:"+(System.nanoTime()-tim)+"ns.");
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);				
				break;
			case 5:
				rs = new RelatrixKVStatementJson(args[2],args[3]);
				break;
			case 6:
				rs = new RelatrixKVStatementJson(args[2],args[3],args[4]);
				break;
			case 7:
				rs = new RelatrixKVStatementJson(args[2],args[3],args[4],args[5]);
				break;
			case 8:
				rs = new RelatrixKVStatementJson(args[2],args[3],args[4],args[5],args[6]);
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
		rc.close();
	}


}
