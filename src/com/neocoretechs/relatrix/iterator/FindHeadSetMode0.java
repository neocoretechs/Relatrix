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
	Object[] endarg;
	public FindHeadSetMode0(char dop, char mop, char rop, Object ... endarg) { 	
		super(dop,mop,rop);
		if(endarg.length != 3)
			throw new RuntimeException("Must supply 3 qualifying arguments for Headset domain and map and range.");
		this.endarg = endarg;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain((Comparable) Relatrix.lastKey((Class)endarg[0]));
			} else {
				xdmr.setDomain((Comparable)endarg[0]); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setMap((Comparable) Relatrix.lastKey((Class)endarg[1]));
			} else {
				xdmr.setMap((Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[2] instanceof Class) {
				xdmr.setRange((Comparable) Relatrix.lastKey((Class)endarg[2]));
			} else {
				xdmr.setRange((Comparable)endarg[2]);
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
			if(endarg[0] instanceof Class) {
				xdmr.setDomain(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg[0]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setMap(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg[1]));
			} else {
				xdmr.setMap(alias,(Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[2] instanceof Class) {
				xdmr.setRange(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg[2]));
			} else {
				xdmr.setRange(alias,(Comparable)endarg[2]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
