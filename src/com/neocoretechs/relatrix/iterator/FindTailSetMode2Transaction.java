package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Mode 2 find returns a tailSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate greater or equal to 'from' element. 
* Legal permutations are:<br/>
* *,[object],*,... <br/>
* *,[object],?,... <br/>
* ?,[object],?,... <br/>
* ?,[object],*,... <br/>
*  <p/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
* 
*/
public class FindTailSetMode2Transaction extends FindSetMode2Transaction {
	Object[] endarg;
    public FindTailSetMode2Transaction(TransactionId xid, char dop, Object marg, char rop, Object ... endarg) { 	
    	super(xid, dop, marg, rop);
    	if(endarg.length != 2)
    		throw new RuntimeException("Must supply 2 qualifying arguments for Tailset domain and range.");
		this.endarg = endarg;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixTransaction.firstKey(xid, (Class)endarg[0]));
			} else {
				xdmr.setDomain((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setRange((Comparable) RelatrixTransaction.firstKey(xid, (Class)endarg[1]));
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
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[0]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[0]);
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
