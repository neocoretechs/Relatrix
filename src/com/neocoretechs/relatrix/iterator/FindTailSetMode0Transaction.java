package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.TransactionId;


/**
 * Find elements greater or equal to 'from' element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindTailSetMode0Transaction extends FindSetMode0Transaction {
	Object endarg0,endarg1,endarg2;
	public FindTailSetMode0Transaction(TransactionId xid, char dop, char mop, char rop, Object arg1, Object arg2, Object arg3) { 	
		super(xid,dop,mop,rop);
		endarg0 = arg1;
		endarg1 = arg2;
		endarg2 = arg3; 	
	}

	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg1));
			} else {
				xdmr.setMap((Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg2));
			} else {
				xdmr.setRange((Comparable)endarg2);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg1));
			} else {
				xdmr.setMap(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg2));
			} else {
				xdmr.setRange(alias,(Comparable)endarg2);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
