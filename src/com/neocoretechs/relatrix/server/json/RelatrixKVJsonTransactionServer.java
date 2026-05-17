package com.neocoretechs.relatrix.server.json;

import java.io.File;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;

import java.nio.channels.SocketChannel;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.server.remoteiterator.json.RemoteKVIteratorJsonTransactionServer;

/**
 * Key/Value Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it is 
 * for non-serializable iterators and streams, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence.
 * Functionally, this class Extends TCPServer
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<br>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * Use this class when transaction context is required.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2021,2022
 *
 */
public final class RelatrixKVJsonTransactionServer extends RelatrixKVTransactionServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;

	// in server, we are using local repository for handlerclassloader, but only one
	// and that one will be located on port 9999
	boolean isThisBytecodeRepository = false;

	private ConcurrentHashMap<String, TCPJsonKVTransactionWorker> dbToWorker = new ConcurrentHashMap<String, TCPJsonKVTransactionWorker>();
	
	public static String[] iteratorServers = new String[]{
			"com.neocoretechs.relatrix.iterator.IteratorWrapper"
	};				
	public static int[] iteratorPorts = new int[] {
			9030
	};
	public static int findIteratorServerPort(String clazz) {
		return iteratorPorts[Arrays.asList(iteratorServers).indexOf(clazz)];
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVJsonTransactionServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVJsonTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKVTransaction", 0);
		address = startServer(port);
		if(port == 9999) {
			isThisBytecodeRepository = true;
			System.out.println("NOTE: This Json KV transaction server now Serving bytecode, port "+port+" is reserved for bytecode repository!");
			try {
				HandlerClassLoader.connectToLocalRepository(RelatrixKVTransaction.getTableSpace()); // use default path
			} catch (IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteKVIteratorJsonTransactionServer(iteratorServers[i], ((InetSocketAddress)RelatrixKVJsonTransactionServer.address).getAddress(), iteratorPorts[i]);
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVJsonTransactionServer(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVJsonTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKVTransaction", 0);	
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
		if(port == 9999) {
			isThisBytecodeRepository = true;
			System.out.println("NOTE: This Json KV transaction server now Serving bytecode, port "+port+" is reserved for bytecode repository!");
			try {
				HandlerClassLoader.connectToLocalRepository(RelatrixKVTransaction.getTableSpace()); // use default path
			} catch (IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteKVIteratorJsonTransactionServer(iteratorServers[i], ((InetSocketAddress)RelatrixKVTransactionServer.address).getAddress(), iteratorPorts[i]);
		
		SynchronizedThreadManager.startSupervisorThread();
	}

	@Override
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
                    TCPJsonKVTransactionWorker uworker = dbToWorker.get(datasocket.getRemoteAddress().toString());
                    if( uworker != null ) {
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    }
                    uworker = new TCPJsonKVTransactionWorker(datasocket);
                    dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
                    SynchronizedThreadManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println(this.getClass().getName()+" starting new worker "+uworker);
                    }
                    
				} catch(Exception e) {
                    System.out.println(this.getClass().getName()+" node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	}
	/**
	 * Load the methods of main RelatrixKV class as remotely invokable then we instantiate RelatrixKVTransactionServer.<p/>
	 * @param args If length 1, then default port 9002,  If port 9999, start as transactional byte code repository server.
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixKVTransaction.getInstance();
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    RelatrixKVTransaction.setTablespace(db);
		    new RelatrixKVJsonTransactionServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixKVJsonTransactionServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixKVJsonTransactionServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVTransactionServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}
	}	

}
