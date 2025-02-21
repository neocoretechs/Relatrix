package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.rocksack.Alias;

/**
* Find the set of objects in the relation via the specified predicate. Mode 1 = findset("*|?","*|?",object)
* returning identity, 1 or 2 element Comparable array of tuples for each iteration of the retrieval.
* Legal permutations are:
* *,*,[object] <br/>
* *,?,[object] <br/>
* ?,?,[object] <br/>
* ?,*,[object] <br/>
* *,*,[TemplateClass] <br/>
* *,?,[TemplateClass] <br/>
* ?,?,[TemplateClass] <br/>
* ?,*,[TemplateClass] <br/>
* @author Jonthan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
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
	    AbstractRelation dmr = new RangeDomainMap(true, null, null, (Comparable)rarg);
	    return createRelatrixIterator(dmr);
	}
	
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIterator( tdmr, dmr_return);
	}
	
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
	    AbstractRelation dmr = new RangeDomainMap(true, alias, null, null, (Comparable)rarg);
	    return createRelatrixIterator(alias, dmr);
	}
	
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIterator(alias, tdmr, dmr_return);
	}
}
