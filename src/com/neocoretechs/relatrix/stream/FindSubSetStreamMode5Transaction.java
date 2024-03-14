package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 5. Permutation with 2 objects. FindSubset transaction stream.
* Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
* Find the set of objects in the relation via the specified predicate. 
* Legal permutations are:<br/>
* [object],*,[object] <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 2. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022
* 
*/
public class FindSubSetStreamMode5Transaction extends FindSetStreamMode5Transaction {
	Object[] xarg;
    public FindSubSetStreamMode5Transaction(String xid, Object darg, char mop, Object rarg, Object ... xarg) { 	
    	super(xid, darg, mop, rarg);
    	dmr_return[1] = 1;
    	dmr_return[3] = 1;
    	this.xarg = xarg;
		if(xarg.length != 2) throw new RuntimeException("Wrong number of end range arguments for 'findSubSetStream', expected 2 got "+xarg.length);
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
			   if( tdmr.getDomain() != null ) {
					  templdmr.setDomainTemplate(xid, (Comparable) xarg[ipos++]); 
			   }
			   if( tdmr.getRange() != null ) {
					  templdmr.setRangeTemplate(xid, (Comparable) xarg[ipos++]); 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return (Stream<?>) new RelatrixSubsetStreamTransaction(xid, tdmr, templdmr, dmr_return);
	   }
}
