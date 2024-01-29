package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;

/**
* This class represents the morphisms stored in map,domain,range (codomain) order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015,2021
*/ 
public class MapDomainRange extends Morphism {
	private static final long serialVersionUID = -3223516008906545636L;
    public MapDomainRange() {}
    
    public MapDomainRange(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    public MapDomainRange(String alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }
	public MapDomainRange(Comparable<?> d, Comparable<?> m, Comparable<?> r, boolean template) {
		super(d,m,r,template);
	}
	@Override
	public int compareTo(Object o) {
		if(!((KeySet)o).isMapKeyValid())
			return 1;
		int i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isDomainKeyValid())
			return 1;
		i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isRangeKeyValid())
			return 1;
		return getRangeKey().compareTo(((KeySet)o).getRangeKey());
	} 
	@Override
	public boolean equals(Object o) {
		if(!((KeySet)o).isValid())
			return false;
		return getMapKey().equals(((KeySet)o).getMapKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey());
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
		if(isMapKeyValid())
			result = prime * result + getMapKey().hashCode();
		if(isDomainKeyValid())
			result = prime * result + (int) (getDomainKey().hashCode() ^ (getDomainKey().hashCode() >>> 32));
	    if(isRangeKeyValid())
	    	result = prime * result + getRangeKey().hashCode();
	    return result;
	}
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		MapDomainRange dmr = (MapDomainRange)dmrpk;
		int cmp = 0;
		if( dmr.getMap() == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		if( cmp != 0 ) return cmp;
		if( dmr.getDomain() == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		cmp = Morphism.fullCompareTo(getDomain(), dmr.getDomain());
		if( cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 1;
		//return range.compareTo(dmr.range);
		return Morphism.fullCompareTo(getRange(), dmr.getRange());

	}

	@Override
	public boolean equals(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		MapDomainRange dmr = (MapDomainRange)dmrpk;
		boolean cmp = false;
		if( dmr.getMap() == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(getMap(), dmr.getMap());
		if( !cmp ) return cmp;
		if( dmr.getDomain() == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(getDomain(), dmr.getDomain());
		if( !cmp ) return cmp;
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
	*/
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
    	if(alias == null)
    		return new MapDomainRange(getDomain(), getMap(), getRange());
   		return new MapDomainRange(alias, getDomain(), getMap(), getRange());
    }
    
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getMapKey());
		out.writeObject(getDomainKey());
		out.writeObject(getRangeKey());	
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setMapKey((DBKey) in.readObject());
		setDomainKey((DBKey) in.readObject());
		setRangeKey((DBKey) in.readObject());
	}

}
