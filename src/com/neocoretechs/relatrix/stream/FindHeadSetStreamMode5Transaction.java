package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;


/**
* Mode 5. Permutation with 2 objects in a transaction context.
* Find the set of objects in the relation via the specified predicate. 
* Legal permutations are:<br/>
* [object],*,[object] <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
*  @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindHeadSetStreamMode5Transaction extends FindHeadSetStreamMode5 {
	String xid;
    public FindHeadSetStreamMode5Transaction(String xid, Object darg, char mop, Object rarg) { 	
    	super(darg, mop, rarg);
    	this.xid = xid;
    }
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return (Stream<?>) new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
