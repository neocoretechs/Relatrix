package com.neocoretechs.relatrix.iterator.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.iterator.FindSetMode4;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;

/**
* Find the set of objects in the relation via the specified predicate. 
* This variation accommodates findSet(object,"*","*") which returns a Result
* for each iteration wherein the object specified in the domain functions as the domain in the retrieved relationships.
* Legal permutations are:<br>
* [object],*,* <br>
* [TemplateClass],*,* <br>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetMode4Transaction extends FindSetMode4 {
	// mode 4
	TransactionId xid;
    public FindSetMode4Transaction(TransactionId transactionId, Object darg, char mop, char rop) {
    	super(darg, mop, rop);
    	this.xid = transactionId;
    }
    
    /**
     *  @return The iterator for the returned set, each iterator return is {@link Result}
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		AbstractRelation dmr = new Relation(true, null, xid, (Comparable)darg, null, null);
		return createRelatrixIterator(dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
	
	/**
     *  @return The iterator for the returned set, each iterator return is a Result
     */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation dmr = new Relation(true, alias, xid, (Comparable)darg, null, null);
		return createRelatrixIterator(alias, dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		return new RelatrixIteratorTransaction(alias, xid, tdmr, dmr_return);
	}
}
