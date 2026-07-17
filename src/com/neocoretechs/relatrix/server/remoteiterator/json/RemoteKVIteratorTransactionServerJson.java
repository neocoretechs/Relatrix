package com.neocoretechs.relatrix.server.remoteiterator.json;

import java.io.IOException;

import java.net.InetAddress;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixKVJsonTransaction;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.TCPServer;

/**
 * When an iterator is created for remote delivery of objects, the address of the remote server
 * is sent back to the requesting client. The client 
 * creates a {@link com.neocoretechs.relatrix.server.TCPWorker} that creates a 
 * {@link com.neocoretechs.relatrix.server.WorkerRequestProcessor} that dequeues requests
 * with the proper ServerSideIterator that receives each hasNext and next request. The process method of
 * a {@link com.neocoretechs.relatrix.client.RemoteCompletionInterface}, which is implemented
 * by each server side iterator, is called with the dequeued object.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteKVIteratorTransactionServerJson extends TCPServer {
	private ConcurrentHashMap<String, TCPKVIteratorTransactionWorkerJson> dbToWorker = new ConcurrentHashMap<String, TCPKVIteratorTransactionWorkerJson>();
	private static  boolean DEBUG = false;
	private String iteratorClass;
	
	public RemoteKVIteratorTransactionServerJson(String iteratorClass, InetAddress host, int port) throws IOException, ClassNotFoundException {
		super();
		this.iteratorClass = iteratorClass;
		startServer(port, host, iteratorClass);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	@Override
	public void run() {
		while(!shouldStop) {
			try {
				SocketChannel datasocket = server.accept();
				//
				// if we get a command packet with no statement, assume it to start a new instance
				TCPKVIteratorTransactionWorkerJson uworker = dbToWorker.get(datasocket.getRemoteAddress().toString());
				if( uworker != null ) {
						if( uworker.shouldRun )
							uworker.stopWorker();
				}                   
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPKVIteratorTransactionWorkerJson(datasocket, iteratorClass, RelatrixKVJsonTransaction.classLoader);
				dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
             	IndexResolver indexResolver = new IndexResolver();
            	indexResolver.setLocalJson();
            	ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());
            	SynchronizedThreadManager.getInstance().spinWithContext(uworker, pec);      
				if( DEBUG ) {
					System.out.println(this.getClass().getName()+" starting new worker "+uworker);
				}
			} catch(Exception e) {
				System.out.println(this.getClass().getName()+" Server node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
}
