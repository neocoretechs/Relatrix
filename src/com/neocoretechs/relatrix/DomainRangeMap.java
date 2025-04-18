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
* This class represents the morphisms stored in domain,range (codomain),map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/
@DatabaseClass(tablespace="com.neocoretechs.relatrix.Relation")
public class DomainRangeMap extends AbstractRelation {
	private static final long serialVersionUID = -1694888225034392347L;
    public DomainRangeMap() {}
    
    public DomainRangeMap(AbstractRelation identity) throws IOException {
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

    DomainRangeMap(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
	
	DomainRangeMap(Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(alias, d,m,r);
	}

	public DomainRangeMap(boolean b, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(b, d, m, r);
	}
	
	public DomainRangeMap(boolean b, Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(b, alias, d, m, r);
	}
	
	DomainRangeMap(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainRangeMap(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainRangeMap(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainRangeMap(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMap(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	DomainRangeMap(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainRangeMap(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	DomainRangeMap(Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	DomainRangeMap(boolean templateFlag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(templateFlag, transactionId, d, domainKey, m, mapKey, r , rangeKey);
	}

	DomainRangeMap(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		//if(!keyCompare)
			//return compareToResolved(o);
		int i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0)
			return i;
		i = getRangeKey().compareTo(((KeySet)o).getRangeKey());
		if(i != 0)
			return i;
		return getMapKey().compareTo(((KeySet)o).getMapKey());
	
	} 
	@Override
	public boolean equals(Object o) {
		//if(!keyCompare)
			//return equalsResolved(o);
		return getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey());
	}
	@Override
	public int hashCode() {
		//if(!keyCompare)
			//return hashCodeResolved();
	    final int prime = 31;
	    int result = 1;
		result = prime * result + getDomainKey().hashCode();
	    result = prime * result + getRangeKey().hashCode() ^ (getRangeKey().hashCode() >>> 32);
		result = prime * result + (int)(getMapKey().hashCode());
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
    	try {
			return new DomainRangeMap(this);
		} catch (IOException e) {
			throw new CloneNotSupportedException(e.getMessage());
		}
    }

    @Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
		domainKey = new DBKey(in.readLong(), in.readLong());
		rangeKey = new DBKey(in.readLong(), in.readLong());
		mapKey = new DBKey(in.readLong(), in.readLong());
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(domainKey.getMsb());
		out.writeLong(domainKey.getLsb());
		out.writeLong(rangeKey.getMsb());
		out.writeLong(rangeKey.getLsb());
		out.writeLong(mapKey.getMsb());
		out.writeLong(mapKey.getLsb());
	}
	
}
