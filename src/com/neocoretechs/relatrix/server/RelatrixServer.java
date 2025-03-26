package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteIteratorServer;


/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of {@link ServerInvokeMethod} and populating that at construction of this class.<p/>
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
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021, 2024
 *
 */
public class RelatrixServer extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static InetAddress address;
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();
	
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	public static String[] iteratorServers = new String[]{
	 "com.neocoretechs.relatrix.iterator.RelatrixIterator",
	 "com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator",
	 "com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator",
	 "com.neocoretechs.relatrix.iterator.RelatrixTailsetIterator",
	 "com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator",				
	 "com.neocoretechs.relatrix.iterator.RelatrixKeysetIterator"
	};				
	public static int[] iteratorPorts = new int[] {
			9090,9091,9092,9093,9094,9095
	};
	public static int findIteratorServerPort(String clazz) {
		return iteratorPorts[Arrays.asList(iteratorServers).indexOf(clazz)];
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.Relatrix", 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteIteratorServer(iteratorServers[i], address, iteratorPorts[i]);
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param address IP address
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixServer(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.Relatrix", 0);
		address = InetAddress.getByName(iaddress);
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteIteratorServer(iteratorServers[i], address, iteratorPorts[i]);
		startServer(port,address);
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
	 * @param args If length 1, then default port 9000
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		Relatrix.getInstance();
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    Relatrix.setTablespace(db);
		    new RelatrixServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}
	}
}
