package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;

import java.util.Map;
import java.util.UUID;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;
import com.neocoretechs.relatrix.parallel.CircularBlockingDeque;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixServer} 
 * Worker threads located on a remote node. 
 * this client has a master worker thread that handles traffic back from the server.
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class AsynchRelatrixClient extends AsynchRelatrixClientInterfaceImpl implements AsynchRelatrixClientInterface, ClientNonTransactionInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static final int REQUEST_QUEUE = 1024;
	
	protected CircularBlockingDeque<RelatrixStatementInterface> queuedRequests = new CircularBlockingDeque<RelatrixStatementInterface>(REQUEST_QUEUE);
	private String remoteNode;
	private int remotePort;
	private HandlerClassLoader classLoader;
	private UUID session = UUID.randomUUID();

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	protected ConnectionHandler workerHandler;
	
	protected RemoteIteratorClient iteratorClient;

	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 

	public AsynchRelatrixClient() { }
	
	/**
	 * Start a Relatrix client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public AsynchRelatrixClient(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		// send message to spin connection
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		classLoader = new HandlerClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		workerHandler = new ConnectionHandler(workerSocket, classLoader);
		if(DEBUG)
			System.out.printf("%s Channel created to %s%n",this.getClass().getName(),workerHandler);	
		SynchronizedThreadManager.getInstance().spin(this);
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
	public int getRemotePort( ) {
		return remotePort;
	}
	/**
	 * Set the RemoteIteratorClient
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
	* Set up the socket. In the case of an iterator, if the iteratorClient is set to an existing client via setIterator from the main client,
	* then set the existing client comm handler to that saved client. Otherwise call the 'process()' method of {@link RemoteCompletionInterface}
	* which will spin a new connection to the remote iterator server, causing a new server handler thread to be created. To minimize the number
	* of sockets and threads in large loop queries, call the setIterator() method of the client with the previous iterator obtained via findSet
	* immediately before the call to findSet()
	*/
	@Override
	public void run() {
  	    try {
  	    	while(shouldRun ) {
  	    		RelatrixStatementInterface rs = queuedRequests.takeFirstNotify();
  	    		workerHandler.sendObject(rs);
  	    		RemoteResponseInterface iori = (RemoteResponseInterface) workerHandler.readObject();
  	    		// get the original request from the stored table
  	    		if( DEBUG )
  	    			System.out.printf("%s Asynch FROM Remote, response:%s remote:%s port:%d%n",this.getClass().getName(),iori,remoteNode,remotePort);
  	    		Object o = iori.getObjectReturn();
  	    		if( o instanceof Throwable ) {
  	    			System.out.println(this.getClass().getName()+" ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
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
  	    		rs.setObjectReturn(o);
  	    		rs.signalCompletion(o);
  	    		if(DEBUG)
  	    			System.out.printf("%s ASYNC got cf=%x for rs=%x after complete, oClass=%s%n",this.getClass().getName(),System.identityHashCode(rs.getCompletionFuture()), System.identityHashCode(rs), o == null ? "null" : o.getClass().getName());
  	    	}
		} catch(Throwable e) {
			if(!(e instanceof SocketException) && !(e instanceof InterruptedException)) {
				// we lost the remote master, try to close worker and wait for reconnect
				e.printStackTrace();
  	    		System.out.printf("%s Asynch exception:%s remote:%s slave:%s%n",this.getClass().getName(),e,remoteNode,String.valueOf(remotePort));
			}
		} finally {
			shutdown();
  	    }
  	    synchronized(waitHalt) {
  	    	waitHalt.notifyAll();
  	    }
	}
	/**
	 * Queue a RelatrixStatementInterface command to the blocking deque. block at max entries. Create completion object
	 * of CompletableFuture, set in RelatrixStatementInterface
	 * @param rs The RelatrixStatementInterface to receive CompletableFuture in CompletionObject
	*/ 
	@Override
	public CompletableFuture<Object> queueCommand(RelatrixStatementInterface rs) {
		CompletableFuture<Object> cf = null;
		rs.setCompletionObject();
		try {
			queuedRequests.addLastWait(rs);
			cf = rs.getCompletionFuture();
		} catch (InterruptedException e) {}
		if(DEBUG)
			System.out.printf("%s ENQUEUE rs=%x cf=%x thread=%s%n",this.getClass().getName(),System.identityHashCode(rs), System.identityHashCode(cf), Thread.currentThread().getName());
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
	
	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d output socket%s%n",this.getClass().getName(), remoteNode, remotePort, workerSocket);
	}

	static int i = 0;
	/**
	 * Generic call to server remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args  remote server, remote server port, className for entrySet or (method, argument, argument, argument...) 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AsynchRelatrixClient rc = new AsynchRelatrixClient(args[0],Integer.parseInt(args[1]));
		RelatrixStatement rs = null;
		UUID session = rc.getSession();
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
				rs = new RelatrixStatement(session,args[2], args[3]);
				break;
			case 6:
				rs = new RelatrixStatement(session,args[2],args[3], args[4]);
				break;
			case 7:
				rs = new RelatrixStatement(session,args[2],args[3],args[4], args[5]);
				break;
			case 8:
				rs = new RelatrixStatement(session,args[2],args[3],args[4],args[5], args[6]);
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
