package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode1Transaction extends FindSetMode1Transaction {
		Object[] endarg;
	   public FindHeadSetMode1Transaction(TransactionId xid, char dop, char mop, Object rarg, Object ... endarg ) { 	
		   super(xid,dop,mop,rarg);
			if(endarg.length != 2)
				throw new RuntimeException("Must supply 2 qualifying arguments for Headset domain and map.");
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
					xdmr.setDomain((Comparable) RelatrixKVTransaction.lastKey(xid,(Class)endarg[0]));
				} else {
					xdmr.setDomain((Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			if(tdmr.getMap() == null) {
				if(endarg[1] instanceof Class) {
					xdmr.setMap((Comparable) RelatrixKVTransaction.lastKey(xid,(Class)endarg[1]));
				} else {
					xdmr.setMap((Comparable)endarg[1]);
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
		    return new RelatrixHeadsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
			Morphism xdmr = null;
			try {
				xdmr = (Morphism) tdmr.clone(); // concrete instance in range
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[0] instanceof Class) {
					xdmr.setDomain(alias, (Comparable) RelatrixKVTransaction.lastKey(alias,xid,(Class)endarg[0]));
				} else {
					xdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			if(tdmr.getMap() == null) {
				if(endarg[1] instanceof Class) {
					xdmr.setMap(alias,(Comparable) RelatrixKVTransaction.lastKey(alias,xid,(Class)endarg[1]));
				} else {
					xdmr.setMap(alias,(Comparable)endarg[1]);
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
		    return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	   }
}
