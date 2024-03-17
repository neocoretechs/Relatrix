package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;

/**
* Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* [object],*,*,... <br/>
* [object],*,?,...  <br/>
* [object],?,?,...  <br/>
* [object],?,*,... <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2024 
*/
public class FindTailSetStreamMode4 extends FindSetStreamMode4 {
	Object[] endarg;
    public FindTailSetStreamMode4(Object darg, char mop, char rop, Object ... endarg) { 	
    	super(darg, mop, rop);
    	if(endarg.length != 2)
			throw new RuntimeException("Must supply 2 qualifying arguments for Tailset map and range.");
		this.endarg = endarg;
    }
    /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setMap((Comparable) RelatrixKV.firstKey((Class)endarg[0]));
				xdmr.setMapKey(DBKey.nullDBKey); // full range
				ydmr.setMapKey(DBKey.fullDBKey);
			} else {
				tdmr.setMap((Comparable)endarg[0]);
				xdmr.setMapKey(tdmr.getMapKey());
				ydmr.setMapKey(tdmr.getMapKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				tdmr.setRange((Comparable) RelatrixKV.firstKey((Class)endarg[1]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange((Comparable)endarg[1]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetStream(tdmr, xdmr, ydmr, dmr_return);
	}
    /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setMap(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg[0]));
				xdmr.setMapKey(DBKey.nullDBKey); // full range
				ydmr.setMapKey(DBKey.fullDBKey);
			} else {
				tdmr.setMap(alias,(Comparable)endarg[0]);
				xdmr.setMapKey(tdmr.getMapKey());
				ydmr.setMapKey(tdmr.getMapKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				tdmr.setRange(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg[1]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange(alias,(Comparable)endarg[1]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetStream(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
