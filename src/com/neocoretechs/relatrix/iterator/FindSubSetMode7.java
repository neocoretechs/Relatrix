package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.rocksack.Alias;

/**
* Mode 7 all objects. Take subset of identity Morphisms from instance d, m, r
* in findSet predicate to range of d,m,r in suffix of 3 concrete instances.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2024
*/
public class FindSubSetMode7 extends FindSetMode7 {
	public static boolean DEBUG = false;
	// mode 7
    public FindSubSetMode7(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
    	if(DEBUG)
    		System.out.printf("%s darg:%s marg:%s rarg:%s%n", this.getClass().getName(), darg, marg, rarg);
    }
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixSubsetIterator(tdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		return new RelatrixSubsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
