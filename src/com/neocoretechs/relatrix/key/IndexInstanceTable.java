package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
/**
 * The IndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. We use the DBKey wrapper class to carry the integer index inside the Morphism.
 * which also adds validation. A constructor carrying a transaction Id sets up methods for calls to the
 * transaction oriented classes.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public final class IndexInstanceTable implements IndexInstanceTableInterface {
	public static boolean DEBUG = false;
	private Object mutex = new Object();
	/**
	 * Set up for non transaction context. 
	 */
	public IndexInstanceTable() {
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
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put index=%s instance=%s%n", index.getClass().getName(), index, instance);
			try {
					RelatrixKV.store(index, instance);
			} catch(DuplicateKeyException dke) {
				dke.printStackTrace();
				throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered for instance:%s. Index class=%s Instance class=%s%n",index,instance,index.getClass().getName(),instance.getClass().getName()));
			}
			try {
					RelatrixKV.store(instance, index);
			} catch(DuplicateKeyException dke) {
				dke.printStackTrace();
				throw new IOException(String.format("Instance to DBKey duplicate instance:%s encountered for key:%s Instance class=%s Index class=%s%n",instance,index,instance.getClass().getName(),index.getClass().getName()));	
			}

		}
	}
	
	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void put(String transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put Xid:%s index=%s instance=%s%n", index.getClass().getName(), transactionId, index, instance);
			try {
					RelatrixKVTransaction.store(transactionId, index, instance);
			} catch(DuplicateKeyException dke) {
				dke.printStackTrace();
				throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered for instance:%s. Index class=%s Instance class=%s%n",index,instance,index.getClass().getName(),instance.getClass().getName()));
			}
			try {
					RelatrixKVTransaction.store(transactionId, instance, index);
			} catch(DuplicateKeyException dke) {
				dke.printStackTrace();
				throw new IOException(String.format("Instance to DBKey duplicate instance:%s encountered for key:%s Instance class=%s Index class=%s%n",instance,index,instance.getClass().getName(),index.getClass().getName()));	
			}

		}
	}
	
	@Override
	public void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			Comparable instance = null;
			// index is valid
			instance = (Comparable) getByIndex(index);
			if(instance != null) {
				RelatrixKV.remove(instance);
			}
			RelatrixKV.remove(index);
		}
	}
	
	@Override
	public void delete(String transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			Comparable instance = null;
			// index is valid
			instance = (Comparable) getByIndex(index);
			if(instance != null) {
				RelatrixKVTransaction.remove(transactionId, instance);
			}
			RelatrixKVTransaction.remove(transactionId, index);
		}
	}
	
	@Override
	public void commit(String transactionId) throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.commit committing "+transactionId);
			RelatrixKVTransaction.commit(transactionId);
		}
	}
	
	@Override
	public void rollback(String transactionId) throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollback "+transactionId);
			RelatrixKVTransaction.rollback(transactionId);
		}
	}
	
	@Override
	public void checkpoint(String transactionId) throws IllegalAccessException, IOException {
		synchronized(mutex) {
			RelatrixKVTransaction.checkpoint(transactionId);
		}
	}
	
	@Override
	public void rollbackToCheckpoint(String transactionId) throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollback "+transactionId);
			RelatrixKVTransaction.rollbackToCheckpoint(transactionId);
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
		//synchronized(mutex) {
			return RelatrixKV.get(index);
		//}
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
	public Object getByIndex(String transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
			return RelatrixKVTransaction.get(transactionId, index);
		//}
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
		//synchronized(mutex) {
			return (DBKey) RelatrixKV.get((Comparable) instance);
		//}
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
	public DBKey getByInstance(String transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
			return (DBKey) RelatrixKVTransaction.get(transactionId, (Comparable) instance);
		//}
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
			return new DBKey(Relatrix.getNewKey(), Relatrix.getNewKey());
	}
	
	@Override
	public DBKey getNewDBKeyTransaction() throws ClassNotFoundException, IllegalAccessException, IOException {
			return new DBKey(RelatrixTransaction.getNewKey(), RelatrixTransaction.getNewKey());	
	}


}
