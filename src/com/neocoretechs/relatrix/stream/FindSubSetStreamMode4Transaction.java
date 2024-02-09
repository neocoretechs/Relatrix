package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 4. FindSubset stream transaction.
* Find the set of objects in the relation via the specified predicate.Legal permutations are:<br/>
* [object],*,* <br/>
* [object],*,?  <br/>
* [object],?,?  <br/>
* [object],?,* <br/>
* [TemplateClass],*,* <br/>
* [TemplateClass],*,? <br/>
* [TemplateClass],?,? <br/>
* [TemplateClass],?,* <br/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 1. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
*/
public class FindSubSetStreamMode4Transaction extends FindSetStreamMode4Transaction {
	Object[] xarg;
    public FindSubSetStreamMode4Transaction(String xid, Object darg, char mop, char rop, Object ... xarg) { 	
    	super(xid, darg, mop, rop);
    	dmr_return[1] = 1; // set to return range of target objects
		this.xarg = xarg;
		if(xarg.length != 1) throw new RuntimeException("Wrong number of end range arguments for 'findSubSetStream', expected 1 got "+xarg.length);
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
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return (Stream<?>) new RelatrixSubsetStreamTransaction(xid, tdmr, templdmr, dmr_return);
	   }
}
