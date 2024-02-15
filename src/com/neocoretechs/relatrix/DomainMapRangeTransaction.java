package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.KeySet;

/**
 * This class represents the morphisms stored in their natural retrieval order.
 * The concept behind these permutations are to allow the Relatrix to go from Cat to Set.
 * By storing these indexes with all their possible retrieval combinations for the morphisms,
 * which turns out to be 6 indexes, we facilitate the retrieval of posets from our categories
 * based on any number of possible operators and objects passed to the various 'findSet' permutations. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 *
 */
public class DomainMapRangeTransaction extends MorphismTransaction {
	private static final long serialVersionUID = 8664384659501163179L;
	protected transient DBKey identity;
	
    public DomainMapRangeTransaction() {}
    
    public DomainMapRangeTransaction(String transactionId, Comparable d, Comparable m, Comparable r) {
    	super(transactionId, d,m,r);
    }

	public DomainMapRangeTransaction(String alias, String transactionId, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(alias, transactionId, d, m, r);
	}
	
	public DomainMapRangeTransaction(boolean b, String transactionId, Comparable d, Comparable m, Comparable r) {
		super(b, transactionId, d,m,r);
	}
	
	public DomainMapRangeTransaction(boolean b, String alias, String transactionId, Comparable d, Comparable m, Comparable r) {
		super(b, alias, transactionId, d,m,r);
	}
	
	public DomainMapRangeTransaction(boolean flag, String transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRangeTransaction(boolean flag, String alias, String transactionId, Comparable d, DBKey domainkey,
			Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(flag, alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRangeTransaction(String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey,
			Comparable r, DBKey rangeKey) {
		super(transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}

	public DomainMapRangeTransaction(String alias, String transactionId, Comparable d, DBKey domainkey, Comparable m,
			DBKey mapKey, Comparable r, DBKey rangeKey) {
		super(alias, transactionId, d, domainkey, m, mapKey, r, rangeKey);
	}
	public DBKey getDBKey() {
		return identity;
	}	
	public void setDBKey(DBKey identity) {
		this.identity = identity;
	}
    /*
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		DomainMapRangeTransaction dmr = (DomainMapRangeTransaction)dmrpk;
		if( dmr.getDomain() == null )
			return 1;
		//int cmp = domain.compareTo(dmr.domain);
		int cmp = Morphism.fullCompareTo(getDomain(), dmr.getDomain());
		if( cmp != 0 ) return cmp;
		if( dmr.getMap() == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		// if uniqueKey is set, compare only the domain and map
		if( uniqueKey || cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 1;
		//return range.compareTo(dmr.range);
		return Morphism.fullCompareTo(getRange(), dmr.getRange());
	}

	@Override
	public boolean equals(Object dmrpk) {
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		DomainMapRangeTransaction dmr = (DomainMapRangeTransaction)dmrpk;
		boolean cmp = false;
		if( dmr.getDomain() == null )
			return false;
		//cmp = domain.equals(dmr.domain);
		cmp = Morphism.fullEquals(getDomain(), dmr.getDomain());
		if( !cmp ) return cmp;
		if( dmr.getMap() == null )
			return false;
		//cmp = map.equals(dmr.map);
		cmp = Morphism.fullEquals(getMap(), dmr.getMap());
		if( uniqueKey || !cmp ) return cmp;
		if( dmr.getRange() == null )
			return false;
		//return range.equals(dmr.range);
		return Morphism.fullEquals(getRange(), dmr.getRange());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + (getDomain() == null ? 0 : getDomain().hashCode());
		result = 37*result + (getMap() == null ? 0 : getMap().hashCode());
		result = 37*result + (getRange() == null ? 0 : getRange().hashCode());
		return result;
	}
	*/

    @Override
    public Object clone() throws CloneNotSupportedException {
       	if(alias == null) {
    		if(templateFlag)
    			return new DomainMapRangeTransaction(templateFlag, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    		return new DomainMapRangeTransaction(transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    	}
   		if(templateFlag)
			return new DomainMapRangeTransaction(templateFlag, alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
   		return new DomainMapRangeTransaction(alias, transactionId, getDomain(), getDomainKey(), getMap(), getMapKey(), getRange(), getRangeKey());
    }
/*
    @Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
		domainKey.readExternal(in);
		mapKey.readExternal(in);
		rangeKey.readExternal(in);
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {  
		domainKey.writeExternal(out);
		mapKey.writeExternal(out);
		rangeKey.writeExternal(out);
	}
	*/

}
