package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Mode 4.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* [object],*,* [object],*,? [object],?,? [object],?,*
* [TemplateClass],*,* [TemplateClass],*,? [TemplateClass],?,? [TemplateClass],?,*
*
*/
public class FindSubSetMode4 extends FindSetMode4 {
	Object[] xarg;
    public FindSubSetMode4(Object darg, char mop, char rop, Object ... xarg) { 	
    	super(darg, mop, rop);
		this.xarg = xarg;
		if(xarg.length != 1) throw new RuntimeException("Wrong number of end range arguments for 'findSubSet', expected 1 got "+xarg.length);
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	  @Override
	  protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		   // make a new Morphism template
		   Morphism templdmr;
		   try {
			   // primarily for class type than values of instance
			   templdmr = (Morphism) tdmr.clone();
			   // move the end range into the new template in the proper position
			   int ipos = 0;
			   if( tdmr.domain != null ) {
					  templdmr.domain = (Comparable) xarg[ipos++]; 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return new RelatrixSubsetIterator(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, templdmr, dmr_return);
	   }
}
