package com.neocoretechs.relatrix.server;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.bigsack.io.IOWorker;
import com.neocoretechs.bigsack.io.request.IoRequestInterface;
import com.neocoretechs.bigsack.io.request.cluster.CompletionLatchInterface;
import com.neocoretechs.bigsack.io.request.cluster.IoResponse;

/**
 * Once requests from master are queued we extract them here and process them
 * This class functions as a generic threaded request processor for entries on a BlockingQueue of 
 * CompletionLatchInterface implementors managed by a DistributeWorkerResponseInterface implementation.
 * This request processor is spun up in conjunction with IO workers such as TCPMaster and UDPMaster. 
 * The intent is to separate the processing of requests, the maintenance of latches, etc from the communication
 * processing. In addition, increased parallelism can be achieved by separation of these tasks.
 * The WorkerRequestProcessors are responsible for setting the fields for the ioUnit and countdownlatch.
 * Essentially, the transient fields of outbound cluster requests, and the template fields in standalone
 * requests are filled in by methods invoked by this processor before 'process' is called on the request.
 * @see IoRequestInterface
 * Copyright (C) NeoCoreTechs 2014,2015
 * @author jg
 *
 */
public final class WorkerRequestProcessor implements Runnable {
	private static boolean DEBUG = false;
	private static int QUEUESIZE = 1024;
	private BlockingQueue<IoRequestInterface> requestQueue;
	private DistributedWorkerResponseInterface worker;
	private boolean shouldRun = true;
	public WorkerRequestProcessor(DistributedWorkerResponseInterface worker) {
		this.worker = worker;
		requestQueue = new ArrayBlockingQueue<IoRequestInterface>(QUEUESIZE, true);
	}
	
	public void stop() {
		shouldRun = false;
	}
	
	public BlockingQueue<IoRequestInterface> getQueue() { return requestQueue; }
	
	@Override
	public void run() {
	  while(shouldRun) {
		IoRequestInterface iori = null;
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
		((CompletionLatchInterface)iori).setCountDownLatch(cdl);
		if( DEBUG  ) {
			System.out.println("port:"+worker.getSlavePort()+" data:"+iori);
		}
		// tablespace set before request comes down
		
		try {
			iori.process();
			try {
				if( DEBUG )
					System.out.println("port:"+worker.getSlavePort()+" avaiting countdown latch...");
				cdl.await();
			} catch (InterruptedException e) {
				// most likely executor shutdown request during latching, be good and bail
			    // quit the processing thread
			    break;	
			}
			// we have flipped the latch from the request to the thread waiting here, so send an outbound response
			// with the result of our work if a response is required
			if( DEBUG ) {
				System.out.println("Local processing complete, queuing response to "+worker.getMasterPort());
			}
			IoResponse ioresp = new IoResponse(iori);
			// And finally, send the package back up the line
			worker.queueResponse(ioresp);
			if( DEBUG ) {
				System.out.println("Response queued:"+ioresp);
			}
		} catch (IOException e1) {
			if( DEBUG ) {
				System.out.println("***Local processing EXCEPTION "+e1+", queuing fault to "+worker.getMasterPort());
			}
			((CompletionLatchInterface)iori).setObjectReturn(e1);
			IoResponse ioresp = new IoResponse(iori);
			// And finally, send the package back up the line
			worker.queueResponse(ioresp);
			//if( DEBUG ) {
				System.out.println("***FAULT Response queued:"+ioresp);
			//}
		}
	  } //shouldRun
	  
	}

}
