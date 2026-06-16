package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixJsonTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Mode 3. The findSet contains two object references, therefore the subset variable array must also.
* Find the subset of objects in the relation via the specified predicate. Legal permutations are:<br>
* *,[object],[object],[class] <br>
* ?,[object],[object],[class] <br>
* *,[object],[object],[object],[object] <br>
* ?,[object],[object],[object],[object] <br>
* The number of Comparable objects returned is the sum of the number of "?" PLUS the number of concrete object instances
* specified in the variable parameters, in this case 2 for a potential total of 3 object returned per iteration.<p>
* Since we are returning a range of concrete objects we need to include
* these items, and if a retrieval of a range of concrete objects is desired, the subset and substream are the means of doing so.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
*/
public class FindSubSetMode3JsonTransaction extends FindSetMode3JsonTransaction {
	Object[] endarg;
	int argCtr = 0;
    public FindSubSetMode3JsonTransaction(TransactionId xid, char dop, Object marg, Object rarg, Object ... endarg) { 	
    	super(xid, dop, marg, rarg);
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
		if(tdmr.getDomain() == null) {
			if(endarg[argCtr] instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixJsonTransaction.firstKey(xid, (Class)endarg[argCtr]));
				ydmr.setDomain((Comparable) RelatrixJsonTransaction.lastKey(xid, (Class)endarg[argCtr++]));
			} else {
				xdmr.setDomain((Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				ydmr.setDomain((Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		return new RelatrixSubsetIteratorJsonTransaction(xid, tdmr, xdmr, ydmr, dmr_return);
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
				xdmr.setDomain(alias,(Comparable) RelatrixJsonTransaction.firstKey(alias, xid, (Class)endarg[argCtr]));
				ydmr.setDomain(alias,(Comparable) RelatrixJsonTransaction.lastKey(alias, xid, (Class)endarg[argCtr++]));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg[argCtr++]); // same as concrete type in d,m,r field, but we are returning relations with that value
				ydmr.setDomain(alias,(Comparable)endarg[argCtr++]);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template."); // all wildcard or return tuple, should all be null
		return new RelatrixSubsetIteratorJsonTransaction(alias, xid, tdmr, xdmr, ydmr, dmr_return);
	}
}
