package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;

/**
* Find the head set of objects in the relation via the specified predicate strictly less than 'to' target. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* Concrete instances in map and range
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindHeadSetMode3 extends FindSetMode3 {
	Object endarg0;
    public FindHeadSetMode3(char dop, Object marg, Object rarg, Object arg1) { 	
    	super(dop, marg, rarg);
     	endarg0 = arg1;
    }
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) Relatrix.lastKey((Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixHeadsetIterator(tdmr, xdmr, dmr_return);
	}
    
    @Override
 	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
 	}
}
