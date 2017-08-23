package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.iterator.TailSetIterator;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.Morphism;
/**
 * Our main representable analog. Instances of this class deliver the set of identity morphisms, or
 * deliver sets of compositions of morphisms representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a Comparable[] of one element. For findSet("?",object,"?") we
 * would get back a Comparable[2] array, with each element of the array containing the relationship returned.<br/>
 * @author jg Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class RelatrixIterator implements Iterator<Comparable[]> {
	private static boolean DEBUG = false;
	TransactionalTreeSet deepStore;
	protected TailSetIterator iter;
    protected Morphism buffer = null;
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = false;
    protected boolean identity = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixIterator(TransactionalTreeSet bts, Morphism template, short[] dmr_return) throws IOException {
    	this.deepStore = bts;
    	this.dmr_return = dmr_return;
    	identity = isIdentity(this.dmr_return);
    	iter = (TailSetIterator) bts.tailSet(template);
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.println("RelatrixIterator.hasNext() "+iter.hasNext());
		return iter.hasNext();
	}

	
	@Override
	public Comparable[] next() {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
	    		try { // might catch a tuple not ready to be displayed
	    			System.out.println("RelatrixIterator.next() before iteration:"+buffer);
	       		} catch(Exception e) {
	    			System.out.println("RelatrixIterator.next() before iteration TUPLE DISPLAY FAILED WITH:"+e);
	    		}
			}
	    		
			buffer = (Morphism)iter.next();
			
			if( DEBUG ) {
	    		try { // might catch a tuple not ready to be displayed
	    			System.out.println("RelatrixIterator.next() after iteration:"+buffer);
	    		} catch(Exception e) {
	    			System.out.println("RelatrixIterator.next() after iteration TUPLE DISPLAY FAILED WITH:"+e);
	    		}
			}
			needsIter = false;
		}
		try {
			return iterateDmr();
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
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
	private Comparable[] iterateDmr() throws IllegalAccessException, IOException
	{
		int returnTupleCtr = 0;
	    Comparable[] tuples = new Comparable[getReturnTuples(dmr_return)];
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples[0] = buffer;
	    	needsIter = true;
	    	return tuples;
	    }
	    dmr_return[0] = 0;
	    for(int i = 0; i < tuples.length; i++) {
	    	if( DEBUG ) {
	    		try { // might catch a tuple not ready to be displayed
	    			System.out.println("RelatrixIterator.iterateDmr() before iteration of "+i+" tuple:"+tuples[i]);
	    		} catch(Exception e) {
	    			System.out.println("RelatrixIterator.iterateDmr() before iteration of "+i+" TUPLE DISPLAY FAILED WITH:"+e);
	    		}
	    	}
	    	
	    	tuples[i] = buffer.iterate_dmr(dmr_return);
	    	
	    	if( DEBUG ) {
	    		try { // might catch a tuple not ready to be displayed
	    			System.out.println("RelatrixIterator.iterateDmr() after iteration of "+i+" tuple:"+tuples[i]);
	    		} catch(Exception e) {
	    			System.out.println("RelatrixIterator.iterateDmr() after iteration of "+i+" TUPLE DISPLAY FAILED WITH:"+e);
	    		}
	    	}
	    }
		needsIter = true;
		return tuples;
	}
	/**
	 * Return the number of tuple elements to be returned from specified query in each iteration
	 * @param dmr_return
	 * @return
	 */
	protected static short getReturnTuples(short[] dmr_return) {
		short cnt = 0;
		if( dmr_return[0] == -1 ||  isIdentity(dmr_return) ) // return all relationship types, 1 tuple special case
			return 1;
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] == 1 ) ++cnt;
		}
		return cnt;
	}
	
	protected static boolean isIdentity(short[] dmr_return) {
	    return ( dmr_return[0] == (-1) || (dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0));
	}
	

}
