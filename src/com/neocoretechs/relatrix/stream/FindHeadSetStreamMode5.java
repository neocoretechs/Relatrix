package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;


/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],*,[object] [object],?,[object]
* [TemplateClass],*,[TemplateClass] [TemplateClass],?,[TemplateClass]
* 
*/
public class FindHeadSetStreamMode5 extends FindSetStreamMode5 {

    public FindHeadSetStreamMode5(Object darg, char mop, Object rarg) { 	
    	super(darg, mop, rarg);
    }
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return (Stream<?>) new RelatrixHeadsetStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
