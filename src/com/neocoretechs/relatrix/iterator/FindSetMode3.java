package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.MapRangeDomain;

/**
* Find the set of objects in the relation via the specified predicate. Mode 3 = findSet("?|*",object,object)
* returns a 1 element Result with the identity findSet("*",object,object) for all elements matching the
* last 2 objects. In the case of findSet("?",object,object) a {@link com.neocoretechs.relatrix.Result1} is return for each iteration
* and it contains the object functioning as the domain in all relationships where the last 2 objects are the map and range.
Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetMode3 extends IteratorFactory {
	// mode 3
	char dop;
	Object marg,rarg;
	short[] dmr_return = new short[4];
    public FindSetMode3(char dop, Object marg, Object rarg) { 	
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
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    AbstractRelation dmr = new MapRangeDomain(true, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixIterator(dmr);
	}
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIterator( tdmr, dmr_return);
	}
	
	   /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
	    AbstractRelation dmr = new MapRangeDomain(true, alias, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixIterator(alias, dmr);
	}
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIterator(alias, tdmr, dmr_return);
	}
}
