package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixJsonTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. Legal permutations are:<br>
* [object],*,[object] <br>
* [object],?,[object] <br>
* Concrete domain and range
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
* 
*/
public class FindHeadSetMode5JsonTransaction extends FindSetMode5JsonTransaction {
	Object endarg0;
    public FindHeadSetMode5JsonTransaction(TransactionId xid, Object darg, char mop, Object rarg, Object arg1) { 	
    	super(xid, darg, mop, rarg);
		endarg0 = arg1;
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setMap((Comparable) RelatrixJsonTransaction.lastKey(xid, (Class)endarg0));
			} else {
				xdmr.setMap((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJsonTransaction(xid, tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixJsonTransaction.lastKey(alias, xid, (Class)endarg0));
			} else {
				xdmr.setMap(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJsonTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
