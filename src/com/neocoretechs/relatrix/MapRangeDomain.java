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
* This class represents the morphisms stored in map,range (codomain),domain order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/
@DatabaseClass(tablespace="com.neocoretechs.relatrix.Relation")
public class MapRangeDomain extends AbstractRelation {
	private static final long serialVersionUID = 7422941687414371614L;
    public MapRangeDomain() {}
    
    public MapRangeDomain(AbstractRelation identity) throws IOException {
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
    
    MapRangeDomain(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
    MapRangeDomain(Alias alias, Comparable d, Comparable m, Comparable r) {
       	super(alias,d,m,r);
    }

    MapRangeDomain(Relation identity) throws IOException {
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

	public MapRangeDomain(boolean b, Comparable d, Comparable m, Comparable r) {
		super(b, d, m, r);
	}
	
	public MapRangeDomain(boolean b, Alias alias, Comparable d, Comparable m, Comparable r) {
		super(b, alias, d, m, r);
	}
	
	MapRangeDomain(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapRangeDomain(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapRangeDomain(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	MapRangeDomain(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public MapRangeDomain(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	MapRangeDomain(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	MapRangeDomain(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	MapRangeDomain(Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	MapRangeDomain(boolean templateFlag, TransactionId transactionId, Comparable d, DBKey domainKey,Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(templateFlag, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	MapRangeDomain(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		//if(!keyCompare)
			//return compareToResolved(o);
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
		//if(!keyCompare)
			//return equalsResolved(o);
		return getMapKey().equals(((KeySet)o).getMapKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getDomainKey().equals(((KeySet)o).getDomainKey());
	}
	@Override
	public int hashCode() {
		//if(!keyCompare)
			//return hashCodeResolved();
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getMapKey().hashCode();
		result = prime * result + (int) (getRangeKey().hashCode() ^ (getRangeKey().hashCode() >>> 32));
	    result = prime * result + getDomainKey().hashCode();
	    return result;
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
      	try {
    		return new MapRangeDomain(this);
    	} catch (IOException e) {
    		throw new CloneNotSupportedException(e.getMessage());
    	}
    }

    @Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
    	mapKey = new DBKey(in.readLong(), in.readLong());
     	rangeKey = new DBKey(in.readLong(), in.readLong());
    	domainKey = new DBKey(in.readLong(), in.readLong());
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(mapKey.getMsb());
		out.writeLong(mapKey.getLsb());
		out.writeLong(rangeKey.getMsb());
		out.writeLong(rangeKey.getLsb());
		out.writeLong(domainKey.getMsb());
		out.writeLong(domainKey.getLsb());
	}
	
}
