package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.rocksack.Alias;

/**
* Find elements greater or equal to 'from' element.
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. 
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015.2021 
*/
public class FindTailSetMode7 extends FindSetMode7 {
	// mode 7
    public FindTailSetMode7(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
    }
 
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
	    return new RelatrixTailsetIterator(tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
