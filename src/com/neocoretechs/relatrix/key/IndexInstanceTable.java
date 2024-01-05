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
	String transactionId = null;
	private Object mutex = new Object();
	/**
	 * Set up for non transaction context. 
	 */
	public IndexInstanceTable() {
	}
	
	/**
	 * Set up for transaction context. Ensure elsewhere that lifecycle of this instance is per transaction.
	 * @param xid
	 */
	public IndexInstanceTable(String xid) {
			this.transactionId = xid;
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
				System.out.printf("%s.put Xid:%s index=%s instance=%s%n", index.getClass().getName(), transactionId, index, instance);
			try {
				if(transactionId == null)
					RelatrixKV.store(index, instance);
				else
					RelatrixKVTransaction.store(transactionId, index, instance);
			} catch(DuplicateKeyException dke) {
				dke.printStackTrace();
				throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered for instance:%s. Index class=%s Instance class=%s%n",index,instance,index.getClass().getName(),instance.getClass().getName()));
			}
			try {
				if(transactionId == null)
					RelatrixKV.store(instance, index);
				else
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
				if(transactionId == null)
					RelatrixKV.remove(instance);
				else
					RelatrixKVTransaction.remove(transactionId, instance);
			}
			if(transactionId == null)
				RelatrixKV.remove(index);
			else
				RelatrixKVTransaction.remove(transactionId, index);
		}
	}
	
	@Override
	public void commit() throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(transactionId != null) {
				if(DEBUG)
					System.out.printf("IndexInstanceTable.commit committing "+transactionId);
				RelatrixKVTransaction.commit(transactionId);
			}
		}
	}
	
	@Override
	public void rollback() throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(transactionId != null) {
				if(DEBUG)
					System.out.printf("IndexInstanceTable.rollback "+transactionId);
				RelatrixKVTransaction.rollback(transactionId);
			}
		}
	}
	
	@Override
	public void checkpoint() throws IllegalAccessException, IOException {
		synchronized(mutex) {
			if(transactionId != null)
				RelatrixKVTransaction.checkpoint(transactionId);
		}	
	}
	
	@Override
	public void rollbackToCheckpoint() throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(transactionId != null) {
				if(DEBUG)
					System.out.printf("IndexInstanceTable.rollback "+transactionId);
				RelatrixKVTransaction.rollbackToCheckpoint(transactionId);
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
		//synchronized(mutex) {
		if(transactionId == null)
			return RelatrixKV.get(index);
		else
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
		if(transactionId == null)
			return (DBKey) RelatrixKV.get((Comparable) instance);
		else
			return (DBKey) RelatrixKVTransaction.get(transactionId, (Comparable) instance);
		//}
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		if(transactionId == null)
			return new DBKey(Relatrix.getNewKey());
		else
			return new DBKey(RelatrixTransaction.getNewKey());	
	}
	
	@Override
	public void setTransactionId(String xid) {
		this.transactionId = xid;	
	}

}
