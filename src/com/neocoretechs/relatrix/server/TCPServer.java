package com.neocoretechs.relatrix.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.ServerSocketChannel;

/**
* TCPServer is the superclass of all objects using ServerSockets.
*/
public abstract class TCPServer implements Cloneable, Runnable {
	private static boolean DEBUG = false;
	protected ServerSocketChannel server = null;
	private int port;
	protected volatile boolean shouldStop = false;
	protected volatile boolean isRunning = false;
	private CountDownLatch startLatch = new CountDownLatch(1);
		
	/**
     * Creates an {@link InetAddress} with both an IP and a host set so that no
     * further resolving will take place.
     * 
     * If an IP address string is specified, this method ensures that it will be
     * used in place of a host name.
     * 
     * If a host name other than {@code Address.LOCALHOST} is specified, this
     * method tries to find a non-loopback IP associated with the supplied host
     * name.
     * 
     * If the specified host name is {@code Address.LOCALHOST}, this method
     * returns a loopback address.
     * 
     * 
     * @param bind address to bind a new random port to
   	 * @param threadName a thread group name under which to spin the new thread for processing
     * @throws IOException if socket or thread fails
     */
	public synchronized SocketAddress startServer(int port) throws IOException {
		if( this.server == null ) {
			this.port = port;
			server = ServerSocketChannel.open();
			server.configureBlocking(true);
			server.bind(new InetSocketAddress(port));
			if(DEBUG)
				System.out.printf("%s bound %s%n",this.getClass().getName(),server);
		}
		if(!this.isRunning) {
			SynchronizedThreadManager.getInstance().init(new String[]{"TCPSERVER","WORKERS"}, false);
			SynchronizedThreadManager.getInstance().spin(this,"TCPSERVER");
			this.isRunning = true;
			startLatch.countDown();
			if(DEBUG)
				System.out.printf("%s starting %s%n",this.getClass().getName(),server);
		}
		return server.getLocalAddress();
	}
	public synchronized SocketAddress startServer(int port, String threadName) throws IOException {
		if( server == null ) {
			this.port = port;
			server = ServerSocketChannel.open().bind(new InetSocketAddress(port));
			if(DEBUG)
				System.out.printf("%s bound %s%n",this.getClass().getName(),server);
		}
		if(!this.isRunning) {
			SynchronizedThreadManager.getInstance().init(new String[]{threadName}, false);
			SynchronizedThreadManager.getInstance().spin(this,threadName);
			this.isRunning = true;
			startLatch.countDown();
			if(DEBUG)
				System.out.printf("%s starting %s%n",this.getClass().getName(),server);
		}
		return server.getLocalAddress();
	}
	public synchronized void startServer(int port, InetAddress binder) throws IOException {
		if( server == null ) {
			InetSocketAddress iSockAddr = new InetSocketAddress(binder,port);
			server = ServerSocketChannel.open();
			server.configureBlocking(true);
			server.bind(iSockAddr);
			if(DEBUG)
				System.out.printf("%s bound %s%n",this.getClass().getName(),server);
		}
		if(!this.isRunning) {
			SynchronizedThreadManager.getInstance().init(new String[]{"TCPSERVER","WORKERS"}, false);
			SynchronizedThreadManager.getInstance().spin(this,"TCPSERVER");
			this.isRunning = true;
			startLatch.countDown();
			if(DEBUG)
				System.out.printf("%s starting %s%n",this.getClass().getName(),server);
		}
	}
	public synchronized void startServer(int port, InetAddress binder, String threadName) throws IOException {
		if( server == null ) {
			this.port = port;
			InetSocketAddress iSockAddr = new InetSocketAddress(binder,port);
			server = ServerSocketChannel.open();
			server.configureBlocking(true);
			server.bind(iSockAddr);
			if(DEBUG)
				System.out.printf("%s bound %s with thread %s%n",this.getClass().getName(),server,threadName);
		}
		if(!this.isRunning) {
			SynchronizedThreadManager.getInstance().init(new String[]{threadName}, false);
			SynchronizedThreadManager.getInstance().spin(this,threadName);
			this.isRunning = true;
			startLatch.countDown();
			if(DEBUG)
				System.out.printf("%s starting %s%n",this.getClass().getName(),server);
		}
	}
	public synchronized void startServer(SocketAddress address) throws IOException {
		if( server == null ) {
			server = ServerSocketChannel.open();
			server.configureBlocking(true);
			server.bind(address);
			this.port = server.socket().getLocalPort();
			if(DEBUG)
				System.out.printf("%s bound %s%n",this.getClass().getName(),server);
		}
		if(!this.isRunning) {
			SynchronizedThreadManager.getInstance().init(new String[]{"TCPSERVER","WORKERS"}, false);
			SynchronizedThreadManager.getInstance().spin(this,"TCPSERVER");
			this.isRunning = true;
			startLatch.countDown();
			if(DEBUG)
				System.out.printf("%s starting %s%n",this.getClass().getName(),server);
		}
	}
	public synchronized void startServer(SocketAddress address, String threadName) throws IOException {
		if( server == null ) {
			server = ServerSocketChannel.open();
			server.configureBlocking(true);
			server.bind(address);
			this.port = server.socket().getLocalPort();
			if(DEBUG)
				System.out.printf("%s bound %s with thread %s%n",this.getClass().getName(),server,threadName);
		}
		if(!this.isRunning) {
			SynchronizedThreadManager.getInstance().init(new String[]{threadName}, false);
			SynchronizedThreadManager.getInstance().spin(this,threadName);
			this.isRunning = true;
			startLatch.countDown();
			if(DEBUG)
				System.out.printf("%s starting %s%n",this.getClass().getName(),server);
		}
	}
	public synchronized void stopServer() throws IOException {
		if( this.server != null && this.isRunning) {
			if(DEBUG)
				System.out.printf("%s stopping %s%n",this.getClass().getName(),server);
			shouldStop = true;
			server.close();
			server = null;
			SynchronizedThreadManager.getInstance().shutdown("TCPSERVER");
			SynchronizedThreadManager.getInstance().shutdown("WORKERS");
		}
		this.isRunning = false;
	}

	public void shutdown() throws IOException {
		stopServer();
	}
	public void awaitStart() throws InterruptedException {
		startLatch.await();
	}

	public boolean awaitStart(long timeout, TimeUnit unit) throws InterruptedException {
		return startLatch.await(timeout, unit);
	}

    public int getPort() {
    	return port;
    }
    
	@Override
	public String toString() {
		return String.format("%s channel=%s port=%s isRunning:%b%n",this.getClass().getName(),server,String.valueOf(port),isRunning);
	}
}	

