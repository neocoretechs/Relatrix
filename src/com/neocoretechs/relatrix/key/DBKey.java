package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;

/**
 * Class fronts the actual instances in the Relatrix relations so as to normalize those actual instances.<p/>
 * Since our relations are composed of multiple indexes of otherwise redundant data, we need to have a means of
 * reducing the overhead at the cost of computational cycles. Minor redundancy is incurred due to the method
 * of storage of 2 tables indexed by [index, instance] and [instance, index] so we can do
 * lookups by index or instance. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public final class DBKey implements Comparable, Serializable {
	private static final long serialVersionUID = -7511519913473997228L;
	private static boolean DEBUG = false;
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
		synchronized(instanceIndex) {
			return instanceIndex;
		}
	}
	
	protected UUID getDatabaseIndex() {
		synchronized(databaseIndex) {
			return databaseIndex;
		}
	}
	
	public static boolean isValid(DBKey key) {
		if(key == null)
			return false;
		return key.databaseIndex != null && key.instanceIndex != null;
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
		boolean b = false;
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
		b = databaseIndex.equals(((DBKey)o).databaseIndex);
		if(!b)
			return b;
		return instanceIndex.equals(((DBKey)o).instanceIndex);
		
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
		return databaseIndex.hashCode()+instanceIndex.hashCode();
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
		int n = databaseIndex.compareTo(((DBKey)o).databaseIndex);
		if(n != 0)
			return n;
		return instanceIndex.compareTo(((DBKey)o).instanceIndex);
	}

	@Override
	public String toString() {
		//synchronized(instanceIndex) {
			return String.format("key:%s %s%n", databaseIndex != null ? databaseIndex.toString() : "NULL" ,instanceIndex != null ? instanceIndex.toString() : "NULL");
		//}
	}

}
