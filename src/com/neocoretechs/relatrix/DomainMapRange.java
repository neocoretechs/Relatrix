package com.neocoretechs.relatrix;
/**
 * This class represents the morphisms stored in their natural retrieval order.
 * The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
 * By storing these indexes with all their possible retrieval combinations for the morphisms,
 * which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
 * based on any number of possible operators and objects passed to the various 'findSet' permutations. 
 * @author jg
 *
 */
public final class DomainMapRange extends Morphism {
	private static final long serialVersionUID = 8664384659501163179L;
	private static boolean uniqueKey = false;
	
    public DomainMapRange() {}
    
    public DomainMapRange(Comparable d, Comparable m, Comparable r) {
    	domain = d;
        map = m;
        range = r;
    }
    
    public void setUniqueKey(boolean unique) { uniqueKey = unique; }
    
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		if( dmr.domain == null )
			return 1;
		//int cmp = domain.compareTo(dmr.domain);
		int cmp = Morphism.fullCompareTo(domain, dmr.domain);
		if( cmp != 0 ) return cmp;
		if( dmr.map == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(map, dmr.map);
		// if uniqueKey is set, compare only the domain and map
		if( uniqueKey || cmp != 0 ) return cmp;
		if( dmr.range == null )
			return 1;
		//return range.compareTo(dmr.range);
		return Morphism.fullCompareTo(range, dmr.range);
	}

	@Override
	public boolean equals(Object dmrpk) {
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		boolean cmp = false;
		if( dmr.domain == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(domain, dmr.domain);
		if( !cmp ) return cmp;
		if( dmr.map == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(map, dmr.map);
		if( uniqueKey || !cmp ) return cmp;
		if( dmr.range == null )
			return false;
		//return range.equals(dmr.range);
		return Morphism.fullEquals(range, dmr.range);
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return new DomainMapRange(domain, map, range);
    }

}
