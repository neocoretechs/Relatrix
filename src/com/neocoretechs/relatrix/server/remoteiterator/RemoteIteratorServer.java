package com.neocoretechs.relatrix.server.remoteiterator;

import java.io.IOException;

import java.net.InetAddress;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;
import com.neocoretechs.relatrix.server.TCPServer;

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
public class RemoteIteratorServer extends TCPServer {
	private ConcurrentHashMap<String, TCPIteratorWorker> dbToWorker = new ConcurrentHashMap<String, TCPIteratorWorker>();
	private static  boolean DEBUG = false;
	private String iteratorClass;
	
	public RemoteIteratorServer(String iteratorClass, InetAddress host, int port) throws IOException, ClassNotFoundException {
		super();
		this.iteratorClass = iteratorClass;
		startServer(port, host, iteratorClass);
	}
	
	@Override
	public void run() {
		while(!shouldStop) {
			try {
				SocketChannel datasocket = server.accept();
				TCPIteratorWorker uworker = dbToWorker.get(datasocket.getRemoteAddress().toString());
				if( uworker != null ) {
						if( uworker.shouldRun )
							uworker.stopWorker();
				}                   
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPIteratorWorker(datasocket, iteratorClass);
				dbToWorker.put(datasocket.getRemoteAddress().toString(), uworker); 
				SynchronizedThreadManager.getInstance().spin(uworker);

	            if( DEBUG ) {
	                System.out.println(this.getClass().getName()+" starting new worker "+uworker);
	            }

			} catch(Exception e) {
				System.out.println("RemoteIteratorServer Server node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
}
