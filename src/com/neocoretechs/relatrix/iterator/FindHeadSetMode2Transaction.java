package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. * Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],* <br/>
* *,[object],? <br/>
* ?,[object],? <br/>
* ?,[object],* <br/>
* *,[TemplateClass],* <br/>
* *,[TemplateClass],? <br/>
* ?,[TemplateClass],? <br/>
* ?,[TemplateClass],* <br/>
*  <p/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
* 
*/
public class FindHeadSetMode2Transaction extends FindSetMode2Transaction {
	Object[] endarg;
    public FindHeadSetMode2Transaction(String xid, char dop, Object marg, char rop, Object ... endarg) { 	
    	super(xid, dop, marg, rop);
    	if(endarg.length != 2)
    		throw new RuntimeException("Must supply 2 qualifying arguments for Headset domain and range.");
		this.endarg = endarg;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain((Comparable) RelatrixKVTransaction.lastKey(xid, (Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[0]);
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				tdmr.setRange((Comparable) RelatrixKVTransaction.lastKey(xid, (Class)endarg[1]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange((Comparable)endarg[1]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIteratorTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
	}
	
	@Override
	public Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain(alias,(Comparable) RelatrixKVTransaction.lastKey(alias,xid,(Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain(alias,(Comparable)endarg[0]);
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class) {
				tdmr.setRange(alias,(Comparable) RelatrixKVTransaction.lastKey(alias,xid,(Class)endarg[1]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				tdmr.setRange(alias,(Comparable)endarg[1]);
				xdmr.setRangeKey(tdmr.getRangeKey());
				ydmr.setRangeKey(tdmr.getRangeKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
