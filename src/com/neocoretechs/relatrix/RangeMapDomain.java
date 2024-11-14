package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.RelatrixIndex;
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
@DatabaseClass(tablespace="com.neocoretechs.relatrix.DomainMapRange")
public class RangeMapDomain extends Morphism {
	private static final long serialVersionUID = -2797189836505364776L;
    public RangeMapDomain() {}
    
    public RangeMapDomain(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    public RangeMapDomain(Alias alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }

    public RangeMapDomain(DomainMapRange identity) throws IOException {
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
    public RangeMapDomain(Alias alias, DomainMapRange identity) throws IOException {
    	this(identity);
    	this.alias = alias;
    }

	public RangeMapDomain(boolean b, Comparable d, Comparable m, Comparable r) {
		super(b, d, m, r);
	}
	
	public RangeMapDomain(boolean b, Alias alias, Comparable d, Comparable m, Comparable r) {
		super(b, alias, d, m, r);
	}
	
	public RangeMapDomain(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeMapDomain(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeMapDomain(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeMapDomain(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeMapDomain(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	public RangeMapDomain(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeMapDomain(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	public RangeMapDomain(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
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
   		if(templateFlag)
			return new RangeMapDomain(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new RangeMapDomain(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    
    @Override  
  	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
    	RelatrixIndex r2 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
      	RelatrixIndex m2 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex d2 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
    	rangeKey = new DBKey(r1,r2);
      	mapKey = new DBKey(m1, m2);
    	domainKey = new DBKey(d1,d2);
  	} 
  	
  	@Override  
  	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(rangeKey.getDatabaseIndex().getMsb());
		out.writeLong(rangeKey.getDatabaseIndex().getLsb());
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getDatabaseIndex().getMsb());
		out.writeLong(mapKey.getDatabaseIndex().getLsb());
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());
		out.writeLong(domainKey.getDatabaseIndex().getMsb());
		out.writeLong(domainKey.getDatabaseIndex().getLsb());
  	}
  	
	public String toString() {
		switch(displayLevel) {
		case VERBOSE:
			return String.format("Class:%s %n %s%n%s%n%s%n %s%n%s%n%s%n %s%n%s%n%s%n-----%n",this.getClass().getName(),
					(getRange() == null ? "NULL" : getRange().getClass().getName()),	
					(getRange() == null ? "NULL" : getRange().toString()),
					(getRangeKey() == null ? "NULL" : getRangeKey().toString()),
					(getMap() == null ? "NULL" : getMap().getClass().getName()),
					(getMap() == null ? "NULL" : getMap().toString()),
					(getMapKey() == null ? "NULL" : getMapKey().toString()),
					(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
					(getDomain() == null ? "NULL" : getDomain().toString()),
					(getDomainKey() == null ? "NULL" : getDomainKey().toString()));
		case BRIEF:
			return String.format("Class:%s %n %s%n%s%n %s%n%s%n %s%n%s%n-----%n",this.getClass().getName(),
					(getRange() == null ? "NULL" : getRange().getClass().getName()),	
					(getRange() == null ? "NULL" : getRange().toString()),
					(getMap() == null ? "NULL" : getMap().getClass().getName()),
					(getMap() == null ? "NULL" : getMap().toString()),
					(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
					(getDomain() == null ? "NULL" : getDomain().toString()));
		case MINIMAL:
		default:
			return String.format("[%s->%s->%s]%n",
					(getDomain() == null ? "NULL" : getDomain().toString()),
					(getMap() == null ? "NULL" : getMap().toString()),
					(getRange() == null ? "NULL" : getRange().toString()));
		}
	}
}
