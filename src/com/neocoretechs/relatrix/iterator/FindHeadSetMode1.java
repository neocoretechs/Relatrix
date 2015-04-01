package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;

public class FindHeadSetMode1 extends FindSetMode1 {
	   public FindHeadSetMode1(char dop, char mop, Object rarg) { 	super(dop,mop,rarg); }
	   
	   @Override
	   protected Iterator<?> createRelatrixIterator(DMRStruc tdmr) throws IllegalAccessException, IOException {
		    return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, dmr_return);
	   }
}
