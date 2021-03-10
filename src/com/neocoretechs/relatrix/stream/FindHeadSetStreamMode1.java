package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;

public class FindHeadSetStreamMode1 extends FindSetStreamMode1 {
	   public FindHeadSetStreamMode1(char dop, char mop, Object rarg) { 	super(dop,mop,rarg); }
	   
	   @Override
	   protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		    return (Stream<?>) new RelatrixHeadsetStream(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);
	   }
}
