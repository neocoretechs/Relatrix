package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Mode 5. Permutation with 2 objects.
* Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* [object],*,[object],.. <br/>
* [object],?,[object],... <br/>
* The number of objects returned is the sum of the number of "?"
* specified in the variable parameters, in this case 2. Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSubSetMode5Transaction extends FindSetMode5Transaction {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode5Transaction(TransactionId xid, Object darg, char mop, Object rarg, Object ... endarg) { 	
    	super(xid, darg, mop, rarg);
    	this.endarg = endarg;
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
				xdmr.setMap((Comparable) RelatrixTransaction.firstKey(xid,(Class)endarg[argCtr]));
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setMap((Comparable) RelatrixTransaction.lastKey(xid,(Class)endarg[argCtr++]));
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
		return new RelatrixSubsetIteratorTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
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
				xdmr.setMap((Comparable) RelatrixTransaction.firstKey(alias,xid,(Class)endarg[argCtr]));
				if(argCtr >= endarg.length)
					throw new IllegalAccessException("Wrong number of arguments to findSubSet");
				ydmr.setMap((Comparable) RelatrixTransaction.lastKey(alias,xid,(Class)endarg[argCtr++]));
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
		return new RelatrixSubsetIteratorTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
