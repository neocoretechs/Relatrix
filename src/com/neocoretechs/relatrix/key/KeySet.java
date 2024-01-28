package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023
 *
 */
public abstract class KeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private DBKey domainKey = new DBKey();
    private DBKey mapKey = new DBKey();
    private DBKey rangeKey = new DBKey();
    protected transient boolean primaryKeyCheck = false;

    public KeySet() {}
    
    public void setPrimaryKeyCheck(boolean check) {
    	primaryKeyCheck = check;
    }
    
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
		return domainKey.isValid() && mapKey.isValid() && rangeKey.isValid();
	}
	public boolean isDomainKeyValid() {
		return domainKey.isValid();
	}
	public boolean isMapKeyValid() {
		return mapKey.isValid();
	}
	public boolean isRangeKeyValid() {
		return rangeKey.isValid();
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
		setRangeKey( DBKey.newKey(indexTable, skeyr)); // puts to index and instance
		setPrimaryKeyCheck(true);
		if(RelatrixKV.get(this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKey(indexTable, this);
	}
	
	public DBKey storeAlias(String alias, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKeyAlias(alias, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKeyAlias(alias, indexTable, skeym)); // puts to index and instance
		setRangeKey(DBKey.newKeyAlias(alias, indexTable, skeyr)); // puts to index and instance
		setPrimaryKeyCheck(true);
		if(RelatrixKV.get(alias, this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKeyAlias(alias, indexTable, this);
	}
	
	public DBKey store(String xid, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKey(xid, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKey(xid, indexTable, skeym)); // puts to index and instance
		setRangeKey( DBKey.newKey(xid, indexTable, skeyr)); // puts to index and instance
		setPrimaryKeyCheck(true);
		if(RelatrixKVTransaction.get(xid, this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKey(xid, indexTable, this);
	}
	
	public DBKey storeAlias(String alias, String xid, Comparable skeyd, Comparable skeym, Comparable skeyr) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKeyAlias(alias, xid, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKeyAlias(alias, xid, indexTable, skeym)); // puts to index and instance
		setRangeKey(DBKey.newKeyAlias(alias, xid, indexTable, skeyr)); // puts to index and instance
		setPrimaryKeyCheck(true);
		if(RelatrixKVTransaction.get(alias, xid, this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKeyAlias(alias, xid, indexTable, this);
	}
	/**
	 * Store the instances to index and instance tables creating instance/DBKey and DBKey/instance tablespace entries
	 * @return the key of stored KeySet, which represents domain, map, range identity triplet index unique by domain and map
	 * @throws DuplicateKeyException if domain/map key already exist
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public DBKey store() throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setPrimaryKeyCheck(true);
		if(RelatrixKV.get(this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKey(indexTable, this);
	}
	
	public DBKey storeAlias(String alias) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setPrimaryKeyCheck(true);
		if(RelatrixKV.get(alias, this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKeyAlias(alias, indexTable, this);
	}
	
	public DBKey store(String xid) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setPrimaryKeyCheck(true);
		if(RelatrixKVTransaction.get(xid, this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKey(xid, indexTable, this);
	}
	
	public DBKey storeAlias(String alias, String xid) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setPrimaryKeyCheck(true);
		if(RelatrixKVTransaction.get(alias, xid, this) != null)
				throw new DuplicateKeyException(this);
		setPrimaryKeyCheck(false);
		return DBKey.newKeyAlias(alias, xid, indexTable, this);
	}
	
	@Override
	public abstract int compareTo(Object o); 
}
