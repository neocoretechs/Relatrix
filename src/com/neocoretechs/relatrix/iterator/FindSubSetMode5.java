package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;

/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],*,[object] [object],?,[object]
* [TemplateClass],*,[TemplateClass] [TemplateClass],?,[TemplateClass]
* 
*/
public class FindSubSetMode5 extends FindSetMode5 {
	Object[] xarg;
    public FindSubSetMode5(Object darg, char mop, Object rarg, Object ... xarg) { 	
    	super(darg, mop, rarg);
    	this.xarg = xarg;
		assert(xarg.length == 2) : "Wrong variable argument length to FindSubsetMode5, expected 2 got "+xarg.length;
    }
	
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	  @Override
	  protected Iterator<?> createRelatrixIterator(DMRStruc tdmr) throws IllegalAccessException, IOException {
		   // make a new DMRStruc template
		   DMRStruc templdmr;
		   try {
			   // primarily for class type than values of instance
			   templdmr = (DMRStruc) tdmr.clone();
			   // move the end range into the new template in the proper position
			   int ipos = 0;
			   if( tdmr.domain != null ) {
					  templdmr.domain = (Comparable) xarg[ipos++]; 
			   }
			   if( tdmr.range != null ) {
					  templdmr.range = (Comparable) xarg[ipos++]; 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return new RelatrixSubsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, templdmr, dmr_return);
	   }
}
