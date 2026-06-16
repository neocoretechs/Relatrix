package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Find elements greater or equal to 'from' element.
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. 
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015.2021,2026 
*/
public class FindTailSetMode7JsonTransaction extends FindSetMode7JsonTransaction {
	// mode 7
    public FindTailSetMode7JsonTransaction(TransactionId xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(xid, darg, marg, rarg);
    }
 
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
	    return new RelatrixTailsetIteratorJsonTransaction(xid, tdmr, xdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixTailsetIteratorJsonTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
