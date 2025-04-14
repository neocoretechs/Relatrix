package com.neocoretechs.relatrix;

import java.io.Serializable;

import com.neocoretechs.relatrix.key.DBKey;



/**
 * This class represents a {@link AbstractRelation} stored in its natural retrieval order.<p/>
 * The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
 * By storing these indexes with all their possible retrieval combinations for the morphisms,
 * which turns out to be 6 indexes, we facilitate the retrieval of ordered sets from our categories
 * based on any number of possible operators and objects passed to the various 'findSet' permutations. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2023,2024
 *
 */
public class Relation extends AbstractRelation implements Comparable, Serializable, Cloneable {
	private static final long serialVersionUID = 8664384659501163179L;
	private static boolean DEBUG = false;
  
    public Relation() {}
    
    public Relation(AbstractRelation identity) {  
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
    public Relation(Comparable d, Comparable m, Comparable r) {
    	super(d,m,r);
    }
  
	public Relation(Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(alias, d, m, r);
	}
 
    public Relation(TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
    	super(transactionId, d, m, r);
    }
    
    public Relation(boolean flag, Comparable d, Comparable m, Comparable r) {
    	super(flag, d,m,r);
    }
  
	public Relation(boolean flag, Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(flag, alias, d, m, r);
	}
	
    public Relation(boolean flag, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, d, domainkey, m, mapKey, r, rangeKey);
	}

	public Relation(boolean flag, Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, d, domainkey, m, mapKey, r, rangeKey);
	}

	public Relation(Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(d, domainkey, m, mapKey, r, rangeKey);
	}

	public Relation(Alias alias, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, d, domainkey, m, mapKey, r, rangeKey);
	}
	
	public Relation(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(flag, alias, transactionId, d, m, r);
	}

	public Relation(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public Relation(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
		super(alias, transactionId, d, m, r);
	}

	public Relation(Alias alias, TransactionId transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public Relation(boolean flag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	public Relation(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainKey, m, mapKey, r, rangeKey);
	}

	@Override
	public int compareTo(Object o) {
		if(DEBUG)
			System.out.println("Relation CompareTo:"+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((Relation)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((Relation)o).getMapKey());
		int i = getDomainKey().compareTo(((Relation)o).getDomainKey());
		if(i != 0) {
			if(DEBUG)
				System.out.println("Relation CompareTo returning "+i+" at DomainKey");
			return i;
		}
		i = getMapKey().compareTo(((Relation)o).getMapKey());
		if(i != 0) {
			if(DEBUG)
				System.out.println("Relation CompareTo returning "+i+" at MapKey");
			return i;
		}
		if(DEBUG)
			System.out.println("Relation CompareTo returning "+getRangeKey().compareTo(((Relation)o).getRangeKey())+" at last RangeKey");
		return getRangeKey().compareTo(((Relation)o).getRangeKey());
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
		return getDomainKey().equals(((Relation)o).getDomainKey()) &&
				getMapKey().equals(((Relation)o).getMapKey()) &&
				getRangeKey().equals(((Relation)o).getRangeKey());
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return new Relation(this);
    }
    
}
