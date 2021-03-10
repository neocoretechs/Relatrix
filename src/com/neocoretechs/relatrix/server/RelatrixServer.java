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
import com.neocoretechs.relatrix.client.RemoteHeadsetIterator;
import com.neocoretechs.relatrix.client.RemoteHeadsetStream;
import com.neocoretechs.relatrix.client.RemoteSubsetIterator;
import com.neocoretechs.relatrix.client.RemoteSubsetStream;
import com.neocoretechs.relatrix.client.RemoteTailsetIterator;
import com.neocoretechs.relatrix.client.RemoteTailsetStream;


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
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it.
 * The client is going to connect and tell the server the master and slave ports that it will be using to process requests.
 * In this way multiple databases can be used by instantiating separate clients.
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
	public static ServerInvokeMethod relatrixSubstreamMethods = null; // Subset stream methods
	public static ServerInvokeMethod relatrixHeadstreamMethods = null; // Headset stream methods
	public static ServerInvokeMethod relatrixTailstreamMethods = null; // Standard Tailset stream methods
	
	
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
		RelatrixServer.relatrixSubsetMethods = new ServerInvokeMethod(RemoteSubsetIterator.className, 0);
		RelatrixServer.relatrixHeadsetMethods = new ServerInvokeMethod(RemoteHeadsetIterator.className, 0);
		RelatrixServer.relatrixTailsetMethods = new ServerInvokeMethod(RemoteTailsetIterator.className, 0);
		RelatrixServer.relatrixSubstreamMethods = new ServerInvokeMethod(RemoteSubsetStream.className, 0);
		RelatrixServer.relatrixHeadstreamMethods = new ServerInvokeMethod(RemoteHeadsetStream.className, 0);
		RelatrixServer.relatrixTailstreamMethods = new ServerInvokeMethod(RemoteTailsetStream.className, 0);
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
                   // db = (new File(db)).toPath().getParent().toString() + File.separator +
                    //		(new File(o.getDatabase()).getName());
                    // if we get a command packet with no statement, assume it to start a new instance
                   
                    TCPWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
                    if( uworker != null ) {
                    	if(o.getTransport().equals("TCP")) {
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    	}
                    }
                    // determine if this worker has started, if so, cancel thread and start a new one.
                    //Relatrix.setTablespaceDirectory(db);
                    
                    // set the remote tablespace directory
                    //String rdb = o.getRemoteDirectory();
                    //if( rdb != null ) {
                    //	rdb = (new File(rdb)).toPath().getParent().toString() + File.separator +
                    //		(new File(o.getRemoteDirectory()).getName());
                    //	String sdb = rdb.replace('\\', '/');
                    //	Relatrix.setRemoteDirectory(sdb);
                    //}
                    
                    // Create the worker, it in turn creates a WorkerRequestProcessor
                    uworker = new TCPWorker(datasocket, o.getRemoteMaster(), o.getMasterPort());
                    dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
                    ThreadPoolManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println("RelatrixServer starting new worker"+uworker+
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
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixServer
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		if(args.length > 0) {
			WORKBOOTPORT = Integer.parseInt(args[1]);
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
