package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Find the head set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],[object] ?,[object],[object]
* *,[TemplateClass],[TemplateClass] ?,[TemplateClass],[TemplateClass]
* 
*/
public class FindHeadSetMode3 extends FindSetMode3 {

    public FindHeadSetMode3(char dop, Object marg, Object rarg) { 	
    	super(dop, marg, rarg);
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
	    return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	}
}
