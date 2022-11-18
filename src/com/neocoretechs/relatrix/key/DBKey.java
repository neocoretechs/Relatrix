package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

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
	
	public DBKey() {}
	
	DBKey(UUID instanceIndex) {
		this.instanceIndex = instanceIndex;
	}
	
	protected UUID getInstanceIndex() {
		synchronized(instanceIndex) {
			return instanceIndex;
		}
	}
	
	public boolean isValid() {
		return instanceIndex != null;
	}
	/**
	 * Factory method to construct a new key and enforce the storage of the instance.
	 * The instance then receives and index into the instance table and the index table.
	 * @param instance The actual object instance, may be another DBKey for a relationship.
	 * @return The new DBKey
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DBKey newKey(IndexInstanceTableInterface indexTable, Object instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		DBKey index = indexTable.getNewDBKey();
		indexTable.put(index, (Comparable) instance); // the passed key is updated
		return index;
	}
	
	@Override
	public boolean equals(Object o) {
		synchronized(instanceIndex) {
			return instanceIndex.equals(((DBKey)o).instanceIndex);
		}
	}
	
	@Override
	public int hashCode() {
		synchronized(instanceIndex) {
			return instanceIndex.hashCode();
		}
	}
	
	@Override
	public int compareTo(Object o) {
		synchronized(instanceIndex) {
			return instanceIndex.compareTo(((DBKey)o).instanceIndex);
		}
	}

	@Override
	public String toString() {
		synchronized(instanceIndex) {
			return String.format("%s: key:%s%n", this.getClass().getName(), instanceIndex.toString());
		}
	}

}
