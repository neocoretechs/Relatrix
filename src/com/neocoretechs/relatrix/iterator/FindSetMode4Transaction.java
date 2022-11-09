package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.DomainMapRange;

/**
* Find the set of objects in the relation via the specified predicate. 
* This variation accommodates findSet(object,"*|?","*|?") which returns a 1 or 2 element Comparable
* for each iteration wherein the object specified in the domain functions as the domain in the retrieved relationships.
* Legal permutations are:<br/>
* [object],*,* <br/>
* [object],*,?  <br/>
* [object],?,?  <br/>
* [object],?,* <br/>
* [TemplateClass],*,* <br/>
* [TemplateClass],*,? <br/>
* [TemplateClass],?,? <br/>
* [TemplateClass],?,* <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode4Transaction extends FindSetMode4 {
	// mode 4
	String xid;
    public FindSetMode4Transaction(String xid, Object darg, char mop, char rop) {
    	super(darg, mop, rop);
    	this.xid = xid;
    }
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}
