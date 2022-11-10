package com.neocoretechs.relatrix.iterator;

import java.io.IOException;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixKeyIteratorTransaction extends RelatrixKeyIterator {
	private static boolean DEBUG = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixKeyIteratorTransaction(String xid, Comparable template) throws IOException {
    	try {
			iter = RelatrixKVTransaction.findTailMap(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixKeyIteratorTransaction Id:"+xid+" hasNext: "+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
}
