package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixJson;

/**
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br>
* [object],[object],* <br>
* [object],[object],? <br>
* Concrete domain and map
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
*
*/
public class FindHeadSetMode6Json extends FindSetMode6Json {
	Object endarg0;
    public FindHeadSetMode6Json(Object darg, Object marg, char rop, Object arg1) { 	
    	super(darg,marg, rop);
		endarg0 = arg1;
    }

	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr)throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setRange((Comparable) RelatrixJson.lastKey((Class)endarg0));
			} else {
				xdmr.setRange((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
	    return new RelatrixHeadsetIteratorJson(tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr)throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setRange(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg0));
			} else {
				xdmr.setRange(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
	    return new RelatrixHeadsetIteratorJson(alias, tdmr, xdmr, dmr_return);
	}
}
