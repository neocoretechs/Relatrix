package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.MapDomainRange;


/**
* Mode 2 find returns a set in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
* 
*/
public class FindSetMode2 extends IteratorFactory {
	// mode 2
	char dop,rop;
	Object marg;
	short[] dmr_return = new short[4];
    public FindSetMode2(char dop, Object marg, char rop) { 	
    	this.dop = dop;
    	this.rop = rop;
    	this.marg = marg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	// 'map' object
    	dmr_return[2] = 0;
    	// range, see if its ? or * operator
    	dmr_return[3] = checkOp(rop);
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		DMRStruc dmr = new MapDomainRange(null, (Comparable)marg, null);
		return new RelatrixIterator(BigSackAdapter.getBigSackSet(dmr), dmr, dmr_return);
	}
}
