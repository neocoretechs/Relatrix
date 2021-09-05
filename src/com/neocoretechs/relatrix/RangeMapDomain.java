package com.neocoretechs.relatrix;

import com.neocoretechs.relatrix.key.KeySet;

/**
* This class represents the morphisms stored in range (codomain),map,domain order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/ 
public final class RangeMapDomain extends Morphism {
	private static final long serialVersionUID = -2797189836505364776L;
    public RangeMapDomain() {}
    
    public RangeMapDomain(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
	public RangeMapDomain(Comparable<?> d, Comparable<?> m, Comparable<?> r, KeySet keys) {
		super(d,m,r,keys);
	}
	public RangeMapDomain(Comparable<?> d, Comparable<?> m, Comparable<?> r, boolean template) {
		super(d,m,r,template);
	}
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		RangeMapDomain dmr = (RangeMapDomain)dmrpk;
		int cmp = 0;
		if( dmr.getRange() == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(getRange(), dmr.getRange());
		if( cmp != 0 ) return cmp;
		if( dmr.getMap() == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		if( cmp != 0 ) return cmp;
		if( dmr.getDomain() == null )
			return 1;
		//return domain.compareTo(dmr.domain);
		return Morphism.fullCompareTo(getDomain(), dmr.getDomain());

	}

	@Override
	public boolean equals(Object dmrpk) {
		RangeMapDomain dmr = (RangeMapDomain)dmrpk;
		boolean cmp = false;
		if( dmr.getRange() == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(getRange(), dmr.getRange());
		if( !cmp ) return cmp;
		if( dmr.getMap() == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(getMap(), dmr.getMap());
		if( !cmp ) return cmp;
		if( dmr.getDomain() == null )
			return false;
		//return domain.equals(dmr.domain);
		return Morphism.fullEquals(getDomain(), dmr.getDomain());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (getDomain() == null ? 0 : getDomain().hashCode());
		result = 37*result + (getMap() == null ? 0 : getMap().hashCode());
		result = 37*result + (getRange() == null ? 0 : getRange().hashCode());
		return result;
	}
	/*
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
	*/
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return new RangeMapDomain(getDomain(), getMap(), getRange(), getKeys());
    }
}
