package com.neocoretechs.relatrix;

/**
* This class represents the morphisms stored in map,domain,range (codomain) order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author jg (C) NeoCoreTechs 2014,2015
*/ 
public final class MapDomainRange extends Morphism {
	private static final long serialVersionUID = -3223516008906545636L;
    public MapDomainRange() {}
    
    public MapDomainRange(Comparable d, Comparable m, Comparable r) {
    	domain = d;
        map = m;
        range = r;
    }
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		MapDomainRange dmr = (MapDomainRange)dmrpk;
		int cmp = 0;
		if( dmr.map == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(map, dmr.map);
		if( cmp != 0 ) return cmp;
		if( dmr.domain == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		cmp = Morphism.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.range == null )
			return 1;
		//return range.compareTo(dmr.range);
		return Morphism.fullCompareTo(range, dmr.range);

	}

	@Override
	public boolean equals(Object dmrpk) {
		MapDomainRange dmr = (MapDomainRange)dmrpk;
		boolean cmp = false;
		if( dmr.map == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(map, dmr.map);
		if( !cmp ) return cmp;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.range == null )
			return false;
		//return range.equals(dmr.range);
		return Morphism.fullEquals(range, dmr.range);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (domain == null ? 0 : domain.hashCode());
		result = 37*result + (map == null ? 0 : map.hashCode());
		result = 37*result + (range == null ? 0 : range.hashCode());
		return result;
	}

    /*
    public Comparable returnTupleOrder(int n) {
    	// default dmr
    	switch(n) {
    		case 1:
    			return map;
    		case 2:
    			return domain;
    		case 3:
    			return range;
    		default:
    			break;
    	}
    	throw new RuntimeException("returnTupleOrder invalid tuple "+n);
    }
    */
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return new MapDomainRange(domain, map, range);
    }
}
