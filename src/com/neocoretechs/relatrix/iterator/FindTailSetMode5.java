package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;


/**
* Mode 5. Permutation with 2 objects.
* Legal permutations are:<br/>
* [object],*,[object],... <br/>
* [object],?,[object],... <br/>
* Concrete domain and range
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindTailSetMode5 extends FindSetMode5 {
	Object[] endarg;
    public FindTailSetMode5(Object darg, char mop, Object rarg, Object ... endarg) { 	
    	super(darg, mop, rarg);
		if(endarg.length != 1)
			throw new RuntimeException("Must supply 1 qualifying argument for Tailset map.");
		this.endarg = endarg;
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setMap((Comparable) RelatrixKV.firstKey((Class)endarg[0]));
			} else {
				xdmr.setMap((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(tdmr, xdmr, ydmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getMap() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setMap(alias,(Comparable) RelatrixKV.firstKey(alias,(Class)endarg[0]));
			} else {
				xdmr.setMap(alias,(Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
