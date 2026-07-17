package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixKV;
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
 * Static methods need no server side object in residence .
 * Functionally, this class Extends TCPServer,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<br>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2021
 *
 */
public class RelatrixKVServer extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static SocketAddress address;
	public static int port;
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();

	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	private ConcurrentHashMap<String, TCPServer> iteratorToServer = new ConcurrentHashMap<String, TCPServer>();
	
	public static Class<?> iteratorServerClass = com.neocoretechs.relatrix.iterator.IteratorWrapper.class;
	
	public static String[] iteratorServers = new String[]{
			iteratorServerClass.getName()
	};		
	
	public static int[] iteratorPorts = new int[] {
			9030
	};
	
	public RelatrixKVServer() {}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. The port at 9999 is reserved for serving Java bytecode specifically in support of server operations.
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServer.port = port;
		RelatrixKVServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKV", 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));	
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public RelatrixKVServer(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServer.port = port;
		RelatrixKVServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKV", 0);
		address = new InetSocketAddress(iaddress,port);
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public RelatrixKVServer(InetAddress iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServer.port = port;
		RelatrixKVServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKV", 0);
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public RelatrixKVServer(InetAddress iaddress, int port, boolean wait) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServer.port = port;
		RelatrixKVServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixKV", 0);
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
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
                    uworker = new TCPWorker(datasocket, pec, RelatrixKV.classLoader);
                    dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker);
                    // Create the worker, it in turn creates a WorkerRequestProcessor
                    if( DEBUG | DEBUGCOMMAND )
                    	System.out.printf("%s created worker worker:%s%n",this.getClass().getName(),uworker);
            		SynchronizedThreadManager.getInstance().spinWithContext(uworker, pec);
                    
                    if( DEBUG ) {
                    	System.out.println(this.getClass().getName()+" starting new worker "+uworker);
                    }
                    
				} catch(Exception e) {
                    System.out.println("Relatrix K/V Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
		}
	}
	
	/**
	 * Load the methods of main RelatrixKV class as remotely invokable then we instantiate RelatrixKVServer.<p>
	 * @param args If length 1, then default port 9000, must specify tablespace or alias subsequently. Same for 2 arg: host, port. 3 args then arg 0 is to set default tablespace
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixKV.getInstance();
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixKV tablespace "+System.getProperty("tablespace"));
			new RelatrixKVServer(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixKV tablespace "+System.getProperty("tablespace"));
				new RelatrixKVServer(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVServer [address] <port>");
			}
		}
		System.out.println(address);
	}

}
