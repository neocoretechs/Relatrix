package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;


/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixEntrysetIteratorTransaction extends RelatrixEntrysetIterator {
	private static boolean DEBUG = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetIteratorTransaction(TransactionId xid, Class c) throws IOException {
    	try {
			iter = RelatrixKVTransaction.entrySet(xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixEntrysetIterator Id:"+xid+" hasNext:"+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
    
    public RelatrixEntrysetIteratorTransaction(Alias alias, TransactionId xid, Class c) throws IOException, NoSuchElementException {
    	try {
			iter = RelatrixKVTransaction.entrySet(alias, xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
    	if( DEBUG )
			System.out.println("RelatrixEntrysetIterator Id:"+xid+" hasNext:"+iter.hasNext()+" "+needsIter+" "+buffer);
    	}
    }
    
}
