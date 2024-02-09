package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapDomainRangeTransaction;


/**
* Mode 2 find returns a set in map, domain, range order. The map value is matched against the constructor
* value. Mode 2 findSet("?|*",object,"?|*") returning a Comparable of 1 or 2 elements containing the identity
* or tuples from retrieval. For identity, if we specify findSet("*",object,"*") we get a Comparable of 1
* element containing a Morphism subclass.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetMode2Transaction extends FindSetMode2 {
	// mode 2
	String xid;
    public FindSetMode2Transaction(String xid, char dop, Object marg, char rop) { 	
    	super(dop, marg, rop);
    	this.xid = xid;
    }
    
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		MorphismTransaction dmr = new MapDomainRangeTransaction(true, xid, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixIterator(dmr);
	}
	
    @Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);	
	}
    
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
		MorphismTransaction dmr = new MapDomainRangeTransaction(true, alias, xid, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixIterator(alias, dmr);
	}
	
    @Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		return new RelatrixIteratorTransaction(alias, xid, tdmr, dmr_return);	
	}
}
