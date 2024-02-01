package com.neocoretechs.relatrix;

import java.io.Externalizable;
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
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021,2023,2024
 *
 */
public class DomainMapRange extends Morphism implements Comparable, Externalizable, Cloneable {
	private static final long serialVersionUID = 8664384659501163179L;
	
    public DomainMapRange() {}
    
    public DomainMapRange(Comparable d, Comparable m, Comparable r) {
    	super(d,m,r);
    }

    public DomainMapRange(Comparable d, Comparable m, Comparable r, boolean template) {
    	super(d,m,r,template);
    }
	public DomainMapRange(String alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) {
		super(alias, d, m, r);
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object dmrpk) {
		if(keyCompare)
			return super.compareTo(dmrpk);
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialCompareTo(this, (Comparable) dmrpk);
		DomainMapRange dmr = (DomainMapRange)dmrpk;
		if( dmr.getDomain() == null )
			return 1;
		//int cmp = domain.compareTo(dmr.domain);
		int cmp = Morphism.fullCompareTo(getDomain(), dmr.getDomain());
		if( cmp != 0 ) return cmp;
		if( dmr.getMap() == null )
			return 1;
		//cmp = map.compareTo(dmr.map);
		cmp = Morphism.fullCompareTo(getMap(), dmr.getMap());
		if( cmp != 0 ) return cmp;
		if( dmr.getRange() == null )
			return 1;
		//return range.compareTo(dmr.range);
		return Morphism.fullCompareTo(getRange(), dmr.getRange());
	}

	@Override
	public boolean equals(Object dmrpk) {
		if(keyCompare)
			return super.equals(dmrpk);
		if(!this.getClass().equals(dmrpk.getClass()) && !dmrpk.getClass().isAssignableFrom(this.getClass())) 
			return Morphism.partialEquals(this, (Comparable) dmrpk);
		DomainMapRange dmr = (DomainMapRange)dmrpk;
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
		if( !cmp ) return cmp;
		if( dmr.getRange() == null )
			return false;
		//return range.equals(dmr.range);
		return Morphism.fullEquals(getRange(), dmr.getRange());
	}
	
	@Override
	public int hashCode() {
		if(keyCompare)
			return super.hashCode();
		int result = 17;
		result = 37*result + (getDomain() == null ? 0 : getDomain().hashCode());
		result = 37*result + (getMap() == null ? 0 : getMap().hashCode());
		result = 37*result + (getRange() == null ? 0 : getRange().hashCode());
		return result;
	}
	
	
    @Override
    public Object clone() throws CloneNotSupportedException {
    	if(alias == null)
    		return new DomainMapRange(getDomain(), getMap(), getRange());
   		return new DomainMapRange(alias, getDomain(), getMap(), getRange());
    }
    
    public String toString() { 
    	return String.format("Class:%s %n[%s->%s->%s]%n[%s->%s->%s]%n",this.getClass().getName(),
    			(getDomain() == null ? "NULL" :getDomain().getClass().getName()), 
    			(getMap() == null ? "NULL" : getMap().getClass().getName()),
    			(getRange() == null ? "NULL" : getRange().getClass().getName()),
    			(getDomain() == null ? "NULL" : getDomain()),
    			(getMap() == null ? "NULL" : getMap()),
    			(getRange() == null ? "NULL" : getRange()));
    }

}
