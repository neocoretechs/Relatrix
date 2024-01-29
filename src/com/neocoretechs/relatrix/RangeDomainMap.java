package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;

/**
* This class represents the morphisms stored in range (codomain),domain,map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/ 
public class RangeDomainMap extends Morphism {
	private static final long serialVersionUID = -1689898604140078900L;
    public RangeDomainMap() {}
    
    public RangeDomainMap(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    public RangeDomainMap(String alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }
	public RangeDomainMap(Comparable<?> d, Comparable<?> m, Comparable<?> r, boolean template) {
		super(d,m,r,template);
	}
	@Override
	public int compareTo(Object o) {
		if(!((KeySet)o).isRangeKeyValid())
			return 1;
		int i = getRangeKey().compareTo(((KeySet)o).getRangeKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isDomainKeyValid())
			return 1;
		i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isMapKeyValid())
			return 1;
		return getMapKey().compareTo(((KeySet)o).getMapKey());
	} 
	@Override
	public boolean equals(Object o) {
		if(!((KeySet)o).isValid())
			return false;
		return getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey());
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
		if(isRangeKeyValid())
			result = prime * result + getRangeKey().hashCode();
		if(isDomainKeyValid())
			result = prime * result + (int) (getDomainKey().hashCode() ^ (getDomainKey().hashCode() >>> 32));
	    if(isMapKeyValid())
	    	result = prime * result + getMapKey().hashCode();
	    return result;
	}
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		RangeDomainMap dmr = (RangeDomainMap)dmrpk;
		int cmp = 0;
		if( dmr.getRange() == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(getRange(), dmr.getRange());
		if( cmp != 0 ) return cmp;
		if( dmr.getDomain() == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		cmp = Morphism.fullCompareTo(getDomain(), dmr.getDomain());
		if( cmp != 0 ) return cmp;
		if( dmr.getMap() == null )
			return 1;
		//return map.compareTo(dmr.map);
		return Morphism.fullCompareTo(getMap(), dmr.getMap());
	}

	@Override
	public boolean equals(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		RangeDomainMap dmr = (RangeDomainMap)dmrpk;
		boolean cmp = false;
		if( dmr.getRange() == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(getRange(), dmr.getRange());
		if( !cmp ) return cmp;
		if( dmr.getDomain() == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(getDomain(), dmr.getDomain());
		if( !cmp ) return cmp;
		if( dmr.getMap() == null )
			return false;
		//return map.equals(dmr.map);
		return Morphism.fullEquals(getMap(), dmr.getMap());
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
    	if(alias == null)
    		return new RangeDomainMap(getDomain(), getMap(), getRange());
  		return new RangeDomainMap(alias, getDomain(), getMap(), getRange());
    }
    
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getRangeKey());	
		out.writeObject(getDomainKey());
		out.writeObject(getMapKey());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setRangeKey((DBKey) in.readObject());
		setDomainKey((DBKey) in.readObject());
		setMapKey((DBKey) in.readObject());
	}
}
