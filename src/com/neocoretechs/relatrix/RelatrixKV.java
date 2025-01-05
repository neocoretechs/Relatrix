package com.neocoretechs.relatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.DatabaseManager;
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
	private static ConcurrentHashMap<String, BufferedMap> mapCache = new ConcurrentHashMap<String, BufferedMap>();
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private RelatrixKV() {}
	// 2.) volatile instance
	private static volatile RelatrixKV instance = null;
	// 3.) lock class, assign instance if null
	public static RelatrixKV getInstance() {
		synchronized(RelatrixKV.class) {
			if(instance == null) {
				instance = new RelatrixKV();
			}
		}
		return instance;
	}
	
	public static BufferedMap getMap(Comparable type) throws IllegalAccessException, IOException {
		return getMap(type.getClass());
	}
	
	public static BufferedMap getMap(Alias alias, Comparable type) throws IllegalAccessException, IOException {
		return getMap(alias, type.getClass());
	}
	
	public static BufferedMap getMap(Class type) throws IllegalAccessException, IOException {
		BufferedMap t = mapCache.get(type.getName());
		if(t == null) {
			t = DatabaseManager.getMap(type);
			mapCache.put(type.getName(), t);
		}
		return t;
	}
	public static BufferedMap getMap(Alias alias, Class type) throws IllegalAccessException, IOException {
		BufferedMap t = mapCache.get(type.getName()+alias.getAlias());
		if(t == null) {
			t = DatabaseManager.getMap(alias, type);
			mapCache.put(type.getName()+alias.getAlias(), t);
		}
		return t;
	}
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespace(String path) throws IOException {
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
	public static void setAlias(Alias alias, String path) throws IOException {
		DatabaseManager.setTableSpaceDir(alias, path);
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	public static void removeAlias(Alias alias) throws NoSuchElementException {
		DatabaseManager.removeAlias(alias);
	}
	
	/**
	 * Will return null if alias does not exist
	 * @param alias
	 * @return
	 */
	public static String getAlias(Alias alias) {
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
	public static void store(Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		BufferedMap ttm = getMap(key);
		if( DEBUG  )
			System.out.println("RelatrixKV.store storing key:"+key+" value:"+value+" in map:"+ttm);
		ttm.put(key, value);
	}

	/**
	 * Store our permutations of the key/value
	 * @param alias The database alias
	 * @param key of comparable
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static void store(Alias alias, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		BufferedMap ttm = getMap(alias, key);
		if( DEBUG  )
			System.out.println("RelatrixKV.store storing alias:"+alias+" key:"+key+" value:"+value+" in map:"+ttm);
		ttm.put(key, value);
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
	 * @return the previous value for the removed key or null if no key was found
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static Object remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(c);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKV.remove prepping to remove:"+c);
		return ttm.remove(c);
	}
	/**
	 * Delete element with given key that this object participates in
	 * @param c The Comparable key
	 * @return the previous value for removed key or null if no key was found to remove
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static Object remove(Alias alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, c);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKV.remove prepping to remove:"+c);
		return ttm.remove(c);
	}

	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findTailMap(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
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
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findTailMap(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	public static Stream<?> findTailMapStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
		return ttm.tailMapStream(darg);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	public static Stream<?> findTailMapStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(alias, darg);
		return ttm.tailMapStream(darg);
	}

	/**
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param darg Object for key of relationship
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findTailMapKV(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
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
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findTailMapKV(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		return ttm.tailMapKV(darg);
	}

	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param darg Comparable for key
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	public static Stream<?> findTailMapKVStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
		return ttm.tailMapKVStream(darg);
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param alias The database alias
	 * @param darg Comparable for key
	 * @exception IOException low-level access or problems modifying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias is not found 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	public static Stream<?> findTailMapKVStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		return ttm.tailMapKVStream(darg);
	}

	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param darg The Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findHeadMap(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
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
	public static Iterator<?> findHeadMap(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
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
	public static Stream<?> findHeadMapStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
		return ttm.headMapStream(darg);
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
	public static Stream<?> findHeadMapStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		return ttm.headMapStream(darg);
	}

	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param darg The comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findHeadMapKV(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
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
	public static Iterator<?> findHeadMapKV(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		// check for at least one object reference in our headset factory
		return ttm.headMapKV(darg);
	}

	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param darg Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
	 */
	public static Stream<?> findHeadMapKVStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
		return ttm.headMapKVStream(darg);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param alias The database alias
	 * @param darg Comparable key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias is not found
	 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
	 */
	public static Stream<?> findHeadMapKVStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		// check for at least one object reference in our headset factory
		return ttm.headMapKVStream(darg);
	}

	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	public static Iterator<?> findSubMap(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
		return ttm.subMap(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first 
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
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
	public static Iterator<?> findSubMap(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		return ttm.subMap(darg, marg);
	}

	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first .
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which data may be retrieved. Fulfills Stream interface.
	 */
	public static Stream<?> findSubMapStream(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg);
		return ttm.subMapStream(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/> 
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
	public static Stream<?> findSubMapStream(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg);
		return ttm.subMapStream(darg, marg);
	}

	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findSubMapKV(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(darg);
		return ttm.subMapKV(darg, marg);
	}
	/**
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key
	 * @param alias The database alias
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias was not ofund
	 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findSubMapKV(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(alias, darg);
		return ttm.subMapKV(darg, marg);
	}

	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Stream<Result>
	 */
	public static Stream<?> findSubMapKVStream(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(darg);
		return ttm.subMapKVStream(darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param alias The database alias
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias was not found
	 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Iterator<Result>
	 */
	public static Stream<?> findSubMapKVStream(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(alias, darg);
		return ttm.subMapKVStream(darg, marg);
	}

	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Iterator for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Iterator<?> entrySet(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static Iterator<?> entrySet(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.entrySet();
	}
	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Stream<?> entrySetStream(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		return ttm.entrySetStream();
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
	public static Stream<?> entrySetStream(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.entrySetStream();
	}
	/**
	 * Return the keyset for the given class
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Iterator<?> keySet(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static Iterator<?> keySet(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.keySet();
	}
	/**
	 * Return the keyset for the given class
	 * @param clazz the class to retrieve
	 * @return The stream from which keyset can be consumed
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static Stream<?> keySetStream(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		return ttm.keySetStream();
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
	public static Stream<?> keySetStream(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.keySetStream();
	}
	/**
	 * return lowest valued key.
	 * @param clazz the class to retrieve
	 * @return the The key/value with lowest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object firstKey(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static Object firstKey(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.firstKey();
	}
	/**
	 * Return the value for the key.
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object get(Comparable<?> key) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(key);
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
	public static Object get(Alias alias, Comparable<?> key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, key);
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
	public static Object firstValue(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static Object firstValue(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.first();
	}
	/**
	 * Return instance having the highest valued key.
	 * @param clazz the class to retrieve
	 * @return the The highest value object
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object lastKey(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static Object lastKey(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.lastKey();
	}
	/**
	 * Return the instance having the value for  the greatest key.
	 * @param clazz the class to retrieve
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static Object lastValue(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static Object lastValue(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.last();
	}
	/**
	 * Size of all elements
	 * @param clazz the class to retrieve
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static long size(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static long size(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.size();
	}
	/**
	 * Is the key contained in the dataset
	 * @param obj The Comparable key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static boolean contains(Comparable<?> obj) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(obj);
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
	public static boolean contains(Alias alias, Comparable<?> obj) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(alias, obj);
		return ttm.containsKey(obj);
	}

	/**
	 * Is the value object present
	 * @param obj the object with equals
	 * @return boolean true if found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static boolean containsValue(Class<?> clazz, Comparable obj) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
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
	public static boolean containsValue(Alias alias, Class<?> clazz, Comparable obj) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.containsValue(obj);
	}
	/**
	 * Return the key/val.ue pair of Map.Entry implementation of the closest key to the passed key template.
	 * May be exact match Up to user. Essentially starts a tailMapKv iterator seeking nearest key.
	 * @param key target key template
	 * @return null if no next for initial iteration
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static Object nearest(Comparable<?> key) throws IllegalAccessException, IOException {
		BufferedMap ttm = getMap(key);
		return ttm.nearest(key);
	}
	/**
	 * Return the key/val.ue pair of Map.Entry implementation of the closest key to the passed key template.
	 * May be exact match Up to user. Essentially starts a tailMapKv iterator seeking nearest key.
	 * @param alias the database alias
	 * @param key target key template
	 * @return null if no next for initial iteration
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static Object nearest(Alias alias, Comparable<?> key) throws IllegalAccessException, IOException, NoSuchElementException {
		BufferedMap ttm = getMap(alias,key);
		return ttm.nearest(key);
	}

	/**
	 * Close and remove database from available set
	 * @param alias
	 * @param clazz
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	public static void close(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		DatabaseManager.removeMap(alias, ttm);
	}
	/**
	 * Close and remove database from available set
	 * @param clazz
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	public static void close(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		DatabaseManager.removeMap(ttm);
	}

}

