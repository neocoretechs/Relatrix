package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Find the set of objects in the relation via the specified predicate greater or equal to 'from' element. Legal permutations are:<br/>
* [object],*,*,... <br/>
* [object],*,?,...  <br/>
* [object],?,?,...  <br/>
* [object],?,*,... <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindTailSetMode4Transaction extends FindSetMode4Transaction {
	Object[] endarg;
    public FindTailSetMode4Transaction(TransactionId xid, Object darg, char mop, char rop, Object ... endarg) { 	
    	super(xid, darg, mop, rop);
       	if(endarg.length != 2)
    			throw new RuntimeException("Must supply 2 qualifying arguments for Tailset map and range.");
    	this.endarg = endarg;
    }
    /**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setMap((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg[0]));
			} else {
				xdmr.setMap((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setRange((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg[1]));
			} else {
				xdmr.setRange((Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}
	
	@Override
	public Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[0]));
			} else {
				xdmr.setMap(alias,(Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[1]));
			} else {
				xdmr.setRange(alias,(Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
