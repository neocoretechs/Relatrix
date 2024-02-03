package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
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
public class RelatrixIterator implements Iterator<Comparable[]> {
	private static boolean DEBUG = true;
	protected Iterator iter;
    protected Morphism buffer = null;
    protected Morphism nextit = null;
    protected Morphism base;
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = true;
    protected boolean identity = false;
    
    public RelatrixIterator() {}
	/**
	 * Pass the array we use to indicate which values to return and element 0 counter
	 * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
	 * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
	 * @throws IOException
	 */
    public RelatrixIterator(Morphism template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKV.findTailMap(template);
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
			System.out.println("RelatrixIterator hasNext:"+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", template:"+base);
    }
	/**
	 * Pass the array we use to indicate which values to return and element 0 counter
	 * @param alias
	 * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
	 * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
	 * @throws IOException
	 */
	public RelatrixIterator(String alias, Morphism template, short[] dmr_return) throws IOException, NoSuchElementException {
	   	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKV.findTailMap(alias, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
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
			System.out.println("RelatrixIterator hasNext:"+iter.hasNext()+", needsIter"+needsIter+", buffer:"+buffer+", template:"+base);
	}
	
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.println("RelatrixIterator.hasNext() "+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		return needsIter;
	}

	
	@Override
	public Comparable[] next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
	    			System.out.println("RelatrixIterator.next() before iteration hasNext::"+iter.hasNext()+" needsIter:"+needsIter+", buffer:"+buffer+", nextit"+nextit);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
				nextit = (Morphism)iter.next();
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
			System.out.println("RelatrixIterator.next() template match after iteration hasNext:"+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		}
		return iterateDmr();
		
		} catch (IllegalAccessException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");
		
	}
	/**
	* iterate_dmr - return proper domain, map, or range
	* based on dmr_return values.  In dmr_return, element 0 is counter, 1-3 flags
	* value 0 means an object occupies that spot in the triple. 
	* value 1 indicates 'return a tuple - ?'.
	* value 2 represents a 'wildcard - *'.
	* Element 0 contains a running counter for the rest of the array 1-3.
	* These function as d,m,r return yes/no for each retrieved tuple and for concrete objects whether to compare tailset.
	* Also determine whether its identity, then just put it in return and iterate.
	* @return the next location to retrieve or null, the only time its null is when we exhaust the buffered tuples
	* @throws IOException 
	* @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	*/
	private Comparable[] iterateDmr() throws IllegalAccessException, IOException, ClassNotFoundException {
	    Comparable[] tuples = new Comparable[getReturnTuples(dmr_return)];
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples[0] = buffer;
	    } else {
	    	dmr_return[0] = 0;
	    	for(int i = 0; i < tuples.length; i++) {
	    		if( DEBUG ) {
	    			System.out.println("RelatrixIterator.iterateDmr() before iteration of "+i+" tuple:"+tuples[i]);
	    		}
	    		tuples[i] = buffer.iterate_dmr(dmr_return);
	    		if( DEBUG ) {
	    			System.out.println("RelatrixIterator.iterateDmr() after iteration of "+i+" tuple:"+tuples[i]);
	    		}
	    	}
	    }
		return tuples;
	}
	/**
	 * Return the number of tuple elements to be returned from specified query in each iteration
	 * @param dmr_return
	 * @return
	 */
	protected static short getReturnTuples(short[] dmr_return) {
		short cnt = 0;
		if( isIdentity(dmr_return) ) // return all relationship types, 1 tuple special case
			return 1;
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] == 1 ) ++cnt; // 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
		}
		return cnt;
	}
	/**
	 * Checks to see if our dmr_return array has any return tuple ? values, which = 1
	 * If the 0 element (the iterator over the array) is -1 or all elements are either 0 or 2 (object or wildcard)
	 * then we say its an identity, and we will return a 1 element Comparable array on each iteration.
	 * @param dmr_return
	 * @return
	 */
	protected static boolean isIdentity(short[] dmr_return) {
		if( dmr_return[0] == (-1) ) return true;
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] == 1 ) return false; // 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
		}
	    return true;
	}
	/**
	 * Determine if a range search has produced an element in range, since we deal with headSet, tailSets and subSets we have
	 * to check our iterator to keep it in range for concrete object keys.
	 * @param template
	 * @param record
	 * @param dmr_return
	 * @return
	 */
	protected static boolean templateMatches(Morphism template, Morphism record, short[] dmr_return) {
		if( DEBUG )
			System.out.println("RelatrixIterator.templateMatches template:"+template+" record:"+record+" dmr_return:"+dmr_return[0]+","+dmr_return[1]+","+dmr_return[2]+","+dmr_return[3]);
		if( dmr_return[1] == 0 && template.getDomain().compareTo(record.getDomain()) != 0 ) return false;
		if( dmr_return[2] == 0 && template.getMap().compareTo(record.getMap()) != 0 ) return false;
		if( dmr_return[3] == 0 && template.getRange().compareTo(record.getRange()) != 0) return false;
		return true;
	}
	

}
