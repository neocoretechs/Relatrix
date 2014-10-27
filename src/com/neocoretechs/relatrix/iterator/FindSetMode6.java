package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.DomainMapRange;

/**
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],[object],* [object],[object],?
* [TemplateClass],[TemplateClass],* [TemplateClass],[TemplateClass],?
*
*/
public class FindSetMode6 extends IteratorFactory {
	// mode 6
	char rop;
	Object darg,marg;
	short[] dmr_return = new short[4];
    public FindSetMode6(Object darg, Object marg, char rop) { 	
    	this.darg = darg;
    	this.marg = marg;
    	this.rop = rop;
    	dmr_return[1] = 0;
    	// 'map' object
    	dmr_return[2] = 0;
    	//  'range'
    	// see if its ? or * operator
    	dmr_return[3] = checkOp(rop);
    }
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    DMRStruc dmr = new DomainMapRange((Comparable)darg, (Comparable)marg, null);
	    return new RelatrixIterator(BigSackAdapter.getBigSackSet(dmr), dmr, dmr_return);
	}
}
