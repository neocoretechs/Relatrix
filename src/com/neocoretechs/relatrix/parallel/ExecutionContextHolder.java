package com.neocoretechs.relatrix.parallel;

/**
 * Factory class that creates a new instance of a ScopedValue holding a {@link ParallelExecutionContext}
 * To facilitate access to an IndexResolver and any other data that the use wishes to inject into a parallel thread complex.
 * The idiomatic use is as follows:<br>
 * <code>
 * 		IndexResolver indexResolver = new IndexResolver(); <br>
 *		indexResolver.setLocal(); <br>
 *		ParallelExecutionContext pec = new ParallelExecutionContext(indexResolver, new ConcurrentHashMap<String,Object>());<br>
 *		ScopedValue.where(ExecutionContextHolder.CONTEXT, pec).run(() -> {});
 *</code>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 * 
 */
public class ExecutionContextHolder {
	 public static final ScopedValue<ParallelExecutionContext> CONTEXT = ScopedValue.newInstance();
}
