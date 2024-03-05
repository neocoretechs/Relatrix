package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixHeadmapIterator;
import com.neocoretechs.relatrix.iterator.RelatrixHeadmapIteratorTransaction;

/**
 * Implementation of the standard stream interface in a transaction context.
 * to set the upper bound of the correct range search for the properly ordered set of subclasses;
 * From beginning to template
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021,2022
 *
 */
public class RelatrixHeadmapStreamTransaction<T> extends RelatrixHeadmapStream<T> {
	private static boolean DEBUG = false;

    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadmapStreamTransaction(String xid, Comparable template) throws IOException {
    	try {
			//stream = RelatrixKVTransaction.findHeadMapStream(xid, template);
    		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixHeadmapIteratorTransaction(xid, template), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
    	if( DEBUG )
			System.out.println("RelatrixHeadmapStreamTransaction Id:"+xid+" stream:"+stream);
    }
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadmapStreamTransaction(String alias, String xid, Comparable template) throws IOException {
    	try {
			//stream = RelatrixKVTransaction.findHeadMapStream(xid, template);
    		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixHeadmapIteratorTransaction(alias, xid, template), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
    	if( DEBUG )
			System.out.println("RelatrixHeadmapStreamTransaction Id:"+xid+" stream:"+stream);
    }
}
