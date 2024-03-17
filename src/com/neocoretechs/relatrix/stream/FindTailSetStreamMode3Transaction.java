package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.key.DBKey;

/**
* Find the tail set of objects in the relation via the specified predicate in a transaction context. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindTailSetStreamMode3Transaction extends FindTailSetStreamMode3 {
	String xid;
	Object[] endarg;
    public FindTailSetStreamMode3Transaction(String xid, char dop, Object marg, Object rarg, Object ... endarg) { 	
    	super(dop, marg, rarg, endarg);
    	this.xid = xid;
      	if(endarg.length != 1)
    		throw new RuntimeException("Must supply 1 qualifying argument for Tailset domain.");
    }
	/**
	 * Create the specific stream. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain((Comparable) RelatrixKVTransaction.firstKey(xid,(Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[0]);
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixTailsetStreamTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
	}
	/**
	 * Create the specific stream. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain(alias,(Comparable) RelatrixKVTransaction.firstKey(alias,xid,(Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain(alias,(Comparable)endarg[0]);
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
    	return new RelatrixTailsetStreamTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
