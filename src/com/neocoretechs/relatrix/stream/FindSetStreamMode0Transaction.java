package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Result3;

/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* or ?,?,? or return identity relationships or 
* domain,map,range 3 element array for each iteration. This mode return a one to three element Result
* depending on the configuration of the findSet. The number of "?" elements PLUS the number of concrete
* target range objects specified determines the size of the returned Comparable array.<p/>
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* Morphism object and the ("?","?","?") returns 3 elements of each of the independent objects.<p/>
* Examples:<br/>
* ?,*,* domain,map,range order return domain in  {@link Result1} <br/>
* *,?,* map,domain,range order return map in  {@link Result1} <br/>
* *,*,? range,map,domain order return range in  {@link Result1} <br/>
* ?,?,* domain,map,range order return domain,map in  {@link Result2} <br/>
* *,?,? range,domain,map order return map,range in  {@link Result2}<br/>
* ?,*,? domain,range,map order return domain,range in  {@link Result2} <br/>
* ?,?,? domain,map,range order, return domain,map,range in  {@link Result3} <br/>
* *,*,* domain,map,range order, return identity dmr instance in  {@link Result1} <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetStreamMode0Transaction extends FindSetStreamMode0 {
	private static boolean DEBUG = false;
	// mode 0
	String xid;
    public FindSetStreamMode0Transaction(String xid, char dop, char mop, char rop) {
    	super( dop, mop, rop);
    	this.xid = xid;
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
 	@Override
 	public Stream<?> createStream() throws IllegalAccessException, IOException {
 		Morphism dmr = null;
 		switch(Morphism.form_template_keyop(new Result3(null,null,null), dmr_return)) {
 			case 0: // dmr
 				dmr = new DomainMapRange(true, null, xid, null, null, null);
 				break;
 			case 1: // drm
 				dmr = new DomainRangeMap(true, null, xid, null, null, null);
 				break;
 			case 2: // mdr
 				dmr = new MapDomainRange(true,null, xid, null, null, null);
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
 			System.out.println("Relatrix FindSetStreamMode0.createStream setting search for "+dmr);
 	    return createRelatrixStream(dmr);
 	}
 	
	@Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixStreamTransaction(xid, tdmr, dmr_return);
	}
}
