package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;
/**
 * Mode 1 find for subset permutation. The main difference we find here is that we deal with an additional argument
 * to the crucial methods that represents the ending range of the set valued results of our findSet query.
 * To get the subSet iterator from the BigSack we need 2 arguments, start and end range. We use the overridden 
 * clone method to render an instance for our template that we fill in with the arguments from the additional semantics.
 * It takes the form of a variable parameter argument to the findSet method at the highest levels of the Relatrix
 * API. 
 * @author Jonathan Groff Copyright (C) 2015,2021 NeoCoreTechs
 *
 */
public class FindSubSetStreamMode1 extends FindSetStreamMode1 {
	   Object[] xarg;
	   // By our model, xarg should only be of length 1
	   public FindSubSetStreamMode1(char dop, char mop, Object rarg, Object ... xarg) { 
		   super(dop,mop,rarg);
		   this.xarg = xarg;
		   if(xarg.length != 1) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', expected 1, got "+xarg.length);
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
					  templdmr.setDomain((Comparable) xarg[ipos++]); 
			   }
			   if( tdmr.getMap() != null ) {
					  templdmr.setMap((Comparable) xarg[ipos++]); 
			   }
			   if( tdmr.getRange() != null ) {
					  templdmr.setRange((Comparable) xarg[ipos++]); 
			   }
		   } catch (CloneNotSupportedException e) {
			   throw new IOException(e);
		   }
		   return (Stream<?>) new RelatrixSubsetStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, templdmr, dmr_return);
	   }
}
