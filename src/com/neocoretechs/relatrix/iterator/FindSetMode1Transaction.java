package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeDomainMapTransaction;

/**
* Find the set of objects in the relation via the specified predicate. Mode 1 = findset("*|?","*|?",object)
* returning identity, 1 or 2 element Comparable array of tuples for each iteration of the retrieval.
* Legal permutations are:
* *,*,[object] <br/>
* *,?,[object] <br/>
* ?,?,[object] <br/>
* ?,*,[object] <br/>
* *,*,[TemplateClass] <br/>
* *,?,[TemplateClass] <br/>
* ?,?,[TemplateClass] <br/>
* ?,*,[TemplateClass] <br/>
* @author Jonthan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode1Transaction extends FindSetMode1 {
	// mode 1
	String xid;
    public FindSetMode1Transaction(String xid, char dop, char mop, Object rarg) { 	
    	super(dop, mop, rarg);
    	this.xid = xid;
    }
    
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    MorphismTransaction dmr = new RangeDomainMapTransaction(null, null, (Comparable)rarg, true);
	    return createRelatrixIterator(dmr);
	}
	
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}
