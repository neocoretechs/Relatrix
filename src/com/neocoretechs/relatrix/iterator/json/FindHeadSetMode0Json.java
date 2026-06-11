package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixJson;
import com.neocoretechs.relatrix.RelatrixKVJson;

/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
 *
 */
public class FindHeadSetMode0Json extends FindSetMode0Json {
	public static boolean DEBUG = false;
	Object endarg0,endarg1,endarg2;
	public FindHeadSetMode0Json(char dop, char mop, char rop, Object arg1, Object arg2, Object arg3) { 	
		super(dop,mop,rop);
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
				xdmr.setDomain((Comparable) RelatrixJson.lastKey((Class)endarg0));
				if(DEBUG)
					System.out.printf("%s domain instanceof Class %s, lastKey=%s%n", this.getClass().getName(),endarg0,RelatrixKVJson.getData(xdmr.getDomain()));
			} else {
				xdmr.setDomain((Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
				if(DEBUG)
					System.out.printf("%s domain is object=%s%n", this.getClass().getName(),RelatrixKVJson.getData(xdmr.getDomain()));
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap((Comparable) RelatrixJson.lastKey((Class)endarg1));
				if(DEBUG)
					System.out.printf("%s map instanceof Class %s, lastKey=%s%n", this.getClass().getName(),endarg1,RelatrixKVJson.getData(xdmr.getMap()));
			} else {
				xdmr.setMap((Comparable)endarg1);
				if(DEBUG)
					System.out.printf("%s map is object=%s%n", this.getClass().getName(),RelatrixKVJson.getData(xdmr.getMap()));
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange((Comparable) RelatrixJson.lastKey((Class)endarg2));
				if(DEBUG)
					System.out.printf("%s map instanceof Class %s, lastKey=%s%n", this.getClass().getName(),endarg2,RelatrixKVJson.getData(xdmr.getRange()));
			} else {
				xdmr.setRange((Comparable)endarg2);
				if(DEBUG)
					System.out.printf("%s range is object=%s%n", this.getClass().getName(),RelatrixKVJson.getData(xdmr.getRange()));
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJson(tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg1));
			} else {
				xdmr.setMap(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg2));
			} else {
				xdmr.setRange(alias,(Comparable)endarg2);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJson(alias, tdmr, xdmr, dmr_return);
	}
}
