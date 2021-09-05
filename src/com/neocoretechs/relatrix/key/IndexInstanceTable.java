package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.neocoretechs.bigsack.keyvaluepages.KeyValue;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Morphism;
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
	static Object mutex = new Object();
	static LinkedHashSet<Class> classCommits = new LinkedHashSet<Class>();
	static volatile DBKey lastKey;
	static volatile DBKey lastGoodKey;
	static {
		synchronized(mutex) {
			Object lastKeyObject = null;
			try {
				classCommits.add(DBKey.class);
				lastKeyObject = RelatrixKV.lastKey(DBKey.class);
			} catch (IllegalAccessException | IOException e) {
				System.out.printf("<<Cannot establish index for object instance storage, must reconcile tables and directories in %s before continuing", RelatrixKV.getTableSpaceDirectory());
				e.printStackTrace();
				System.exit(1);
			}
		if(lastKeyObject == null)
			lastKeyObject = new DBKey(0);
		lastKey = (DBKey) lastKeyObject;
		lastGoodKey = lastKey;
		}
	}
	
	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		synchronized(mutex) {
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
					lastKey.increment();
					index.setInstanceIndex(lastKey.getInstanceIndex());
					if(DEBUG)
						System.out.printf("%s.put(%s)%n", index.getClass().getName(), index);
					try {
						RelatrixKV.transactionalStore(index, instance);
					} catch(DuplicateKeyException dke) {
						throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered contrary to programmatic logic for instance:%s%n",index,instance));
					}
					try {
						RelatrixKV.transactionalStore(instance, index);
					} catch(DuplicateKeyException dke) {
						throw new IOException(String.format("Instance to DBKey duplicate instance:%s encountered contrary to programmatic logic for key:%s%n",instance,index));
						
					}
					classCommits.add(instance.getClass());
				} else {
					// instance is null, no instance in DBkey, keys are not valid, nothing to put.
					throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index.");
				}
			}
		}
	}
	
	public static void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
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
	}
	
	public static void commit() throws IOException {
		synchronized(mutex) {
			synchronized(classCommits) {
				Iterator<Class> it = classCommits.iterator();
				while(it.hasNext()) {
					Class c = it.next();
					if(DEBUG)
						System.out.printf("IndexInstanceTable.commit committing class %s%n",c);
					RelatrixKV.transactionCommit(c);
				}
				classCommits.clear();
				lastGoodKey = lastKey;
			}
		}
	}
	
	public static void rollback() throws IOException {
		synchronized(mutex) {
			synchronized(classCommits) {
				Iterator<Class> it = classCommits.iterator();
				while(it.hasNext())
					RelatrixKV.transactionRollback(it.next());
				classCommits.clear();
			}
			lastKey = lastGoodKey; // account for increments
		}
	}
	
	public static void checkpoint() throws IllegalAccessException, IOException {
		synchronized(mutex) {
			synchronized(classCommits) {
				Iterator<Class> it = classCommits.iterator();
				while(it.hasNext()) {
					RelatrixKV.transactionCheckpoint(it.next());
				}
			}
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
		synchronized(mutex) {
			Object o = RelatrixKV.get(index);
			if(o == null)
				return null;
			KeyValue kv = (KeyValue)o;
			if(kv.getmValue() instanceof Morphism) {
				Morphism m = ((Morphism)o);
				KeySet ks = m.getKeys();
				getByIndex(ks.getDomainKey());
			}				
			return kv.getmValue();
		}
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
		synchronized(mutex) {
			TransactionalTreeMap ttm = BigSackAdapter.getBigSackTransactionalTreeMap(instance.getClass());
			classCommits.add(instance.getClass());
			Object o = RelatrixKV.get((Comparable) instance);
			if(o == null)
				return null;
			KeyValue kv = (KeyValue)o;
			return (DBKey)kv.getmValue();
		}
	}

}
