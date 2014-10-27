package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.RangeDomainMap;

/**
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,*,[object] *,?,[object] ?,?,[object] ?,*,[object]
* *,*,[TemplateClass] *,?,[TemplateClass] ?,?,[TemplateClass] ?,*,[TemplateClass]
*
*/
public class FindSetMode1 extends IteratorFactory {
	// mode 1
	char dop,mop;
	Object rarg;
	short[] dmr_return = new short[4];
    public FindSetMode1(char dop, char mop, Object rarg) { 	
    	this.dop = dop;
    	this.mop = mop;
    	this.rarg = rarg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	dmr_return[2] = checkOp(mop);
    	dmr_return[3] = 0;
    }
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    DMRStruc dmr = new RangeDomainMap(null, null, (Comparable)rarg);
	    return new RelatrixIterator(BigSackAdapter.getBigSackSet(dmr), dmr, dmr_return);
	}
}
