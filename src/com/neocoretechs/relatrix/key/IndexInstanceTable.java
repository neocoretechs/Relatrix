package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
/**
 * The IndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. Since we cant subclass Integer, we use the IntegerIndex wrapper class
 * which also adds validation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public final class IndexInstanceTable {
	public static boolean DEBUG = true;
	static LinkedHashSet<Class> classCommits = new LinkedHashSet<Class>();
	static InstanceIndex lastKey = new InstanceIndex();
	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void put(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(index.getInstanceIndex().isValid() ) {
			// index is valid
			if(index.getInstance() == null) // but instance is null
				throw new IllegalAccessException("DBKey is in an invalid state: valid index but strangeley, no valid instance it refers to.");			
		} else {
			// instance index not valid, key not fully formed, we may have to add instance value to table and index it
			if(index.getInstance() != null) {
				// index not valid, instance present in key, try to retrieve index by instance value
				Integer i = getByInstance(index);
				// index by instance valid, instance value present in table, index valid, key fully formed
				if( i != null) {
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
			Object lastKeyObject = RelatrixKV.lastKey(InstanceIndex.class);
			if(lastKeyObject == null)
				lastKeyObject = new InstanceIndex(0);
			InstanceIndex lastKey = (InstanceIndex) lastKeyObject;
		}
		index.setInstanceIndex(lastKey.getIndex() + 1);
		if(DEBUG)
			System.out.printf("%s.put(%s)%n", index.getClass().getName(), index);
		try {
			RelatrixKV.transactionalStore(index.getInstanceIndex(), index.getInstance());
			RelatrixKV.transactionalStore((Comparable)index.getInstance(), index.getInstanceIndex());
		} catch(DuplicateKeyException dke) {
			System.out.println("Duplicate key encountered contrary to programmatic logic "+index);
		}
		classCommits.add(index.getInstance().getClass());
	}
	public static void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if(index.getInstanceIndex().isValid() ) {
			// index is valid
			if(index.getInstance() == null) // but instance is null
				throw new IllegalAccessException("DBKey is in an invalid state: valid index but strangeley, no valid instance it refers to.");				
		} else {
			// instance index not valid, key not fully formed, 
			if(index.getInstance() != null) {
				// index not valid, instance present in key, try to retrieve index by instance value
				Integer i = getByInstance(index);
				// index by instance valid, instance value present in table, index valid, key fully formed
				if( i != null) {
					index.setInstanceIndex(i);
				}
				// instance index not valid, object instance present in DBkey, but instance is not indexed in table.
			} else {
				// no instance in DBkey, keys are not valid, nothing to delete.
				throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index.");
			}
		}
		RelatrixKV.remove(index.getInstanceIndex());
		RelatrixKV.remove((Comparable)index.getInstance());
		classCommits.add(index.getInstance().getClass());
	}
	public static void commit() throws IOException {
		RelatrixKV.transactionCommit(InstanceIndex.class);
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
		RelatrixKV.transactionRollback(InstanceIndex.class);
		synchronized(classCommits) {
			Iterator<Class> it = classCommits.iterator();
			while(it.hasNext())
				RelatrixKV.transactionRollback(it.next());
			classCommits.clear();
		}
	}
	
	public static void checkpoint() throws IllegalAccessException, IOException {
		RelatrixKV.transactionCheckpoint(InstanceIndex.class);
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
		return RelatrixKV.get(index.getInstanceIndex());
	}
	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed DBKey index
	 * @param index the DbKey containing the instance
	 * @return The Integer index cntained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Integer getByInstance(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		Object o = RelatrixKV.get((Comparable)index.getInstance());
		if(o == null)
			return null;
		return ((InstanceIndex)o).getIndex();
	}

}
