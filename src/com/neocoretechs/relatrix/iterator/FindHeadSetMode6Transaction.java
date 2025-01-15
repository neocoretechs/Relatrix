package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],* <br/>
* [object],[object],? <br/>
* [TemplateClass],[TemplateClass],* <br/>
* [TemplateClass],[TemplateClass],? <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindHeadSetMode6Transaction extends FindSetMode6Transaction {
	Object[] endarg;
    public FindHeadSetMode6Transaction(TransactionId xid, Object darg, Object marg, char rop, Object ... endarg) { 	
    	super(xid, darg, marg, rop);
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Headset range.");
		this.endarg = endarg;
    }

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setRange((Comparable) RelatrixTransaction.lastKey(xid,(Class)endarg[0]));
			} else {
				xdmr.setRange((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixHeadsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr)throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixTransaction.lastKey(alias,xid,(Class)endarg[0]));
			} else {
				xdmr.setRange(alias,(Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
