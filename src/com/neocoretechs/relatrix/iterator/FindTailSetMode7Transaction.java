package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Find elements greater or equal to 'from' element.
 * Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
 * We have an instance of 3 objects, we return the identity relationship objects from the beginning to this identity.
 * the argument acts as a wildcard (*) or a tuple (?) for instances of that
 * class. The objects returned will be of type {@link DomainMapRange} subclass of {@link Morphism}. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015.2021 
 */
public class FindTailSetMode7Transaction extends FindSetMode7Transaction {
	Object[] endarg;
	// mode 7
	public FindTailSetMode7Transaction(TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
		super(xid, darg, marg, rarg);
		if(endarg.length != 0)
			throw new RuntimeException("Must not supply any qualifying arguments for Tailset.");
		this.endarg = endarg;
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
