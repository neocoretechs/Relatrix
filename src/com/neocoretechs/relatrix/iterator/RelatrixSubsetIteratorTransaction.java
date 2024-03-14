package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified.<p/>
 * Our main representable analog. Instances of this class deliver the set of identity morphisms, or
 * deliver sets of compositions of morphisms representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a {@link Result1} of one element. For findSet("?",object,"?") we
 * would get back a {@link Result2}, with each element containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class RelatrixSubsetIteratorTransaction extends RelatrixSubsetIterator {
	String xid;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixSubsetIteratorTransaction(String xid, Morphism template, Morphism template2, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKVTransaction.findSubMap(xid, template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
    public RelatrixSubsetIteratorTransaction(String alias, String xid, Morphism template, Morphism template2, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.xid = xid;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKVTransaction.findSubMap(alias, xid, template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
}
