package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.DomainMapRange;

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
  
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
}
