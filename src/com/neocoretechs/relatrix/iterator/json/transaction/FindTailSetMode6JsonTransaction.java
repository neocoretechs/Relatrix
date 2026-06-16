package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixJsonTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;


/**
* Find elements greater or equal to 'from' element.
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br>
* [object],[object],*,... <br>
* [object],[object],?,... <br>
* Concrete domain and map
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
*
*/
public class FindTailSetMode6JsonTransaction extends FindSetMode6JsonTransaction {
	Object endarg0;
    public FindTailSetMode6JsonTransaction(TransactionId xid, Object darg, Object marg, char rop, Object arg1) { 	
    	super(xid, darg,marg, rop);
		endarg0 = arg1;
    }

	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr)throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setRange((Comparable) RelatrixJsonTransaction.firstKey(xid, (Class)endarg0));
			} else {
				xdmr.setRange((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
	    return new RelatrixTailsetIteratorJsonTransaction(xid, tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr)throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixJsonTransaction.firstKey(alias, xid, (Class)endarg0));
			} else {
				xdmr.setRange(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
	    return new RelatrixTailsetIteratorJsonTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
