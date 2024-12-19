package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * For Morphisms {@link com.neocoretechs.relatrix.Morphism} in the Relatrix, we store Key/Value tables of 
 * instance/DBKey DBKey/Instance {@link DBKey} {@link KeySet} to reference
 * instances of classes by an integer index to reduce redundancy in indexing. Since the 6 permutations of the
 * morphism are stored in various orders (dmr, rmd, mdr, rdm, drm, mrd) to facilitate set retrieval in the proper order, 
 * to store instances in each indexing class would present unnecessary overhead and redundancy.<p/>
 * Consequently, it is necessary to resolve these keys to instances remotely in client/server mode.
 * Since relationships can serve as components of other relationships, to recursively resolve the instances before being
 * sent down the wire is impractical. This interface allows us to have a local or remote resolver if applicable.
 * {@link IndexInstanceTable} {@link RemoteIndexInstanceTable} 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public interface IndexInstanceTableInterface {

	/**
	 * Put the key to the proper tables
	 * @return The DBKey identity of the instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey put(Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException;
	/**
	 * Put the key to the proper tables
	 * @return TODO
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey put(TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException;	
	/**
	 * Put the key to the proper tables
	 * @return TODO
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey put(Alias alias, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
	/**
	 * Put the key to the proper tables
	 * @return TODO
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey put(Alias alias, TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
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
	void put(DBKey dbKey, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException;
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
	 * @throws NoSuchElementException if the alias isnt found
	 */
	void put(Alias alias, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
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
	void put(TransactionId transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
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
	void put(Alias alias, TransactionId transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
	
	/**
	 * Get the instance by using the Instance contained in the passed DBKey
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object get(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;
	/**
	 * Get the instance references by the passed DBKey
	 * @param alias the alias to the desired database
	 * @param index the DBKey index in the alias database
	 * @return The instance desired
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object get(Alias alias, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;
	/**
	 * Get the instance contained in the passed DBKey
	 * @param transactionId
	 * @param index
	 * @return the object instance indexed by dbkey
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object get(TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;
	/**
	 * Get the instance in the alias database under transaction control
	 * @param alias
	 * @param transactionId
	 * @param index
	 * @return the instance desired
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object get(Alias alias, TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;

	/**
	 * Get the index of the instance by retrieving the instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey getKey(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException;

	/**
	 * Get the index of the instance by retrieving the instance present in the passed object
	 * @param alias the database alias
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	DBKey getKey(Alias alias, Object instance) throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException;

	/**
	 * Get index of the instance by retrieving the key for the instance present in the passed object
	 * @param alias the database alias
	 * @param transactionId
	 * @param instance the DBKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException
	 */
	DBKey getKey(Alias alias, TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
	/**
	 * Get the index of the instance by retrieving instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey getKey(TransactionId transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException;
	
	/**
	 * Return a newly generated DBKey
	 * @return a UUID based DBKey
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException;

	void checkpoint(TransactionId transactionId) throws IllegalAccessException, IOException;
	
	void checkpoint(Alias alias, TransactionId transactionId) throws IllegalAccessException, IOException, NoSuchElementException;
	
	void commit(TransactionId transactionId) throws IOException, IllegalAccessException;
	
	void commit(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException;
	
	void rollback(TransactionId transactionId) throws IOException, IllegalAccessException;

	void rollback(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException;
	
	void rollbackToCheckpoint(TransactionId transactionId) throws IOException, IllegalAccessException;

	void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException;
	
	void delete(TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;
	
	void delete(Alias alias, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void delete(Alias alias, TransactionId transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void deleteInstance(Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void deleteInstance(TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void deleteInstance(Alias alias, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void deleteInstance(Alias alias, TransactionId transactionId, Comparable instance) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;
	
	void remove(DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException;
	
	void remove(Alias alias, DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException;

	void remove(TransactionId transactionId, DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException;
	
	void remove(Alias alias, TransactionId transactionId, DBKey dKey, Comparable skeyd) throws IllegalAccessException, ClassNotFoundException, IOException, DuplicateKeyException;

}
