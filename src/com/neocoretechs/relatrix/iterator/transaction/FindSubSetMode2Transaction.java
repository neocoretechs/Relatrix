package com.neocoretechs.relatrix.iterator.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.rocksack.TransactionId;

/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are:<br>
* *,[object],*,... <br>
*  <p>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
* 
*/
public class FindSubSetMode2Transaction extends FindSetMode2Transaction {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode2Transaction(TransactionId xid, char dop, Object marg, char rop, Object ... endarg ) { 	
    	super(xid, dop, marg, rop);
		this.endarg = endarg;
		if(endarg.length < 2) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		AbstractRelation ydmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
			ydmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg[argCtr]));
				ydmr.setDomain((Comparable) RelatrixTransaction.lastKey(xid,(Class)endarg[argCtr++]));
			} else {
				xdmr.setDomain((Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				ydmr.setDomain((Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null

		if(tdmr.getRange() == null) {
			if(endarg[argCtr] instanceof Class) {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg[argCtr]));
				ydmr.setRange((Comparable) RelatrixTransaction.lastKey(xid,(Class)endarg[argCtr]));
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange((Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setRange((Comparable)endarg[argCtr]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixSubsetIteratorTransaction(xid, tdmr, xdmr, ydmr, dmr_return, ctx);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		AbstractRelation ydmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
			ydmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[argCtr]));
				ydmr.setDomain(alias,(Comparable) RelatrixTransaction.lastKey(alias,xid,(Class)endarg[argCtr++]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				ydmr.setDomain(alias,(Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
	
		if(tdmr.getRange() == null) {
			if(endarg[argCtr] instanceof Class) {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[argCtr]));
				ydmr.setRange(alias,(Comparable) RelatrixTransaction.lastKey(alias,xid,(Class)endarg[argCtr]));
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange(alias,(Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setRange(alias,(Comparable)endarg[argCtr]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixSubsetIteratorTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return, ctx);
	}
}
