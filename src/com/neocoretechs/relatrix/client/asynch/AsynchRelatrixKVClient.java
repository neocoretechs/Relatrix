package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import java.util.stream.Stream;

import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixKVStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;

import com.neocoretechs.relatrix.parallel.CircularBlockingDeque;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.RelatrixServer;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixKVServer} 
 * Worker threads located on a remote node. 
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the {@link RelatrixServer}. This client has a worker thread that handles traffic back from the server.
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class AsynchRelatrixKVClient extends AsynchRelatrixKVClientInterfaceImpl implements AsynchRelatrixKVClientInterface, ClientInterface, Runnable {
	private static final boolean DEBUG = true;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static final int REQUEST_QUEUE = 1024;
	
	protected CircularBlockingDeque<RelatrixStatementInterface> queuedRequests = new CircularBlockingDeque<RelatrixStatementInterface>(REQUEST_QUEUE);
	private String remoteNode;
	private int remotePort;
	private HandlerClassLoader classLoader;

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	protected ConnectionHandler workerHandler;
	
	protected RemoteIteratorClient iteratorClient;

	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object();
	private UUID session = UUID.randomUUID();

	public AsynchRelatrixKVClient() { }
	
	/**
	 * Start a Relatrix K/V client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public AsynchRelatrixKVClient(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		classLoader = new HandlerClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		workerHandler = new ConnectionHandler(workerSocket, classLoader);
		if(DEBUG)
			System.out.println("Channel created to "+workerHandler);
		SynchronizedThreadManager.getInstance().spin(this);
	}
	/**
	 * Set the RemoteIteratorClient.
	 * The purpose of this is to prevent a new connection to a remote server inside a loop. The existing
	 * client will be re-used rather than creating a new client connection to the remote iterator server.
	 * This is critical for large queries that contain nested queries as exhaustion of remote connections
	 * can occur otherwise.
	 * @param client
	 */
	public void setIterator(Iterator<?> client) {
		this.iteratorClient = (RemoteIteratorClient)client;
	}
	/**
	 * Get the RemoteIteratorClient
	 * @return
	 */
	public Iterator<?> getIterator() {
		return this.iteratorClient;
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
  	    		Object cf = rs.getCompletionObject();
	    		if( DEBUG )
  	    			System.out.printf("%s %s send using %s completion object=%s%n",this.getClass().getName(),this,workerHandler,cf);
  	    		workerHandler.sendObject(rs);
  	    		RemoteResponseInterface iori = (RemoteResponseInterface) workerHandler.readObject();
  	    		if( DEBUG )
  	    			System.out.printf("%s %s got response %s%n",this.getClass().getName(),this,iori);
  	    		// get the original request from the stored table
  	    		Object o = iori.getObjectReturn();
  	    		if( o instanceof Throwable ) {
  	    			System.out.println(this.getClass().getName()+" ******** REMOTE EXCEPTION ******** "+o);
  	    			o = ((Throwable)o).getCause();
  	    		} else {
	    			if(o instanceof Iterator || o instanceof Stream) {
  	    				if(iteratorClient != null) {
  	    					((RemoteCompletionInterface)o).setClient(iteratorClient);
  	    					iteratorClient = null;
  	    				} else {
  	    					((RemoteCompletionInterface)o).process();
  	    				}
  	    			}
  	    		}
  	    		// We have the request after its session round trip, get it from outstanding waiters and signal
  	    		// set it with the response object
  	    		//rs.setObjectReturn(o);
  	    		// and signal the latch we have finished
	    		if( DEBUG )
  	    			System.out.printf("%s %s signal completion %s%n",this.getClass().getName(),this,o);
  	    		// get the original request from the stored table
  	    		rs.signalCompletion(o);
  	    	}
		} catch(Throwable e) {
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
		CompletableFuture<Object> cf = null;
		rs.setCompletionObject();
		try {
			queuedRequests.addLastWait(rs);
			cf = rs.getCompletionFuture();
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

	public UUID getSession() {
		return session;
	}


	
	static int i = 0;
	/**
	 * Generic call to server remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args remote server, remote server port, className for entrySet or (method, argument, argument, argument...) 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AsynchRelatrixKVClient rc = new AsynchRelatrixKVClient(args[0],Integer.parseInt(args[1]));
		RelatrixKVStatement rs = null;
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
				rs = new RelatrixKVStatement(null,args[2], args[3]);
				break;
			case 6:
				rs = new RelatrixKVStatement(null,args[2],args[3], args[4]);
				break;
			case 7:
				rs = new RelatrixKVStatement(null,args[2],args[3],args[4], args[5]);
				break;
			case 8:
				rs = new RelatrixKVStatement(null,args[2],args[3],args[4],args[5], args[6]);
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
