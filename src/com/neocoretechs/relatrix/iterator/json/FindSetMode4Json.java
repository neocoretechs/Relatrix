package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.iterator.IteratorFactory;

/**
* Find the set of objects in the relation via the specified predicate. 
* This variation accommodates findSet(object,"*|?","*|?") which returns a 1 or 2 element Comparable
* for each iteration wherein the object specified in the domain functions as the domain in the retrieved relationships.
* Legal permutations are:<br>
* [object],*,* <br>
* [object],*,?  <br>
* [object],?,?  <br>
* [object],?,* <br>
* [TemplateClass],*,* <br>
* [TemplateClass],*,? <br>
* [TemplateClass],?,? <br>
* [TemplateClass],?,* <br>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode4Json extends IteratorFactory {
	// mode 4
	char mop, rop;
	protected Object darg;
	protected short[] dmr_return = new short[4];
    public FindSetMode4Json(Object darg, char mop, char rop) { 	
    	this.darg = darg;
    	this.mop = mop;
    	this.rop = rop;
	    // see if its ? or * operator
    	dmr_return[1] = 0;
        // map
        // see if its ? or * operator
        dmr_return[2] = checkOp(mop);
        // range
        dmr_return[3] = checkOp(rop);
    }
    /**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		AbstractRelation dmr = new Relation(true, (Comparable)darg, null, null);
		return createRelatrixIterator(dmr);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		return new RelatrixIteratorJson(tdmr, dmr_return);
	}
	
	/**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation dmr = new Relation(true, alias, (Comparable)darg, null, null);
		return createRelatrixIterator(alias, dmr);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException{
		return new RelatrixIteratorJson(alias, tdmr, dmr_return);
	}
}
