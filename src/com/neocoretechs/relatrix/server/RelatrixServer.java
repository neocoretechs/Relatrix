package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.Relatrix;

/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.<p/>
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it 
 * is for non-serializable iterators, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p/>
 * Static methods need no server side object in residence and can be called willy nilly.<br/>
 * Functionally this class Extends TCPServer, receives CommandPacketinterface,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<p/>
 * WorkerRequestProcessor takes requests and processes them.<br/>
 * On the client and server the following are present as conventions:<br/>
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT.<br/>
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it.<br/>
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it.<br/>
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it.<br/>
 * The client is going to connect and tell the server the master and slave ports that it will be using to process requests.<br/>
 * In this way multiple databases can be used by instantiating separate clients.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021
 *
 */
public final class RelatrixServer extends TCPServer {
	private static boolean DEBUG = true;
	private static boolean DEBUGCOMMAND = false;
	public static int WORKBOOTPORT = 9000; // Boot time portion of server that assigns databases to sockets etc
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	public static ServerInvokeMethod relatrixSubsetMethods = null; // Subset iterator methods
	public static ServerInvokeMethod relatrixHeadsetMethods = null; // Headset iterator methods
	public static ServerInvokeMethod relatrixTailsetMethods = null; // Standard Tailset iterator methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();
	
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.Relatrix", 0);
		RelatrixServer.relatrixSubsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator", 0);
		RelatrixServer.relatrixHeadsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator", 0);
		RelatrixServer.relatrixTailsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixIterator", 0);
		WORKBOOTPORT = port;
		startServer(WORKBOOTPORT);
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
                    if( DEBUGCOMMAND )
                    	System.out.println("Relatrix Server command received:"+o);
                    // if we get a command packet with no statement, assume it to start a new instance
                   
                    TCPWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
                    if( uworker != null ) {
                    	if(o.getTransport().equals("TCP")) {
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    	}
                    }                   
                    // Create the worker, it in turn creates a WorkerRequestProcessor
                    uworker = new TCPWorker(datasocket, o.getRemoteMaster(), o.getMasterPort());
                    dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
                    ThreadPoolManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println("RelatrixServer starting new worker "+uworker+
                    			//( rdb != null ? "remote db:"+rdb : "" ) +
                    			" master port:"+o.getMasterPort());
                    }
                    
				} catch(Exception e) {
                    System.out.println("Relatrix Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	}
	/**
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixServer.<p/>
	 * @param args If length 1, then default port 9000, else parent path of directory descriptor in arg 0 and file name part as database.
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		if(args.length > 0) {
			WORKBOOTPORT = Integer.parseInt(args[1]);
		} else {
			System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixServer /path/to/database/databasename <port>");
		}
        String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
        		(new File(args[0]).getName());
        System.out.println("Bringing up database:"+db+" on port "+WORKBOOTPORT);
        Relatrix.setTablespaceDirectory(db);
        // if we get a command packet with no statement, assume it to start a new instance
		new RelatrixServer(WORKBOOTPORT);
		System.out.println("Relatrix Server started on "+InetAddress.getLocalHost().getHostName()+" port "+WORKBOOTPORT);
	}

}
