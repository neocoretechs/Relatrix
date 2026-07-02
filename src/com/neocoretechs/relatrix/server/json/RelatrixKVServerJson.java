package com.neocoretechs.relatrix.server.json;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import com.neocoretechs.relatrix.server.TCPServer;
import com.neocoretechs.relatrix.server.TCPWorker;

import com.neocoretechs.relatrix.server.remoteiterator.json.RemoteKVIteratorServerJson;

/**
 * Key/Value Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it is 
 * for non-serializable iterators and streams, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence .
 * Functionally, this class Extends TCPServer
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<br>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2021,2026
 *
 */
public class RelatrixKVServerJson extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static SocketAddress address;
	public static int port;
	
	public static ServerInvokeMethodJson relatrixMethods = null; // Main RelatrixKVJson class methods
	
	public static final Class<?> relatrixKVJsonClass = com.neocoretechs.relatrix.RelatrixKVJson.class;
	
	public static final String relatrixKVJson = relatrixKVJsonClass.getName();
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();

	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	private ConcurrentHashMap<String, TCPServer> iteratorToServer = new ConcurrentHashMap<String, TCPServer>();
					
	public static int[] iteratorPorts = new int[] {9130};
	
	public static final Class<?> relatrixIteratorJsonClass = com.neocoretechs.relatrix.iterator.IteratorWrapper.class; 
	
	public static final String relatrixIteratorJson = relatrixIteratorJsonClass.getName();
	
	public static String[] iteratorServers = new String[]{relatrixIteratorJson};
	
	public static Class[] iteratorServerClasses = new Class[]{relatrixIteratorJsonClass};
	
	public RelatrixKVServerJson() {}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server. 
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the {@link RelatrixKVJson} classes reflected is missing, most likely missing jar
	 */
	public RelatrixKVServerJson(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServerJson.port = port;
		RelatrixKVServerJson.relatrixMethods = new ServerInvokeMethodJson(relatrixKVJson, 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));		
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public RelatrixKVServerJson(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServerJson.port = port;
		RelatrixKVServerJson.relatrixMethods = new ServerInvokeMethodJson(relatrixKVJson, 0);
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));	
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public RelatrixKVServerJson(InetAddress iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServerJson.port = port;
		RelatrixKVServerJson.relatrixMethods = new ServerInvokeMethodJson(relatrixKVJson, 0);
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));	
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	public RelatrixKVServerJson(InetAddress iaddress, int port, boolean wait) throws IOException, ClassNotFoundException {
		super();
		RelatrixKVServerJson.port = port;
		RelatrixKVServerJson.relatrixMethods = new ServerInvokeMethodJson(relatrixKVJson, 0);
		address = new InetSocketAddress(iaddress, port);
		startServer(address);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteKVIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));	
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
                    if( DEBUG | DEBUGCOMMAND )
                    	System.out.printf("%s created worker worker:%s%n",this.getClass().getName(),uworker);
                    uworker = new TCPWorker(datasocket);
                    dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
                    SynchronizedThreadManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println(this.getClass().getName()+" starting new worker "+uworker);
                    }
                    
				} catch(Exception e) {
                    e.printStackTrace();
               }
		}
	}
	
	/**
	 * Load the methods of main RelatrixKVJson class as remotely invokable then we instantiate RelatrixKVServerJson.<p>
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixKVJson.getInstance();
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixKVServerJson tablespace "+System.getProperty("tablespace"));
			new RelatrixKVServerJson(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixKVServerJson tablespace "+System.getProperty("tablespace"));
				new RelatrixKVServerJson(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVServerJson [address] <port>");
			}
		}
		System.out.println(address);
	}

}
