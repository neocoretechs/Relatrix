package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.rocksack.TransactionId;


/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* or ?,?,? or return identity relationships or 
* domain,map,range 3 element array for each iteration. This mode returns a one to three object {@link com.neocoretechs.relatrix.Result}
* depending on the configuration of the findSet. The number of "?" elements determines the size of the returned {@link com.neocoretechs.relatrix.Result}.
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* morphism object itself, and the ("?","?","?") returns 3 elements of each of the independent objects that comprise the morphism relationship.
* <p/>
* Examples:<br/>
* ?,*,* domain,map,range order return domain in {@link com.neocoretechs.relatrix.Result1} <br/>
* *,?,* map,domain,range order return map in {@link com.neocoretechs.relatrix.Result1} <br/>
* *,*,? range,map,domain order return range in {@link com.neocoretechs.relatrix.Result1} <br/>
* ?,?,* domain,map,range order return domain,map in {@link com.neocoretechs.relatrix.Result2} <br/>
* *,?,? range,domain,map order return map,range in {@link com.neocoretechs.relatrix.Result2} <br/>
* ?,*,? domain,range,map order return domain,range in {@link com.neocoretechs.relatrix.Result2} <br/>
* ?,?,? domain,map,range order, return domain,map,range in {@link com.neocoretechs.relatrix.Result3} <br/>
* *,*,* domain,map,range order, return identity dmr instance in {@link com.neocoretechs.relatrix.Result1} <br/>
* We can substitute a concrete object instance for any of the above wild cards to retrieve only those
* relationships that contain that object instance.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetMode0Transaction extends FindSetMode0 {
	// mode 0
	private static boolean DEBUG = false;
	TransactionId xid;
    public FindSetMode0Transaction(TransactionId xid, char dop, char mop, char rop) { 
    	super(dop, mop, rop);
    	this.xid = xid;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
 		AbstractRelation dmr = null;
 		switch(AbstractRelation.form_template_keyop(new Result3(null,null,null), dmr_return)) {
 			case 0: // dmr
 				dmr = new Relation(true, null, xid, null, null, null);
 				break;
 			case 1: // drm
 				dmr = new DomainRangeMap(true, null, xid, null, null, null);
 				break;
 			case 2: // mdr
 				dmr = new MapDomainRange(true, null, xid, null, null, null);
 				break;
 			case 3: // mrd
 				dmr = new MapRangeDomain(true, null, xid, null, null, null);
 				break;
 			case 4: // rdm
 				dmr = new RangeDomainMap(true, null, xid, null, null, null);
 				break;
 			case 5: // rmd
 				dmr = new RangeMapDomain(true, null, xid, null, null, null);
 				break;
 		}
 		if( DEBUG  )
 			System.out.printf("%s.createIterator xid=%s setting search for %s%n",this.getClass().getName(),xid,dmr);
 	    return createRelatrixIterator(dmr);
 	}
 	
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
 		AbstractRelation dmr = null;
 		switch(AbstractRelation.form_template_keyop(new Result3(null,null,null), dmr_return)) {
 			case 0: // dmr
 				dmr = new Relation(true, alias, xid, null, null, null);
 				break;
 			case 1: // drm
 				dmr = new DomainRangeMap(true, alias, xid, null, null, null);
 				break;
 			case 2: // mdr
 				dmr = new MapDomainRange(true, alias, xid, null, null, null);
 				break;
 			case 3: // mrd
 				dmr = new MapRangeDomain(true, alias, xid, null, null, null);
 				break;
 			case 4: // rdm
 				dmr = new RangeDomainMap(true, alias, xid, null, null, null);
 				break;
 			case 5: // rmd
 				dmr = new RangeMapDomain(true, alias, xid, null, null, null);
 				break;
 		}
 		if( DEBUG  )
 			System.out.printf("%s.createIterator xid=%s alias=%s setting search for %s%n",this.getClass().getName(),xid,alias,dmr);
 	    return createRelatrixIterator(alias, dmr);
 	}
 	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIteratorTransaction(alias, xid, tdmr, dmr_return);
	}
}
