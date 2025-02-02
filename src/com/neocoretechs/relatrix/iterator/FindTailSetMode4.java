package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;

/**
* Find elements greater or equal to 'from' element.
* Legal permutations are:<br/>
* [object],*,*,... <br/>
* [object],*,?,...  <br/>
* [object],?,?,...  <br/>
* [object],?,*,... <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindTailSetMode4 extends FindSetMode4 {
	Object endarg0,endarg1;
    public FindTailSetMode4(Object darg, char mop, char rop, Object arg1, Object arg2) { 	
    	super(darg, mop, rop);
		endarg0 = arg1;
		endarg1 = arg2;
    }
    /**
     *  @return The iterator for the returned set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
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
		if(tdmr.getRange() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setRange((Comparable) Relatrix.firstKey((Class)endarg1));
			} else {
				xdmr.setRange((Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(tdmr, xdmr, dmr_return);
	}
	
	@Override
	public Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
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
		if(tdmr.getRange() == null) {
			if(endarg1 instanceof Class) {
				xdmr.setRange(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg1));
			} else {
				xdmr.setRange(alias,(Comparable)endarg1);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
