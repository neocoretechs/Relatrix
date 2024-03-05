package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the transaction relation via the specified predicate. Mode 3 = findSet("?|*",object,object)
* returns a 1 element  {@link Result}  with the identity findSet("*",object,object) for all elements matching the
* last 2 objects. In the case of findSet("?",object,object) a  {@link Result1} is returned for each iteration
* and it contains the object functioning as the domain in all relationships where the last 2 objects are the map and range.
* Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
* 
*/
public class FindSetStreamMode3Transaction extends FindSetStreamMode3 {
	// mode 3
	String xid;
    public FindSetStreamMode3Transaction(String xid, char dop, Object marg, Object rarg) { 	
    	super(dop, marg, rarg);
    	this.xid = xid;
    }

    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new MapRangeDomain(true, xid, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException {
	    Morphism dmr = new MapRangeDomain(true, alias, xid, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(alias, dmr);
	}
	@Override
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
	@Override
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(alias, xid, tdmr, dmr_return);
	}
}
