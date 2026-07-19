package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.SocketChannel;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteIteratorServer;

/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of {@link ServerInvokeMethod} and populating that at construction of this class.<p>
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it 
 * is for non-serializable iterators, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence .<br>
 * Functionally this class Extends TCPServer,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<p>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021, 2024
 *
 */
public class RelatrixServer extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static SocketAddress address;
	public static int port;
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();
	
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	private ConcurrentHashMap<String, TCPServer> iteratorToServer = new ConcurrentHashMap<String, TCPServer>();
	
	public static Class<?> relatrixClass = com.neocoretechs.relatrix.Relatrix.class;
	
	public static final Class<?> relatrixIteratorClass = com.neocoretechs.relatrix.iterator.RelatrixIterator.class; 
	public static final Class<?> relatrixSubsetIteratorClass = com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator.class;
	public static final Class<?> relatrixHeadsetIteratorClass = com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator.class;
	public static final Class<?> relatrixTailsetIteratorClass =  com.neocoretechs.relatrix.iterator.RelatrixTailsetIterator.class;
	public static final Class<?> relatrixEntrysetIteratorClass = com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator.class;				
	public static final Class<?> relatrixKeysetIteratorClass =  com.neocoretechs.relatrix.iterator.RelatrixKeysetIterator.class;
	
	public static final String relatrixIterator = relatrixIteratorClass.getName();
	public static final String relatrixSubsetIterator = relatrixSubsetIteratorClass.getName();
	public static final String relatrixHeadsetIterator = relatrixHeadsetIteratorClass.getName();
	public static final String relatrixTailsetIterator =  relatrixTailsetIteratorClass.getName();
	public static final String relatrixEntrysetIterator = relatrixEntrysetIteratorClass.getName();				
	public static final String relatrixKeysetIterator =  relatrixKeysetIteratorClass.getName();
	
	public static String[] iteratorServers = new String[]{
	 relatrixIterator,
	 relatrixSubsetIterator,
	 relatrixHeadsetIterator,
	 relatrixTailsetIterator,
	 relatrixEntrysetIterator,				
	 relatrixKeysetIterator
	};
	
	public static Class[] iteratorServerClasses = new Class[]{
	 relatrixIteratorClass,
	 relatrixSubsetIteratorClass,
	 relatrixHeadsetIteratorClass,
	 relatrixTailsetIteratorClass,
	 relatrixEntrysetIteratorClass,				
	 relatrixKeysetIteratorClass
	};
	
	public static int[] iteratorPorts = new int[] {
			9090,9091,9092,9093,9094,9095
	};

	public RelatrixServer() {}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.port = port;
		RelatrixServer.relatrixMethods = new ServerInvokeMethod(relatrixClass.getName(), 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
		SynchronizedThreadManager.startSupervisorThread();
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
		RelatrixServer.port = port;
		RelatrixServer.relatrixMethods = new ServerInvokeMethod(relatrixClass.getName(), 0);
		address = new InetSocketAddress(iaddress, port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		startServer(address);
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param address IP address
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixServer(InetAddress iaddress, int port, boolean wait) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.port = port;
		RelatrixServer.relatrixMethods = new ServerInvokeMethod(relatrixClass.getName(), 0);
		address = new InetSocketAddress(iaddress,port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorServer(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
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
	           	IndexResolver indexResolver = new IndexResolver();
        		indexResolver.setLocal();
        		ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());
    			uworker = new TCPWorker(datasocket, pec, RelatrixKV.classLoader);
				dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
        		SynchronizedThreadManager.getInstance().spinWithContext(uworker, pec);
                if( DEBUG ) {
                	System.out.println(this.getClass().getName()+" starting new worker "+uworker);
                }
			} catch(Exception e) {
				System.out.println("Relatrix Server node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixServer.<p>
	 * @param args If length 1, then default port 9000, args 2 = server, port
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		Relatrix.getInstance();
			if( args.length == 2) {
				System.out.println("Bringing up Relatrix tablespace "+System.getProperty("tablespace"));
				new RelatrixServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace "+System.getProperty("tablespace"));
					new RelatrixServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixServer [address] <port>");
				}
			}
		System.out.println(address);
	}
}
