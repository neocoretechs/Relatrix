package com.neocoretechs.relatrix.key;

import java.io.IOException;

import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;

import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.DatabaseManager;
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
	private Object mutex = new Object();
	
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
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put class=%s instance=%s%n", this.getClass().getName(), instance.getClass().getName(), instance);
			DBKey retKey = getByInstance(instance);
			// did the instance exist?
			if(retKey == null) {
				DBKey index = getNewDBKey();
				if(DEBUG)
					System.out.printf("%s.put new instance key=%s%n", this.getClass().getName(), index);
				// no new instance exists. store both new entries
				try {
					RelatrixKV.store(index, instance);
					RelatrixKV.store(instance, index);
					return index;
				} catch (DuplicateKeyException e) {
					// should never happen
					throw new IOException(e);
				}
			}
			if(DEBUG)
				System.out.printf("%s.put existing instance key=%s%n", this.getClass().getName(), retKey);
			return retKey;
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
	public DBKey putAlias(String alias, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.putAlias alias=%s class=%s instance=%s%n", this.getClass().getName(), alias, instance.getClass().getName(), instance);
			DBKey retKey = getByInstance(alias, instance);
			// did the instance exist?
			if(retKey == null) {
				DBKey index = getNewDBKey(alias);
				// no new instance exists. store both new entries
				try {
					RelatrixKV.store(alias, index, instance);
					RelatrixKV.store(alias, instance, index);
					return index;
				} catch (DuplicateKeyException e) {
					throw new IOException(e);
				}	
			}
			return retKey;
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
	public DBKey put(String transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put Xid:%s class=%s instance=%s%n", this.getClass().getName(), transactionId, instance.getClass().getName(), instance);
			DBKey retKey = getByInstance(transactionId, instance);
			// did the instance exist?
			if(retKey == null) {
				DBKey index = getNewDBKey();
				// no new instance exists. store both new entries
				try {
					if(DEBUG)
						System.out.printf("%s.put new key Xid:%s DBKey=%s class=%s instance=%s%n", this.getClass().getName(), transactionId, index, instance.getClass().getName(), instance);
					RelatrixKVTransaction.store(transactionId, index, instance);
					RelatrixKVTransaction.store(transactionId, instance, index);
					return index;
				} catch (DuplicateKeyException e) {
					throw new IOException(e);
				}
			} 
			if(DEBUG)
				System.out.printf("%s.put returning existing key Xid:%s DBKey=%s class=%s instance=%s%n", this.getClass().getName(), transactionId, retKey, instance.getClass().getName(), instance);
			return retKey;
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
	public DBKey putAlias(String alias, String transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.putAlias Alias:%s Xid:%s class=%s instance=%s%n", this.getClass().getName(), alias, transactionId, instance.getClass().getName(), instance);
			DBKey retKey = getByInstanceAlias(alias, transactionId, instance);
			// did the instance exist?
			if(retKey == null) {
				DBKey index = getNewDBKey(alias);
				try {
					RelatrixKVTransaction.store(alias, transactionId, index, instance);
					RelatrixKVTransaction.store(alias, transactionId, instance, index);
					// no new instance exists. store both new entries
					return index;
				} catch (DuplicateKeyException e) {
					throw new IOException(e);
				}
			}
			return retKey;
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
			instance = (Comparable) getByIndex(transactionId, index);
			if(instance != null) {
				RelatrixKVTransaction.remove(transactionId, instance);
			}
			RelatrixKVTransaction.remove(transactionId, index);
		}
	}
	
	@Override
	public void deleteAlias(String alias, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			Comparable instance = null;
			// index is valid
			instance = (Comparable) getByIndex(index);
			if(instance != null) {
				RelatrixKV.remove(alias, instance);
			}
			RelatrixKV.remove(alias, index);
		}
	}
	
	@Override
	public void deleteAlias(String alias, String transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			Comparable instance = null;
			// index is valid
			instance = (Comparable) getByIndex(transactionId, index);
			if(instance != null) {
				RelatrixKVTransaction.remove(transactionId, instance);
			}
			RelatrixKVTransaction.remove(transactionId, index);
		}
	}
	
	@Override
	public void deleteInstance(Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			// index is valid
			DBKey index = getByInstance(instance);
			if(index != null) {
				RelatrixKV.remove(index);
			}
			RelatrixKV.remove(instance);
		}
	}
	
	@Override
	public void deleteInstance(String transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			// index is valid
			DBKey index = getByInstance(transactionId, instance);
			if(index != null) {
				RelatrixKVTransaction.remove(transactionId, index);
			}
			RelatrixKVTransaction.remove(transactionId, instance);
		}
	}
	
	@Override
	public void deleteInstanceAlias(String alias, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			// index is valid
			DBKey index = getByInstanceAlias(alias, instance);
			if(index != null) {
				RelatrixKV.remove(alias, index);
			}
			RelatrixKV.remove(alias, instance);
		}
	}
	
	@Override
	public void deleteInstanceAlias(String alias, String transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		synchronized(mutex) {
			// index is valid
			DBKey index = getByInstanceAlias(alias, transactionId, instance);
			if(index != null) {
				RelatrixKVTransaction.remove(alias, transactionId, index);
			}
			RelatrixKVTransaction.remove(alias, transactionId, instance);
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
	public void commit(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.commitAlias committing alias:"+alias+" Xid:"+transactionId);
			RelatrixKVTransaction.commit(alias, transactionId);
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
	public void rollback(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollback alias:"+alias+" Xid:"+transactionId);
			RelatrixKVTransaction.rollback(alias, transactionId);
		}
	}
	
	@Override
	public void checkpoint(String transactionId) throws IllegalAccessException, IOException {
		synchronized(mutex) {
			RelatrixKVTransaction.checkpoint(transactionId);
		}
	}
	
	@Override
	public void checkpoint(String alias, String transactionId) throws IllegalAccessException, IOException, NoSuchElementException {
		synchronized(mutex) {
			RelatrixKVTransaction.checkpoint(alias, transactionId);
		}
	}
	
	@Override
	public void rollbackToCheckpoint(String transactionId) throws IOException, IllegalAccessException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollbackToCheckpoint "+transactionId);
			RelatrixKVTransaction.rollbackToCheckpoint(transactionId);
		}
		
	}
	
	@Override
	public void rollbackToCheckpoint(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("IndexInstanceTable.rollbackToCheckpoint alias:"+alias+" Xid:"+transactionId);
			RelatrixKVTransaction.rollbackToCheckpoint(alias, transactionId);
		}
		
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
	public Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
		String sdb = Relatrix.getDatabasePath(new DatabaseCatalog(index.databaseIndex));
		if(sdb == null) {
			throw new IOException("The database for the index "+index.databaseIndex+" was not found. May have been deleted.");
		}
		if(DEBUG)
			System.out.printf("%s getByIndex for key:%s produces db path:%s%n", this.getClass().getName(), index, sdb);
		BufferedMap bm = DatabaseManager.getMapByPath(sdb, DBKey.class);
		Object o =  bm.get(index);
		if(DEBUG)
			System.out.printf("%s getByIndex for key:%s returning:%s%n", this.getClass().getName(), index, o);
		if(o == null)
			return null;
		return ((KeyValue)o).getmValue();
		//}
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
	public Object getByIndex(String transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
		String sdb = Relatrix.getDatabasePath(new DatabaseCatalog(index.databaseIndex));
		if(sdb == null) {
			throw new IOException("The database for the index "+index.databaseIndex+" was not found. May have been deleted.");
		}
		TransactionalMap tm = DatabaseManager.getTransactionalMapByPath(sdb, DBKey.class, transactionId);
		Object o =  tm.get(index);
		if(o == null)
			return null;
		return ((KeyValue)o).getmValue();
		//}
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
	public DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
		if(DEBUG) {
			DBKey dbkey = (DBKey) RelatrixKV.get((Comparable) instance);
			System.out.printf("%s getByInstance for key:%s produces key:%s%n", this.getClass().getName(), instance, dbkey);
			return dbkey;
		}
			return (DBKey) RelatrixKV.get((Comparable) instance);
		//}
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
	public DBKey getByInstanceAlias(String alias, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		//synchronized(mutex) {
			return (DBKey) RelatrixKV.get(alias, (Comparable) instance);
		//}
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
	public DBKey getByInstance(String transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
			return (DBKey) RelatrixKVTransaction.get(transactionId, (Comparable) instance);
		//}
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
	public DBKey getByInstanceAlias(String alias, String transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException {
		//synchronized(mutex) {
			return (DBKey) RelatrixKVTransaction.get(alias, transactionId, (Comparable) instance);
		//}
	}
	
	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
			return new DBKey(Relatrix.getByPath(Relatrix.getTableSpace(), true).getRelatrixIndex(), Relatrix.getNewKey());
	}
	
	@Override
	public DBKey getNewDBKey(String alias) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
			return new DBKey(Relatrix.getByAlias(alias).getRelatrixIndex(), Relatrix.getNewKey());
	}

}
