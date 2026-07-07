package com.neocoretechs.relatrix;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
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
import com.neocoretechs.rocksack.DirectByteArrayOutputStream;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.SerializedComparatorFactory;

import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.DatabaseManager;

import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.client.asynch.json.AsynchRelatrixKVClientJson;
import com.neocoretechs.relatrix.client.json.util.JsonRecordClassGenerator;
import com.neocoretechs.relatrix.client.json.util.RelatrixTypeSynthesizer;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.BytecodeNotFoundInRepositoryException;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.ClassNameAndBytes;
import com.neocoretechs.relatrix.server.Bytecodes;
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
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	private static ConcurrentHashMap<String, BufferedMap> mapCache = new ConcurrentHashMap<String, BufferedMap>();
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private RelatrixKVJson() {}
	// 2.) volatile instance
	private static volatile RelatrixKVJson instance = null;
	public static HandlerClassLoader classLoader = null;
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
					IndexResolver.setLocal();
				} catch (IllegalAccessException | IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return instance;
	}
	/**
	 * Create an instance of the server as a remote client, in effect. The client process
	 * become the conduit to the remote bytecode repository.
	 * @param cnti The client we have spun up in an application, it will stay pinned as our pipeline
	 * @return The instance of this client process.
	 */
	public static RelatrixKVJson getInstance(ClientNonTransactionInterface cnti) {
		synchronized(RelatrixKVJson.class) {
			if(instance == null) {
				instance = new RelatrixKVJson();
				classLoader = new HandlerClassLoader();
				AsynchRelatrixKVClientJson cntx;
				try {
					cntx = new AsynchRelatrixKVClientJson(((AsynchRelatrixKVClientJson)cnti).getRemoteNode(), ((AsynchRelatrixKVClientJson)cnti).getRemotePort());
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				Thread.currentThread().setContextClassLoader(classLoader);
				SerializedComparatorFactory.setClassLoader(classLoader);
				try {
					HandlerClassLoader.connectToRemoteRepository(cntx);
					IndexResolver.setRemote(cntx);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return instance;
	}
	
	// ... compare() unchanged except it calls deserializeObject(b, loader)
	public static Object deserializeObject(byte[] obuf) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(obuf);
				ObjectInputStream ois = new ClassLoaderObjectInputStream(bais, classLoader)) {
			return ois.readObject();
		} catch (ClassNotFoundException cnf) {
			throw new IOException(cnf.toString() + ":Class Not found, may have been modified beyond version compatibility");
		} catch (IOException ioe) {
			throw new IOException("deserializeObject: " + ioe.toString() + ": from buffer of length " + obuf.length);
		}
	}

	/**
	 * Static method for object to serialized byte conversion.
	 * Uses DirectByteArrayOutputStream, which allows underlying buffer to be retrieved without
	 * copying entire backing store
	 * @param Ob the user object
	 * @return byte buffer containing serialized data
	 * @exception IOException cannot convert
	 */
	public static byte[] serializeObject(Object Ob) throws IOException {
		byte[] retbytes;
		DirectByteArrayOutputStream baos = new DirectByteArrayOutputStream();
		ObjectOutput s = new ObjectOutputStream(baos);
		s.writeObject(Ob);
		s.flush();
		baos.flush();
		retbytes = baos.getBuf();
		s.close();
		baos.close();
		return retbytes;
	}

	// inner static helper
	public static final class ClassLoaderObjectInputStream extends ObjectInputStream {
		private final ClassLoader loader;
		public ClassLoaderObjectInputStream(InputStream in, ClassLoader loader) throws IOException {
			super(in);
			this.loader = loader;
		}
		@Override
		protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			String name = desc.getName();
			try {
				return Class.forName(name, false, loader);
			} catch (ClassNotFoundException ex) {
				return super.resolveClass(desc);
			}
		}
        @Override
        protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
            Class<?>[] intfs = new Class<?>[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                intfs[i] = Class.forName(interfaces[i], false, classLoader);
            }
            try {
                return Proxy.getProxyClass(classLoader, intfs);
            } catch (IllegalArgumentException e) {
                return super.resolveProxyClass(interfaces);
            }
        }
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
	
	public static Class<?> getClassType(Alias alias, JSONObject jsono) throws IllegalAccessException, IOException, ClassNotFoundException {
		BufferedMap bm = getJsonClass(alias, jsono);
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
	
	public static Comparable<?> getObject(Alias alias, JSONObject json) throws IllegalAccessException, IOException, ClassNotFoundException {
		BufferedMap bm = getJsonClass(alias, json);
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
	static Comparable<?> getObject(BufferedMap bm) throws IllegalAccessException, IOException {
		Class<?> c;
		try {
			c = Class.forName(bm.getClassName(), false, classLoader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	   	CborBuilder cb = new CborBuilder();
    	byte[] encodedBytes;
		try {
			encodedBytes = RelatrixTypeSynthesizer.generateMorphicPayload(cb);
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
	public static Map.Entry<String,Object> decodeData(Map.Entry<Comparable,Object> c) {
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
	public static Map.Entry<JSONObject,Object> decodeJsonData(Map.Entry<Comparable,Object> c) {
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
		return new TransformingIterator<>(it,v -> getData((Comparable<?>) v));
	}
	/**
	 * Transform a morphic class iterator to a JSONObject iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed Iterator
	 */
	public static Iterator<?> getJsonIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,v -> getJsonData((Comparable<?>) v));
	}

	/**
	 * Transform a morphic class stream into a String stream using map and getData
	 * @param s The original Stream
	 * @return The transformed Stream
	 */
	public static Stream<?> getStringStream(Stream<?> s) {
		return s.map(e->getData((Comparable<?>)e));
	}
	
	/**
	 * Transform a morphic class stream into a Json stream using map and getData
	 * @param s The original Stream
	 * @return The transformed Stream
	 */
	public static Stream<?> getJsonStream(Stream<?> s) {
		return s.map(e->getJsonData((Comparable<?>)e));
	}

	/**
	 * Transform a morphic class iterator to a String key map iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed iterator
	 */
	public static Iterator<?> getStringMapIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,e->decodeData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class iterator to a JSONObject iterator using TransformingIterator
	 * @param it The original iterator
	 * @return The transformed iterator
	 */
	public static Iterator<?> getJsonMapIterator(Iterator<?> it) {
		return new TransformingIterator<>(it,e->decodeJsonData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class stream into a String stream using map and getData
	 * @param s
	 * @return
	 */
	public static Stream<?> getStringMapStream(Stream<?> s) {
		return s.map(e->decodeData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Transform a morphic class stream into a Json stream using map and getData
	 * @param s The original morphic class key map Stream
	 * @return The transformed Stream of JSONObject keyed maps 
	 */
	public static Stream<?> getJsonMapStream(Stream<?> s) {
		return s.map(e->decodeJsonData((Map.Entry<Comparable,Object>) e));
	}
	/**
	 * Obtain the BufferedMap for the morphic class represented by the JSONObject passed.
	 * @param jsono the JSONObject containing the fields that define a morphic class
	 * @return The BufferedMap that facilitates storage/retrieval of morphic class instances
	 * @throws IllegalAccessException If the class cannot be constructed
	 * @throws IOException If the underlying storage subsystem fails
	 */
	public static BufferedMap getJsonClass(JSONObject jsono) throws IllegalAccessException, IOException {
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
	public static BufferedMap getJsonClass(Alias alias, JSONObject jsono) throws IllegalAccessException, IOException {
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
	
	public static Object[] getKeyValue(Object key, Object value) throws IOException {
		Comparable<?> jkey;
		Object jvalue;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				BufferedMap ttm = getJsonClass(jsonod);
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
				BufferedMap ttm = getJsonClass(jsonod);
				jvalue = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else
			jvalue = value;
		return new Object[] {jkey, jvalue};
	}
	
	public static Object[] getKeyValue(Alias alias, Object key, Object value) throws IOException {
		Comparable<?> jkey;
		Object jvalue;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				BufferedMap ttm = getJsonClass(alias, jsonod);
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
				BufferedMap ttm = getJsonClass(alias, jsonod);
				jvalue = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else
			jvalue = value;
		return new Object[] {jkey, jvalue};
	}
	
	public static class WorkingSet {
		BufferedMap bm;
		public Comparable<?> item; 
	}
	
	public static WorkingSet getWorkingSet(Object key) throws IOException, IllegalAccessException {
		WorkingSet ws = new WorkingSet();
		Comparable<?> jkey;
		BufferedMap ttm;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				ttm = getJsonClass(jsonod);
				jkey = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key instanceof Comparable<?>) {
				jkey = (Comparable<?>)key;
				ttm = getMap(jkey.getClass());
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		ws.bm = ttm;
		ws.item = jkey;
		return ws;
	}
	public static WorkingSet getWorkingSet(Alias alias, Object key) throws IOException, IllegalAccessException {
		WorkingSet ws = new WorkingSet();
		Comparable<?> jkey;
		BufferedMap ttm;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				ttm = getJsonClass(alias, jsonod);
				jkey = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key instanceof Comparable<?>) {
				jkey = (Comparable<?>)key;
				ttm = getMap(alias, jkey.getClass());
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		ws.bm = ttm;
		ws.item = jkey;
		return ws;
	}
	
	public static class WorkingSet2 {
		BufferedMap bm, bm2;
		public Comparable<?> item; 
		public Comparable<?> item2;
	}
	
	public static WorkingSet2 getWorkingSet2(Object key, Object key2) throws IOException, IllegalAccessException {
		WorkingSet2 ws = new WorkingSet2();
		Comparable<?> jkey, jkey2;
		BufferedMap ttm, ttn;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				ttm = getJsonClass(jsonod);
				jkey = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key instanceof Comparable<?>) {
				jkey = (Comparable<?>)key;
				ttm = getMap(jkey.getClass());
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		if(key2 instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key2;
			try {
				ttn = getJsonClass(jsonod);
				jkey2 = getObject(ttn);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key2 instanceof Comparable<?>) {
				jkey2 = (Comparable<?>)key2;
				ttn = getMap(jkey2.getClass());
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		ws.bm = ttm;
		ws.item = jkey;
		ws.bm2 = ttn;
		ws.item2 = jkey2;
		return ws;
	}
	public static WorkingSet2 getWorkingSet2(Alias alias, Object key, Object key2) throws IOException, IllegalAccessException {
		WorkingSet2 ws = new WorkingSet2();
		Comparable<?> jkey, jkey2;
		BufferedMap ttm, ttn;
		if(key instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key;
			try {
				ttm = getJsonClass(alias, jsonod);
				jkey = getObject(ttm);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key instanceof Comparable<?>) {
				jkey = (Comparable<?>)key;
				ttm = getMap(alias, jkey.getClass());
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		if(key2 instanceof JSONObject) {
			JSONObject jsonod = (JSONObject)key2;
			try {
				ttn = getJsonClass(alias, jsonod);
				jkey2 = getObject(ttn);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		} else {
			if(key2 instanceof Comparable<?>) {
				jkey2 = (Comparable<?>)key2;
				ttn = getMap(alias, jkey2.getClass());
			} else {
				throw new IOException("Type must be JSONObject or Comparable, found:"+key+" of type:"+key.getClass());
			}
		}
		ws.bm = ttm;
		ws.bm2 = ttn;
		ws.item = jkey;
		ws.item2 = jkey2;
		return ws;
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
		BufferedMap t = mapCache.get(cjson+alias.getAlias());
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
			mapCache.put(cjson+alias.getAlias(), t);
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
	 * @param key
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static void store(Object key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		Object[] tuple = getKeyValue(key, value);
		BufferedMap ttm = getMap(tuple[0].getClass());
		if( DEBUG  )
			System.out.println("RelatrixKVJson.store storing key:"+tuple[0]+" value:"+tuple[1]);
		ttm.put((Comparable<?>)tuple[0], tuple[1]);
	}
	
	/**
	 * Store our permutations of the key/value
	 * @param alias The database alias
	 * @param key 
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static void store(Alias alias, Object key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		Object[] tuple = getKeyValue(alias, key, value);
		if( DEBUG  )
			System.out.println("RelatrixKVJson.store storing key:"+tuple[0]+" value:"+tuple[1]+" alias:"+alias);
		BufferedMap ttm = getMap(alias, tuple[0].getClass());
		ttm.put((Comparable<?>)tuple[0], tuple[1]);
	}
	
	@ServerMethod
	public static void storekv(Alias alias, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		if(key instanceof Bytecodes && value instanceof ClassNameAndBytes) {
			classLoader.setBytesInRepository(((Bytecodes)key).toString(),((ClassNameAndBytes)value).getBytes());
			return;
		}
		BufferedMap ttm = getMap(alias, key.getClass());
		if( DEBUG  )
			System.out.println("RelatrixKVJson.storekv storing key:"+key+" value:"+value+" in map:"+ttm+" for alias "+alias);
		ttm.put(key, value);
	}
	
	@ServerMethod
	public static void storekv(Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		if(key instanceof Bytecodes && value instanceof ClassNameAndBytes) {
			classLoader.setBytesInRepository(((Bytecodes)key).toString(),((ClassNameAndBytes)value).getBytes());
			return;
		}
		BufferedMap ttm = getMap(key.getClass());
		if( DEBUG  )
			System.out.println("RelatrixKVJson.storekv storing key:"+key+" value:"+value+" in map:"+ttm);
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
		try {
			HandlerClassLoader.removeBytesInRepository(pack);
		} catch (BytecodeNotFoundInRepositoryException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Delete element with given key that this object participates in
	 * @param c The key
	 * @return the previous value for the removed key or null if no key was found
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	@ServerMethod
	public static Object remove(Object c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVJson.remove prepping to remove:"+c);
		WorkingSet ws = getWorkingSet(c);
		return ws.bm.remove(ws.item);
	}
	/**
	 * Delete element with given key that this object participates in
	 * @param c The key
	 * @return the previous value for removed key or null if no key was found to remove
	 * @exception IOException low-level access or problems modifying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	@ServerMethod
	public static Object remove(Alias alias, Object c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixKVJson.remove prepping to remove:"+c);
		WorkingSet ws = getWorkingSet(alias, c);
		return ws.bm.remove(ws.item);
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
	public static Iterator<?> findTailMap(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.tailMap(ws.item);
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
	public static Iterator<?> findTailMap(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.tailMap(ws.item);
	}

	/**
	 * Retrieve from the targeted relationship. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and returns the value elements
	 * @param darg Object marking start of retrieval
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapStream(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.tailMapStream(ws.item);//.map(e->RelatrixKVJson.getData((Comparable<?>)e));
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapStream(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.tailMapStream(ws.item);//.map(e->RelatrixKVJson.getData((Comparable<?>)e));
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
	public static Iterator<?> findTailMapKV(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.tailMapKV(ws.item);//.map(e->RelatrixKVJson.getData((Comparable<?>)e));
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
	public static Iterator<?> findTailMapKV(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.tailMapKV(ws.item);//.map(e->RelatrixKVJson.getData((Comparable<?>)e));
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param darg key
	 * @exception IOException low-level access 
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapKVStream(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.tailMapKVStream(ws.item);
	}
	/**
	 * Returns a view of the portion of this set whose Key/Value elements are greater than or equal to key.
	 * @param alias The database alias
	 * @param darg Object for key
	 * @exception IOException low-level access
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias is not found 
	 * @return The Stream from which the KV data may be retrieved. Follows Stream interface, return Stream<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailMapKVStream(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.tailMapKVStream(ws.item);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param darg The key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The Iterator from which data may be retrieved. Fulfills Iterator interface.
	 */
	@ServerMethod
	public static Iterator<?> findHeadMap(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		// check for at least one object reference in our headset factory
		return ws.bm.headMap(ws.item);
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
	public static Iterator<?> findHeadMap(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		// check for at least one object reference in our headset factory
		return ws.bm.headMap(ws.item);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param darg key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which data may be consumed. Fulfills Stream interface.
	 */
	@ServerMethod
	public static Stream<?> findHeadMapStream(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.headMapStream(ws.item);
	}
	/**
	 * Retrieve the given set of values from the start of the elements to the given key.
	 * @param alias The database alias
	 * @param darg key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias is not found
	 * @return Stream from which data may be consumed. Fulfills Stream interface.
	 */
	@ServerMethod
	public static Stream<?> findHeadMapStream(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.headMapStream(ws.item);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param darg The key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
	 */
	@ServerMethod
	public static Iterator<?> findHeadMapKV(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.headMapKV(ws.item);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param alias The database alias
	 * @param darg The key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias is not ofund
	 * @return Iterator from which KV entry data may be retrieved. Fulfills Iterator interface.
	 */
	@ServerMethod
	public static Iterator<?> findHeadMapKV(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.headMapKV(ws.item);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param darg key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
	 */
	@ServerMethod
	public static Stream<?> findHeadMapKVStream(Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(darg);
		return ws.bm.headMapKVStream(ws.item);
	}
	/**
	 * Retrieve the given set of Key/Value relationships from the start of the elements to the given key
	 * @param alias The database alias
	 * @param darg key
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias is not found
	 * @return Stream from which KV data may be consumed. Fulfills Stream interface.
	 */
	@ServerMethod
	public static Stream<?> findHeadMapKVStream(Alias alias, Object darg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, darg);
		return ws.bm.headMapKVStream(ws.item);
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
	public static Iterator<?> findSubMap(Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet2 ws = getWorkingSet2(darg, marg);
		return ws.bm.subMap(ws.item, ws.item2);
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
	public static Iterator<?> findSubMap(Alias alias, Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet2 ws = getWorkingSet2(alias, darg, marg);
		return ws.bm.subMap(ws.item, ws.item2);
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
	public static Stream<?> findSubMapStream(Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet2 ws = getWorkingSet2(darg, marg);
		return ws.bm.subMapStream(ws.item, ws.item2);
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
	public static Stream<?> findSubMapStream(Alias alias, Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet2 ws = getWorkingSet2(alias, darg, marg);
		return ws.bm.subMapStream(ws.item, ws.item2);
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
	public static Iterator<?> findSubMapKV(Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet2 ws = getWorkingSet2(darg, marg);
		return ws.bm.subMapKV(ws.item, ws.item2);
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
	public static Iterator<?> findSubMapKV(Alias alias, Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet2 ws = getWorkingSet2(alias, darg, marg);
		return ws.bm.subMapKV(ws.item, ws.item2);
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
	public static Stream<?> findSubMapKVStream(Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		WorkingSet2 ws = getWorkingSet2(darg, marg);
		return ws.bm.subMapKVStream(ws.item, ws.item2);
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
	public static Stream<?> findSubMapKVStream(Alias alias, Object darg, Object marg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet2 ws = getWorkingSet2(alias, darg, marg);
		return ws.bm.subMapKVStream(ws.item, ws.item2);
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
	public static Object get(Object key) throws IOException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(key);
		Object o = ws.bm.get(ws.item);
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
	public static Object get(Alias alias, Object key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		WorkingSet ws = getWorkingSet(alias, key);
		Object o = ws.bm.get(ws.item);
		if( o == null )
			return null;
		return ((KeyValue)o).getmValue();
	}
	/**
	 * Return the Object pointed to by the DBKey. this is to support remote iterators.
	 * @param key the key to retrieve
	 * @return The instance by DBKey
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Object getByIndex(DBKey key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return get(key);
	}
	/**
	 * Return the Object pointed to by the DBKey. this is to support remote iterators.
	 * @param alias the db alias
	 * @param key the key to retrieve
	 * @return The instance by DBKey
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Object getByIndex(Alias alias, DBKey key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return get(alias,key);
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
	 * @param obj The key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static boolean contains(Object obj) throws IOException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(obj);
		return ws.bm.containsKey(ws.item);
	}
	/**
	 * Is the key contained in the dataset
	 * @param alias The database alias
	 * @param obj The key to search for
	 * @return true if key is found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException If the alias is not found
	 */
	@ServerMethod
	public static boolean contains(Alias alias, Object obj) throws IOException, IllegalAccessException
	{
		WorkingSet ws = getWorkingSet(alias, obj);
		return ws.bm.containsKey(ws.item);
	}

	/**
	 * Is the value object present
	 * @param clazz The class of the key portion
	 * @param obj the object with equals to match value
	 * @return boolean true if found
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static boolean containsValue(Class<?> clazz, Object obj) throws IOException, IllegalAccessException
	{
		BufferedMap ttm = getMap(clazz);
		WorkingSet ws = getWorkingSet(obj);
		return ttm.containsValue(ws.item);
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
		WorkingSet ws = getWorkingSet(obj);
		return ttm.containsValue(ws.item);
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
	public static Object nearest(Object key) throws IllegalAccessException, IOException {
		WorkingSet ws = getWorkingSet(key);
		return ws.bm.nearest(ws.item);
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
	public static Object nearest(Alias alias, Object key) throws IllegalAccessException, IOException, NoSuchElementException {
		WorkingSet ws = getWorkingSet(alias, key);
		return ws.bm.nearest(ws.item);
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

