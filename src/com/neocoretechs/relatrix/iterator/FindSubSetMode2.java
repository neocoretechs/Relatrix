package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
* 
*/
public class FindSubSetMode2 extends FindSetMode2 {
	Object[] xarg;
    public FindSubSetMode2(char dop, Object marg, char rop, Object ... xarg ) { 	
    	super(dop, marg, rop);
		this.xarg = xarg;
		assert(xarg.length == 1) : "Wrong variable argument length to FindSubsetMode2, expected 2 got "+xarg.length;
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
			   if( tdmr.map != null ) {
					  templdmr.map = (Comparable) xarg[ipos++]; 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return new RelatrixSubsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, templdmr, dmr_return);
	   }
}