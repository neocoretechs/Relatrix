package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.bigsack.io.ThreadPoolManager;
import com.neocoretechs.relatrix.Relatrix;


/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked
 * to an object instance on the server, as it is for non-serializable iterators, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.
 * Static methods need no server side object in residence and can be called willy nilly.
 * Functionally this class Extends TCPServer, receives CommandPacketinterface.
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.
 * WorkerRequestProcessor takes requests and processes them.
 * On the client and server the following are present as conventions:
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it
 * @author jg Copyright (C) NeoCoreTechs 2015
 *
 */
public final class RelatrixServer extends TCPServer {
	private static boolean DEBUG = true;
	public static final int WORKBOOTPORT = 9000; // Boot time portion of server that assigns databases to sockets etc
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	public static ServerInvokeMethod relatrixSubsetMethods = null; // Subset iterator methods
	public static ServerInvokeMethod relatrixHeadsetMethods = null; // Headset iterator methods
	public static ServerInvokeMethod relatrixTailsetMethods = null; // Standard Tailset iterator methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();
	
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public RelatrixServer() throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.Relatrix", 0);
		RelatrixServer.relatrixSubsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator", 0);
		RelatrixServer.relatrixHeadsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator", 0);
		RelatrixServer.relatrixTailsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixIterator", 0);
		startServer(WORKBOOTPORT);
	}

	/**
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixServer
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		new RelatrixServer();
		System.out.println("Relatrix Server started on "+InetAddress.getLocalHost().getHostName()+" port "+WORKBOOTPORT);
	}
	
	public void run() {
			while(!shouldStop) {
				try {
					Socket datasocket = server.accept();
                    // disable Nagles algoritm; do not combine small packets into larger ones
                    datasocket.setTcpNoDelay(true);
                    // wait 1 second before close; close blocks for 1 sec. and data can be sent
                    datasocket.setSoLinger(true, 1);
					//
                    ObjectInputStream ois = new ObjectInputStream(datasocket.getInputStream());
                    CommandPacketInterface o = (CommandPacketInterface) ois.readObject();
                    if( DEBUG )
                    	System.out.println("Relatrix Server command received:"+o);
    
                    String db = o.getDatabase();
   
                    db = (new File(db)).toPath().getParent().toString() + File.separator +
                    		(new File(o.getDatabase()).getName());
                    // if we get a command packet with no statement, assume it to start a new instance
                   
                    TCPWorker uworker = dbToWorker.get(db);
                    if( uworker != null ) {
                    	if(o.getTransport().equals("TCP")) {
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    	}
                    }
                    // determine if this worker has started, if so, cancel thread and start a new one.
                    Relatrix.setTablespaceDirectory(db);
                    
                    // set the remote tablespace directory
                    String rdb = o.getRemoteDirectory();
                    if( rdb != null ) {
                    	rdb = (new File(rdb)).toPath().getParent().toString() + File.separator +
                    		(new File(o.getRemoteDirectory()).getName());
                    	String sdb = rdb.replace('\\', '/');
                    	Relatrix.setRemoteDirectory(sdb);
                    }
                    
                    // Create the worker, it in turn creates a WorkerRequestProcessor
                    uworker = new TCPWorker(db, o.getRemoteMaster(), Integer.valueOf(o.getMasterPort()), Integer.valueOf(o.getSlavePort()));
                    dbToWorker.put(db, uworker); 
                    ThreadPoolManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println("RelatrixServer starting new worker db:"+db+
                    			( rdb != null ? "remote db:"+rdb : "" ) +
                    			" master port:"+o.getMasterPort()+" slave port:"+o.getSlavePort());
                    }
                    
				} catch(Exception e) {
                    System.out.println("Relatrix Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	
	}
	

}
