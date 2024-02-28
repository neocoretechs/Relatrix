package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.DerivedClass;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023
 *
 */
public class PrimaryKeySet extends DomainMapRange implements Externalizable, Comparable, DerivedClass {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
    //private ConcurrentHashMap<String, Boolean> primaryKeyCheck = new ConcurrentHashMap<String,Boolean>();

    public PrimaryKeySet() {}
    
	public boolean isValid() {
		return DBKey.isValid(domainKey) && DBKey.isValid(mapKey);
	}
	public boolean isDomainKeyValid() {
		return DBKey.isValid(domainKey);
	}
	public boolean isMapKeyValid() {
		return DBKey.isValid(mapKey);
	}
	public boolean domainKeyEquals(PrimaryKeySet o) {
		return domainKey.equals(o.domainKey);
	}
	public boolean mapKeyEquals(PrimaryKeySet o) {
		return mapKey.equals(o.mapKey);
	}

	/**
	 * Store the instances to index and instance tables creating instance/DBKey and DBKey/instance tablespace entries
	 * @param skeyd instance for domain
	 * @param skeym instance for map
	 * @return the key of stored KeySet, which represents domain, map, range identity triplet index unique by domain and map
	 * @throws DuplicateKeyException if domain/map key already exist
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public DBKey store(Comparable skeyd, Comparable skeym) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKey(indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKey(indexTable, skeym)); // puts to index and instance
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		if(RelatrixKV.get(this) != null) {
			throw new DuplicateKeyException("Duplicate key for relationship:"+this);
		}
		return DBKey.newKey(indexTable, this);
	}
	
	public DBKey storeAlias(String alias, Comparable skeyd, Comparable skeym) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKeyAlias(alias, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKeyAlias(alias, indexTable, skeym)); // puts to index and instance
		if(RelatrixKV.get(this) != null) {	
			throw new DuplicateKeyException("Duplicate key for relationship:"+this);
		}
		return DBKey.newKeyAlias(alias, indexTable, this);
	}
	
	public DBKey store(String xid, Comparable skeyd, Comparable skeym) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKey(xid, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKey(xid, indexTable, skeym)); // puts to index and instance
		if(RelatrixKVTransaction.get(xid,this) != null) {
			RelatrixKVTransaction.rollback(xid);
			throw new DuplicateKeyException("Duplicate key for relationship:"+this);
		}
		return DBKey.newKey(xid, indexTable, this);
	}
	
	public DBKey storeAlias(String alias, String xid, Comparable skeyd, Comparable skeym) throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		setDomainKey(DBKey.newKeyAlias(alias, xid, indexTable, skeyd)); // puts to index and instance
		setMapKey(DBKey.newKeyAlias(alias, xid, indexTable, skeym)); // puts to index and instance
		if(RelatrixKVTransaction.get(xid,this) != null) {
			RelatrixKVTransaction.rollback(alias,xid);
			throw new DuplicateKeyException("Duplicate key for relationship:"+this);
		}
		return DBKey.newKeyAlias(alias, xid, indexTable, this);
	}

	@Override  
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		RelatrixIndex d2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m2 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
		domainKey = new DBKey(d1,d2);
		mapKey = new DBKey(m1, m2);	
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
		return getMapKey().compareTo(((KeySet)o).getMapKey());
	}
	
	@Override
	public boolean equals(Object o) {
		return getDomainKey().equals(((KeySet)o).getDomainKey()) &&
				getMapKey().equals(((KeySet)o).getMapKey());// &&
	}
	
	@Override
	public int hashCode() {
		return getDomainKey().hashCode() + getMapKey().hashCode();
	}
	
	public String toString() {
		return String.format("domainKey:%s mapKey:%s%n", domainKey, mapKey);
	}
	
}
