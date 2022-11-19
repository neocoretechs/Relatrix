package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;

public interface RelatrixKVClientTransactionInterface {

	String getLocalNode();

	String getRemoteNode();

	int getRemotePort();

	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	void send(RemoteRequestInterface iori);

	void close();

	/**
	 * Call the remote server method to send a manually constructed command
	 * @param rs The RelatrixKvStatement manually constructed
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	Object sendCommand(RelatrixStatementInterface rs) throws IllegalAccessException, IOException, DuplicateKeyException;

	/**
	 * Call the remote server method to store an object.
	 * @param k The Comparable representing the key relationship
	 * @param v The value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	Object store(String xid, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException;

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
	Object transactionalStore(String xid, Comparable k, Object v)
			throws IllegalAccessException, IOException, DuplicateKeyException;

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

	UUID getNewKey(String xid) throws ClassNotFoundException, IllegalAccessException, IOException;

	void transactionCommit(String xid) throws IOException;

	void transactionRollback(String xid) throws IOException;

	void transactionCheckpoint(String xid) throws IOException, IllegalAccessException;

	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	*/
	Object remove(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object firstValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Get the keyed value
	 * @param key The Comparable key
	 * @return The value for the given key, or null if not found
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	Object get(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException;

	Object lastValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable firstKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	Comparable lastKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	long size(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException;

	boolean contains(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException;

	boolean containsValue(String xid, Class keyType, Object value)
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
	RemoteTailMapIterator findTailMap(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findTailMapSteam(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteEntrySetIterator entrySet(String xid, Class clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream entrySetStream(String xid, Class clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteKeySetIterator keySet(String xid, Class clazz)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream keySetStream(String xid, Class clazz)
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
	RemoteTailMapKVIterator findTailMapKV(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findTailMapKVStream(String xid, Comparable key)
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
	RemoteHeadMapIterator findHeadMap(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findHeadMapStream(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed.
	 * @param darg Domain of morphism
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	RemoteHeadMapKVIterator findHeadMapKV(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findHeadMapKVStream(String xid, Comparable key)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
	 * arguments to the ending point of the associated variable number of parameters, which must match the number of objects
	 * passed in the first three arguments. If a passed argument in the first 3 parameters is neither "*" (wildcard)
	 * or "?" (return the object from the retrieved tuple morphism) then it is presumed to be an object.
	 * Semantically, this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed.
	 * @param darg The domain of the relationship to retrieve
	 * @param marg The map of the relationship to retrieve
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	RemoteSubMapIterator findSubMap(String xid, Comparable key1, Comparable key2)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findSubMapStream(String xid, Comparable key1, Comparable key2)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
	 * arguments to the ending point of the associated variable number of parameters, which must match the number of objects
	 * passed in the first three arguments. If a passed argument in the first 3 parameters is neither "*" (wildcard)
	 * or "?" (return the object from the retrieved tuple morphism) then it is presumed to be an object.
	 * Semantically, this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed.
	 * @param darg The domain of the relationship to retrieve
	 * @param marg The map of the relationship to retrieve
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	RemoteSubMapKVIterator findSubMapKV(String xid, Comparable key1, Comparable key2)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	RemoteStream findSubMapKVStream(String xid, Comparable key1, Comparable key2)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Load a class into the handlerclassloader from remote repository via jar file
	 * @param jar
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	Object loadClassFromJar(String xid, String jar) throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Load a class in to handlerclassloader via package and directory path.
	 * @param pack The package designation
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	Object loadClassFromPath(String xid, String pack, String path)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Remove package in handlerclassloader and from repository.
	 * @param pack The package designation, everything starting with this descriptor will be removed
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	Object removePackageFromRepository(String xid, String pack)
			throws IOException, ClassNotFoundException, IllegalAccessException;

	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return Object of iteration, depends on iterator being used, typically, Map.Entry derived serializable instance of next element
	 */
	Object next(String xid, RemoteObjectInterface rii) throws NoSuchElementException;

	boolean hasNext(String xid, RemoteObjectInterface rii);

	void remove(String xid, RemoteObjectInterface rii) throws UnsupportedOperationException, IllegalStateException;

	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	void close(String xid, RemoteObjectInterface rii);

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

	String getTransactionId(Class clazz) throws ClassNotFoundException, IllegalAccessException, IOException;

}