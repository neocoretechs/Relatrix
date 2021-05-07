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
	private transient Comparable instance = null; // not stored, but retrieved dynamically from instance table
	private InstanceIndex instanceIndex;
	
	private DBKey() {}
	
	protected InstanceIndex getInstanceIndex() {
		return instanceIndex;
	}
	
	public Object getInstance() throws IllegalAccessException, ClassNotFoundException, IOException {
		if(instance != null)
			return instance;
		if(instanceIndex.isValid()) {
			instance = (Comparable) IndexInstanceTable.getByIndex(this);
		}
		return null;
	}

	private static InstanceIndex newInstanceIndex(Integer index) {
		return new InstanceIndex(index);
	}
	
	protected void setInstanceIndex(Integer index) {
		instanceIndex = new InstanceIndex(index);
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
		DBKey dbKey = new DBKey();
		dbKey.instance = (Comparable) instance;
		Integer index = IndexInstanceTable.getByInstance(dbKey);
		if(index == null)
			try {
				IndexInstanceTable.put(dbKey); // the passed key is updated
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		dbKey.instanceIndex = newInstanceIndex(index);
		return dbKey;	
	}
	
	@Override
	public boolean equals(Object o) {
		if(!instanceIndex.isValid())
			try {
				IndexInstanceTable.put(this);
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		if(!((DBKey)o).instanceIndex.isValid())
			try {
				IndexInstanceTable.put((DBKey) o);
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}		
     	// now see if the classes are compatible for comparison
    	Class toClass = ((DBKey)o).instance.getClass();
    	if( !instance.getClass().equals(toClass) && !toClass.isAssignableFrom(instance.getClass())) {
    		throw new RuntimeException("Classes are incompatible and the schema would be violated for "+o+" and "+instance+
    				" whose classes are "+instance.getClass().getName()+" and "+((DBKey)o).instance.getClass().getName());
    	}
    	// Otherwise, use the standard compareTo for all objects which invokes our indicies
    	try {
			return instance.equals(((DBKey)o).getInstance());
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int hashCode() {
		if(!instanceIndex.isValid())
			try {
				IndexInstanceTable.put(this);
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		return instance.hashCode();
	}
	
	@Override
	public int compareTo(Object o) {
		if(!instanceIndex.isValid())
			try {
				IndexInstanceTable.put(this);
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		if(!((DBKey)o).instanceIndex.isValid())
			try {
				IndexInstanceTable.put((DBKey) o);
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}		
     	// now see if the classes are compatible for comparison, 
    	Class toClass = ((DBKey)o).instance.getClass();
    	if( !instance.getClass().equals(toClass) && !toClass.isAssignableFrom(instance.getClass())) {
    		//compare a universal string representation as a unifying datatype for typed class templates
    		//return from.toString().compareTo(to.toString());
    		throw new RuntimeException("Classes are incompatible and the schema would be violated for "+o+" and "+instance+
    				" whose classes are "+instance.getClass().getName()+" and "+((DBKey)o).instance.getClass().getName());
    	}
    	// Otherwise, use the standard compareTo for all objects which invokes our indicies
    	try {
			return instance.compareTo(((DBKey)o).getInstance());
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s instance:%s, index:%s", this.getClass().getName(), instance, instanceIndex);
	}

}
