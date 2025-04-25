package com.neocoretechs.relatrix.server.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.json.*;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.server.ThreadPoolManager;
import com.neocoretechs.relatrix.server.remoteiterator.json.RemoteIteratorJsonServer;


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
public final class RelatrixJsonServer extends RelatrixServer {
	private static boolean DEBUG = false;
	private static boolean DEBUGCOMMAND = false;
	
	private ConcurrentHashMap<String, TCPJsonWorker> dbToWorker = new ConcurrentHashMap<String, TCPJsonWorker>();
	
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixJsonServer(int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.Relatrix", 0);
		address = startServer(port);
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteIteratorJsonServer(iteratorServers[i], address, iteratorPorts[i]);
	}
	/**
	 * Construct the Server, populate the target classes for remote invocation, which is local invocation here.
	 * @param address IP address
	 * @param port Port upon which to start server
	 * @throws IOException
	 * @throws ClassNotFoundException If one of the Relatrix classes reflected is missing, most likely missing jar
	 */
	public RelatrixJsonServer(String iaddress, int port) throws IOException, ClassNotFoundException {
		super();
		RelatrixServer.relatrixMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.Relatrix", 0);
		address = InetAddress.getByName(iaddress);
		for(int i = 0; i < iteratorServers.length; i++)
			new RemoteIteratorJsonServer(iteratorServers[i], address, iteratorPorts[i]);
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
                InputStream ins = datasocket.getInputStream();
    			BufferedReader in = new BufferedReader(new InputStreamReader(ins));
    			JSONObject jobj = new JSONObject(in.readLine());
                CommandPacketInterface o = (CommandPacketInterface) jobj.toObject();//CommandPacketInterface.class);
				if( DEBUGCOMMAND )
					System.out.println("Relatrix Server command received:"+o);
				// if we get a command packet with no statement, assume it to start a new instance

				TCPJsonWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
				if( uworker != null ) {
					if(o.getTransport().equals("TCP")) {
						if( uworker.shouldRun )
							uworker.stopWorker();
					}
				}                   
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPJsonWorker(datasocket, o.getRemoteMaster(), o.getMasterPort());
				dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
				ThreadPoolManager.getInstance().spin(uworker);

				if( DEBUG ) {
					System.out.println("RelatrixJsonServer starting new worker "+uworker+
							//( rdb != null ? "remote db:"+rdb : "" ) +
							" master port:"+o.getMasterPort());
				}

			} catch(Exception e) {
				System.out.println("Relatrix Json Server node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	/**
	 * Load the methods of main Relatrix class as remotely invokable then we instantiate RelatrixServer.<p/>
	 * @param args If length 1, then default port 9003
	 * @throws Exception If problem starting server.
	 */
	public static void main(String args[]) throws Exception {
		Relatrix.getInstance();
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    Relatrix.setTablespace(db);
		    new RelatrixJsonServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixJsonServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixJsonServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixJsonServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}
	}
}
