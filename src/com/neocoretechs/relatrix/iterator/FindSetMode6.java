package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;

/**
* Find the set of objects in the relation via the specified predicate. Mode 6 is for findSet(object,object,"*")
*
* Legal permutations are:<br>
* [object],[object],* <br>
* [TemplateClass],[TemplateClass],* <br>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode6 extends IteratorFactory {
	// mode 6
	char rop;
	protected Object darg;
	protected Object marg;
	protected short[] dmr_return = new short[4];
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
	public Iterator<?> createIterator(ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
	    AbstractRelation dmr = new Relation(true, (Comparable)darg, (Comparable)marg, null);
	    return createRelatrixIterator(dmr, ctx);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx)throws IllegalAccessException, IOException {
	    return new RelatrixIterator( tdmr, dmr_return, ctx);
	}
	
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
	    AbstractRelation dmr = new Relation(true, alias, (Comparable)darg, (Comparable)marg, null);
	    return createRelatrixIterator(alias, dmr, ctx);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx)throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIterator(alias, tdmr, dmr_return, ctx);
	}
}
