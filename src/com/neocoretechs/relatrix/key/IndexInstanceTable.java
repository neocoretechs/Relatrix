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
	public static boolean DEBUG = true;
	private Object mutex = new Object();
	
	/**
	 * Put the key to the proper tables. The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store unless key exists and differ
	 * @param index the DBKey index
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DuplicateKeyExcpetion keys are different with existing instance, we are trying to change key
	 */
	@Override
	public void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, DuplicateKeyException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put index=%s instance=%s%n", index.getClass().getName(), index, instance);
			DBKey retKey = getByInstance(instance);
			// did the instance exist?
			if(retKey == null) {
				// no new instance exists. store both new entries, if instance doesnt exist but key does, new instance overwrites
				RelatrixKV.store(index, instance);
				RelatrixKV.store(instance, index);
			} else {
				// we had an existing instance, are keys different?
				if(!retKey.equals(index))
					// keys are different with existing instance, we are trying to change key, throw duplicate key exception
					throw new DuplicateKeyException("Duplicate instance with different key attempted, integrity constraint violated:"+index+" "+instance+" replacing "+retKey);
			}
			// do nothing if they were the same
		}
	}
	
	/**
	 * Put the key to the proper tables using the database alias.
	 * The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed in their rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store unless key exists and differ
	 * @param alias the database alias
	 * @param index the DBKey index
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 * @throws DuplicateKeyExcpetion keys are different with existing instance, we are trying to change key
	 */
	@Override
	public void putAlias(String alias, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, DuplicateKeyException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.putAlias alias=%s index=%s instance=%s%n", index.getClass().getName(), alias, index, instance);
			DBKey retKey = getByInstance(alias, instance);
			// did the instance exist?
			if(retKey == null) {
				// no new instance exists. store both new entries, if instance doesnt exist but key does, new instance overwrites
				RelatrixKV.store(alias, index, instance);
				RelatrixKV.store(alias, instance, index);
			} else {
				// we had an existing instance, are keys different?
				if(!retKey.equals(index))
					// keys are different with existing instance, we are trying to change key, throw duplicate key exception
					throw new DuplicateKeyException("Duplicate instance with different key attempted, integrity constraint violated:"+index+" "+instance+" replacing "+retKey);
			}
			// do nothing if they were the same
		}
	}
	/**
	 * Put the key to the proper tables in the scope of this transaction.
	 * The operation is a simple K/V put using {@link RelatrixKV} since we
	 * form the {@link DBKey} when we set the values of domain/map/range in the mutator methods of {@link Morphism}, and
	 * the proper instances are placed intheir rightful databases at that time. Here we are just storing the
	 * presumably fully formed DBKey indexes. getByInstance, if no instance exists store unless key exists and differ
	 * @param transactionId the transaction identifier
	 * @param index the DBKey index
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DuplicateKeyExcpetion keys are different with existing instance, we are trying to change key
	 */
	@Override
	public void put(String transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, DuplicateKeyException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.put Xid:%s index=%s instance=%s%n", index.getClass().getName(), transactionId, index, instance);
			DBKey retKey = getByInstance(transactionId, instance);
			// did the instance exist?
			if(retKey == null) {
				// no new instance exists. store both new entries, if instance doesnt exist but key does, new instance overwrites
				RelatrixKVTransaction.store(transactionId, index, instance);
				RelatrixKVTransaction.store(transactionId, instance, index);
			} else {
				// we had an existing instance, are keys different?
				if(!retKey.equals(index))
					// keys are different with existing instance, we are trying to change key, throw duplicate key exception
					throw new DuplicateKeyException("Duplicate instance with different key attempted, integrity constraint violated:"+index+" "+instance+" replacing "+retKey);
			}
			// do nothing if they were the same
	
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
	 * @param index the DBKey index
	 * @param instance the object instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 * @throws DuplicateKeyExcpetion keys are different with existing instance, we are trying to change key
	 */
	@Override
	public void putAlias(String alias, String transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, DuplicateKeyException, NoSuchElementException {
		synchronized(mutex) {
			if(DEBUG)
				System.out.printf("%s.putAlias Alias:%s Xid:%s index=%s instance=%s%n", index.getClass().getName(), alias, transactionId, index, instance);
			DBKey retKey = getByInstanceAlias(transactionId, instance);
			// did the instance exist?
			if(retKey == null) {
				RelatrixKVTransaction.store(alias, transactionId, index, instance);
				RelatrixKVTransaction.store(alias, transactionId, instance, index);
				// no new instance exists. store both new entries, if instance doesnt exist but key does, new instance overwrites
			} else {
				// we had an existing instance, are keys different?
				if(!retKey.equals(index))
					// keys are different with existing instance, we are trying to change key, throw duplicate key exception
					throw new DuplicateKeyException("Duplicate instance with different key attempted, integrity constraint violated:"+index+" "+instance+" replacing "+retKey);
			}
			// do nothing if they were the same
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
	 * @return the instance object indexed by this DBKey which is used to determine database and hence proper catalog path, then unique instance UUID
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
		String sdb = Relatrix.getDatabasePath(index.databaseIndex);
		if(sdb == null) {
			throw new IOException("The database for the UUID "+index.databaseIndex+" was not found. May have been deleted.");
		}
		BufferedMap bm = DatabaseManager.getMapByPath(sdb, DBKey.class);
		Object o =  bm.get(index);
		if(o == null)
			return null;
		return ((KeyValue)o).getmValue();
		//}
	}
	
	/**
	 * Get the instance contained in the passed DBKey using a transactional context. 
	 * @param transactionId the trasnaction
	 * @param index the DBKey from which we extract the database index, and hence the proper path from catalog
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object getByIndex(String transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		//synchronized(mutex) {
		String sdb = Relatrix.getDatabasePath(index.databaseIndex);
		if(sdb == null) {
			throw new IOException("The database for the UUID "+index.databaseIndex+" was not found. May have been deleted.");
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
			return new DBKey(Relatrix.getByPath(Relatrix.getTableSpace(), true), Relatrix.getNewKey());
	}
	
	@Override
	public DBKey getNewDBKey(String alias) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
			return new DBKey(Relatrix.getByAlias(alias), Relatrix.getNewKey());
	}

}
