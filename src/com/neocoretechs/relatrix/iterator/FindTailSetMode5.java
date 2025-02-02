package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;

/**
* Find elements greater or equal to 'from' element.
* Mode 5. Permutation with 2 objects.
* Legal permutations are:<br/>
* [object],*,[object],... <br/>
* [object],?,[object],... <br/>
* Concrete domain and range
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindTailSetMode5 extends FindSetMode5 {
	Object endarg0;
    public FindTailSetMode5(Object darg, char mop, Object rarg, Object arg1) { 	
    	super(darg, mop, rarg);
		endarg0 = arg1;
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setMap((Comparable) Relatrix.firstKey((Class)endarg0));
			} else {
				xdmr.setMap((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setMap(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg0));
			} else {
				xdmr.setMap(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
