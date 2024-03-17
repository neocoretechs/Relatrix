package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
/**
 * Mode 1.
 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
 * Mode 1 find for subset permutation. The main difference we find here is that we deal with an additional argument
 * to the crucial methods that represents the ending range of the set valued results of our findSet query.
 * To get the subSet iterator from the RockSack we need 2 arguments, start and end range. We use the overridden 
 * clone method to render an instance for our template that we fill in with the arguments from the additional semantics.
 * It takes the form of a variable parameter argument to the findSet method at the highest levels of the Relatrix
 * API. <br/>
 * The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
 * specified in the variable parameters, in this case 1. Since we are returning a range of concrete objects we need to include
 * these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
 * @author Jonathan Groff Copyright (C) 2015,2021 NeoCoreTechs
 *
 */
public class FindSubSetStreamMode1 extends FindSetStreamMode1 {
	   Object[] endarg;
	   int argCtr = 0;
	   // By our model, xarg should only be of length 1
	   public FindSubSetStreamMode1(char dop, char mop, Object rarg, Object ... endarg) { 
		   super(dop,mop,rarg);
		   this.endarg = endarg;
		   if(endarg.length < 2) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
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
			if(tdmr.getMap() == null) {
				if(endarg[argCtr] instanceof Class) {
					tdmr.setMap((Comparable) RelatrixKV.firstKey((Class)endarg[argCtr]));
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					zdmr.setMap((Comparable) RelatrixKV.lastKey((Class)endarg[argCtr++]));
					xdmr.setMapKey(DBKey.nullDBKey); // full range
					ydmr.setMapKey(DBKey.fullDBKey);
				} else {
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					tdmr.setMap((Comparable)endarg[argCtr++]);
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					zdmr.setMap((Comparable)endarg[argCtr++]);
					xdmr.setMapKey(DBKey.nullDBKey); // full range
					ydmr.setMapKey(DBKey.fullDBKey);
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
			if(tdmr.getMap() == null) {
				if(endarg[argCtr] instanceof Class) {
					tdmr.setMap((Comparable) RelatrixKV.firstKey(alias,(Class)endarg[argCtr]));
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					zdmr.setMap((Comparable) RelatrixKV.lastKey(alias,(Class)endarg[argCtr++]));
					xdmr.setMapKey(DBKey.nullDBKey); // full range
					ydmr.setMapKey(DBKey.fullDBKey);
				} else {
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					tdmr.setMap((Comparable)endarg[argCtr++]);
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					zdmr.setMap((Comparable)endarg[argCtr++]);
					xdmr.setMapKey(DBKey.nullDBKey); // full range
					ydmr.setMapKey(DBKey.fullDBKey);
				}
			} else
				throw new IllegalAccessException("Improper Morphism template.");
			return new RelatrixSubsetStream(alias, tdmr, zdmr, xdmr, ydmr, dmr_return);
		}
}
