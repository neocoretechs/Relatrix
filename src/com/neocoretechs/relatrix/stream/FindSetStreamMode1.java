package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RangeDomainMap;

/**
* Find the set of objects in the relation via the specified predicate. Mode 1 = findset("*|?","*|?",object)
* returning identity, 1 or 2 element Comparable array of tuples for each iteration of the retrieval.
* Legal permutations are: <br/>
* *,*,[object] <br/>
* *,?,[object] <br/>
* ?,?,[object] <br/>
* ?,*,[object] <br/>
* *,*,[TemplateClass] <br/>
* *,?,[TemplateClass] <br/>
* ?,?,[TemplateClass] <br/>
* ?,*,[TemplateClass] <br/>
* * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*
*/
public class FindSetStreamMode1 extends StreamFactory {
	// mode 1
	char dop,mop;
	Object rarg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode1(char dop, char mop, Object rarg) { 	
    	this.dop = dop;
    	this.mop = mop;
    	this.rarg = rarg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	dmr_return[2] = checkOp(mop);
    	dmr_return[3] = 0;
    }
    /**
     *  @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new RangeDomainMap(null, null, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStream(tdmr, dmr_return);
	}
}
