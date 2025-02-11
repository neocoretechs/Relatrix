package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Implementation of the standard Iterator interface which operates on Morphisms formed into a template
 * to set the lower bound of the correct range search for the properly ordered set of Morphism subclasses;
 * The N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link Result} elements in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p/>
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Stated again, The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a Result1 of one element. For findSet("?",object,"?") we
 * would get back a {@link Result2}, with each element containing the relationship returned.<br/>
 * findSet("*","*","*") = {@link Result1} containing identity of instance DomainMapRange <br/>
 * findSet("*","*",object) = {@link Result1} identity of RangeDomainMap where 'object' is range <br/>
 * findSet("*",object,object) = {@link Result1} identity of MapRangeDomain matching the 2 concrete objects <br/>
 * findSet(object,object,object) = {@link Result1} identity of DomainMapRange matching 3 objects <br/>
 * findSet("?","?","?") = {@link Result3} return all, for each element in the database.<br/>
 * findSet("?","?",object) = {@link Result2} return all domain and map objects for a given range object <br/>
 * findSet("?","*","?") = {@link Result2}] return all elements of domain and range <br/>
 * etc.
 * <p/>
 * findHeadSet works in the same fashion but returns elements strictly less than the target element. <p/>
 * A special case is the subset, where the number of returned elements includes the target range object(s).<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017
 *
 */
public class RelatrixIteratorTransaction extends RelatrixIterator {
	private static boolean DEBUG = false;
	private TransactionId xid = null;
	/**
	 * Pass the array we use to indicate which values to return and element 0 counter
	 * @param xid the transaction id
	 * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
	 * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
	 * @throws IOException
	 */
    public RelatrixIteratorTransaction(TransactionId xid, Morphism template, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKVTransaction.findTailMapKV(xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
    		Map.Entry me = (Entry) iter.next();
			buffer = (Morphism) me.getKey();
			buffer.setTransactionId(xid);
			buffer.setIdentity((DBKey) me.getValue());
			if( !templateMatches(base, buffer, dmr_return) ) {
				buffer = null;
				needsIter = false;
			}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixIteratorTransaction Id:"+xid+" "+super.toString());
    }
	/**
	 * Pass the array we use to indicate which values to return and element 0 counter
	 * @param alias
	 * @param xid the transaction id
	 * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
	 * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
	 * @throws IOException
	 */
    public RelatrixIteratorTransaction(Alias alias, TransactionId xid, Morphism template, short[] dmr_return) throws IOException, NoSuchElementException {
      	this.alias = alias;
    	this.xid = xid;
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKVTransaction.findTailMapKV(alias, xid, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
       		Map.Entry me = (Entry) iter.next();
			buffer = (Morphism)me.getKey();
			buffer.setTransactionId(xid);
			buffer.setAlias(alias);
			buffer.setIdentity((DBKey) me.getValue());
			if( !templateMatches(base, buffer, dmr_return) ) {
				buffer = null;
				needsIter = false;
			}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixIteratorTransaction Id:"+xid+" "+super.toString());
    }
    
	@Override
	@ServerMethod
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
	    			System.out.println(this.toString());
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
				Map.Entry me = (Entry) iter.next();
				nextit = (Morphism)me.getKey();
				nextit.setIdentity((DBKey) me.getValue());
				nextit.setTransactionId(xid);
				if(alias != null)
					nextit.setAlias(alias);
				if( !templateMatches(base, nextit, dmr_return) ) {
					nextit = null;
					needsIter = false;
				}
			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUG ) {
			System.out.println("RelatrixIteratorTransaction.next() template match after iteration "+this.toString());
		}
		return iterateDmr();
		
		} catch (IllegalAccessException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
    @Override
    public String toString() {
    	return this.getClass().getName()+":"+super.toString();
    }
}
