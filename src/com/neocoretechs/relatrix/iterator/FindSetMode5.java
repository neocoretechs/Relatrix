package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.DomainRangeMap;

/**
* Find the set of objects in the relation via the specified predicate. 
* This mode is for (object,"?|*",object) returning 1 object or identity, but always a 1 element array of Comparable
* where the map is returned where it is a component of the relationship, or an identity DMRStruc subclass
* in the case of findSet(object,"*",object) where the identity is returned for each relationship where the objects match
* the specified two objects in the findSet.
* Legal permutations are
* [object],*,[object] [object],?,[object]
* [TemplateClass],*,[TemplateClass] [TemplateClass],?,[TemplateClass]
* @author jg Groff Copyright (C) NeoCoreTechs 2014,2105
* 
*/
public class FindSetMode5 extends IteratorFactory {
	// mode 5
	char mop;
	Object darg,rarg;
	short[] dmr_return = new short[4];
    public FindSetMode5(Object darg, char mop, Object rarg) { 	
    	this.darg = darg;
    	this.mop = mop;
    	this.rarg = rarg;
	    // see if its ? or * operator
    	dmr_return[1] = 0;
    	// 'map' object
    	// see if its ? or * operator
    	dmr_return[2] = checkOp(mop);
    	//  'range'
    	dmr_return[3] = 0;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		DMRStruc dmr = new DomainRangeMap((Comparable)darg, null, (Comparable)rarg);
		return createRelatrixIterator(dmr);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(DMRStruc tdmr)throws IllegalAccessException, IOException {
		return new RelatrixIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, dmr_return);
	}
}
