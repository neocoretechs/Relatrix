package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.iterator.RelatrixSubmapIteratorTransaction;

/**
 * Our main representable analog. Instances of this class delivers the set of keys from a transaction context.
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021,2022
 *
 */
public class RelatrixSubmapStreamTransaction<T> extends RelatrixSubmapStream<T> {

    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixSubmapStreamTransaction(String xid, Comparable template, Comparable template2) throws IOException {
    	try {
			//stream = RelatrixKVTransaction.findSubMapStream(xid, template, template2);
       		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixSubmapIteratorTransaction(xid, template, template2), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixSubmapStreamTransaction(String alias, String xid, Comparable template, Comparable template2) throws IOException {
    	try {
			//stream = RelatrixKVTransaction.findSubMapStream(xid, template, template2);
       		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixSubmapIteratorTransaction(alias, xid, template, template2), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
    }
}
