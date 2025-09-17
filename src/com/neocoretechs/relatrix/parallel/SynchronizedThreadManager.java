package com.neocoretechs.relatrix.parallel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
	private static boolean DEBUG = false;
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
	
	public ExecutorService getExecutor() {
		return executor.get("SYSTEMSYNC").exs;
	}
	
	public ExecutorService getExecutor(String group) {
		ExtendedExecutor ftl = executor.get(group);
		if(ftl == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		return ftl.exs;
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
	 * Wait for completion of submitted Future tasks
	 * @param futures array of executing thread Futures
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
	public void shutdownAll() {
		Collection<ExtendedExecutor> ex = executor.values();
		for(ExtendedExecutor e : ex) {
			List<Runnable> spun = e.exs.shutdownNow();
			for(Runnable rs : spun) {
				if(DEBUG)
					System.out.println("Marked for Termination:"+rs.toString()+" "+e.toString());
			}
			try {
				e.waitForGroupToTerminate();
			} catch (InterruptedException e1) {}
		}
	}
	/**
     * Shutdown all threads for a group
     */
	public void shutdown(String group) {
		ExtendedExecutor ex = executor.get(group);
		if(ex == null)
			throw new RuntimeException("Executor Group "+group+" not initialized");
		List<Runnable> spun = ex.exs.shutdownNow();
		for(Runnable rs : spun) {
			if(DEBUG)
				System.out.println("Marked for Termination:"+rs.toString()+" "+ex.toString());
		}
		try {
			ex.waitForGroupToTerminate();
		} catch (InterruptedException e1) {}
	}
	/**
     * Shutdown default group
     */
	public void shutdown() {
		ExtendedExecutor ex = executor.get("SYSTEMSYNC");
		List<Runnable> spun = ex.exs.shutdownNow();
		for(Runnable rs : spun) {
			if(DEBUG)
				System.out.println("Marked for Termination:"+rs.toString()+" "+ex.toString());
		}
		try {
			ex.waitForGroupToTerminate();
		} catch (InterruptedException e1) {}
	}
	/**
	 * The ExtenedeExecutor doing the work.
	 */
	static class ExtendedExecutor {
		public ExecutorService exs;
		public String group;
		private long threadNum = 0L;
		public ExtendedExecutor(String group, ExecutorService exs) {
			this.group = group;
			this.exs = exs;
		}
		
		public void waitForGroupToTerminate() throws InterruptedException {
			this.exs.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}

		public Future<?> submitDaemon(ThreadGroup threadGroup, Runnable r) {
			Thread thread = new Thread(threadGroup, r, threadGroup.getName()+threadNum++);
			thread.setDaemon(true);
			Future<?> f = exs.submit(thread);
			return f;
		}
		public Future<?> submitDaemon(Runnable r) {
	        Thread thread = new Thread(r);
	        thread.setDaemon(true);
	        Future<?> f = exs.submit(thread);
	        return f;
		}
		public Future<?> submit(Runnable r) {
	        return exs.submit(r);
		}
		public Future<Object> submit(Callable<Object> r) {
			return exs.submit(r); // Submit the Callable to the executor
		}
		public void execute(Runnable r) {
	        exs.execute(r);
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

