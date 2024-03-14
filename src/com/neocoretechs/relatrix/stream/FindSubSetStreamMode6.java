package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Find the set of objects in the relation via the specified predicate. 
* Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
* Mode 6 = findSet(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],* <br/>
* [object],[object],? <br/>
* [TemplateClass],[TemplateClass],* <br/>
* [TemplateClass],[TemplateClass],? <br/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 2. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindSubSetStreamMode6 extends FindSetStreamMode6 {
	Object[] xarg;
    public FindSubSetStreamMode6(Object darg, Object marg, char rop, Object ...xarg) { 	
    	super(darg,marg, rop);
    	dmr_return[1] = 1;
    	dmr_return[2] = 1;
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
			   if( tdmr.getDomain() != null ) {
					  templdmr.setDomainTemplate((Comparable) xarg[ipos++]); 
			   }
			   if( tdmr.getMap() != null ) {
					  templdmr.setMapTemplate((Comparable) xarg[ipos++]); 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return (Stream<?>) new RelatrixSubsetStream(tdmr, templdmr, dmr_return);
	   }
	   /**
	     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
	     */
		  @Override
		  protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
			   // make a new Morphism template
			   Morphism templdmr;
			   try {
				   // primarily for class type than values of instance
				   templdmr = (Morphism) tdmr.clone();
				   // move the end range into the new template in the proper position
				   int ipos = 0;
				   if( tdmr.getDomain() != null ) {
						  templdmr.setDomainTemplate(alias, (Comparable) xarg[ipos++]); 
				   }
				   if( tdmr.getMap() != null ) {
						  templdmr.setMapTemplate(alias, (Comparable) xarg[ipos++]); 
				   }
			   } catch (CloneNotSupportedException e) {
				   throw new IOException(e);
			   }
			   return (Stream<?>) new RelatrixSubsetStream(alias, tdmr, templdmr, dmr_return);
		   }
}
