package com.neocoretechs.relatrix.server;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.CountDownLatch;

import org.rocksdb.RocksDBException;

import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;

/**
 * Once requests from master are queued we extract them here and process them.<p/>
 * This class functions as a generic threaded request processor for entries on a BlockingQueue of 
 * RemoteCompletionInterface implementors.<br/>
 * This request processor is spun up in conjunction with IO workers such as TCPMaster. 
 * The intent is to separate the processing of requests and the maintenance of latches, etc from the communication
 * processing. In addition, increased parallelism can be achieved by separation of these tasks.
 * The WorkerRequestProcessors are responsible for setting the fields for the countdownlatch.
 * @author Jonathan Groff  Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public final class WorkerRequestProcessor implements Runnable {
	private static boolean DEBUG = false;
	//public static boolean SHOWDUPEKEYEXCEPTION = false;
	private static int QUEUESIZE = 1024;
	private BlockingQueue<RemoteCompletionInterface> requestQueue;

	private TCPWorker responseQueue;

	private volatile boolean shouldRun = true;
	private Object waitHalt = new Object();
	
	public WorkerRequestProcessor(TCPWorker tcpworker) {
		this.responseQueue = tcpworker;
		this.requestQueue = new ArrayBlockingQueue<RemoteCompletionInterface>(QUEUESIZE, true);
	}
	/**
	 * Wait for request queue to finish then exit and shutdown thread
	 */
	public void stop() {
		synchronized(waitHalt) {
			try {
				shouldRun = false;
				waitHalt.wait();
			} catch (InterruptedException e) {}
		}
	}
	
	public BlockingQueue<RemoteCompletionInterface> getQueue() { return requestQueue; }
	
	@Override
	public void run() {
	  while(shouldRun || !requestQueue.isEmpty()) {
		RemoteCompletionInterface iori = null;
		try {
			iori = requestQueue.take();
		} catch (InterruptedException e1) {
			// Executor has requested shutdown during take
		    // quit the processing thread
		    break;
		}
		// Down here at the worker level we only need to set the countdown latch to 1
		// because all operations are taking place on 1 tablespace and thread with coordination
		// at the Master level otherwise
		CountDownLatch cdl = new CountDownLatch(1);
		((RemoteCompletionInterface)iori).setCountDownLatch(cdl);
		if( DEBUG  ) {
			System.out.println("WorkerRequestProcessor preparing to process:"+iori);
		}
		
		try {
			//
			// invoke the designated method on the server, wait for countdown latch to signal finish
			// The result is placed in the return object or other property of the request/response/completion object
			//
			((RemoteCompletionInterface)iori).process();
			
			try {
				if( DEBUG )
					System.out.println(" avaiting countdown latch...");
				cdl.await();
			} catch (InterruptedException e) {
				// most likely executor shutdown request during latching, be good and bail
			    // quit the processing thread
			    break;	
			}

			// we have flipped the latch from the request to the thread waiting here, so send an outbound response
			// with the result of our work if a response is required
			if( DEBUG ) {
				System.out.println("WorkerRequestProcessor processing complete, queuing response:"+iori);
			}

			// And finally, send the package back up the line
			queueResponse((RemoteResponseInterface) iori);
			if( DEBUG ) {
				System.out.println("Response queued:"+iori);
			}
		} catch (Exception e1) {
			//if( !((e1.getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION ) {
				System.out.println("***Local processing EXCEPTION "+e1+", queuing fault to response");
				e1.printStackTrace();
			//}
			if(e1 instanceof NoSuchMethodException) {
				Exception e = new Exception();
				e.initCause(e1);
				iori.setObjectReturn(e);
				// clear the request queue
				requestQueue.clear();	
				// And finally, send the package back up the line
				queueResponse((RemoteResponseInterface) iori);
				continue;
			}
			// RocksDBException contains Status, which does not serialize, so trap and resend all possible permutations
			// Initial exception will always be InvocationTargetException from ServerInvokeMethod reflection call
			// depending on exception hierarchy, db exception may be wrapped one or 2 layers down
			if(e1.getCause().getCause() instanceof RocksDBException) {
				StringBuilder sb = new StringBuilder(e1.getCause().getCause().getMessage());
				sb.append(" RocksDBException Status:");
				sb.append(((RocksDBException)e1.getCause().getCause()).getStatus().getCodeString());
				Exception e = new Exception();
				e.initCause(new Exception(sb.toString()));
				iori.setObjectReturn(e);
				System.out.println("Queuing RocksDBException:"+sb.toString());
			} else {
				if(e1.getCause() instanceof RuntimeException && 
						e1.getCause().getCause() instanceof IOException &&
						e1.getCause().getCause().getCause() instanceof RocksDBException) {
					StringBuilder sb = new StringBuilder(e1.getCause().getCause().getCause().getMessage());
					sb.append(" RocksDBException Status:");
					sb.append(((RocksDBException)e1.getCause().getCause().getCause()).getStatus().getCodeString());
					Exception e = new Exception();
					e.initCause(new Exception(sb.toString()));
					iori.setObjectReturn(e);
					System.out.println("Queuing RocksDBException:"+sb.toString());
				} else {
					iori.setObjectReturn(e1);
				}
			}
			// clear the request queue
			requestQueue.clear();	
			// And finally, send the package back up the line
			queueResponse((RemoteResponseInterface) iori);
		}
	  } //shouldRun
	  synchronized(waitHalt) {
		  waitHalt.notify();
	  }
	  
	}

	public void queueResponse(RemoteResponseInterface iori) {
		responseQueue.queueResponse(iori);		
	}
}
