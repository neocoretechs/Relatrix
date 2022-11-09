package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.RelatrixKV;


/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixEntrysetIterator implements Iterator<Comparable> {
	private static boolean DEBUG = false;
	protected Iterator iter;
    protected Comparable buffer = null;
    protected Comparable nextit = null;
    protected boolean needsIter = true;
    
    public RelatrixEntrysetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetIterator(Class c) throws IOException {
    	try {
			iter = RelatrixKV.entrySet(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixEntrysetIterator "+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
    
    public RelatrixEntrysetIterator(Comparable c) throws IOException {
    	this(c.getClass());
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.println("RelatrixEntrysetIterator.hasNext() "+iter.hasNext()+" "+needsIter+" "+buffer+" "+nextit);
		return needsIter;
	}

	@Override
	public Comparable next() {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
	    			System.out.println("RelatrixEntrysetIterator.next() before iteration:"+iter.hasNext()+" "+needsIter+" "+buffer+" "+nextit);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
				nextit = (Comparable)iter.next();
			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUG ) {
			System.out.println("RelatrixEntrysetIterator.next() template match after iteration:"+iter.hasNext()+" "+needsIter+" "+buffer+" "+nextit);
		}
		return nextit;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

}
