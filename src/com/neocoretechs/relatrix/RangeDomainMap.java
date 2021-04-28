package com.neocoretechs.relatrix;

/**
* This class represents the morphisms stored in range (codomain),domain,map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author jg (C) NeoCoreTechs 2014,2015
*/ 
public final class RangeDomainMap extends Morphism {
	private static final long serialVersionUID = -1689898604140078900L;
    public RangeDomainMap() {}
    
    public RangeDomainMap(Comparable d, Comparable m, Comparable r) {
    	domain = d;
        map = m;
        range = r;
    }
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		RangeDomainMap dmr = (RangeDomainMap)dmrpk;
		int cmp = 0;
		if( dmr.range == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(range, dmr.range);
		if( cmp != 0 ) return cmp;
		if( dmr.domain == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		cmp = Morphism.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.map == null )
			return 1;
		//return map.compareTo(dmr.map);
		return Morphism.fullCompareTo(map, dmr.map);
	}

	@Override
	public boolean equals(Object dmrpk) {
		RangeDomainMap dmr = (RangeDomainMap)dmrpk;
		boolean cmp = false;
		if( dmr.range == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(range, dmr.range);
		if( !cmp ) return cmp;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.map == null )
			return false;
		//return map.equals(dmr.map);
		return Morphism.fullEquals(map, dmr.map);
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
    			return range;
    		case 2:
    			return domain;
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
    	return new RangeDomainMap(domain, map, range);
    }
}
