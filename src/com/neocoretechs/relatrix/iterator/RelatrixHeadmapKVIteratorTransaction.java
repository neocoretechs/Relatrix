package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the upper bound of the correct range search for the properly ordered set of subclasses;
 * From beginning to template
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixHeadmapKVIteratorTransaction extends RelatrixHeadmapKVIterator {
	private static boolean DEBUG = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadmapKVIteratorTransaction(String xid, Comparable template) throws IOException {
    	try {
			iter = RelatrixKVTransaction.findHeadMapKV(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixHeadmapKVIteratorTransaction Id:"+xid+" hasNext:"+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
    
    public RelatrixHeadmapKVIteratorTransaction(String alias, String xid, Comparable template) throws IOException, NoSuchElementException {
    	try {
			iter = RelatrixKVTransaction.findHeadMapKV(alias, xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixHeadmapKVIteratorTransaction Id:"+xid+" hasNext:"+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
}
