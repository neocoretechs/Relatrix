package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
/**
 * Implementation of the standard Iterator interface which operates on {@link com.neocoretechs.relatrix.Morphism}s formed into a template
 * to set the lower bound of the correct range search for the properly ordered set of Morphism subclasses;
 * The N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link com.neocoretechs.relatrix.Result} in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p/>
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Stated again, The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a  of one element. For findSet("?",object,"?") we
 * would get back a Result2, with each object of the Result hierarchy containing the relationship returned.<br/>
 * findSet("*","*","*") = {@link Result1} containing identity of instance DomainMapRange <br/>
 * findSet("*","*",object) =  {@link Result1} identity of RangeDomainMap where 'object' is range <br/>
 * findSet("*",object,object) = {@link Result1} identity of MapRangeDomain matching the 2 concrete objects <br/>
 * findSet(object,object,object) = {@link Result1} identity of DomainMapRange matching 3 objects <br/>
 * findSet("?","?","?") = {@link Result3} return all, for each element in the database.<br/>
 * findSet("?","?",object) = {@link Result2} return all domain and map objects for a given range object <br/>
 * findSet("?","*","?") = {@link Result2} return all elements of domain and range <br/>
 * etc.
 * <p/>
 * findHeadSet works in the same fashion but returns elements strictly less than the target element. <p/>
 * A special case is the subset, where the number of returned elements includes the target range object(s).<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017
 *
 */
public class RelatrixIterator implements Iterator<Result> {
	private static boolean DEBUG = false;
	protected Iterator<?> iter;
    protected Morphism buffer = null;
    protected Morphism nextit = null;
    protected Morphism base;
    protected short dmr_return[] = new short[4];
    protected Alias alias = null;

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
			iter = RelatrixKV.findTailMapKV(template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
    		Map.Entry me = (Entry) iter.next();
			buffer = (Morphism)me.getKey();
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
			System.out.println(this.toString());
    }
	/**
	 * Pass the array we use to indicate which values to return and element 0 counter
	 * @param alias
	 * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
	 * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
	 * @throws IOException
	 */
	public RelatrixIterator(Alias alias, Morphism template, short[] dmr_return) throws IOException, NoSuchElementException {
	   	this.dmr_return = dmr_return;
    	this.base = template;
    	this.alias = alias;
    	identity = isIdentity(this.dmr_return);
    	try {
			iter = RelatrixKV.findTailMapKV(alias, template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
      		Map.Entry me = (Entry) iter.next();
			buffer = (Morphism)me.getKey();
			buffer.setIdentity((DBKey)me.getValue());
			buffer.setAlias(alias);
			if( !templateMatches(base, buffer, dmr_return) ) {
				buffer = null;
				needsIter = false;
			}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println(this.toString());
	}
	
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.println(this.toString());
		return needsIter;
	}
	
	@Override
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
			System.out.println("RelatrixIterator.next() template match after iteration "+this.toString());
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
	private Result iterateDmr() throws IllegalAccessException, IOException, ClassNotFoundException {
	    Result tuples = getReturnTuples(dmr_return);
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples.set(0, buffer);
	    } else {
	    	dmr_return[0] = 0;
	    	for(int i = 0; i < tuples.length(); i++) {
	    		if( DEBUG ) {
	    			System.out.println("RelatrixIterator.iterateDmr() before iteration of "+i+" tuple:"+tuples.get(i));
	    		}
	    		tuples.set(i, buffer.iterate_dmr(dmr_return));
	    		if( DEBUG ) {
	    			System.out.println("RelatrixIterator.iterateDmr() after iteration of "+i+" tuple:"+tuples.get(i));
	    		}
	    	}
	    }
		return tuples;
	}
	/**
	 * Return the number of tuple elements to be returned from specified query in each iteration
	 * @param dmr_return For each element of the dmr_return array, element 0 is counter, for elements 1-3, 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *<br/>
	 * @return The {@link Result} object based on number of ? return tuples in dmr_return array and whether its considered an identity {@link Morphism}
	 */
	protected static Result getReturnTuples(short[] dmr_return) {
		short cnt = 0;
		if( isIdentity(dmr_return) ) // return all relationship types, 1 tuple special case
			return new Result1();
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] == 1 ) ++cnt; // 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
		}
		switch(cnt) {
			case 1:
				return new Result1();
			case 2:
				return new Result2();
			case 3:
				return new Result3();
		}
		throw new RuntimeException("Bad parameter to getReturnTuples:"+cnt);
	}
	/**
	 * Checks to see if our dmr_return array has any return tuple ? values.<br/>
	 * if any element of our dmr_return array is 1, we have return ? tuple present.<p/>
	 * For each element of the dmr_return array elements 1-3, 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *<br/>
	 * If the 0 element (the iterator over the array) is -1 or all elements are either 0 (object), or 2 (wildcard)
	 * then we say its an identity, and we will return a {@link Result1} on each iteration with a {@link com.neocoretechs.relatrix.DomainMapRange} relationship object.
	 * @param dmr_return our 4 element array of element 0 counter, and element 1-3 of 0 (object), 1 (? return tuple) or 2 (wildcard)
	 * @return true if element 0 is -1, or any element 1-3 is 1. We then consider it an identity {@link com.neocoretechs.relatrix.Morphism}
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
	 * @param template The template {@link com.neocoretechs.relatrix.Morphism} to match with the record
	 * @param record The record Morphism matched against the template
	 * @param dmr_return For each element of the array, 0 is counter,for elements 1-3, 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
	 * @return true if for each template domain, map, range key that is not null, dmr_return 1-3 is 0 for domain, map, range, and template key matches record key
	 */
	protected static boolean templateMatches(Morphism template, Morphism record, short[] dmr_return) {
		if( DEBUG )
			System.out.println("RelatrixIterator.templateMatches template:"+template+" record:"+record+" dmr_return:"+Arrays.toString(dmr_return));
		if(template.getDomainKey() != null)
			if( dmr_return[1] == 0 && template.getDomainKey().compareTo(record.getDomainKey()) != 0 ) return false;
		if(template.getMapKey() != null)
			if( dmr_return[2] == 0 && template.getMapKey().compareTo(record.getMapKey()) != 0 ) return false;
		if(template.getRangeKey() != null)
			if( dmr_return[3] == 0 && template.getRangeKey().compareTo(record.getRangeKey()) != 0) return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(" hasNext:");
	    sb.append(iter == null ? "iter NULL" : iter.hasNext());
		sb.append(" needsIter:");
		sb.append(needsIter);
		sb.append(" Identity:");
		sb.append(identity);
		sb.append(" buffer:");
		sb.append(buffer);
		sb.append(" base:");
		sb.append(base);
		sb.append(" nextit:");
		sb.append(nextit);
		sb.append(" dmr_return:");
		sb.append(Arrays.toString(dmr_return));
		return sb.toString();
	}
}
