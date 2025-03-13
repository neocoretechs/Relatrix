package com.neocoretechs.relatrix.iterator;

import java.util.Iterator;

import com.neocoretechs.relatrix.server.ServerMethod;

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
