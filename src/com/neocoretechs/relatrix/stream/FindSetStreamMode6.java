package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DomainMapRange;

/**
* Find the set of objects in the relation via the specified predicate. Mode 6 is for findSet(object,object,"?|*")
* where a 1 element Comparable array is returned from each iteration of the constructed iterator returned from findSet.
* The array has either the identity where the first 2 objects are components of the relationship for each iterated element of the 
* findSet, or the object functioning as the range (codomain) for each iteration where the first 2 objects are domain and
* map.
* Legal permutations are
* [object],[object],* [object],[object],?
* [TemplateClass],[TemplateClass],* [TemplateClass],[TemplateClass],?
* @author jg Groff Copyright (C) NeoCoreTechs 2014,2105
*
*/
public class FindSetStreamMode6 extends StreamFactory {
	// mode 6
	char rop;
	Object darg,marg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode6(Object darg, Object marg, char rop) { 	
    	this.darg = darg;
    	this.marg = marg;
    	this.rop = rop;
    	dmr_return[1] = 0;
    	// 'map' object
    	dmr_return[2] = 0;
    	//  'range'
    	// see if its ? or * operator
    	dmr_return[3] = checkOp(rop);
    }
    /**
     *  @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new DomainMapRange((Comparable)darg, (Comparable)marg, null);
	    return createRelatrixStream(dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr)throws IllegalAccessException, IOException {
	    return new RelatrixStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
