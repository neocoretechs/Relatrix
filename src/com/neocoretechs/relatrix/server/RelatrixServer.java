package com.neocoretechs.relatrix.server;

import java.io.File;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.bigsack.io.ThreadPoolManager;

/**
 * Extends TCPServer, receives CommandPacketinterface.
 * Starts a TCPWorker, which spawns a WorkerRequestProcessor.
 * WorkerRequestProcessor takes IoRequestInterface, processes, returns DistributedWorkerResponseInterface
 * Get an address down, at the master we coordinate the assignment of addresses
 * It comes to this known address via TCP packet of serialized
 * command.  Also sent down is the database to operate on for the worker we are spinning. 
 * @author jg Copyright (C) NeoCoreTechs 2015
 *
 */
public final class RelatrixServer extends TCPServer {
	private static boolean DEBUG = true;
	public static int port = 8000;
	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	/**
	 * Spin the worker, get the tablespace from the cmdl param
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		if( args.length > 0 ) {
			port = Integer.valueOf(args[0]);
		}
		RelatrixServer wb = new RelatrixServer();
		wb.startServer(port);
		System.out.println("Relatrix Server started on "+port+" of "+InetAddress.getLocalHost().getHostName());
	}
	

	public void run() {
			while(!shouldStop) {
				try {
					Socket datasocket = server.accept();
                    // disable Nagles algoritm; do not combine small packets into larger ones
                    datasocket.setTcpNoDelay(true);
                    // wait 1 second before close; close blocks for 1 sec. and data can be sent
                    datasocket.setSoLinger(true, 1);
					//
                    ObjectInputStream ois = new ObjectInputStream(datasocket.getInputStream());
                    CommandPacketInterface o = (CommandPacketInterface) ois.readObject();
                    if( DEBUG )
                    	System.out.println("Relatrix Server command received:"+o);
    
                    String db = o.getDatabase();
   
                    db = (new File(db)).toPath().getParent().toString() + File.separator +
                    		(new File(o.getDatabase()).getName());
                    // determine if this worker has started, if so, cancel thread and start a new one.
                    TCPWorker uworker = null;
                    if( (uworker = dbToWorker.get(db)) != null &&  o.getTransport().equals("TCP")) {
                    	((TCPWorker)uworker).stopWorker();
                    }
  
                    uworker = new TCPWorker(db, o.getRemoteMaster(), Integer.valueOf(o.getMasterPort()), Integer.valueOf(o.getSlavePort()));
                    dbToWorker.put(db, uworker);
                    ThreadPoolManager.getInstance().spin(uworker);
                    if( DEBUG ) {
                    		System.out.println("RelatrixServer starting new worker "+db+" master port:"+o.getMasterPort()+" slave port:"+o.getSlavePort());
                    }	
				} catch(Exception e) {
                    System.out.println("Relatrix Server node configuration server socket accept exception "+e);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
               }
			}
	
	}

}
