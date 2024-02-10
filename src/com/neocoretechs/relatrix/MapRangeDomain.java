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

    public MapRangeDomain(DomainMapRange identity) throws IOException {
    	if(!identity.isDomainKeyValid())
    		throw new IOException("Domain key of identity is invalid.");
    	setDomainKey(identity.getDomainKey());
      	if(!identity.isMapKeyValid())
    		throw new IOException("Map key of identity is invalid.");
    	setMapKey(identity.getMapKey());
    	if(!identity.isRangeKeyValid())
    		throw new IOException("Range key of identity is invalid.");
    	setRangeKey(identity.getRangeKey()); 	
    }
    public MapRangeDomain(String alias, DomainMapRange identity) throws IOException {
    	this(identity);
    	this.alias = alias;
    }

	public MapRangeDomain(boolean b, Comparable d, Comparable m, Comparable r) {
		super(b, d, m, r);
	}
	
	public MapRangeDomain(boolean b, String alias, Comparable d, Comparable m, Comparable r) {
		super(b, alias, d, m, r);
	}
	
	public MapRangeDomain(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomain(boolean flag, String alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomain(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomain(String alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		if(!keyCompare)
			return compareToResolved(o);
		int i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(i != 0)
			return i;
		i = getRangeKey().compareTo(((KeySet)o).getRangeKey());
		if(i != 0)
			return i;
		return getDomainKey().compareTo(((KeySet)o).getDomainKey());
	} 
	@Override
	public boolean equals(Object o) {
		if(!keyCompare)
			return equalsResolved(o);
		return getMapKey().equals(((KeySet)o).getMapKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey());
	}
	@Override
	public int hashCode() {
		if(!keyCompare)
			return hashCodeResolved();
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getMapKey().hashCode();
		result = prime * result + (int) (getRangeKey().hashCode() ^ (getRangeKey().hashCode() >>> 32));
	    result = prime * result + getDomainKey().hashCode();
	    return result;
	}
	
	@SuppressWarnings("unchecked")
	public int compareToResolved(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		MapRangeDomain dmr = (MapRangeDomain)dmrpk;
		int cmp = 0;
		if(dmr.getMap() == null )
			return 0;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		if( cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 0;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(getRange(), dmr.getRange());
		if( cmp != 0 ) return cmp;
		if( dmr.getDomain() == null )
			return 0;
		//return domain.compareTo(dmr.domain);
		return Morphism.fullCompareTo(getDomain(), dmr.getDomain());
	}

	public boolean equalsResolved(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		MapRangeDomain dmr = (MapRangeDomain)dmrpk;
		boolean cmp = false;
		if( dmr.getMap() == null )
			return true;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(getMap(), dmr.getMap());
		if( !cmp ) return cmp;
		if( dmr.getRange() == null )
			return true;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(getRange(), dmr.getRange());
		if( !cmp ) return cmp;
		if( dmr.getDomain() == null )
			return true;
		//return domain.equals(dmr.domain);
		return Morphism.fullEquals(getDomain(), dmr.getDomain());
	}
	
	public int hashCodeResolved() {
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
       	if(alias == null) {
    		if(templateFlag)
    			return new MapRangeDomain(templateFlag, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new MapRangeDomain(getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
   		if(templateFlag)
			return new MapRangeDomain(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new MapRangeDomain(alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
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
	public String toString() { 
		return String.format("Class:%s %n %s%n%s%n%s%n %s%n%s%n%s%n %s%n%s%n%s%n-----%n",this.getClass().getName(),
				(getMap() == null ? "NULL" : getMap().getClass().getName()),
				(getMap() == null ? "NULL" : getMap().toString()),
				(getMapKey() == null ? "NULL" : getMapKey().toString()),
				(getRange() == null ? "NULL" : getRange().getClass().getName()),	
				(getRange() == null ? "NULL" : getRange().toString()),
				(getRangeKey() == null ? "NULL" : getRangeKey().toString()),
				(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
				(getDomain() == null ? "NULL" : getDomain().toString()),
				(getDomainKey() == null ? "NULL" : getDomainKey().toString()));
	}

}
