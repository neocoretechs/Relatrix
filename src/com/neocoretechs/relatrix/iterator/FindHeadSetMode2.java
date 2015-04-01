package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
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
	public Iterator<?> createRelatrixIterator(DMRStruc tdmr) throws IllegalAccessException, IOException {
		return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, dmr_return);
	}
}
