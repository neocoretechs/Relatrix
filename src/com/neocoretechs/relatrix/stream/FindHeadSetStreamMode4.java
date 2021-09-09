package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],*,* 
* [object],*,? 
* [object],?,? 
* [object],?,*
* [TemplateClass],*,* 
* [TemplateClass],*,? 
* [TemplateClass],?,? 
* [TemplateClass],?,*
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode4 extends FindSetStreamMode4 {
    public FindHeadSetStreamMode4(Object darg, char mop, char rop) { 	
    	super(darg, mop, rop);
    }
    /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return (Stream<?>) new RelatrixHeadsetStream(tdmr, dmr_return);
	}
}
