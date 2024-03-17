package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;

/** 
* Mode 6 = findTailSet(object,object,"*|?",[class | object]) return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],*,... <br/>
* [object],[object],?,... <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindTailSetMode6Transaction extends FindSetMode6Transaction {
	Object[] endarg;
    public FindTailSetMode6Transaction(String xid, Object darg, Object marg, char rop, Object ... endarg) { 	
    	super(xid, darg, marg, rop);
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Tailset range.");
		this.endarg = endarg;
    }

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setRange((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[0]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange((Comparable)endarg[0]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr)throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setRange(alias,(Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[0]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange(alias,(Comparable)endarg[0]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
