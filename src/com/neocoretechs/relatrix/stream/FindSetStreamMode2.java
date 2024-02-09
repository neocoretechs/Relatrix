package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MapDomainRange;

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
public class FindSetStreamMode2 extends StreamFactory {
	// mode 2
	char dop,rop;
	Object marg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode2(char dop, Object marg, char rop) { 	
    	this.dop = dop;
    	this.rop = rop;
    	this.marg = marg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	// 'map' object
    	dmr_return[2] = 0;
    	// range, see if its ? or * operator
    	dmr_return[3] = checkOp(rop);
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		Morphism dmr = new MapDomainRange(null, (Comparable)marg, null, true);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixStream(dmr);
	}
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixStream(tdmr, dmr_return);	
	}
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism dmr = new MapDomainRange(alias, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixStream(alias, dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		return new RelatrixStream(alias, tdmr, dmr_return);
	}
}
