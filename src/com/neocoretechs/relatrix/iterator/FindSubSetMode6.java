package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate. 
* Mode 6 = findSeT(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are
* [object],[object],* [object],[object],?
* [TemplateClass],[TemplateClass],* [TemplateClass],[TemplateClass],?
*
*/
public class FindSubSetMode6 extends FindSetMode6 {
	Object[] xarg;
    public FindSubSetMode6(Object darg, Object marg, char rop, Object ...xarg) { 	
    	super(darg,marg, rop);
       	this.xarg = xarg;
    	if(xarg.length != 2) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', expected 2 got "+xarg.length);
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
			   if( tdmr.getDomain() != null ) {
					  templdmr.setDomain((Comparable) xarg[ipos++]); 
			   }
			   if( tdmr.getMap() != null ) {
					  templdmr.setMap((Comparable) xarg[ipos++]); 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return new RelatrixSubsetIterator(BigSackAdapter.getBigSackTransactionalTreeSet(tdmr), tdmr, templdmr, dmr_return);
	   }
}
