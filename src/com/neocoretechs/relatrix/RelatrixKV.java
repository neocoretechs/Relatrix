package com.neocoretechs.relatrix;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.bigsack.btree.TreeSearchResult;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;

import com.neocoretechs.relatrix.server.HandlerClassLoader;


/**
* Top-level class that imparts behavior to the Key/Value subclasses which contain references for key/value.

* The compareTo and fullCompareTo provide the comparison methods to drive the processes.
* The retrieval operators allow us to form the partially ordered result sets that are returned.<br/>
* @author jg, Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020
*/
public final class RelatrixKV {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	
	public static char OPERATOR_WILDCARD_CHAR = '*';
	public static char OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	private static final int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED; 

	/**
	* Calling these methods allows the user to substitute their own
	* symbology for the usual Findset semantics. If you absolutely
	* need to store values confusing to the standard findset *,? semantics.
	* TODO: In k/v context, are these relevant?
	* */
	public static void setWildcard(char wc) {
		OPERATOR_WILDCARD_CHAR = wc;
		OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	}
	public static void setTuple(char tp) {
		OPERATOR_TUPLE_CHAR = tp;
		OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	}
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static synchronized void setTablespaceDirectory(String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set tablespace directory for fileset "+path+" to allocate persistent storage.");
		BigSackAdapter.setTableSpaceDir(path);
	}
	
	public static synchronized String getTableSpaceDirectory() {
		return BigSackAdapter.getTableSpaceDir();
	}
	/**
	 * We cant reasonably check the validity. Set the path to the remote directory that contains the
	 * BigSack tablespaces that comprise our database on remote nodes.
	 * @param path
	 * @throws IOException
	 */
	public static synchronized void setRemoteDirectory(String path) {
		BigSackAdapter.setRemoteDir(path);
	}
	
