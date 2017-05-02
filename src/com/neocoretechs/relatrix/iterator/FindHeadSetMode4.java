package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;


/**
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],*,* [object],*,? [object],?,? [object],?,*
* [TemplateClass],*,* [TemplateClass],*,? [TemplateClass],?,? [TemplateClass],?,*
*
*/
public class FindHeadSetMode4 extends FindSetMode4 {
    public FindHeadSetMode4(Object darg, char mop, char rop) { 	
    	super(darg, mop, rop);
    }
    /**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, dmr_return);
	}
}
