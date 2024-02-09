package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
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
	public static String nullKey = "00000000-0000-0000-0000-000000000000";
	transient byte[] dbb = new byte[nullKey.length()];
	
	String instanceIndex = null;
	String databaseIndex = null;
	
	public DBKey() {}
	
	DBKey(UUID databaseIndex, UUID instanceIndex) {
		this.databaseIndex = databaseIndex.toString();
		this.instanceIndex = instanceIndex.toString();
		if(DEBUG)
			System.out.println("DBKey ctor:"+this.databaseIndex+" "+this.instanceIndex);
	}
	
	protected UUID getInstanceIndex() {
		synchronized(instanceIndex) {
			return UUID.fromString(databaseIndex);
		}
	}
	
	protected UUID getDatabaseIndex() {
		synchronized(databaseIndex) {
			return UUID.fromString(databaseIndex);
		}
	}
	
	public void setNullKey() {
		this.databaseIndex = nullKey;
		this.instanceIndex = nullKey;
	}
	
	public void setNullKey(String alias) {
		this.databaseIndex = Relatrix.getByAlias(alias).toString();
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
		StringBuilder sb = new StringBuilder(databaseIndex);
		sb.append(instanceIndex);
		StringBuilder sb2 = new StringBuilder(((DBKey)o).databaseIndex);
		sb2.append(((DBKey)o).instanceIndex);
		return sb.toString().equals(sb2.toString());
		
	}
	
	@Override
	public int hashCode() {
		/*synchronized(instanceIndex) {
		if(instanceIndex == null || databaseIndex == null)
			return 31;
		final int prime = 31;
		int result = 1;
		result = prime * result + databaseIndex.hashCode();
		return prime * result + instanceIndex.hashCode();
		}*/
		StringBuilder sb = new StringBuilder(databaseIndex);
		sb.append(instanceIndex);
		return sb.toString().hashCode();
		
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
		StringBuilder sb = new StringBuilder(databaseIndex);
		sb.append(instanceIndex);
		StringBuilder sb2 = new StringBuilder(((DBKey)o).databaseIndex);
		sb2.append(((DBKey)o).instanceIndex);
		return sb.toString().compareTo(sb2.toString());
	}

	@Override
	public String toString() {
		//synchronized(instanceIndex) {
			return String.format("key:%s %s%n", databaseIndex != null ? databaseIndex.toString() : "NULL" ,instanceIndex != null ? instanceIndex.toString() : "NULL");
		//}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeBytes(databaseIndex);
		out.writeBytes(instanceIndex);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		in.readFully(dbb);
		databaseIndex = new String(dbb);
		in.readFully(dbb);
		instanceIndex = new String(dbb);
	}

}
