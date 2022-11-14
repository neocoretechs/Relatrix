package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc. from a transaction context.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* This winds up serving 2 functions. One is identity morphism retrieval if we get 3 objects that are not
* part of TemplateClass retrieval. the second is when one or more params is instanceof TemplateClass. In the second
* instance, the class functions as template for the enclosed class to retrieve objects of that class (or subclass).
* Depending on the subclass of TemplateClass, the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. Category theory analog is a representable. 
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindHeadSetStreamMode7Transaction extends FindHeadSetStreamMode7 {
	String xid;
	// mode 7
    public FindHeadSetStreamMode7Transaction(String xid, Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
    	this.xid = xid;
    }
 
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
