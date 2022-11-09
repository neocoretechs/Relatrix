package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RangeDomainMap;

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
	
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}
