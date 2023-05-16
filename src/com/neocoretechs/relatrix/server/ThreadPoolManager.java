package com.neocoretechs.relatrix.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;


/**
 * Class to manage thread resources throughout the application. Singleton
 * Usage pattern is ThreadPoolManager.getInstance().spin([your Runnable])
 * ThreadPoolManager.shutdown() shuts all groups
 * ThreadPoolManager.shutdown([group]) shuts down named group
 * The default group is determined by constant DEFAULT_THREAD_POOL and is used when no arguments are provided in overloaded methods
 * additional groups may be named using init() and an array containing group names
 * @author Jonathan Groff Copyright 2014 NeoCoreTechs
 *
 */
public class ThreadPoolManager {
	private static final boolean DEBUG = false;
	private static String DEFAULT_THREAD_POOL = "SACKIO";
	private int threadNum = 0;
    private static Map<String, ExecutorService> executor = new HashMap<String, ExecutorService>();// = Executors.newCachedThreadPool(dtf);

	public static volatile ThreadPoolManager threadPoolManager = null;
	private ThreadPoolManager() { }
	
	public static ThreadPoolManager getInstance() {
		synchronized(ThreadPoolManager.class) {
			if( threadPoolManager == null ) {
				threadPoolManager = new ThreadPoolManager();
				// set up pool for system processes
				executor.put(DEFAULT_THREAD_POOL, Executors.newCachedThreadPool(threadPoolManager.new LocalThreadFactory(DEFAULT_THREAD_POOL)));
			}
		}
		return threadPoolManager;
	}
	/**
	 * Update the array of Executors that manage a cached thread pool for
	 * reading topics. One thread pool per topic to notify listeners of data ready.
	 * In each appropriate place, ThreadPoolmanager.init("group") may be called to add "group" to the
	 * list of known thread group names. The names are continually appended throughout the run.
	 * @param threadGroupNames The topics for which thread groups are established
	 */
	public static void init(String[] threadGroupNames, boolean overWrite) {
		for(String tgn : threadGroupNames) {
			if(!overWrite) {
				if( executor.containsKey(tgn))
					continue;
			}
			executor.put(tgn, Executors.newCachedThreadPool(getInstance().new LocalThreadFactory(tgn))); 
		}
	}
	
	public void waitGroup(String group) {
		try {
			ExecutorService w = executor.get(group);
			synchronized(w) {
				w.wait();
			}
		} catch (InterruptedException e) {
		}
	}
	
	public void waitGroup(String group, long millis) {
		try {
			ExecutorService w = executor.get(group);
			synchronized(w) {
				w.wait(millis);
			}
		} catch (InterruptedException e) {
		}
	}
	
	public void notifyGroup(String group) {
			ExecutorService w = executor.get(group);
			synchronized(w) {
				w.notifyAll();
			}
	}
	
	public Future<?> spin(Callable<Object> ioWorker, String ioWorkerNames) throws InterruptedException, ExecutionException {
		if(DEBUG)
			System.out.printf("%s.spin(%s,%s) executor=%s%n",this.getClass().getName(), ioWorker, ioWorkerNames, executor.get(ioWorkerNames));
	    return executor.get(ioWorkerNames).submit(ioWorker);
	}
	
	public void spin(Runnable r, String group) {
	    executor.get(group).execute(r);
	}
	
	public void spin(Runnable r) {
	    executor.get(DEFAULT_THREAD_POOL).execute(r);
	}
	
	public void shutdown() {
		Collection<ExecutorService> ex = executor.values();
		for(ExecutorService e : ex) {
			List<Runnable> spun = e.shutdownNow();
			for(Runnable rs : spun) {
				if( DEBUG )
				System.out.println("Marked for Termination:"+rs.toString()+" "+e.toString());
			}
		}
	}
	
	public void shutdown(String group) {
		ExecutorService ex = executor.get(group);
		List<Runnable> spun = ex.shutdownNow();
		for(Runnable rs : spun) {
			if( DEBUG )
				System.out.println("Marked for Termination:"+rs.toString()+" "+ex.toString());
		}
	}
	
	/**
     * Submits a Runnable task for execution and returns a Future representing
     * that task.
     *
     * @param task a Runnable task for execution
     *
     * @return a Future representing the task
     */
    public static Future<?> submit(Runnable task)
    {
        return executor.get(DEFAULT_THREAD_POOL).submit(task);
    }
    
    /**
     * Submits a Runnable task for execution and returns a Future representing
     * that task.
     * @param group The thread group to submit to
     * @param task a Runnable task for execution
     * @return a Future representing the task
     */
    public static Future<?> submit(String group, Runnable task)
    {
        return executor.get(group).submit(task);
    }

    /**
     * Waits for all threads to complete computation.
     *
     * @param futures array of Future objects
     */
    public static void waitForCompletion(Future<?>[] futures)
    {
    	if(DEBUG)
    		System.out.println("waitForCompletion on:"+futures.length);
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
    
	class LocalThreadFactory implements ThreadFactory {
		ThreadGroup threadGroup;
	
		public LocalThreadFactory(String threadGroupName) {
			threadGroup = new ThreadGroup(threadGroupName);
		}	
		public ThreadGroup getThreadGroup() { return threadGroup; }		
	    public Thread newThread(Runnable r) {
	        Thread thread = new Thread(threadGroup, r, threadGroup.getName()+(++threadNum));
	        //thread.setDaemon(true);
	        return thread;
	    }
	}
}
