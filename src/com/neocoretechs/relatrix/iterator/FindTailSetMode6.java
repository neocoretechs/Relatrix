package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;

/**
* Find elements greater or equal to 'from' element.
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],*,... <br/>
* [object],[object],?,... <br/>
* Concrete domain and map
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindTailSetMode6 extends FindSetMode6 {
	Object endarg0;
    public FindTailSetMode6(Object darg, Object marg, char rop, Object arg1) { 	
    	super(darg,marg, rop);
		endarg0 = arg1;
    }

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setRange((Comparable) Relatrix.firstKey((Class)endarg0));
			} else {
				xdmr.setRange((Comparable)endarg0);
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
			if(endarg0 instanceof Class) {
				xdmr.setRange(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg0));
			} else {
				xdmr.setRange(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixTailsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
