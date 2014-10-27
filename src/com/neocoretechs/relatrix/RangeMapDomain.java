package com.neocoretechs.relatrix;

import java.io.IOException;


public class RangeMapDomain extends DMRStruc {
	private static final long serialVersionUID = -2797189836505364776L;
    public RangeMapDomain() {}
    
    public RangeMapDomain(Comparable d, Comparable m, Comparable r) {
    	domain = d;
        map = m;
        range = r;
    }
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		RangeMapDomain dmr = (RangeMapDomain)dmrpk;
		int cmp = 0;
		if( dmr.range == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = DMRStruc.fullCompareTo(range, dmr.range);
		if( cmp != 0 ) return cmp;
		if(dmr. map == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = DMRStruc.fullCompareTo(map, dmr.map);
		if( cmp != 0 ) return cmp;
		if( dmr.domain == null )
			return 1;
		//return domain.compareTo(dmr.domain);
		return DMRStruc.fullCompareTo(domain, dmr.domain);

	}

	@Override
	public boolean equals(Object dmrpk) {
		RangeMapDomain dmr = (RangeMapDomain)dmrpk;
		boolean cmp = false;
		if( range == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = DMRStruc.fullEquals(range, dmr.range);
		if( !cmp ) return cmp;
		if( map == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = DMRStruc.fullEquals(map, dmr.map);
		if( !cmp ) return cmp;
		if( domain == null )
			return false;
		//return domain.equals(dmr.domain);
		return DMRStruc.fullEquals(domain, dmr.domain);
	}
	public String toString() { return range.toString()+"->"+map.toString()+"->"+domain.toString(); }
	
    public Comparable returnTupleOrder(int n) {
      	// default dmr
      	switch(n) {
      		case 1:
      			return range;
      		case 2:
      			return map;
      		case 3:
      			return domain;
      		default:
      			break;
      	}
      	throw new RuntimeException("returnTupleOrder invalid tuple "+n);
    }
}
