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
	public static UUID nullKey = new UUID(0L, 0L);
	
	UUID instanceIndex = null;
	UUID databaseIndex = null;
	
	public DBKey() {}
	
	DBKey(UUID databaseIndex, UUID instanceIndex) {
		this.databaseIndex = databaseIndex;
		this.instanceIndex = instanceIndex;
		if(DEBUG)
			System.out.println("DBKey ctor:"+this.databaseIndex+" "+this.instanceIndex);
	}
	
	protected UUID getInstanceIndex() {
			return instanceIndex;
	}
	
	protected UUID getDatabaseIndex() {
		return databaseIndex;
	}
	
	public void setNullKey() {
		this.databaseIndex = nullKey;
		this.instanceIndex = nullKey;
	}
	
	public void setNullKey(String alias) {
		this.databaseIndex = Relatrix.getByAlias(alias);
		this.instanceIndex = nullKey;
	}
	
	public static boolean isValid(DBKey key) {
		if(key == null)
			return false;
		return key.databaseIndex != null && !key.databaseIndex.equals(nullKey) && key.instanceIndex != null && !key.instanceIndex.equals(nullKey);
	}
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
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
	public static DBKey newKeyAlias(String alias, IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException, NoSuchElementException {
		return indexTable.putAlias(alias, (Comparable) instance); // the passed key is updated
	}
	
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
	 * @param xid transaction id
	 * @param indexTable the local or remote interface to facilitate the index creation
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKey(String xid, IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		return indexTable.put(xid, (Comparable) instance); // the passed key is updated
	}
	
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @param indexTable the local or remote interface to facilitate the index creation
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKeyAlias(String alias, String xid, IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException, NoSuchElementException {
		return indexTable.putAlias(alias, xid, (Comparable) instance); // the passed key is updated
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
		return databaseIndex.equals(((DBKey)o).databaseIndex) && instanceIndex.equals(((DBKey)o).instanceIndex);
		
	}
	
	@Override
	public int hashCode() {
		if(instanceIndex == null || databaseIndex == null)
			return 31;
		final int prime = 31;
		int result = 1;
		result = prime * result + databaseIndex.hashCode();
		return prime * result + instanceIndex.hashCode();	
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
		int i = databaseIndex.compareTo(((DBKey)o).databaseIndex);
		if(i != 0)
			return i;
		return instanceIndex.compareTo(((DBKey)o).instanceIndex);
	}

	@Override
	public String toString() {
		//synchronized(instanceIndex) {
			return String.format("key:%s %s%n", databaseIndex != null ? databaseIndex.toString() : "NULL" ,instanceIndex != null ? instanceIndex.toString() : "NULL");
		//}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(databaseIndex.getMostSignificantBits());
		out.writeLong(databaseIndex.getLeastSignificantBits());
		out.writeLong(instanceIndex.getMostSignificantBits());
		out.writeLong(instanceIndex.getLeastSignificantBits());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		Long msbDb = in.readLong();
		Long lsbDb = in.readLong();
		Long msbIn = in.readLong();
		Long lsbIn = in.readLong();
		databaseIndex = new UUID(msbDb, lsbDb);
		instanceIndex = new UUID(msbIn, lsbIn);
	}
	
	public byte[] longsToBytes(long x, long y) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES*2);
	    buffer.putLong(x);
	    buffer.putLong(y);
	    return buffer.array();
	}
}
