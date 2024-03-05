package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction;
/**
 * Implementation of the standard Stream interface which operates on Morphisms formed into a template in a transaction context.<p/>
 * to set the lower bound of the correct range search for the properly ordered set of Morphism subclasses;
 * The N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link Result} elements in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p/>
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Stated again, The critical element about retrieving relationships is to remember that the number of elements from each
 * RelatrixStream is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSetStream("*","?","*") we get back a  {@link Result} of one element. For findSetStream("?",object,"?") we
 * would get back a  {@link Result2} array, with each element of the array containing the relationship returned.<br/>
 * findSetStream("*","*","*") =  {@link Result1} containing identity of instance DomainMapRange<br/>
 * findSetStream("*","*",object) =  {@link Result1} identity of RangeDomainMap where 'object' is range<br/>
 * findSetStream("*",object,object) =  {@link Result1} identity of MapRangeDomain matching the 2 concrete objects<br/>
 * findSetStream(object,object,object) =  {@link Result1} identity of DomainMapRange matching 3 objects<br/>
 * and the findHeadSeStreamt and findSubSetStream work the same way.<p/>
 * findSet("?","?","?") =  {@link Result2} return all, for each element in the database.<br/>
 * findSet("?","?",object) =  {@link Result3} return all domain and map objects for a given range object<br/>
 * findSet("?","*","?") =  {@link Result3} return all elements of domain and range<br/>
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
    		//stream = RelatrixKVTransaction.findTailMapKVStream(xid,template);
    		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixIteratorTransaction(xid, template, dmr_return), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
    	} catch (IllegalArgumentException e) {
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
    		//stream = RelatrixKVTransaction.findTailMapKVStream(alias,xid,template);
       		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixIteratorTransaction(alias, xid, template, dmr_return), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
    	} catch (IllegalArgumentException e) {
    		throw new IOException(e);
    	}

    	if( DEBUG )
			System.out.println("RelatrixStreamTransaction alias:"+alias+" Id:"+xid+" stream: "+stream+" BASELINE:"+base);
    }
}
