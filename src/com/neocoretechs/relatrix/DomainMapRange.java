package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * This class represents a {@link Morphism} stored in its natural retrieval order.<p/>
 * The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
 * By storing these indexes with all their possible retrieval combinations for the morphisms,
 * which turns out to be 6 indexes, we facilitate the retrieval of ordered sets from our categories
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
	
    public DomainMapRange(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainMapRange(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainMapRange(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainMapRange(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}
	
	public DomainMapRange(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	DomainMapRange(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRange(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	DomainMapRange(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	DomainMapRange(boolean flag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	DomainMapRange(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		if(DEBUG)
			System.out.println("DomainMapRange CompareTo:"+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((DomainMapRange)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((DomainMapRange)o).getMapKey());
		int i = getDomainKey().compareTo(((DomainMapRange)o).getDomainKey());
		if(i != 0) {
			if(DEBUG)
				System.out.println("DomainMapRange CompareTo returning "+i+" at DomainKey");
			return i;
		}
		i = getMapKey().compareTo(((DomainMapRange)o).getMapKey());
		if(i != 0) {
			if(DEBUG)
				System.out.println("DomainMapRange CompareTo returning "+i+" at MapKey");
			return i;
		}
		if(DEBUG)
			System.out.println("DomainMapRange CompareTo returning "+getRangeKey().compareTo(((DomainMapRange)o).getRangeKey())+" at last RangeKey");
		return getRangeKey().compareTo(((DomainMapRange)o).getRangeKey());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (getDomainKey() == null ? 0 : getDomainKey().hashCode());
		result = 37*result + (getMapKey() == null ? 0 : getMapKey().hashCode());
		result = 37*result + (getRangeKey() == null ? 0 : getRangeKey().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		return getDomainKey().equals(((DomainMapRange)o).getDomainKey()) &&
				getMapKey().equals(((DomainMapRange)o).getMapKey()) &&
				getRangeKey().equals(((DomainMapRange)o).getRangeKey());
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
    	if(alias == null) {
    		if(transactionId == null) {
    			if(templateFlag)
    				return new DomainMapRange(templateFlag, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    			return new DomainMapRange(getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		}
  			if(templateFlag)
				return new DomainMapRange(templateFlag, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
			return new DomainMapRange(transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());		
    	}
    	if(transactionId == null) {
    		if(templateFlag)
    			return new DomainMapRange(templateFlag, alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new DomainMapRange(alias, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
    	if(templateFlag)
    		return new DomainMapRange(templateFlag, alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	return new DomainMapRange(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
    
}
