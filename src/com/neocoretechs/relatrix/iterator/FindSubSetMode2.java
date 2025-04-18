package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relatrix;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],* <br/>
* *,[object],? <br/>
* ?,[object],? <br/>
* ?,[object],* <br/>
*  <p/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 1. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSubSetMode2 extends FindSetMode2 {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode2(char dop, Object marg, char rop, Object ... endarg ) { 	
    	super(dop, marg, rop);
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
		if(tdmr.getRange() == null) {
			if(endarg[argCtr] instanceof Class) {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange((Comparable) Relatrix.firstKey((Class)endarg[argCtr]));
				ydmr.setRange((Comparable) Relatrix.lastKey((Class)endarg[argCtr]));
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange((Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setRange((Comparable)endarg[argCtr]);
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
		if(tdmr.getRange() == null) {
			if(endarg[argCtr] instanceof Class) {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange(alias,(Comparable) Relatrix.firstKey(alias,(Class)endarg[argCtr]));
				ydmr.setRange(alias,(Comparable) Relatrix.lastKey(alias,(Class)endarg[argCtr]));
			} else {
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				xdmr.setRange(alias,(Comparable)endarg[argCtr++]);
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setRange(alias,(Comparable)endarg[argCtr]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
		return new RelatrixSubsetIterator(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
