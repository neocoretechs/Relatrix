package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.Future;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClient;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClientTransaction;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixKVClient;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.asynch.json.AsynchRelatrixClientJson;
import com.neocoretechs.relatrix.client.asynch.json.AsynchRelatrixClientTransactionJson;
import com.neocoretechs.relatrix.client.asynch.json.AsynchRelatrixKVClientJson;
import com.neocoretechs.relatrix.client.asynch.json.AsynchRelatrixKVClientTransactionJson;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClientTransaction;
import com.neocoretechs.relatrix.client.json.RelatrixClientJson;
import com.neocoretechs.relatrix.client.json.RelatrixClientJsonTransaction;
import com.neocoretechs.relatrix.client.json.RelatrixKVClientJson;
import com.neocoretechs.relatrix.client.json.RelatrixKVClientJsonTransaction;
import com.neocoretechs.relatrix.Relatrix;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * The RemoteIndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. We use the DBKey wrapper class to carry the integer index inside the AbstractRelation.
 * which also adds validation. This class carries the client interface instances that allow over the wire
 * communication to remote tables.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public final class RemoteIndexInstanceTable implements IndexInstanceTableInterface {
	public static boolean DEBUG = false;
	private ClientInterface rc = null;
	private Object mutex = new Object();
	@Deprecated
	public static ClientInterface getClient(ClientInterface rc, IndexResolver resolver) throws IOException {
		return switch(rc) {
			case ClientTransactionInterface _ -> getTransactionClient((ClientTransactionInterface) rc, resolver);
			case ClientNonTransactionInterface _ -> getNonTransactionClient((ClientNonTransactionInterface) rc, resolver);
			case RemoteIteratorClient _ -> getIteratorClient((RemoteIteratorClient) rc, resolver);
			default -> throw new IllegalArgumentException("Unexpected value: " + rc);
		};
	}
	@Deprecated
	public static ClientInterface getTransactionClient(ClientTransactionInterface ctf, IndexResolver resolver) throws IOException {
		return switch(ctf) {
		case AsynchRelatrixKVClientTransaction _ -> new RelatrixKVClientTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixKVClientTransaction _ -> new RelatrixKVClientTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case AsynchRelatrixClientTransaction _ -> new RelatrixClientTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixClientTransaction _ -> new RelatrixClientTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case AsynchRelatrixKVClientTransactionJson _ -> new RelatrixKVClientJsonTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixKVClientJsonTransaction _ -> new RelatrixKVClientJsonTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case AsynchRelatrixClientTransactionJson _ -> new RelatrixClientJsonTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixClientJsonTransaction _ -> new RelatrixClientJsonTransaction(ctf.getRemoteNode(), ctf.getRemotePort());
		default -> throw new IllegalArgumentException("Unexpected value: " + ctf);
		};	
	}
	@Deprecated
	public static ClientInterface getNonTransactionClient(ClientNonTransactionInterface ctf, IndexResolver resolver) throws IOException {
		return switch(ctf) {
		case AsynchRelatrixKVClient _ -> new IndexResolverClient(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixKVClient _ -> new IndexResolverClient(ctf.getRemoteNode(), ctf.getRemotePort());
		case AsynchRelatrixClient _ -> new IndexResolverClient(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixClient _ -> new IndexResolverClient(ctf.getRemoteNode(), ctf.getRemotePort());
		case AsynchRelatrixKVClientJson _ -> new RelatrixKVClientJson(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixKVClientJson _ -> new RelatrixKVClientJson(ctf.getRemoteNode(), ctf.getRemotePort());
		case AsynchRelatrixClientJson _ -> new RelatrixClientJson(ctf.getRemoteNode(), ctf.getRemotePort());
		case RelatrixClientJson _ -> new RelatrixClientJson(ctf.getRemoteNode(), ctf.getRemotePort());
		default -> throw new IllegalArgumentException("Unexpected value: " + ctf);
		};
	}
	@Deprecated
	public static ClientInterface getIteratorClient(RemoteIteratorClient ric, IndexResolver resolver) throws IOException {
		if(ric instanceof RemoteIteratorClientTransaction)
			return new RelatrixClientTransaction(ric.getRemoteNode(), ric.getRemotePort());
		return new IndexResolverClient(ric.getRemoteNode(), ric.getRemotePort());
	}
	@Deprecated
	public RemoteIndexInstanceTable(ClientInterface rc, IndexResolver resolver) throws IOException {
		this.rc = getClient(rc, resolver);
		if(DEBUG)
			System.out.printf("%s c'tor setting ClientInterface=%s%n", this.getClass().getName(), rc);
	}	

	/**
	 * Put the key to the proper tables
	 * @param instance the Comparable instance payload
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey put(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// instance index not valid, key not fully formed, we may have to add instance value to table and index it
		DBKey retKey = getKey(instance);
		if(retKey == null) {
			DBKey index = getNewDBKey();
			try {
				((ClientNonTransactionInterface)rc).storekv(index, instance);
				((ClientNonTransactionInterface)rc).storekv((Comparable) instance, index);
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
	public DBKey put(TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// instance index not valid, key not fully formed, we may have to add instance value to table and index it
		DBKey retKey = getKey(transactionId, instance);
		if(retKey == null) {
			DBKey index = getNewDBKey();
			((ClientTransactionInterface)rc).storekv(transactionId, index, instance);
			((ClientTransactionInterface)rc).storekv(transactionId,  (Comparable) instance, index);
			return index;
		}
		return retKey;
	}
	@Override
	public DBKey put(Alias alias, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
		DBKey retKey = getKey(alias, instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			// no new instance exists. store both new entries
			Future<?>[] jobs = new Future[2];
			jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						((ClientNonTransactionInterface)rc).storekv(alias, index, instance);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						((ClientNonTransactionInterface)rc).storekv(alias, (Comparable) instance, index);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedThreadManager.waitForCompletion(jobs);
		}
		return retKey;		
	}

	@Override
	public DBKey put(Alias alias, TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
		DBKey retKey = getKey(alias, transactionId, instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			// no new instance exists. store both new entries
			Future<?>[] jobs = new Future[2];
			jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						((ClientTransactionInterface)rc).storekv(alias, transactionId, index, instance);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						((ClientTransactionInterface)rc).storekv(alias, transactionId, (Comparable) instance, index);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedThreadManager.waitForCompletion(jobs);
			return index;
		}
		return retKey;		
	}

	@Override
	public void put(DBKey dbKey, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		Future<?>[] jobs = new Future[2];
		jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientNonTransactionInterface)rc).storekv(dbKey, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientNonTransactionInterface)rc).storekv((Comparable) instance, dbKey);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedThreadManager.waitForCompletion(jobs);
	}

	@Override
	public void put(Alias alias, DBKey index, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		Future<?>[] jobs = new Future[2];
		jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientNonTransactionInterface)rc).storekv(alias, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientNonTransactionInterface)rc).storekv(alias, (Comparable) instance, index);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedThreadManager.waitForCompletion(jobs);	
	}

	@Override
	public void put(TransactionId transactionId, DBKey index, Object instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		Future<?>[] jobs = new Future[2];
		jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientTransactionInterface)rc).storekv(transactionId, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientTransactionInterface)rc).storekv(transactionId, (Comparable) instance, index);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedThreadManager.waitForCompletion(jobs);
	}

	@Override
	public void put(Alias alias, TransactionId transactionId, DBKey index, Object instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		Future<?>[] jobs = new Future[2];
		if(DEBUG)
			System.out.printf("%s.putAlias Alias:%s Xid:%s class=%s instance=%s%n", this.getClass().getName(), alias, transactionId, instance.getClass().getName(), instance);
		jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientTransactionInterface)rc).storekv(alias, transactionId, index, instance);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					((ClientTransactionInterface)rc).storekv(alias, transactionId, (Comparable) instance, index);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedThreadManager.waitForCompletion(jobs);
	}

	public static synchronized DBKey getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID uuid = UUID.randomUUID();
		DBKey nkey = new DBKey(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
		if(DEBUG)
			System.out.printf("RemoteIndexInstanceTable.getNewKey Returning NewKey=%s%n", nkey.toString());
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
		Object o = ((ClientNonTransactionInterface)rc).getByIndex(index);
		if(o == null) {
			if(DEBUG)
				System.out.printf("%s get for DBKey:%s returning null for getByIndex%n", this.getClass().getName(), index);
			return null;
		}
		if(o instanceof PrimaryKeySet) {
			if(DEBUG)
				System.out.printf("%s get for DBKey:%s Setting primary key identity, returning PrimaryKeySet %s for getByIndex%n", this.getClass().getName(), index, o);
			((PrimaryKeySet)o).setIdentity(index);
		}
		if(DEBUG)
			System.out.printf("%s get for key:%s returning %s%n", this.getClass().getName(), index, o);
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
		Object o = ((ClientNonTransactionInterface)rc).getByIndex(alias,index);
		if(DEBUG)
			System.out.printf("%s get for alias:%s key:%s returning:%s%n", this.getClass().getName(), alias, index, o);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setAlias(alias);
		}
		if(DEBUG)
			System.out.printf("%s get for key:%s returning %s%n", this.getClass().getName(), index, o);
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
		Object o = ((ClientTransactionInterface)rc).getByIndex(alias, transactionId, index);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setAlias(alias);
			((PrimaryKeySet)o).setTransactionId(transactionId);
		}
		if(DEBUG)
			System.out.printf("%s get for key:%s returning %s%n", this.getClass().getName(), index, o);
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
		Object o = ((ClientTransactionInterface)rc).getByIndex(transactionId, index);
		if(o == null)
			return null;
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setTransactionId(transactionId);
		}
		if(DEBUG)
			System.out.printf("%s get for key:%s returning %s%n", this.getClass().getName(), index, o);
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
		if(DEBUG)
			System.out.printf("%s get for key:%s returns %s%n", this.getClass().getName(), instance, ((ClientNonTransactionInterface)rc).get(instance));
		return (DBKey)((ClientNonTransactionInterface)rc).get(instance);
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
		return (DBKey)((ClientTransactionInterface)rc).get(transactionId, (Comparable) instance);
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		return getNewKey();
	}

	@Override
	public DBKey getKey(Alias alias, Object instance) throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException {
		return (DBKey) ((ClientNonTransactionInterface)rc).get(alias, instance);
	}

	@Override
	public DBKey getKey(Alias alias, TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		return (DBKey) ((ClientTransactionInterface)rc).get(alias, transactionId, (Comparable) instance);
	}

	@Override
	public void putKey(Alias alias2, DBKey dbKey, Object instance) {
		try {
			((ClientNonTransactionInterface)rc).storekv(alias2, dbKey, instance);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putKey(DBKey dbKey, Object instance) {
		try {
			((ClientNonTransactionInterface)rc).storekv(dbKey, instance);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void putKey(Alias alias2, TransactionId transactionId, DBKey dbKey, Object instance) {
		try {
			((ClientTransactionInterface)rc).storekv(alias2, transactionId, dbKey, instance);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putKey(TransactionId transactionId, DBKey dbKey, Object instance) {
		try {
			((ClientTransactionInterface)rc).storekv(transactionId, dbKey, instance);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
