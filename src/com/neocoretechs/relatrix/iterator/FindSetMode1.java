package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RangeDomainMap;

/**
* Find the set of objects in the relation via the specified predicate. Mode 1 = findset("*|?","*|?",object)
* returning identity, 1 or 2 element Comparable array of tuples for each iteration of the retrieval.
* Legal permutations are
* *,*,[object] *,?,[object] ?,?,[object] ?,*,[object]
* *,*,[TemplateClass] *,?,[TemplateClass] ?,?,[TemplateClass] ?,*,[TemplateClass]
* * @author jg Groff Copyright (C) NeoCoreTechs 2014,2105
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
	    Morphism dmr = new RangeDomainMap(null, null, (Comparable)rarg);
	    return createRelatrixIterator(dmr);
	}
	
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIterator(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
