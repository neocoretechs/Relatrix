package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.neocoretechs.bigsack.keyvaluepages.KeyValue;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
/**
 * The IndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. We use the DBKey wrapper class to carry the integer index inside the Morphism.
 * which also adds validation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public final class RemoteIndexInstanceTable implements IndexInstanceTableInterface {
	public static boolean DEBUG = true;
	LinkedHashSet<Class> classCommits = new LinkedHashSet<Class>();
	DBKey lastKey;
	DBKey lastGoodKey;
	private RelatrixClientInterface rc = null;
	
	public RemoteIndexInstanceTable(RelatrixClientInterface rc) throws IOException {
		this.rc = rc;
		Object lastKeyObject = null;
		try {
			classCommits.add(DBKey.class);
			lastKeyObject = lastKey(DBKey.class);
		} catch (IllegalAccessException | IOException e) {
			System.out.printf("<<Cannot establish index for object instance storage, must reconcile tables and directories in %s before continuing", RelatrixKV.getTableSpaceDirectory());
			e.printStackTrace();
			System.exit(1);
		}
		// we cant make our own locally, it would be irrelevant
		if(lastKeyObject == null) {
			throw new RuntimeException("No remote DBKey was delivered upon request for last key from server.");
		}
		lastKey = (DBKey) lastKeyObject;
		if(DEBUG) {
			System.out.printf("lastKey=%slastGoodKey=%s%n", lastKey, lastGoodKey);
		}
	}
	
	@Override
	public Integer getIncrementedLastGoodKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		return rc.getIncrementedLastGoodKey();
		//return ++lastKey.instanceIndex;
	}
	
	
	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
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
				rc.transactionalStore(index, instance);
			} catch(DuplicateKeyException dke) {
					throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered for instance:%s. Existing entry=%s/%s%n",index,instance,((KeyValue)RelatrixKV.get(index)).getmKey(),((KeyValue)RelatrixKV.get(index)).getmValue()));
			}
			try {
				rc.transactionalStore(instance, index);
			} catch(DuplicateKeyException dke) {
					throw new IOException(String.format("Instance to DBKey duplicate instance:%s encountered for key:%s Existing entry=%s/%s%n",instance,index,((KeyValue)RelatrixKV.get(instance)).getmKey(),((KeyValue)RelatrixKV.get(instance)).getmValue()));	
			}
			classCommits.add(index.getClass());
			classCommits.add(instance.getClass());
	}
	
	@Override
	public void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
			Comparable instance = null;
			if(index.isValid() ) {
				// index is valid
				instance = (Comparable) getByIndex(index);
				if(instance != null) {
					rc.remove(instance);
					classCommits.add(instance.getClass());
				}						
			} else {
				// no instance in DBkey, keys are not valid, nothing to delete.
				throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index. index="+index);
			}
			rc.remove(index);
			classCommits.add(index.getClass());	
	}
	
	@Override
	public void commit() throws IOException {
			synchronized(classCommits) {
				Iterator<Class> it = classCommits.iterator();
				while(it.hasNext()) {
					Class c = it.next();
					if(DEBUG)
						System.out.printf("IndexInstanceTable.commit committing class %s%n",c);
					rc.transactionCommit(c);
				}
				classCommits.clear();
			}
	}
	
	@Override
	public void rollback() throws IOException {
			synchronized(classCommits) {
				Iterator<Class> it = classCommits.iterator();
				while(it.hasNext())
					rc.transactionRollback(it.next());
				classCommits.clear();
			}
	}
	
	@Override
	public void checkpoint() throws IllegalAccessException, IOException {
			synchronized(classCommits) {
				Iterator<Class> it = classCommits.iterator();
				while(it.hasNext()) {
					rc.transactionCheckpoint(it.next());
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
	@Override
	public Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
			return ((RelatrixClient)rc).getByIndex(index);
	}
	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The Integer index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
			return (DBKey)rc.get((Comparable) instance);
	}

	@Override
	public Object lastKey(Class<DBKey> class1) throws IllegalAccessException, IOException {		
		try {
			return rc.lastKey(DBKey.class);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
