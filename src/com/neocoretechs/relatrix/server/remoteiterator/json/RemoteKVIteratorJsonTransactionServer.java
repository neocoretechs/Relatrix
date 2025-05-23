package com.neocoretechs.relatrix.server.remoteiterator.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.TCPServer;

import com.neocoretechs.relatrix.server.ThreadPoolManager;
/**
 * When an iterator is created for remote delivery of objects, the address of the remote server
 * is sent back to the requesting client. The client then issues the handshake connection. The client
 * connects to waiting serversocket for each type of iterator. The serversocket then connects back to the client
 * and creates a {@link com.neocoretechs.relatrix.server.TCPWorker} that creates a 
 * {@link com.neocoretechs.relatrix.server.WorkerRequestProcessor} that dequeues requests
 * with the proper ServerSideIterator that receives each hasNext and next request. The process method of
 * a {@link com.neocoretechs.relatrix.client.RemoteCompletionInterface}, which is implemented
 * by each server side iterator, is called with the dequeued object.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteKVIteratorJsonTransactionServer extends TCPServer {
	private ConcurrentHashMap<String, TCPKVJsonIteratorTransactionWorker> dbToWorker = new ConcurrentHashMap<String, TCPKVJsonIteratorTransactionWorker>();
	private static  boolean DEBUG = false;
	private String iteratorClass;
	
	public RemoteKVIteratorJsonTransactionServer(String iteratorClass, InetAddress host, int port) throws IOException, ClassNotFoundException {
		super();
		this.iteratorClass = iteratorClass;
		startServer(port, host, iteratorClass);
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
				BufferedReader in = new BufferedReader(new InputStreamReader(datasocket.getInputStream()));
				JSONObject inLine = new JSONObject(in.readLine());
				if(DEBUG)
					System.out.println("RemoteKVIteratorJsonTransactionServer "+datasocket+" raw data:"+inLine);
				CommandPacket o = (CommandPacket) inLine.toObject();	
				if( DEBUG )
					System.out.println("RemoteKVIteratorJsonTransactionServer command received:"+o);
				// if we get a command packet with no statement, assume it to start a new instance

				TCPKVJsonIteratorTransactionWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
				if( uworker != null ) {
					if(o.getTransport().equals("TCP")) {
						if( uworker.shouldRun )
							uworker.stopWorker();
					}
				}                   
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPKVJsonIteratorTransactionWorker(datasocket, o.getRemoteMaster(), o.getMasterPort(), iteratorClass);
				dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
				ThreadPoolManager.getInstance().spin(uworker);

				if( DEBUG ) {
					System.out.println("RemoteKVIteratorJsonTransactionServer starting new worker "+uworker+
							//( rdb != null ? "remote db:"+rdb : "" ) +
							" master port:"+o.getMasterPort());
				}

			} catch(Exception e) {
				System.out.println("RemoteKVIteratorJsonTransactionServer Server node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
}
