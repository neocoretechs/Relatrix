package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;

/**
* Identity morphism retrieval in a transaction context.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017,2021,2022
*/
public class FindSetStreamMode7Transaction extends FindSetStreamMode7 {
	// mode 7
	String xid;
    public FindSetStreamMode7Transaction(String xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
    	this.xid = xid;
    }
 
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new DomainMapRange(null, xid, (Comparable)darg, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
	
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
	    Morphism dmr = new DomainMapRange(true, alias, xid, (Comparable)darg, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(alias, dmr);
	}
	
	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixStreamTransaction(alias, xid, tdmr, dmr_return);
	}
}
