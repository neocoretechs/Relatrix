package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DuplicateKeyException;
/**
 * For Morphisms in the Relatrix, we store Key/Value tables of instance/DBKey DBKey/Instance to reference
 * instances of classes by an integer index to reduce redundancy in indexing. Since the 6 permutations of the
 * morphism are stored in various orders (dmr, rmd, mdr, rdm, drm, mrd) to facilitate set retrieval in the proper order, 
 * to store instances in each indexing class would present unnecessary overhead and redundancy.<p/>
 * Consequently, it is necessary to resolve these keys to instances remotely in client/server mode.
 * Since relationships can serve as components of other relationships, to recursively resolve the instances before being
 * sent down the wire is impractical. This interface allows us to have a local or remote resolver if applicable.
 * {@see IndexInstanceTable} {@see RemoteIndexInstanceTable} 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public interface IndexInstanceTableInterface {

	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException;

	void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	/**
	 * Get the instance by using the Instance contained in the passed DBKey
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;

	/**
	 * Get the index of the instance by retrieving the instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException;

	DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException;

	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void put(String transactionId, DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException;

	void delete(String transactionId, DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException;

	void commit(String transactionId) throws IOException, IllegalAccessException;

	void rollback(String transactionId) throws IOException, IllegalAccessException;

	void checkpoint(String transactionId) throws IllegalAccessException, IOException;

	void rollbackToCheckpoint(String transactionId) throws IOException, IllegalAccessException;

	/**
	 * Get the instance contained in the passed DBKey
	 * @param transactionId
	 * @param index
	 * @return the object instance indexed by dbkey
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object getByIndex(String transactionId, DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;

	/**
	 * Get the index of the instance by retrieving instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The index contained in the retrieved Instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey getByInstance(String transactionId, Object instance) throws IllegalAccessException, IOException, ClassNotFoundException;

	DBKey getNewDBKey(String alias) throws ClassNotFoundException, IllegalAccessException, IOException;

	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void putAlias(String alias, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;

	/**
	 * Put the key to the proper tables
	 * @param index
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void putAlias(String alias, String transactionId, DBKey index, Comparable instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;

	void commit(String alias, String transactionId) throws IOException, IllegalAccessException, NoSuchElementException;

	void rollback(String alias, String transactionId)
			throws IOException, IllegalAccessException, NoSuchElementException;

	void checkpoint(String alias, String transactionId)
			throws IllegalAccessException, IOException, NoSuchElementException;

	void rollbackToCheckpoint(String alias, String transactionId)
			throws IOException, IllegalAccessException, NoSuchElementException;

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
	DBKey getByInstanceAlias(String alias, Object instance)
			throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException;

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
	DBKey getByInstanceAlias(String alias, String transactionId, Object instance)
			throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchElementException;
}