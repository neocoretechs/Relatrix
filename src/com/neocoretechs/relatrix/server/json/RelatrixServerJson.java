package com.neocoretechs.relatrix.server.json;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.nio.channels.SocketChannel;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixJson;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.server.TCPServer;
import com.neocoretechs.relatrix.server.TCPWorker;

import com.neocoretechs.relatrix.server.remoteiterator.json.RemoteIteratorServerJson;

/**
 * Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of {@link ServerInvokeMethod} and populating that at construction of this class.<p>
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it 
 * is for non-serializable iterators, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p>
 * Static methods need no server side object in residence .<br>
 * Functionally this class Extends TCPServer, on behalf of the RelatrixJson embedded server, then
 * starts a TCPWorker, which spawns a WorkerRequestProcessor.<p>
 * WorkerRequestProcessor takes requests and processes them.<br>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015, 2021, 2024, 2026
 *
 */
public class RelatrixServerJson extends TCPServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	public static SocketAddress address;
	public static int port;
	
	public static ServerInvokeMethod relatrixMethods = null; // Main Relatrix class methods
	public static final String relatrixJson = "com.neocoretechs.relatrix.RelatrixJson";
	
	public static ConcurrentHashMap<String, Object> sessionToObject = new ConcurrentHashMap<String,Object>();
	
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	
	private ConcurrentHashMap<String, TCPServer> iteratorToServer = new ConcurrentHashMap<String, TCPServer>();
	
	public static final String relatrixIteratorJson = "com.neocoretechs.relatrix.iterator.json.RelatrixIteratorJson"; 
	public static final String relatrixSubsetIteratorJson = "com.neocoretechs.relatrix.iterator.json.RelatrixSubsetIteratorJson";
	public static final String relatrixHeadsetIteratorJson = "com.neocoretechs.relatrix.iterator.json.RelatrixHeadsetIteratorJson";
	public static final String relatrixTailsetIteratorJson =  "com.neocoretechs.relatrix.iterator.json.RelatrixTailsetIteratorJson";
	public static final String relatrixEntrysetIteratorJson = "com.neocoretechs.relatrix.iterator.json.RelatrixEntrysetIteratorJson";				
	public static final String relatrixKeysetIteratorJson =  "com.neocoretechs.relatrix.iterator.json.RelatrixKeysetIteratorJson";
	
	public static final Class<?> relatrixIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.RelatrixIteratorJson.class; 
	public static final Class<?> relatrixSubsetIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.RelatrixSubsetIteratorJson.class;
	public static final Class<?> relatrixHeadsetIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.RelatrixHeadsetIteratorJson.class;
	public static final Class<?> relatrixTailsetIteratorJsonClass =  com.neocoretechs.relatrix.iterator.json.RelatrixTailsetIteratorJson.class;
	public static final Class<?> relatrixEntrysetIteratorJsonClass = com.neocoretechs.relatrix.iterator.json.RelatrixEntrysetIteratorJson.class;				
	public static final Class<?> relatrixKeysetIteratorJsonClass =  com.neocoretechs.relatrix.iterator.json.RelatrixKeysetIteratorJson.class;
	
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
			9190,9191,9192,9193,9194,9195
	};

	public RelatrixServerJson() {}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the RelatrixJson classes reflected is missing, most likely missing jar
	 */
	public RelatrixServerJson(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServerJson.port = port;
		RelatrixServerJson.relatrixMethods = new ServerInvokeMethod(relatrixJson, 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		
		SynchronizedThreadManager.startSupervisorThread();
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param address IP address
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the RelatrixJson classes reflected is missing, most likely missing jar
	 */
	public RelatrixServerJson(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServerJson.port = port;
		RelatrixServerJson.relatrixMethods = new ServerInvokeMethod(relatrixJson, 0);
		address = new InetSocketAddress(iaddress, port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));
		startServer(address);
		SynchronizedThreadManager.startSupervisorThread();
	}
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param address IP address
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the RelatrixJson classes reflected is missing, most likely missing jar
	 */
	public RelatrixServerJson(InetAddress iaddress, int port, boolean wait) throws IOException, ClassNotFoundException {
		super();
		RelatrixServerJson.port = port;
		RelatrixServerJson.relatrixMethods = new ServerInvokeMethod(relatrixJson, 0);
		address = new InetSocketAddress(iaddress,port);
		for(int i = 0; i < iteratorServers.length; i++)
			iteratorToServer.put(iteratorServers[i],new RemoteIteratorServerJson(iteratorServers[i], ((InetSocketAddress)address).getAddress(), iteratorPorts[i]));	
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
	 * Load the methods of main RelatrixJson class as remotely invokable then we instantiate RelatrixServerJson.<p/>
	 * @param args If length 1, then set port, args 2 server bind address, port. Sets the default tablespace to cmdl property
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		RelatrixJson.getInstance();
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixJson tablespace "+System.getProperty("tablespace"));
			new RelatrixServerJson(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixJson tablespace "+System.getProperty("tablespace"));
				new RelatrixServerJson(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.json.RelatrixServerJson [address] <port>");
			}
		}
		System.out.println(address);
	}
}
