package com.neocoretechs.relatrix.key;

import java.io.IOException;

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

	void commit() throws IOException, IllegalAccessException;

	void rollback() throws IOException, IllegalAccessException;

	void checkpoint() throws IllegalAccessException, IOException;

	/**
	 * Get the instance by using the InstanceIndex contained in the passed DBKey
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException;

	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed object
	 * @param instance the DbKey containing the instance
	 * @return The Integer index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException;

	Object lastKey(Class<DBKey> class1) throws IllegalAccessException, IOException;
	
	Integer getIncrementedLastGoodKey() throws ClassNotFoundException, IllegalAccessException, IOException;
}