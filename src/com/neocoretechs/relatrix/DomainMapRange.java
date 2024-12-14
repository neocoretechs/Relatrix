package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.Serializable;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * This class represents the morphisms stored in their natural retrieval order.
 * The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
 * By storing these indexes with all their possible retrieval combinations for the morphisms,
 * which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
 * based on any number of possible operators and objects passed to the various 'findSet' permutations. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2023,2024
 *
 */
public class DomainMapRange extends Morphism implements Comparable, Serializable, Cloneable {
	private static final long serialVersionUID = 8664384659501163179L;
	private static boolean DEBUG = false;
    
    public DomainMapRange() {}
    
    public DomainMapRange(Comparable d, Comparable m, Comparable r) {
    	super(d,m,r);
    }
  
	public DomainMapRange(Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(alias, d, m, r);
	}
    
    public DomainMapRange(boolean flag, Comparable d, Comparable m, Comparable r) {
    	super(flag, d,m,r);
    }
  
	public DomainMapRange(boolean flag, Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(flag, alias, d, m, r);
	}
	
    public DomainMapRange(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r,
			DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRange(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRange(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRange(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}
	
	public DomainMapRange(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	public DomainMapRange(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRange(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	public DomainMapRange(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DBKey store(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException {
		if(locate(d, m)) {
			setDomainResolved(d);
			setMapResolved(m);
			setRange(r);
			identity = DBKey.newKey(IndexResolver.getIndexInstanceTable(), this);
			return identity;
		}
		throw new DuplicateKeyException("Relationship "+d+"->"+r+" already exists.");
	}
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (getDomain() == null ? 0 : getDomain().hashCode());
		result = 37*result + (getMap() == null ? 0 : getMap().hashCode());
		result = 37*result + (getRange() == null ? 0 : getRange().hashCode());
		return result;
	}
	
	
    @Override
    public Object clone() throws CloneNotSupportedException {
    	if(alias == null) {
    		if(templateFlag)
    			return new DomainMapRange(templateFlag, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new DomainMapRange(getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
   		if(templateFlag)
			return new DomainMapRange(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new DomainMapRange(alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    

    public String toString() { 
    	switch(displayLevel) {
    	case VERBOSE:
    		return String.format("Class:%s %n %s%n%s%n%s%n %s%n%s%n%s%n %s%n%s%n%s%n-----%n",this.getClass().getName(),
    				(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
    				(getDomain() == null ? "NULL" : getDomain().toString()),
    				(getDomainKey() == null ? "NULL" : getDomainKey().toString()),
    				(getMap() == null ? "NULL" : getMap().getClass().getName()),
    				(getMap() == null ? "NULL" : getMap().toString()),
    				(getMapKey() == null ? "NULL" : getMapKey().toString()),
    				(getRange() == null ? "NULL" : getRange().getClass().getName()),	
    				(getRange() == null ? "NULL" : getRange().toString()),
    				(getRangeKey() == null ? "NULL" : getRangeKey().toString()));
    	case BRIEF:
    		return String.format("Class:%s %n %s%n%s%n %s%n%s%n %s%n%s%n-----%n",this.getClass().getName(),
    				(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
    				(getDomain() == null ? "NULL" : getDomain().toString()),
    				(getMap() == null ? "NULL" : getMap().getClass().getName()),
    				(getMap() == null ? "NULL" : getMap().toString()),
    				(getRange() == null ? "NULL" : getRange().getClass().getName()),	
    				(getRange() == null ? "NULL" : getRange().toString()));
    	case MINIMAL:
    	default:
    		return String.format("[%s->%s->%s]%n",
    				(getDomain() == null ? "NULL" : getDomain().toString()),
    				(getMap() == null ? "NULL" : getMap().toString()),
    				(getRange() == null ? "NULL" : getRange().toString()));
    	}
    }

}
