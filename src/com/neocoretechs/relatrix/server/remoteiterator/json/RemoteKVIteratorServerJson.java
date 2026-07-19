package com.neocoretechs.relatrix.server.remoteiterator.json;

import java.io.IOException;

import java.net.InetAddress;

import java.nio.channels.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.TCPServer;

/**
 * When an iterator is created for remote delivery of objects, The client
 * connects to waiting serversocket for each type of iterator.  a 
 * {@link com.neocoretechs.relatrix.server.WorkerRequestProcessor} dequeues requests
 * with the proper ServerSideIterator that receives each hasNext and next request. The process method of
 * a {@link com.neocoretechs.relatrix.client.RemoteCompletionInterface}, which is implemented
 * by each server side iterator, is called with the dequeued object.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteKVIteratorServerJson extends TCPServer {
	private ConcurrentHashMap<String, TCPKVIteratorWorkerJson> dbToWorker = new ConcurrentHashMap<String, TCPKVIteratorWorkerJson>();
	private static  boolean DEBUG = false;
	private String iteratorClass;
	
	public RemoteKVIteratorServerJson(String iteratorClass, InetAddress address, int port) throws IOException, ClassNotFoundException {
		super();
		this.iteratorClass = iteratorClass;
		startServer(port, address, iteratorClass);
	}
	
	@Override
	public void run() {
		while(!shouldStop) {
			try {
				SocketChannel datasocket = server.accept();
				TCPKVIteratorWorkerJson uworker = dbToWorker.get(datasocket.getRemoteAddress().toString());
				if( uworker != null ) {
						if( uworker.shouldRun )
							uworker.stopWorker();
				}                   
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPKVIteratorWorkerJson(datasocket, iteratorClass, RelatrixKVJson.classLoader);
				dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker);
	           	IndexResolver indexResolver = new IndexResolver();
            	indexResolver.setLocal();
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
