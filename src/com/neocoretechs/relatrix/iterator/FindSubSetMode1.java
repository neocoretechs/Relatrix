package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relatrix;


/**
 * Mode 1 find for subset permutation. The main difference we find here is that we deal with an additional argument
 * to the crucial methods that represents the ending range of the set valued results of our findSet query.
 * To get the subSet iterator from the RockSack we need 2 arguments, start and end range. We use the overridden 
 * clone method to render an instance for our template that we fill in with the arguments from the additional semantics.
 * It takes the form of a variable parameter argument to the findSet method at the highest levels of the Relatrix
 * API. <p/>
 * * Legal permutations are:
 * *,*,[object],... <br/>
 * *,?,[object],... <br/>
 * ?,?,[object],... <br/>
 * ?,*,[object],... <br/>
 * The number of objects returned is the sum of the number of "?" PLUS the number of concrete object instances
 * specified in the variable parameters, in this case 1. Since we are returning a range of concrete objects we need to include
 * these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
 * @author Jonathan Copyright (C) 2015 NeoCoreTechs
 *
 */
public class FindSubSetMode1 extends FindSetMode1 {
	   Object[] endarg;
		int argCtr = 0;
	   public FindSubSetMode1(char dop, char mop, Object rarg, Object ... endarg) { 
		   super(dop,mop,rarg);
		   this.endarg = endarg;
		   if(endarg.length < 2) throw new RuntimeException( "Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
	   }
	   
		@Override
		protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
			AbstractRelation xdmr = null;
			AbstractRelation ydmr = null;
			try {
				xdmr = (AbstractRelation) tdmr.clone();
				ydmr = (AbstractRelation) tdmr.clone();
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[argCtr] instanceof Class) {
					xdmr.setDomain((Comparable) Relatrix.firstKey((Class)endarg[argCtr]));
					ydmr.setDomain((Comparable) Relatrix.lastKey((Class)endarg[argCtr++]));
				} else {
					xdmr.setDomain((Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
					ydmr.setDomain((Comparable)endarg[argCtr++]);
				}
			} else
				throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
			if(tdmr.getMap() == null) {
				if(endarg[argCtr] instanceof Class) {
					xdmr.setMap((Comparable) Relatrix.firstKey((Class)endarg[argCtr]));
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					ydmr.setMap((Comparable) Relatrix.lastKey((Class)endarg[argCtr++]));
				} else {
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					xdmr.setMap((Comparable)endarg[argCtr++]);
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					ydmr.setMap((Comparable)endarg[argCtr++]);
				}
			} else
				throw new IllegalAccessException("Improper AbstractRelation template.");
			return new RelatrixSubsetIterator(tdmr, xdmr, ydmr, dmr_return);
		}

		@Override
		protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
			AbstractRelation xdmr = null;
			AbstractRelation ydmr = null;
			try {
				xdmr = (AbstractRelation) tdmr.clone();
				ydmr = (AbstractRelation) tdmr.clone();
			} catch (CloneNotSupportedException e) {}
			if(tdmr.getDomain() == null) {
				if(endarg[argCtr] instanceof Class) {
					xdmr.setDomain(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg[argCtr]));
					ydmr.setDomain(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg[argCtr++]));
				} else {
					xdmr.setDomain(alias,(Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
					ydmr.setDomain(alias,(Comparable)endarg[argCtr++]);
				}
			} else
				throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
			if(tdmr.getMap() == null) {
				if(endarg[argCtr] instanceof Class) {
					xdmr.setMap(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg[argCtr]));
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					ydmr.setMap(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg[argCtr++]));
				} else {
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					xdmr.setMap(alias,(Comparable)endarg[argCtr++]);
					if(argCtr >= endarg.length)
						throw new IllegalAccessException("Wrong number of arguments to findSubSet");
					ydmr.setMap(alias,(Comparable)endarg[argCtr++]);
				}
			} else
				throw new IllegalAccessException("Improper AbstractRelation template.");
			return new RelatrixSubsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
		}

}
