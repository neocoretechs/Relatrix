package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;

/**
* Mode 7, when all operators are present, equivalent of 'SELECT ALL', table scan etc.
* We have an instance of 3 objects, we return the identities from the beginning to this identity.
* This winds up serving 2 functions. One is identity morphism retrieval if we get 3 objects that are not
* part of TemplateClass retrieval. the second is when one or more params is instanceof TemplateClass. In the second
* instance, the class functions as template for the enclosed class to retrieve objects of that class (or subclass).
* Depending on the subclass of TemplateClass, the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. Category theory analog is a representable. 
* @author jg Groff Copyright (C) NeoCoreTechs 2014,2105 
*/
public class FindHeadSetMode7 extends FindSetMode7 {
	// mode 7
    public FindHeadSetMode7(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	super(darg, marg, rarg);
    }
 
	@Override
	protected Iterator<?> createRelatrixIterator(DMRStruc tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixHeadsetIterator(BigSackAdapter.getBigSackSet(tdmr), tdmr, dmr_return);
	}
}
