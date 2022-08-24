package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Morphism;
/**
 * Find elements strictly less than 'to' target.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class FindHeadSetMode1 extends FindSetMode1 {
	   public FindHeadSetMode1(char dop, char mop, Object rarg) { 	super(dop,mop,rarg); }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		    return new RelatrixHeadsetIterator(tdmr, dmr_return);
	   }
}
