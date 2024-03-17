package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;


/**
* Mode 5. Permutation with 2 objects in a transaction context.
* Find the set of objects in the relation via the specified predicate. 
* Legal permutations are:<br/>
* [object],*,[object],... <br/>
* [object],?,[object],... <br/>
*  @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindTailSetStreamMode5Transaction extends FindTailSetStreamMode5 {
	String xid;
	Object[] endarg;
    public FindTailSetStreamMode5Transaction(String xid, Object darg, char mop, Object rarg, Object ... endarg) { 	
    	super(darg, mop, rarg, endarg);
    	this.xid = xid;
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Tailset map.");
    }
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setMap((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[0]));
				xdmr.setMapKey(DBKey.nullDBKey); // full range
				ydmr.setMapKey(DBKey.fullDBKey);
			} else {
				tdmr.setMap((Comparable)endarg[0]);
				xdmr.setMapKey(tdmr.getMapKey());
				ydmr.setMapKey(tdmr.getMapKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetStreamTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
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
				tdmr.setMap(alias,(Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[0]));
				xdmr.setMapKey(DBKey.nullDBKey); // full range
				ydmr.setMapKey(DBKey.fullDBKey);
			} else {
				tdmr.setMap(alias,(Comparable)endarg[0]);
				xdmr.setMapKey(tdmr.getMapKey());
				ydmr.setMapKey(tdmr.getMapKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetStreamTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
