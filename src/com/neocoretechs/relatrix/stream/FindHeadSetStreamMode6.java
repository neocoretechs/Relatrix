package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are
* [object],[object],* 
* [object],[object],?
* [TemplateClass],[TemplateClass],* 
* [TemplateClass],[TemplateClass],?
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode6 extends FindSetStreamMode6 {
    public FindHeadSetStreamMode6(Object darg, Object marg, char rop) { 	
    	super(darg,marg, rop);
    }

	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr)throws IllegalAccessException, IOException {
	    return (Stream<?>) new RelatrixHeadsetStream(tdmr, dmr_return);
	}
}
