package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys in a transaction context
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021,2022
 *
 */
public class RelatrixEntrysetStreamTransaction<T> extends RelatrixEntrysetStream<T> {
	private static boolean DEBUG = false;

    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStreamTransaction(TransactionId xid, Class c) throws IOException { 
		stream = new StreamHelper<T>(new RelatrixEntrysetIteratorTransaction(xid, c));
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStreamTransaction Id:"+xid+" stream:"+stream);
    }
    
    public RelatrixEntrysetStreamTransaction(TransactionId xid, Comparable c) throws IOException {
    	this(xid, c.getClass());
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStreamTransaction(Alias alias, TransactionId xid, Class c) throws IOException {
		stream = new StreamHelper<T>(new RelatrixEntrysetIteratorTransaction(alias, xid, c));
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStreamTransaction Id:"+xid+" stream:"+stream);
    }
    
    public RelatrixEntrysetStreamTransaction(Alias alias, TransactionId xid, Comparable c) throws IOException {
    	this(alias, xid, c.getClass());
    }
}
