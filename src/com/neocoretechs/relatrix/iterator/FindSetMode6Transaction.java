package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainMapRangeTransaction;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;

/**
* Find the set of objects in the relation via the specified predicate. Mode 6 is for findSet(object,object,"?|*")
* where a 1 element Comparable array is returned from each iteration of the constructed iterator returned from findSet.
* The array has either the identity where the first 2 objects are components of the relationship for each iterated element of the 
* findSet, or the object functioning as the range (codomain) for each iteration where the first 2 objects are domain and
* map.
* Legal permutations are:<br/>
* [object],[object],* <br/>
* [object],[object],? <br/>
* [TemplateClass],[TemplateClass],* <br/>
* [TemplateClass],[TemplateClass],? <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode6Transaction extends FindSetMode6 {
	// mode 6
	String xid;
    public FindSetMode6Transaction(String xid, Object darg, Object marg, char rop) { 	
    	super(darg, marg, rop);
    	this.xid = xid;
    }
    
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    MorphismTransaction dmr = new DomainMapRangeTransaction(xid, (Comparable)darg, (Comparable)marg, null, true);
	    return createRelatrixIterator(dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}