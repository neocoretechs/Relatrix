package com.neocoretechs.relatrix.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteKVIteratorJsonTransactionServer;

/**
 * Key/Value Remote invocation of methods consists of providing reflected classes here which are invoked via simple
 * serializable descriptions of the method and parameters. Providing additional resources involves adding
 * another static instance of ServerInvokeMethod and populating that at construction of this class.
 * In the processing pipeline you must provide a 'process' implementation which will call 'invokeMethod'
 * and if the remote call is linked to an object instance on the server, as it is 
 * for non-serializable iterators and streams, then you must maintain 
 * a mapping from session GUID to an instance of the object you are invoking on the server side.<p/>
 * Static methods need no server side object in residence and can be called willy nilly.
 * Functionally, this class Extends TCPServer, receives CommandPacketinterface,
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.<br/>
 * WorkerRequestProcessor takes requests and processes them.<br/>
 * On the client and server the following are present as conventions:<br/>
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT.<br/>
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it.<br/>
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it.<br/>
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it.<br/>
 * The client is going to connect and tell the server the master and slave ports that it will be using to process requests.<br/>
 * In this way multiple databases can be used by instantiating separate clients.<p/>
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
			new RemoteKVIteratorJsonTransactionServer(iteratorServers[i], address, iteratorPorts[i]);
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
		address = InetAddress.getByName(iaddress);
		startServer(port,address);
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
			new RemoteKVIteratorJsonTransactionServer(iteratorServers[i], address, iteratorPorts[i]);
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
                    InputStream ins = datasocket.getInputStream();
        			BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        			JSONObject jobj = new JSONObject(in.readLine());
                    CommandPacketInterface o = (CommandPacketInterface) jobj.toObject();//, CommandPacket.class);
                    if( DEBUG | DEBUGCOMMAND )
                    	System.out.println("Relatrix K/V Json Transaction Server command received:"+o);
                    TCPJsonKVTransactionWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
                    if( uworker != null ) {
                    	if(o.getTransport().equals("TCP")) {
                    		if( uworker.shouldRun )
                    			uworker.stopWorker();
                    	}
                    }
                    uworker = new TCPJsonKVTransactionWorker(datasocket, o.getRemoteMaster(), o.getMasterPort());
                    dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
                    ThreadPoolManager.getInstance().spin(uworker);
                    
                    if( DEBUG ) {
                    	System.out.println("RelatrixKVTransactionServer starting new worker "+uworker+
                    			//( rdb != null ? "remote db:"+rdb : "" ) +
                    			" master port:"+o.getMasterPort());
                    }
                    
				} catch(Exception e) {
                    System.out.println("Relatrix K/V Transaction Server node configuration server socket accept exception "+e);
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
