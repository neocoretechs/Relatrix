package com.neocoretechs.relatrix;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.session.RockSackAdapter;
import com.neocoretechs.rocksack.session.TransactionalMap;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
* Top-level class that imparts behavior to the Key/Value subclasses which contain references for key/value.
* The methods here are all performed in a transaction context and require a transaction id.
* The transaction id is returned through a method call to the RockSackAdapter that returns a standard UUID.
* The compareTo and fullCompareTo provide the comparison methods to drive the processes.
* The retrieval operators allow us to form the partially ordered result sets that are returned.<br/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021,2022,2023
*/
public final class RelatrixKVTransaction {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	
	private static final int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED; 
	
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespace(String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set tablespace directory for fileset "+path+" to allocate persistent storage.");
		RockSackAdapter.setTableSpaceDir(path);
	}
	
	/**
	 * Get the default tablespace directory
	 * @return the path/dbname of current default tablespace
	 */
	public static String getTableSpace() {
		return RockSackAdapter.getTableSpaceDir();
	}
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @param path
	 * @throws IOException
	 */
	public static void setAlias(String alias, String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set alias for tablespace directory using fileset "+path+" to allocate persistent storage.");
		RockSackAdapter.setTableSpaceDir(alias, path);
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	public static void removeAlias(String alias) throws NoSuchElementException {
		RockSackAdapter.removeAlias(alias);
	}
	
	/**
	 * Will return null if alias does not exist
	 * @param alias
	 * @return
	 */
	public static String getAlias(String alias) {
		return RockSackAdapter.getTableSpaceDir(alias);
	}
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	public static String[][] getAliases() {
		return RockSackAdapter.getAliases();
	}
	/**
	 * @return the transaction id
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static String getTransactionId() throws IllegalAccessException, IOException, ClassNotFoundException {
		String xid = RockSackAdapter.getRockSackTransactionId();
		IndexResolver.setAlias(null);
		IndexResolver.setIndexInstanceTable(xid);
		return xid;
	}
	
	/**
	 * @param xid the transaction id
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void endTransaction(String xid) throws IllegalAccessException, IOException, ClassNotFoundException {
		RockSackAdapter.removeRockSackTransactionalMap(xid);
		IndexResolver.remove(xid);
	}	
	/**
	 * Get the new DBkey.
	 * @return The UUID of new key
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 */
	public static synchronized UUID getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID nkey = UUID.randomUUID();
		if(DEBUG)
			System.out.printf("Returning NewKey=%s%n", nkey.toString());
		return nkey;
	}
	/**
	 * Store our permutations of the key/value
	 * This is a transactional store in the context of a previously initiated transaction.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation.
	 * @param xid the transaction id
	 * @param key of comparable
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static void store(String xid, Comparable key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key.getClass(), xid);
		if( DEBUG  )
			System.out.println("RelatrixKVTransaction.transactionalStore Id:"+xid+" storing key:"+key+" value:"+value);
		ttm.put(key, value);
	}
	/**
	 * Store our permutations of the key/value
	 * This is a transactional store in the context of a previously initiated transaction.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation.
	 * @param alias database alias
	 * @param xid the transaction id
	 * @param key of comparable
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws NoSuchElementException If alias not found
	 */
	public static void store(String alias, String xid, Comparable key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, key.getClass(), xid);
		if( DEBUG  )
			System.out.println("RelatrixKVTransaction.transactionalStore Id:"+xid+" storing key:"+key+" value:"+value);
		ttm.put(key, value);
	}
	/**
	 * Commit the outstanding transaction data in each active class.
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void commit(String xid) throws IOException, IllegalAccessException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.commitRockSackTransaction(xid);
		if( DEBUG || TRACE )
			System.out.println("Committed transaction:"+xid+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Commit the outstanding transaction data in each active class for database at alias.
	 * @param alias database alias
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void commit(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.commitRockSackTransaction(alias, xid);
		if( DEBUG || TRACE )
			System.out.println("Committed transaction:"+xid+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Commit the outstanding transaction data in specified class.
	 * @param xid transaction id
	 * @param clazz class to commit
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void commit(String xid, Class clazz) throws IOException, IllegalAccessException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.getRockSackTransactionalMap(clazz, xid).Commit();
		if( DEBUG || TRACE )
			System.out.println("Committed "+clazz+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Commit the outstanding transaction data in specified class.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @param clazz class to commit
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if alias not found
	 */
	public static void commit(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.getRockSackTransactionalMap(clazz, xid).Commit();
		if( DEBUG || TRACE )
			System.out.println("Committed "+clazz+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Rollback the outstanding transaction data in each active class.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void rollback(String xid) throws IOException, IllegalAccessException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.rollbackRockSackTransaction(xid);
		if( DEBUG || TRACE )
			System.out.println("Rolled back transaction:"+xid+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Rollback the outstanding transaction data in each active class.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if alias not found
	 */
	public static void rollback(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		long startTime = System.currentTimeMillis();
		RockSackAdapter.rollbackRockSackTransaction(alias, xid);
		if( DEBUG || TRACE )
			System.out.println("Rolled back transaction:"+xid+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Roll back all outstanding transactions on the indicies
	 * @param xid the transaction id
	 * @param clazz the class to roll back
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void rollback(String xid, Class clazz) throws IOException, IllegalAccessException {
		RockSackAdapter.getRockSackTransactionalMap(clazz, xid).Rollback();
	}
	/**
	 * Roll back all outstanding transactions on the indicies
	 * @param alias the alias of the database
	 * @param xid the transaction id
	 * @param clazz the class to roll back
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static void rollback(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid).Rollback();
	}
	/**
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void checkpoint(String xid) throws IOException, IllegalAccessException {
		RockSackAdapter.checkpointRockSackTransaction(xid);
	}
	/**
	 * @param alias the database alias
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	public static void checkpoint(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RockSackAdapter.checkpointRockSackTransaction(alias, xid);
	}
	/**
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static void rollbackToCheckpoint(String xid) throws IOException, IllegalAccessException {
		RockSackAdapter.rollbackToCheckpoint(xid);
	}
	/**
	 * @param alias the database alias
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException If the alias was not found
	 */
	public static void rollbackToCheckpoint(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RockSackAdapter.rollbackToCheckpoint(alias, xid);
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
	 * @param xid the transaction id
	 * @param c The Comparable key
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static void remove(String xid, Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(c.getClass(), xid);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVTransaction.remove prepping to remove:"+c);
		ttm.remove(c);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVTransaction.remove exiting remove for key:"+c+" should have removed"+c);
	}
	/**
	 * Delete element with given key that this object participates in
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param c The Comparable key
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static void remove(String alias, String xid, Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, c.getClass(), xid);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVTransaction.remove prepping to remove:"+c);
		ttm.remove(c);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVTransaction.remove exiting remove for key:"+c+" should have removed"+c);
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param xid the transaction id
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
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static Iterator<?> findTailMap(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		return ttm.tailMap(darg);
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param xid the transaction ID
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
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param alias the database alias
	 * @param xid the transaction ID
	 * @param darg Comparable marking start of retrieval
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Comparable[]>
	 */
	public static Stream<?> findTailMapStream(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMap(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param xid the transaction ID
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
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param alias the database alias
	 * @param xid the transaction ID
	 * @param darg Object for key of relationship
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static Iterator<?> findTailMapKV(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		return ttm.tailMapKV(darg);
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param xid the transaction ID
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
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param alias the database alias
	 * @param xid the transaction ID
	 * @param darg Comparable for key
	 * @param parallel Optional true to execute parallel stream
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Comparable[]>
	 */
	public static Stream<?> findTailMapKVStream(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMapKV(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param xid the transaction id
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
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findHeadMap(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		return ttm.headMap(darg);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param xid the transaction id
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
	 * Retrieve the given set of values from the start of the elements to the given key as a stream.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return Stream from which data may be consumed. Fulfills Stream interface.
	 */
	public static Stream<?> findHeadMapStream(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMap(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findHeadMapKV(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		return ttm.headMapKV(darg);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param xid the transaction id
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
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param alaias the database alias
	 * @param xid the transaction id
	 * @param darg Comparable key
	 * @param parallel true for parallel stream
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
	 */
	public static Stream<?> findHeadMapKVStream(String alias, String xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		// check for at least one object reference in our headset factory
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMapKV(darg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
	 * @param xid the transaction id
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
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findSubMap(String alias, String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		return ttm.subMap(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first
	 * @param xid the transaction id 
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
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first
	 * @param alias the database alias
	 * @param xid the transaction id 
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias was not found
	 * @return Stream from which data may be retrieved. Fulfills Stream interface.
	 */
	public static Stream<?> findSubMapStream(String alias, String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMap(darg, marg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
	 * @param xid the transaction id
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
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static Iterator<?> findSubMapKV(String alias, String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// check for at least one object reference
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		return ttm.subMapKV(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
	 * @param xid the transaction id
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
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Sterator<Comparable[]>
	 */
	public static Stream<?> findSubMapKVStream(String alias, String xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// check for at least one object reference
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, darg.getClass(), xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMapKV(darg, marg), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * Return the entry set for the given class type
	 * @param xid the transaction id
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
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return Iterator for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Iterator<?> entrySet(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.entrySet();
	}
	/**
	 * Return the entry set for the given class type
	 * @param xid the transaction id
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
	 * Return the entry set for the given class type
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Stream<?> entrySetStream(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.entrySet(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true); //true = parallel
	}
	/**
	 * Return the keyset for the given class
	 * @param xid the transaction id
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
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	public static Iterator<?> keySet(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.keySet();
	}
	/**
	 * Return the keyset for the given class
	 * @param xid the transaction id
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
	 * Return the keyset for the given class
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return The stream from which keyset can be consumed
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Stream<?> keySetStream(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.keySet(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * return lowest valued key.
	 * @param xid the transaction id
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
	 * return lowest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the The key/value with lowest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Object firstKey(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.firstKey();
	}
	/**
	 * Return the value for the key.
	 * @param xid the transaction id
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
	 * Return the value for the key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Object get(String alias, String xid, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, key.getClass(), xid);
		Object o = ttm.get(key);
		if( o == null )
			return null;
		return ((KeyValue)o).getmValue();
	}
	/**
	 * The lowest key value object
	 * @param xid the transaction id
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
	 * The lowest key value object
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return The first value of the class with given key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Object firstValue(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.first();
	}
	/**
	 * Return instance having the highest valued key.
	 * @param xid the transaction id
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
	 * Return instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the The highest value object
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Object lastKey(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.lastKey();
	}
	/**
	 * Return the instance having the value for the greatest key.
	 * @param xid the transaction id
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
	 * Return the instance having the value for the greatest key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static Object lastValue(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.last();
	}
	/**
	 * Size of all elements
	 * @param xid the transaction id
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
	 * Size of all elements
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	public static long size(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, clazz, xid);
		return ttm.size();
	}
	/**
	 * Is the key contained in the dataset
	 * @param xid the transaction id
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
	 * Is the key contained in the dataset
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @parameter obj The Comparable key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias does not exist
	 */
	public static boolean contains(String alias, String xid, Comparable obj) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, obj.getClass(), xid);
		return ttm.containsKey(obj);
	}
	/**
	 * Is the value object present
	 * @param xid the transaction id
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
	/**
	 * Is the value object present
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param keyType the class to retrieve
	 * @param obj the object with equals, CAUTION explicit conversion is needed
	 * @return boolean true if found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	public static boolean containsValue(String alias, String xid, Class keyType, Object obj) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(alias, keyType, xid);
		return ttm.containsValue(obj);
	}

}

