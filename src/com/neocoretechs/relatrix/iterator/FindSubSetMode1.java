package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;
/**
 * Mode 1 find for subset permutation. The main difference we find here is that we deal with an additional argument
 * to the crucial methods that represents the ending range of the set valued results of our findSet query.
 * To get the subSet iterator from the BigSack we need 2 arguments, start and end range. We use the overridden 
 * clone method to render an instance for our template that we fill in with the arguments from the additional semantics.
 * It takes the form of a variable parrameter argument to the findSet method at the highest levels of the Relatrix
 * API. 
 * @author jg Copyright (C) 2015 NeoCoreTechs
 *
 */
public class FindSubSetMode1 extends FindSetMode1 {
	   Object[] xarg;
	   // By our model, xarg should only be of length 1
	   public FindSubSetMode1(char dop, char mop, Object rarg, Object ... xarg) { 
		   super(dop,mop,rarg);
		   this.xarg = xarg;
		   assert(xarg.length == 1) : "Wrong variable argument length to FindSubsetMode1, expected 1 got "+xarg.length;
	   }
	   
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
			   if( tdmr.map != null ) {
					  templdmr.map = (Comparable) xarg[ipos++]; 
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
