package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainRangeMapTransaction;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;

/**
* Find the set of objects in the transaction relation via the specified predicate. 
* This mode is for (object,"?|*",object) returning 1 object or identity, but always a 1 element array of Comparable
* where the map is returned where it is a component of the relationship, or an identity Morphism subclass
* in the case of findSet(object,"*",object) where the identity is returned for each relationship where the objects match
* the specified two objects in the findSet.
* Legal permutations are:<br/>
* [object],*,[object]  <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105,2021,2022
* 
*/
public class FindSetStreamMode5Transaction extends FindSetStreamMode5 {
	// mode 5
	String xid;
    public FindSetStreamMode5Transaction(String xid, Object darg, char mop, Object rarg) { 	
    	super(darg, mop, rarg);
    	this.xid = xid;
    }

    /**
     * @return stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		MorphismTransaction dmr = new DomainRangeMapTransaction((Comparable)darg, null, (Comparable)rarg);
		return createRelatrixStream(dmr);
	}
	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr)throws IllegalAccessException, IOException {
		return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
}
