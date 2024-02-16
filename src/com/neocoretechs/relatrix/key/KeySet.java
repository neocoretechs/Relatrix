package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.DuplicateKeyException;
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
	protected DBKey domainKey = new DBKey();
    protected DBKey mapKey = new DBKey();
    protected DBKey rangeKey = new DBKey();
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
	 * Store the instances to index and instance tables creating instance/DBKey and DBKey/instance tablespace entries
	 * @param skeyd instance for domain
	 * @param skeym instance for map
	 * @param skeyr instance for range
	 * @return the key of stored KeySet, which represents domain, map, range identity triplet index unique by domain and map
	 * @throws DuplicateKeyException if domain/map key already exist
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public DBKey store(Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKey(indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKey(indexTable, skeym)); // puts to index and instance
		PrimaryKeySet pks = new PrimaryKeySet(this);
		if(RelatrixKV.contains(KeySet.class, pks))
				throw new DuplicateKeyException(this);
		setRangeKey( DBKey.newKey(indexTable, skeyr)); // puts to index and instance
		return DBKey.newKey(indexTable, this);
	}
	
	public DBKey storeAlias(String alias, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKeyAlias(alias, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKeyAlias(alias, indexTable, skeym)); // puts to index and instance
		PrimaryKeySet pks = new PrimaryKeySet(this);
		if(RelatrixKV.contains(KeySet.class, pks))
				throw new DuplicateKeyException(this);
		setRangeKey(DBKey.newKeyAlias(alias, indexTable, skeyr)); // puts to index and instance
		return DBKey.newKeyAlias(alias, indexTable, this);
	}
	
	public DBKey store(String xid, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKey(xid, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKey(xid, indexTable, skeym)); // puts to index and instance
		PrimaryKeySet pks = new PrimaryKeySet(this);
		if(RelatrixKV.contains(KeySet.class, pks))
				throw new DuplicateKeyException(this);
		setRangeKey( DBKey.newKey(xid, indexTable, skeyr)); // puts to index and instance
		return DBKey.newKey(xid, indexTable, this);
	}
	
	public DBKey storeAlias(String alias, String xid, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKeyAlias(alias, xid, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKeyAlias(alias, xid, indexTable, skeym)); // puts to index and instance
		PrimaryKeySet pks = new PrimaryKeySet(this);
		if(RelatrixKV.contains(KeySet.class, pks))
				throw new DuplicateKeyException(this);
		setRangeKey(DBKey.newKeyAlias(alias, xid, indexTable, skeyr)); // puts to index and instance
		return DBKey.newKeyAlias(alias, xid, indexTable, this);
	}

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
		if(this instanceof PrimaryKeySet || o instanceof PrimaryKeySet) {
			if(DEBUG)
				System.out.println("Keyset CompareTo returning "+i+" at primary:"+getClass()+", "+o.getClass());
			return i;
		}
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
