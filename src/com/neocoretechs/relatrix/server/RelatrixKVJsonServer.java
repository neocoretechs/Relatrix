package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.neocoretechs.relatrix.RelatrixKV;

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
public final class RelatrixKVJsonServer extends RelatrixKVServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	public static int WORKBOOTPORT = 9001; // Boot time portion of server that assigns databases to sockets etc
	Jsonb jsonb = JsonbBuilder.create();
	byte[] buf = new byte[4096];

	//
	// in server, we are using local repository for handlerclassloader, but only one
	// and that one will be located on port 9999
	boolean isThisBytecodeRepository = false;

	private ConcurrentHashMap<String, TCPJsonWorker> dbToWorker = new ConcurrentHashMap<String, TCPJsonWorker>();
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVJsonServer(int port) throws IOException, ClassNotFoundException {
		super(port);
	}
	
	public RelatrixKVJsonServer(String address, int port) throws IOException, ClassNotFoundException {
		super(address, port);
	}
	
	@Override
	public void run() {
			while(!shouldStop) {
				try {
					Socket datasocket = server.accept();
                    // disable Nagles algoritm; do not combine small packets into larger ones
                    datasocket.setTcpNoDelay(true);
                    // wait 1 second before close; close blocks for 1 sec. and data can be sent
                    datasocket.setSoLinger(true, 1);
					//
                    InputStream ois = datasocket.getInputStream();                
                    CommandPacketInterface o = jsonb.fromJson(ois, CommandPacket.class);
                    if( DEBUG | DEBUGCOMMAND )
                    	System.out.println("Relatrix K/V JsonServer command received:"+o);
                    // if we get a command packet with no statement, assume it to start a new instance
                   
                    TCPJsonWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
                    if( uworker != null ) {
                    	if(o.getTransport().equals("TCP")) {
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    	}
                    }
                    // Create the worker, it in turn creates a WorkerRequestProcessor
                    uworker = new TCPJsonWorker(datasocket, o.getRemoteMaster(), o.getMasterPort());
                    dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
                    ThreadPoolManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println("RelatrixKVJsonServer starting new worker "+uworker+
                    			//( rdb != null ? "remote db:"+rdb : "" ) +
                    			" master port:"+o.getMasterPort());
                    }
                    
				} catch(Exception e) {
                    System.out.println("Relatrix K/V Json Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	}
	/**
	 * Load the methods of main RelatrixKV class as remotely invokable then we instantiate RelatrixKVServer.<p/>
	 * @param args If length 1, then default port 9001, must specify tablespace or alias subsequently. Same for 2 arg: host, port. 3 args then arg 0 is to set default tablespace
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixKV.getInstance();
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    RelatrixKV.setTablespace(db);
		    new RelatrixKVJsonServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixKVJsonServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixKVJsonServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVJsonServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}
 
	}
	

}
