package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.DatabaseClass;
import com.neocoretechs.rocksack.TransactionId;


/**
* This class represents the morphisms stored in range (codomain),map,domain order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
*/
@DatabaseClass(tablespace="com.neocoretechs.relatrix.Relation")
public class RangeMapDomain extends AbstractRelation {
	private static final long serialVersionUID = -2797189836505364776L;
    public RangeMapDomain() {}
    
    public RangeMapDomain(AbstractRelation identity) throws IOException {
    	this.templateFlag = identity.templateFlag;
    	this.alias = identity.getAlias();
    	this.transactionId = identity.getTransactionId();
    	this.identity = identity.getIdentity();
    	//if(!identity.isDomainKeyValid())
    	//	throw new IOException("Domain key of identity is invalid: "+DBKey.whyInvalid(identity.getDomainKey())+".");
    	setDomainKey(identity.getDomainKey());
    	domain = identity.domain;
      	//if(!identity.isMapKeyValid())
    	//	throw new IOException("Map key of identity is invalid: "+DBKey.whyInvalid(identity.getMapKey())+".");
    	setMapKey(identity.getMapKey());
    	map = identity.map;
    	//if(!identity.isRangeKeyValid())
    	//	throw new IOException("Range key of identity is invalid: "+DBKey.whyInvalid(identity.getRangeKey())+".");
    	setRangeKey(identity.getRangeKey());
    	range = identity.range;
    }
    
    RangeMapDomain(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    
    RangeMapDomain(Alias alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }

    RangeMapDomain(Relation identity) throws IOException {
	   	this.templateFlag = identity.templateFlag;
    	this.alias = identity.getAlias();
    	this.transactionId = identity.getTransactionId();
    	this.identity = identity.getIdentity();
    	//if(!identity.isDomainKeyValid())
    	//	throw new IOException("Domain key of identity is invalid: "+DBKey.whyInvalid(identity.getDomainKey())+".");
    	setDomainKey(identity.getDomainKey());
    	domain = identity.domain;
      	//if(!identity.isMapKeyValid())
    	//	throw new IOException("Map key of identity is invalid: "+DBKey.whyInvalid(identity.getMapKey())+".");
    	setMapKey(identity.getMapKey());
    	map = identity.map;
    	//if(!identity.isRangeKeyValid())
    	//	throw new IOException("Range key of identity is invalid: "+DBKey.whyInvalid(identity.getRangeKey())+".");
    	setRangeKey(identity.getRangeKey());
    	range = identity.range;
    }

	public RangeMapDomain(boolean b, Comparable d, Comparable m, Comparable r) {
		super(b, d, m, r);
	}
	
	public RangeMapDomain(boolean b, Alias alias, Comparable d, Comparable m, Comparable r) {
		super(b, alias, d, m, r);
	}
	
	RangeMapDomain(boolean flag, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, d, domainKey, m, mapKey, r, rangeKey);
	}

	RangeMapDomain(boolean flag, Alias alias, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainKey, m, mapKey, r, rangeKey);
	}

	RangeMapDomain(Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainKey, m, mapKey, r, rangeKey);
	}

	RangeMapDomain(Alias alias, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainKey, m, mapKey, r, rangeKey);
	}

	public RangeMapDomain(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	RangeMapDomain(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	RangeMapDomain(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	RangeMapDomain(Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	RangeMapDomain(boolean templateFlag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(templateFlag, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	RangeMapDomain(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		//if(!keyCompare)
			//return compareToResolved(o);
		int i = getRangeKey().compareTo(((KeySet)o).getRangeKey());
		if(i != 0)
			return i;
		i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(i != 0)
			return i;
		return getDomainKey().compareTo(((KeySet)o).getDomainKey());
	} 
	@Override
	public boolean equals(Object o) {
		//if(!keyCompare)
			//return equalsResolved(o);
		return getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey());
	}
	@Override
	public int hashCode() {
		//if(!keyCompare)
			//return hashCodeResolved();
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getRangeKey().hashCode();
		result = prime * result + (int) (getMapKey().hashCode() ^ (getMapKey().hashCode() >>> 32));
	    result = prime * result + getDomainKey().hashCode();
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
    	try {
    		return new RangeMapDomain(this);
    	} catch (IOException e) {
    		throw new CloneNotSupportedException(e.getMessage());
    	}
    }
    
    @Override  
  	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
    	rangeKey = new DBKey(in.readLong(), in.readLong());
      	mapKey = new DBKey(in.readLong(), in.readLong());
    	domainKey = new DBKey(in.readLong(), in.readLong());
  	} 
  	
  	@Override  
  	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(rangeKey.getMsb());
		out.writeLong(rangeKey.getLsb());
		out.writeLong(mapKey.getMsb());
		out.writeLong(mapKey.getLsb());
		out.writeLong(domainKey.getMsb());
		out.writeLong(domainKey.getLsb());
  	}
  	
}
