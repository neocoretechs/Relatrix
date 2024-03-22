package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindTailSetMode0 extends FindSetMode0 {
	Object[] endarg;
	public FindTailSetMode0(char dop, char mop, char rop, Object ... endarg) { 	
		super(dop,mop,rop);
		if(endarg.length != 3)
			throw new RuntimeException("Must supply 3 qualifying arguments for Tailset domain and map and range.");
		this.endarg = endarg;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixKV.firstKey((Class)endarg[0]));
			} else {
				xdmr.setDomain((Comparable)endarg[0]); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setMap((Comparable) RelatrixKV.firstKey((Class)endarg[1]));
			} else {
				xdmr.setMap((Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[2] instanceof Class) {
				xdmr.setRange((Comparable) RelatrixKV.firstKey((Class)endarg[2]));
			} else {
				xdmr.setRange((Comparable)endarg[2]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(tdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg[0]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete type in d,m,r field, but we are returning relations with that value
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg[1]));
			} else {
				xdmr.setMap(alias,(Comparable)endarg[1]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[2] instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg[2]));
			} else {
				xdmr.setRange(alias,(Comparable)endarg[2]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
