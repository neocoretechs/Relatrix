package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.MapRangeDomain;


/**
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],[object] ?,[object],[object]
* *,[TemplateClass],[TemplateClass] ?,[TemplateClass],[TemplateClass]
* 
*/
public class FindSetMode3 extends IteratorFactory {
	// mode 3
	char dop;
	Object marg,rarg;
	short[] dmr_return = new short[4];
    public FindSetMode3(char dop, Object marg, Object rarg) { 	
    	this.dop = dop;
    	this.rarg = rarg;
    	this.marg = marg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	// 'map' object
    	dmr_return[2] = 0;
    	//  'range'
    	dmr_return[3] = 0;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    DMRStruc dmr = new MapRangeDomain(null, (Comparable)marg, (Comparable)rarg);
	    return new RelatrixIterator(BigSackAdapter.getBigSackSet(dmr), dmr, dmr_return);
	}
}
