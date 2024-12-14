package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Class fronts the actual instances in the Relatrix relations so as to normalize those actual instances.<p/>
 * Since our relations are composed of multiple indexes of otherwise redundant data, we need to have a means of
 * reducing the overhead at the cost of computational cycles. Minor redundancy is incurred due to the method
 * of storage of 2 tables indexed by [index, instance] and [instance, index] so we can do
 * lookups by index or instance. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public final class DBKey implements Comparable, Externalizable {
	private static final long serialVersionUID = -7511519913473997228L;
	private static boolean DEBUG = false;
	public static RelatrixIndex nullKey = new RelatrixIndex(0L, 0L);
	public static RelatrixIndex fullKey = new RelatrixIndex(0xFFFFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL);
	public static DBKey nullDBKey = new DBKey(nullKey, nullKey);
	public static DBKey fullDBKey = new DBKey(fullKey, fullKey);
	
	RelatrixIndex instanceIndex = null;
	RelatrixIndex databaseIndex = null;
	
	public DBKey() {}
	
	public DBKey(UUID databaseIndex, UUID instanceIndex) {
		this.databaseIndex = new RelatrixIndex(databaseIndex.getMostSignificantBits(), databaseIndex.getLeastSignificantBits());
		this.instanceIndex = new RelatrixIndex(instanceIndex.getMostSignificantBits(), instanceIndex.getLeastSignificantBits());
		if(DEBUG)
			System.out.println("DBKey ctor:"+this.databaseIndex+" "+this.instanceIndex);
	}
	
	public DBKey(RelatrixIndex databaseIndex, RelatrixIndex instanceIndex) {
		this.databaseIndex = databaseIndex;
		this.instanceIndex = instanceIndex;
		if(DEBUG)
			System.out.println("DBKey ctor:"+this.databaseIndex+" "+this.instanceIndex);
	}
	
	public RelatrixIndex getInstanceIndex() {
			return instanceIndex;
	}
	
	public RelatrixIndex getDatabaseIndex() {
		return databaseIndex;
	}
	
	public void setNullKey() {
		this.databaseIndex = nullKey;
		this.instanceIndex = nullKey;
	}
	
	public void setNullKey(Alias alias) throws IOException {
		this.databaseIndex = DatabaseCatalog.getByAlias(alias);
		this.instanceIndex = nullKey;
	}
	
	public static boolean isValid(DBKey key) {
		if(key == null)
			return false;
		return key.databaseIndex != null && !key.databaseIndex.equals(nullKey) && key.instanceIndex != null && !key.instanceIndex.equals(nullKey);
	}
	/**
	 * Returns an expanded diagnostic reason for DBKey being invalid.
	 * Recall the DBKey is composed of a database index and an instance index. 
	 * The database index points to the corresponding entry in the {@link DatabaseCatalog} database catalog. 
	 * The instance index is the key to the instance in the tablespace by {@link PrimaryKeySet}
	 * and {@link KeySet}, which means
	 * it has an entry in the {@link DBKey} class of the database in question and is also the value
	 * in the key/value portion of the class it represents.<p/>
	 * If either of these is null when it is presumed valid, or if the value is equal to the null
	 * key representation when presumed valid, the key is considered invalid.
	 * @param key the {@link DBKey}
	 * @return the string representation of the reason for invalid
	 */
	public static String whyInvalid(DBKey key) {
		if(key == null) 
			return "Key is null";
		if(key.databaseIndex == null) 
			return "Database index is null";
		if(key.databaseIndex.equals(nullKey))
			return "Database index has null key component";
		if(key.instanceIndex == null)
			return "Instance index is null";
		if(key.instanceIndex.equals(nullKey))
			return "Instance index has null key component";
		return "No known reason, should be valid!";
	}
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives an index into the instance table and the index table.
	 * @param indexTable the local or remote interface to facilitate the index creation
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKey(IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		return indexTable.put((Comparable) instance); // the passed key is updated
	}
	
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
	 * @param alias the database alias
	 * @param indexTable the local or remote interface to facilitate the index creation
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKey(Alias alias, IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException, NoSuchElementException {
		return indexTable.put(alias, (Comparable) instance); // the passed key is updated
	}
	
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
	 * @param transactionId transaction id
	 * @param indexTable the local or remote interface to facilitate the index creation
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKey(TransactionId transactionId, IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		return indexTable.put(transactionId, (Comparable) instance); // the passed key is updated
	}
	
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
	 * @param alias the database alias
	 * @param transactionId transaction id
	 * @param indexTable the local or remote interface to facilitate the index creation
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKey(Alias alias, TransactionId transactionId, IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException, NoSuchElementException {
		return indexTable.put(alias, transactionId, (Comparable) instance); // the passed key is updated
	}

	@Override
	public boolean equals(Object o) {
		/*
		synchronized(instanceIndex) {
			if(databaseIndex != null && ((DBKey)o).databaseIndex != null)
				b = databaseIndex.equals(((DBKey)o).databaseIndex);
			if(databaseIndex == null && ((DBKey)o).databaseIndex == null)
				b = true;
			if(instanceIndex != null && ((DBKey)o).instanceIndex != null)
				return b && instanceIndex.equals(((DBKey)o).instanceIndex);
			if(instanceIndex == null && ((DBKey)o).instanceIndex == null)
				return b && true;
			return false;
		}
		*/
		return instanceIndex.equals(((DBKey)o).instanceIndex) && databaseIndex.equals(((DBKey)o).databaseIndex);
		
	}
	
	@Override
	public int hashCode() {
		return instanceIndex.hashCode()+ databaseIndex.hashCode();	
	}
	
	@Override
	public int compareTo(Object o) {
		/*
		synchronized(instanceIndex) {
			int n = 0;
			if(databaseIndex != null && ((DBKey)o).databaseIndex != null) {
				n = databaseIndex.compareTo(((DBKey)o).databaseIndex);
			} else {
				if(databaseIndex == null && ((DBKey)o).databaseIndex == null) {
					throw new RuntimeException("DBKEY DATABASE INDEX BOTH INSTANCES NULL IN COMPARETO");
				}
				if(databaseIndex != null && ((DBKey)o).databaseIndex == null) {
					throw new RuntimeException("DBKEY DATABASE INDEX TARGET INSTANCE NULL IN COMPARETO");
				}
				throw new RuntimeException("DBKEY DATABASE INDEX SOURCE INSTANCE NULL IN COMPARETO");
			}
			if(instanceIndex != null && ((DBKey)o).instanceIndex != null) {
				if(n != 0)
					return n;
				return instanceIndex.compareTo(((DBKey)o).instanceIndex);
			}
			
			if(instanceIndex == null && ((DBKey)o).instanceIndex == null) {
				throw new RuntimeException("DBKEY INSTANCE INDEX BOTH INSTANCES NULL IN COMPARETO");
			}
			if(instanceIndex != null && ((DBKey)o).instanceIndex == null) {
				throw new RuntimeException("DBKEY INSTANCE INDEX TARGET INSTANCE NULL IN COMPARETO");
			}
			throw new RuntimeException("DBKEY INSTANCE INDEX SOURCE INSTANCE NULL IN COMPARETO");
		}
		*/
		int i = instanceIndex.compareTo(((DBKey)o).instanceIndex);
		if(i != 0)
			return i;
		return databaseIndex.compareTo(((DBKey)o).databaseIndex);
	}

	@Override
	public String toString() {
		//synchronized(instanceIndex) {
			return String.format("key:%s %s%n", databaseIndex != null ? databaseIndex.toString() : "NULL" ,instanceIndex != null ? instanceIndex.toString() : "NULL");
		//}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		instanceIndex.writeExternal(out);
		databaseIndex.writeExternal(out);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		instanceIndex = new RelatrixIndex();
		databaseIndex = new RelatrixIndex();
		instanceIndex.readExternal(in);
		databaseIndex.readExternal(in);
	}
	
	public static byte[] longsToBytes(long x, long y) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES*2);
	    buffer.putLong(x);
	    buffer.putLong(y);
	    return buffer.array();
	}


}
