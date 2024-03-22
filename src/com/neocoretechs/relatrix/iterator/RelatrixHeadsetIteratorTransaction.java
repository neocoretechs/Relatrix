package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
/**
 * Our main representable analog. Instances of this class deliver the set of identity {@link Morphism}s, or
 * deliver sets of compositions of morphisms representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link com.neocoretechs.relatrix.Result} elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the headset, or from beginning to the template element, is retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIterator} is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a Result of one element. For findSet("?",object,"?") we
 * would get back a Result2 instance, with each element of the Result containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class RelatrixHeadsetIteratorTransaction extends RelatrixHeadsetIterator {
	public static boolean DEBUG = false;
	String xid;

    public RelatrixHeadsetIteratorTransaction() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param templateo 
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadsetIteratorTransaction(String xid, Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	buffer.setTransactionId(xid);
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
 
    }
    
    public RelatrixHeadsetIteratorTransaction(String alias, String xid, Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.xid = xid;
    	this.alias = alias;
     	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	buffer.setTransactionId(xid);
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
  
    }
    
	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Result next() {
		return null;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	
	
	/**
	 * iterate_dmr - return proper domain, map, or range
	 * based on dmr_return values.  In dmr_return, value 0
	 * is iterator for ?,*.  1-3 BOOLean for d,m,r return yes/no
	 * @return the next location to retrieve or null, the only time its null is when we exhaust the buffered tuples
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	private Result iterateDmr() throws IllegalAccessException, IOException
	{
		++keysReturned;
	    Result tuples = RelatrixIterator.getReturnTuples(dmr_return);
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples.set(0, buffer);
	    	if(DEBUG)
				System.out.println("RelatrixHeadSetIterator iterateDmr returning identity tuples:"+tuples);
	    	return tuples;
	    }
	    dmr_return[0] = 0;
	    for(int i = 0; i < tuples.length(); i++)
	    	tuples.set(i, buffer.iterate_dmr(dmr_return));
		if(DEBUG)
			System.out.println("RelatrixHeadSetIterator iterateDmr returning tuples:"+tuples);
		return tuples;
	}
 
}
