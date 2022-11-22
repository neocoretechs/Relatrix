package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DomainMapRangeTransaction;
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
	Object transactionalStore(String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException;

	void transactionalStore(String xid, Comparable<?> index, Object instance) throws IllegalAccessException, IOException, DuplicateKeyException;
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

	Object firstValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object get(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable lastKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object lastValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	RemoteKeySetIterator keySet(String xid, Class<String> clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Delete specific relationship and all relationships that it participates in
	 * @param d
	 * @param m
	 * @param r
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 */
	Object remove(String xid, Comparable d, Comparable m, Comparable r)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	* Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	* matching the given set of operators and/or objects. Essentially this is the default permutation which
	* retrieves the equivalent of a tailSet and the parameters can be objects and/or operators. Semantically,
	* the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	* In support of the typed lambda calculus, When presented with 3 objects, the options are to return an identity composed of those 3 or
	* a set composed of identity elements matching the class of the template(s) in the argument(s)
	* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
	* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
	* the requirement to be 'categorical'. In general, all 3 element arraysw return by the Cat->set representable operators are
	* the mathematical identity, or constitute the unique key in database terms.
	* @param darg Object for domain of relationship or a class template
	* @param marg Object for the map of relationship or a class template
	* @param rarg Object for the range of the relationship or a class template
	* @exception IOException low-level access or problems modifiying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	*/
	RemoteTailSetIteratorTransaction findSet(String xid, Object darg, Object marg, Object rarg)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findSetStream(String xid, Object darg, Object marg, Object rarg)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	* Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	* matching the given set of operators and/or objects.
	* The parameters can be objects and/or operators. Semantically,
	* this set-based retrieval makes no sense without at least one object to supply a value to
	* work against, so in this method that check is performed.
	* In support of the typed lambda calculus, When presented with 3 objects, the options are to return a
	* a set composed of elements matching the class of the template(s) in the argument(s)
	* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
	* @param darg Object for domain of relationship or a class template
	* @param marg Object for the map of relationship or a class template
	* @param rarg Object for the range of the relationship or a class template
	* @exception IOException low-level access or problems modifiying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	*/
	RemoteTailSetIterator findTailSet(String xid, Object darg, Object marg, Object rarg)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findTailSetStream(String xid, Object darg, Object marg, Object rarg)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed.
	 * @param darg Domain of morphism
	 * @param marg Map of morphism relationship
	 * @param rarg Range or codomain or morphism relationship
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	RemoteHeadSetIterator findHeadSet(String xid, Object darg, Object marg, Object rarg)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findHeadSetStream(String xid, Object darg, Object marg, Object rarg)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return
	 */
	Object next(String xid, RemoteObjectInterface rii) throws NoSuchElementException;

	boolean hasNext(String xid, RemoteObjectInterface rii);

	void close();

	/**
	 * Get the last good DBKey from the DBKey table, which is the highest numbered last key delivered.
	 * @param transactionId 
	 * @return The last good key
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	UUID getNewKey(String transactionId) throws ClassNotFoundException, IllegalAccessException, IOException;

	int getRemotePort();

	String getRemoteNode();

	String getLocalNode();

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

	String getTransactionId() throws ClassNotFoundException, IllegalAccessException, IOException;

	String endTransaction(String xid) throws ClassNotFoundException, IllegalAccessException, IOException;

	
}