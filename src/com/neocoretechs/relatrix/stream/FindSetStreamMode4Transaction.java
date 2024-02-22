package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the transaction relation via the specified predicate. 
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
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
*
*/
public class FindSetStreamMode4Transaction extends FindSetStreamMode4 {
	// mode 4
	String xid;
    public FindSetStreamMode4Transaction(String xid, Object darg, char mop, char rop) { 	
    	super(darg, mop, rop);
    	this.xid = xid;
    }
    
    /**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		Morphism dmr = new DomainMapRange(true, null, (Comparable)darg, null, null);
		return createRelatrixStream(dmr);
	}
	/**
     *  @return The stream for the returned set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException {
		Morphism dmr = new DomainMapRange(true, alias, xid, (Comparable)darg, null, null);
		return createRelatrixStream(dmr);
	}
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
}
