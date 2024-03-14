package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator;
import com.neocoretechs.relatrix.key.DBKey;
/**
* Headset stream mode 1. From beginning of order to value of instance argument.
* Find the set of objects in the relation via the specified predicate. Mode 1 = findset("*|?","*|?",object)
* returning identity, 1 or 2 element Comparable array of tuples for each iteration of the retrieval.
* Legal permutations are:
* *,*,[object] 
* *,?,[object] 
* ?,?,[object] 
* ?,*,[object]
* *,*,[TemplateClass] 
* *,?,[TemplateClass] 
* ?,?,[TemplateClass] 
* ?,*,[TemplateClass]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
 *
 */
public class FindHeadSetStreamMode1 extends FindSetStreamMode1 {
	Object[] endarg;
	public FindHeadSetStreamMode1(char dop, char mop, Object rarg, Object ... endarg) { 
		super(dop,mop,rarg);
		if(endarg.length != 2)
			throw new RuntimeException("Must supply 2 qualifying arguments for HeadSetStream domain and map.");
		this.endarg = endarg;
	}

	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone(); // concrete instance in range
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
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
		return new RelatrixHeadsetStream(tdmr, xdmr, ydmr, dmr_return);
	}
	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone(); // concrete instance in range
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain(alias, (Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getMap() == null) {
			if(endarg[1] instanceof Class) {
				tdmr.setMap(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[1]));
				xdmr.setMapKey(DBKey.nullDBKey); // full range
				ydmr.setMapKey(DBKey.fullDBKey);
			} else {
				tdmr.setMap(alias,(Comparable)endarg[1]);
				xdmr.setMapKey(tdmr.getMapKey());
				ydmr.setMapKey(tdmr.getMapKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetStream(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
