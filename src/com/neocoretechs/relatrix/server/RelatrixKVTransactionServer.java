package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteKVIteratorServer;

/**
 * Key/Value Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it is 
 * for non-serializable iterators and streams, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence.
 * Functionally, this class Extends TCPServer,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<br>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * Use this class when transaction context is required.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2021,2022
 */
public class RelatrixKVTransactionServer extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static SocketAddress address;
	public static int port;
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods

	// in server, we are using local repository for handlerclassloader, but only one
	// and that one will be located on port 9999
	boolean isThisBytecodeRepository = false;
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();

	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	private ConcurrentHashMap<String, TCPServer> iteratorToServer = new ConcurrentHashMap<String, TCPServer>();
	
	public static String[] iteratorServers = new String[]{
			"com.neocoretechs.relatrix.iterator.IteratorWrapper"
	};				
	public static int[] iteratorPorts = new int[] {
			9020
	};
	public static int findIteratorServerPort(String clazz) {
		return iteratorPorts[Arrays.asList(iteratorServers).indexOf(clazz)];
	}
	
	protected RelatrixKVTransactionServer() {}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVTransactionServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVTransactionServer.port = port;
		RelatrixKVTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKVTransaction", 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVTransactionServer(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVTransactionServer.port = port;
		RelatrixKVTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKVTransaction", 0);	
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
		SynchronizedThreadManager.startSupervisorThread();
	}

	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVTransactionServer(SocketAddress iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVTransactionServer.port = port;
		RelatrixKVTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKVTransaction", 0);	
		address = iaddress;
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVTransactionServer(SocketAddress iaddress, int port, boolean wait) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVTransactionServer.port = port;
		RelatrixKVTransactionServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKVTransaction", 0);	
		address = iaddress;
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	@Override
	public void stopServer() {
		iteratorToServer.forEach((k,e)->{
			try {
				e.stopServer();
			} catch (IOException e1) {}
		});
		dbToWorker.forEach((k,e)->{
			e.stopWorker();
		});
		try {
			super.stopServer();
		} catch (IOException e1) {}
		SynchronizedThreadManager.stopAllSupervisors();
	}
	
	@Override
	public void run() {
			while(!shouldStop) {
				try {
					SocketChannel datasocket = server.accept();
					if(DEBUG)
						System.out.printf("%s accept channel: %s%n", this.getClass().getName(),datasocket);
                    TCPWorker uworker = dbToWorker.get(datasocket.getRemoteAddress().toString());
                    if( uworker != null ) {
                        if( DEBUG | DEBUGCOMMAND )
                        	System.out.printf("%s found existing worker:%s%n",this.getClass().getName(),uworker);
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    }
                	IndexResolver indexResolver = new IndexResolver();
            		indexResolver.setLocal();
            		ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());
                    uworker = new TCPWorker(datasocket, pec, RelatrixKVTransaction.classLoader);
                    dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker);
                    if( DEBUG ) {
                    	System.out.println(this.getClass().getName()+" starting new worker "+uworker);
                    }
            		SynchronizedThreadManager.getInstance().spinWithContext(uworker, pec);
                    
				} catch(Exception e) {
                    System.out.println("Relatrix K/V Transaction Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	}
	/**
	 * Load the methods of main RelatrixKV class as remotely invokable then we instantiate RelatrixKVTransactionServer.<p>
	 * @param args If length 1, then default port 9000, 
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixKVTransaction.getInstance();
			if( args.length == 2) {
				System.out.println("Bringing up RelatrixKVTransaction tablespace "+System.getProperty("tablespace"));
				new RelatrixKVTransactionServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up RelatrixKVTransaction tablespace "+System.getProperty("tablespace"));
					new RelatrixKVTransactionServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVTransactionServer [address] <port>");
				}
			}
		System.out.println(address);
	}	

}
