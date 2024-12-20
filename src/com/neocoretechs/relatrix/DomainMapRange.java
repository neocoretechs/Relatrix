package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.Serializable;

import com.neocoretechs.relatrix.key.DBKey;
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

	public DomainMapRange(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
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

	public DomainMapRange(boolean flag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	public DomainMapRange(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}
	/**
	 * Store the fully prepared Morphism. The assumption is that all tenasactionId, alias, primary key, have been set and resolved.
	 * The range will added and resolved and a newKey operation will be performed to set the identity.
	 * @param d The domain instance resolved via locate in {@link com.neocoretechs.relatrix.key.PrimaryKeySet}
	 * @param m The map instance resolved via loate in {@link com.neocoretechs.relatrix.key.PrimaryKeySet}
	 * @param r The range instance which will be resolved via setRange.
	 * @return The {@link DBKey} identity of the new dmr relation.
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws DuplicateKeyException
	 */
	public DBKey store(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException {
		if(locate(d, m)) {
			setDomainResolved(d);
			setMapResolved(m);
			if(alias != null)
				setRange(alias, r);
			else
				setRange(r);
			identity = newKey(this);
			return identity;
		}
		throw new DuplicateKeyException("Relationship ["+d+"->"+r+"] already exists.");
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
