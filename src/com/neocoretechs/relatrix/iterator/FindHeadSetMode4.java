package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;


/**
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. Legal permutations are:<br/>
* [object],*,* <br/>
* [object],*,?  <br/>
* [object],?,?  <br/>
* [object],?,* <br/>
* [TemplateClass],*,* <br/>
* [TemplateClass],*,? <br/>
* [TemplateClass],?,? <br/>
* [TemplateClass],?,* <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindHeadSetMode4 extends FindSetMode4 {
	Object[] endarg;
    public FindHeadSetMode4(Object darg, char mop, char rop, Object[] endarg) { 	
    	super(darg, mop, rop);
    	if(endarg.length != 2)
			throw new RuntimeException("Must supply 2 qualifying arguments for Headset map and range.");
		this.endarg = endarg;
    }
    /**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setMap((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
			else
				tdmr.setMap((Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class)
				tdmr.setRange((Comparable) RelatrixKV.lastKey((Class)endarg[1]));
			else
				tdmr.setRange((Comparable)endarg[1]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(tdmr, dmr_return);
	}
	
	@Override
	public Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setMap(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
			else
				tdmr.setMap(alias,(Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() == null) {
			if(endarg[1] instanceof Class)
				tdmr.setRange(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[1]));
			else
				tdmr.setRange(alias,(Comparable)endarg[1]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(alias, tdmr, dmr_return);
	}
}
