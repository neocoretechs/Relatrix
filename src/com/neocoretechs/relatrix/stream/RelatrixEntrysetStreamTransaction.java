package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator;
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
    public RelatrixEntrysetStreamTransaction(String xid, Class c) throws IOException {
    	//stream = RelatrixKVTransaction.entrySetStream(xid, c);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixEntrysetIteratorTransaction(xid, c), RelatrixKV.characteristics);
		stream = StreamSupport.stream(spliterator, true);
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStreamTransaction Id:"+xid+" stream:"+stream);
    }
    
    public RelatrixEntrysetStreamTransaction(String xid, Comparable c) throws IOException {
    	this(xid, c.getClass());
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStreamTransaction(String alias, String xid, Class c) throws IOException {
    	//stream = RelatrixKVTransaction.entrySetStream(xid, c);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixEntrysetIteratorTransaction(alias, xid, c), RelatrixKV.characteristics);
		stream = StreamSupport.stream(spliterator, true);
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStreamTransaction Id:"+xid+" stream:"+stream);
    }
    
    public RelatrixEntrysetStreamTransaction(String alias, String xid, Comparable c) throws IOException {
    	this(alias, xid, c.getClass());
    }
}
