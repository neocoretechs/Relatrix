package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* In this permutation we are dealing with 3 objects represent identity so we have the option
* of setting a retrieval bringing in the identities in their natural d,m,r order.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* This winds up serving 2 functions. One is identity morphism retrieval if we get 3 objects that are not
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindSubSetMode7Transaction extends FindSetMode7Transaction {
	Object[] endarg;
	// mode 7
    public FindSubSetMode7Transaction(String xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
    	super(xid, darg, marg, rarg);
       	this.endarg = endarg;
    	if(endarg.length != 0) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixSubsetIterator(tdmr, zdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixSubsetIterator(alias, tdmr, zdmr, xdmr, ydmr, dmr_return);
	}
}
