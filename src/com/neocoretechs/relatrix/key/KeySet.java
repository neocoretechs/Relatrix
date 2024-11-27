package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.<p/>
 *  * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.<p/>
 * Since we are dealing with morphisms, basically an algebraic function mapping for f:x->y, or m:d->r, then
 * the primary key is composed of the domain and map components of the morphism. Since a function which takes a domain
 * object and maps it to a given range through a mapping object can result in only 1 mapping of a domain to range
 * through a particular mapping function. Consider as an extremely simplified example the domain integer object 1 
 * using the mapping function addOne results in a range object of 2, and only 2, and naturally composes with the
 * morphism domain 2 map addOne with a range of 3, producing functors 1 addOne 2 addOne 3 etc.<p/>
 * KeySet extends {@link PrimaryKeySet} to include the range object forming a complete morphism.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023,2024
 *
 */
public class KeySet extends PrimaryKeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
    protected DBKey rangeKey;
    //private ConcurrentHashMap<String, Boolean> primaryKeyCheck = new ConcurrentHashMap<String,Boolean>();

    public KeySet() {}
    
	public DBKey getRangeKey() {
		return rangeKey;
	}
	
	public void setRangeKey(DBKey rangeKey) {
		this.rangeKey = rangeKey;
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && DBKey.isValid(rangeKey);
	}

	public boolean isRangeKeyValid() {
		return DBKey.isValid(rangeKey);
	}
	
	public boolean rangeKeyEquals(KeySet o) {
		return rangeKey.equals(o.rangeKey);
	}

	/**
	 * Universal Store the instances to index and instance tables creating instance/DBKey and DBKey/instance tablespace entries
	 * @param alias alias or null
	 * @param transactionId or null
	 * @param skeyd instance for domain
	 * @param skeym instance for map
	 * @param skeyr instance for range
	 * @return the key of stored KeySet, which represents domain, map, range identity triplet index unique by domain and map
	 * @throws DuplicateKeyException if domain/map key already exist
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public DBKey store(Alias alias, TransactionId transactionId, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		if(transactionId == null) {
			if(alias == null) {
				setDomainKey(DBKey.newKey(indexTable, skeyd)); // puts to index and instance
				setMapKey(DBKey.newKey(indexTable, skeym)); // puts to index and instance
				setRangeKey(DBKey.newKey(indexTable, skeyr)); // puts to index and instance
				if(RelatrixKV.get(this) != null) {
					throw new DuplicateKeyException("Duplicate key for relationship:"+this);
				}
				return DBKey.newKey(indexTable, this);
			}
			setDomainKey(DBKey.newKey(alias, indexTable, skeyd)); // puts to index and instance
			setMapKey(DBKey.newKey(alias, indexTable, skeym)); // puts to index and instance
			setRangeKey(DBKey.newKey(alias, indexTable, skeyr)); // puts to index and instance
			if(RelatrixKV.get(alias,this) != null) {	
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			return DBKey.newKey(alias, indexTable, this);
		} else {
			if(alias == null) {
				setDomainKey(DBKey.newKey(transactionId, indexTable, skeyd)); // puts to index and instance
				setMapKey(DBKey.newKey(transactionId, indexTable, skeym)); // puts to index and instance
				setRangeKey(DBKey.newKey(transactionId, indexTable, skeyr)); // puts to index and instance
				if(RelatrixKVTransaction.get(transactionId,this) != null) {
					RelatrixKVTransaction.rollback(transactionId);
					throw new DuplicateKeyException("Duplicate key for relationship:"+this);
				}
				return DBKey.newKey(transactionId, indexTable, this);
			}
			setDomainKey(DBKey.newKey(alias, transactionId, indexTable, skeyd)); // puts to index and instance
			setMapKey(DBKey.newKey(alias, transactionId, indexTable, skeym)); // puts to index and instance
			setRangeKey(DBKey.newKey(alias, transactionId, indexTable, skeyr)); // puts to index and instance
			if(RelatrixKVTransaction.get(alias, transactionId,this) != null) {
				RelatrixKVTransaction.rollback(alias,transactionId);
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			return DBKey.newKey(alias, transactionId, indexTable, this);
		}
	}

	@Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		super.readExternal(in);
		RelatrixIndex r2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
		rangeKey = new DBKey(r1,r2);
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(rangeKey.getDatabaseIndex().getMsb());
		out.writeLong(rangeKey.getDatabaseIndex().getLsb());
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
		return String.format("domainKey:%s mapKey:%s rangeKey:%s%n", domainKey, mapKey, rangeKey);
	}
	
}
