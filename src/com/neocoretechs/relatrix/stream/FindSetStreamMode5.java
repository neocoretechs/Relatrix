package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DomainRangeMap;

/**
* Find the set of objects in the relation via the specified predicate. 
* This mode is for (object,"?|*",object) returning 1 object or identity, but always a 1 element array of Comparable
* where the map is returned where it is a component of the relationship, or an identity Morphism subclass
* in the case of findSet(object,"*",object) where the identity is returned for each relationship where the objects match
* the specified two objects in the findSet.
* Legal permutations are
* [object],*,[object] [object],?,[object]
* [TemplateClass],*,[TemplateClass] [TemplateClass],?,[TemplateClass]
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105,2021
* 
*/
public class FindSetStreamMode5 extends StreamFactory {
	// mode 5
	char mop;
	Object darg,rarg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode5(Object darg, char mop, Object rarg) { 	
    	this.darg = darg;
    	this.mop = mop;
    	this.rarg = rarg;
	    // see if its ? or * operator
    	dmr_return[1] = 0;
    	// 'map' object
    	// see if its ? or * operator
    	dmr_return[2] = checkOp(mop);
    	//  'range'
    	dmr_return[3] = 0;
    }
    /**
     * @return stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		Morphism dmr = new DomainRangeMap((Comparable)darg, null, (Comparable)rarg);
		return createRelatrixStream(dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr)throws IllegalAccessException, IOException {
		return new RelatrixStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
