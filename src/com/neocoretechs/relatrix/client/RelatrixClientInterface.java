package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.key.DBKey;
/**
 * Defines the contract for client side communications with remote Relatrix server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RelatrixClientInterface {

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
	 * Get the last good DBKey from the DBKey table, which is the highest numbered last key delivered.
	 * @return The last good key
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	Integer getIncrementedLastGoodKey() throws ClassNotFoundException, IllegalAccessException, IOException;


	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	void transactionCommit() throws IOException;

	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	void transactionRollback() throws IOException;

	/**
	 * Take a check point of our current indicies. What this means is that we are
	 * going to write a log record such that if we crash will will restore the logs from that point forward.
	 * We have to have confidence that we are doing this at a legitimate point, so this should only be called if things are well
	 * and processing is proceeding normally. Its a way to say "start from here and go forward in time 
	 * if we crash, to restore the data to its state up to that point", hence check, point...
	 * If we are loading lots of data and we want to partially confirm it as part of the database, we do this.
	 * It does not perform a 'commit' because if we chose to do so we could start a roll forward recovery and restore
	 * even the old data before the checkpoint.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	void transactionCheckpoint() throws IOException, IllegalAccessException;

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

	/**
	 * Take a check point of our current indicies. What this means is that we are
	 * going to write a log record such that if we crash will will restore the logs from that point forward.
	 * We have to have confidence that we are doing this at a legitimate point, so this should only be called if things are well
	 * and processing is proceeding normally. Its a way to say "start from here and go forward in time 
	 * if we crash, to restore the data to its state up to that point", hence check, point...
	 * If we are loading lots of data and we want to partially confirm it as part of the database, we do this.
	 * It does not perform a 'commit' because if we chose to do so we could start a roll forward recovery and restore
	 * even the old data before the checkpoint.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	void transactionCheckpoint(Class clazz) throws IOException, IllegalAccessException;

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

	/**
	 * Store our k/v
	 * This is a transactional store in the context of a previously initiated transaction.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation
	 * @param d The Comparable representing the key object for this morphism relationship.
	 * @param m The value object
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 * @throws DuplicateKeyException 
	 */
	Object transactionalStore(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException;

	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	void transactionCommit(Class clazz) throws IOException;

	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	void transactionRollback(Class clazz) throws IOException;


}