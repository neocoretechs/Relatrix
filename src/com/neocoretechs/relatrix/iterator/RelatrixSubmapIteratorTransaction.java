package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Our main representable analog. Instances of this class deliver the set of keys
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixSubmapIteratorTransaction extends RelatrixSubmapIterator {
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixSubmapIteratorTransaction(String xid, Comparable template, Comparable template2) throws IOException {
    	try {
			iter = RelatrixKVTransaction.findSubMap(xid, template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
    public RelatrixSubmapIteratorTransaction(String alias, String xid, Comparable template, Comparable template2) throws IOException, NoSuchElementException {
    	try {
			iter = RelatrixKVTransaction.findSubMap(alias, xid, template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
}
