package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;

/**
* Mode 2 find returns a set in map, domain, range order. The map value is matched against the constructor
* value. Mode 2 findSet("*",object,"*") returning  the identity
* or tuples from retrieval. For identity, if we specify findSet("*",object,"*") we get a Comparable of 1
* element containing a AbstractRelation subclass.
* Find the set of objects in the relation via the specified predicate. Legal permutations are
* *,[object],* 
* *,[TemplateClass],* *,[TemplateClass],* 
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetMode2 extends IteratorFactory {
	// mode 2
	char dop,rop;
	protected Object marg;
	protected short[] dmr_return = new short[4];
    public FindSetMode2(char dop, Object marg, char rop) { 	
    	this.dop = dop;
    	this.rop = rop;
    	this.marg = marg;
	    // see if its * operator
    	dmr_return[1] = checkOp(dop);
    	// 'map' object
    	dmr_return[2] = 0;
    	// range, see if * operator
    	dmr_return[3] = checkOp(rop);
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
		AbstractRelation dmr = new MapDomainRange(true, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixIterator(dmr, ctx);
	}
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
		return new RelatrixIterator( tdmr, dmr_return, ctx);	
	}
	
	/**
     * @return Iterator for the set
     */
	@Override
	public Iterator<?> createIterator(Alias alias, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
		AbstractRelation dmr = new MapDomainRange(true, alias, null, (Comparable)marg, null);
		//System.out.println("DMR "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
		return createRelatrixIterator(alias, dmr, ctx);
	}
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
		return new RelatrixIterator(alias, tdmr, dmr_return, ctx);	
	}
}
