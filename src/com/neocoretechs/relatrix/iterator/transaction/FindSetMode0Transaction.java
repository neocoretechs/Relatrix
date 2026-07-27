package com.neocoretechs.relatrix.iterator.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.iterator.FindSetMode0;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* return identity relationships. This mode returns a one object {@link com.neocoretechs.relatrix.Result}
* depending on the configuration of the findSet.
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* morphism object itself, or the morphism relationship.
* <p>
* Examples:<br>
* *,*,* domain,map,range order, return identity dmr instance in {@link com.neocoretechs.relatrix.Result1} <br>
* We can substitute a concrete object instance for any of the above wild cards to retrieve only those
* relationships that contain that object instance.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetMode0Transaction extends FindSetMode0 {
	// mode 0
	private static boolean DEBUG = false;
	TransactionId xid;
    public FindSetMode0Transaction(TransactionId xid, char dop, char mop, char rop) { 
    	super(dop, mop, rop);
    	this.xid = xid;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
 		AbstractRelation dmr = new Relation(true, null, xid, null, null, null);
 		if( DEBUG  )
 			System.out.printf("%s.createIterator xid=%s setting search for %s%n",this.getClass().getName(),xid,dmr);
 	    return createRelatrixIterator(dmr);
 	}
 	
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
 		AbstractRelation dmr = new Relation(true, alias, xid, null, null, null);
 		if( DEBUG  )
 			System.out.printf("%s.createIterator xid=%s alias=%s setting search for %s%n",this.getClass().getName(),xid,alias,dmr);
 	    return createRelatrixIterator(alias, dmr);
 	}
 	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIteratorTransaction(alias, xid, tdmr, dmr_return);
	}
}
