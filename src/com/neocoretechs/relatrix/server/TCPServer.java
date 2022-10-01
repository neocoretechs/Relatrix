package com.neocoretechs.relatrix.server;
import java.net.*;
import java.io.*;

/**
* TCPServer is the superclass of all objects using ServerSockets.
*/
public abstract class TCPServer implements Cloneable, Runnable {
	ServerSocket server = null;
	Socket data = null;
	volatile boolean shouldStop = false;
	public synchronized void startServer(int port) throws IOException {
		if( server == null ) {
			server = new ServerSocket(port);
			//runner = new Thread(this);
			//runner.start();
			ThreadPoolManager.init(new String[]{"TCPSERVER"}, false);
			ThreadPoolManager.getInstance().spin(this,"TCPSERVER");
		}
	}
	public synchronized void startServer(int port, InetAddress binder) throws IOException {
		if( server == null ) {
            System.out.println("TCPServer attempt local bind "+binder+" port "+port);
			server = new ServerSocket(port, 1000, binder);
			//runner = new Thread(this);
			//runner.start();
			ThreadPoolManager.getInstance().spin(this,"TCPSERVER");
		}
	}
	public synchronized void stopServer() throws IOException {
		if( server != null ) {
			shouldStop = true;
			server.close();
			server = null;
			ThreadPoolManager.getInstance().shutdown("TCPSERVER");
		}
	}

    public void reInit() throws IOException {
         	if( data != null ) data.close();
    }
}	

