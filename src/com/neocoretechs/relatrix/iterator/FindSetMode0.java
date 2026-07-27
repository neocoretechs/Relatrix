package com.neocoretechs.relatrix.iterator;

import java.io.IOException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;

import com.neocoretechs.rocksack.Alias;

/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,*  return identity relationships or 
* This mode returns a one element Result hierarchy
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* morphism object itself,  of the independent objects that comprise the morphism relationship.
* {@link AbstractRelation}
* <p>
* Examples:<br>
* *,*,* domain,map,range order, return identity dmr instance in {@link com.neocoretechs.relatrix.Result1} <br>
* We can substitute a concrete object instance for any of the above wild cards to retrieve only those
* relationships that contain that object instance.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetMode0 extends IteratorFactory {
	// mode 0
	char dop,mop,rop;
	protected short[] dmr_return = new short[4];
	private static boolean DEBUG = false;
    public FindSetMode0(char dop, char mop, char rop) { 	
    	this.dop = dop;
    	this.mop = mop;
    	this.rop = rop;
	    // see if its class or * operator
	    dmr_return[1] = checkOp(dop);
	    dmr_return[2] = checkOp(mop);
	    dmr_return[3] = checkOp(rop);
	    if( isReturnRelationships(dop, mop, rop) )
	    	dmr_return[0] = -1;
    }
    /**
    * @return Iterator for the set, each iterator return is a Comparable array of tuples 
    */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		AbstractRelation dmr = new Relation(true, null, null, null);
	
		if( DEBUG  )
			System.out.println("Relatrix FindsetMode0.createIterator setting search for "+dmr);
	    return createRelatrixIterator(dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIterator(tdmr, dmr_return);
	}
	
    /**
    * @return Iterator for the set, each iterator return is a Comparable array of tuples
    */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation dmr = new Relation(true, alias, null, null, null);
		if( DEBUG  )
			System.out.println("Relatrix FindsetMode0.createIterator alias:"+alias+" setting search for "+dmr);
	    return createRelatrixIterator(alias, dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIterator(alias, tdmr, dmr_return);
	}
	
}
