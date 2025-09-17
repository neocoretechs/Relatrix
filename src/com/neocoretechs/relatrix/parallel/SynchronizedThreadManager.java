package com.neocoretechs.relatrix.parallel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class to manage thread resources throughout the application. Singleton. Unbounded virtual threads.
 * Attached a run completion method that decrements a countdown latch when all threads in a group finish.
 * have completed, or use the standard Futures array for finer grained control.<p/>
 * When a new task is submitted in method execute(Runnable),
 * a new thread is created to handle the request, even if other worker threads are idle. 
 * Threads are essentially unbounded number of concurrent tasks.<p/>
 * An ExtendedExecutor maintains a queue of running threads. Each new thread group is managed by a new ExtendedExecutor.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class SynchronizedThreadManager {
	int threadNum = 0;
    private static Map<String, ExtendedExecutor> executor = new ConcurrentHashMap<String, ExtendedExecutor>();
	public static volatile SynchronizedThreadManager threadManager = null;
	private SynchronizedThreadManager() { }

	public static SynchronizedThreadManager getInstance() {
		if( threadManager == null )
			synchronized(SynchronizedThreadManager.class) {
				if(threadManager == null) {
					threadManager = new SynchronizedThreadManager();
					threadManager.init();
				}
			}
		return threadManager;
	}
	
	/**
	 * Create an array of Executors that manage threads for
	 * reading topics. One thread pool per topic to notify listeners of data ready
	 * @param threadGroupNames The topics for which thread groups are established
	 */
	public void init(String[] threadGroupNames) {
		for(String tgn : threadGroupNames) {
			ExtendedExecutor ftl = executor.get(tgn);
			if( ftl != null ) {
				//ftl.exs.shutdownNow();
				//executor.remove(ftl.group);
				//System.out.println("Group "+tgn+" already initialized, use reinit to alter.");
				continue;
			}
			ExecutorService tpx = Executors.newVirtualThreadPerTaskExecutor();
			executor.put(tgn, new ExtendedExecutor(tgn,tpx));
		}
	}
	/**
	 * Create an array of Executors that manage threads for
	 * reading topics. One thread pool per topic to notify listeners of data ready
	 * @param threadGroupNames The topics for which thread groups are established
	 * @param overWrite if a name provided in the threadGroupNames matches a name in the cached thread pool map and overWrite is true, value is overwritten
	 */
	public void init(String[] threadGroupNames, boolean reInit) {
		for(String tgn : threadGroupNames) {
			ExtendedExecutor ftl = executor.get(tgn);
			if( ftl != null ) {
				if(reInit) {
					ftl.exs.shutdownNow();
					executor.remove(tgn);
				} else
					continue;
			}
			ExecutorService tpx = Executors.newVirtualThreadPerTaskExecutor();
			executor.put(tgn, new ExtendedExecutor(tgn,tpx));
		}
	}
	/**
	 * Init or re-init a group. If group was previously initialized, a shutdown occurs before initialization of new instance.
	 * @param group The group name
	 */
	public void reinit(String group) {
		ExtendedExecutor ftl = executor.get(group);
		if( ftl != null ) {
			ftl.exs.shutdownNow();
			executor.remove(ftl.group);
		}
		ExecutorService tpx = Executors.newVirtualThreadPerTaskExecutor();
		executor.put(group, new ExtendedExecutor(group, tpx));
	}
	public void reinit(int maxThreads, int executionLimit) {
		reinit("SYSTEMSYNC");
	}
	/**
	 * Initialize default group SYSTEMSYNC<p/>
	 */
	public void init() {
		ExtendedExecutor ftl = executor.get("SYSTEMSYNC");
		if( ftl != null ) {
			//ftl.exs.shutdownNow();
			//executor.remove(ftl.group);
			//System.out.println("Default group SYSTEMSYNC already initialized, use reinit to alter.");
			return;
		}
		ExecutorService tpx = Executors.newVirtualThreadPerTaskExecutor();
		executor.put("SYSTEMSYNC", new ExtendedExecutor("SYSTEMSYNC", tpx));
	}
	/**
	 * Reset countdown latch for default SYSTEMSYNC group
	 */
	public void resetLatch() {
		ExtendedExecutor exe = ((ExtendedExecutor)executor.get("SYSTEMSYNC"));
		// now reset latch
		exe.latch = new CountDownLatch(1);
	}
	/**
	 * Reset countdown latch for named group
	 * @param group group for new latch
	 */
	public void resetLatch(String group) {
		ExtendedExecutor exe = ((ExtendedExecutor)executor.get(group));
		// now reset latch
		if(exe != null)
			exe.latch = new CountDownLatch(1);
		else
			throw new RuntimeException("Executor Group "+group+" not initialized");
	}
	/**
	 * Wait for group to finish based on latch
	 * @param group The group name
	 * @throws InterruptedException
	 */
	public void waitForGroupToFinish(String group) throws InterruptedException {
		ExtendedExecutor exe = executor.get(group);
		if(exe != null)
			exe.waitForGroupToFinish();
		else
			throw new RuntimeException("Executor Group "+group+" not initialized");
	}
	/**
	 * Wait for default SYSTEMSYNC group to finish based on latch
	 * @throws InterruptedException
	 */
	public void waitForGroupToFinish() throws InterruptedException {
		ExtendedExecutor exe = executor.get("SYSTEMSYNC");
		exe.waitForGroupToFinish();
	}
	/**
	 * Get task queue for group
	 * @param group The group name
	 * @return Queue of Futures executing
	 */
	public BlockingQueue<Future> getQueue(String group) {
		ExtendedExecutor ftl = executor.get(group);
		if(ftl == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		return ftl.getQueue();
	}
	/**
	 * Get task queue for default group
	 * @return
	 */
	public BlockingQueue<Future> getQueue() {
		ExtendedExecutor ftl = executor.get("SYSTEMSYNC");
		return ftl.getQueue();
	}
	/**
	 * Wait for notification of synchronized ExecutorService
	 * @param group
	 */
	public void waitGroup(String group) {
		ExtendedExecutor exe = executor.get(group);
		if(exe == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		exe.waitForGroupToFinish();
	}
	/**
	 * Timed wait for notification of group executor service, each executing thread is
	 * waited upon for the given time in milliseconds.
	 * @param group
	 * @param millis
	 * @throws TimeoutException 
	 */
	public void waitGroup(String group, long millis) throws TimeoutException {
		ExtendedExecutor exe = executor.get(group);
		if(exe == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		exe.waitForGroupToFinish(millis);
	}
	/**
	 * Wait for completion of submitted Future tasks
	 * @param futures array of executing thread Futures
	 */
	public void waitForCompletion(Future<?>[] futures) {
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
	 * @param group The group name
	 */
	public void notifyGroup(String group) {
		ExtendedExecutor exe = executor.get(group);
		if(exe == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		synchronized(exe) {
			exe.notifyAll();
		}
	}
	/**
	 * Use ExtendedExecutor ExecutorService to execute runnable
	 * @param r The runnable thread
	 * @param group ThreadGroup name in executor
	 */
	public void spin(Runnable r, ThreadGroup group) {
		ExtendedExecutor ftl = executor.get(group.getName());
		if(ftl == null)
			throw new RuntimeException("Executor Group "+group.getName()+" not initialized");
	    ftl.execute(r);
	}
	/**
	 * Use named ExtendedExecutor ExecutorService to execute Runnable
	 * @param r the Runnable
	 * @param group the Group
	 */
	public void spin(Runnable r, String group) {
		ExtendedExecutor ftl = executor.get(group);
		if(ftl == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
	    ftl.execute(r);
	}
	/**
	 * Execute runnable in default group name SYSTEMSYNC
	 * @param r the Runnable
	 */
	public void spin(Runnable r) {
		ExtendedExecutor ftl = executor.get("SYSTEMSYNC");
	    ftl.execute(r);
	}
	/**
	 * Get the Future via executor submit for default SYSTEMSYNC
	 * @param r The Runnable
	 * @return the Future executing thread
	 */
    public Future<?> submit(Runnable r) {
		ExtendedExecutor ftl = executor.get("SYSTEMSYNC");
        return ftl.submit(r);
    }
    /**
     * Get the Future via executor for named group
     * @param r The Runnable
     * @param group The group name
     * @return
     */
	public Future<?> submit(Runnable r, String group) {
		ExtendedExecutor ftl = executor.get(group);
		if(ftl == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
	    return ftl.submit(r);
	}
	/**
	 * Get the Future via executor submit for default SYSTEMSYNC
	 * @param r The Callable
	 * @return The Future<Object>
	 */
	public Future<Object> submit(Callable<Object> r) {
		ExtendedExecutor ftl = executor.get("SYSTEMSYNC");
        return ftl.submit(r);
    }
    /**
     * Get the Future via executor for named group
     * @param r The Callable<Object>
     * @param group the group name
     * @return
     */
	public Future<Object> submit(Callable<Object> r, String group) {
		ExtendedExecutor ftl = executor.get(group);
		if(ftl == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
	    return ftl.submit(r);
	}
    /**
     * Shutdown all threads
     */
	public void shutdown() {
		Collection<ExtendedExecutor> ex = executor.values();
		for(ExtendedExecutor e : ex) {
			List<Runnable> spun = e.exs.shutdownNow();
			for(Runnable rs : spun) {
				System.out.println("Marked for Termination:"+rs.toString()+" "+e.toString());
			}
		}
	}
	   /**
     * Shutdown all threads
     */
	public void shutdown(String group) {
		ExtendedExecutor ex = executor.get(group);
		if(ex == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		List<Runnable> spun = ex.exs.shutdownNow();
		for(Runnable rs : spun) {
				System.out.println("Marked for Termination:"+rs.toString()+" "+ex.toString());
		}
	}
	/**
	 * The ExtenedeExecutor doing the work. Maintains a LinkedBlockingDeque of Futures of running threads by group name.
	 */
	static class ExtendedExecutor {
		public ExecutorService exs;
		public String group;
		public CountDownLatch latch;
		LinkedBlockingDeque<Future> queue = new LinkedBlockingDeque<Future>();
		public ExtendedExecutor(String group, ExecutorService exs) {
			this.group = group;
			this.exs = exs;
			this.latch = new CountDownLatch(1);
		}
		public CountDownLatch getLatch() { return latch; }
		public BlockingQueue<Future> getQueue() { return queue; }
		
		public void waitForGroupToTerminate() throws InterruptedException {
			this.exs.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			this.latch.countDown();
			this.latch = new CountDownLatch(1);
		}
		public void waitForGroupToFinish() {
			Iterator<?> it = queue.iterator();
			try {
				while(it.hasNext()) {
					Future<?> f = (Future<?>) it.next();
					f.get();
				}
			} catch (ExecutionException | InterruptedException ex) {
				ex.printStackTrace();
			}
			this.latch.countDown();
			this.latch = new CountDownLatch(1);
			this.notifyAll();
		}
		public void waitForGroupToFinish(long millis) throws TimeoutException {
			Iterator<?> it = queue.iterator();
			try {
				while(it.hasNext()) {
					Future<?> f = (Future<?>) it.next();
					f.get(millis, TimeUnit.MILLISECONDS);
				}
			} catch (ExecutionException | InterruptedException ex) {
				ex.printStackTrace();
			}
			this.latch.countDown();
			this.latch = new CountDownLatch(1);
			this.notifyAll();
		}
		public Future<?> submitDaemon(ThreadGroup threadGroup, Runnable r) {
			Thread thread = new Thread(threadGroup, r, threadGroup.getName()+queue.size()+1);
			thread.setDaemon(true);
			Future<?> f = exs.submit(thread);
			queue.add(f);
			return f;
		}
		public Future<?> submitDaemon(Runnable r) {
	        Thread thread = new Thread(r);
	        thread.setDaemon(true);
	        Future<?> f = exs.submit(thread);
	        queue.add(f);
	        return f;
		}
		public Future<?> submit(Runnable r) {
	        Future<?> f = exs.submit(r);
	        queue.add(f);
	        return f;
		}
		public Future<Object> submit(Callable<Object> r) {
			Runnable runnable = () -> {
				try {
					r.call(); // Call the Callable's method
				} catch (Exception e) {
					e.printStackTrace(); // Handle exceptions
				}
			};
			Future f = exs.submit(r); // Submit the Callable to the executor
			queue.add(f);
			return f;
		}
		public void execute(Runnable r) {
	        Future f = exs.submit(r);
	        queue.add(f);
		}
		@Override
		public boolean equals(Object o) {
			return group.equals(((ExtendedExecutor)o).group);
		}
		@Override
		public int hashCode() {
			return group.hashCode();
		}
	}

}

