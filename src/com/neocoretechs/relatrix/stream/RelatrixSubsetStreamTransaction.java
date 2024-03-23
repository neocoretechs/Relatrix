package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator;
import com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction;
/**
 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
 * Instances of this class deliver an stream of objects from a transaction context representing the
 * N return tuple '?' elements of the query. If its an identity morphism {@link Morphism} of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in the stream, since 1 full tuple element is streamed, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * element of a RelatrixStream is dependent on the number of "?" operators in a 'findSetStream'. For example,
 * if we declare findHeadSetStream("*","?","*") we get back a {@link Result} of one element. For findSetStream("?",object,"?") we
 * would get back a {@link Result2}, with each element of the array containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015(iterator), 2021 (stream), 2022, 2024
 *
 */
public class RelatrixSubsetStreamTransaction<T> extends RelatrixSubsetStream<T> {

    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template The starting position of the retrieval
     * @param template2 The ending position of the retrieval
     * @param dmr_return The encoded tuple control array that here just tells us if we have an identity
     * @throws IOException 
     */
    public RelatrixSubsetStreamTransaction(String xid, Morphism template, Morphism templatez, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	try {
			//stream = RelatrixKVTransaction.findSubMapStream(xid, template, template2);
      		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixSubsetIteratorTransaction(xid, template, templateo, templatep, dmr_return), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template The starting position of the retrieval
     * @param template2 The ending position of the retrieval
     * @param dmr_return The encoded tuple control array that here just tells us if we have an identity
     * @throws IOException 
     */
    public RelatrixSubsetStreamTransaction(String alias, String xid, Morphism template, Morphism templatez, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	try {
			//stream = RelatrixKVTransaction.findSubMapStream(xid, template, template2);
      		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(new RelatrixSubsetIteratorTransaction(alias, xid, template, templateo, templatep, dmr_return), RelatrixKV.characteristics);
    		stream = StreamSupport.stream(spliterator, true);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
    }
}
