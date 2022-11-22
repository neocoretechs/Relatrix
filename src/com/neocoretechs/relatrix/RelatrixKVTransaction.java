package com.neocoretechs.relatrix;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.session.RockSackAdapter;
import com.neocoretechs.rocksack.session.TransactionalMap;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
* Top-level class that imparts behavior to the Key/Value subclasses which contain references for key/value.

* The compareTo and fullCompareTo provide the comparison methods to drive the processes.
* The retrieval operators allow us to form the partially ordered result sets that are returned.<br/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021,2022
*/
public final class RelatrixKVTransaction {
	private static boolean DEBUG = true;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	
	private static final int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED; 
	
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespaceDirectory(String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set tablespace directory for fileset "+path+" to allocate persistent storage.");
		RockSackAdapter.setTableSpaceDir(path);
	}
	
	public static String getTableSpaceDirectory() {
		return RockSackAdapter.getTableSpaceDir();
	}
	
	/**
	 * For remote client calls, we are passing the string representation of class to provide
	 * a serializable wire friendly instance.
	 * @param key
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static String getTransactionId() throws IllegalAccessException, IOException, ClassNotFoundException {
		return RockSackAdapter.getRockSackTransactionId();
	}
	
	/**
	 * @param xid
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void endTransaction(String xid) throws IllegalAccessException, IOException, ClassNotFoundException {
		RockSackAdapter.removeRockSackTransactionalMap(xid);
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
	 */
	public static void transactionalStore(String xid, Comparable key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key.getClass(), xid);
		if( DEBUG  )
			System.out.println("RelatrixKVTransaction.transactionalStore storing dmr:"+key+"/"+value);
		ttm.put(key, value);
		if( DEBUG )
			System.out.println("RelatrixKVTransaction.store Tree Search Result: ");

	}
	/**
	 * Commit the outstanding transaction data in each active transaction.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void transactionCommit(String xid, Class clazz) throws IOException, IllegalAccessException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.getRockSackTransactionalMap(clazz, xid).Commit();
		if( DEBUG || TRACE )
			System.out.println("Committed "+clazz+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void transactionRollback(String xid, Class clazz) throws IOException, IllegalAccessException {
		RockSackAdapter.getRockSackTransactionalMap(clazz, xid).Rollback();
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
	public static void transactionCheckpoint(String xid, Class clazz) throws IOException, IllegalAccessException {
		RockSackAdapter.getRockSackTransactionalMap(clazz, xid).Checkpoint();
	}

	/**
	 * Load the stated package from the declared path into the bytecode repository
	 * @param pack
	 * @param path
	 * @throws IOException
	 */
	public static void loadClassFromPath(String pack, String path) throws IOException {
		Path p = FileSystems.getDefault().getPath(path);
		HandlerClassLoader.setBytesInRepository(pack,p);
	}
	/**
	 * Load the jar file located at jar into the repository
	 * @param jar
	 * @throws IOException
	 */
	public static void loadClassFromJar(String jar) throws IOException {
		HandlerClassLoader.setBytesInRepositoryFromJar(jar);
	}
	/**
	 * Remove the stated package from the declared package and all subpackages from the bytecode repository
	 * @param pack
	 * @param path
	 * @throws IOException
	 */
	public static void removePackageFromRepository(String pack) throws IOException {
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
	public static void remove(String xid, Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(c.getClass(), xid);
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
	public static Iterator<?> findTailMap(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		return ttm.tailMap(darg);
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param darg Comparable marking start of retrieval
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Comparable[]>
	 */
	public static Stream<?> findTailMapStream(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMap(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	public static Iterator<?> findTailMapKV(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
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
	public static Stream<?> findTailMapKVStream(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMapKV(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param darg The Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findHeadMap(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		return ttm.headMap(darg);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param darg Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which data may be consumed. Fulfills Stream interface.
	 */
	public static Stream<?> findHeadMapStream(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMap(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param darg The comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findHeadMapKV(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
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
	public static Stream<?> findHeadMapKVStream(String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMapKV(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	public static Iterator<?> findSubMap(String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		return ttm.subMap(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which data may be retrieved. Fulfills Stream interface.
	 */
	public static Stream<?> findSubMapStream(String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMap(darg, marg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	public static Iterator<?> findSubMapKV(String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// check for at least one object reference
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		return ttm.subMapKV(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Sterator<Comparable[]>
	 */
	public static Stream<?> findSubMapKVStream(String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// check for at least one object reference
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMapKV(darg, marg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Iterator for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Iterator<?> entrySet(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.entrySet();
	}
	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Stream<?> entrySetStream(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.entrySet(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true); //true = parallel
	}
	/**
	 * Return the keyset for the given class
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Iterator<?> keySet(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.keySet();
	}
	/**
	 * Return the keyset for the given class
	 * @param clazz the class to retrieve
	 * @return The stream from which keyset can be consumed
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Stream<?> keySetStream(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.keySet(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * return lowest valued key.
	 * @param clazz the class to retrieve
	 * @return the The key/value with lowest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object firstKey(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.firstKey();
	}
	/**
	 * Return the value for the key.
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object get(String xid, Comparable key) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key.getClass(), xid);
		Object o = ttm.get(key);
		if( o == null )
			return null;
		return ((KeyValue)o).getmValue();
	}
	/**
	 * The lowest key value object
	 * @param clazz the class to retrieve
	 * @return The first value of the class with given key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object firstValue(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.first();
	}
	/**
	 *	 Return instance having the highest valued key.
	 * @param clazz the class to retrieve
	 * @return the The highest value object
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object lastKey(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.lastKey();
	}
	/**
	 * Return the instance having the value for  the greatest key.
	 * @param clazz the class to retrieve
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object lastValue(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.last();
	}
	/**
	 * Size of all elements
	 * @param clazz the class to retrieve
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static long size(String xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz, xid);
		return ttm.size();
	}
	/**
	 * Is the key contained in the dataset
	 * @parameter obj The Comparable key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static boolean contains(String xid, Comparable obj) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(obj.getClass(), xid);
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
	public static boolean containsValue(String xid, Class keyType, Object obj) throws IOException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(keyType, xid);
		return ttm.containsValue(obj);
	}


}
