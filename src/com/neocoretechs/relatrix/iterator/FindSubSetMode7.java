package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* In this permutation we are dealing with 3 objects represent identity so we have the option
* of setting a retrieval bringing in the identities in their natural d,m,r order.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* This winds up serving 2 functions. One is identity morphism retrieval if we get 3 objects that are not
* the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class.
* @author jg Groff Copyright (C) NeoCoreTechs 2014,2105 
*/
public class FindSubSetMode7 extends FindSetMode7 {
	Object[] xarg;
	// mode 7
    public FindSubSetMode7(Object darg, Object marg, Object rarg, Object ... xarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
       	this.xarg = xarg;
    	if(xarg.length != 3) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', expected 3 got "+xarg.length);
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
		   return new RelatrixSubsetIterator(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, templdmr, dmr_return);
	 }

}
