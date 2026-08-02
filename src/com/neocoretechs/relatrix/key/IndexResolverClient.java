package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;

import com.neocoretechs.relatrix.parallel.CircularBlockingDeque;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
/**
 * Client for {@link IndexResolver} <p>
 * We dont have a resolver, we are the resolver. We dont deal with iterators, those are dealt with by higher level clients.
 * This client resolves key to indexes and vice versa. It is coded standalone as it functions at a lower level.
 * We want to avoid re-entrant deadlocks etc.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public class IndexResolverClient extends IndexResolverClientInterfaceImpl implements Runnable {
	private static final long serialVersionUID = 8377607548947015076L;
	private static boolean DEBUG = false;
	public String remoteNode;
	public int remotePort;
	private UUID session = UUID.randomUUID();

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	protected ConnectionHandler workerHandler;
	protected HandlerClassLoader classLoader;
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object();
	public static final int REQUEST_QUEUE = 1024;
	
	protected CircularBlockingDeque<RelatrixStatementInterface> queuedRequests = new CircularBlockingDeque<RelatrixStatementInterface>(REQUEST_QUEUE);
	
	public IndexResolverClient(String remoteNode, int remotePort)  throws IOException {
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		workerSocket = SocketChannel.open(new InetSocketAddress(remoteNode, remotePort));
		classLoader = new HandlerClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		workerHandler = new ConnectionHandler(workerSocket, classLoader);
		SynchronizedThreadManager.getInstance().spin(this);
	}
	
	@Override
	public UUID getSession() {
		return session;
	}
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
  	    			System.out.println(this.getClass().getName()+" ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
  	    			o = ((Throwable)o).getCause();
  	    		}
  	    		// We have the request after its session round trip, get it from outstanding waiters and signal
  	    		// set it with the response object
  	    		rs.setObjectReturn(o);
  	    		// and signal the latch we have finished
	    		if( DEBUG )
  	    			System.out.printf("%s %s signal completion%n",this.getClass().getName(),this);
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
	public void queueCommand(RelatrixStatementInterface rs) {
		rs.setCompletionObject();
		try {
			queuedRequests.addLastWait(rs);
		} catch (InterruptedException e) {}
	}
	
	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		if(DEBUG)
			System.out.printf("%s.sendCommand statement=%s%n", this.getClass().getName(), s);
		queueCommand(s);
		return s.getCompletionFuture().orTimeout(30, TimeUnit.SECONDS).get();
	}

	@Override
	public String getRemoteNode() {
		return remoteNode;
	}
	@Override
	public int getRemotePort() {
		return remotePort;
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
		return String.format("%s RemoteNode:%s RemotePort:%d output socket%s this:%x%n",this.getClass().getName(), remoteNode, remotePort, workerSocket, System.identityHashCode(this));
	}
}
