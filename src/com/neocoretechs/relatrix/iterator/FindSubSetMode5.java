package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relatrix;

/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* [object],*,[object] <br/>
* [object],?,[object] <br/>
* [TemplateClass],*,[TemplateClass] <br/>
* [TemplateClass],?,[TemplateClass] <br/>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 2. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSubSetMode5 extends FindSetMode5 {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode5(Object darg, char mop, Object rarg, Object ... xarg) { 	
    	super(darg, mop, rarg);
    	this.endarg = xarg;
		if(endarg.length < 1) throw new RuntimeException("Wrong number of end range arguments for 'findSubSet', got "+endarg.length);
    }
	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		AbstractRelation ydmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
			ydmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
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
