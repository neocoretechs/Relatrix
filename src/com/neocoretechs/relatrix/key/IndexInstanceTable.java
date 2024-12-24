package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.TransactionalMap;
/**
 * The IndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the  index. We use the DBKey wrapper class to carry the index inside the Morphism.
 * which also adds validation. A constructor carrying a transaction Id sets up methods for calls to the
 * transaction oriented classes.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public final class IndexInstanceTable implements IndexInstanceTableInterface {
	public static boolean DEBUG = false;
	public static boolean ASSERTKEY = true; // on get resolving getKey verify DBKey instance

	/**
	 * Put the key to the proper tables. The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store 
	 * @param instance the object instance
	 * @return the new DBKey for instance or existing DBKey
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey put(Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
		DBKey retKey = getKey(instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			if(DEBUG)
				System.out.printf("%s.put new instance key=%s%n", this.getClass().getName(), index);
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKV.store(index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKV.store(instance, index);
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
			return index;
		}
		if(DEBUG)
			System.out.printf("%s.put existing instance key=%s%n", this.getClass().getName(), retKey);
		return retKey;
	}
	/**
	 * Put the key to the proper tables. The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store
	 * @param dbKey the DBKey of the previously stored primary key 
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put Index=%s class=%s instance=%s%n", this.getClass().getName(), index, instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		// no new instance exists. store both new entries
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKV.store(index, instance);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKV.store(instance, index);
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
	/**
	 * Put the key to the proper tables using the database alias.
	 * The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store
	 * @param alias the database alias
	 * @param instance the object instance
	 * @return the existing DBKey or new instance key
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	@Override
	public DBKey put(Alias alias, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
		DBKey retKey = getKey(alias, instance);
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s class=%s instance=%s getByInstance result=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance, retKey.toString());
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			if(DEBUG)
				System.out.printf("%s.putAlias alias=%s class=%s instance=%s getNewDBKey result=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance, index.toString());			
			// no new instance exists. store both new entries
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKV.store(alias, index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},Relatrix.storeI);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKV.store(alias, instance, index);
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
			return index;
		}
		return retKey;
	}
	/**
	 * Put the key to the proper tables. The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store
	 * @param alias the db alias
	 * @param dbKey the DBKey of the previously stored primary key 
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws noSuchElementException if the alias is not found
	 */
	@Override
	public void put(Alias alias, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias alias=%s DBKey=%s class=%s instance=%s%n", this.getClass().getName(), alias.getAlias(), index.toString(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKV.store(alias, index, instance);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},Relatrix.storeI);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKV.store(alias, instance, index);
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
	/**
	 * Put the key to the proper tables in the scope of this transaction.
	 * The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed intheir rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store
	 * @param transactionId the transaction identifier
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey put(TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s.put Xid:%s class=%s instance=%s%n", this.getClass().getName(), transactionId, instance.getClass().getName(), instance);
		DBKey retKey = getKey(transactionId, instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			// no new instance exists. store both new entries
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKVTransaction.store(transactionId, index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},RelatrixTransaction.storeITransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKVTransaction.store(transactionId, instance, index);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},RelatrixTransaction.storeITransaction);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(RelatrixTransaction.storeITransaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return index;
		} 
		if(DEBUG)
			System.out.printf("%s.put returning existing key Xid:%s DBKey=%s class=%s instance=%s%n", this.getClass().getName(), transactionId, retKey, instance.getClass().getName(), instance);
		return retKey;
	}
	/**
	 * Put the key to the proper tables. The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store
	 * @param transactionId the transaction id
	 * @param dbKey the DBKey of the previously stored primary key 
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void put(TransactionId transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.put DBKey=%s class=%s instance=%s%n", this.getClass().getName(),index.toString(), instance.getClass().getName(), instance);
		// no new instance exists, based on primary check. store both new entries
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKVTransaction.store(transactionId, index, instance);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},RelatrixTransaction.storeITransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKVTransaction.store(transactionId, instance, index);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},RelatrixTransaction.storeITransaction);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(RelatrixTransaction.storeITransaction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Put the key to the proper tables in the scope of this transaction using the database alias.
	 * The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store unless key exists and differ
	 * @param alias the database alias
	 * @param transactionId
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	@Override
	public DBKey put(Alias alias, TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias Alias:%s Xid:%s class=%s instance=%s%n", this.getClass().getName(), alias, transactionId, instance.getClass().getName(), instance);
		DBKey retKey = getKey(alias, transactionId, instance);
		// did the instance exist?
		if(retKey == null) {
			DBKey index = getNewDBKey();
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKVTransaction.store(alias, transactionId, index, instance);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},RelatrixTransaction.storeITransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						RelatrixKVTransaction.store(alias, transactionId, instance, index);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			},RelatrixTransaction.storeITransaction);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(RelatrixTransaction.storeITransaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return index;
		}
		return retKey;
	}
	/**
	 * Put the key to the proper tables in the scope of this transaction using the database alias.
	 * The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store unless key exists and differ
	 * @param alias the database alias
	 * @param transactionId
	 * @param index the db index from primary key
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	@Override
	public void put(Alias alias, TransactionId transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("%s.putAlias Alias:%s Xid:%s DBKey=%s class=%s instance=%s%n", this.getClass().getName(), alias, transactionId,index.toString(), instance.getClass().getName(), instance);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKVTransaction.store(alias, transactionId, index, instance);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},RelatrixTransaction.storeITransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKVTransaction.store(alias, transactionId, instance, index);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},RelatrixTransaction.storeITransaction);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(RelatrixTransaction.storeITransaction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Object instance = RelatrixKV.remove(index);
		if(instance != null) {
			RelatrixKV.remove((Comparable<?>) instance);
		}
	}

	@Override
	public void delete(TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Object instance = RelatrixKVTransaction.remove(transactionId, index);
		// index is valid
		if(instance != null) {
			RelatrixKVTransaction.remove(transactionId, (Comparable) instance);
		}
	}

	@Override
	public void delete(Alias alias, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Object instance = RelatrixKV.remove(alias, index);
		// index is valid
		if(instance != null) {
			RelatrixKV.remove(alias, (Comparable<?>) instance);
		}
	}

	@Override
	public void delete(Alias alias, TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		Object instance = RelatrixKVTransaction.remove(alias, transactionId, index);
		// index is valid
		if(instance != null) {
			RelatrixKVTransaction.remove(alias, transactionId, (Comparable) instance);
		}
	}

	@Override
	public void deleteInstance(Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = RelatrixKV.remove(instance);
		if(index != null) {
			RelatrixKV.remove((Comparable<?>) index);
		}
	}

	@Override
	public void deleteInstance(TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = RelatrixKVTransaction.remove(transactionId, instance);
		if(index != null) {
			RelatrixTransaction.remove(transactionId, (Comparable) index);
		}	

	}

	@Override
	public void deleteInstance(Alias alias, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = RelatrixKV.remove(alias, instance);
		if(index != null) {
			Relatrix.remove(alias, (Comparable<?>) index);
		}	
	}

	@Override
	public void deleteInstance(Alias alias, TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		// index is valid
		Object index = RelatrixKVTransaction.remove(alias, transactionId, instance);
		if(index != null) {
			RelatrixTransaction.remove(alias, transactionId, (Comparable) index);
		}	
	}

	@Override
	public void commit(TransactionId transactionId) throws IOException, IllegalAccessException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.commit committing "+transactionId);
		RelatrixKVTransaction.commit(transactionId);
	}

	@Override
	public void commit(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.commitAlias committing alias:"+alias+" Xid:"+transactionId);
		RelatrixKVTransaction.commit(alias, transactionId);
	}

	@Override
	public void rollback(TransactionId transactionId) throws IOException, IllegalAccessException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.rollback "+transactionId);
		RelatrixKVTransaction.rollback(transactionId);
	}

	@Override
	public void rollback(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.rollback alias:"+alias+" Xid:"+transactionId);
		RelatrixKVTransaction.rollback(alias, transactionId);
	}

	@Override
	public void checkpoint(TransactionId transactionId) throws IllegalAccessException, IOException {
		RelatrixKVTransaction.checkpoint(transactionId);
	}

	@Override
	public void checkpoint(Alias alias, TransactionId transactionId) throws IllegalAccessException, IOException, NoSuchElementException {
		RelatrixKVTransaction.checkpoint(alias, transactionId);
	}

	@Override
	public void rollbackToCheckpoint(TransactionId transactionId) throws IOException, IllegalAccessException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.rollbackToCheckpoint "+transactionId);
		RelatrixKVTransaction.rollbackToCheckpoint(transactionId);
	}

	@Override
	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		if(DEBUG)
			System.out.printf("IndexInstanceTable.rollbackToCheckpoint alias:"+alias+" Xid:"+transactionId);
		RelatrixKVTransaction.rollbackToCheckpoint(alias, transactionId);
	}

	/**
	 * Get the instance contained in the passed DBKey
	 * @param index the DBKey from which we extract the database index, and hence the proper path from catalog
	 * @return the instance object indexed by this DBKey which is used to determine database and hence proper catalog path
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object get(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s get for key:%s%n", this.getClass().getName(), index);
		BufferedMap bm = RelatrixKV.getMap(DBKey.class);
		Object o =  bm.get(index);
		if(DEBUG)
			System.out.printf("%s get for key:%s returning:%s%n", this.getClass().getName(), index, o);
		if(o == null)
			return null;
		o = ((KeyValue)o).getmValue();
		if(o instanceof PrimaryKeySet)
			((PrimaryKeySet)o).setIdentity(index);
		return o;
	}

	@Override
	public Object get(Alias alias, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s get for Alias:%s index:%s%n", this.getClass().getName(), alias, index);
		BufferedMap bm = RelatrixKV.getMap(alias, DBKey.class);
		Object o =  bm.get(index);
		if(DEBUG)
			System.out.printf("%s getByIndex for key:%s returning:%s%n", this.getClass().getName(), index, o);
		if(o == null)
			return null;
		o = ((KeyValue)o).getmValue();
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setAlias(alias);
		}
		return o;
	}
	/**
	 * Get the instance contained in the passed DBKey using a transactional context. 
	 * @param transactionId the transaction
	 * @param index the DBKey from which we extract the database index, and hence the proper path from catalog
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object get(TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionalMap tm = RelatrixKVTransaction.getMap(DBKey.class, transactionId);
		Object o =  tm.get(transactionId, index);
		if(o == null)
			return null;
		o = ((KeyValue)o).getmValue();
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setTransactionId(transactionId);
		}
		return o;
	}

	@Override
	public Object get(Alias alias, TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
		TransactionalMap tm = RelatrixKVTransaction.getMap(alias, DBKey.class, transactionId);
		Object o =  tm.get(transactionId, index);
		if(o == null)
			return null;
		o = ((KeyValue)o).getmValue();
		if(o instanceof PrimaryKeySet) {
			((PrimaryKeySet)o).setIdentity(index);
			((PrimaryKeySet)o).setAlias(alias);
			((PrimaryKeySet)o).setTransactionId(transactionId);
		}
		return o;
	}
	/**
	 * Get the index of the instance by retrieving the key for the instance present in the passed object.
	 * Merely does a RelatrixKV.get on instance, whose payload is presumed to be a {@link DBKey}.
	 * Cast will fail if not a DBKey.
	 * @param instance the DBKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey getKey(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(DEBUG) {
			DBKey dbkey = (DBKey) RelatrixKV.get((Comparable) instance);
			System.out.printf("%s getKey:%s produces key:%s%n", this.getClass().getName(), instance, dbkey);
			return dbkey;
		}
		return (DBKey) RelatrixKV.get((Comparable) instance);
	}

	/**
	 * Get the index of the instance by retrieving the key for the instance present in the passed object in the database alias indicated.
	 * @param alias the database alias
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	@Override
	public DBKey getKey(Alias alias, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(DEBUG) {
			DBKey dbkey = (DBKey) RelatrixKV.get((Comparable) instance);
			System.out.printf("%s getKey:%s Alias:%s produces key:%s%n", this.getClass().getName(), instance, alias, dbkey);
			return dbkey;
		}
		return (DBKey) RelatrixKV.get(alias, (Comparable) instance);
	}

	/**
	 * Get index of the instance by retrieving the key for the instance present in the passed object
	 * @param transaction id
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey getKey(TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		return (DBKey) RelatrixKVTransaction.get(transactionId, (Comparable) instance);
	}

	/**
	 * Get index of the instance by retrieving the key for the instance present in the passed object
	 * @param alias the database alias
	 * @param transactionId
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	@Override
	public DBKey getKey(Alias alias, TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		if(ASSERTKEY) {
			Object o = RelatrixKVTransaction.get(alias, transactionId, (Comparable) instance);
			if(o != null && !(o instanceof DBKey))
				System.out.println("Error getting "+o+" instance:"+instance+" alias:"+alias+" xid:"+transactionId);
			return (DBKey) o;
		}
		return (DBKey) RelatrixKVTransaction.get(alias, transactionId, (Comparable) instance);
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		return Relatrix.getNewKey();
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
