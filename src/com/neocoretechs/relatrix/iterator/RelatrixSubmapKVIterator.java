package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.RelatrixKV;

/**
 * Our main representable analog. Instances of this class deliver the set of key/value
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixSubmapKVIterator implements Iterator<Comparable> {
	protected Iterator iter;
	protected Comparable buffer;
    protected boolean needsIter = false;
    
    public RelatrixSubmapKVIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixSubmapKVIterator(Comparable template, Comparable template2) throws IOException {
    	try {
			iter = RelatrixKV.findSubMapKV(template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
    public RelatrixSubmapKVIterator(String alias, Comparable template, Comparable template2) throws IOException, NoSuchElementException {
    	try {
			iter = RelatrixKV.findSubMapKV(alias, template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public Comparable next() {
		if( buffer == null || needsIter) {
			buffer = (Comparable) iter.next();
			needsIter = false;
		}
		return (Comparable) iter.next();
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");
		
	}


}
