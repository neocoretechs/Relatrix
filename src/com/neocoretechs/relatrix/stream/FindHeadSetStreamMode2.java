package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* 
* *,[object],? 
* ?,[object],? 
* ?,[object],*
* *,[TemplateClass],*
* *,[TemplateClass],? 
* ?,[TemplateClass],? 
* ?,[TemplateClass],*
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode2 extends FindSetStreamMode2 {
    public FindHeadSetStreamMode2(char dop, Object marg, char rop) { 	
    	super(dop, marg, rop);
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return (Stream<?>) new RelatrixHeadsetStream(tdmr, dmr_return);
	}
}
