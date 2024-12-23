package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.TransactionId;

/**
 * The RemoteIndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. We use the DBKey wrapper class to carry the integer index inside the Morphism.
 * which also adds validation. This class carries the client interface instances that allow over the wire
 * communication to remote tables.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public final class RemoteIndexInstanceTable implements IndexInstanceTableInterface {
	public static boolean DEBUG = false;
	private RelatrixClientInterface rc = null;
	private RelatrixClientTransactionInterface rcx = null;
	private Object mutex = new Object();

	public RemoteIndexInstanceTable(RelatrixClientInterface rc) throws IOException {
		this.rc = rc;
	}	

	public RemoteIndexInstanceTable(RelatrixClientTransactionInterface rc) throws IOException {
		this.rcx = rc;
	}	
	/**
	 * Put the key to the proper tables
	 * @param instance the Comparable instance payload
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey put(Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// instance index not valid, key not fully formed, we may have to add instance value to table and index it
		DBKey retKey = getKey(instance);
		if(retKey == null) {
			DBKey index = getNewDBKey();
			try {
				rc.storekv(index, instance);
				rc.storekv(instance, index);
			} catch (IOException e) {
				throw new IOException(e);
			}
			return index;
		}
		return retKey;
	}

	/**
	 * Put the key to the proper tables
	 * @param instance the Comparable instance payload
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey put(TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// instance index not valid, key not fully formed, we may have to add instance value to table and index it
		DBKey retKey = getKey(transactionId, instance);
		if(retKey == null) {
			DBKey index = getNewDBKey();
			rcx.storekv(transactionId, index, instance);
			rcx.storekv(transactionId,  instance, index);
			return index;
		}
		return retKey;
	}
	@Override
	public DBKey put(Alias alias, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
		DBKey retKey = getKey(alias, instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rc.storekv(alias, index, instance);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rc.storekv(alias, instance, index);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return retKey;		
	}

	@Override
	public DBKey put(Alias alias, TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
		DBKey retKey = getKey(alias, transactionId, instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rcx.storekv(alias, transactionId, index, instance);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rcx.storekv(alias, transactionId, instance, index);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return index;
		}
		return retKey;		
	}

	@Override
	public void put(DBKey dbKey, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rc.storekv(dbKey, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rc.storekv(instance, dbKey);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(Alias alias, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rc.storekv(alias, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rc.storekv(alias, instance, index);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void put(TransactionId transactionId, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rcx.storekv(transactionId, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rcx.storekv(transactionId, instance, index);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(Alias alias, TransactionId transactionId, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias Alias:%s Xid:%s class=%s instance=%s%n", this.getClass().getName(), alias, transactionId, instance.getClass().getName(), instance);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rcx.storekv(alias, transactionId, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					rcx.storekv(alias, transactionId, instance, index);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Comparable instance = null;
		instance = (Comparable) get(index);
		if(instance != null) {
			rc.remove(instance);
		}
		rc.remove(index);
	}

	@Override
	public void delete(TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Comparable instance = null;
		instance = (Comparable) get(index);
		if(instance != null) {
			rcx.remove(transactionId, instance);
		}
		rcx.remove(transactionId, index);
	}

	@Override
	public void commit(TransactionId transactionId) throws IOException, IllegalAccessException {
		rcx.commit(transactionId);
	}

	@Override
	public void rollback(TransactionId transactionId) throws IOException, IllegalAccessException {
		rcx.rollback(transactionId);
	}

	@Override
	public void checkpoint(TransactionId transactionId) throws IllegalAccessException, IOException {
		rcx.checkpoint(transactionId);
	}

	public static synchronized DBKey getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID uuid = UUID.randomUUID();
		DBKey nkey = new DBKey(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
		if(DEBUG)
			System.out.printf("Returning NewKey=%s%n", nkey.toString());
		return nkey;
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
	public Object get(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		Object o = rc.getByIndex(index);
		if(DEBUG)
			System.out.printf("%s getByIndex for key:%s returning:%s%n", this.getClass().getName(), index, o);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet)
			((PrimaryKeySet)o).setIdentity(index);
		return o;
	}
	/**
	 * Get the instance by using the InstanceIndex contained in the passed DBKey from the alias database
	 * @param alias
	 * @param index
	 * @return the instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object get(Alias alias, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		Object o = rc.getByIndex(alias,index);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setAlias(alias);
		}
		return o;
	}
	/**
	 * Get the instance by using the InstanceIndex contained in the passed DBKey from the alias database under transaction control
	 * @param alias
	 * @param transactionId
	 * @param index
	 * @return the instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object get(Alias alias, TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		Object o = rcx.getByIndex(alias, transactionId, index);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setAlias(alias);
			((PrimaryKeySet)o).setTransactionId(transactionId);
		}
		return o;
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
	public Object get(TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		Object o = rcx.getByIndex(transactionId, index);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setTransactionId(transactionId);
		}
		return o;
	}
	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed object
	 * @param instance the DBKey containing the instance
	 * @return The Integer index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey getKey(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		return (DBKey)rc.get((Comparable) instance);
	}

	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed object
	 * @param instance the DBKey containing the instance
	 * @return The Integer index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey getKey(TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		return (DBKey)rcx.get(transactionId, (Comparable) instance);
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		return getNewKey();
	}

	@Override
	public void rollbackToCheckpoint(TransactionId transactionId) throws IOException, IllegalAccessException {
		rcx.rollbackToCheckpoint(transactionId);	
	}


	@Override
	public void commit(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.commitAlias committing alias:"+alias+" Xid:"+transactionId);
		rcx.commit(alias, transactionId);
	}

	@Override
	public void rollback(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.rollback alias:"+alias+" Xid:"+transactionId);
		rcx.rollback(alias, transactionId);
	}

	@Override
	public void checkpoint(Alias alias, TransactionId transactionId) throws IllegalAccessException, IOException, NoSuchElementException {
		rcx.checkpoint(alias, transactionId);
	}

	@Override
	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.rollbackToCheckpoint alias:"+alias+" Xid:"+transactionId);
		rcx.rollbackToCheckpoint(alias, transactionId);
	}

	@Override
	public DBKey getKey(Alias alias, Object instance) throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException {
		return (DBKey) rc.get(alias, (Comparable) instance);
	}

	@Override
	public DBKey getKey(Alias alias, TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		return (DBKey) rcx.get(alias, transactionId, (Comparable) instance);
	}

	@Override
	public void deleteInstance(Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = rc.removekv(instance);
		if(index != null) {
			rc.remove((Comparable<?>) index);
		}
	}

	@Override
	public void deleteInstance(TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = rcx.removekv(transactionId, instance);
		if(index != null) {
			rcx.remove(transactionId, (Comparable) index);
		}	
	}

	@Override
	public void delete(Alias alias, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Object instance = rc.removekv(index);
		// index is valid
		if(instance != null) {
			rc.remove((Comparable) instance);
		}	
	}

	@Override
	public void delete(Alias alias, TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Object instance = rcx.removekv(alias, transactionId, index);
		// index is valid
		if(instance != null) {
			rcx.remove(transactionId, (Comparable) instance);
		}
	}

	@Override
	public void deleteInstance(Alias alias, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = rc.removekv(alias, instance);
		if(index != null) {
			rc.remove(alias, (Comparable<?>) index);
		}	
	}

	@Override
	public void deleteInstance(Alias alias, TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = rcx.removekv(alias, transactionId, instance);
		if(index != null) {
			rcx.remove(alias, transactionId, (Comparable) index);
		}
	}

	@Override
	public void remove(DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException {
		deleteInstance(skeyd);
		delete(dKey);
	}

	@Override
	public void remove(Alias alias, DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException {
		deleteInstance(alias, skeyd);
		delete(alias, dKey);
	}

	@Override
	public void remove(TransactionId transactionId, DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException {
		deleteInstance(transactionId, skeyd);
		delete(transactionId, dKey);
	}

	@Override
	public void remove(Alias alias, TransactionId transactionId, DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException {
		deleteInstance(alias, transactionId, skeyd);
		delete(alias, transactionId, dKey);
	}
}
