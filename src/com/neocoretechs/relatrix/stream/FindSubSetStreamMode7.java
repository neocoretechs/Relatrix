package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
* In this permutation we are dealing with 3 objects represent identity so we have the option
* of setting a retrieval bringing in the identities in their natural d,m,r order.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* This winds up serving 2 functions. One is identity morphism retrieval if we get 3 objects that are not
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindSubSetStreamMode7 extends FindSetStreamMode7 {
	Object[] xarg;
	// mode 7
    public FindSubSetStreamMode7(Object darg, Object marg, Object rarg, Object ... xarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
       	this.xarg = xarg;
    	if(xarg.length != 3) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', expected 3 got "+xarg.length);
    }
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
			   if( tdmr.getRange() != null ) {
					  templdmr.setRangeTemplate((Comparable) xarg[ipos++]); 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return (Stream<?>) new RelatrixSubsetStream( tdmr, templdmr, dmr_return);
	 }
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
			if( tdmr.getRange() != null ) {
				templdmr.setRangeTemplate(alias, (Comparable) xarg[ipos++]); 
			}
		} catch (CloneNotSupportedException e) {
			throw new IOException(e);
		}
		return (Stream<?>) new RelatrixSubsetStream(alias, tdmr, templdmr, dmr_return);
	}

}
