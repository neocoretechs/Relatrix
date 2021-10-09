package com.neocoretechs.relatrix;

import com.neocoretechs.relatrix.key.KeySet;

/**
 * This class represents the morphisms stored in their natural retrieval order.
 * The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
 * By storing these indexes with all their possible retrieval combinations for the morphisms,
 * which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
 * based on any number of possible operators and objects passed to the various 'findSet' permutations. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class DomainMapRange extends Morphism {
	private static final long serialVersionUID = 8664384659501163179L;
	private static boolean uniqueKey = false;
	
    public DomainMapRange() {}
    
    public DomainMapRange(Comparable d, Comparable m, Comparable r) {
    	super(d,m,r);
    }
    public DomainMapRange(Comparable d, Comparable m, Comparable r, KeySet keys) {
    	super(d,m,r,keys);
    }
    public DomainMapRange(Comparable d, Comparable m, Comparable r, boolean template) {
    	super(d,m,r,template);
    }
    /**
     * The purpose here is to control the compareTo method such that when storing, the range portion
     * is ignored by setting uniqueKey to true, otherwise, we want to include the range value in our
     * comparisons when retrieving.
     * @param unique
     */
    public void setUniqueKey(boolean unique) { uniqueKey = unique; }
    
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		if( dmr.getDomain() == null )
			return 1;
		//int cmp = domain.compareTo(dmr.domain);
		int cmp = Morphism.fullCompareTo(getDomain(), dmr.getDomain());
		if( cmp != 0 ) return cmp;
		if( dmr.getMap() == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		// if uniqueKey is set, compare only the domain and map
		if( uniqueKey || cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 1;
		//return range.compareTo(dmr.range);
		return Morphism.fullCompareTo(getRange(), dmr.getRange());
	}

	@Override
	public boolean equals(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		boolean cmp = false;
		if( dmr.getDomain() == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(getDomain(), dmr.getDomain());
		if( !cmp ) return cmp;
		if( dmr.getMap() == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(getMap(), dmr.getMap());
		if( uniqueKey || !cmp ) return cmp;
		if( dmr.getRange() == null )
			return false;
		//return range.equals(dmr.range);
		return Morphism.fullEquals(getRange(), dmr.getRange());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (getDomain() == null ? 0 : getDomain().hashCode());
		result = 37*result + (getMap() == null ? 0 : getMap().hashCode());
		result = 37*result + (getRange() == null ? 0 : getRange().hashCode());
		return result;
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return new DomainMapRange(getDomain(), getMap(), getRange(), getKeys());
    }

}
