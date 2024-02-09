package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;
import com.neocoretechs.relatrix.DomainRangeMapTransaction;

/**
* Find the set of objects in the relation via the specified predicate. 
* This mode is for (object,"?|*",object) returning 1 object or identity, but always a 1 element array of Comparable
* where the map is returned where it is a component of the relationship, or an identity Morphism subclass
* in the case of findSet(object,"*",object) where the identity is returned for each relationship where the objects match
* the specified two objects in the findSet.
* Legal permutations are:<br/>
* [object],*,[object] <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetMode5Transaction extends FindSetMode5 {
	// mode 5
	String xid;
    public FindSetMode5Transaction(String xid, Object darg, char mop, Object rarg) { 	
    	super(darg, mop, rarg);
    	this.xid = xid;
    }
    
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		MorphismTransaction dmr = new DomainRangeMapTransaction(true, xid, (Comparable)darg, null, (Comparable)rarg);
		return createRelatrixIterator(dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
		return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
	
	  /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
		MorphismTransaction dmr = new DomainRangeMapTransaction(true, alias, xid, (Comparable)darg, null, (Comparable)rarg);
		return createRelatrixIterator(alias, dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr)throws IllegalAccessException, IOException, NoSuchElementException {
		return new RelatrixIteratorTransaction(alias, xid, tdmr, dmr_return);
	}
}
