package com.neocoretechs.relatrix.server.json;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.SocketChannel;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixJsonTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.server.TCPServer;
import com.neocoretechs.relatrix.server.TCPWorker;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteIteratorTransactionServer;

/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.<p>
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it 
 * is for non-serializable iterators, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence.<br>
 * Functionally this class Extends TCPServer, 
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<p>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021, 2022, 2024
 *
 */
public class RelatrixTransactionServerJson extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static SocketAddress address;
	public static int port;
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();

	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	private ConcurrentHashMap<String, TCPServer> iteratorToServer = new ConcurrentHashMap<String, TCPServer>();
	
	public static final Class<?> relatrixIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.transaction.RelatrixIteratorJsonTransaction.class; 
	public static final Class<?> relatrixSubsetIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.transaction.RelatrixSubsetIteratorJsonTransaction.class;
	public static final Class<?> relatrixHeadsetIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.transaction.RelatrixHeadsetIteratorJsonTransaction.class;
	public static final Class<?> relatrixTailsetIteratorJsonClass =  com.neocoretechs.relatrix.iterator.json.transaction.RelatrixTailsetIteratorJsonTransaction.class;
	public static final Class<?> relatrixEntrysetIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.transaction.RelatrixEntrysetIteratorJsonTransaction.class;				
	public static final Class<?> relatrixKeysetIteratorJsonClass =  com.neocoretechs.relatrix.iterator.json.transaction.RelatrixKeysetIteratorJsonTransaction.class;
	
	public static final String relatrixIteratorJson = relatrixIteratorJsonClass.getName();
	public static final String relatrixSubsetIteratorJson = relatrixSubsetIteratorJsonClass.getName();
	public static final String relatrixHeadsetIteratorJson = relatrixHeadsetIteratorJsonClass.getName();
	public static final String relatrixTailsetIteratorJson =  relatrixTailsetIteratorJsonClass.getName();
	public static final String relatrixEntrysetIteratorJson = relatrixEntrysetIteratorJsonClass.getName();				
	public static final String relatrixKeysetIteratorJson =  relatrixKeysetIteratorJsonClass.getName();
	
	public static String[] iteratorServers = new String[]{
	 relatrixIteratorJson,
	 relatrixSubsetIteratorJson,
	 relatrixHeadsetIteratorJson,
	 relatrixTailsetIteratorJson,
	 relatrixEntrysetIteratorJson,				
	 relatrixKeysetIteratorJson
	};
	
	public static Class[] iteratorServerClasses = new Class[]{
	 relatrixIteratorJsonClass,
	 relatrixSubsetIteratorJsonClass,
	 relatrixHeadsetIteratorJsonClass,
	 relatrixTailsetIteratorJsonClass,
	 relatrixEntrysetIteratorJsonClass,				
	 relatrixKeysetIteratorJsonClass
	};
	
	public static int[] iteratorPorts = new int[] {
			9080,9081,9082,9083,9084,9085
	};
	
	public RelatrixTransactionServerJson() {}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixTransactionServerJson(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServerJson.port = port;
		RelatrixTransactionServerJson.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorTransactionServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));		
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the server bound to stated address
	 * @param address
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public RelatrixTransactionServerJson(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServerJson.port = port;
		RelatrixTransactionServerJson.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		address = new InetSocketAddress(iaddress, port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorTransactionServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		startServer(address);	
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the server bound to stated address
	 * @param address
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public RelatrixTransactionServerJson(InetAddress iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServerJson.port = port;
		RelatrixTransactionServerJson.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		address = new InetSocketAddress(iaddress, port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorTransactionServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		startServer(address);	
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the server bound to stated address
	 * @param address
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public RelatrixTransactionServerJson(InetAddress iaddress, int port, boolean wait) throws IOException, ClassNotFoundException {
		super();
		RelatrixTransactionServerJson.port = port;
		RelatrixTransactionServerJson.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.RelatrixTransaction", 0);
		address = new InetSocketAddress(iaddress, port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorTransactionServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));	
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
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPWorker(datasocket);
				dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
             	IndexResolver indexResolver = new IndexResolver();
            	indexResolver.setLocalJson();
            	ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());
            	SynchronizedThreadManager.getInstance().spinWithContext(uworker, pec);
                if( DEBUG ) {
                	System.out.println(this.getClass().getName()+" starting new worker "+uworker);
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
		RelatrixJsonTransaction.getInstance();
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixJsonTransaction default tablespace "+System.getProperty("tablespace"));
			new RelatrixTransactionServerJson(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixJsonTransaction default tablespace "+System.getProperty("tablespace"));
				new RelatrixTransactionServerJson(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.json.RelatrixTransactionServerJson [address] <port>");
			}
		}
		System.out.println(address);
	}

}
