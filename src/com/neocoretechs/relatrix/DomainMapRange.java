package com.neocoretechs.relatrix;

public class DomainMapRange extends DMRStruc {
	private static final long serialVersionUID = 8664384659501163179L;
    public DomainMapRange() {}
    
    public DomainMapRange(Comparable d, Comparable m, Comparable r) {
    	domain = d;
        map = m;
        range = r;
    }
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		if( dmr.domain == null )
			return 1;
		//int cmp = domain.compareTo(dmr.domain);
		int cmp = DMRStruc.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.map == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = DMRStruc.fullCompareTo(map, dmr.map);
		if( cmp != 0 ) return cmp;
		if( dmr.range == null )
			return 1;
		//return range.compareTo(dmr.range);
		return DMRStruc.fullCompareTo(range, dmr.range);
	}

	@Override
	public boolean equals(Object dmrpk) {
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		boolean cmp = false;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = DMRStruc.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.map == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = DMRStruc.fullEquals(map, dmr.map);
		if( !cmp ) return cmp;
		if( dmr.range == null )
			return false;
		//return range.equals(dmr.range);
		return DMRStruc.fullEquals(range, dmr.range);
	}

}
