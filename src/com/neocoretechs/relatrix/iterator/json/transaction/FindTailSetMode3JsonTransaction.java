package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixJsonTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Find elements greater or equal to 'from' element.
* Legal permutations are:<br>
* *,[object],[object],[class] <br>
* ?,[object],[object],[object] <br>
* Concrete instances in map and range
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2026
* 
*/
public class FindTailSetMode3JsonTransaction extends FindSetMode3JsonTransaction {
	Object endarg0;
    public FindTailSetMode3JsonTransaction(TransactionId xid, char dop, Object marg, Object rarg, Object arg1) { 	
    	super(xid, dop, marg, rarg);
     	endarg0 = arg1;
    }
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain((Comparable) RelatrixJsonTransaction.firstKey(xid, (Class)endarg0));
			} else {
				xdmr.setDomain((Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
	    return new RelatrixTailsetIteratorJsonTransaction(xid, tdmr, xdmr, dmr_return);
	}
    
    @Override
 	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation xdmr = null;
		try {
			xdmr = (AbstractRelation) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg0 instanceof Class) {
				xdmr.setDomain(alias,(Comparable) RelatrixJsonTransaction.firstKey(alias, xid, (Class)endarg0));
			} else {
				xdmr.setDomain(alias,(Comparable)endarg0);
			}
		} else
			throw new IllegalAccessException("Improper AbstractRelation template.");
	    return new RelatrixTailsetIteratorJsonTransaction(alias, xid, tdmr, xdmr, dmr_return);
 	}
}
