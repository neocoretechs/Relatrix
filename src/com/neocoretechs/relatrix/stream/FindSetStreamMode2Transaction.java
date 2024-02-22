package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.Morphism;

/**
* Mode 2 find returns a set in map, domain, range order. The map value is matched against the constructor
* value. Mode 2 findSet("?|*",object,"?|*") returning a Comparable of 1 or 2 elements containing the identity
* or tuples from retrieval. For identity, if we specify findSetStream("*",object,"*") we get a Comparable of 1
* element containing a Morphism subclass.
* Find the set of objects in the relation via the specified predicate. * Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],* <br/>
* *,[object],? <br/>
* ?,[object],? <br/>
* ?,[object],* <br/>
* *,[TemplateClass],* <br/>
* *,[TemplateClass],? <br/>
* ?,[TemplateClass],? <br/>
* ?,[TemplateClass],* <br/>
*  <p/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetStreamMode2Transaction extends FindSetStreamMode2 {
	// mode 2
	String xid;
	short[] dmr_return = new short[4];
    public FindSetStreamMode2Transaction(String xid, char dop, Object marg, char rop) { 	
    	super(dop, marg, rop);
    	this.xid = xid;
    }
 
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		Morphism dmr = new MapDomainRange(true, null, xid, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixStream(dmr);
	}
	/**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException {
		Morphism dmr = new MapDomainRange(true, alias, xid, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixStream(dmr);
	}
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixStreamTransaction(xid, tdmr, dmr_return);	
	}
}
