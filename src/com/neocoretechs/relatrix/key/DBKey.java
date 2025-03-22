package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.TransactionId;



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
	private long msb;
	private long lsb;

	public static DBKey nullDBKey = new DBKey(0L,0L);
	public static DBKey fullDBKey = new DBKey(0xFFFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFFFL);
	
	public DBKey() {}
	
	public DBKey(UUID uuid) {
		this.msb = uuid.getMostSignificantBits();
		this.lsb = uuid.getLeastSignificantBits();
		if(DEBUG)
			System.out.println("DBKey ctor:"+this.msb+","+this.lsb);
	}
	
	public DBKey(long msb, long lsb) {
		this.msb = msb;
		this.lsb = lsb;
	}
	
	public void setNullKey() {
		msb = 0L;
		lsb = 0L;
	}
	
	/**
	 * Checks the validity of a given {@link DBKey} for not null and not equal nullDBKey
	 * @param key the DBKey in question
	 * @return true if key is valid, i.e, is not null and does not equal nullDBKey
	 */
	public static boolean isValid(DBKey key) {
		if(key == null)
			return false;
		return !key.equals(nullDBKey);
	}
	
	/**
	 * Returns an expanded diagnostic reason for DBKey being invalid.
	 * Recall the DBKey is composed of a database index and an instance index. 
	 * The instance index is the key to the instance in the tablespace by {@link PrimaryKeySet}
	 * and {@link KeySet}, which means
	 * it has an entry in the {@link DBKey} class of the database in question and is also the value
	 * in the key/value portion of the class it represents.<p/>
	 * If null when it is presumed valid, or if the value is equal to the null
	 * key representation when presumed valid, the key is considered invalid.
	 * @param key the {@link DBKey}
	 * @return the string representation of the reason for invalid
	 */
	public static String whyInvalid(DBKey key) {
		if(key == null) 
			return "Key is null";
		if(key.equals(nullDBKey))
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
	/**
	 * @return the msb
	 */
	public long getMsb() {
		return msb;
	}

	/**
	 * @param msb the msb to set
	 */
	public void setMsb(long msb) {
		this.msb = msb;
	}

	/**
	 * @return the lsb
	 */
	public long getLsb() {
		return lsb;
	}

	/**
	 * @param lsb the lsb to set
	 */
	public void setLsb(long lsb) {
		this.lsb = lsb;
	}

	public UUID getAsUUID() {
		return new UUID(msb, lsb);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (lsb ^ (lsb >>> 32));
		result = prime * result + (int) (msb ^ (msb >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBKey other = (DBKey) obj;
		if (lsb != other.lsb)
			return false;
		if (msb != other.msb)
			return false;
		return true;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(msb);
		out.writeLong(lsb);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		msb = in.readLong();
		lsb = in.readLong();
	}

	@Override
	public int compareTo(Object o) {
		int i = Long.compareUnsigned(msb, ((DBKey)o).msb);
		if(i != 0)
			return i;
		return Long.compareUnsigned(lsb, ((DBKey)o).lsb);
	}
	
	@Override
	public Object clone() {
		return new DBKey(msb,lsb);
	}
	
	@Override
	public String toString() {
		return String.format("[0x%16X,0x%16X]", msb,lsb);
	}

	public byte[] toBytes() {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES*2);
	    buffer.putLong(msb);
	    buffer.putLong(lsb);
	    return buffer.array();
	}
	
	public static byte[] longsToBytes(long x, long y) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES*2);
	    buffer.putLong(x);
	    buffer.putLong(y);
	    return buffer.array();
	}


}
