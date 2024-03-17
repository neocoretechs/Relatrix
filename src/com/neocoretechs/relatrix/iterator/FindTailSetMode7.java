package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. 
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015.2021 
*/
public class FindTailSetMode7 extends FindSetMode7 {
	Object[] endarg;
	// mode 7
    public FindTailSetMode7(Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
		if(endarg.length != 0)
			throw new RuntimeException("Must not supply any qualifying arguments for Tailset.");
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
		return new RelatrixTailsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
