package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;

/**
* Mode 3. The findSet contains two object references, therefore the subset variable array must also.
* Find the subset of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],[object],[class] <br/>
* ?,[object],[object],[class] <br/>
* *,[object],[object],[object],[object] <br/>
* ?,[object],[object],[object],[object] <br/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 2 for a potential total of 3 object returned per iteration.<p/>
* Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSubSetMode3 extends FindSetMode3 {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode3(char dop, Object marg, Object rarg, Object ... endarg) { 	
    	super(dop, marg, rarg);
		this.endarg = endarg;
		if(endarg.length < 1) throw new RuntimeException("Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				tdmr.setDomain((Comparable) RelatrixKV.firstKey((Class)endarg[argCtr]));
				zdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[argCtr++]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				zdmr.setDomain((Comparable)endarg[argCtr++]);
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		return new RelatrixSubsetIterator(tdmr, zdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Iterator<?> createRelatrixIterator(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				tdmr.setDomain((Comparable) RelatrixKV.firstKey(alias,(Class)endarg[argCtr]));
				zdmr.setDomain((Comparable) RelatrixKV.lastKey(alias,(Class)endarg[argCtr++]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				zdmr.setDomain((Comparable)endarg[argCtr++]);
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template."); // all wildcard or return tuple, should all be null
		return new RelatrixSubsetIterator(alias, tdmr, zdmr, xdmr, ydmr, dmr_return);
	}
}
