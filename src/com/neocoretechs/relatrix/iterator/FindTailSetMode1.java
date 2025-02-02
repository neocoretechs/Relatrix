package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.rocksack.Alias;
/**
 * Find elements greater or equal to 'from' element.
 * Concrete object instance in range component = mode 1
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindTailSetMode1 extends FindSetMode1 {
	Object endarg0,endarg1;
	public FindTailSetMode1(char dop, char mop, Object rarg, Object arg1, Object arg2) { 	
		super(dop,mop,rarg);
		endarg0 = arg1;
		endarg1 = arg2;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone(); // concrete instance in range
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixKV.firstKey((Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0); // same as concrete instance in domain, but we are returning, so for ranging no diff
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap((Comparable) RelatrixKV.firstKey((Class)endarg1));
			} else {
				xdmr.setMap((Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone(); // concrete instance in range
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias, (Comparable) RelatrixKV.firstKey(alias,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0); // same as concrete instance in domain, but we are returning, so for ranging no diff
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getMap() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg1));
			} else {
				xdmr.setMap(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
