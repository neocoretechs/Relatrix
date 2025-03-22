package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.TransactionId;



/**
 * Find elements greater or equal to 'from' element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindTailSetMode1Transaction extends FindSetMode1Transaction {
	Object endarg0,endarg1;
	public FindTailSetMode1Transaction(TransactionId xid, char dop, char mop, Object rarg, Object arg1, Object arg2) { 	
		super(xid,dop,mop,rarg);
		endarg0 = arg1;
		endarg1 = arg2;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone(); // concrete instance in range
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0); // same as concrete instance in domain, but we are returning, so for ranging no diff
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg1));
			} else {
				xdmr.setMap((Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone(); // concrete instance in range
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias, (Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0); // same as concrete instance in domain, but we are returning, so for ranging no diff
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg1));
			} else {
				xdmr.setMap(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
