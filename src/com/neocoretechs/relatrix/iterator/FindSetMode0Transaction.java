package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainMapRangeTransaction;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.DomainRangeMapTransaction;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapDomainRangeTransaction;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.MapRangeDomainTransaction;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.MorphismTransaction;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeDomainMapTransaction;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.RangeMapDomainTransaction;


/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* or ?,?,? or return identity relationships or 
* domain,map,range 3 element array for each iteration. This mode returns a one to three element Comparable[]
* depending on the configuration of the findSet. The number of "?" elements determines the size of the returned Comparable array.
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* morphism object itself, and the ("?","?","?") returns 3 elements of each of the independent objects that comprise the morphism relationship.
* <p/>
* Examples:<br/>
* ?,*,* domain,map,range order return domain in Comparable[0] <br/>
* *,?,* map,domain,range order return map in Comparable[0] <br/>
* *,*,? range,map,domain order return range in Comparable[0] <br/>
* ?,?,* domain,map,range order return domain,map in Comparable[0], Comparable[1] <br/>
* *,?,? range,domain,map order return map,range in Comparable[0], Comparable[1] <br/>
* ?,*,? domain,range,map order return domain,range in Comparable[0],Comparable[1] <br/>
* ?,?,? domain,map,range order, return domain,map,range in Comparable[0],Comparable[1],Comparable[2] <br/>
* *,*,* domain,map,range order, return identity dmr instance in Comparable[0] <br/>
* We can substitute a concrete object instance for any of the above wild cards to retrieve only those
* relationships that contain that object instance.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetMode0Transaction extends FindSetMode0 {
	// mode 0
	private static boolean DEBUG = false;
	String xid;
    public FindSetMode0Transaction(String xid, char dop, char mop, char rop) { 
    	super(dop, mop, rop);
    	this.xid = xid;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
 		MorphismTransaction dmr = null;
 		switch(Morphism.form_template_keyop(new Comparable[]{null,null,null}, dmr_return)) {
 			case 0: // dmr
 				dmr = new DomainMapRangeTransaction(xid, null, null, null, true);
 				break;
 			case 1: // drm
 				dmr = new DomainRangeMapTransaction(xid, null, null, null, true);
 				break;
 			case 2: // mdr
 				dmr = new MapDomainRangeTransaction(xid, null, null, null, true);
 				break;
 			case 3: // mrd
 				dmr = new MapRangeDomainTransaction(xid, null, null, null, true);
 				break;
 			case 4: // rdm
 				dmr = new RangeDomainMapTransaction(xid, null, null, null, true);
 				break;
 			case 5: // rmd
 				dmr = new RangeMapDomainTransaction(xid, null, null, null, true);
 				break;
 		}
 		if( DEBUG  )
 			System.out.println("Relatrix FindsetMode0.createIterator setting search for "+dmr);
 	    return createRelatrixIterator(dmr);
 	}	
	@Override
	protected Iterator<?> createRelatrixIterator(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorTransaction(xid, tdmr, dmr_return);
	}
}