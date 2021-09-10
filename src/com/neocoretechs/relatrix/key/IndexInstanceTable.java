package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.neocoretechs.bigsack.keyvaluepages.KeyValue;
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
	static Object mutex = new Object();
	static LinkedHashSet<Class> classCommits = new LinkedHashSet<Class>();
	
	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put index=%s instance=%s valid=%s%n", index.getClass().getName(), index, instance, String.valueOf(index.isValid()));
			if(index.isValid() ) {
				// index is valid
				if(instance == null) // but instance is null
					throw new IllegalAccessException("DBKey is in an invalid state: valid index but strangeley, no valid instance it refers to.");			
			} else {
				// instance index not valid, key not fully formed, we may have to add instance value to table and index it
				if(instance == null) {
					// instance is null, no instance in DBkey, keys are not valid, nothing to put.
					throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index.");
				}
			}
			try {
				RelatrixKV.transactionalStore(index, instance);
			} catch(DuplicateKeyException dke) {
					throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered for instance:%s. Existing entry=%s/%s%n",index,instance,((KeyValue)RelatrixKV.get(index)).getmKey(),((KeyValue)RelatrixKV.get(index)).getmValue()));
			}
			try {
				RelatrixKV.transactionalStore(instance, index);
			} catch(DuplicateKeyException dke) {
					throw new IOException(String.format("Instance to DBKey duplicate instance:%s encountered for key:%s Existing entry=%s/%s%n",instance,index,((KeyValue)RelatrixKV.get(instance)).getmKey(),((KeyValue)RelatrixKV.get(instance)).getmValue()));	
			}
			classCommits.add(index.getClass());
			classCommits.add(instance.getClass());
		}
	}
	
	public static void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			Comparable instance = null;
			if(index.isValid() ) {
				// index is valid
				instance = (Comparable) getByIndex(index);
				if(instance != null) {
					RelatrixKV.remove(instance);
					classCommits.add(instance.getClass());
				}						
			} else {
				// no instance in DBkey, keys are not valid, nothing to delete.
				throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index. index="+index);
			}
			RelatrixKV.remove(index);
			classCommits.add(index.getClass());	
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
				DBKey.setLastGoodKey();
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
			DBKey.setLastKey(); // account for increments
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
			classCommits.add(instance.getClass());
			Object o = RelatrixKV.get((Comparable) instance);
			if(DEBUG)
				System.out.printf("IndexInstanceTable.getByInstance %s, result=%s%n",instance,o);
			if(o == null)
				return null;
			KeyValue kv = (KeyValue)o;
			return (DBKey)kv.getmValue();
		}
	}

}
