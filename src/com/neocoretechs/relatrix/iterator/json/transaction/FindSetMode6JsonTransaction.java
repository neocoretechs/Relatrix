package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.iterator.json.FindSetMode6Json;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;

/**
* Find the set of objects in the relation via the specified predicate. Mode 6 is for findSet(object,object,"*")
* where a 1 element Comparable Result Relation is returned from each iteration of the constructed iterator returned from findSet.
* Legal permutations are:<br>
* [object],[object],* <br>
* [TemplateClass],[TemplateClass],* <br>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode6JsonTransaction extends FindSetMode6Json {
	// mode 6
	TransactionId xid;
    public FindSetMode6JsonTransaction(TransactionId transactionId, Object darg, Object marg, char rop) { 	
    	super(darg, marg, rop);
    	this.xid = transactionId;
    }
    
    /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
	    AbstractRelation dmr = new Relation(true, null, xid, (Comparable)darg, (Comparable)marg, null);
	    return createRelatrixIterator(dmr, ctx);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx)throws IllegalAccessException, IOException {
	    return new RelatrixIteratorJsonTransaction(xid, tdmr, dmr_return);
	}
	
	 /**
     *  @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
	    AbstractRelation dmr = new Relation(true, alias, xid, (Comparable)darg, (Comparable)marg, null);
	    return createRelatrixIterator(alias, dmr, ctx);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx)throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIteratorJsonTransaction(alias, xid, tdmr, dmr_return);
	}
}
