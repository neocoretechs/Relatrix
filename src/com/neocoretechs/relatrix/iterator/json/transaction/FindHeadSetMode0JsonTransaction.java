package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixJsonTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.RelatrixKVJsonTransaction;

/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
 *
 */
public class FindHeadSetMode0JsonTransaction extends FindSetMode0JsonTransaction {
	public static boolean DEBUG = false;
	Object endarg0,endarg1,endarg2;
	public FindHeadSetMode0JsonTransaction(TransactionId xid, char dop, char mop, char rop, Object arg1, Object arg2, Object arg3) { 	
		super(xid, dop,mop,rop);
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
				xdmr.setDomain((Comparable) RelatrixJsonTransaction.lastKey(xid, (Class)endarg0));
				if(DEBUG)
					System.out.printf("%s domain instanceof Class %s, lastKey=%s%n", this.getClass().getName(),endarg0,RelatrixKVJsonTransaction.getData(xdmr.getDomain()));
			} else {
				xdmr.setDomain((Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
				if(DEBUG)
					System.out.printf("%s domain is object=%s%n", this.getClass().getName(),RelatrixKVJsonTransaction.getData(xdmr.getDomain()));
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap((Comparable) RelatrixJsonTransaction.lastKey(xid, (Class)endarg1));
				if(DEBUG)
					System.out.printf("%s map instanceof Class %s, lastKey=%s%n", this.getClass().getName(),endarg1,RelatrixKVJsonTransaction.getData(xdmr.getMap()));
			} else {
				xdmr.setMap((Comparable)endarg1);
				if(DEBUG)
					System.out.printf("%s map is object=%s%n", this.getClass().getName(),RelatrixKVJsonTransaction.getData(xdmr.getMap()));
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange((Comparable) RelatrixJsonTransaction.lastKey(xid, (Class)endarg2));
				if(DEBUG)
					System.out.printf("%s map instanceof Class %s, lastKey=%s%n", this.getClass().getName(),endarg2,RelatrixKVJsonTransaction.getData(xdmr.getRange()));
			} else {
				xdmr.setRange((Comparable)endarg2);
				if(DEBUG)
					System.out.printf("%s range is object=%s%n", this.getClass().getName(),RelatrixKVJsonTransaction.getData(xdmr.getRange()));
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJsonTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixJsonTransaction.lastKey(alias, xid, (Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixJsonTransaction.lastKey(alias, xid, (Class)endarg1));
			} else {
				xdmr.setMap(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixJsonTransaction.lastKey(alias, xid, (Class)endarg2));
			} else {
				xdmr.setRange(alias,(Comparable)endarg2);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJsonTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
