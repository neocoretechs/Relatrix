package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the head set of objects in the relation via the specified predicate in a transaction context. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindHeadSetStreamMode3Transaction extends FindHeadSetStreamMode3 {
	String xid;
    public FindHeadSetStreamMode3Transaction(String xid, char dop, Object marg, Object rarg) { 	
    	super(dop, marg, rarg);
    	this.xid = xid;
    }
	/**
	 * Create the specific stream. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return (Stream<?>) new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
