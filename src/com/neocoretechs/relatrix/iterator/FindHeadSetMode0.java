package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode0 extends FindSetMode0 {
	Object endarg0,endarg1,endarg2;
	public FindHeadSetMode0(char dop, char mop, char rop, Object arg1, Object arg2, Object arg3) { 	
		super(dop,mop,rop);
		endarg0 = arg1;
		endarg1 = arg2;
		endarg2 = arg3;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) Relatrix.lastKey((Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap((Comparable) Relatrix.lastKey((Class)endarg1));
			} else {
				xdmr.setMap((Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange((Comparable) Relatrix.lastKey((Class)endarg2));
			} else {
				xdmr.setRange((Comparable)endarg2);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg1));
			} else {
				xdmr.setMap(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg2 instanceof Class) {
				xdmr.setRange(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg2));
			} else {
				xdmr.setRange(alias,(Comparable)endarg2);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
