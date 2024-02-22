package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.RelatrixIndex;

/**
* This class represents the morphisms stored in map,range (codomain),domain order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/ 
public class MapRangeDomainTransaction extends MorphismTransaction {
	private static final long serialVersionUID = 7422941687414371614L;
    public MapRangeDomainTransaction() {}
    
    public MapRangeDomainTransaction(DomainMapRangeTransaction identity) throws IOException {
    	this.transactionId = identity.transactionId;
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
    public MapRangeDomainTransaction(String alias, DomainMapRangeTransaction identity) throws IOException {
    	this(identity);
    	this.alias = alias;
    }
    public MapRangeDomainTransaction(String transactionId, Comparable d, Comparable m, Comparable r) {
       	super(transactionId,d,m,r);
    }
    public MapRangeDomainTransaction(String alias, String transactionId, Comparable d, Comparable m, Comparable r) {
       	super(alias,transactionId,d,m,r);
    }

	public MapRangeDomainTransaction(boolean flag, String transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, transactionId, d, m, r);
	}

	public MapRangeDomainTransaction(boolean flag, String transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomainTransaction(boolean flag, String alias, String transactionId, Comparable d, Comparable m,
			Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	public MapRangeDomainTransaction(boolean flag, String alias, String transactionId, Comparable d, DBKey domainkey,
			Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomainTransaction(String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomainTransaction(String alias, String transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		MapRangeDomainTransaction dmr = (MapRangeDomainTransaction)dmrpk;
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
		MapRangeDomainTransaction dmr = (MapRangeDomainTransaction)dmrpk;
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
    			return new MapRangeDomainTransaction(templateFlag, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new MapRangeDomainTransaction(transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
   		if(templateFlag)
			return new MapRangeDomainTransaction(templateFlag, alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new MapRangeDomainTransaction(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    
    @Override  
 	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
       	RelatrixIndex m2 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex r2 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex d2 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
      	mapKey = new DBKey(m1, m2);
      	rangeKey = new DBKey(r1,r2);
    	domainKey = new DBKey(d1,d2);
 	} 
 	
 	@Override  
 	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getDatabaseIndex().getMsb());
		out.writeLong(mapKey.getDatabaseIndex().getLsb());
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(rangeKey.getDatabaseIndex().getMsb());
		out.writeLong(rangeKey.getDatabaseIndex().getLsb());
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());	
		out.writeLong(domainKey.getDatabaseIndex().getMsb());
		out.writeLong(domainKey.getDatabaseIndex().getLsb());
 	}
 	
}
