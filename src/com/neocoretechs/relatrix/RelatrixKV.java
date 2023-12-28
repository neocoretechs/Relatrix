package com.neocoretechs.relatrix;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.rocksack.session.TransactionalMap;

import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
* Top-level class that imparts behavior to the Key/Value subclasses which contain references for key/value.

* The compareTo and fullCompareTo provide the comparison methods to drive the processes.
* The retrieval operators allow us to form the partially ordered result sets that are returned.<br/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021
*/
public final class RelatrixKV {
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
		DatabaseManager.setTableSpaceDir(path);
	}
	
	public static String getTableSpace() {
		return DatabaseManager.getTableSpaceDir();
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
		DatabaseManager.setTableSpaceDir(alias, path);
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	public static void removeAlias(String alias) throws NoSuchElementException {
		DatabaseManager.removeAlias(alias);
	}
	
	/**
	 * Will return null if alias does not exist
	 * @param alias
	 * @return
	 */
	public static String getAlias(String alias) {
		return DatabaseManager.getTableSpaceDir(alias);
	}
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	public static String[][] getAliases() {
		return DatabaseManager.getAliases();
	}
/**
 * Store our permutations of the key/value
 * @param key of comparable
 * @param value
 * @throws IllegalAccessException
 * @throws IOException
 */
public static void store(Comparable key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key);
	BufferedMap ttm = DatabaseManager.getMap(key);
	if( DEBUG  )
		System.out.println("RelatrixKV.transactionalStore storing dmr:"+key+"/"+value);
	ttm.put(key, value);
	if( DEBUG )
		System.out.println("RelatrixKV.store Tree Search Result: ");
}

/**
 * Store our permutations of the key/value
 * @param alias The database alias
 * @param key of comparable
 * @param value
 * @throws IllegalAccessException
 * @throws IOException
 */
