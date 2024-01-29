package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;

/**
* This class represents the morphisms stored in map,range (codomain),domain order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/ 
public class MapRangeDomain extends Morphism {
	private static final long serialVersionUID = 7422941687414371614L;
    public MapRangeDomain() {}
    
    public MapRangeDomain(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    public MapRangeDomain(String alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }
	public MapRangeDomain(Comparable<?> d, Comparable<?> m, Comparable<?> r, boolean template) {
		super(d,m,r,template);
	}
	@Override
	public int compareTo(Object o) {
		if(!((KeySet)o).isMapKeyValid())
			return 1;
		int i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isRangeKeyValid())
			return 1;
		i = getRangeKey().compareTo(((KeySet)o).getRangeKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isDomainKeyValid())
			return 1;
		return getDomainKey().compareTo(((KeySet)o).getDomainKey());
	} 
	@Override
	public boolean equals(Object o) {
		if(!((KeySet)o).isValid())
			return false;
		return getMapKey().equals(((KeySet)o).getMapKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey());
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
		if(isMapKeyValid())
			result = prime * result + getMapKey().hashCode();
		if(isRangeKeyValid())
			result = prime * result + (int) (getRangeKey().hashCode() ^ (getRangeKey().hashCode() >>> 32));
	    if(isDomainKeyValid())
	    	result = prime * result + getDomainKey().hashCode();
	    return result;
	}
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		MapRangeDomain dmr = (MapRangeDomain)dmrpk;
		int cmp = 0;
		if(dmr.getMap() == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		if( cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(getRange(), dmr.getRange());
		if( cmp != 0 ) return cmp;
		if( dmr.getDomain() == null )
			return 1;
		//return domain.compareTo(dmr.domain);
		return Morphism.fullCompareTo(getDomain(), dmr.getDomain());
	}

	@Override
	public boolean equals(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		MapRangeDomain dmr = (MapRangeDomain)dmrpk;
		boolean cmp = false;
		if( dmr.getMap() == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(getMap(), dmr.getMap());
		if( !cmp ) return cmp;
		if( dmr.getRange() == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(getRange(), dmr.getRange());
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
	*/
    /*
    public Comparable returnTupleOrder(int n) {
    	// default dmr
    	switch(n) {
    		case 1:
    			return map;
    		case 2:
    			return range;
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
    	if(alias == null)
    		return new MapRangeDomain(getDomain(), getMap(), getRange());
		return new MapRangeDomain(alias, getDomain(), getMap(), getRange());
    }
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getMapKey());
		out.writeObject(getRangeKey());	
		out.writeObject(getDomainKey());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setMapKey((DBKey) in.readObject());
		setRangeKey((DBKey) in.readObject());
		setDomainKey((DBKey) in.readObject());
	}
}
