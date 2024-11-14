package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
/**
 * Find elements strictly less than 'to' target.<p/>
 * Concrete object instance in range component = mode 1
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode1 extends FindSetMode1 {
	Object[] endarg;
	public FindHeadSetMode1(char dop, char mop, Object rarg, Object ... endarg) { 	
		super(dop,mop,rarg);
		if(endarg.length != 2)
			throw new RuntimeException("Must supply 2 qualifying arguments for Headset domain and map.");
		this.endarg = endarg;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone(); // concrete instance in range
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
			} else {
				xdmr.setDomain((Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setMap((Comparable) RelatrixKV.lastKey((Class)endarg[1]));
			} else {
				xdmr.setMap((Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone(); // concrete instance in range
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain(alias, (Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[1]));
			} else {
				xdmr.setMap(alias,(Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
