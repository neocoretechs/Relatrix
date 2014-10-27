package com.neocoretechs.relatrix;

import java.io.IOException;


public class RangeDomainMap extends DMRStruc {
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
		cmp = DMRStruc.fullCompareTo(range, dmr.range);
		if( cmp != 0 ) return cmp;
		if( dmr.domain == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		DMRStruc.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.map == null )
			return 1;
		//return map.compareTo(dmr.map);
		return DMRStruc.fullCompareTo(map, dmr.map);
	}

	@Override
	public boolean equals(Object dmrpk) {
		RangeDomainMap dmr = (RangeDomainMap)dmrpk;
		boolean cmp = false;
		if( dmr.range == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = DMRStruc.fullEquals(range, dmr.range);
		if( !cmp ) return cmp;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = DMRStruc.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.map == null )
			return false;
		//return map.equals(dmr.map);
		return DMRStruc.fullEquals(map, dmr.map);
	}
    public String toString() { return range.toString()+"->"+domain.toString()+"->"+map.toString(); }
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
}
