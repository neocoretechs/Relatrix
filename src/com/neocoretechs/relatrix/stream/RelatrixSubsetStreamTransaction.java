package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Instances of this class deliver an stream of objects from a transaction context representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in the stream, since 1 full tuple element is streamed, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * element of a RelatrixStream is dependent on the number of "?" operators in a 'findSetStream'. For example,
 * if we declare findHeadSetStream("*","?","*") we get back a Comparable[] of one element. For findSetStream("?",object,"?") we
 * would get back a Comparable[2] array, with each element of the array containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015(iterator), 2021 (stream), 2022
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
    public RelatrixSubsetStreamTransaction(String xid, Morphism template, Morphism template2, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	identity = RelatrixStream.isIdentity(this.dmr_return);
    	try {
			stream = RelatrixKVTransaction.findSubMapStream(xid, template, template2);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
}
