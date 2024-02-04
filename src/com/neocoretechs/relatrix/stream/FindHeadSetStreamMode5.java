package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;


/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate. 
* Legal permutations are:<br/>
* [object],*,[object] <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
*  @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode5 extends FindSetStreamMode5 {

    public FindHeadSetStreamMode5(Object darg, char mop, Object rarg) { 	
    	super(darg, mop, rarg);
    }
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixHeadsetStream(tdmr, dmr_return);
	}
	   /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		return new RelatrixHeadsetStream(alias, tdmr, dmr_return);
	}
}
