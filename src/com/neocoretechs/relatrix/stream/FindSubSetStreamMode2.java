package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
* 
*/
public class FindSubSetStreamMode2 extends FindSetStreamMode2 {
	Object[] xarg;
    public FindSubSetStreamMode2(char dop, Object marg, char rop, Object ... xarg ) { 	
    	super(dop, marg, rop);
		this.xarg = xarg;
		if(xarg.length != 2) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', expected 2 got "+xarg.length);
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
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
			   if( tdmr.map != null ) {
					  templdmr.map = (Comparable) xarg[ipos++]; 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return (Stream<?>) new RelatrixSubsetStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, templdmr, dmr_return);
	   }
}
