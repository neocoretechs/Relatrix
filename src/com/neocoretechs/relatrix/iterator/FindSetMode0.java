package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Result3;


/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* or ?,?,? or return identity relationships or 
* domain,map,range 3 element array for each iteration. This mode returns a one to three element Result hierarchy
* depending on the configuration of the findSet. The number of "?" elements determines the size of the returned Comparable array.
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* morphism object itself, and the ("?","?","?") returns 3 elements of each of the independent objects that comprise the morphism relationship.
* {@link AbstractRelation}
* <p/>
* Examples:<br/>
* ?,*,* domain,map,range order return domain in {@link com.neocoretechs.relatrix.Result1} <br/>
* *,?,* map,domain,range order return map in Result1 {@link com.neocoretechs.relatrix.Result1}<br/>
* *,*,? range,map,domain order return range in Result1 {@link com.neocoretechs.relatrix.Result1}<br/>
* ?,?,* domain,map,range order return domain,map in Result2 {@link com.neocoretechs.relatrix.Result2}<br/>
* *,?,? range,domain,map order return map,range in Result2 {@link com.neocoretechs.relatrix.Result2}<br/>
* ?,*,? domain,range,map order return domain,range in Result2 {@link com.neocoretechs.relatrix.Result2}<br/>
* ?,?,? domain,map,range order, return domain,map,range in {@link Result3} <br/>
* *,*,* domain,map,range order, return identity dmr instance in {@link com.neocoretechs.relatrix.Result1} <br/>
* We can substitute a concrete object instance for any of the above wild cards to retrieve only those
* relationships that contain that object instance.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetMode0 extends IteratorFactory {
	// mode 0
	char dop,mop,rop;
	short[] dmr_return = new short[4];
	private static boolean DEBUG = false;
    public FindSetMode0(char dop, char mop, char rop) { 	
    	this.dop = dop;
    	this.mop = mop;
    	this.rop = rop;
	    // see if its ? or * operator
	    dmr_return[1] = checkOp(dop);
	    dmr_return[2] = checkOp(mop);
	    dmr_return[3] = checkOp(rop);
	    if( isReturnRelationships(dop, mop, rop) )
	    	dmr_return[0] = -1;
    }
    /**
    * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
    */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
		AbstractRelation dmr = null;
		switch(AbstractRelation.form_template_keyop(new Result3(null,null,null), dmr_return)) {
			case 0: // dmr
				dmr = new Relation(true, null, null, null);
				break;
			case 1: // drm
				dmr = new DomainRangeMap(true, null, null, null);
				break;
			case 2: // mdr
				dmr = new MapDomainRange(true, null, null, null);
				break;
			case 3: // mrd
				dmr = new MapRangeDomain(true, null, null, null);
				break;
			case 4: // rdm
				dmr = new RangeDomainMap(true, null, null, null);
				break;
			case 5: // rmd
				dmr = new RangeMapDomain(true, null, null, null);
				break;
		}
		if( DEBUG  )
			System.out.println("Relatrix FindsetMode0.createIterator setting search for "+dmr);
	    return createRelatrixIterator(dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr) throws IllegalAccessException, IOException {
	    return new RelatrixIterator(tdmr, dmr_return);
	}
	
    /**
    * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
    */
	@Override
	public Iterator<?> createIterator(Alias alias) throws IllegalAccessException, IOException, NoSuchElementException {
		AbstractRelation dmr = null;
		switch(AbstractRelation.form_template_keyop(new Result3(null,null,null), dmr_return)) {
			case 0: // dmr
				dmr = new Relation(true, alias, null, null, null);
				break;
			case 1: // drm
				dmr = new DomainRangeMap(true, alias, null, null, null);
				break;
			case 2: // mdr
				dmr = new MapDomainRange(true, alias, null, null, null);
				break;
			case 3: // mrd
				dmr = new MapRangeDomain(true, alias, null, null, null);
				break;
			case 4: // rdm
				dmr = new RangeDomainMap(true, alias, null, null, null);
				break;
			case 5: // rmd
				dmr = new RangeMapDomain(true, alias, null, null, null);
				break;
		}
		if( DEBUG  )
			System.out.println("Relatrix FindsetMode0.createIterator alias:"+alias+" setting search for "+dmr);
	    return createRelatrixIterator(alias, dmr);
	}
	
	@Override
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIterator(alias, tdmr, dmr_return);
	}
}
