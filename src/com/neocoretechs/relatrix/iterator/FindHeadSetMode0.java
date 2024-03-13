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
public class FindHeadSetMode0 extends FindSetMode0 {
	Object[] endarg;
	public FindHeadSetMode0(char dop, char mop, char rop, Object[] endarg) { 	
		super(dop,mop,rop);
		if(endarg.length != 3)
			throw new RuntimeException("Must supply 3 qualifying arguments for Headset domain and map and range.");
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
				tdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[0]); // same as concrete type in d,m,r field, but we are returning relations with that value
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				tdmr.setMap((Comparable) RelatrixKV.lastKey((Class)endarg[1]));
				xdmr.setMapKey(DBKey.nullDBKey); // full range
				ydmr.setMapKey(DBKey.fullDBKey);
			} else {
				tdmr.setMap((Comparable)endarg[1]);
				xdmr.setMapKey(tdmr.getMapKey());
				ydmr.setMapKey(tdmr.getMapKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[2] instanceof Class) {
				tdmr.setRange((Comparable) RelatrixKV.lastKey((Class)endarg[2]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange((Comparable)endarg[2]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(tdmr, xdmr, ydmr, dmr_return);
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
		if(tdmr.getRange() == null) {
			if(endarg[2] instanceof Class)
				tdmr.setRange((Comparable) RelatrixKV.lastKey(alias,(Class)endarg[2]));
			else
				tdmr.setMap(alias,(Comparable)endarg[2]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
