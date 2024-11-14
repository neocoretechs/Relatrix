package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindTailSetMode1Transaction extends FindSetMode1Transaction {
		Object[] endarg;
	   public FindTailSetMode1Transaction(TransactionId xid, char dop, char mop, Object rarg, Object ... endarg ) { 	
		   super(xid,dop,mop,rarg);
			if(endarg.length != 2)
				throw new RuntimeException("Must supply 2 qualifying arguments for Tailset domain and map.");
			this.endarg = endarg;
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
			Morphism xdmr = null;
			try {
				xdmr = (Morphism) tdmr.clone(); // concrete instance in range
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[0] instanceof Class) {
					xdmr.setDomain((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[0]));
				} else {
					xdmr.setDomain((Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			if(tdmr.getMap() == null) {
				if(endarg[1] instanceof Class) {
					xdmr.setMap((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[1]));
				} else {
					xdmr.setMap((Comparable)endarg[1]);
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
		    return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
			Morphism xdmr = null;
			try {
				xdmr = (Morphism) tdmr.clone(); // concrete instance in range
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[0] instanceof Class) {
					xdmr.setDomain(alias, (Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[0]));
				} else {
					xdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			if(tdmr.getMap() == null) {
				if(endarg[1] instanceof Class) {
					xdmr.setMap(alias,(Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[1]));
				} else {
					xdmr.setMap(alias,(Comparable)endarg[1]);
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
		    return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	   }
}
