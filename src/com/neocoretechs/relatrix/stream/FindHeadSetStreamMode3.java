package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Find the head set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],[object] ?,[object],[object]
* *,[TemplateClass],[TemplateClass] ?,[TemplateClass],[TemplateClass]
* 
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
	    return (Stream<?>) new RelatrixHeadsetStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
