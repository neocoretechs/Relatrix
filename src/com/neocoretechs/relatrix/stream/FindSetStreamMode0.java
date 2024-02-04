package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;

/**
* Construct an iterator from findSet or one of its subclasses (headSet, subset, tailSet is the default).
* Permutation for predicate *,*,* or ?,?,? or return identity relationships or 
* domain,map,range 3 element array for each iteration. This mode return a one to three element Comparable[]
* depending on the configuration of the findSet. The number of "?" elements PLUS the number of concrete
* target range objects specified determines the size of the returned Comparable array.<p/>
* This mode represents the equivalent of 'SELECT ALL' for identities or morphisms where identities return 1 array element of the
* Morphism object and the ("?","?","?") returns 3 elements of each of the independent objects.<p/>
* Examples:<br/>
* ?,*,* domain,map,range order return domain in Comparable[0] <br/>
* *,?,* map,domain,range order return map in Comparable[0] <br/>
* *,*,? range,map,domain order return range in Comparable[0] <br/>
* ?,?,* domain,map,range order return domain,map in Comparable[0], Comparable[1] <br/>
* *,?,? range,domain,map order return map,range in Comparable[0], Comparable[1] <br/>
* ?,*,? domain,range,map order return domain,range in Comparable[0],Comparable[1] <br/>
* ?,?,? domain,map,range order, return domain,map,range in Comparable[0],Comparable[1],Comparable[2] <br/>
* *,*,* domain,map,range order, return identity dmr instance in Comparable[0] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
public class FindSetStreamMode0 extends StreamFactory {
	// mode 0
	char dop,mop,rop;
	short[] dmr_return = new short[4];
	private static boolean DEBUG = false;
    public FindSetStreamMode0(char dop, char mop, char rop) { 	
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
    * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
    */
	@Override
	public Stream<?> createStream() throws IllegalAccessException, IOException {
		Morphism dmr = null;
		switch(Morphism.form_template_keyop(new Comparable[]{null,null,null}, dmr_return)) {
			case 0: // dmr
				dmr = new DomainMapRange(null, null, null, true);
				break;
			case 1: // drm
				dmr = new DomainRangeMap(null, null, null, true);
				break;
			case 2: // mdr
				dmr = new MapDomainRange(null, null, null, true);
				break;
			case 3: // mrd
				dmr = new MapRangeDomain(null, null, null, true);
				break;
			case 4: // rdm
				dmr = new RangeDomainMap(null, null, null, true);
				break;
			case 5: // rmd
				dmr = new RangeMapDomain(null, null, null, true);
				break;
		}
		if( DEBUG  )
			System.out.println("Relatrix FindSetStreamMode0.createStream setting search for "+dmr);
	    return createRelatrixStream(dmr);
	}
	
	/**
	 * @param alias the database alias
	 * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
	 * @throws NoSuchElementException if alias doesnt exist
	 */
	@Override
	public Stream<?> createStream(String alias) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism dmr = null;
		switch(Morphism.form_template_keyop(new Comparable[]{null,null,null}, dmr_return)) {
		case 0: // dmr
			dmr = new DomainMapRange(alias, null, null, null, true);
			break;
		case 1: // drm
			dmr = new DomainRangeMap(alias, null, null, null, true);
			break;
		case 2: // mdr
			dmr = new MapDomainRange(alias, null, null, null, true);
			break;
		case 3: // mrd
			dmr = new MapRangeDomain(alias, null, null, null, true);
			break;
		case 4: // rdm
			dmr = new RangeDomainMap(alias, null, null, null, true);
			break;
		case 5: // rmd
			dmr = new RangeMapDomain(alias, null, null, null, true);
			break;
		}
		if( DEBUG  )
			System.out.println("Relatrix FindSetStreamMode0.createStream setting search for "+dmr);
		return createRelatrixStream(alias, dmr);
	}

	@Override
	/**
	 * @param tdmr Morphism the template
	 */
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixStream(tdmr, dmr_return);
	}

	@Override
	/**
	 * @param alias database alias
	 * @param tdmr The template Morphism
	 */
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		return new RelatrixStream(alias, tdmr, dmr_return);
	}
}
