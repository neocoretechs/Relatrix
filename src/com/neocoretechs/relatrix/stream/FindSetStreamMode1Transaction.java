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
public class FindSetStreamMode1Transaction extends FindSetStreamMode1 {
	// mode 1
	String xid;
    public FindSetStreamMode1Transaction(String xid, char dop, char mop, Object rarg) { 	
    	super(dop, mop, rarg);
    	this.xid = xid;
    }
    /**
     *  @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new RangeDomainMap(true, null, xid, null, null, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException {
	    Morphism dmr = new RangeDomainMap(true, alias, xid, null, null, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
}
