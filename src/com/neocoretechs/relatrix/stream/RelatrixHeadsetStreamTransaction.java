package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Our main representable analog. Instances of this class work in a transaction context and
 * deliver sets of morphisms (object relationships). More plainly, an array is returned representing the
 * N return tuple '?' elements of the query. If its a wildcard query or query of three object keys (as in the *,*,* query
 * or the object,object,object query)
 * then 1 Comparable array element is returned in next(), that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the headset, or from beginning to the template element, is retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each
 * member of a RelatrixStream is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a Comparable[] of one element. For findSet("?",object,"?") we
 * would get back a Comparable[2] array, with each element of the array containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
 *
 */
public class RelatrixHeadsetStreamTransaction<T> extends RelatrixHeadsetStream<T> {
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid Transaction id
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadsetStreamTransaction(String xid, Morphism template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	identity = RelatrixStream.isIdentity(this.dmr_return);
    	try {
			stream = RelatrixKVTransaction.findHeadMapStream(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
}
