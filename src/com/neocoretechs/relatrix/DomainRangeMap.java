package com.neocoretechs.relatrix;
/**
* This class represents the morphisms stored in domain,range (codomain),map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author jg (C) NeoCoreTechs 2014,2015
*/ 
public final class DomainRangeMap extends Morphism {
	private static final long serialVersionUID = -1694888225034392347L;
    public DomainRangeMap() {}
    
    public DomainRangeMap(Comparable d, Comparable m, Comparable r) {
    	domain = d;
        map = m;
        range = r;
    }
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		DomainRangeMap dmr = (DomainRangeMap)dmrpk;
		int cmp = 0;
		if( dmr.domain == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		cmp = Morphism.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.range == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(range, dmr.range);
		if( cmp != 0 ) return cmp;
		if( dmr.map == null )
			return 1;
		//return map.compareTo(dmr.map);
		return Morphism.fullCompareTo(map, dmr.map);
	}

	@Override
	public boolean equals(Object dmrpk) {
		DomainRangeMap dmr = (DomainRangeMap)dmrpk;
		boolean cmp = false;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.range == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(range, dmr.range);
		if( !cmp ) return cmp;
		if( dmr.map == null )
			return false;
		//return map.equals(dmr.map);
		return Morphism.fullEquals(map, dmr.map);
	}
 
    /*
    public Comparable returnTupleOrder(int n) {
    	// default dmr
    	switch(n) {
    		case 1:
    			return domain;
    		case 2:
    			return range;
    		case 3:
    			return map;
    		default:
    			break;
    	}
    	throw new RuntimeException("returnTupleOrder invalid tuple "+n);
    }
    */
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return new DomainRangeMap(domain, map, range);
    }

}
