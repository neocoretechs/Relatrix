package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate in a transaction context. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],* <br/>
* [object],[object],? <br/>
* [TemplateClass],[TemplateClass],* <br/>
* [TemplateClass],[TemplateClass],? <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
*/
public class FindHeadSetStreamMode6Transaction extends FindHeadSetStreamMode6 {
	String xid;
    public FindHeadSetStreamMode6Transaction(String xid, Object darg, Object marg, char rop) { 	
    	super(darg,marg, rop);
    	this.xid = xid;
    }

	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr)throws IllegalAccessException, IOException {
	    return (Stream<?>) new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
