package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

/**
 * Implementation of the standard Stream interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021
 *
 */
public class RelatrixKeyStreamTransaction<T> extends RelatrixKeyStream<T> {
	private static boolean DEBUG = false;
  
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixKeyStreamTransaction(String xid, Comparable template) throws IOException {
    	try {
			stream = RelatrixKVTransaction.findTailMapKVStream(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( DEBUG )
			System.out.println("RelatrixKeyStreamTransaction Id:"+xid+" stream:"+stream);
    }
}
