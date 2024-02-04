package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the head set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode3 extends FindSetStreamMode3 {

    public FindHeadSetStreamMode3(char dop, Object marg, Object rarg) { 	
    	super(dop, marg, rarg);
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
	    return new RelatrixHeadsetStream(tdmr, dmr_return);
	}
	/**
	 * Create the specific stream. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixHeadsetStream(alias, tdmr, dmr_return);
	}
}
