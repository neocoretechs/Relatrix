package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode1 extends FindSetMode1 {
	Object[] endarg;
	public FindHeadSetMode1(char dop, char mop, Object rarg, Object[] endarg) { 	
		super(dop,mop,rarg);
		if(endarg.length != 2)
			throw new RuntimeException("Must supply 2 qualifying arguments for Headset domain and map.");
		this.endarg = endarg;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
			else
				tdmr.setDomain((Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class)
				tdmr.setMap((Comparable) RelatrixKV.lastKey((Class)endarg[1]));
			else
				tdmr.setMap((Comparable)endarg[1]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setDomain(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
			else
				tdmr.setDomain(alias,(Comparable)endarg[0]);
		}
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class)
				tdmr.setMap(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[1]));
			else
				tdmr.setMap(alias,(Comparable)endarg[1]);
		}
		return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
