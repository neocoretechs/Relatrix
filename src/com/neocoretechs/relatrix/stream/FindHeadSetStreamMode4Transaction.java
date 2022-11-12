package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate in a transaction context. Legal permutations are
Legal permutations are:<br/>
* [object],*,* <br/>
* [object],*,?  <br/>
* [object],?,?  <br/>
* [object],?,* <br/>
* [TemplateClass],*,* <br/>
* [TemplateClass],*,? <br/>
* [TemplateClass],?,? <br/>
* [TemplateClass],?,* <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindHeadSetStreamMode4Transaction extends FindHeadSetStreamMode4 {
	String xid;
    public FindHeadSetStreamMode4Transaction(String xid, Object darg, char mop, char rop) { 	
    	super(darg, mop, rop);
    	this.xid = xid;
    }
    /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return (Stream<?>) new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
