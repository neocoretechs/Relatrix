package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode1Transaction extends FindSetMode1Transaction {
		Object[] endarg;
	   public FindHeadSetMode1Transaction(String xid, char dop, char mop, Object rarg, Object ... endarg ) { 	
		   super(xid,dop,mop,rarg);
			if(endarg.length != 2)
				throw new RuntimeException("Must supply 2 qualifying arguments for Headset domain and map.");
			this.endarg = endarg;
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
			Morphism xdmr = null;
			Morphism ydmr = null;
			try {
				xdmr = (Morphism) tdmr.clone(); // concrete instance in range
				ydmr = (Morphism) tdmr.clone();
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[0] instanceof Class) {
					tdmr.setDomain((Comparable) RelatrixKVTransaction.lastKey(xid,(Class)endarg[0]));
					xdmr.setDomainKey(DBKey.nullDBKey); // full range
					ydmr.setDomainKey(DBKey.fullDBKey);
				} else {
					tdmr.setDomain((Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
					xdmr.setDomainKey(tdmr.getDomainKey());
					ydmr.setDomainKey(tdmr.getDomainKey());
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			if(tdmr.getMap() == null) {
				if(endarg[1] instanceof Class) {
					tdmr.setMap((Comparable) RelatrixKVTransaction.lastKey(xid,(Class)endarg[1]));
					xdmr.setMapKey(DBKey.nullDBKey); // full range
					ydmr.setMapKey(DBKey.fullDBKey);
				} else {
					tdmr.setMap((Comparable)endarg[1]);
					xdmr.setMapKey(tdmr.getMapKey());
					ydmr.setMapKey(tdmr.getMapKey());
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
		    return new RelatrixHeadsetIteratorTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
			Morphism xdmr = null;
			Morphism ydmr = null;
			try {
				xdmr = (Morphism) tdmr.clone(); // concrete instance in range
				ydmr = (Morphism) tdmr.clone();
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[0] instanceof Class) {
					tdmr.setDomain(alias, (Comparable) RelatrixKVTransaction.lastKey(alias,xid,(Class)endarg[0]));
					xdmr.setDomainKey(DBKey.nullDBKey); // full range
					ydmr.setDomainKey(DBKey.fullDBKey);
				} else {
					tdmr.setDomain(alias,(Comparable)endarg[0]); // same as concrete instance in domain, but we are returning, so for ranging no diff
					xdmr.setDomainKey(tdmr.getDomainKey());
					ydmr.setDomainKey(tdmr.getDomainKey());
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			if(tdmr.getMap() == null) {
				if(endarg[1] instanceof Class) {
					tdmr.setMap(alias,(Comparable) RelatrixKVTransaction.lastKey(alias,xid,(Class)endarg[1]));
					xdmr.setMapKey(DBKey.nullDBKey); // full range
					ydmr.setMapKey(DBKey.fullDBKey);
				} else {
					tdmr.setMap(alias,(Comparable)endarg[1]);
					xdmr.setMapKey(tdmr.getMapKey());
					ydmr.setMapKey(tdmr.getMapKey());
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
		    return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	   }
}
