package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MapRangeDomain;

/**
* Find the set of objects in the relation via the specified predicate. Mode 3 = findSet("?|*",object,object)
* returns a 1 element Comparable with the identity findSet("*",object,object) for all elements matching the
* last 2 objects. In the case of findSet("?",object,object) a  {@link Result} is return for each iteration
* and it contains the object functioning as the domain in all relationships where the last 2 objects are the map and range.
* Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetStreamMode3 extends StreamFactory {
	// mode 3
	char dop;
	Object marg,rarg;
	short[] dmr_return = new short[4];
    public FindSetStreamMode3(char dop, Object marg, Object rarg) { 	
    	this.dop = dop;
    	this.rarg = rarg;
    	this.marg = marg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	// 'map' object
    	dmr_return[2] = 0;
    	//  'range'
    	dmr_return[3] = 0;
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
	    Morphism dmr = new MapRangeDomain(true, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(dmr);
	}
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStream(tdmr, dmr_return);
	}
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
	    Morphism dmr = new MapRangeDomain(true, alias, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixStream(alias, dmr);
	}
	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixStream(alias, tdmr, dmr_return);
	}
}
