package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;

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
			DBKey retKey = getByInstance(instance);
			if(instance == null) {
				DBKey index = getNewDBKey();
				try {
					rc.storekv(index, instance);
					rc.storekv(instance, index);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
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
	public DBKey put(String transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
				System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
			// instance index not valid, key not fully formed, we may have to add instance value to table and index it
		DBKey retKey = getByInstance(transactionId, instance);
		if(instance == null) {
			DBKey index = getNewDBKey();
			try {
				rcx.store(transactionId, index, instance);
				rcx.store(transactionId,  instance, index);
				return index;
			} catch(DuplicateKeyException dke) {
				throw new IOException(dke);
			}
		}
		return retKey;
	}
	
	@Override
	public void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Comparable instance = null;
		instance = (Comparable) getByIndex(index);
		if(instance != null) {
				rc.remove(instance);
		}
		rc.remove(index);
	}
	
	@Override
	public void delete(String transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Comparable instance = null;
		instance = (Comparable) getByIndex(index);
		if(instance != null) {
				rcx.remove(transactionId, instance);
		}
		rcx.remove(transactionId, index);
	}
	
	@Override
	public void commit(String transactionId) throws IOException, IllegalAccessException {
		rcx.commit(transactionId);
	}
	
	@Override
	public void rollback(String transactionId) throws IOException, IllegalAccessException {
		rcx.rollback(transactionId);
	}
	
	@Override
	public void checkpoint(String transactionId) throws IllegalAccessException, IOException {
		rcx.checkpoint(transactionId);
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
	 * Get the instance by using the InstanceIndex contained in the passed DBKey
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object getByIndex(String transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		return rcx.getByIndex(transactionId, index);
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
	public DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		return (DBKey)rc.get((Comparable) instance);
	}
	
	public static synchronized RelatrixIndex getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID uuid = UUID.randomUUID();
		RelatrixIndex nkey = new RelatrixIndex(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
		if(DEBUG)
			System.out.printf("Returning NewKey=%s%n", nkey.toString());
		return nkey;
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
	public DBKey getByInstance(String transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		return (DBKey)rcx.get(transactionId, (Comparable) instance);
	}
	
	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		return new DBKey(rcx.getByPath(Relatrix.getTableSpace(), true), getNewKey());
	}
	
	@Override
	public DBKey getNewDBKey(String alias) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
			return new DBKey(rcx.getByAlias(alias), getNewKey());
	}
	
	@Override
	public void rollbackToCheckpoint(String transactionId) throws IOException, IllegalAccessException {
		rcx.rollbackToCheckpoint(transactionId);	
	}

	@Override
	public DBKey putAlias(String alias, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		//synchronized(mutex ) {
			if(DEBUG)
				System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
			DBKey retKey = getByInstance(alias, instance);
			// did the instance exist?
			if(retKey == null) {
				DBKey index = getNewDBKey(alias);
				// no new instance exists. store both new entries
				// no new instance exists. store both new entries
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {
						try {
							rc.storekv(alias, index, instance);
						} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
							throw new RuntimeException(e);
						}
					}
				},Relatrix.storeI);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {
						try {
							rc.storekv(alias, instance, index);
						} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
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
		//..}		
	}

	@Override
	public DBKey putAlias(String alias, String transactionId, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
			if(DEBUG)
				System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
			DBKey retKey = getByInstanceAlias(alias, transactionId, instance);
			// did the instance exist?
			if(retKey == null) {
				DBKey index = getNewDBKey(alias);
				// no new instance exists. store both new entries
				// no new instance exists. store both new entries
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {
						try {
							rcx.store(alias, index, instance);
						} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
							throw new RuntimeException(e);
						}
					}
				},Relatrix.storeI);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {
						try {
							rcx.store(alias, instance, index);
						} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
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
	public void put(DBKey dbKey, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
			if(DEBUG)
				System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
			// no new instance exists, based on primary check. store both new entries
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rc.storekv(dbKey, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rc.storekv(instance, dbKey);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
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
	public void putAlias(String alias, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		//synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
			// no new instance exists, based on primary check. store both new entries
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rc.storekv(alias, index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rc.storekv(alias, instance, index);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		//}
		
	}

	@Override
	public void put(String transactionId, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		//synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
			// no new instance exists, based on primary check. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rcx.store(transactionId, index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rcx.store(transactionId, instance, index);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		//}
		
	}

	@Override
	public void putAlias(String alias, String transactionId, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		//synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.putAlias Alias:%s Xid:%s class=%s instance=%s%n", this.getClass().getName(), alias, transactionId, instance.getClass().getName(), instance);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rcx.store(alias, transactionId, index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException | ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						rcx.store(alias, transactionId, instance, index);
					} catch (IllegalAccessException | IOException | DuplicateKeyException | ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(Relatrix.storeI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		//}
		
	}
	@Override
	public void commit(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		//synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.commitAlias committing alias:"+alias+" Xid:"+transactionId);
			rcx.commit(alias, transactionId);
		//}
		
	}

	@Override
	public void rollback(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		//synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollback alias:"+alias+" Xid:"+transactionId);
			rcx.rollback(alias, transactionId);
		//}
		
	}

	@Override
	public void checkpoint(String alias, String transactionId) throws IllegalAccessException, IOException, NoSuchElementException {
		//synchronized(mutex) {
			rcx.checkpoint(alias, transactionId);
		//}
		
	}

	@Override
	public void rollbackToCheckpoint(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		//synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollbackToCheckpoint alias:"+alias+" Xid:"+transactionId);
			rcx.rollbackToCheckpoint(alias, transactionId);
		//}	
		
	}

	@Override
	public DBKey getByInstanceAlias(String alias, Object instance) 
			throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException {
		return (DBKey) rc.get(alias, (Comparable) instance);
	}

	@Override
	public DBKey getByInstanceAlias(String alias, String transactionId, Object instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		return (DBKey) rcx.get(alias, transactionId, (Comparable) instance);
	}

	@Override
	public void deleteInstance(Comparable instance)
			throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		//synchronized(mutex) {
			// index is valid
			Object index = rc.removekv(instance);
			if(index != null) {
				rc.remove((Comparable<?>) index);
			}
		//}
		
	}

	@Override
	public void deleteInstance(String transactionId, Comparable instance)
			throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		//synchronized(mutex) {
			// index is valid
			Object index = rcx.removekv(transactionId, instance);
			if(index != null) {
				rcx.remove(transactionId, (Comparable) index);
			}	
		//}
		
	}

	@Override
	public void deleteAlias(String alias, DBKey index)
			throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		//synchronized(mutex) {
			Object instance = rc.removekv(index);
			// index is valid
			if(instance != null) {
				rc.remove((Comparable) instance);
			}	
		//}
	}

	@Override
	public void deleteAlias(String alias, String transactionId, DBKey index)
			throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			Object instance = rcx.removekv(alias, transactionId, index);
			// index is valid
			if(instance != null) {
				rcx.remove(transactionId, (Comparable) instance);
			}	
		}	
	}

	@Override
	public void deleteInstanceAlias(String alias, Comparable instance)
			throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		//synchronized(mutex) {
			// index is valid
			Object index = rc.removekv(alias, instance);
			if(index != null) {
				rc.remove(alias, (Comparable<?>) index);
			}	
		//}
	}

	@Override
	public void deleteInstanceAlias(String alias, String transactionId, Comparable instance)
			throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		//synchronized(mutex) {
		// index is valid
		Object index = rcx.removekv(alias, transactionId, instance);
		if(index != null) {
			rcx.remove(alias, transactionId, (Comparable) index);
		}	
	//}
		
	}

}
