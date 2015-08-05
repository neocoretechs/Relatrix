package com.neocoretechs.relatrix.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;

/**
 * Once requests from master are queued we extract them here and process them
 * This class functions as a generic threaded request processor for entries on a BlockingQueue of 
 * RemoteCompletionInterface implementors 
 * This request processor is spun up in conjunction with IO workers such as TCPMaster. 
 * The intent is to separate the processing of requests, the maintenance of latches, etc from the communication
 * processing. In addition, increased parallelism can be achieved by separation of these tasks.
 * The WorkerRequestProcessors are responsible for setting the fields for the countdownlatch.
 * Copyright (C) NeoCoreTechs 2014,2015
 * @author jg
 *
 */
public final class WorkerRequestProcessor implements Runnable {
	private static boolean DEBUG = false;
	private static int QUEUESIZE = 1024;
	private BlockingQueue<RemoteCompletionInterface> requestQueue;
	private TCPWorker responseQueue;

	private boolean shouldRun = true;
	public WorkerRequestProcessor(TCPWorker tcpworker) {
		this.responseQueue = tcpworker;
		this.requestQueue = new ArrayBlockingQueue<RemoteCompletionInterface>(QUEUESIZE, true);
	}
	
	public void stop() {
		shouldRun = false;
	}
	
	public BlockingQueue<RemoteCompletionInterface> getQueue() { return requestQueue; }
	
	@Override
	public void run() {
	  while(shouldRun) {
		RemoteCompletionInterface iori = null;
		RemoteResponseInterface irri = null;
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
			// invoke the designated method on the server, return any result
			//
			irri = RelatrixServer.process(iori);
			
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
				System.out.println("WorkerRequestProcessor processing complete, queuing response:"+irri);
			}

			// And finally, send the package back up the line
			queueResponse(irri);
			if( DEBUG ) {
				System.out.println("Response queued:"+irri);
			}
		} catch (Exception e1) {
			
			System.out.println("***Local processing EXCEPTION "+e1+", queuing fault to response");
			e1.printStackTrace();
			
			iori.setObjectReturn(e1);
			
			// And finally, send the package back up the line
			queueResponse((RemoteResponseInterface) iori);
			
		}
	  } //shouldRun
	  
	}

	public void queueResponse(RemoteResponseInterface iori) {
		responseQueue.queueResponse(iori);
		
	}


}
