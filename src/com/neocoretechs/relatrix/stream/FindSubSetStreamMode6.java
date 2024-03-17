package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;

/**
* Find the set of objects in the relation via the specified predicate. 
* Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
* Mode 6 = findSet(object,object,"*|?") return identity or 1 element tuple.
* Legal permutations are:<br/>
* [object],[object],*,... <br/>
* [object],[object],?,... <br/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 2. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindSubSetStreamMode6 extends FindSetStreamMode6 {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetStreamMode6(Object darg, Object marg, char rop, Object ...endarg) { 	
    	super(darg,marg, rop);
       	this.endarg = endarg;
    	if(endarg.length < 1) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }

	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[argCtr] instanceof Class) {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				tdmr.setRange((Comparable) RelatrixKV.firstKey((Class)endarg[argCtr]));
				zdmr.setRange((Comparable) RelatrixKV.lastKey((Class)endarg[argCtr]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				tdmr.setRange((Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				zdmr.setRange((Comparable)endarg[argCtr]);
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixSubsetStream(tdmr, zdmr, xdmr, ydmr, dmr_return);
	}

	@Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		Morphism zdmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
			zdmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
		if(tdmr.getRange() == null) {
			if(endarg[argCtr] instanceof Class) {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				tdmr.setRange((Comparable) RelatrixKV.firstKey(alias,(Class)endarg[argCtr]));
				zdmr.setRange((Comparable) RelatrixKV.lastKey(alias,(Class)endarg[argCtr]));
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				tdmr.setRange((Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				zdmr.setRange((Comparable)endarg[argCtr]);
				xdmr.setRangeKey(DBKey.nullDBKey); // full range
				ydmr.setRangeKey(DBKey.fullDBKey);
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
		return new RelatrixSubsetStream(alias, tdmr, zdmr, xdmr, ydmr, dmr_return);
	}
}
