package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7 all objects. Take subset of identity Morphisms from instance d, m, r
* in findSet predicate to range of d,m,r in suffix of 3 concrete instances.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2024
*/
public class FindSubSetMode7 extends FindSetMode7 {
	Object[] endarg;
	int argCtr = 0;
	// mode 7
    public FindSubSetMode7(Object darg, Object marg, Object rarg, Object ... endarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
       	this.endarg = endarg;
    	if(endarg.length != 3) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() != null) {
			ydmr.setDomain((Comparable)endarg[argCtr++]);
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple are all null
		if(tdmr.getMap() != null) {
			ydmr.setMap((Comparable)endarg[argCtr++]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() != null) {
			ydmr.setRange((Comparable)endarg[argCtr++]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixSubsetIterator(tdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() != null) {
			ydmr.setDomain(alias,(Comparable)endarg[argCtr++]);
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple are all null
		if(tdmr.getMap() != null) {
			ydmr.setMap(alias,(Comparable)endarg[argCtr++]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		if(tdmr.getRange() != null) {
			ydmr.setRange(alias,(Comparable)endarg[argCtr++]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixSubsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
