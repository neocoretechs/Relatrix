package com.neocoretechs.relatrix.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import java.io.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
* TCPServer is the superclass of all objects using ServerSockets.
*/
public abstract class TCPServer implements Cloneable, Runnable {
	private static boolean DEBUG;
	protected ServerSocketChannel server = null;
	SocketChannel data = null;
	private int port;
	protected volatile boolean shouldStop = false;
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
		if( server == null ) {
			this.port = port;
			server = ServerSocketChannel.open().bind(new InetSocketAddress(port));
			SynchronizedThreadManager.getInstance().init(new String[]{"TCPSERVER","WORKERS"}, false);
			SynchronizedThreadManager.getInstance().spin(this,"TCPSERVER");
			startLatch.countDown();
		}
		return server.getLocalAddress();
	}
	public synchronized SocketAddress startServer(int port, String threadName) throws IOException {
		if( server == null ) {
			this.port = port;
			server = ServerSocketChannel.open().bind(new InetSocketAddress(port));
			SynchronizedThreadManager.getInstance().init(new String[]{threadName}, false);
			SynchronizedThreadManager.getInstance().spin(this,threadName);
			startLatch.countDown();
		}
		return server.getLocalAddress();
	}
	public synchronized void startServer(int port, InetAddress binder) throws IOException {
		if( server == null ) {
			if( DEBUG )
				System.out.println("TCPServer attempt local bind "+binder+" port "+port);
			this.port = port;
			InetSocketAddress iSockAddr = new InetSocketAddress(binder,port);
			server = ServerSocketChannel.open().bind(iSockAddr);
			SynchronizedThreadManager.getInstance().init(new String[]{"TCPSERVER","WORKERS"}, false);
			SynchronizedThreadManager.getInstance().spin(this,"TCPSERVER");
			startLatch.countDown();
		}
	}
	public synchronized void startServer(int port, InetAddress binder, String threadName) throws IOException {
		if( server == null ) {
			if( DEBUG )
				System.out.println("TCPServer attempt local bind "+binder+" port "+port+" using thread:"+threadName);
			this.port = port;
			InetSocketAddress iSockAddr = new InetSocketAddress(binder,port);
			server = ServerSocketChannel.open().bind(iSockAddr);
			SynchronizedThreadManager.getInstance().init(new String[]{threadName}, false);
			SynchronizedThreadManager.getInstance().spin(this,threadName);
			startLatch.countDown();
		}
	}
	public synchronized void startServer(SocketAddress address) throws IOException {
		if( server == null ) {
			if( DEBUG )
				System.out.println("TCPServer attempt local bind "+address);
			server = ServerSocketChannel.open().bind(address);
			this.port = server.socket().getLocalPort();
			SynchronizedThreadManager.getInstance().init(new String[]{"TCPSERVER","WORKERS"}, false);
			SynchronizedThreadManager.getInstance().spin(this,"TCPSERVER");
			startLatch.countDown();
		}
	}
	public synchronized void startServer(SocketAddress address, String threadName) throws IOException {
		if( server == null ) {
			if( DEBUG )
				System.out.println("TCPServer attempt local bind "+address+" using thread "+threadName);
			server = ServerSocketChannel.open().bind(address);
			this.port = server.socket().getLocalPort();
			SynchronizedThreadManager.getInstance().init(new String[]{threadName}, false);
			SynchronizedThreadManager.getInstance().spin(this,threadName);
			startLatch.countDown();
		}
	}
	public synchronized void stopServer() throws IOException {
		if( server != null ) {
			shouldStop = true;
			server.close();
			server = null;
			SynchronizedThreadManager.getInstance().shutdown("TCPSERVER");
			SynchronizedThreadManager.getInstance().shutdown("WORKERS");
		}
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

    public void reInit() throws IOException {
         	if( data != null ) data.close();
    }
    
    public int getPort() {
    	return port;
    }
 
}	

