package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.<p/>
 * Since we are dealing with morphisms, basically an algebraic function mapping for f:x->y, or m:d->r, then
 * the primary key is composed of the domain and map components of the morphism. Since a function which takes a domain
 * object and maps it to a given range through a mapping object can result in only 1 mapping of a domain to range
 * through a particular mapping function. Consider as an extremely simplified example the domain integer object 1 
 * using the mapping function addOne results in a range object of 2, and only 2, and naturally composes with the
 * morphism domain 2 map addOne with a range of 3, producing functors 1 addOne 2 addOne 3 etc.<p/>
 * KeySet extends {@link PrimaryKeySet} to include the range object forming a complete morphism.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023,2024
 */
public class KeySet extends PrimaryKeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
    protected DBKey rangeKey;

    public KeySet() {}
    
	protected KeySet(DBKey domainKey, DBKey mapKey, DBKey rangeKey) {
		super(domainKey, mapKey);
		this.rangeKey = rangeKey;
	}
	protected KeySet(DBKey domainKey, DBKey mapKey, DBKey rangeKey, TransactionId transactionId) {
		super(domainKey, mapKey, transactionId);
		this.rangeKey = rangeKey;
	}
	protected KeySet(DBKey domainKey, DBKey mapKey, DBKey rangeKey, Alias alias) {
		super(domainKey, mapKey, alias);
		this.rangeKey = rangeKey;
	}
	protected KeySet(DBKey domainKey, DBKey mapKey, DBKey rangeKey, Alias alias, TransactionId transactionId) {
		super(domainKey, mapKey, alias, transactionId);
		this.rangeKey = rangeKey;
	} 
	public DBKey getRangeKey() {
		return rangeKey;
	}
	
	public void setRangeKey(DBKey rangeKey) {
		this.rangeKey = rangeKey;
	}
	
	@Override
	boolean isValid() {
		return super.isValid() && DBKey.isValid(rangeKey);
	}

	public boolean isRangeKeyValid() {
		return DBKey.isValid(rangeKey);
	}
	
	public boolean rangeKeyEquals(KeySet o) {
		return rangeKey.equals(o.rangeKey);
	}

	@Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		super.readExternal(in);
		rangeKey = new DBKey(in.readLong(), in.readLong());
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeLong(rangeKey.getMsb());
		out.writeLong(rangeKey.getLsb());
	}
	
	@Override
	public int compareTo(Object o) {
		//if(DEBUG)
			//System.out.println("Keyset CompareTo "+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((KeySet)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((KeySet)o).getMapKey());
		int i = super.compareTo(o);
		if(i != 0) {
			//if(DEBUG)
				//System.out.println("Keyset CompareTo returning "+i+" at MapKey");
			return i;
		}
		//if(DEBUG)
			//System.out.println("Keyset CompareTo returning "+getRangeKey().compareTo(((KeySet)o).getRangeKey())+" at last RangeKey");
		return getRangeKey().compareTo(((KeySet)o).getRangeKey());
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && getRangeKey().equals(((KeySet)o).getRangeKey());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + getRangeKey().hashCode();
	}
	
	@Override
	public String toString() {
		return super.toString()+String.format(" rangeKey:%s%n", rangeKey);
	}
	
}
