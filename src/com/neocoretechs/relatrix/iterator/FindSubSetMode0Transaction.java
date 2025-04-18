package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified.<p/>
 * In this case, the domain, map, and range operators can be ?, *, or concrete instance, just as other permutations, but the variable parameter
 * section suffix contains either a class designation for ? or * wildcard, or two concrete object instances for a start and end range
 * in the event there is a ? or * and no concrete instance in the corresponding operator section. In other words, we need a range to 
 * designate the subset where we do not have a class to use. If we have a class, we use the first to last keys in the corresponding class.<p/>
 * If we dont have a class to use, we need those concrete instances to designate the range.<p/>
 * Just as in the other set retrievals, if we have a concrete object in the operator section, other than ? or *, that concrete object
 * determines the relationship for that domain, map, or range part and we forego having either a class or concrete object range in the variable
 * list of objects and classes in the suffix.<p/>
 * Examples:<p/>
 * ?,*,[object],[object],[object],[class] <br/>
 * ?,?,?,[class],[class],[object],[object] <br/>
 * ?,?,?,[object],[object],[object],[object],[object],[object] <br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2024
 *
 */
public class FindSubSetMode0Transaction extends FindSetMode0Transaction {
	Object[] endarg;
	int argCtr = 0;
	public FindSubSetMode0Transaction(TransactionId xid, char dop, char mop, char rop, Object ... endarg) { 	
		super(xid,dop,mop,rop);
		if(endarg.length < 3)
			throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
		this.endarg = endarg;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
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
		if(tdmr.getMap() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setMap((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[argCtr]));
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setMap((Comparable) RelatrixTransaction.lastKey(xid,(Class)endarg[argCtr++]));
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setMap((Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setMap((Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
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
		return new RelatrixSubsetIteratorTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
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
		if(tdmr.getMap() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[argCtr]));
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setMap(alias,(Comparable) RelatrixTransaction.lastKey(alias,xid,(Class)endarg[argCtr++]));
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setMap(alias,(Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setMap(alias,(Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
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
		return new RelatrixSubsetIteratorTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
