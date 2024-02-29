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
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023
 *
 */
public class KeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
	protected DBKey domainKey;
    protected DBKey mapKey;
    protected DBKey rangeKey;
    //private ConcurrentHashMap<String, Boolean> primaryKeyCheck = new ConcurrentHashMap<String,Boolean>();

    public KeySet() {}
    
	public DBKey getDomainKey() {
		return domainKey;
	}
	public void setDomainKey(DBKey domainKey) {
		this.domainKey = domainKey;
	}
	public DBKey getMapKey() {
		return mapKey;
	}
	public void setMapKey(DBKey mapKey) {
		this.mapKey = mapKey;
	}
	public DBKey getRangeKey() {
		return rangeKey;
	}
	public void setRangeKey(DBKey rangeKey) {
		this.rangeKey = rangeKey;
	}

	public boolean isValid() {
		return DBKey.isValid(domainKey) && DBKey.isValid(mapKey) && DBKey.isValid(rangeKey);
	}
	public boolean isDomainKeyValid() {
		return DBKey.isValid(domainKey);
	}
	public boolean isMapKeyValid() {
		return DBKey.isValid(mapKey);
	}
	public boolean isRangeKeyValid() {
		return DBKey.isValid(rangeKey);
	}
	public boolean domainKeyEquals(KeySet o) {
		return domainKey.equals(o.domainKey);
	}
	public boolean mapKeyEquals(KeySet o) {
		return mapKey.equals(o.mapKey);
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
	public DBKey store(String alias, String transactionId, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
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
			setDomainKey(DBKey.newKeyAlias(alias, indexTable, skeyd)); // puts to index and instance
			setMapKey(DBKey.newKeyAlias(alias, indexTable, skeym)); // puts to index and instance
			setRangeKey(DBKey.newKeyAlias(alias,indexTable, skeyr)); // puts to index and instance
			if(RelatrixKV.get(alias,this) != null) {	
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			return DBKey.newKeyAlias(alias, indexTable, this);
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
			setDomainKey(DBKey.newKeyAlias(alias, transactionId, indexTable, skeyd)); // puts to index and instance
			setMapKey(DBKey.newKeyAlias(alias, transactionId, indexTable, skeym)); // puts to index and instance
			setRangeKey(DBKey.newKeyAlias(alias, transactionId, indexTable, skeyr)); // puts to index and instance
			if(RelatrixKVTransaction.get(alias, transactionId,this) != null) {
				RelatrixKVTransaction.rollback(alias,transactionId);
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			return DBKey.newKeyAlias(alias, transactionId, indexTable, this);
		}
	}

	@Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		RelatrixIndex d2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex r2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex r1 = new RelatrixIndex(in.readLong(), in.readLong());
		domainKey = new DBKey(d1,d2);
		mapKey = new DBKey(m1, m2);	
		rangeKey = new DBKey(r1,r2);
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());
		out.writeLong(domainKey.getDatabaseIndex().getMsb());
		out.writeLong(domainKey.getDatabaseIndex().getLsb());
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getDatabaseIndex().getMsb());
		out.writeLong(mapKey.getDatabaseIndex().getLsb());
		out.writeLong(rangeKey.getInstanceIndex().getMsb());
		out.writeLong(rangeKey.getInstanceIndex().getLsb());
		out.writeLong(rangeKey.getDatabaseIndex().getMsb());
		out.writeLong(rangeKey.getDatabaseIndex().getLsb());
	}
	
	@Override
	public int compareTo(Object o) {
		//if(DEBUG)
			//System.out.println("Keyset CompareTo "+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((KeySet)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((KeySet)o).getMapKey());
		int i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0) {
			//if(DEBUG)
				//System.out.println("Keyset CompareTo returning "+i+" at DomainKey");
			return i;
		}
		i = getMapKey().compareTo(((KeySet)o).getMapKey());
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
		return getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey());
	}
	
	@Override
	public int hashCode() {
		return getDomainKey().hashCode() + getMapKey().hashCode() + getRangeKey().hashCode();
	}
	
	public String toString() {
		return String.format("domainKey:%s mapKey:%s rangeKey:%s%n", domainKey, mapKey, rangeKey);
	}
	
}
