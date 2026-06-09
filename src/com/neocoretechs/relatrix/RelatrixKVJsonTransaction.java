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
import org.rocksdb.RocksDBException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.SerializedComparatorFactory;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.rocksack.session.TransactionalMap;

import com.neocoretechs.relatrix.client.json.util.JsonRecordClassGenerator;
import com.neocoretechs.relatrix.client.json.util.RelatrixTypeSynthesizer;

import com.neocoretechs.relatrix.server.BytecodeNotFoundInRepositoryException;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.ServerMethod;

/**
* Top-level class that imparts behavior to the Key/Value JSON subclasses which contain references for key/value.
* The methods here are all performed in a transaction context and require a transaction id.
* The transaction id is returned through a method call to the RockSackAdapter that returns a standard UUID.
* The compareTo and fullCompareTo provide the comparison methods to drive the processes.
* The retrieval operators allow us to form the partially ordered result sets that are returned.<br/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021,2022,2023,2024,2026
*/
public final class RelatrixKVJsonTransaction {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	private static ConcurrentHashMap<String, TransactionalMap> mapCache = new ConcurrentHashMap<String, TransactionalMap>();
	private static HandlerClassLoader classLoader = null;
	public static boolean optimisticConcurrency = true;
	
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private RelatrixKVJsonTransaction() {
	}
	// 2.) volatile instance
	private static volatile RelatrixKVJsonTransaction instance = null;
	// 3.) lock class, assign instance if null
	public static RelatrixKVJsonTransaction getInstance() {
		synchronized(RelatrixKVJsonTransaction.class) {
			if(instance == null) {
				instance = new RelatrixKVJsonTransaction();
				classLoader = new HandlerClassLoader();
				Thread.currentThread().setContextClassLoader(classLoader);
				SerializedComparatorFactory.setClassLoader(classLoader);
				try {
					HandlerClassLoader.connectToLocalRepository(null);
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
	
	public static Class<?> getClassType(JSONObject jsono, TransactionId xid) throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionalMap tm = getJsonClass(jsono, xid);
		Class<?> c;
		c = Class.forName(tm.getClassName(), false, classLoader);
		if(DEBUG)
			System.out.println("RelatrixKVJsonTransaction.getClassType returning class:"+c+" for map "+tm);
		return c;
	}
	
	public static Comparable<?> getObject(JSONObject json, TransactionId xid) throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionalMap tm = getJsonClass(json, xid);
		return getObject(tm);
	}
	/**
	 * Must call getJsonClass initially! It will define the fields and contents of fields and place them
	 * in the RelatrixTypeSynthesizer.structuralTokens and elements.<p>
	 * Creates a class definition from BufferedMap that represents the morphic class.
	 * @param tm the TransactionalMap with relevant class and database definition
	 * @return The comparable object
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Comparable<?> getObject(TransactionalMap tm) throws IllegalAccessException, IOException {
		Class<?> c;
		try {
			c = Class.forName(tm.getClassName(), false, classLoader);
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
			System.out.println("RelatrixKVJsonTransaction.getData for:"+c+" class:"+c.getClass());
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
	        	System.out.println("RelatrixKVJsonTransaction.TransformingIterator ctor iterator:"+this.src);
	    }
	    @Override public boolean hasNext(){ 
	    	return src.hasNext(); 
	    }
	    @Override public R next(){
	        T t = src.next();
	        if(DEBUG)
	        	System.out.println("RelatrixKVJsonTransaction.TransformingIterator:"+t.getClass().getName()+" "+t);
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
		return new TransformingIterator<>(it,v -> RelatrixKVJsonTransaction.getData((Comparable<?>) v));
	}
	/**
	 * Transform a morphic class iterator to a JSONObject iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed Iterator
	 */
	public static Iterator<?> getJsonIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,v -> RelatrixKVJsonTransaction.getJsonData((Comparable<?>) v));
	}
	/**
	 * Transform a morphic class stream into a String stream using map and getData
	 * @param s The original Stream
	 * @return The transformed Stream
	 */
	public static Stream<?> getStringStream(Stream<?> s) {
		return s.map(e->RelatrixKVJsonTransaction.getData((Comparable<?>)e));
	}
	/**
	 * Transform a morphic class stream into a Json stream using map and getData
	 * @param s The original Stream
	 * @return The transformed Stream
	 */
	public static Stream<?> getJsonStream(Stream<?> s) {
		return s.map(e->RelatrixKVJsonTransaction.getJsonData((Comparable<?>)e));
	}	
	/**
	 * Transform a morphic class iterator to a String key map iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed iterator
	 */
	public static Iterator<?> getStringMapIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,e->RelatrixKVJsonTransaction.getData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class iterator to a JSONObject iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed iterator
	 */
	public static Iterator<?> getJsonMapIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,e->RelatrixKVJsonTransaction.getJsonData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class stream into a String stream using map and getData
	 * @param s
	 * @return
	 */
	public static Stream<?> getStringMapStream(Stream<?> s) {
		return s.map(e->RelatrixKVJsonTransaction.getData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class stream into a Json stream using map and getData
	 * @param s The original morphic class key map Stream
	 * @return The transformed Stream of JSONObject keyed maps 
	 */
	public static Stream<?> getJsonMapStream(Stream<?> s) {
		return s.map(e->RelatrixKVJsonTransaction.getJsonData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Obtain the TransactionalMap for the morphic class represented by the JSONObject passed.
	 * @param jsono the JSONObject containing the fields that define a morphic class
	 * @param xid transaction id
	 * @return The TransactionalMap that facilitates storage/retrieval of morphic class instances
	 * @throws IllegalAccessException If the class cannot be constructed
	 * @throws IOException If the underlying storage subsystem fails
	 */
	public static TransactionalMap getJsonClass(JSONObject jsono, TransactionId xid) throws IllegalAccessException, IOException {
		String cjson = RelatrixTypeSynthesizer.generateMorphicClassName(jsono, JsonRecordClassGenerator.generatedJsonClassPrefix);
		TransactionalMap t = mapCache.get(cjson);
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
			if(optimisticConcurrency)
				t = DatabaseManager.getOptimisticTransactionalMap(c, xid);
			else
				t = DatabaseManager.getTransactionalMap(c, xid);
			mapCache.put(cjson, t);
			return t;
		}
		if(!DatabaseManager.isSessionAssociated(xid, t))
			DatabaseManager.associateSession(xid, t);
		return t;
	}
	/**
	 * Obtain the TransactionalMap for the morphic class represented by the JSONObject passed.
	 * @param alias The alias for the database target
	 * @param jsono the JSONObject containing the fields that define a morphic class
	 * @param xid Transaction id
	 * @return The TransactionalMap that facilitates storage/retrieval of morphic class instances
	 * @throws IllegalAccessException If the class cannot be constructed
	 * @throws IOException If the underlying storage subsystem fails
	 */
	public static TransactionalMap getJsonClass(Alias alias, JSONObject jsono, TransactionId xid) throws IllegalAccessException, IOException {
		String cjson = RelatrixTypeSynthesizer.generateMorphicClassName(jsono, JsonRecordClassGenerator.generatedJsonClassPrefix);
		TransactionalMap t = mapCache.get(cjson+alias.getAlias());
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
				if(optimisticConcurrency)
					t = DatabaseManager.getOptimisticTransactionalMap(alias, c, xid);
				else
					t = DatabaseManager.getTransactionalMap(alias, c, xid);
				mapCache.put(cjson+alias.getAlias(), t);
				return t;
			}
			if(!DatabaseManager.isSessionAssociated(xid, t))
				DatabaseManager.associateSession(xid, t);
			return t;
	}
	
	public static TransactionalMap getMap(Class<?> json, TransactionId xid) throws IllegalAccessException, IOException {
		String cjson = json.getName();
		TransactionalMap t = mapCache.get(cjson);
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
			if(optimisticConcurrency)
				t = DatabaseManager.getOptimisticTransactionalMap(c, xid);
			else
				t = DatabaseManager.getTransactionalMap(c, xid);
			mapCache.put(cjson, t);
		}
		if(!DatabaseManager.isSessionAssociated(xid, t))
			DatabaseManager.associateSession(xid, t);	
		return t;
	}

	public static TransactionalMap getMap(Alias alias, Class<?> json, TransactionId xid) throws IllegalAccessException, IOException {
		String cjson = json.getName();
		TransactionalMap t = mapCache.get(cjson+alias.getAlias());
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
			if(optimisticConcurrency)
				t = DatabaseManager.getOptimisticTransactionalMap(alias, c, xid);
			else
				t = DatabaseManager.getTransactionalMap(alias, c, xid);
			mapCache.put(cjson+alias.getAlias(), t);
		}
		if(!DatabaseManager.isSessionAssociated(xid, t))
			DatabaseManager.associateSession(xid, t);
		return t;
	}
	
