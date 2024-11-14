package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Mode 5. Permutation with 2 objects.
* Legal permutations are:<br/>
* [object],*,[object],... <br/>
* [object],?,[object],... <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindTailSetMode5Transaction extends FindSetMode5Transaction {
	Object[] endarg;
    public FindTailSetMode5Transaction(TransactionId xid, Object darg, char mop, Object rarg, Object ... endarg) { 	
    	super(xid, darg, mop, rarg);
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Tailset map.");
		this.endarg = endarg;
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setMap((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[0]));
			} else {
				xdmr.setMap((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[0]));
			} else {
				xdmr.setMap(alias,(Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
