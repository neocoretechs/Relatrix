package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Implementation of the standard Iterator interface which operates on Morphisms formed into a template
 * to set the lower bound of the correct range search for the properly ordered set of Morphism subclasses;
 * The N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p/>
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Stated again, The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a Comparable[] of one element. For findSet("?",object,"?") we
 * would get back a Comparable[2] array, with each element of the array containing the relationship returned.<br/>
 * findSet("*","*","*") = Comparable[1] containing identity in [0] of instance DomainMapRange <br/>
 * findSet("*","*",object) = Comparable[1] identity in [0] of RangeDomainMap where 'object' is range <br/>
 * findSet("*",object,object) = Comparable[1] identity in [0] of MapRangeDomain matching the 2 concrete objects <br/>
 * findSet(object,object,object) = Comparable[1] identity in [0] of DomainMapRange matching 3 objects <br/>
 * findSet("?","?","?") = Comparable[3] return all, for each element in the database.<br/>
 * findSet("?","?",object) = Comparable[2] return all domain and map objects for a given range object <br/>
 * findSet("?","*","?") = Comparable[2] return all elements of domain and range <br/>
 * etc.
 * <p/>
 * findHeadSet works in the same fashion but returns elements strictly less than the target element. <p/>
 * A special case is the subset, where the number of returned elements includes the target range object(s).<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017
 *
 */
public class RelatrixIteratorTransaction extends RelatrixIterator {
	private static boolean DEBUG = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixIteratorTransaction(String xid, Morphism template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKVTransaction.findTailMap(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}//(TailSetIterator) bts.tailSet(template);
    	if( iter.hasNext() ) {
			buffer = (Morphism) iter.next();
			if( !templateMatches(base, buffer, dmr_return) ) {
				buffer = null;
				needsIter = false;
			}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixIteratorTransaction Id:"+xid+" hasNext:"+iter.hasNext()+" "+needsIter+" "+buffer+" BASELINE:"+base);
    }
    
	
}
