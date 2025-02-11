package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.rocksack.Alias;
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
	private ClientInterface rc = null;
	private Object mutex = new Object();

	public RemoteIndexInstanceTable(RelatrixClientInterface remoteIndexInstanceTable) throws IOException {
		this.rc = (ClientInterface) remoteIndexInstanceTable;
	}	

	public RemoteIndexInstanceTable(RelatrixClientTransactionInterface rc) throws IOException {
		this.rc = (ClientInterface) rc;
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
				((RelatrixClient)rc).storekv(index, instance);
				((RelatrixClient)rc).storekv(instance, index);
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
			((RelatrixClientTransaction)rc).storekv(transactionId, index, instance);
			((RelatrixClientTransaction)rc).storekv(transactionId,  instance, index);
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
						((RelatrixClient)rc).storekv(alias, index, instance);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						((RelatrixClient)rc).storekv(alias, instance, index);
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
						((RelatrixClientTransaction)rc).storekv(alias, transactionId, index, instance);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						((RelatrixClientTransaction)rc).storekv(alias, transactionId, instance, index);
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
					((RelatrixClient)rc).storekv(dbKey, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					((RelatrixClient)rc).storekv(instance, dbKey);
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
					((RelatrixClient)rc).storekv(alias, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					((RelatrixClient)rc).storekv(alias, instance, index);
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
					((RelatrixClientTransaction)rc).storekv(transactionId, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					((RelatrixClientTransaction)rc).storekv(transactionId, instance, index);
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
					((RelatrixClientTransaction)rc).storekv(alias, transactionId, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					((RelatrixClientTransaction)rc).storekv(alias, transactionId, instance, index);
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
		if(DEBUG)
			System.out.printf("%s get for key:%s%n", this.getClass().getName(), index);
		Object o = ((RelatrixClient)rc).getByIndex(index);
		if(DEBUG)
			System.out.printf("%s get for key:%s returning:%s%n", this.getClass().getName(), index, o);
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
		Object o = ((RelatrixClient)rc).getByIndex(alias,index);
		if(DEBUG)
			System.out.printf("%s get for alias:%s key:%s returning:%s%n", this.getClass().getName(), alias, index, o);
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
		Object o = ((RelatrixClientTransaction)rc).getByIndex(alias, transactionId, index);
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
		if(DEBUG)
			System.out.printf("%s get for xid:%s key:%s%n", this.getClass().getName(), transactionId, index);
		Object o = ((RelatrixClientTransaction)rc).getByIndex(transactionId, index);
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
		return (DBKey)((RelatrixClient)rc).get((Comparable) instance);
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
		return (DBKey)((RelatrixClientTransaction)rc).get(transactionId, (Comparable) instance);
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		return getNewKey();
	}

	@Override
	public DBKey getKey(Alias alias, Object instance) throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException {
		return (DBKey) ((RelatrixClient)rc).get(alias, (Comparable) instance);
	}

	@Override
	public DBKey getKey(Alias alias, TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		return (DBKey) ((RelatrixClientTransaction)rc).get(alias, transactionId, (Comparable) instance);
	}

	
}
