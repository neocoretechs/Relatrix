package com.neocoretechs.relatrix.parallel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class to manage thread resources throughout the application. Singleton. Fixed thread pool.
 * Attached a run completion method that decrements a countdown latch until all threads up to executionLimit
 * have completed, or use the standard Futures array for finer grained control.<p/>
 * Core and maximum pool sizes:<br/>
 * A ThreadPoolExecutor will automatically adjust the pool size (see getPoolSize())
 * according to the bounds set bycorePoolSize (see getCorePoolSize()) and
 * maximumPoolSize (see getMaximumPoolSize()).<p/>
 * When a new task is submitted in method execute(Runnable),and fewer than corePoolSize threads are running, 
 * a new thread is created to handle the request, even if other worker threads are idle. 
 * If there are more than corePoolSize but less than maximumPoolSize threads running,
 * a new thread will be created only if the queue is full. By setting corePoolSize and maximumPoolSize
 * the same, you create a fixed-size thread pool.<p/>
 * By setting maximumPoolSize to an essentially unbounded value such as Integer.MAX_VALUE, you allow the pool to accommodate an arbitrary
 * number of concurrent tasks. Most typically, core and maximum poolsizes are set only upon construction, but they may also be changed
 * dynamically using setCorePoolSize(int) and setMaximumPoolSize(int). <p/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public class SynchronizedFixedThreadPoolManager {
	int threadNum = 0;
    private static Map<String, FactoryThreadsLimit> executor = new ConcurrentHashMap<String, FactoryThreadsLimit>();
	public static volatile SynchronizedFixedThreadPoolManager threadPoolManager = null;
	private SynchronizedFixedThreadPoolManager() { }

	public static SynchronizedFixedThreadPoolManager getInstance() {
		if( threadPoolManager == null )
			synchronized(SynchronizedFixedThreadPoolManager.class) {
				if(threadPoolManager == null) {
					threadPoolManager = new SynchronizedFixedThreadPoolManager();
				}
			}
		return threadPoolManager;
	}
	
	/**
	 * Create an array of Executors that manage a cached thread pool for
	 * reading topics. One thread pool per topic to notify listeners of data ready
	 * @param maxThreads - corePoolSize
	 * @param executionLimit - maximumPoolSize set to Integer.MAX_VALUE for unbounded, otherwise fixed sized pool.
	 * @param threadGroupNames The topics for which thread groups are established
	 */
	public static void init(int maxThreads, int executionLimit, String[] threadGroupNames) {
		for(String tgn : threadGroupNames) {
			FactoryThreadsLimit ftl = executor.get(tgn);
			if( ftl != null ) {
				//ftl.exs.shutdownNow();
				//executor.remove(ftl.group);
				//System.out.println("Group "+tgn+" already initialized, use reinit to alter.");
				continue;
			}
			//executor.put(tgn, getInstance(maxExecution, executionLimit).new ExtendedExecutor(maxExecution, executionLimit, new ArrayBlockingQueue<Runnable>(executionLimit)));
			DaemonThreadFactory dtf = (getInstance().new DaemonThreadFactory(tgn));
			ExecutorService tpx = (getInstance().new ExtendedExecutor(maxThreads, executionLimit, new LinkedBlockingQueue<Runnable>(), dtf));
			executor.put(tgn, getInstance().new FactoryThreadsLimit(tgn, dtf, tpx, maxThreads, executionLimit));
			((ExtendedExecutor)tpx).prestartAllCoreThreads();
		}
	}
	/**
	 * Init or re-init a group. If group was previously initialized, a shutdown occurs before initialization of new instance.
	 * @param maxThreads - corePoolSize
	 * @param executionLimit - maximumPoolSize set to Integer.MAX_VALUE for unbounded, otherwise fixed sized pool.
	 * @param group The group name
	 */
	public void reinit(int maxThreads, int executionLimit, String group) {
		FactoryThreadsLimit ftl = executor.get(group);
		if( ftl != null ) {
			ftl.exs.shutdownNow();
			executor.remove(ftl.group);
		}
		DaemonThreadFactory dtf = new DaemonThreadFactory(group);
		ExecutorService tpx = new ExtendedExecutor(maxThreads, executionLimit, new LinkedBlockingQueue<Runnable>(), dtf);
		executor.put(group, new FactoryThreadsLimit(group, dtf, tpx, maxThreads, executionLimit));
		((ExtendedExecutor)tpx).prestartAllCoreThreads();
	}
	public void reinit(int maxThreads, int executionLimit) {
		reinit(maxThreads, executionLimit, "SYSTEMSYNC");
	}
	/**
	 * Initialize default group SYSTEMSYNC<p/>
	 * @param maxThreads CorePoolSize - number of threads to keep in pool, even if idle
	 * @param executionLimit MaximumPoolSize - 
	 */
	public void init(int maxThreads, int executionLimit) {
		FactoryThreadsLimit ftl = executor.get("SYSTEMSYNC");
		if( ftl != null ) {
			//ftl.exs.shutdownNow();
			//executor.remove(ftl.group);
			//System.out.println("Default group SYSTEMSYNC already initialized, use reinit to alter.");
			return;
		}
		DaemonThreadFactory dtf = new DaemonThreadFactory("SYSTEMSYNC");
		ExecutorService tpx = new ExtendedExecutor(maxThreads, executionLimit, new LinkedBlockingQueue<Runnable>(), dtf);
		executor.put("SYSTEMSYNC", new FactoryThreadsLimit("SYSTEMSYNC", dtf, tpx, maxThreads, executionLimit));
		((ExtendedExecutor)tpx).prestartAllCoreThreads();
	}
	/**
	 * Reset countdown latch for default SYSTEMSYNC group
	 * @param count Value to re-init latch with
	 */
	public static void resetLatch(int count) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get("SYSTEMSYNC"));
		ExecutorService exe = ftl.exs;
		ftl.maxExecution = count;
		// now reset latch
		((ExtendedExecutor)exe).latch = new CountDownLatch(ftl.maxExecution);
	}
	/**
	 * Reset countdown latch for named group
	 * @param count Value for constructor of new CountDownLatch
	 * @param group group for new latch
	 */
	public static void resetLatch(int count, String group) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
		ExecutorService exe = ftl.exs;
		ftl.maxExecution = count;
		// now reset latch
		((ExtendedExecutor)exe).latch = new CountDownLatch(ftl.maxExecution);
	}
	/**
	 * Wait for group to finish based on latch
	 * @param group
	 * @throws InterruptedException
	 */
	public static void waitForGroupToFinish(String group) throws InterruptedException {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
		ExecutorService exe = ftl.exs;
		((ExtendedExecutor)exe).getLatch().await();
		// now reset latch
		((ExtendedExecutor)exe).latch = new CountDownLatch(ftl.maxExecution);
	}
	/**
	 * Wait for default SYSTEMSYNC group to finish based on latch
	 * @throws InterruptedException
	 */
	public static void waitForGroupToFinish() throws InterruptedException {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get("SYSTEMSYNC"));
		ExecutorService exe = ftl.exs;
		((ExtendedExecutor)exe).getLatch().await();
		// now reset latch
		((ExtendedExecutor)exe).latch = new CountDownLatch(ftl.maxExecution);
	}
	/**
	 * Get task queue for group
	 * @param group
	 * @return
	 */
	public static BlockingQueue<Runnable> getQueue(String group) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
		ExecutorService exe = ftl.exs;
		return ((ExtendedExecutor)exe).getQueue();
		//return ((ThreadPoolExecutor)executor.get(group)).getQueue();
	}
	/**
	 * Get task queue for default group
	 * @return
	 */
	public static BlockingQueue<Runnable> getQueue() {
		//return ((ThreadPoolExecutor)executor.get("SYSTEMSYNC")).getQueue();
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get("SYSTEMSYNC"));
		ExecutorService exe = ftl.exs;
		return ((ExtendedExecutor)exe).getQueue();
	}
	/**
	 * Wait for notification of synchronized ExecutorService
	 * @param group
	 */
	public static void waitGroup(String group) {
		try {
			FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
			ExecutorService w = ftl.exs;
			//ExecutorService w = executor.get(group);
			synchronized(w) {
				w.wait();
			}
		} catch (InterruptedException e) {
		}
	}
	/**
	 * Timed wait for notification of group executor service
	 * @param group
	 * @param millis
	 */
	public static void waitGroup(String group, long millis) {
		try {
			FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
			ExecutorService w = ftl.exs;
			//ExecutorService w = executor.get(group);
			synchronized(w) {
				w.wait(millis);
			}
		} catch (InterruptedException e) {
		}
	}
	/**
	 * Wait for completion of submitted Future tasks
	 * @param futures
	 */
	public static void waitForCompletion(Future<?>[] futures) {
	    	//System.out.println("waitForCompletion on:"+futures.length);
	        int size = futures.length;
	        try {
	            for (int j = 0; j < size; j++) {
	                futures[j].get();
	            }
	        } catch (ExecutionException ex) {
	            ex.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	}
	/**
	 * Notify group waiting on ExecutorService
	 * @param group
	 */
	public static void notifyGroup(String group) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
		ExecutorService w = ftl.exs;
		//ExecutorService w = executor.get(group);
		synchronized(w) {
			w.notifyAll();
		}
	}
	/**
	 * Use ExecutorService to execute runnable
	 * @param r
	 * @param group ThreadGroup name in executor
	 */
	public static void spin(Runnable r, ThreadGroup group) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group.getName()));
		ExecutorService exe = ftl.exs;
	    /*executor.get(group.getName())*/exe.execute(r);
	}
	/**
	 * Use named ExecutorService to execute Runnable
	 * @param r
	 * @param group
	 */
	public static void spin(Runnable r, String group) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
		ExecutorService exe = ftl.exs;
	    /*executor.get(group)*/exe.execute(r);
	}
	/**
	 * Execute runnable in default group name SYSTEMSYNC
	 * @param r
	 */
	public static void spin(Runnable r) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get("SYSTEMSYNC"));
		ExecutorService exe = ftl.exs;
	    /*executor.get("SYSTEMSYNC")*/exe.execute(r);
	}
	/**
	 * Get the Future via executor submit for default SYSTEMSYNC
	 * @param r
	 * @return
	 */
    public static Future<?> submit(Runnable r) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get("SYSTEMSYNC"));
        return ftl.exs.submit(r);
    }
    /**
     * Get the Future via executor for named group
     * @param r
     * @param group
     * @return
     */
	public static Future<?> submit(Runnable r, String group) {
		FactoryThreadsLimit ftl = ((FactoryThreadsLimit)executor.get(group));
		ExecutorService exe = ftl.exs;
	    return exe.submit(r);
	}
    /**
     * Shutdown all threads
     */
	public static void shutdown() {
		//Collection<ExecutorService> ex = executor.values();
		Collection<FactoryThreadsLimit> ex = executor.values();
		for(/*ExecutorService*/FactoryThreadsLimit e : ex) {
			List<Runnable> spun = e.exs.shutdownNow();
			for(Runnable rs : spun) {
				System.out.println("Marked for Termination:"+rs.toString()+" "+e.toString());
			}
		}
	}
	
	class DaemonThreadFactory implements ThreadFactory {
		ThreadGroup threadGroup;
	
		public DaemonThreadFactory(String threadGroupName) {
			threadGroup = new ThreadGroup(threadGroupName);
		}	
		public ThreadGroup getThreadGroup() { return threadGroup; }		
	    public Thread newThread(Runnable r) {
	        Thread thread = new Thread(threadGroup, r, threadGroup.getName()+(++threadNum));
	        //thread.setDaemon(true);
	        return thread;
	    }
	}
	
	class ExtendedExecutor extends ThreadPoolExecutor {
		public CountDownLatch latch;
		public ExtendedExecutor(int maxThreads, int executionLimit, BlockingQueue<Runnable> threadQueue, DaemonThreadFactory dtf) {
			super(maxThreads, executionLimit, Long.MAX_VALUE, TimeUnit.DAYS, threadQueue, dtf );
			latch = new CountDownLatch(executionLimit);
		}
		public CountDownLatch getLatch() { return latch; }
		
		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
		  // regardless of errors, etc, count down the latch
			latch.countDown();
		}
		
	}
	
	class FactoryThreadsLimit {
		public DaemonThreadFactory dtf;
		public ExecutorService exs;
		public int totalThreads;
		public int maxExecution;
		public String group;
		public FactoryThreadsLimit(String group, DaemonThreadFactory dtf, ExecutorService exs, int totalThreads, int maxExecution ) {
			this.group = group;
			this.dtf = dtf;
			this.exs = exs;
			this.totalThreads = totalThreads;
			this.maxExecution = maxExecution;
		}
		@Override
		public boolean equals(Object o) {
			return group.equals(((FactoryThreadsLimit)o).group);
		}
		@Override
		public int hashCode() {
			return group.hashCode();
		}
	}
}
