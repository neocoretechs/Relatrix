package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.bigsack.io.ThreadPoolManager;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.client.RemoteEntrySetIterator;
import com.neocoretechs.relatrix.client.RemoteEntrySetStream;
import com.neocoretechs.relatrix.client.RemoteHeadMapIterator;
import com.neocoretechs.relatrix.client.RemoteHeadMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteHeadMapKVStream;
import com.neocoretechs.relatrix.client.RemoteHeadMapStream;
import com.neocoretechs.relatrix.client.RemoteKeySetIterator;
import com.neocoretechs.relatrix.client.RemoteKeySetStream;
import com.neocoretechs.relatrix.client.RemoteSubMapIterator;
import com.neocoretechs.relatrix.client.RemoteSubMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteSubMapKVStream;
import com.neocoretechs.relatrix.client.RemoteSubMapStream;
import com.neocoretechs.relatrix.client.RemoteTailMapIterator;
import com.neocoretechs.relatrix.client.RemoteTailMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteTailMapKVStream;
import com.neocoretechs.relatrix.client.RemoteTailMapStream;


/**
 * Key/Value Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it is 
 * for non-serializable iterators and streams, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p/>
 * Static methods need no server side object in residence and can be called willy nilly.
 * Functionally, this class Extends TCPServer, receives CommandPacketinterface,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<br/>
 * WorkerRequestProcessor takes requests and processes them.<br/>
 * On the client and server the following are present as conventions:<br/>
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT.<br/>
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it.<br/>
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it.<br/>
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it.<br/>
 * The client is going to connect and tell the server the master and slave ports that it will be using to process requests.<br/>
 * In this way multiple databases can be used by instantiating separate clients.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2021
 *
 */
public final class RelatrixKVServer extends TCPServer {
	private static boolean DEBUG = true;
	private static boolean DEBUGCOMMAND = false;
	public static int WORKBOOTPORT = 9000; // Boot time portion of server that assigns databases to sockets etc
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	public static ServerInvokeMethod relatrixSubmapMethods = null; // Submap iterator methods
	public static ServerInvokeMethod relatrixSubmapKVMethods = null; // submap K/V methods
	public static ServerInvokeMethod relatrixHeadmapMethods = null; // Headmap iterator methods
	public static ServerInvokeMethod relatrixHeadmapKVMethods = null; // Headmap iterator methods
	public static ServerInvokeMethod relatrixTailmapMethods = null; // Standard Tailmap iterator methods
	public static ServerInvokeMethod relatrixTailmapKVMethods = null;// Tailmap KV methods
	public static ServerInvokeMethod relatrixEntrysetMethods = null;// EntrySet KV methods
	public static ServerInvokeMethod relatrixKeysetMethods = null; // Keyset KV methods
	//
	public static ServerInvokeMethod relatrixSubmapStreamMethods = null; // Submap stream methods
	public static ServerInvokeMethod relatrixSubmapKVStreamMethods = null; // submap K/V stream methods
	public static ServerInvokeMethod relatrixHeadmapStreamMethods = null; // Headmap stream methods
	public static ServerInvokeMethod relatrixHeadmapKVStreamMethods = null; // Headmap stream methods
	public static ServerInvokeMethod relatrixTailmapStreamMethods = null; // Standard Tailmap stream methods
	public static ServerInvokeMethod relatrixTailmapKVStreamMethods = null;// Tailmap KV methods
	public static ServerInvokeMethod relatrixEntrysetStreamMethods = null;// EntrySet KV methods
	public static ServerInvokeMethod relatrixKeysetStreamMethods = null; // Keyset KV methods
	// in server, we are using local repository for handlerclassloader, but only one
	// and that one will be located on port 9999
	boolean isThisBytecodeRepository = false;
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();

	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKV", 0);
		RelatrixKVServer.relatrixSubmapMethods = new ServerInvokeMethod(RemoteSubMapIterator.className, 0);
		RelatrixKVServer.relatrixSubmapKVMethods = new ServerInvokeMethod(RemoteSubMapKVIterator.className, 0);
		RelatrixKVServer.relatrixHeadmapMethods = new ServerInvokeMethod(RemoteHeadMapIterator.className, 0);
		RelatrixKVServer.relatrixHeadmapKVMethods = new ServerInvokeMethod(RemoteHeadMapKVIterator.className, 0);
		RelatrixKVServer.relatrixTailmapMethods = new ServerInvokeMethod(RemoteTailMapIterator.className, 0);
		RelatrixKVServer.relatrixTailmapKVMethods = new ServerInvokeMethod(RemoteTailMapKVIterator.className, 0);
		RelatrixKVServer.relatrixEntrysetMethods = new ServerInvokeMethod(RemoteEntrySetIterator.className, 0);
		RelatrixKVServer.relatrixKeysetMethods = new ServerInvokeMethod(RemoteKeySetIterator.className, 0);
		//
		RelatrixKVServer.relatrixSubmapStreamMethods = new ServerInvokeMethod(RemoteSubMapStream.className, 0);
		RelatrixKVServer.relatrixSubmapKVStreamMethods = new ServerInvokeMethod(RemoteSubMapKVStream.className, 0);
		RelatrixKVServer.relatrixHeadmapStreamMethods = new ServerInvokeMethod(RemoteHeadMapStream.className, 0);
		RelatrixKVServer.relatrixHeadmapKVStreamMethods = new ServerInvokeMethod(RemoteHeadMapKVStream.className, 0);
		RelatrixKVServer.relatrixTailmapStreamMethods = new ServerInvokeMethod(RemoteTailMapStream.className, 0);
		RelatrixKVServer.relatrixTailmapKVStreamMethods = new ServerInvokeMethod(RemoteTailMapKVStream.className, 0);
		RelatrixKVServer.relatrixEntrysetStreamMethods = new ServerInvokeMethod(RemoteEntrySetStream.className, 0);
		RelatrixKVServer.relatrixKeysetStreamMethods = new ServerInvokeMethod(RemoteKeySetStream.className, 0);
		WORKBOOTPORT = port;
		startServer(WORKBOOTPORT);
		if(port == 9999) {
			isThisBytecodeRepository = true;
			System.out.println("NOTE: This server now Serving bytecode, port "+port+" is reserved for bytecode repository!");
			try {
				HandlerClassLoader.connectToLocalRepository(null); // use default path
			} catch (IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}
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
                    	System.out.println("Relatrix K/V Server command received:"+o);
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
                    	System.out.println("RelatrixKVServer starting new worker "+uworker+
                    			//( rdb != null ? "remote db:"+rdb : "" ) +
                    			" master port:"+o.getMasterPort());
                    }
                    
				} catch(Exception e) {
                    System.out.println("Relatrix K/V Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	}
	/**
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixKVServer.<p/>
	 * The path to the database must conform to that of the the tablespace layout created by {@code com.neocoretechs.bigsack.CreateDatabaseVolume}<p/> 
	 * @param args If length 1, then default port 9000, else parent path of directory descriptor in arg 0 and file name part as database.
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		if(args.length > 0) {
			WORKBOOTPORT = Integer.parseInt(args[1]);
		} else {
			System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVServer /path/to/database/databasename <port>");
		}
        String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
        		(new File(args[0]).getName());
        System.out.println("Bringing up database:"+db+" on port "+WORKBOOTPORT);
        RelatrixKV.setTablespaceDirectory(db);
        // if we get a command packet with no statement, assume it to start a new instance
		new RelatrixKVServer(WORKBOOTPORT);
		System.out.println("Relatrix K/V Server started on "+InetAddress.getLocalHost().getHostName()+" port "+WORKBOOTPORT);
	}
	

}
