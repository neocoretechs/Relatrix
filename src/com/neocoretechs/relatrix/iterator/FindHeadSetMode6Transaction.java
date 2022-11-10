package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate strictly less than 'to' target. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],* <br/>
* [object],[object],? <br/>
* [TemplateClass],[TemplateClass],* <br/>
* [TemplateClass],[TemplateClass],? <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindHeadSetMode6Transaction extends FindSetMode6Transaction {
    public FindHeadSetMode6Transaction(String xid, Object darg, Object marg, char rop) { 	
    	super(xid, darg, marg, rop);
    }

	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr)throws IllegalAccessException, IOException {
	    return new RelatrixHeadsetIteratorTransaction(xid, tdmr, dmr_return);
	}
}
