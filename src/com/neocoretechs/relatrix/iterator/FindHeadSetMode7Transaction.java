package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;


/**
 * Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
 * We have an instance of 3 objects, we return the identity relationship objects from the beginning to this identity.
 * the argument acts as a wildcard (*) or a tuple (?) for instances of that
 * class. The objects returned will be of type {@link Relation} subclass of {@link AbstractRelation}. 
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
 */
public class FindHeadSetMode7Transaction extends FindSetMode7Transaction {
	public FindHeadSetMode7Transaction(TransactionId xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
		super(xid, darg, marg, rarg);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixHeadsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
