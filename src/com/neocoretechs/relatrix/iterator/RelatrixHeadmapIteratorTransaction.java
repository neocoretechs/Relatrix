package com.neocoretechs.relatrix.iterator;

import java.io.IOException;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the upper bound of the correct range search for the properly ordered set of subclasses;
 * From beginning to template
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class RelatrixHeadmapIteratorTransaction extends RelatrixHeadmapIterator {
	private static boolean DEBUG = false;

    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadmapIteratorTransaction(String xid, Comparable template) throws IOException {
    	try {
			iter = RelatrixKVTransaction.findHeadMap(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixHeadmapIterator "+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
}
