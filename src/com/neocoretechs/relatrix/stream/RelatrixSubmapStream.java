package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.iterator.SubSetIterator;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;
/**
 * Our main representable analog. Instances of this class deliver the set of keys
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * @author jg Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixSubmapStream implements Iterator<Comparable> {
	protected SubSetIterator iter;
	protected Comparable buffer;
    protected boolean needsIter = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixSubmapStream(TransactionalTreeMap bts, Comparable template, Comparable template2) throws IOException {
    	iter = (SubSetIterator) bts.subMap(template, template2);
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
