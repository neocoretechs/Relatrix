package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.key.DBKey;

/**
 * Defines the contract for client side communications with remote RelatrixTransaction and RelatrixKVTransaction server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RelatrixClientTransactionInterface {
	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	void send(RemoteRequestInterface iori);
	
	int getRemotePort();

	String getRemoteNode();

	String getLocalNode();
	
	void close();
	
	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	* @throws ClassNotFoundException 
	* @throws IllegalAccessException 
	*/
	Object remove(String xid, Comparable c) throws IOException, ClassNotFoundException, IllegalAccessException;
	
	boolean hasNext(String xid, RemoteObjectInterface rii);
	
	RemoteStream entrySetStream(String xid, Class<?> clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;
	
	RemoteKeySetIteratorTransaction keySet(String xid, Class<String> clazz) throws IOException, ClassNotFoundException, IllegalAccessException;
	
	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return
	 */
	Object next(String xid, RemoteObjectInterface rii) throws NoSuchElementException;

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

	void remove(String xid, RemoteObjectInterface rii) throws UnsupportedOperationException, IllegalStateException;

	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	void close(String xid, RemoteObjectInterface rii);
	
	Comparable firstKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object sendCommand(RelatrixStatementInterface rs) throws DuplicateKeyException, IllegalAccessException, IOException;

	Object firstValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;
	
	Object get(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable lastKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object lastValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	void commit(String xid) throws IOException;

	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	void rollback(String xid) throws IOException;

	/**
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	void checkpoint(String xid) throws IOException, IllegalAccessException;

	void rollbackToCheckpoint(String xid) throws IOException, IllegalAccessException;

	/**
	 * Get the last good DBKey from the DBKey table, which is the highest numbered last key delivered.
	 * @param transactionId 
	 * @return The last good key
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	UUID getNewKey(String transactionId) throws ClassNotFoundException, IllegalAccessException, IOException;

	String getTransactionId() throws ClassNotFoundException, IllegalAccessException, IOException;

	String endTransaction(String xid) throws ClassNotFoundException, IllegalAccessException, IOException;
	
	// Alias methods

	Object get(String alias, String transactionId, Comparable instance) throws IllegalAccessException, IOException, NoSuchElementException;

	Object remove(String alias, String transactionId, Comparable instance) throws IOException, NoSuchElementException;
	
	void storekv(String xid, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException;
	
	void storekv(String alias, String transactionId, Comparable<?> index, Object instance) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException;

	Object getByIndex(String alias, String transactionId, DBKey index) throws IllegalAccessException, IOException, NoSuchElementException;

	void rollbackToCheckpoint(String alias, String xid) throws IOException, IllegalAccessException;

	void rollback(String alias, String xid) throws IOException;

	void commit(String alias, String xid) throws IOException;

	void checkpoint(String alias, String xid) throws IOException, IllegalAccessException;

	Object firstValue(String alias, String xid, Class clazz)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	Object lastValue(String alias, String xid, Class clazz)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable firstKey(String alias, String xid, Class clazz)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable lastKey(String alias, String xid, Class clazz)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	RemoteStream entrySetStream(String alias, String xid, Class clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteKeySetIteratorTransaction keySet(String alias, String xid, Class clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;


	
}