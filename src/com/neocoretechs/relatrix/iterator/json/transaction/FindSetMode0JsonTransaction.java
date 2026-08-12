package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.iterator.json.FindSetMode0Json;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.AbstractRelation;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* return identity relationships or 
* domain,map,range 3 element array for each iteration. 
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* morphism object itself.
* Examples:<br>
* *,*,* domain,map,range order, return identity dmr instance in {@link com.neocoretechs.relatrix.Result1} <br/>
* We can substitute a concrete object instance for any of the above wild cards to retrieve only those
* relationships that contain that object instance.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetMode0JsonTransaction extends FindSetMode0Json {
	// mode 0
	private static boolean DEBUG = false;
	TransactionId xid;
    public FindSetMode0JsonTransaction(TransactionId xid, char dop, char mop, char rop) { 
    	super(dop, mop, rop);
    	this.xid = xid;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator(ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
 		AbstractRelation dmr = new Relation(true, null, xid, null, null, null);
 		if( DEBUG  )
 			System.out.printf("%s.createIterator xid=%s setting search for %s%n",this.getClass().getName(),xid,dmr);
 	    return createRelatrixIterator(dmr, ctx);
 	}
 	
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator(Alias alias, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
 		AbstractRelation dmr = new Relation(true, alias, xid, null, null, null);
 		if( DEBUG  )
 			System.out.printf("%s.createIterator xid=%s alias=%s setting search for %s%n",this.getClass().getName(),xid,alias,dmr);
 	    return createRelatrixIterator(alias, dmr, ctx);
 	}
 	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorJsonTransaction(xid, tdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIteratorJsonTransaction(alias, xid, tdmr, dmr_return);
	}
}