	public static synchronized String getRemoteDirectory() {
		return BigSackAdapter.getRemoteDir();
	}

/**
 * Store our permutations of the key/value
 * This is a transactional store in the context of a previously initiated transaction.
 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
 * to prevent a recovery on the next operation.
 * @param key of comparable
 * @param value
 * @throws IllegalAccessException
 * @throws IOException
 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
 */
public static synchronized void transactionalStore(Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(key);
	if( DEBUG  )
		System.out.println("RelatrixKV.transactionalStore storing dmr:"+key+"/"+value);
	TreeSearchResult tsr = ttm.locate(key);
	if( DEBUG )
		System.out.println("RelatrixKV.store Tree Search Result: "+tsr);
	if(tsr.atKey) {
			throw new DuplicateKeyException(key);
	}
	ttm.put(key, value);
}
/**
 * Commit the outstanding transaction data in each active transactional treeset.
 * @throws IOException
 */
public static synchronized void transactionCommit(Class clazz) throws IOException {
	long startTime = System.currentTimeMillis();
	BigSackAdapter.commitMap(clazz);
		if( DEBUG || TRACE )
			System.out.println("Committed treeSet in " + (System.currentTimeMillis() - startTime) + "ms.");		
}
/**
 * Roll back all outstanding transactions on the indicies
 * @throws IOException
 */
public static synchronized void transactionRollback(Class clazz) throws IOException {
	BigSackAdapter.rollbackMap(clazz);
}
/**
 * Take a check point of our current indicies. What this means is that we are
 * going to write a log record such that if we crash will will restore the logs from that point forward.
 * We have to have confidence that we are doing this at a legitimate point, so this should only be called if things are well
 * and processing is proceeding normally. Its a way to say "start from here and go forward in time 
 * if we crash, to restore the data to its state up to that point", hence check, point...
 * If we are loading lots of data and we want to partially confirm it as part of the database, we do this.
 * It does not perform a 'commit' because if we chose to do so we could start a roll forward recovery and restore
 * even the old data before the checkpoint.
 * @param clazz The class for which the map has been created.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized void transactionCheckpoint(Class clazz) throws IOException, IllegalAccessException {
	BigSackAdapter.checkpointMapTransactions(clazz);
}
/**
 * Load the stated package from the declared path into the bytecode repository
 * @param pack
 * @param path
 * @throws IOException
 */
public static synchronized void loadClassFromPath(String pack, String path) throws IOException {
	Path p = FileSystems.getDefault().getPath(path);
	HandlerClassLoader.setBytesInRepository(pack,p);
}
/**
 * Load the jar file located at jar into the repository
 * @param jar
 * @throws IOException
 */
public static synchronized void loadClassFromJar(String jar) throws IOException {
	HandlerClassLoader.setBytesInRepositoryFromJar(jar);
}
/**
 * Remove the stated package from the declared package and all subpackages from the bytecode repository
 * @param pack
 * @param path
 * @throws IOException
 */
public static synchronized void removePackageFromRepository(String pack) throws IOException {
	HandlerClassLoader.removeBytesInRepository(pack);
}
/**
* Delete element with given key that this object participates in
* @param c The Comparable key
* @exception IOException low-level access or problems modifying schema
* @throws IllegalAccessException 
* @throws ClassNotFoundException 
* @throws IllegalArgumentException 
*/
public static synchronized void remove(Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(c);
	if( DEBUG || DEBUGREMOVE )
		System.out.println("RelatrixKV.remove prepping to remove:"+c);
		ttm.remove(c);
	if( DEBUG || DEBUGREMOVE )
		System.out.println("Relatrix.remove exiting remove for key:"+c+" should have removed"+c);
}

/**
* Retrieve from the targeted relationship. Essentially this is the default permutation which
* retrieves the equivalent of a tailSet and returns the value elements
* @param darg Object marking start of retrieval
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException the operator is invalid
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
*/
public static synchronized Iterator<?> findTailMap(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	return ttm.tailMap(darg);
}
/**
* Retrieve from the targeted relationship. Essentially this is the default permutation which
* retrieves the equivalent of a tailSet and returns the value elements
* @param darg Comparable marking start of retrieval
* @param parallel optional true to execute parallel stream
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException the operator is invalid
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Comparable[]>
*/
public static synchronized Stream<?> findTailMapStream(Comparable darg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMap(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}

/**
* Retrieve from the targeted Key/Value relationship from given key.
* Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
* @param darg Object for key of relationship
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException At least one argument must be a valid object reference
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
*/
public static synchronized Iterator<?> findTailMapKV(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	return ttm.tailMapKV(darg);
}
/**
* Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
* @param darg Comparable for key
* @param parallel Optional true to execute parallel stream
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Comparable[]>
*/
public static synchronized Stream<?> findTailMapKVStream(Comparable darg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMapKV(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * Retrieve the given set of values from the start of the elements to the given key.
 * @param darg The Comparable key
 * @throws IllegalArgumentException At least one argument must be a valid object reference
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return The Iterator from which data may be retrieved. Fulfills Iterator interface.
 */
public static synchronized Iterator<?> findHeadMap(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	// check for at least one object reference in our headset factory
	return ttm.headMap(darg);
}
/**
 * Retrieve the given set of values from the start of the elements to the given key.
 * @param darg Comparable key
 * @param parallel Optional true to execute parallel stream
 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return Stream from which data may be consumed. Fulfills Stream interface.
 */
public static synchronized Stream<?> findHeadMapStream(Comparable darg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	// check for at least one object reference in our headset factory
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMap(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
 * @param darg The comparable key
 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
 */
public static synchronized Iterator<?> findHeadMapKV(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	// check for at least one object reference in our headset factory
	return ttm.headMapKV(darg);
}
/**
 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
 * @param darg Comparable key
 * @param parallel true for parallel stream
 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
 */
public static synchronized Stream<?> findHeadMapKVStream(Comparable darg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	// check for at least one object reference in our headset factory
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMapKV(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
 * @param darg The starting key
 * @param marg The ending key
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return Iterator from which data may be retrieved. Fulfills Iterator interface.
 */
public static synchronized Iterator<?> findSubMap(Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	return ttm.subMap(darg, marg);
}
/**
 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
 * @param darg The starting key
 * @param marg The ending key
 * @param parallel Optional true for parallel stream execution
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return Stream from which data may be retrieved. Fulfills Stream interface.
 */
public static synchronized Stream<?> findSubMapStream(Comparable darg, Comparable marg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMap(darg, marg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
 * @param darg The starting key
 * @param marg The ending key
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
 */
public static synchronized Iterator<?> findSubMapKV(Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	return ttm.subMapKV(darg, marg);
}
/**
 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
 * @param darg The starting key
 * @param marg The ending key
 * @param parallel Optional true for parallel stream execution
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Sterator<Comparable[]>
 */
public static synchronized Stream<?> findSubMapKVStream(Comparable darg, Comparable marg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMapKV(darg, marg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * Return the entry set for the given class type
 * @param clazz the class to retrieve
 * @return Iterator for entry set
 * @throws IOException
 * @throws IllegalAccessException
 */
public static synchronized Iterator<?> entrySet(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.entrySet();
}

/**
 * Return the entry set for the given class type
 * @param clazz the class to retrieve
 * @param Optional true for parallel stream execution
 * @return Stream for entry set
 * @throws IOException
 * @throws IllegalAccessException
 */
public static synchronized Stream<?> entrySetStream(Class clazz, boolean... parallel) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.entrySet(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * Return the keyset for the given class
 * @param clazz the class to retrieve
 * @return the iterator for the keyset
 * @throws IOException
 * @throws IllegalAccessException
 */
public static synchronized Iterator<?> keySet(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.keySet();
}
/**
 * Return the keyset for the given class
 * @param clazz the class to retrieve
 * @param parallel Optional true for parallel stream
 * @return The stream from which keyset can be consumed
 * @throws IOException
 * @throws IllegalAccessException
 */
public static synchronized Stream<?> keySetStream(Class clazz, boolean... parallel) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.keySet(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
}
/**
 * return lowest valued key.
 * @param clazz the class to retrieve
 * @return the The key/value with lowest key value.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized Object firstKey(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.firstKey();
}
/**
 * Return the value for the key.
 * @param key the key to retrieve
 * @return The value for the key.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized Object get(Comparable key) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(key);
	return ttm.get(key);
}
/**
 * The lowest key value object
 * @param clazz the class to retrieve
 * @return The first value of the class with given key
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized Object firstValue(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.first();
}
/**
 * Return instance having the highest valued key.
 * @param clazz the class to retrieve
 * @return the The highest value object
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized Object lastKey(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.lastKey();
}
/**
 * Return the instance having the highest valued key.
 * @param clazz the class to retrieve
 * @return the DomainMapRange morphism having the highest key value.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized Object lastValue(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.last();
}
/**
 * Size of all elements
 * @param clazz the class to retrieve
 * @return the number of DomainMapRange morphisms.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized long size(Class clazz) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(clazz);
	return ttm.size();
}
/**
 * Is the key contained in the dataset
 * @parameter obj The Comparable key to search for
 * @return true if key is found
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized boolean contains(Comparable obj) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(obj);
	return ttm.containsKey(obj);
}
/**
 * Is the value object present
 * @param keyType the class to retrieve
 * @param obj the object with equals, CAUTION explicit conversion is needed
 * @return boolean true if found
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized boolean containsValue(Class keyType, Object obj) throws IOException, IllegalAccessException
{
	TransactionalTreeMap ttm = BigSackAdapter.getBigSackMapTransaction(keyType);
	return ttm.containsValue(obj);
}

}

