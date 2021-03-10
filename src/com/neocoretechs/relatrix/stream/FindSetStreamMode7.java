package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DomainMapRange;


/**
* Identity morphism retrieval.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105,2017,2021
*/
public class FindSetStreamMode7 extends StreamFactory {
	// mode 7
	Object darg,marg,rarg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode7(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	this.darg = darg;
    	this.marg = marg;
    	this.rarg = rarg;
    	dmr_return[1] = 0;
        dmr_return[2] = 0;
        dmr_return[3] = 0;
        if( isReturnRelationships(dmr_return) )
        	dmr_return[0] = -1;
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new DomainMapRange((Comparable)darg, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
