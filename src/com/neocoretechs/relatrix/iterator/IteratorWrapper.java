package com.neocoretechs.relatrix.iterator;

import java.util.Iterator;

import com.neocoretechs.relatrix.server.ServerMethod;
/**
 * The primary purpose of this class is to wrap a remote server-side iterator
 * such that the {@link ServerMethod} annotation can be used to mark it as remotely executable.<p>
 * In general, methods in the KV servers that produce a Stream or Iterator, will extract
 * the underlying iterator and wrap it in this class, and store it with a session ID
 * to be invoked by remote calls. The iterators and streams that come from external systems such
 * as RockSack cant be marked with ServerMethod, and so must be wrapped. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 */
public class IteratorWrapper implements Iterator<Object> {
	private Iterator<?> iterator;
	public IteratorWrapper(Iterator<?> iterator) {
		this.iterator = iterator;
	}
	
	@ServerMethod
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	@ServerMethod
	@Override
	public Object next() {
		return iterator.next();
	}

}
