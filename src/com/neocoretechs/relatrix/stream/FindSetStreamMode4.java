package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.DomainMapRange;

/**
* Find the set of objects in the relation via the specified predicate. 
* This variation accommodates findSet(object,"*|?","*|?") which returns a 1 or 2 element Comparable
* for each iteration wherein the object specified in the domain functions as the domain in the retrieved relationships.
* Legal permutations are:<br/>
* [object],*,* <br/>
* [object],*,?  <br/>
* [object],?,?  <br/>
* [object],?,* <br/>
* [TemplateClass],*,* <br/>
* [TemplateClass],*,? <br/>
* [TemplateClass],?,? <br/>
* [TemplateClass],?,* <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetStreamMode4 extends StreamFactory {
	// mode 4
	char mop, rop;
	Object darg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode4(Object darg, char mop, char rop) { 	
    	this.darg = darg;
    	this.mop = mop;
    	this.rop = rop;
	    // see if its ? or * operator
    	dmr_return[1] = 0;
        // map
        // see if its ? or * operator
        dmr_return[2] = checkOp(mop);
        // range
        dmr_return[3] = checkOp(rop);
    }
    /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		Morphism dmr = new DomainMapRange(true,(Comparable)darg, null, null);
		return createRelatrixStream(dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixStream(tdmr, dmr_return);
	}
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism dmr = new DomainMapRange(true, alias, (Comparable)darg, null, null);
		return createRelatrixStream(alias, dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism dmr = new DomainMapRange(true, alias, (Comparable)darg, null, null);
		return createRelatrixStream(alias, dmr);
	}
}
