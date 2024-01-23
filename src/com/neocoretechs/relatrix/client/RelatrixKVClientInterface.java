package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.key.DBKey;

/**
 * Defines the contract for client side communications with remote RelatrixKV server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RelatrixKVClientInterface {

	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	void send(RemoteRequestInterface iori);

	void close();

	String getLocalNode();

	String getRemoteNode();

	int getRemotePort();

	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	* @throws ClassNotFoundException 
	* @throws IllegalAccessException 
	*/
	Object remove(Comparable c) throws IOException, ClassNotFoundException, IllegalAccessException;

	boolean hasNext(RemoteObjectInterface rii);
	
	RemoteStream entrySetStream(Class<?> forName) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteKeySetIterator keySet(Class<String> class1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	Object next(RemoteObjectInterface it);
	
	void remove(RemoteObjectInterface rii) throws UnsupportedOperationException, IllegalStateException;

	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	void close(RemoteObjectInterface rii);

	/**
	 * Open a socket to the remote worker located at 'remoteWorker' with the tablespace appended
	 * so each node is named [remoteWorker]0 [remoteWorker]1 etc. The fname should be full qualified.
	 * If remote is null, the defaults will all be used, otherwise, database name will be massaged for cluster
	 * @param fname
	 * @param remote remote database name
	 * @param port remote port
	 * @return
	 * @throws IOException
	 */
	Socket Fopen(String bootNode) throws IOException;

	/**
	 * Call the remote server method to send a manually constructed command
	 * @param rs The RelatrixKvStatement manually constructed
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	Object sendCommand(RelatrixStatementInterface rs) throws IllegalAccessException, IOException, DuplicateKeyException;

	Comparable firstKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object firstValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Get the keyed value
	 * @param key The Comparable key
	 * @return The value for the given key, or null if not found
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	Object get(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable lastKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object lastValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Call the remote server method to store an object.
	 * @param k The Comparable representing the key relationship
	 * @param v The value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	Object store(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException;

	Object get(String alias, Comparable instance) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException;

	Object remove(String alias, Comparable instance) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException;

	/**
	 * Call the remote server method to store an object.
	 * @param alias The database alias
	 * @param k The Comparable representing the key relationship
	 * @param v The value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws DuplicateKeyException if the storage of a duplicate key was attempted
	 * @throws NoSuchElementException if the alias was not found
	 * @return 
	 */
	Object store(String alias, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException;

	Object firstValue(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object lastValue(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable firstKey(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable lastKey(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	RemoteStream entrySetStream(String alias, Class<?> clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteKeySetIterator keySet(String alias, Class<String> clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

}