package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixJson;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;

/**
* Mode 3. The findSet contains two object references, therefore the subset variable array must also.
* Find the subset of objects in the relation via the specified predicate. Legal permutations are:<br>
* *,[object],[object],[class] <br>
* *,[object],[object],[object],[object] <br>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
*/
public class FindSubSetMode3Json extends FindSetMode3Json {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode3Json(char dop, Object marg, Object rarg, Object ... endarg) { 	
    	super(dop, marg, rarg);
		this.endarg = endarg;
		if(endarg.length < 1) throw new RuntimeException("Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		AbstractRelation ydmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
			ydmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixJson.firstKey((Class)endarg[argCtr]));
				ydmr.setDomain((Comparable) RelatrixJson.lastKey((Class)endarg[argCtr++]));
			} else {
				xdmr.setDomain((Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				ydmr.setDomain((Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		return new RelatrixSubsetIteratorJson(tdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		AbstractRelation ydmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
			ydmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixJson.firstKey(alias,(Class)endarg[argCtr]));
				ydmr.setDomain(alias,(Comparable) RelatrixJson.lastKey(alias,(Class)endarg[argCtr++]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				ydmr.setDomain(alias,(Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		return new RelatrixSubsetIteratorJson(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