public static void store(String alias, Comparable key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key);
	BufferedMap ttm = DatabaseManager.getMap(alias, key);
	if( DEBUG  )
		System.out.println("RelatrixKV.transactionalStore storing dmr:"+key+"/"+value);
	ttm.put(key, value);
	if( DEBUG )
		System.out.println("RelatrixKV.store Tree Search Result: ");
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
public static void remove(Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(c);
	BufferedMap ttm = DatabaseManager.getMap(c);
	if( DEBUG || DEBUGREMOVE )
		System.out.println("RelatrixKV.remove prepping to remove:"+c);
		ttm.remove(c);
	if( DEBUG || DEBUGREMOVE )
		System.out.println("RelatrixKV.remove exiting remove for key:"+c+" should have removed"+c);
}
/**
* Delete element with given key that this object participates in
* @param c The Comparable key
* @exception IOException low-level access or problems modifying schema
* @throws IllegalAccessException 
* @throws ClassNotFoundException 
* @throws IllegalArgumentException 
*/
public static void remove(String alias, Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(c);
	BufferedMap ttm = DatabaseManager.getMap(alias, c);
	if( DEBUG || DEBUGREMOVE )
		System.out.println("RelatrixKV.remove prepping to remove:"+c);
		ttm.remove(c);
	if( DEBUG || DEBUGREMOVE )
		System.out.println("RelatrixKV.remove exiting remove for key:"+c+" should have removed"+c);
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
public static Iterator<?> findTailMap(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	return ttm.tailMap(darg);
}
/**
* Retrieve from the targeted relationship. Essentially this is the default permutation which
* retrieves the equivalent of a tailSet and returns the value elements
* @param alias The database alias
* @param darg Object marking start of retrieval
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException the operator is invalid
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @throws NoSuchElementException
* @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
*/
public static Iterator<?> findTailMap(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Stream<?> findTailMapStream(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMap(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
* Retrieve from the targeted relationship. Essentially this is the default permutation which
* retrieves the equivalent of a tailSet and returns the value elements
* @param alias The database alias
* @param darg Comparable marking start of retrieval
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException the operator is invalid
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Comparable[]>
*/
public static Stream<?> findTailMapStream(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Iterator<?> findTailMapKV(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	return ttm.tailMapKV(darg);
}
/**
* Retrieve from the targeted Key/Value relationship from given key.
* Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
* @param alias The database alias
* @param darg Object for key of relationship
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException At least one argument must be a valid object reference
* @exception ClassNotFoundException if the Class of Object is invalid
* @exception NoSuchElementException If the alias was not ofund
* @throws IllegalAccessException 
* @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
*/
public static Iterator<?> findTailMapKV(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Stream<?> findTailMapKVStream(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.tailMapKV(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
* Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
* @param alias The database alias
* @param darg Comparable for key
* @param parallel Optional true to execute parallel stream
* @exception IOException low-level access or problems modifying schema
* @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException
* @throws NoSuchElementException if the alias is not found 
* @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Comparable[]>
*/
public static Stream<?> findTailMapKVStream(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Iterator<?> findHeadMap(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	// check for at least one object reference in our headset factory
	return ttm.headMap(darg);
}
/**
 * Retrieve the given set of values from the start of the elements to the given key.
 * @param alias The database alias
 * @param darg The Comparable key
 * @throws IllegalArgumentException At least one argument must be a valid object reference
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException
 * @return The Iterator from which data may be retrieved. Fulfills Iterator interface.
 */
public static Iterator<?> findHeadMap(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Stream<?> findHeadMapStream(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	// check for at least one object reference in our headset factory
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMap(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
 * Retrieve the given set of values from the start of the elements to the given key.
 * @param alias The database alias
 * @param darg Comparable key
 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias is not found
 * @return Stream from which data may be consumed. Fulfills Stream interface.
 */
public static Stream<?> findHeadMapStream(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Iterator<?> findHeadMapKV(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	// check for at least one object reference in our headset factory
	return ttm.headMapKV(darg);
}
/**
 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
 * @param alias The database alias
 * @param darg The comparable key
 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias is not ofund
 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
 */
public static Iterator<?> findHeadMapKV(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Stream<?> findHeadMapKVStream(Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	// check for at least one object reference in our headset factory
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize( ttm.headMapKV(darg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
 * @param alias The database alias
 * @param darg Comparable key
 * @param parallel true for parallel stream
 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias is not found
 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
 */
public static Stream<?> findHeadMapKVStream(String alias, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Iterator<?> findSubMap(Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	return ttm.subMap(darg, marg);
}
/**
 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
 * @param alias The database alias
 * @param darg The starting key
 * @param marg The ending key
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias is not ofund
 * @return Iterator from which data may be retrieved. Fulfills Iterator interface.
 */
public static Iterator<?> findSubMap(String alias, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Stream<?> findSubMapStream(Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMap(darg, marg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
 * @param alias The database alias
 * @param darg The starting key
 * @param marg The ending key
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias was not found
 * @return Stream from which data may be retrieved. Fulfills Stream interface.
 */
public static Stream<?> findSubMapStream(String alias, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Iterator<?> findSubMapKV(Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	return ttm.subMapKV(darg, marg);
}
/**
 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
 * @param alias The database alias
 * @param darg The starting key
 * @param marg The ending key
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias was not ofund
 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
 */
public static Iterator<?> findSubMapKV(String alias, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	// check for at least one object reference
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Stream<?> findSubMapKVStream(Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(darg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.subMapKV(darg, marg), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
 * @param alias The database alias
 * @param darg The starting key
 * @param marg The ending key
 * @throws IOException
 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias was not found
 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Sterator<Comparable[]>
 */
public static Stream<?> findSubMapKVStream(String alias, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
{
	// check for at least one object reference
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(darg);
	BufferedMap ttm = DatabaseManager.getMap(alias, darg);
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
public static Iterator<?> entrySet(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.entrySet();
}
/**
 * Return the entry set for the given class type
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return Iterator for entry set
 * @throws IOException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias is nout found
 */
public static Iterator<?> entrySet(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.entrySet();
}
/**
 * Return the entry set for the given class type
 * @param clazz the class to retrieve
 * @return Stream for entry set
 * @throws IOException
 * @throws IllegalAccessException
 */
public static Stream<?> entrySetStream(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.entrySet(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true); //true = parallel
}
/**
 * Return the entry set for the given class type
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return Stream for entry set
 * @throws IOException
 * @throws IllegalAccessException
 * @throws NoSuchElementException if the alias was not found
 */
public static Stream<?> entrySetStream(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
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
public static Iterator<?> keySet(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.keySet();
}
/**
 * Return the keyset for the given class
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return the iterator for the keyset
 * @throws IOException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias was not found
 */
public static Iterator<?> keySet(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.keySet();
}
/**
 * Return the keyset for the given class
 * @param clazz the class to retrieve
 * @return The stream from which keyset can be consumed
 * @throws IOException
 * @throws IllegalAccessException
 */
public static Stream<?> keySetStream(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ttm.keySet(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, true);
}
/**
 * Return the keyset for the given class
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return The stream from which keyset can be consumed
 * @throws IOException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias was not ofund
 */
public static Stream<?> keySetStream(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
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
public static Object firstKey(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.firstKey();
}
/**
 * return lowest valued key.
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return the The key/value with lowest key value.
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias was not found
 */
public static Object firstKey(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.firstKey();
}
/**
 * Return the value for the key.
 * @param key the key to retrieve
 * @return The value for the key.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static Object get(Comparable key) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key);
	BufferedMap ttm = DatabaseManager.getMap(key);
	 Object o = ttm.get(key);
	 if( o == null )
		 return null;
	return ((KeyValue)o).getmValue();
}
/**
 * Return the value for the key.
 * @param alias The database alias
 * @param key the key to retrieve
 * @return The value for the key.
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias is not found
 */
public static Object get(String alias, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(key);
	BufferedMap ttm = DatabaseManager.getMap(alias, key);
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
public static Object firstValue(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.first();
}
/**
 * The lowest key value object
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return The first value of the class with given key
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias was not found
 */
public static Object firstValue(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.first();
}
/**
 * Return instance having the highest valued key.
 * @param clazz the class to retrieve
 * @return the The highest value object
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static Object lastKey(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.lastKey();
}
/**
 * Return instance having the highest valued key.
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return the The highest value object
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias was not found
 */
public static Object lastKey(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.lastKey();
}
/**
 * Return the instance having the value for  the greatest key.
 * @param clazz the class to retrieve
 * @return the DomainMapRange morphism having the highest key value.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static Object lastValue(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.last();
}
/**
 * Return the instance having the value for  the greatest key.
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return the DomainMapRange morphism having the highest key value.
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias was not found
 */
public static Object lastValue(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.last();
}
/**
 * Size of all elements
 * @param clazz the class to retrieve
 * @return the number of DomainMapRange morphisms.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static long size(Class clazz) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(clazz);
	return ttm.size();
}
/**
 * Size of all elements
 * @param alias The database alias
 * @param clazz the class to retrieve
 * @return the number of DomainMapRange morphisms.
 * @throws IOException
 * @throws IllegalAccessException
 * @throws NoSuchElementException If the alias was not found 
 */
public static long size(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(clazz);
	BufferedMap ttm = DatabaseManager.getMap(alias, clazz);
	return ttm.size();
}
/**
 * Is the key contained in the dataset
 * @param obj The Comparable key to search for
 * @return true if key is found
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static boolean contains(Comparable obj) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(obj);
	BufferedMap ttm = DatabaseManager.getMap(obj);
	return ttm.containsKey(obj);
}
/**
 * Is the key contained in the dataset
 * @param alias The database alias
 * @param obj The Comparable key to search for
 * @return true if key is found
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias is not found
 */
public static boolean contains(String alias, Comparable obj) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(obj);
	BufferedMap ttm = DatabaseManager.getMap(alias, obj);
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
public static boolean containsValue(Class keyType, Object obj) throws IOException, IllegalAccessException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(keyType);
	BufferedMap ttm = DatabaseManager.getMap(keyType);
	return ttm.containsValue(obj);
}
/**
 * Is the value object present
 * @param alias The database alias
 * @param keyType the class to retrieve
 * @param obj the object with equals, CAUTION explicit conversion is needed
 * @return boolean true if found
 * @throws IOException
 * @throws IllegalAccessException 
 * @throws NoSuchElementException If the alias was not found
 */
public static boolean containsValue(String alias, Class keyType, Object obj) throws IOException, IllegalAccessException, NoSuchElementException
{
	//TransactionalMap ttm = RockSackAdapter.getRockSackTransactionalMap(keyType);
	BufferedMap ttm = DatabaseManager.getMap(alias, keyType);
	return ttm.containsValue(obj);
}
/**
 * Get the new DBkey.
 * @return
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

}

