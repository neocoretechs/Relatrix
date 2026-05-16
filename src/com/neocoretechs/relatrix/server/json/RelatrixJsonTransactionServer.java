package com.neocoretechs.relatrix.server.json;

import java.io.File;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;

import java.nio.channels.SocketChannel;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

import com.neocoretechs.relatrix.server.remoteiterator.json.RemoteIteratorJsonTransactionServer;

/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.<p>
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it 
 * is for non-serializable iterators, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence and can be called willy nilly.<br>
 * Functionally this class Extends TCPServer, receives CommandPacketinterface,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<p>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * Use this server for transaction context.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021, 2022, 2024
 *
 */
public class RelatrixJsonTransactionServer extends RelatrixTransactionServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	private ConcurrentHashMap<String, TCPJsonTransactionWorker> dbToWorker = new ConcurrentHashMap<String, TCPJsonTransactionWorker>();
	
	public static String[] iteratorServers = new String[]{
			"com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction",
			"com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction",
			"com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction",
			"com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction",
			"com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction",				
			"com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction"
	};				
	public static int[] iteratorPorts = new int[] {
			9080,9081,9082,9083,9084,9085
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
	public RelatrixJsonTransactionServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteIteratorJsonTransactionServer(iteratorServers[i], ((InetSocketAddress)RelatrixJsonTransactionServer.address).getAddress(), iteratorPorts[i]);
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the server bound to stated address
	 * @param address
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public RelatrixJsonTransactionServer(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		address = new InetSocketAddress(iaddress, port);
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteIteratorJsonTransactionServer(iteratorServers[i], ((InetSocketAddress)RelatrixJsonTransactionServer.address).getAddress(), iteratorPorts[i]);
		startServer(address);
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public void run() {
		while(!shouldStop) {
			try {
				SocketChannel datasocket = server.accept();
				datasocket.configureBlocking(true);
                // disable Nagles algoritm; do not combine small packets into larger ones
	            datasocket.setOption(StandardSocketOptions.TCP_NODELAY, true);
	            // wait 1 second before close; close blocks for 1 sec. and data can be sent
	            datasocket.setOption(StandardSocketOptions.SO_LINGER, 1);
				datasocket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				datasocket.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
				datasocket.setOption(StandardSocketOptions.SO_SNDBUF, 32767);	
				//
				// if we get a command packet with no statement, assume it to start a new instance

				TCPJsonTransactionWorker uworker = dbToWorker.get(datasocket.getRemoteAddress().toString());
				if( uworker != null ) {
						if( uworker.shouldRun )
							uworker.stopWorker();
				}              
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPJsonTransactionWorker(datasocket);
				dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
				SynchronizedThreadManager.getInstance().spin(uworker);
				if( DEBUG ) {
					System.out.println(this.getClass().getName()+" started new worker "+uworker);
				}

			} catch(Exception e) {
				System.out.println(this.getClass().getName()+" node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	/**
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixServer.<p/>
	 * @param args If length 1, then default port 9004, else parent path of directory descriptor in arg 0 and file name part as database.
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixTransaction.getInstance();
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    RelatrixTransaction.setTablespace(db);
		    new RelatrixJsonTransactionServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixJsonTransactionServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixJsonTransactionServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixJsonTransactionServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}
	}

}
