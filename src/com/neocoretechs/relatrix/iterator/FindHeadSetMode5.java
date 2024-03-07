package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;


/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. Legal permutations are:<br/>
* [object],*,[object] <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindHeadSetMode5 extends FindSetMode5 {
	Object[] endarg;
    public FindHeadSetMode5(Object darg, char mop, Object rarg, Object[] endarg) { 	
    	super(darg, mop, rarg);
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Headset map.");
		this.endarg = endarg;
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setMap((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
			else
				tdmr.setMap((Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(tdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setMap(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
			else
				tdmr.setMap(alias,(Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixHeadsetIterator(alias, tdmr, dmr_return);
	}
}
