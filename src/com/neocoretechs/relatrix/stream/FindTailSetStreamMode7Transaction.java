package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc. from a transaction context.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. 
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindTailSetStreamMode7Transaction extends FindTailSetStreamMode7 {
	String xid;
	// mode 7
    public FindTailSetStreamMode7Transaction(String xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg, endarg);
    	this.xid = xid;
		if(endarg.length != 0)
			throw new RuntimeException("Must not supply any qualifying arguments for Tailset.");
    }
 
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
	    return new RelatrixTailsetStreamTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
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
		return new RelatrixTailsetStreamTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
