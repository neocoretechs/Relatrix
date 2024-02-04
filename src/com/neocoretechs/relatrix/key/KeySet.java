package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

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
	private static boolean DEBUG = true;
	private DBKey domainKey = new DBKey();
    private DBKey mapKey = new DBKey();
    private DBKey rangeKey = new DBKey();
    private boolean primaryKeyCheck = false;

    public KeySet() {}
    
    public void setPrimaryKeyCheck(boolean check) {
    	if(DEBUG)
    		System.out.println("Setting primary key check:"+check+" for "+this);
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
	 
	
	@Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {  
		setDomainKey((DBKey) in.readObject());
		setMapKey((DBKey) in.readObject());
		setRangeKey((DBKey) in.readObject());
		primaryKeyCheck = in.readBoolean();
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {  
		out.writeObject(getDomainKey());
		out.writeObject(getMapKey());
		out.writeObject(getRangeKey());
		out.writeBoolean(primaryKeyCheck);
	}
	@Override
	public int compareTo(Object o) {
		if(DEBUG)
			System.out.println("Keyset CompareTo "+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((KeySet)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((KeySet)o).getMapKey());
		if(!((KeySet)o).isDomainKeyValid())
			return 0;
		int i = getDomainKey().compareTo(((KeySet)o).getDomainKey());
		if(i != 0)
			return i;
		if(!((KeySet)o).isMapKeyValid())
			return 0;
		i = getMapKey().compareTo(((KeySet)o).getMapKey());
		if(primaryKeyCheck || ((KeySet)o).primaryKeyCheck) {
			if(DEBUG)
				System.out.println("***** Primary key check for "+getDomainKey()+" "+getMapKey()+" returning "+i);
			return i;
		}
		if(i != 0)
			return i;
		if(!((KeySet)o).isRangeKeyValid())
			return 0;
		return getRangeKey().compareTo(((KeySet)o).getRangeKey());
	} 
	@Override
	public boolean equals(Object o) {
		if(!((KeySet)o).isValid())
			return false;
		if(primaryKeyCheck || ((KeySet)o).primaryKeyCheck)
			return getDomainKey().equals(((KeySet)o).getDomainKey()) &&
					getMapKey().equals(((KeySet)o).getMapKey());
		return getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey()) &&
				getRangeKey().equals(((KeySet)o).getRangeKey());
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
		if(isDomainKeyValid())
			result = prime * result + getDomainKey().hashCode();
		if(isMapKeyValid())
			result = prime * result + (int) (getMapKey().hashCode() ^ (getMapKey().hashCode() >>> 32));
	    if(!primaryKeyCheck && isRangeKeyValid())
	    	result = prime * result + getRangeKey().hashCode();
	    return result;
	}
	
}
