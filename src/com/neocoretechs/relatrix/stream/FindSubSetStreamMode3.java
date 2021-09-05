package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Mode 3. The findSet contains two object references, therefore the subset variable array must also.
* Find the head set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],[object] ?,[object],[object]
* *,[TemplateClass],[TemplateClass] ?,[TemplateClass],[TemplateClass]
* 
*/
public class FindSubSetStreamMode3 extends FindSetStreamMode3 {
	Object[] xarg;
    public FindSubSetStreamMode3(char dop, Object marg, Object rarg, Object ...xarg) { 	
    	super(dop, marg, rarg);
		this.xarg = xarg;
		if(xarg.length != 2) throw new RuntimeException("Wrong number of end range arguments for 'findSubSet', expected 2 got "+xarg.length);
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
		   return (Stream<?>) new RelatrixSubsetStream(BigSackAdapter.getBigSackTransactionalTreeSet(tdmr), tdmr, templdmr, dmr_return);
	   }
}
