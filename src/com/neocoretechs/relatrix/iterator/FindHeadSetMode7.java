package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.rocksack.Alias;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. Find elements strictly less than 'to' target.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015.2021 
*/
public class FindHeadSetMode7 extends FindSetMode7 {
	Object[] endarg;
	// mode 7
    public FindHeadSetMode7(Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
		if(endarg.length != 0)
			throw new RuntimeException("Must not supply any qualifying arguments for Headset.");
		this.endarg = endarg;
    }
 
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
	    return new RelatrixHeadsetIterator(tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixHeadsetIterator(alias, tdmr, xdmr, dmr_return);
	}
}
