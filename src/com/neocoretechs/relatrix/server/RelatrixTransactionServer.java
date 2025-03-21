package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixTransaction;

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
 * Use this server for transaction context.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021, 2022, 2024
 *
 */
public class RelatrixTransactionServer extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	public static int WORKBOOTPORT = 9000; // Boot time portion of server that assigns databases to sockets etc
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	public static ServerInvokeMethod relatrixSubsetMethods = null; // FindSubset iterator methods
	public static ServerInvokeMethod relatrixHeadsetMethods = null; // FindHeadset iterator methods
	public static ServerInvokeMethod relatrixTailsetMethods = null; // FindTailset iterator methods
	public static ServerInvokeMethod relatrixSetMethods = null; // FindSet iterator methods
	public static ServerInvokeMethod relatrixEntrysetMethods = null; // Entryset iterator methods
	public static ServerInvokeMethod relatrixKeysetMethods = null; // Keyset iterator methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();

	
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixTransactionServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		RelatrixTransactionServer.relatrixSubsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixHeadsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixTailsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixSetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixEntrysetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixKeysetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction", 0);
		WORKBOOTPORT = port;
		startServer(WORKBOOTPORT);
	}
	
	/**
	 * Construct the server bound to stated address
	 * @param address
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public RelatrixTransactionServer(String address, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		RelatrixTransactionServer.relatrixSubsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixHeadsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixTailsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixSetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixEntrysetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction", 0);
		RelatrixTransactionServer.relatrixKeysetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction", 0);
		WORKBOOTPORT = port;
		startServer(WORKBOOTPORT,InetAddress.getByName(address));
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
					System.out.println("Relatrix Transaction Server command received:"+o);
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
					System.out.println("RelatrixTransactionServer starting new worker "+uworker+
							//( rdb != null ? "remote db:"+rdb : "" ) +
							" master port:"+o.getMasterPort());
				}

			} catch(Exception e) {
				System.out.println("Relatrix Transaction Server node configuration server socket accept exception "+e);
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
		RelatrixTransaction.getInstance();
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    RelatrixTransaction.setTablespace(db);
		    new RelatrixTransactionServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixTransactionServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixTransactionServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixTransactionServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}
	}

}
