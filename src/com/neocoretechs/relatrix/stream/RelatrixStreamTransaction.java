package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Implementation of the standard Stream interface which operates on Morphisms formed into a template in a transaction context.<p/>
 * to set the lower bound of the correct range search for the properly ordered set of Morphism subclasses;
 * The N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p/>
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Stated again, The critical element about retrieving relationships is to remember that the number of elements from each
 * RelatrixStream is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSetStream("*","?","*") we get back a Comparable[] of one element. For findSetStream("?",object,"?") we
 * would get back a Comparable[2] array, with each element of the array containing the relationship returned.<br/>
 * findSetStream("*","*","*") = Comparable[1] containing identity in [0] of instance DomainMapRange<br/>
 * findSetStream("*","*",object) = Comparable[1] identity in [0] of RangeDomainMap where 'object' is range<br/>
 * findSetStream("*",object,object) = Comparable[1] identity in [0] of MapRangeDomain matching the 2 concrete objects<br/>
 * findSetStream(object,object,object) = Comparable[1] identity in [0] of DomainMapRange matching 3 objects<br/>
 * and the findHeadSeStreamt and findSubSetStream work the same way.<p/>
 * findSet("?","?","?") = Comparable[3] return all, for each element in the database.<br/>
 * findSet("?","?",object) = Comparable[2] return all domain and map objects for a given range object<br/>
 * findSet("?","*","?") = Comparable[2] return all elements of domain and range<br/>
 * etc.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017,2021,2022
 *
 */
public class RelatrixStreamTransaction<T> extends RelatrixStream<T> {
	private static boolean DEBUG = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixStreamTransaction(String xid, Morphism template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			stream = RelatrixKVTransaction.keySetStream(xid, template.getClass());
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}

    	if( DEBUG )
			System.out.println("RelatrixStreamTransaction Id:"+xid+" stream: "+stream+" BASELINE:"+base);
    }
    
    public RelatrixStreamTransaction(String alias, String xid, Morphism template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			stream = RelatrixKVTransaction.keySetStream(alias, xid, template.getClass());
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( DEBUG )
			System.out.println("RelatrixStreamTransaction alias:"+alias+" Id:"+xid+" stream: "+stream+" BASELINE:"+base);
    }
}
