package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
/**
 * The IndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. We use the DBKey wrapper class to carry the integer index inside the Morphism.
 * which also adds validation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public final class IndexInstanceTable {
	public static boolean DEBUG = false;
	static LinkedHashSet<Class> classCommits = new LinkedHashSet<Class>();
	static DBKey lastKey = new DBKey(-1);
	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(index.isValid() ) {
			// index is valid
			if(instance == null) // but instance is null
				throw new IllegalAccessException("DBKey is in an invalid state: valid index but strangeley, no valid instance it refers to.");			
		} else {
			// instance index not valid, key not fully formed, we may have to add instance value to table and index it
			if(instance != null) {
				// index not valid, instance present in key, try to retrieve index by instance value
				DBKey i = getByInstance(instance);
				// index by instance valid, instance value present in table, index valid, key fully formed
				if( i != null) {
					index.setInstanceIndex(i.getInstanceIndex());
					return;
				}
				// instance index not valid, object instance present in DBkey, but instance is not indexed in table.
			} else {
				// no instance in DBkey, keys are not valid, nothing to put.
				throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index.");
			}
		}
		// instance index not valid, instance is null or index to instance not in table	
		if(!lastKey.isValid()) {
			Object lastKeyObject = RelatrixKV.lastKey(DBKey.class);
			if(lastKeyObject == null)
				lastKeyObject = new DBKey(0);
			lastKey = (DBKey) lastKeyObject;
		}
		lastKey.increment();
		index.setInstanceIndex(lastKey.getInstanceIndex());
		if(DEBUG)
			System.out.printf("%s.put(%s)%n", index.getClass().getName(), index);
		try {
			RelatrixKV.transactionalStore(index, instance);
			RelatrixKV.transactionalStore(instance, index);
		} catch(DuplicateKeyException dke) {
			System.out.println("Duplicate key encountered contrary to programmatic logic "+index);
		}
		classCommits.add(instance.getClass());
	}
	public static void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Comparable instance = null;
		if(index.isValid() ) {
			// index is valid
			instance = (Comparable) getByIndex(index);
			if(instance == null) // but instance is null
				throw new IllegalAccessException("DBKey is in an invalid state: valid index but strangeley, no valid instance it refers to.");				
		} else {
			// no instance in DBkey, keys are not valid, nothing to delete.
			throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index.");
		}
		RelatrixKV.remove(index);
		RelatrixKV.remove(instance);
		classCommits.add(instance.getClass());
	}
	public static void commit() throws IOException {
		RelatrixKV.transactionCommit(DBKey.class);
		synchronized(classCommits) {
			Iterator<Class> it = classCommits.iterator();
			while(it.hasNext()) {
				Class c = it.next();
				if(DEBUG)
					System.out.printf("IndexInstanceTable.commit committing class %s%n",c);
				RelatrixKV.transactionCommit(c);
			}
			classCommits.clear();
		}
	}
	public static void rollback() throws IOException {
		RelatrixKV.transactionRollback(DBKey.class);
		synchronized(classCommits) {
			Iterator<Class> it = classCommits.iterator();
			while(it.hasNext())
				RelatrixKV.transactionRollback(it.next());
			classCommits.clear();
		}
	}
	
	public static void checkpoint() throws IllegalAccessException, IOException {
		RelatrixKV.transactionCheckpoint(DBKey.class);
		synchronized(classCommits) {
			Iterator<Class> it = classCommits.iterator();
			while(it.hasNext())
				RelatrixKV.transactionCheckpoint(it.next());
		}
		
	}
	/**
	 * Get the instance by using the InstanceIndex contained in the passed DBKey
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		return RelatrixKV.get(index);
	}
	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The Integer index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		Object o = RelatrixKV.get((Comparable)instance);
		if(o == null)
			return null;
		return (DBKey)o;
	}

}
