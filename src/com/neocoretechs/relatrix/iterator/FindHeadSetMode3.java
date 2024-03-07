package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;

/**
* Find the head set of objects in the relation via the specified predicate strictly less than 'to' target. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindHeadSetMode3 extends FindSetMode3 {
	Object[] endarg;
    public FindHeadSetMode3(char dop, Object marg, Object rarg, Object[] endarg) { 	
    	super(dop, marg, rarg);
       	if(endarg.length != 1)
    		throw new RuntimeException("Must supply 1 qualifying argument for Headset domain.");
     	this.endarg = endarg;
    }
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
			else
				tdmr.setDomain((Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixHeadsetIterator(tdmr, dmr_return);
	}
    
    @Override
 	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class)
				tdmr.setDomain(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
			else
				tdmr.setDomain(alias,(Comparable)endarg[0]);
		} else
			throw new IllegalAccessException("Improper Morphism template.");
 	    return new RelatrixHeadsetIterator(alias, tdmr, dmr_return);
 	}
}
