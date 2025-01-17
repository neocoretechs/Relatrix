package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Find elements greater or equal to 'from' element
 * Legal permutations are:<br/>
 * *,[object],[object],[class] <br/>
 * ?,[object],[object],[object] <br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 * 
 */
public class FindTailSetMode3Transaction extends FindSetMode3Transaction {
	Object[] endarg;
	public FindTailSetMode3Transaction(TransactionId xid, char dop, Object marg, Object rarg, Object ... endarg) { 	
		super(xid, dop, marg, rarg);
       	if(endarg.length != 1)
    		throw new RuntimeException("Must supply 1 qualifying argument for Tailset domain.");
     	this.endarg = endarg;
	}
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg[0]));
			} else {
				xdmr.setDomain((Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIteratorTransaction(xid, tdmr, xdmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[0]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[0]);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixTailsetIteratorTransaction(alias, xid, tdmr, xdmr, dmr_return);
	}
}
