package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.bigsack.BigSackAdapter;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MapDomainRange;


/**
* Mode 2 find returns a set in map, domain, range order. The map value is matched against the constructor
* value. Mode 2 findSet("?|*",object,"?|*") returning a Comparable of 1 or 2 elements containing the identity
* or tuples from retrieval. For identity, if we specify findSet("*",object,"*") we get a Comparable of 1
* element containing a Morphism subclass.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* *,[object],? ?,[object],? ?,[object],*
* *,[TemplateClass],* *,[TemplateClass],? ?,[TemplateClass],? ?,[TemplateClass],*
* @author jg Groff Copyright (C) NeoCoreTechs 2014,2105
* 
*/
public class FindSetMode2 extends IteratorFactory {
	// mode 2
	char dop,rop;
	Object marg;
	short[] dmr_return = new short[4];
    public FindSetMode2(char dop, Object marg, char rop) { 	
    	this.dop = dop;
    	this.rop = rop;
    	this.marg = marg;
	    // see if its ? or * operator
    	dmr_return[1] = checkOp(dop);
    	// 'map' object
    	dmr_return[2] = 0;
    	// range, see if its ? or * operator
    	dmr_return[3] = checkOp(rop);
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		Morphism dmr = new MapDomainRange(null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixIterator(dmr);
	}
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixIterator(BigSackAdapter.getBigSackSetTransaction(tdmr), tdmr, dmr_return);	
	}
}
