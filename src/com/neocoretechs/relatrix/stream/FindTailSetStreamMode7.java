package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* The class functions as template for the enclosed class to retrieve objects of that class (or subclass).
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindTailSetStreamMode7 extends FindSetStreamMode7 {
	Object[] endarg;
	// mode 7
    public FindTailSetStreamMode7(Object darg, Object marg, Object rarg, Object ... endarg ) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
		if(endarg.length != 0)
			throw new RuntimeException("Must not supply any qualifying arguments for Tailset.");
		this.endarg = endarg;
    }
 
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixTailsetStream(tdmr, xdmr, ydmr, dmr_return);
	}
	/**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixTailsetStream(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
