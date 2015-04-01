package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.DomainRangeMap;

/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],*,[object] [object],?,[object]
* [TemplateClass],*,[TemplateClass] [TemplateClass],?,[TemplateClass]
* 
*/
public class FindHeadSetMode5 extends FindSetMode5 {

    public FindHeadSetMode5(Object darg, char mop, Object rarg) { 	
    	super(darg, mop, rarg);
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(DMRStruc tdmr) throws IllegalAccessException, IOException {
		return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, dmr_return);
	}
}