	public static void setOptimisticConcurrency(boolean optimistic) {
		optimisticConcurrency = optimistic;
	}
	
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespace(String path) throws IOException {
		DatabaseManager.setTableSpaceDir(path);
	}
	
	/**
	 * Get the default tablespace directory
	 * @return the path/dbname of current default tablespace
	 */
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
	 * @param alias the alias to which a path is assigned
	 * @return the path to this alias, null if alias does not exist.
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
	 * @return the transaction id
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@ServerMethod
	public static TransactionId getTransactionId() throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionId xid = new TransactionId(DatabaseManager.getTransactionId().getTransactionId());
		return xid;
	}
	/**
	 * @param the lock timeout in milliseconds
	 * @return the transaction id as a LockingTransactionId subclass of TransactionId
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@ServerMethod
	public static TransactionId getTransactionId(long timeout) throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionId xid =  new TransactionId(DatabaseManager.getTransactionId(timeout).getTransactionId());
		return xid;
	}
	/**
	 * @param xid the transaction id
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static void endTransaction(TransactionId xid) throws IOException {
		DatabaseManager.endTransaction(xid);
	}	
	
	@ServerMethod
	public static synchronized void rollbackAllTransactions() {
		DatabaseManager.clearAllOutstandingTransactions();
	}
	
	@ServerMethod
	public static synchronized void rollbackTransaction(TransactionId uid) throws IOException {
		try {
			DatabaseManager.clearOutstandingTransaction(uid);
		} catch (RocksDBException e) {
			throw new IOException(e);
		}
	}
	
	@ServerMethod
	public static synchronized Object[] getTransactionState() {
		return DatabaseManager.getOutstandingTransactionState().toArray();
	}
	
	/**
	 * Store our permutations of the key/value
	 * @param xid transaction id
	 * @param key of comparable
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static void store(TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		JSONObject jsono = new JSONObject(String.valueOf(key));
		Comparable<?> jkey;
		Object jvalue;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				TransactionalMap ttm = getJsonClass(jsonod, xid);
				jkey = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key instanceof Comparable<?>) {
				jkey = (Comparable<?>)key;
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		if(value instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)value;
			try {
				TransactionalMap ttm = getJsonClass(jsonod, xid);
				jvalue = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else
			jvalue = value;
		if( DEBUG  )
			System.out.println("RelatrixKVJsonTransaction.store storing key:"+jkey+" value:"+value);
		storekv(xid, jkey, value);
	}
	
	@ServerMethod
	public static void storekv(TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		TransactionalMap ttm = getMap(key.getClass(), xid);
		if( DEBUG  )
			System.out.println("RelatrixKVJsonTransaction.storekv storing key:"+key+" value:"+value+" in map:"+ttm);
		ttm.put(xid, key, value);
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
	public static void store(Alias alias, TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		JSONObject jsono = new JSONObject(String.valueOf(key));
		Comparable<?> jkey;
		Object jvalue;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				TransactionalMap ttm = getJsonClass(alias, jsonod, xid);
				jkey = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key instanceof Comparable<?>) {
				jkey = (Comparable<?>)key;
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		if(value instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)value;
			try {
				TransactionalMap ttm = getJsonClass(alias, jsonod, xid);
				jvalue = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else
			jvalue = value;
		if( DEBUG  )
			System.out.println("RelatrixKVJsonTransaction.store storing key:"+jkey+" value:"+value+" for alias "+alias);
		storekv(alias, xid, jkey, value);
	}
	
	@ServerMethod
	public static void storekv(Alias alias, TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		TransactionalMap ttm = getMap(alias, key.getClass(), xid);
		if( DEBUG  )
			System.out.println("RelatrixKVJsonTransaction.storekv storing key:"+key+" value:"+value+" in map:"+ttm+" for alias "+alias);
		ttm.put(xid, key, value);
	}
	
	/**
	 * Commit the outstanding transaction data in each active class.
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void commit(TransactionId xid) throws IOException, IllegalAccessException {
		long startTime = System.currentTimeMillis();
		DatabaseManager.commitTransaction(xid);
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
	@ServerMethod
	public static void commit(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		long startTime = System.currentTimeMillis();
		DatabaseManager.commitTransaction(alias, xid);
		if( DEBUG || TRACE )
			System.out.println("Committed transaction:"+xid+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	
	/**
	 * Rollback the outstanding transaction data in each active class.
	 * @param transactionId the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void rollback(TransactionId transactionId) throws IOException, IllegalAccessException {
		long startTime = System.currentTimeMillis();
		DatabaseManager.rollbackTransaction(transactionId);
		if( DEBUG || TRACE )
			System.out.println("Rolled back transaction:"+transactionId+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}
	/**
	 * Rollback the outstanding transaction data in each active class.
	 * @param alias the database alias
	 * @param transactionId the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if alias not found
	 */
	@ServerMethod
	public static void rollback(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		long startTime = System.currentTimeMillis();
		DatabaseManager.rollbackTransaction(alias, transactionId);
		if( DEBUG || TRACE )
			System.out.println("Rolled back transaction:"+transactionId+" in " + (System.currentTimeMillis() - startTime) + "ms.");		
	}

	/**
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void checkpoint(TransactionId xid) throws IOException, IllegalAccessException {
		DatabaseManager.checkpointTransaction(xid);
	}
	/**
	 * @param alias the database alias
	 * @param transactionId transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	@ServerMethod
	public static void checkpoint(Alias alias, TransactionId transactionId) throws IOException, IllegalAccessException, NoSuchElementException {
		DatabaseManager.checkpointTransaction(alias, transactionId);
	}
	/**
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void rollbackToCheckpoint(TransactionId xid) throws IOException, IllegalAccessException {
		DatabaseManager.rollbackToCheckpoint(xid);
	}
	/**
	 * @param alias the database alias
	 * @param xid transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException If the alias was not found
	 */
	@ServerMethod
	public static void rollbackToCheckpoint(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		DatabaseManager.rollbackToCheckpoint(alias, xid);
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
	 * @param transactionId the transaction id
	 * @param c The Comparable key
	 * @return the previous value for removed key, or null if no key was found to remove
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	@ServerMethod
	public static Object remove(TransactionId transactionId, Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(c.getClass(), transactionId);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVTransaction.remove prepping to remove:"+c);
		return ttm.remove(transactionId, c);
	}
	/**
	 * Delete element with given key that this object participates in
	 * @param alias the database alias
	 * @param transactionId the transaction id
	 * @param c The Comparable key
	 * @return the previous value for removed key or null if no key was found to remove
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	@ServerMethod
	public static Object remove(Alias alias, TransactionId transactionId, Comparable c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, c.getClass(), transactionId);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVTransaction.remove prepping to remove:"+c);
		return ttm.remove(transactionId, c);
	}

	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param xid the transaction id
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMap(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.tailMap(xid, darg);
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Iterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMap(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.tailMap(xid, darg);
	}

	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param xid the transaction ID
	 * @param darg Comparable marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapStream(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.tailMapStream(xid, darg);
	}
	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param alias the database alias
	 * @param xid the transaction ID
	 * @param darg Comparable marking start of retrieval
	 * @exception IOException low-level access 
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapStream(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.tailMapStream(xid, darg);
	}

	/**
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param xid the transaction ID
	 * @param darg Object for key of relationship
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMapKV(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.tailMapKV(xid, darg);
	}
	/**
	 * Retrieve from the targeted Key/Value relationship from given key.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * @param alias the database alias
	 * @param xid the transaction ID
	 * @param darg Object for key of relationship
	 * @exception IOException low-level access 
	 * @exception IllegalArgumentException At least one argument must be a valid object reference
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The RelatrixIterator from which the KV data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailMapKV(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.tailMapKV(xid, darg);
	}

	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param xid the transaction ID
	 * @param darg Comparable for key
	 * @param parallel Optional true to execute parallel stream
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapKVStream(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.tailMapKVStream(xid, darg);
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param alias the database alias
	 * @param xid the transaction ID
	 * @param darg Comparable for key
	 * @param parallel Optional true to execute parallel stream
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapKVStream(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.tailMapKVStream(xid, darg);
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
	@ServerMethod
	public static Iterator<?> findHeadMap(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.headMap(xid, darg);
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
	@ServerMethod
	public static Iterator<?> findHeadMap(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.headMap(xid, darg);
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
	@ServerMethod
	public static Stream<?> findHeadMapStream(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.headMapStream(xid, darg);
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
	@ServerMethod
	public static Stream<?> findHeadMapStream(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.headMapStream(xid, darg);
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
	@ServerMethod
	public static Iterator<?> findHeadMapKV(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.headMapKV(xid, darg);
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
	@ServerMethod
	public static Iterator<?> findHeadMapKV(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.headMapKV(xid, darg);
	}

	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param xid the transaction id
	 * @param darg Comparable key
	 * @param parallel true for parallel stream
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
	 */
	@ServerMethod
	public static Stream<?> findHeadMapKVStream(TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.headMapKVStream(xid, darg);
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
	@ServerMethod
	public static Stream<?> findHeadMapKVStream(Alias alias, TransactionId xid, Comparable darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.headMapKVStream(xid, darg);
	}

	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first.
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/> 
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	@ServerMethod
	public static Iterator<?> findSubMap(TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.subMap(xid, darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first.
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
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
	@ServerMethod
	public static Iterator<?> findSubMap(Alias alias, TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.subMap(xid, darg, marg);
	}

	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param xid the transaction id 
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which data may be retrieved. Fulfills Stream interface.
	 */
	@ServerMethod
	public static Stream<?> findSubMapStream(TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.subMapStream(xid, darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of keys from the point of the relationship of the first.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
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
	@ServerMethod
	public static Stream<?> findSubMapStream(Alias alias, TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.subMapStream(xid, darg, marg);
	}

	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findSubMapKV(TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// check for at least one object reference
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.subMapKV(xid, darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return The RelatrixIterator from which the Key/Value data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findSubMapKV(Alias alias, TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// check for at least one object reference
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.subMapKV(xid, darg, marg);
	}

	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Stream<?> findSubMapKVStream(TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		TransactionalMap ttm = getMap(darg.getClass(), xid);
		return ttm.subMapKVStream(xid, darg, marg);
	}
	/**
	 * Retrieve the subset of the given set of Key/Value pairs from the point of the  first key, to the end key.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param darg The starting key
	 * @param marg The ending key
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias was not found
	 * @return The Stream from which the Key/Value data may be consumed. Follows Stream interface, return Sterator<Result>
	 */
	@ServerMethod
	public static Stream<?> findSubMapKVStream(Alias alias, TransactionId xid, Comparable darg, Comparable marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// check for at least one object reference
		TransactionalMap ttm = getMap(alias, darg.getClass(), xid);
		return ttm.subMapKVStream(xid, darg, marg);
	}

	/**
	 * Return the entry set for the given class type
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return Iterator for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> entrySet(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.entrySet(xid);
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
	@ServerMethod
	public static Iterator<?> entrySet(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.entrySet(xid);
	}
	/**
	 * Return the entry set for the given class type
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> entrySetStream(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.entrySetStream(xid);
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
	@ServerMethod
	public static Stream<?> entrySetStream(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.entrySetStream(xid);
	}
	/**
	 * Return the keyset for the given class
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> keySet(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.keySet(xid);
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
	@ServerMethod
	public static Iterator<?> keySet(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.keySet(xid);
	}
	/**
	 * Return the keyset for the given class
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return The stream from which keyset can be consumed
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> keySetStream(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.keySetStream(xid);
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
	@ServerMethod
	public static Stream<?> keySetStream(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.keySetStream(xid);
	}
	/**
	 * return lowest valued key.
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the The key/value with lowest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object firstKey(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.firstKey(xid);
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
	@ServerMethod
	public static Object firstKey(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.firstKey(xid);
	}
	/**
	 * Return the value for the key.
	 * @param transactionId the transaction id
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object get(TransactionId transactionId, Comparable key) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(key.getClass(), transactionId);
		Object o = ttm.get(transactionId, key);
		if( o == null )
			return null;
		return ((KeyValue)o).getmValue();
	}
	/**
	 * Return the value for the key.
	 * @param alias the database alias
	 * @param transactionId the transaction id
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	@ServerMethod
	public static Object get(Alias alias, TransactionId transactionId, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, key.getClass(), transactionId);
		Object o = ttm.get(transactionId, key);
		if( o == null )
			return null;
		return ((KeyValue)o).getmValue();
	}
	/**
	 * Return the value for the key.
	 * @param mainClass the class of the tablespace
	 * @param key the key to retrieve subclass of mainClass
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object get(TransactionId xid, Class mainClass, Comparable key) throws IOException, IllegalAccessException
	{
		TransactionalMap ttm = getMap(mainClass, xid);
		Object o = ttm.get(xid, key);
		if( o == null )
			return null;
		return ((KeyValue)o).getmValue();
	}
	/**
	 * Return the value for the key.
	 * @param <T>
	 * @param alias The database alias
	 * @param key the key to retrieve, subclass of mainCLass
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException If the alias is not found
	 */
	@ServerMethod
	public static Object get(Alias alias, TransactionId xid, Class mainClass, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		TransactionalMap ttm = getMap(alias, mainClass, xid);
		Object o = ttm.get(xid, key);
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
	@ServerMethod
	public static Object firstValue(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.first(xid);
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
	@ServerMethod
	public static Object firstValue(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.first(xid);
	}
	/**
	 * Return instance having the highest valued key.
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the The highest value object
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object lastKey(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.lastKey(xid);
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
	@ServerMethod
	public static Object lastKey(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.lastKey(xid);
	}
	/**
	 * Return the instance having the value for the greatest key.
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object lastValue(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.last(xid);
	}
	/**
	 * Return the instance having the value for the greatest key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias was not found
	 */
	@ServerMethod
	public static Object lastValue(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.last(xid);
	}
	/**
	 * Size of all elements
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static long size(TransactionId xid, Class clazz) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(clazz, xid);
		return ttm.size(xid);
	}
	/**
	 * Size of all elements
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	@ServerMethod
	public static long size(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, clazz, xid);
		return ttm.size(xid);
	}
	/**
	 * Is the key contained in the dataset
	 * @param xid the transaction id
	 * @parameter obj The Comparable key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static boolean contains(TransactionId xid, Comparable obj) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(obj.getClass(), xid);
		return ttm.containsKey(xid, obj);
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
	@ServerMethod
	public static boolean contains(Alias alias, TransactionId xid, Comparable obj) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, obj.getClass(), xid);
		return ttm.containsKey(xid, obj);
	}
	/**
	 * Is the key contained in the dataset of given class database for stated subclass
	 * @param <T>
	 * @param xid the transaction id
	 * @param mainClass the class of the tablespace to search
	 * @param subClass The Comparable subclass of tablespace mainClass key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static boolean contains(TransactionId xid, Class mainClass, Comparable subclass) throws IOException, IllegalAccessException
	{
		TransactionalMap ttm = getMap(mainClass, xid);
		return ttm.containsKey(xid, subclass);
	}
	/**
	 * Is the key contained in the dataset of given class database for stated subclass
	 * @param <T>
	 * @param alias The database alias
	 * @param xid the transaction id
	 * @param mainClass the class of tablespace to search
	 * @param subClass The Comparable subclass of tablespace mainClass key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException If the alias is not found
	 */
	@ServerMethod
	public static boolean contains(Alias alias, TransactionId xid, Class mainClass, Comparable subClass) throws IOException, IllegalAccessException
	{
		TransactionalMap ttm = getMap(alias, mainClass, xid);
		return ttm.containsKey(xid, subClass);
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
	@ServerMethod
	public static boolean containsValue(TransactionId xid, Class keyType, Object obj) throws IOException, IllegalAccessException {
		TransactionalMap ttm = getMap(keyType, xid);
		return ttm.containsValue(xid, obj);
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
	@ServerMethod
	public static boolean containsValue(Alias alias, TransactionId xid, Class keyType, Object obj) throws IOException, IllegalAccessException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias, keyType, xid);
		return ttm.containsValue(xid, obj);
	}
	/**
	 * Return the key/val.ue pair of Map.Entry implementation of the closest key to the passed key template.
	 * May be exact match Up to user. Essentially starts a tailMapKv iterator seeking nearest key.
	 * @param xid transaction id
	 * @param key target key template
	 * @return null if no next for initial iteration
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static Object nearest(TransactionId xid, Comparable key) throws IllegalAccessException, IOException {
		TransactionalMap ttm = getMap(key.getClass(), xid);
		return ttm.nearest(xid, key);
	}
	/**
	 * Return the key/val.ue pair of Map.Entry implementation of the closest key to the passed key template.
	 * May be exact match Up to user. Essentially starts a tailMapKv iterator seeking nearest key.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @param key target key template
	 * @return null if no next for initial iteration
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static Object nearest(Alias alias, TransactionId xid, Comparable key) throws IllegalAccessException, IOException, NoSuchElementException {
		TransactionalMap ttm = getMap(alias,key.getClass(),xid);
		return ttm.nearest(xid, key);
	}

	/**
	 * Close and remove database from available set
	 * @param alias
	 * @param xid Transaction id
	 * @param clazz
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	@ServerMethod
	public static void close(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		TransactionalMap ttm = getMap(alias, clazz, xid);
		DatabaseManager.removeTransactionalMap(alias, ttm);
		mapCache.remove(clazz.getName()+alias);
	}
	/**
	 * Close and remove database from available set
	 * @param clazz
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	@ServerMethod
	public static void close(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		TransactionalMap ttm = getMap(clazz, xid);
		DatabaseManager.removeTransactionalMap(xid, ttm);
		mapCache.remove(clazz.getName());
	}


}

