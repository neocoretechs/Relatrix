package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;

/**
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],*,... <br/>
* [object],[object],?,... <br/>
* Concrete domain and map
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindTailSetMode6 extends FindSetMode6 {
	Object[] endarg;
    public FindTailSetMode6(Object darg, Object marg, char rop, Object ... endarg) { 	
    	super(darg,marg, rop);
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Tailset range.");
		this.endarg = endarg;
    }

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setRange((Comparable) Relatrix.firstKey((Class)endarg[0]));
			} else {
				xdmr.setRange((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixTailsetIterator(tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr)throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setRange(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg[0]));
			} else {
				xdmr.setRange(alias,(Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixTailsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
