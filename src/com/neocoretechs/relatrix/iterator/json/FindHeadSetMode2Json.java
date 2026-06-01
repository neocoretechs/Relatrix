package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixJson;

/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. Map must contain concrete instance.
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. 
* * Find the set of objects in the relation via the specified predicate. Legal permutations are:<br>
* *,[object],* <br>
* *,[object],? <br>
* ?,[object],? <br>
* ?,[object],* <br>
*  <p>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015, 2021,2026
* 
*/
public class FindHeadSetMode2Json extends FindSetMode2Json {
	Object endarg0,endarg1;
    public FindHeadSetMode2Json(char dop, Object marg, char rop, Object arg1, Object arg2) { 	
    	super(dop, marg, rop);
    	endarg0 = arg1;
    	endarg1 = arg2;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixJson.lastKey((Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setRange((Comparable) RelatrixJson.lastKey((Class)endarg1));
			} else {
				xdmr.setRange((Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJson(tdmr, xdmr, dmr_return);
	}
	
	@Override
	public Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		if(tdmr.getRange() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg1));
			} else {
				xdmr.setRange(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixHeadsetIteratorJson(alias, tdmr, xdmr, dmr_return);
	}
}
