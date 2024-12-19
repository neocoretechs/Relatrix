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
* This class represents the morphisms stored in range (codomain),domain,map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/ 
@DatabaseClass(tablespace="com.neocoretechs.relatrix.DomainMapRange")
public class RangeDomainMap extends Morphism {
	private static final long serialVersionUID = -1689898604140078900L;
    public RangeDomainMap() {}
    
    public RangeDomainMap(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    public RangeDomainMap(Alias alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }

    public RangeDomainMap(DomainMapRange identity) throws IOException {
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
    
    public RangeDomainMap(Alias alias, DomainMapRange identity) throws IOException {
    	this(identity);
    	this.alias = alias;
    }

	public RangeDomainMap(boolean b, Comparable d, Comparable m, Comparable r) {
		super(b, d, m, r);
	}
	
	public RangeDomainMap(boolean b, Alias alias, Comparable d, Comparable m, Comparable r) {
		super(b, alias, d, m, r);
	}
	
	public RangeDomainMap(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	public RangeDomainMap(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	public RangeDomainMap(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(boolean templateFlag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(templateFlag, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	public RangeDomainMap(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		//if(!keyCompare)
			//return compareToResolved(o);
		int i = getRangeKey().compareTo(((KeySet)o).getRangeKey());
		if(i != 0)
			return i;
		i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0)
			return i;
		return getMapKey().compareTo(((KeySet)o).getMapKey());
	} 
	@Override
	public boolean equals(Object o) {
		//if(!keyCompare)
			//return equalsResolved(o);
		return getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey());
	}
	@Override
	public int hashCode() {
		//if(!keyCompare)
			//return hashCodeResolved();
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getRangeKey().hashCode();
		result = prime * result + (int) (getDomainKey().hashCode() ^ (getDomainKey().hashCode() >>> 32));
	    result = prime * result + getMapKey().hashCode();
	    return result;
	}
	
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
      	if(alias == null) {
    		if(transactionId == null) {
    			if(templateFlag)
    				return new RangeDomainMap(templateFlag, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    			return new MapDomainRange(getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		}
  			if(templateFlag)
				return new RangeDomainMap(templateFlag, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
			return new RangeDomainMap(transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey()); 		
    	}
    	if(transactionId == null) {
    		if(templateFlag)
    			return new RangeDomainMap(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new RangeDomainMap(alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
    	if(templateFlag)
    		return new RangeDomainMap(templateFlag, alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	return new RangeDomainMap(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    
    @Override  
 	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
    	RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
    	RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
       	rangeKey = new DBKey(r1);
       	domainKey = new DBKey(d1);
    	mapKey = new DBKey(m1);
 	} 
 	
 	@Override  
 	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
 	}
 	
}
