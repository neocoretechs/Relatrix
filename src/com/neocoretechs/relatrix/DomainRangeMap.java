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
* This class represents the morphisms stored in domain,range (codomain),map order.
* The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
* By storing these indexes with all their possible retrieval combinations for the morphisms,
* which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
* based on any number of possible operators and objects passed to the various 'findSet' permutations.
* @author Jonathan Groff (C) NeoCoreTechs 2014,2015
*/
@DatabaseClass(tablespace="com.neocoretechs.relatrix.DomainMapRange")
public class DomainRangeMap extends Morphism {
	private static final long serialVersionUID = -1694888225034392347L;
    public DomainRangeMap() {}
    
    public DomainRangeMap(DomainMapRange identity) throws IOException {
    	if(!identity.isDomainKeyValid())
    		throw new IOException("Domain key of identity is invalid: "+DBKey.whyInvalid(identity.getDomainKey())+".");
    	setDomainKey(identity.getDomainKey());
      	if(!identity.isMapKeyValid())
    		throw new IOException("Map key of identity is invalid: "+DBKey.whyInvalid(identity.getMapKey())+".");
    	setMapKey(identity.getMapKey());
    	if(!identity.isRangeKeyValid())
    		throw new IOException("Range key of identity is invalid: "+DBKey.whyInvalid(identity.getRangeKey())+".");
    	setRangeKey(identity.getRangeKey()); 	
    }
    public DomainRangeMap(Alias alias, DomainMapRange identity) throws IOException {
       	this(identity);
    	this.alias = alias;
    }
    public DomainRangeMap(Comparable d, Comparable m, Comparable r) {
       	super(d,m,r);
    }
	
	public DomainRangeMap(Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(alias, d,m,r);
	}

	public DomainRangeMap(boolean b, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(b, d, m, r);
	}
	
	public DomainRangeMap(boolean b, Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(b, alias, d, m, r);
	}
	
	public DomainRangeMap(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMap(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMap(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMap(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMap(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	public DomainRangeMap(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainRangeMap(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	public DomainRangeMap(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
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
   		if(templateFlag)
			return new DomainRangeMap(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new DomainRangeMap(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	
    }

    @Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
		RelatrixIndex d2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex r2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
		domainKey = new DBKey(d1,d2);
		rangeKey = new DBKey(r1,r2);
		mapKey = new DBKey(m1, m2);
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException { 
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());
		out.writeLong(domainKey.getDatabaseIndex().getMsb());
		out.writeLong(domainKey.getDatabaseIndex().getLsb());
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(rangeKey.getDatabaseIndex().getMsb());
		out.writeLong(rangeKey.getDatabaseIndex().getLsb());
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getDatabaseIndex().getMsb());
		out.writeLong(mapKey.getDatabaseIndex().getLsb());
	}
	
	
	public String toString() {
		switch(displayLevel) {
		case VERBOSE:
			return String.format("Class:%s %n %s%n%s%n%s%n %s%n%s%n%s%n %s%n%s%n%s%n-----%n",this.getClass().getName(),
					(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
					(getDomain() == null ? "NULL" : getDomain().toString()),
					(getDomainKey() == null ? "NULL" : getDomainKey().toString()),
					(getRange() == null ? "NULL" : getRange().getClass().getName()),	
					(getRange() == null ? "NULL" : getRange().toString()),
					(getRangeKey() == null ? "NULL" : getRangeKey().toString()),
					(getMap() == null ? "NULL" : getMap().getClass().getName()),
					(getMap() == null ? "NULL" : getMap().toString()),
					(getMapKey() == null ? "NULL" : getMapKey().toString()));
		case BRIEF:
			return String.format("Class:%s %n %s%n%s%n %s%n%s%n %s%n%s%n-----%n",this.getClass().getName(),
					(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
					(getDomain() == null ? "NULL" : getDomain().toString()),
					(getRange() == null ? "NULL" : getRange().getClass().getName()),	
					(getRange() == null ? "NULL" : getRange().toString()),
					(getMap() == null ? "NULL" : getMap().getClass().getName()),
					(getMap() == null ? "NULL" : getMap().toString()));
		case MINIMAL:
		default:
			return String.format("[%s->%s->%s]%n",
					(getDomain() == null ? "NULL" : getDomain().toString()),
					(getMap() == null ? "NULL" : getMap().toString()),
					(getRange() == null ? "NULL" : getRange().toString()));			
		}

	}
}
