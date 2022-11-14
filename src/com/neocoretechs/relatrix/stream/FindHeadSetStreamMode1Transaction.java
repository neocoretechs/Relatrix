package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
/**
* Headset stream mode 1. From beginning of order to value of instance argument in a transaction context.
* Find the set of objects in the relation via the specified predicate. Mode 1 = findset("*|?","*|?",object)
* returning identity, 1 or 2 element Comparable array of tuples for each iteration of the retrieval.
* Legal permutations are:
* *,*,[object] 
* *,?,[object] 
* ?,?,[object] 
* ?,*,[object]
* *,*,[TemplateClass] 
* *,?,[TemplateClass] 
* ?,?,[TemplateClass] 
* ?,*,[TemplateClass]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2022 
 *
 */
public class FindHeadSetStreamMode1Transaction extends FindHeadSetStreamMode1 {
	String xid;
	public FindHeadSetStreamMode1Transaction(String xid, char dop, char mop, Object rarg) { 
		super(dop,mop,rarg); 
		this.xid = xid;
	}
	   
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		 return new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
