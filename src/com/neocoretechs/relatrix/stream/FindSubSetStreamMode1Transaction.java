package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
/**
 * Mode 1 find for subset transaction stream permutation. The main difference we find here is that we deal with an additional argument
 * to the crucial methods that represents the ending range of the set valued results of our findSet query.
 * To get the subSet iterator from the RockSack we need 2 arguments, start and end range. We use the overridden 
 * clone method to render an instance for our template that we fill in with the arguments from the additional semantics.
 * It takes the form of a variable parameter argument to the findSet method at the highest levels of the Relatrix
 * API. <br/>
 * The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
 * specified in the variable parameters, in this case 1. Since we are returning a range of concrete objects we need to include
 * these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
 * @author Jonathan Groff Copyright (C) 2015,2021 NeoCoreTechs
 *
 */
public class FindSubSetStreamMode1Transaction extends FindSetStreamMode1Transaction {
	Object[] xarg;
	// By our model, xarg should only be of length 1
	public FindSubSetStreamMode1Transaction(String xid, char dop, char mop, Object rarg, Object ... xarg) { 
		super(xid,dop,mop,rarg);
		dmr_return[3] = 1;
		this.xarg = xarg;
		if(xarg.length != 1) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSetStream', expected 1, got "+xarg.length);
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
		return (Stream<?>) new RelatrixSubsetStreamTransaction(xid, tdmr, templdmr, dmr_return);
	}
}
