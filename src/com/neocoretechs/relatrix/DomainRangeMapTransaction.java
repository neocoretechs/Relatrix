package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.RelatrixIndex;

/**
* This class represents the morphisms stored in domain,range (codomain),map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/ 
public class DomainRangeMapTransaction extends MorphismTransaction {
	private static final long serialVersionUID = -1694888225034392347L;
    public DomainRangeMapTransaction() {}
    
    public DomainRangeMapTransaction(DomainMapRangeTransaction identity) throws IOException {
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
    public DomainRangeMapTransaction(String alias, DomainMapRangeTransaction identity) throws IOException {
       	this(identity);
    	this.alias = alias;
    }
    
    public DomainRangeMapTransaction(String transactionId, Comparable d, Comparable m, Comparable r) {
       	super(transactionId,d,m,r);
    }
    public DomainRangeMapTransaction(String alias, String transactionId, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }

	public DomainRangeMapTransaction(boolean b, String xid, Comparable d, Comparable m, Comparable r) {
		super(b, xid, d, m, r);
	}
	
	public DomainRangeMapTransaction(boolean b, String alias, String xid, Comparable d, Comparable m, Comparable r) {
		super(b, alias, xid, d, m, r);
	}
	
	public DomainRangeMapTransaction(boolean flag, String transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMapTransaction(boolean flag, String alias, String transactionId, Comparable d, DBKey domainkey,
			Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMapTransaction(String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMapTransaction(String alias, String transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		DomainRangeMapTransaction dmr = (DomainRangeMapTransaction)dmrpk;
		int cmp = 0;
		if( dmr.getDomain() == null )
			return 1;
		//cmp = domain.compareTo(dmr.domain);
		cmp = Morphism.fullCompareTo(getDomain(), dmr.getDomain());
		if( cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 1;
		//cmp = range.compareTo(dmr.range);
		cmp = Morphism.fullCompareTo(getRange(), dmr.getRange());
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
		DomainRangeMapTransaction dmr = (DomainRangeMapTransaction)dmrpk;
		boolean cmp = false;
		if( dmr.getDomain() == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(getDomain(), dmr.getDomain());
		if( !cmp ) return cmp;
		if( dmr.getRange() == null )
			return false;
		//cmp = range.equals(dmr.range);
		cmp = Morphism.fullEquals(getRange(), dmr.getRange());
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
    /*
    public Comparable returnTupleOrder(int n) {
    	// default dmr
    	switch(n) {
    		case 1:
    			return domain;
    		case 2:
    			return range;
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
       	if(alias == null) {
    		if(templateFlag)
    			return new DomainRangeMapTransaction(templateFlag, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new DomainRangeMapTransaction(transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
   		if(templateFlag)
			return new DomainRangeMapTransaction(templateFlag, alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new DomainRangeMapTransaction(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    
    @Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
		RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex d2 = new RelatrixIndex(in.readLong(), in.readLong());
		domainKey = new DBKey(d1,d2);
		RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex r2 = new RelatrixIndex(in.readLong(), in.readLong());
		rangeKey = new DBKey(r1,r2);
		RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m2 = new RelatrixIndex(in.readLong(), in.readLong());
		mapKey = new DBKey(m1, m2);
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {  
		out.writeLong(domainKey.getDatabaseIndex().getMsb());
		out.writeLong(domainKey.getDatabaseIndex().getLsb());
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());
		out.writeLong(rangeKey.getDatabaseIndex().getMsb());
		out.writeLong(rangeKey.getDatabaseIndex().getLsb());
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getDatabaseIndex().getMsb());
		out.writeLong(mapKey.getDatabaseIndex().getLsb());
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
	}


}
