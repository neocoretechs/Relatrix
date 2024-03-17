package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;


/**
* Mode 7, transaction context stream when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
* In this permutation we are dealing with 3 objects represent identity so we have the option
* of setting a retrieval bringing in the identities in their natural d,m,r order.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* This winds up serving 2 functions. One is identity morphism retrieval if we get 3 objects that are not
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindSubSetStreamMode7Transaction extends FindSubSetStreamMode7 {
	String xid;
	// mode 7
    public FindSubSetStreamMode7Transaction(String xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg, endarg);
    	this.xid= xid;
    }
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixSubsetStreamTransaction(xid, tdmr, zdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixSubsetStreamTransaction(alias, xid, tdmr, zdmr, xdmr, ydmr, dmr_return);
	}

}
