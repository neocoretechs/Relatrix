package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

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
			stream = RelatrixKVTransaction.findHeadMapStream(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( DEBUG )
			System.out.println("RelatrixHeadmapStreamTransaction Id:"+xid+" stream:"+stream);
    }
}
