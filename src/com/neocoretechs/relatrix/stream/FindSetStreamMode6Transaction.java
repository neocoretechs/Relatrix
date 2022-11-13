package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation transaction via the specified predicate. Mode 6 is for findSet(object,object,"?|*")
* where a 1 element Comparable array is returned from each iteration of the constructed iterator returned from findSet.
* The array has either the identity where the first 2 objects are components of the relationship for each iterated element of the 
* findSet, or the object functioning as the range (codomain) for each iteration where the first 2 objects are domain and
* map.<br/>
* Legal permutations are:<br/>
* [object],[object],* <br/>
* [object],[object],? <br/>
* [TemplateClass],[TemplateClass],* <br/>
* [TemplateClass],[TemplateClass],? <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
*
*/
public class FindSetStreamMode6Transaction extends FindSetStreamMode6 {
	// mode 6
	String xid;
    public FindSetStreamMode6Transaction(String xid, Object darg, Object marg, char rop) { 	
    	super(darg, marg, rop);
    	this.xid = xid;
    }

	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr)throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
}
