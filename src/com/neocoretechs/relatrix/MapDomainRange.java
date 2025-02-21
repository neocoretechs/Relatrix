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
* This class represents the morphisms stored in map,domain,range (codomain) order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015,2021
*/
@DatabaseClass(tablespace="com.neocoretechs.relatrix.Relation")
public class MapDomainRange extends AbstractRelation {
	private static final long serialVersionUID = -3223516008906545636L;
    public MapDomainRange() {}
    
    MapDomainRange(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    MapDomainRange(Alias alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }

    MapDomainRange(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapDomainRange(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapDomainRange(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	MapDomainRange(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapDomainRange(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	MapDomainRange(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapDomainRange(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	MapDomainRange(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapDomainRange(Relation identity) throws IOException {
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
	
    MapDomainRange(Alias alias, Relation identity) throws IOException {
    	this(identity);
    	this.alias = alias;
    }

	public MapDomainRange(boolean b, Comparable d, Comparable m, Comparable r) {
		super(b, d, m, r);
	}

	public MapDomainRange(boolean b, Alias alias, Comparable d, Comparable m, Comparable r) {
		super(b, alias, d, m, r);
	}
	
	MapDomainRange(boolean templateFlag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(templateFlag, transactionId,d, domainKey, m , mapKey, r, rangeKey);
	}

	MapDomainRange(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		//if(!keyCompare)
			//return compareToResolved(o);
		int i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(i != 0)
			return i;
		i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0)
			return i;
		return getRangeKey().compareTo(((KeySet)o).getRangeKey());
	} 
	@Override
	public boolean equals(Object o) {
		//if(!keyCompare)
			//return equalsResolved(o);
		return getMapKey().equals(((KeySet)o).getMapKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey());
	}
	@Override
	public int hashCode() {
		//if(!keyCompare)
			//return hashCodeResolved();
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getMapKey().hashCode();
		result = prime * result + (int) (getDomainKey().hashCode() ^ (getDomainKey().hashCode() >>> 32));
	    result = prime * result + getRangeKey().hashCode();
	    return result;
	}
		
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
       	if(alias == null) {
    		if(transactionId == null) {
    			if(templateFlag)
    				return new MapDomainRange(templateFlag, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    			return new MapDomainRange(getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		}
  			if(templateFlag)
				return new MapDomainRange(templateFlag, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
			return new MapDomainRange(transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey()); 		
    	}
    	if(transactionId == null) {
    		if(templateFlag)
    			return new MapDomainRange(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new MapDomainRange(alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
    	if(templateFlag)
    		return new MapDomainRange(templateFlag, alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	return new MapDomainRange(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    
    @Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		mapKey = new DBKey(in.readLong(), in.readLong());
		domainKey = new DBKey(in.readLong(), in.readLong());
		rangeKey = new DBKey(in.readLong(), in.readLong());
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(mapKey.getMsb());
		out.writeLong(mapKey.getLsb());
		out.writeLong(domainKey.getMsb());
		out.writeLong(domainKey.getLsb());
		out.writeLong(rangeKey.getMsb());
		out.writeLong(rangeKey.getLsb());
	}
	
}
