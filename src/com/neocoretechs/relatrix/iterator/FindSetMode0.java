package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.DomainMapRange;

/**
* Permutation for predicate *,*,* or ?,?,? or return identity relationships or 
* domain,map,range 3 element array for each iteration
*/
public class FindSetMode0 extends IteratorFactory {
	// mode 0
	char dop,mop,rop;
	short[] dmr_return = new short[4];
    public FindSetMode0(char dop, char mop, char rop) { 	
    	this.dop = dop;
    	this.mop = mop;
    	this.rop = rop;
	    // see if its ? or * operator
	    dmr_return[1] = checkOp(dop);
	    dmr_return[2] = checkOp(mop);
	    dmr_return[3] = checkOp(rop);
	    if( isReturnRelationships(dop, mop, rop) )
	    	dmr_return[0] = -1;
    }
    /**
    * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
    */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    DMRStruc dmr = new DomainMapRange(null, null, null);
	    return new RelatrixIterator(BigSackAdapter.getBigSackSet(dmr), dmr, dmr_return);
	}
}
