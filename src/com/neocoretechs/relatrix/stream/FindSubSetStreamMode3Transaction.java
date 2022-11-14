package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 3. The findSet transaction stream contains two object references, therefore the subset variable array must also.
* Find the head set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* * The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
 * specified in the variable parameters, in this case 2. Since we are returning a range of concrete objects we need to include
 * these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
*  @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindSubSetStreamMode3Transaction extends FindSetStreamMode3Transaction {
	Object[] xarg;
    public FindSubSetStreamMode3Transaction(String xid, char dop, Object marg, Object rarg, Object ...xarg) { 	
    	super(xid, dop, marg, rarg);
    	dmr_return[2] = 1;
    	dmr_return[3] = 1;
		this.xarg = xarg;
		if(xarg.length != 2) throw new RuntimeException("Wrong number of end range arguments for 'findSubSetStream', expected 2 got "+xarg.length);
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
    @Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		// make a new Morphism template
		Morphism templdmr;
		try {
			// primarily for class type than values of instance
			templdmr = (Morphism) tdmr.clone();
			// move the end range into the new template in the proper position
			int ipos = 0;
			if( tdmr.getMap() != null ) {
				templdmr.setMapTemplate((Comparable) xarg[ipos++]); 
			}
			if( tdmr.getRange() != null ) {
				templdmr.setRangeTemplate((Comparable) xarg[ipos++]); 
			}
		} catch (CloneNotSupportedException e) {
			throw new IOException(e);
		}
		return (Stream<?>) new RelatrixSubsetStreamTransaction(xid, tdmr, templdmr, dmr_return);
	}
}
