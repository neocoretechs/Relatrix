package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.DatabaseClass;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.<p/>
 * Since we are dealing with morphisms, basically an algebraic function mapping for f:x->y, or m:d->r, then
 * the primary key is composed of the domain and map components of the morphism. Since a function which takes a domain
 * object and maps it to a given range through a mapping object can result in only 1 mapping of a domain to range
 * through a particular mapping function. Consider as an extremely simplified example the domain integer object 1 
 * using the mapping function addOne results in a range object of 2, and only 2, and naturally composes with the
 * morphism domain 2 map addOne with a range of 3, producing functors 1 addOne 2 addOne 3 etc.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023
 *
 */
@DatabaseClass(tablespace="com.neocoretechs.relatrix.DomainMapRange")
public class PrimaryKeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
	protected DBKey domainKey;
    protected DBKey mapKey;
	private transient TransactionId transactionId = null;
	private transient Alias alias = null;
    //private ConcurrentHashMap<String, Boolean> primaryKeyCheck = new ConcurrentHashMap<String,Boolean>();

    public PrimaryKeySet() {}
    
	public PrimaryKeySet(Morphism identity) {
		setDomainKey(identity.getDomainKey());
		setMapKey(identity.getMapKey());
		this.transactionId = identity.getTransactionId();
		this.alias = identity.getAlias();
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
	 * Universal Store the instances to index and instance tables creating instance/DBKey and DBKey/instance tablespace entries
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
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		if(transactionId == null) {
			if(alias == null) {
				setDomainKey(DBKey.newKey(indexTable, skeyd)); // puts to index and instance
				setMapKey(DBKey.newKey(indexTable, skeym)); // puts to index and instance
				if(RelatrixKV.get(this) != null) {
					throw new DuplicateKeyException("Duplicate key for relationship:"+this);
				}
				return DBKey.newKey(indexTable, this);
			}
			setDomainKey(DBKey.newKey(alias, indexTable, skeyd)); // puts to index and instance
			setMapKey(DBKey.newKey(alias, indexTable, skeym)); // puts to index and instance
			if(RelatrixKV.get(alias, this) != null) {	
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			return DBKey.newKey(alias, indexTable, this);
		} else {
			if(alias == null) {
				setDomainKey(DBKey.newKey(transactionId, indexTable, skeyd)); // puts to index and instance
				setMapKey(DBKey.newKey(transactionId, indexTable, skeym)); // puts to index and instance
				if(RelatrixKVTransaction.get(transactionId, this) != null) {
					RelatrixKVTransaction.rollback(transactionId);
					throw new DuplicateKeyException("Duplicate key for relationship:"+this);
				}
				return DBKey.newKey(transactionId, indexTable, this);
			}
			setDomainKey(DBKey.newKey(alias, transactionId, indexTable, skeyd)); // puts to index and instance
			setMapKey(DBKey.newKey(alias, transactionId, indexTable, skeym)); // puts to index and instance
			if(RelatrixKVTransaction.get(alias, transactionId, this) != null) {
				RelatrixKVTransaction.rollback(alias, transactionId);
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			return DBKey.newKey(alias, transactionId, indexTable, this);
		}
	}
	
	/**
	* Store keys from superclass copy constructor, checking for existing
	* @return the new DBKey to use as our key for the main class
	*/
	public DBKey store() throws DuplicateKeyException, IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		DBKey newKey = null;
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		if(transactionId == null) {
			if(alias == null) {
				if(RelatrixKV.get(this) != null) {
					throw new DuplicateKeyException("Duplicate key for relationship:"+this);
				}
				newKey = indexTable.getNewDBKey();
				RelatrixKV.store(this, newKey);
				return newKey;
			} 
			if(RelatrixKV.get(alias, this) != null) {	
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			newKey = indexTable.getNewDBKey(alias);
			RelatrixKV.store(alias, this, newKey);
			return newKey;
		} else {
			if(alias == null) {
				if(RelatrixKVTransaction.get(transactionId,this) != null) {
					RelatrixKVTransaction.rollback(transactionId);
					throw new DuplicateKeyException("Duplicate key for relationship:"+this);
				}
				newKey = indexTable.getNewDBKey(transactionId);
				RelatrixKVTransaction.store(transactionId, this, newKey);
				return newKey;
			}
			if(RelatrixKVTransaction.get(alias, transactionId, this) != null) {
				RelatrixKVTransaction.rollback(alias,transactionId);
				throw new DuplicateKeyException("Duplicate key for relationship:"+this);
			}
			newKey = indexTable.getNewDBKey(alias, transactionId);
			RelatrixKVTransaction.store(alias, transactionId, this, newKey);
			return newKey;
		}
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
		int i = getDomainKey().compareTo(((PrimaryKeySet)o).getDomainKey());
		if(i != 0) {
			//if(DEBUG)
				//System.out.println("Keyset CompareTo returning "+i+" at DomainKey");
			return i;
		}
		return getMapKey().compareTo(((PrimaryKeySet)o).getMapKey());
	}
	
	@Override
	public boolean equals(Object o) {
		return getDomainKey().equals(((PrimaryKeySet)o).getDomainKey()) &&
				getMapKey().equals(((PrimaryKeySet)o).getMapKey());// &&
	}
	
	@Override
	public int hashCode() {
		return getDomainKey().hashCode() + getMapKey().hashCode();
	}
	
	public String toString() {
		return String.format("domainKey:%s mapKey:%s%n", domainKey, mapKey);
	}
	
}
