package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.relatrix.DuplicateKeyException;

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

	void commit() throws IOException;

	void rollback() throws IOException;

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
	
	Integer getIncrementedLastGoodKey();
}