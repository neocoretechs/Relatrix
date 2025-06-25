package com.neocoretechs.relatrix.server;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.*;

/**
* TCPServer is the superclass of all objects using ServerSockets.
*/
public abstract class TCPServer implements Cloneable, Runnable {
	protected ServerSocket server = null;
	Socket data = null;
	private int port;
	protected volatile boolean shouldStop = false;
	private CountDownLatch startLatch = new CountDownLatch(1);
	
	public synchronized InetAddress startServer(int port) throws IOException {
		if( this.server == null ) {
			this.port = port;
			System.out.println("Server "+this.getClass().getName()+" starting on "+InetAddress.getLocalHost().getHostName()+" port "+port);
			this.server = new ServerSocket(port);
			//runner = new Thread(this);
			//runner.start();
			ThreadPoolManager.init(new String[]{"TCPSERVER"}, false);
			ThreadPoolManager.getInstance().spin(this,"TCPSERVER");
			startLatch.countDown();
		}
		return server.getInetAddress();
	}
	
	public synchronized void startServer(int port, InetAddress binder) throws IOException {
		if( this.server == null ) {
			this.port = port;
            System.out.println("Server "+this.getClass().getName()+" starting on "+binder+" port "+port);
			this.server = new ServerSocket(port, 1000, binder);
			ThreadPoolManager.init(new String[]{"TCPSERVER"}, false);
			ThreadPoolManager.getInstance().spin(this,"TCPSERVER");
			startLatch.countDown();
		}
	}
	
	public synchronized InetAddress startServer(int port, String threadName) throws IOException {
		if( this.server == null ) {
			this.port = port;
			System.out.println("Server "+this.getClass().getName()+" starting on "+InetAddress.getLocalHost().getHostName()+" port "+port);
			this.server = new ServerSocket(port);
			ThreadPoolManager.init(new String[]{threadName}, false);
			ThreadPoolManager.getInstance().spin(this,threadName);
			startLatch.countDown();
		}
		return server.getInetAddress();
	}
	
	public synchronized void startServer(int port, InetAddress binder, String threadName) throws IOException {
		if( this.server == null ) {
			this.port = port;
            System.out.println("Server "+this.getClass().getName()+" starting on "+binder+" port "+port);
			this.server = new ServerSocket(port, 1000, binder);
			ThreadPoolManager.init(new String[]{threadName}, false);
			ThreadPoolManager.getInstance().spin(this,threadName);
			startLatch.countDown();
		}
	}
	
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
    public synchronized void startServer(InetAddress binder, String threadName) throws IOException {
		if( this.server == null ) {
          	//InetAddress binder = InetAddress.getByName(host);
          	this.server = new ServerSocket(0,0,binder);
  			this.port = this.server.getLocalPort();
  			System.out.println("Server "+this.getClass().getName()+" starting on "+binder.getHostName()+" port "+port);
			ThreadPoolManager.init(new String[]{threadName}, false);
			ThreadPoolManager.getInstance().spin(this,threadName);
			startLatch.countDown();
		}
    }

	public synchronized void stopServer() throws IOException {
		if( server != null ) {
			shouldStop = true;
			server.close();
			server = null;
			startLatch = new CountDownLatch(1);
			ThreadPoolManager.getInstance().shutdown("TCPSERVER");
		}
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

