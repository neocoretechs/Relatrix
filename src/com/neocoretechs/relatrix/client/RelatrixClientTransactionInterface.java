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
public interface RelatrixClientTransactionInterface {

	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	void transactionCommit(String xid) throws IOException;

	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	void transactionRollback(String xid) throws IOException;

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
	void transactionCheckpoint(String xid) throws IOException, IllegalAccessException;


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
	void transactionCheckpoint(String xid, Class clazz) throws IOException, IllegalAccessException;

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
	Object transactionalStore(String xid, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException;

	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	void transactionCommit(String xid, Class clazz) throws IOException;

	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	void transactionRollback(String xid, Class clazz) throws IOException;

	Comparable firstKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object store(String xid, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException;

	RemoteStream entrySetStream(String xid, Class<?> clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	Object sendCommand(RelatrixStatementInterface rs) throws DuplicateKeyException, IllegalAccessException, IOException;

	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	void send(RemoteRequestInterface iori);

	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	* @throws ClassNotFoundException 
	* @throws IllegalAccessException 
	*/
	Object remove(String xid, Comparable c) throws IOException, ClassNotFoundException, IllegalAccessException;


}