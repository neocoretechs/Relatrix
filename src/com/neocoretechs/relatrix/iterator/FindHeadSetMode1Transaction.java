package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode1Transaction extends FindSetMode1Transaction {
	   public FindHeadSetMode1Transaction(String xid, char dop, char mop, Object rarg) { 	
		   super(xid,dop,mop,rarg); 
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		    return new RelatrixHeadsetIteratorTransaction(xid, tdmr, dmr_return);
	   }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		    return new RelatrixHeadsetIteratorTransaction(alias, xid, tdmr, dmr_return);
	   }
}
