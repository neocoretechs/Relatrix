package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are
* [object],[object],* [object],[object],?
* [TemplateClass],[TemplateClass],* [TemplateClass],[TemplateClass],?
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindHeadSetMode6 extends FindSetMode6 {
    public FindHeadSetMode6(Object darg, Object marg, char rop) { 	
    	super(darg,marg, rop);
    }

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
	    return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
