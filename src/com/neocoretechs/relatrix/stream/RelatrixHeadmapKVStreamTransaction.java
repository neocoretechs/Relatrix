package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

/**
 * Implementation of the standard Stream interface which operates on K/V keys in a transaction context.
 * to set the upper bound of the correct range search for the properly ordered set of subclasses;
 * From beginning to template
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021,2022
 *
 */
public class RelatrixHeadmapKVStreamTransaction<T,V> extends RelatrixHeadmapKVStream<T,V> {
	private static boolean DEBUG = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadmapKVStreamTransaction(String xid, Comparable template) throws IOException {
    	try {
			stream = RelatrixKVTransaction.findHeadMapKVStream(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( DEBUG )
			System.out.println("RelatrixHeadmapKVStreamTransaction Id:"+xid+" stream:"+stream);
    }
}
