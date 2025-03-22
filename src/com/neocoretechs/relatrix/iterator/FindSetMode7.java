package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.Relation;

/**
* Identity morphism retrieval.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017,2021
*/
public class FindSetMode7 extends IteratorFactory {
	// mode 7
	Object darg,marg,rarg;
	short[] dmr_return = new short[4];
    public FindSetMode7(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	this.darg = darg;
    	this.marg = marg;
    	this.rarg = rarg;
    	dmr_return[1] = 0;
        dmr_return[2] = 0;
        dmr_return[3] = 0;
        if( isReturnRelationships(dmr_return) )
        	dmr_return[0] = -1;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    AbstractRelation dmr = new Relation((Comparable)darg, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixIterator(dmr);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIterator( tdmr, dmr_return);
	}
	
	/**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
	    AbstractRelation dmr = new Relation(alias, (Comparable)darg, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixIterator(alias, dmr);
	}
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIterator(alias, tdmr, dmr_return);
	}
}
