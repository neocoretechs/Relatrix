package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainMapRangeTransaction;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;


/**
* Identity morphism retrieval.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017,2021
*/
public class FindSetMode7Transaction extends FindSetMode7 {
	// mode 7
	String xid;
    public FindSetMode7Transaction(String xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
    	this.xid = xid;
    }
    
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    MorphismTransaction dmr = new DomainMapRangeTransaction(xid, (Comparable)darg, (Comparable)marg, (Comparable)rarg, true);
	    return createRelatrixIterator(dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}
