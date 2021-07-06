package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class fronts the actual instances in the Relatrix relations so as to normalize those actual instances.<p/>
 * Since our relations are composed of multiple indexes of otherwise redundant data, we need to have a means of
 * reducing the overhead at the cost of computational cycles. Minor redundancy is incurred due to the method
 * of storage of 2 tables indexed by [integer index, instance] and [instance, integer index] so we can do
 * lookups by index or instance. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public final class DBKey implements Comparable, Serializable {
	private static final long serialVersionUID = -7511519913473997228L;
	private static boolean DEBUG = false;
	private Integer instanceIndex = new Integer(-1);
	
	public DBKey() {}
	
	public DBKey(int index) {
		instanceIndex = new Integer(index);
	}
	
	protected Integer getInstanceIndex() {
		synchronized(instanceIndex) {
			return instanceIndex;
		}
	}
	
	protected void setInstanceIndex(Integer index) {
		synchronized(instanceIndex) {
			instanceIndex = new Integer(index);
		}
	}
	
	public boolean isValid() {
		synchronized(instanceIndex) {
			return instanceIndex == -1 ? false : true;
		}
	}
	
	public void increment() {
		synchronized(instanceIndex) {
			++instanceIndex;
		}
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
	public static DBKey newKey(Object instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		DBKey index = new DBKey();
		try {
			IndexInstanceTable.put(index, (Comparable) instance); // the passed key is updated
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
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
			return String.format("%s: key:%d%n", this.getClass().getName(), instanceIndex);
		}
	}

}
