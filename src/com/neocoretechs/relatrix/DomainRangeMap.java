package com.neocoretechs.relatrix;


public class DomainRangeMap extends DMRStruc {
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
		cmp = DMRStruc.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.range == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = DMRStruc.fullCompareTo(range, dmr.range);
		if( cmp != 0 ) return cmp;
		if( dmr.map == null )
			return 1;
		//return map.compareTo(dmr.map);
		return DMRStruc.fullCompareTo(map, dmr.map);
	}

	@Override
	public boolean equals(Object dmrpk) {
		DomainRangeMap dmr = (DomainRangeMap)dmrpk;
		boolean cmp = false;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = DMRStruc.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.range == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = DMRStruc.fullEquals(range, dmr.range);
		if( !cmp ) return cmp;
		if( dmr.map == null )
			return false;
		//return map.equals(dmr.map);
		return DMRStruc.fullEquals(map, dmr.map);
	}
 
    
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
    

}
