package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MapRangeDomain;


/**
* Find the set of objects in the relation via the specified predicate. Mode 3 = findSet("?|*",object,object)
* returns a 1 element Comparable with the identity findSet("*",object,object) for all elements matching the
* last 2 objects. In the case of findSet("?",object,object) a Comparable[1] is return for each iteration
* and it contains the object functioning as the domain in all relationships where the last 2 objects are the map and range.
Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetMode3Transaction extends FindSetMode3 {
	// mode 3
	String xid;
    public FindSetMode3Transaction(String xid, char dop, Object marg, Object rarg) { 	
    	super(dop, marg, rarg);
    	this.xid = xid;
    }
    @Override
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}
