package com.neocoretechs.relatrix;

import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.json.cbor.CborBuilder;
import org.json.cbor.CborDecoder;
import org.json.cbor.CborException;
import org.json.cbor.model.DataItem;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.SerializedComparatorFactory;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.relatrix.client.json.util.JsonRecordClassGenerator;
import com.neocoretechs.relatrix.client.json.util.RelatrixTypeSynthesizer;
import com.neocoretechs.relatrix.server.BytecodeNotFoundInRepositoryException;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.ServerMethod;

/**
* Top-level class that imparts behavior to the Key/Value subclasses which contain references for key/value.
* The compareTo in the dynamically generated JSOn wrapper classes provide the comparison methods to drive the processes
* via the RocksDB AbstractComparator in RockSack, then in RocksDB.<p>
* The retrieval operators allow us to form the partially ordered result sets that are returned.<br>
* The Json version differs in that classes are generated dynamically based on the fields in the JSON payload.
* These classes are named after the hash of the fields and a dynamically generated wrapper class stored the CBOR bytes.
* CBOR is a compact binary standard representation for JSON. Instantiates its own ClassLoader for the purpose.
* @author Jonathan Groff (C) NeoCoreTechs 2026
*/
public final class RelatrixKVJson {
	private static boolean DEBUG = true;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	private static ConcurrentHashMap<String, BufferedMap> mapCache = new ConcurrentHashMap<String, BufferedMap>();
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private RelatrixKVJson() {}
	// 2.) volatile instance
	private static volatile RelatrixKVJson instance = null;
	private static HandlerClassLoader classLoader = null;
	// 3.) lock class, assign instance if null
	public static RelatrixKVJson getInstance() {
		synchronized(RelatrixKVJson.class) {
			if(instance == null) {
				instance = new RelatrixKVJson();
				classLoader = new HandlerClassLoader();
				Thread.currentThread().setContextClassLoader(classLoader);
				SerializedComparatorFactory.setClassLoader(classLoader);
				try {
					HandlerClassLoader.connectToLocalRepository(null); // tablespace property
				} catch (IllegalAccessException | IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return instance;
	}
	/**
	 * Generate a class name from a JSONObject
	 * @param jsono the JSONObject with the fields
	 * @return
	 */
	public static String getClassName(JSONObject jsono) {
		return RelatrixTypeSynthesizer.generateMorphicClassName(jsono,JsonRecordClassGenerator.generatedJsonClassPrefix);
	}
	
	public static Class<?> getClassType(JSONObject jsono) throws IllegalAccessException, IOException, ClassNotFoundException {
		BufferedMap bm = getJsonClass(jsono);
		Class<?> c;
		c = Class.forName(bm.getClassName(), false, classLoader);
		if(DEBUG)
			System.out.println("RelatrixKVJson.getClassType returning class:"+c+" for map "+bm);
		return c;
	}
	
	public static Comparable<?> getObject(JSONObject json) throws IllegalAccessException, IOException, ClassNotFoundException {
		BufferedMap bm = getJsonClass(json);
		return getObject(bm);
	}
	/**
	 * Must call getJsonClass initially! It will define the fields and contents of fields and place them
	 * in the RelatrixTypeSynthesizer.structuralTokens and elements.<p>
	 * Creates a class definition from BufferedMap that represents the morphic class.
	 * @param bm the BufferedMap with relevant class and database definition
	 * @return The comparable object
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static Comparable<?> getObject(BufferedMap bm) throws IllegalAccessException, IOException {
		Class<?> c;
		try {
			c = Class.forName(bm.getClassName(), false, classLoader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	   	CborBuilder cb = new CborBuilder();
    	byte[] encodedBytes;
		try {
			encodedBytes = RelatrixTypeSynthesizer.generateMorphicPayload(RelatrixTypeSynthesizer.structuralTokens, RelatrixTypeSynthesizer.elements, cb);
		} catch (CborException e) {
			throw new IOException(e);
		}
    	Constructor<?> ctor;
		try {
			ctor = c.getConstructor(byte[].class);
			return (Comparable<?>) ctor.newInstance(encodedBytes);
		} catch (InstantiationException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
				throw new IllegalAccessException(e.getMessage());
		}
	}
	/**
	 * Transform a morphic class into a String class instance by extracting the 'cbor' field
	 * with the binary payload, performing the decode, and rendering the String instance
	 * @param c The morphic class
	 * @return The String representation
	 */
	public static String getData(Comparable c) {
		Field f;
		if(DEBUG)
			System.out.println("RelatrixKVJson.getData for:"+c+" class:"+c.getClass());
		try {
			f = c.getClass().getField("cbor");
		} catch (NoSuchFieldException e) {
			if(DEBUG)
				e.printStackTrace();
			return null;
		}
		byte[] payload = null;
		try {
			payload = (byte[]) f.get(c);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			if(DEBUG)
				e.printStackTrace();
			return null;
		}
		List<DataItem> d = null;
		try {
			d = CborDecoder.decode(payload);
		} catch (CborException e) {
			if(DEBUG)
				e.printStackTrace();
			return null;
		}
		return d.get(0).toString();
	}
	/**
	 * Transform a morphic class instance into a JSONObject by calling getData on the
	 * morphic class to transform it into a String, then creating a JSONObject from that String.
	 * @param c The original Class
	 * @return The JSONObject from getData
	 */
	public static JSONObject getJsonData(Comparable c) {
		return new JSONObject(getData(c));
	}
	/**
	 * Transform a morphic keyed map into a String keyed map
	 * @param c The original Map
	 * @return The transformed Map
	 */
	public static Map.Entry<String,Object> getData(Map.Entry<Comparable,Object> c) {
		Field f;
		try {
			f = c.getKey().getClass().getField("cbor");
		} catch (NoSuchFieldException e) {
			return null;
		}
		byte[] payload = null;
		try {
			payload = (byte[]) f.get(c.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
		List<DataItem> d = null;
		try {
			d = CborDecoder.decode(payload);
		} catch (CborException e) {
			return null;
		}
		String kload = d.get(0).toString();
		return new AbstractMap.SimpleEntry<String,Object>(kload,c.getValue());
	}
	/**
	 * Transform a morphic keyed map into a JSONObject keyed map
	 * @param c The original Map
	 * @return The transformed Map
	 */
	public static Map.Entry<JSONObject,Object> getJsonData(Map.Entry<Comparable,Object> c) {
		Field f;
		try {
			f = c.getKey().getClass().getField("cbor");
		} catch (NoSuchFieldException e) {
			return null;
		}
		byte[] payload = null;
		try {
			payload = (byte[]) f.get(c.getKey());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
		List<DataItem> d = null;
		try {
			d = CborDecoder.decode(payload);
		} catch (CborException e) {
			return null;
		}
		String kload = d.get(0).toString();
		JSONObject jo = new JSONObject(kload);
		return new AbstractMap.SimpleEntry<JSONObject,Object>(jo,c.getValue());
	}
	/**
	 * Utility class to translate a morphic class iterator into a another type of iterator
	 * @param <T>  The morphic iterator type
	 * @param <R> The function return type that performs the conversion, for instance, the getData methods
	 */
	public static final class TransformingIterator<T,R> implements Iterator<R> {
	    private final Iterator<? extends T> src;
	    private final Function<? super T, ? extends R> fn;
	    public TransformingIterator(Iterator<? extends T> iterator, Function<? super T, ? extends R> fn){
	        this.src = Objects.requireNonNull(iterator);
	        this.fn  = Objects.requireNonNull(fn);
	        if(DEBUG)
	        	System.out.println("RelatrixKVJson.TransformingIterator ctor iterator:"+this.src);
	    }
	    @Override public boolean hasNext(){ 
	    	return src.hasNext(); 
	    }
	    @Override public R next(){
	        T t = src.next();
	        if(DEBUG)
	        	System.out.println("RelatrixKVJson.TransformingIterator:"+t.getClass().getName()+" "+t);
	        return fn.apply(t);
	    }
	    @Override public void remove(){ src.remove(); }
	}
	/**
	 * Transform a morphic class iterator to a String iterator using TransformingIterator
	 * @param it The original Iterator
	 * @return The transformed Iterator
	 */
	public static Iterator<?> getStringIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,v -> RelatrixKVJson.getData((Comparable<?>) v));
	}
	/**
	 * Transform a morphic class iterator to a JSONObject iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed Iterator
	 */
	public static Iterator<?> getJsonIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,v -> RelatrixKVJson.getJsonData((Comparable<?>) v));
	}
	/**
	 * Transform a morphic class stream into a String stream using map and getData
	 * @param s The original Stream
	 * @return The transformed Stream
	 */
	public static Stream<?> getStringStream(Stream<?> s) {
		return s.map(e->RelatrixKVJson.getData((Comparable<?>)e));
	}
	/**
	 * Transform a morphic class stream into a Json stream using map and getData
	 * @param s The original Stream
	 * @return The transformed Stream
	 */
	public static Stream<?> getJsonStream(Stream<?> s) {
		return s.map(e->RelatrixKVJson.getJsonData((Comparable<?>)e));
	}	
	/**
	 * Transform a morphic class iterator to a String key map iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed iterator
	 */
	public static Iterator<?> getStringMapIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,e->RelatrixKVJson.getData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class iterator to a JSONObject iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed iterator
	 */
	public static Iterator<?> getJsonMapIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,e->RelatrixKVJson.getJsonData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class stream into a String stream using map and getData
	 * @param s
	 * @return
	 */
	public static Stream<?> getStringMapStream(Stream<?> s) {
		return s.map(e->RelatrixKVJson.getData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class stream into a Json stream using map and getData
	 * @param s The original morphic class key map Stream
	 * @return The transformed Stream of JSONObject keyed maps 
	 */
	public static Stream<?> getJsonMapStream(Stream<?> s) {
		return s.map(e->RelatrixKVJson.getJsonData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Obtain the BufferedMap for the morphic class represented by the JSONObject passed.
	 * @param jsono the JSONObject containing the fields that define a morphic class
	 * @return The BufferedMap that facilitates storage/retrieval of morphic class instances
	 * @throws IllegalAccessException If the class cannot be constructed
	 * @throws IOException If the underlying storage subsystem fails
	 */
	private static BufferedMap getJsonClass(JSONObject jsono) throws IllegalAccessException, IOException {
		String cjson = RelatrixTypeSynthesizer.generateMorphicClassName(jsono, JsonRecordClassGenerator.generatedJsonClassPrefix);
		BufferedMap t = mapCache.get(cjson);
		byte[] ctype = null;
		if(t == null) {
			Class<?> c;
			try {
				c = Class.forName(cjson, false, classLoader);
			} catch (ClassNotFoundException cnf) {
				try {
					ctype = HandlerClassLoader.getBytesFromRepository(cjson);
				} catch (BytecodeNotFoundInRepositoryException e) {
					ctype = JsonRecordClassGenerator.buildJsonRecordClassBytes(cjson);
					HandlerClassLoader.setBytesInRepository(cjson, ctype);
				}
				c = classLoader.defineAClass(cjson,ctype,0,ctype.length);
			}
			t = DatabaseManager.getMap(c);
			mapCache.put(cjson, t);
		}
		return t;
	}
	/**
	 * Obtain the BufferedMap for the morphic class represented by the JSONObject passed.
	 * @param alias The alias for the database target
	 * @param jsono the JSONObject containing the fields that define a morphic class
	 * @return The BufferedMap that facilitates storage/retrieval of morphic class instances
	 * @throws IllegalAccessException If the class cannot be constructed
	 * @throws IOException If the underlying storage subsystem fails
	 */
	private static BufferedMap getJsonClass(Alias alias, JSONObject jsono) throws IllegalAccessException, IOException {
		String cjson = RelatrixTypeSynthesizer.generateMorphicClassName(jsono, JsonRecordClassGenerator.generatedJsonClassPrefix);
		BufferedMap t = mapCache.get(cjson);
		byte[] ctype = null;
		if(t == null) {
			Class<?> c;
			try {
				c = Class.forName(cjson, false, classLoader);
			} catch (ClassNotFoundException cnf) {
				try {
					ctype = HandlerClassLoader.getBytesFromRepository(cjson);
				} catch (BytecodeNotFoundInRepositoryException e) {
					ctype = JsonRecordClassGenerator.buildJsonRecordClassBytes(cjson);
					HandlerClassLoader.setBytesInRepository(cjson, ctype);
				}
				c = classLoader.defineAClass(cjson,ctype,0,ctype.length);
			}
			t = DatabaseManager.getMap(alias, c);
			mapCache.put(cjson, t);
		}
		return t;
	}
	
	/**
	 * Get a class definition and hence a BufferedMap from the JSON payload. The fields define the class via
	 * the hashed representation. Assumes getClassName has previously been called. 
	 * Approx 50ms timing for class build and define.
	 * @param json The JSON payload with field names
	 * @return The BufferedMap for the generated class
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @see RelatrixTypeSynthesizer
	 * @see JsonRecordClassGenerator
	 */
	public static BufferedMap getMap(Class<?> json) throws IllegalAccessException, IOException {
		String cjson = json.getName();
		BufferedMap t = mapCache.get(cjson);
		if(DEBUG)
			System.out.println("RelatrixKVJson.getMap for "+cjson+" got BufferedMap "+t);
		byte[] ctype = null;
		if(t == null) {
			Class<?> c;
			try {
				c = Class.forName(cjson, false, classLoader);
			} catch (ClassNotFoundException cnf) {
				try {
					ctype = HandlerClassLoader.getBytesFromRepository(cjson);
				} catch (BytecodeNotFoundInRepositoryException e) {
					ctype = JsonRecordClassGenerator.buildJsonRecordClassBytes(cjson);
					HandlerClassLoader.setBytesInRepository(cjson, ctype);
				}
				c = classLoader.defineAClass(cjson,ctype,0,ctype.length);
			}
			t = DatabaseManager.getMap(c);
			if(DEBUG)
				System.out.println("RelatrixKVJson.getMap for "+cjson+" created BufferedMap "+t);
			mapCache.put(cjson, t);
		}
		return t;
	}
	
	public static BufferedMap getMap(Alias alias, Class<?> json) throws IllegalAccessException, IOException {
		String cjson = json.getName();
		BufferedMap t = mapCache.get(cjson);
		byte[] ctype = null;
		if(t == null) {
			Class<?> c;
			try {
				c = Class.forName(cjson, false, classLoader);
			} catch (ClassNotFoundException cnf) {
				try {
					ctype = HandlerClassLoader.getBytesFromRepository(cjson);
				} catch (BytecodeNotFoundInRepositoryException e) {
					ctype = JsonRecordClassGenerator.buildJsonRecordClassBytes(cjson);
					HandlerClassLoader.setBytesInRepository(cjson, ctype);
				}
				c = classLoader.defineAClass(cjson,ctype,0,ctype.length);
			}
			t = DatabaseManager.getMap(alias, c);
			mapCache.put(cjson, t);
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
	 * Set an alias relative to the current tablespace
	 * @param alias
	 * @param path
	 * @throws IOException
	 */
	@ServerMethod
	public static void setRelativeAlias(Alias alias) throws IOException {
		if(alias.getAlias().contains("/") || alias.getAlias().contains("\\") || alias.getAlias().contains("..") || alias.getAlias().contains("~"))
			throw new IOException("No path allowed");
		setAlias(alias, getTableSpace()+alias.getAlias());
	}
		
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	@ServerMethod	
	public static void removeAlias(Alias alias) throws NoSuchElementException {
		DatabaseManager.removeAlias(alias);
	}
	
	/**
	 * Will return null if alias does not exist
	 * @param alias
	 * @return
	 */
	@ServerMethod
	public static String getAlias(Alias alias) {
		return DatabaseManager.getTableSpaceDir(alias);
	}
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	@ServerMethod
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
	@ServerMethod
	public static void store(Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		JSONObject jsono = new JSONObject(String.valueOf(key));
		BufferedMap ttm = getJsonClass(jsono);
		Comparable<?> jkey = getObject(ttm);
		if( DEBUG  )
			System.out.println("RelatrixKVJson.store storing key:"+jkey+" value:"+value+" in map:"+ttm);
		ttm.put(jkey, value);
	}
	
	@ServerMethod
	public static void storekv(Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		BufferedMap ttm = getMap(key.getClass());
		if( DEBUG  )
			System.out.println("RelatrixKVJson.storekv storing key:"+key+" value:"+value+" in map:"+ttm);
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
	@ServerMethod
	public static void store(Alias alias, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		JSONObject jsono = new JSONObject(key);
		BufferedMap ttm = getJsonClass(alias, jsono);
		Comparable<?> jkey = getObject(ttm);
		if( DEBUG  )
			System.out.println("RelatrixKVJson.store storing alias:"+alias+" key:"+jkey+" value:"+value+" in map:"+ttm);
		ttm.put(jkey, value);
	}
	
	@ServerMethod
	public static void storekv(Alias alias, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		BufferedMap ttm = getMap(alias, key.getClass());
		if( DEBUG  )
			System.out.println("RelatrixKVJson.storekv storing key:"+key+" value:"+value+" in map:"+ttm+" for alias "+alias);
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
	@ServerMethod
	public static Object remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVJson.remove prepping to remove:"+c);
		BufferedMap ttm = getMap(c.getClass());
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
	@ServerMethod
	public static Object remove(Alias alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, c.getClass());
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVJson.remove prepping to remove:"+c);
		return ttm.remove(c);
	}

	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access 
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMap(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
		return ttm.tailMap(darg);
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param alias The database alias
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMap(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
		return ttm.tailMap(darg);
	}

	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param darg Comparable marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
		return ttm.tailMapStream(darg);//.map(e->RelatrixKVJson.getData((Comparable<?>)e));
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param alias The database alias
	 * @param darg Comparable marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
		return ttm.tailMapStream(darg);
	}
	/**
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param darg Object for key of relationship
 	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMapKV(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
		return ttm.tailMapKV(darg);
	}
	/**
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param alias The database alias
	 * @param darg Object for key of relationship
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @exception NoSuchElementException If the alias was not ofund
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMapKV(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
		return ttm.tailMapKV(darg);
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param darg Comparable for key
	 * @exception IOException low-level access 
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapKVStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
		return ttm.tailMapKVStream(darg);
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param alias The database alias
	 * @param darg Comparable for key
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias is not found 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapKVStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
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
	@ServerMethod
	public static Iterator<?> findHeadMap(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
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
	@ServerMethod
	public static Iterator<?> findHeadMap(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
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
	@ServerMethod
	public static Stream<?> findHeadMapStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
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
	@ServerMethod
	public static Stream<?> findHeadMapStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
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
	@ServerMethod
	public static Iterator<?> findHeadMapKV(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
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
	@ServerMethod
	public static Iterator<?> findHeadMapKV(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
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
	@ServerMethod
	public static Stream<?> findHeadMapKVStream(Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
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
	@ServerMethod
	public static Stream<?> findHeadMapKVStream(Alias alias, Comparable<?> darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
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
	@ServerMethod
	public static Iterator<?> findSubMap(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
		return ttm.subMap(darg,marg);
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
	@ServerMethod
	public static Iterator<?> findSubMap(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
		return new TransformingIterator<>(ttm.subMap(darg,marg),v -> RelatrixKVJson.getData((Comparable<?>) v));
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
	@ServerMethod
	public static Stream<?> findSubMapStream(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		BufferedMap ttm = getMap(darg.getClass());
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
	@ServerMethod
	public static Stream<?> findSubMapStream(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, darg.getClass());
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
	@ServerMethod
	public static Iterator<?> findSubMapKV(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(darg.getClass());
		return ttm.subMapKV(darg,marg);
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
	@ServerMethod
	public static Iterator<?> findSubMapKV(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(alias, darg.getClass());
		return ttm.subMapKV(darg,marg);
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
	@ServerMethod
	public static Stream<?> findSubMapKVStream(Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(darg.getClass());
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
	@ServerMethod
	public static Stream<?> findSubMapKVStream(Alias alias, Comparable<?> darg, Comparable<?> marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		BufferedMap ttm = getMap(alias, darg.getClass());
		return ttm.subMapKVStream(darg, marg);
	}
	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Iterator for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> entrySet(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		return ttm.entrySet();//new TransformingIterator<>(ttm.entrySet(),v -> RelatrixKVJson.getData((Comparable<?>) v));
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
	public static Iterator<?> keySet(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		return ttm.keySet();// TransformingIterator<>(ttm.keySet(),v -> RelatrixKVJson.getData((Comparable<?>) v));
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
	public static Object get(Comparable<?> key) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(key.getClass());
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
	@ServerMethod
	public static Object get(Alias alias, Comparable<?> key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, key.getClass());
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
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
	@ServerMethod
	public static Object lastKey(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.lastKey();
	}
	/**
	 * Return the instance having the value for  the greatest key.
	 * @param clazz the class to retrieve
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object lastValue(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		return ttm.last();
	}
	/**
	 * Return the instance having the value for  the greatest key.
	 * @param alias The database alias
	 * @param clazz the class to retrieve
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException If the alias was not found
	 */
	@ServerMethod
	public static Object lastValue(Alias alias, Class<?> clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.last();
	}
	/**
	 * Size of all elements
	 * @param clazz the class to retrieve
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static long size(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		return ttm.size();
	}
	/**
	 * Size of all elements
	 * @param alias The database alias
	 * @param clazz the class to retrieve
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias was not found 
	 */
	@ServerMethod
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
	@ServerMethod
	public static boolean contains(Comparable<?> obj) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(obj.getClass());
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
	@ServerMethod
	public static boolean contains(Alias alias, Comparable<?> obj) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(alias, obj.getClass());
		return ttm.containsKey(obj);
	}

	/**
	 * Is the value object present
	 * @param obj the object with equals
	 * @return boolean true if found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
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
	@ServerMethod
	public static boolean containsValue(Alias alias, Class<?> clazz, Comparable obj) throws IOException, IllegalAccessException, NoSuchElementException
	{
		BufferedMap ttm = getMap(alias, clazz);
		return ttm.containsValue(obj);
	}
	/**
	 * Return the key/value pair of Map.Entry implementation of the closest key to the passed key template.
	 * May be exact match Up to user. Essentially starts a tailMapKv iterator seeking nearest key.
	 * @param key target key template
	 * @return null if no next for initial iteration
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static Object nearest(Comparable<?> key) throws IllegalAccessException, IOException {
		BufferedMap ttm = getMap(key.getClass());
		return ttm.nearest(key);
	}
	/**
	 * Return the key/value pair of Map.Entry implementation of the closest key to the passed key template.
	 * May be exact match Up to user. Essentially starts a tailMapKv iterator seeking nearest key.
	 * @param alias the database alias
	 * @param key target key template
	 * @return null if no next for initial iteration
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static Object nearest(Alias alias, Comparable<?> key) throws IllegalAccessException, IOException, NoSuchElementException {
		BufferedMap ttm = getMap(alias,key.getClass());
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
	@ServerMethod
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
	@ServerMethod
	public static void close(Class<?> clazz) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		DatabaseManager.removeMap(ttm);
	}

}

