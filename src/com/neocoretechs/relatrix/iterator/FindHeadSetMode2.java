package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015, 2021
* 
*/
public class FindHeadSetMode2 extends FindSetMode2 {
    public FindHeadSetMode2(char dop, Object marg, char rop) { 	
    	super(dop, marg, rop);
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackTransactionalTreeSet(tdmr), tdmr, dmr_return);
	}
}
