package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DomainMapRange;

/**
* Find the set of objects in the relation via the specified predicate. 
* This variation accommodates findSet(object,"*|?","*|?") which returns a 1 or 2 element Comparable
* for each iteration wherein the object specified in the domain functions as the domain in the retrieved relationships.
* Legal permutations are
* [object],*,* [object],*,? [object],?,? [object],?,*
* [TemplateClass],*,* [TemplateClass],*,? [TemplateClass],?,? [TemplateClass],?,*
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105
*
*/
public class FindSetMode4 extends IteratorFactory {
	// mode 4
	char mop, rop;
	Object darg;
	short[] dmr_return = new short[4];
    public FindSetMode4(Object darg, char mop, char rop) { 	
    	this.darg = darg;
    	this.mop = mop;
    	this.rop = rop;
	    // see if its ? or * operator
    	dmr_return[1] = 0;
        // map
        // see if its ? or * operator
        dmr_return[2] = checkOp(mop);
        // range
        dmr_return[3] = checkOp(rop);
    }
    /**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		Morphism dmr = new DomainMapRange((Comparable)darg, null, null);
		return createRelatrixIterator(dmr);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixIterator(BigSackAdapter.getBigSackTransactionalTreeSet(tdmr), tdmr, dmr_return);
	}
}
