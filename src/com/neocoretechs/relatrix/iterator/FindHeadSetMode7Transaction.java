package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
 * We have an instance of 3 objects, we return the identity relationship objects from the beginning to this identity.
 * the argument acts as a wildcard (*) or a tuple (?) for instances of that
 * class. The objects returned will be of type {@link DomainMapRange} subclass of {@link Morphism}. 
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
 */
public class FindHeadSetMode7Transaction extends FindSetMode7Transaction {
	public FindHeadSetMode7Transaction(TransactionId xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
		super(xid, darg, marg, rarg);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixHeadsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
