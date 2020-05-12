package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.iterator.HeadSetIterator;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;
/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the upper bound of the correct range search for the properly ordered set of subclasses;
 * From beginning to template
 * @author jg Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixHeadmapIterator implements Iterator<Comparable> {
	private static boolean DEBUG = false;
	TransactionalTreeMap deepStore;
	protected HeadSetIterator iter;
    protected Comparable buffer = null;
    protected Comparable nextit = null;
    protected boolean needsIter = true;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadmapIterator(TransactionalTreeMap bts, Comparable template) throws IOException {
    	this.deepStore = bts;
    	iter = (HeadSetIterator) bts.headMap(template);
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixHeadmapIterator "+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.println("RelatrixHeadmapIterator.hasNext() "+iter.hasNext()+" "+needsIter+" "+buffer+" "+nextit);
		return needsIter;
	}

	@Override
	public Comparable next() {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
	    			System.out.println("RelatrixHeadmapIterator.next() before iteration:"+iter.hasNext()+" "+needsIter+" "+buffer+" "+nextit);
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
			System.out.println("RelatrixHeadmapIterator.next() template match after iteration:"+iter.hasNext()+" "+needsIter+" "+buffer+" "+nextit);
		}
		return nextit;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

}
