package com.neocoretechs.relatrix;

import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.json.JSONObject;

import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode0Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode1Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode2Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode3Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode4Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode5Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode6Json;
import com.neocoretechs.relatrix.iterator.json.FindHeadSetMode7Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode0Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode1Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode2Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode3Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode4Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode5Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode6Json;
import com.neocoretechs.relatrix.iterator.json.FindSetMode7Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode0Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode1Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode2Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode3Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode4Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode5Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode6Json;
import com.neocoretechs.relatrix.iterator.json.FindSubSetMode7Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode0Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode1Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode2Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode3Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode4Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode5Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode6Json;
import com.neocoretechs.relatrix.iterator.json.FindTailSetMode7Json;
import com.neocoretechs.relatrix.iterator.json.RelatrixIteratorJson;
import com.neocoretechs.relatrix.iterator.json.RelatrixEntrysetIteratorJson;
import com.neocoretechs.relatrix.iterator.json.RelatrixKeysetIteratorJson;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.relatrix.stream.json.RelatrixStreamJson;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.type.Tuple;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.SerializedComparatorFactory;
import com.neocoretechs.rocksack.session.BufferedMap;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
* Top-level class that imparts behavior to the {@link AbstractRelation} subclasses which contain references for domain, map, range.<p>
* The lynch pin is the AbstractRelation and its subclasses indexed
* by the 6 permutations of the domain,map,and range so we can retrieve instances in all
* the potential sort orders.<p>
* The Json implementation creates a dynamic morphic class from the fields of an arbitrary Json payload.<p>
* Various transformation methods are provided to turn the canonical CBOR of the generated class data into JSONObject or
* Comparable String representations.<p>
* The compareTo and fullCompareTo of AbstractRelation provide the comparison methods to drive the processes.
* For retrieval, a partial template is constructed of the proper AbstractRelation subclass which puts the three elements
* in the proper sort order. To retrieve the proper AbstractRelation subclass, partially construct a morphism template to
* order the result set. The retrieval operators allow us to form the partially ordered result sets that are returned.<p>
* The critical concept about retrieving relationships is to remember that the number of elements from each passed
* stream element or iteration of a Stream or Iterator is dependent on the number of "?" operators in a 'findSet'. For example,
* if we declare findHeadSet("*","?","*") we get back a {@link Result} of one element, for findSet("?",object,"?") we
* would get back a {@link Result2}, with each element of the relationship returned.<br>
* If we findHeadStream("*","?","*") we return a stream where one  {@link Result} element can be mapped, reduced, consumed, etc.<br>
* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
* the requirement to be 'categorical'.<p>
* In general, all Streams or '3 element' arrays returned by the operators are
* the mathematical identity. To follow Categorical rules, the unique key in database terms are the first 2 elements, the domain and map,
* since conceptually a AbstractRelation is a domain acted upon by the map function yielding the range.<p>
* A given domain run through a 'map function' always yields the same range, 
* as any function that processes an element yields one consistent result.<p>
* The morphism components are indexed by a {@link com.neocoretechs.relatrix.key.DBKey} that contains a reference to the instance
* within that database as a (UUID). <p> The Json permutation uses dynamically generated classes formed from the hash of the field
* names in the target Json. The payload is CBOR encoded in a Seerialiazable, Comparable class.
* Some of this work is based on a DBMS described by Alfonso F. Cardenas and Dennis McLeod (1990). Research Foundations 
* in Object-Oriented and Semantic Database Systems. Prentice Hall.
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism and the works of
* Mac Lane<p>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021,2026
*/
public final class RelatrixJson {
	private static boolean DEBUG = true;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	
	public static Character OPERATOR_WILDCARD_CHAR = '*';
	public static Character OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
  
	private static SynchronizedThreadManager sftpm;
	public static final String storeX = "STOREXTX";
	public static final String storeI = "STOREITX";
	public static final String deleteX = "DELETEXTX";
	public static final String searchX = "SEARCHXTX";
	
	public static final int numMultiStoreThreads = 16;
	public static final String multiStoreX = "MULTISTOREX";
	
	static {
		sftpm = SynchronizedThreadManager.getInstance();
		sftpm.init(new String[] {storeX});
		sftpm.init(new String[] {deleteX});
		sftpm.init(new String[] {storeI});
		sftpm.init(new String[] {searchX});
		sftpm.init(new String[] {multiStoreX});
	}
	
	
	private static Object mutex = new Object();
	
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private RelatrixJson() {
	}
	// 2.) volatile instance
	private static volatile RelatrixJson instance = null;
	// 3.) lock class, assign instance if null
	public static RelatrixJson getInstance() {
		synchronized(RelatrixJson.class) {
			if(instance == null) {
				instance = new RelatrixJson();
				RelatrixKVJson.classLoader = new HandlerClassLoader();
				Thread.currentThread().setContextClassLoader(RelatrixKVJson.classLoader);
				SerializedComparatorFactory.setClassLoader(RelatrixKVJson.classLoader);
				try {
					HandlerClassLoader.connectToLocalRepository(null); // tablespace property
				} catch (IllegalAccessException | IOException e) {
					throw new RuntimeException(e);
				}
				IndexResolver.setLocalJson();
			}
		}
		return instance;
	}	
	
	/**
	* Calling these methods allows the user to substitute their own
	* symbology for the usual Findset semantics. If you absolutely
	* need to store values confusing to the standard findset *,? semantics.
	*/
	@ServerMethod
	public static void setWildcard(Character wc) {
		OPERATOR_WILDCARD_CHAR = wc;
		OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	}
	@ServerMethod
	public static void setTuple(Character tp) {
		OPERATOR_TUPLE_CHAR = tp;
		OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	}
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespace(String path) throws IOException {
		getInstance();
		RelatrixKVJson.setTablespace(path);
	}
	
	@ServerMethod
	public static String getTableSpace() {
		return RelatrixKVJson.getTableSpace();
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
		RelatrixKVJson.setAlias(alias, RelatrixKVJson.getTableSpace()+alias.getAlias());
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @param path
	 * @throws IOException
	 */
	public static void setAlias(Alias alias, String path) throws IOException {
		RelatrixKVJson.setAlias(alias, path);
	}
	/**
	 * Get the tablespace path for this alias. Will return null if alias does not exist
	 * @param alias
	 * @return The tablespace path for the given alias returned as a String.
	 */
	@ServerMethod
	public static String getAlias(Alias alias) {
		return RelatrixKVJson.getAlias(alias);
	}
	
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	@ServerMethod
	public static String[][] getAliases() {
		return RelatrixKVJson.getAliases();
	}
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	@ServerMethod
	public static void removeAlias(Alias alias) throws NoSuchElementException {
		RelatrixKVJson.removeAlias(alias);
	}
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * @param d The payload representing the domain object for this morphism relationship.
	 * @param m The payload representing the map object for this morphism relationship.
	 * @param r The payload representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException if fields or methods are inaccessable
	 * @throws IOException Underlying storage failure
	 * @throws ClassNotFoundException if bytecode repository fails or generation of morphic class fails
	 * @throws DuplicateKeyException if a duplicate key is attempted in any of the morphism elements
	 * @return The identity element of the set - The Relation of stored object composed of d,m,r
	 */
	@ServerMethod
	public static Relation store(Object d, Object m, Object r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		JSONObject jsono;
		Comparable<?> jkeyd, jkeym, jkeyr;
		if(d instanceof JSONObject) {
			jsono = (JSONObject)d;
			BufferedMap ttmm = RelatrixKVJson.getJsonClass(jsono);
			jkeyd = RelatrixKVJson.getObject(ttmm);
		} else {
			if(d instanceof Comparable<?>) {
				jkeyd = (Comparable<?>)d;
			} else {
				throw new IllegalAccessException("Domain type must be JSONObject or Comparable for:"+d+" found:"+d.getClass());
			}
		}
		if(m instanceof JSONObject) {
			jsono = (JSONObject)m;
			BufferedMap ttmm = RelatrixKVJson.getJsonClass(jsono);
			jkeym = RelatrixKVJson.getObject(ttmm);
		} else {
			if(m instanceof Comparable<?>) {
				jkeym = (Comparable<?>)m;
			} else {
				throw new IllegalAccessException("Map type must be JSONObject or Comparable for:"+d+" found:"+d.getClass());
			}
		}
		if(r instanceof JSONObject) {
			jsono = (JSONObject)r;
			BufferedMap ttmm = RelatrixKVJson.getJsonClass(jsono);
			jkeyr = RelatrixKVJson.getObject(ttmm);
		} else {
			if(r instanceof Comparable<?>) {
				jkeyr = (Comparable<?>)m;
			} else {
				throw new IllegalAccessException("Range type must be JSONObject or Comparable for:"+d+" found:"+d.getClass());
			}
		}
		Relation identity = new Relation(); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		PrimaryKeySet pk = PrimaryKeySet.locate(jkeyd, jkeym);
		if(DEBUG)
			System.out.println("RelatrixJson.store PrimaryKeySet:"+pk+" from domain key:"+jkeyd+" map key"+jkeym);
		if(pk.getIdentity() == null) {
			identity.setDomainKey(pk.getDomainKey());
			identity.setMapKey(pk.getMapKey());
			identity.setDomainResolved(jkeyd);
			identity.setMapResolved(jkeym);
			if(DEBUG)
				System.out.println("RelatrixJson.store PrimaryKeySet identity was null, now set to:"+identity);
			DBKey rKey = AbstractRelation.checkMorphism(jkeyr);
			if(rKey == null) {
				identity.setRange(jkeyr);
				if(DEBUG)
					System.out.println("RelatrixJson.store PrimaryKeySet identity rkey was null, identity set to:"+identity);
			} else {
				identity.setRangeKey(rKey);
				identity.setRangeResolved(jkeyr);
				if(DEBUG)
					System.out.println("RelatrixJson.store PrimaryKeySet identity rkey was not null, identity set to:"+identity);
			}
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(identity));
			if(DEBUG)
				System.out.println("RelatrixJson.storeJson setting identity from newKey:"+identity);
		} else
			throw new DuplicateKeyException("Relationship primary key ["+d+"->"+m+"] already exists.");
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		if( DEBUG  )
			System.out.println("RelatrixJson.storeJson calling storeParallel with identity:"+identity+" and pk:"+pk);
		// store the primary, but not in the DBKey table
		storeParallel(identity, pk);
		if(DEBUG)
			System.out.println("RelatrixJson.storeJson returning identity:"+identity);
		return identity;
	}
	
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * @param alias the database alias
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws NoSuchElementException if the alias does not exist
	 * @return The identity element of the set - The Relation of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Relation store(Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		Relation identity = new Relation(); // form it as template for duplicate key search
		identity.setAlias(alias);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		PrimaryKeySet pk = PrimaryKeySet.locate(alias, d, m);
		if(pk.getIdentity() == null) {
			identity.setDomainKey(pk.getDomainKey());
			identity.setMapKey(pk.getMapKey());
			identity.setDomainResolved(d);
			identity.setMapResolved(m);
			DBKey rKey = AbstractRelation.checkMorphism(r);
			if(rKey == null)
				identity.setRange(alias, r);
			else {
				identity.setRangeKey(rKey);
				identity.setRangeResolved(r);
			}
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(alias,identity));
		} else
			throw new DuplicateKeyException("Relationship primary key ["+d+"->"+m+"] already exists.");
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		if( DEBUG  )
			System.out.println("RelatrixJson.store stored :"+identity);
		// store the primary, but not in the DBKey table
		storeParallel(alias, identity, pk);
		return identity;
	}

	/**
	 * Designed to interoperate with {@link Tuple}<p>
	 * Store the set of prepared tuples. Expectes the first tuple to have d, m, r. The remaining tuples
	 * have m, r and the relation of the first tuple will be used as domain. If any duplicate keys occur, a null will be 
	 * returned in the array position of the returned tuple. 
	 * @param tuples the set of prepared tuples, domain, map, range, for first tuple, map range for remaining
	 * @return the set of stored tuples with identity key set and null for duplicates
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@ServerMethod
	public static List store(ArrayList<Comparable[]> tuples) throws IOException, IllegalAccessException, ClassNotFoundException {
		List<Comparable> identities = new RelationList();
		Comparable[] tuple = tuples.get(0);
		Relation identity = new Relation();
		PrimaryKeySet pk = PrimaryKeySet.locate(tuple[0], tuple[1]);
		identity.setDomainKey(pk.getDomainKey());
		identity.setMapKey(pk.getMapKey());
		DBKey rKey = AbstractRelation.checkMorphism(tuple[2]);
		if(rKey == null)
			identity.setRange(tuple[2]);
		else {
			identity.setRangeKey(rKey);
			identity.setRangeResolved(tuple[2]);
		}
		if(pk.getIdentity() == null) {
			identity.setDomainResolved(tuple[0]);
			identity.setMapResolved(tuple[1]);
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(identity));
			storeParallel(identity, pk);
		} else {
			identity.setIdentity(pk.getIdentity());
		}
		identities.add(identity);
		for(int i = 1; i < tuples.size(); i++) {
			tuple = tuples.get(i);
			try {
				identities.add(store(identity, tuple[0], tuple[1]));
			} catch(DuplicateKeyException dke) {}
		}
		return identities;
	}
	/**
	 * Designed to interoperate with {@link Tuple}<p>
	 * Store the set of prepared tuples. Expects the first tuple to have d, m, r. The remaining tuples
	 * have m, r and the relation of the first tuple will be used as domain. If any duplicate keys occur, a null will be 
	 * returned in the array position of the returned tuple. 
	 * @param alias the database alias
	 * @param tuples the set of prepared tuples, domain, map, range, for first tuple, map range for remaining
	 * @return the set of stored tuples with identity key set and null for duplicates
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@ServerMethod
	public static List store(Alias alias, ArrayList<Comparable[]> tuples) throws IOException, IllegalAccessException, ClassNotFoundException {
		List<Comparable> identities = new RelationList();
		Comparable[] tuple = tuples.get(0);
		Relation identity = new Relation();
		identity.setAlias(alias);
		PrimaryKeySet pk = PrimaryKeySet.locate(alias, tuple[0], tuple[1]);
		identity.setDomainKey(pk.getDomainKey());
		identity.setMapKey(pk.getMapKey());
		DBKey rKey = AbstractRelation.checkMorphism(tuple[2]);
		if(rKey == null)
			identity.setRange(alias, tuple[2]);
		else {
			identity.setRangeKey(rKey);
			identity.setRangeResolved(tuple[2]);
		}
		if(pk.getIdentity() == null) {
			identity.setDomainResolved(tuple[0]);
			identity.setMapResolved(tuple[1]);
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(alias, identity));
			storeParallel(alias, identity, pk);
		} else {
			identity.setIdentity(pk.getIdentity());
		}
		identities.add(identity);
		for(int i = 1; i < tuples.size(); i++) {
			tuple = tuples.get(i);
			try {
				identities.add(store(alias, identity, tuple[0], tuple[1]));
			} catch(DuplicateKeyException dke) {}
		}
		return identities;
	}
	/**
	 * Perform multiple store on passed List
	 * @param tuples ArrayList of Comparable domain, map, range arrays
	 * @return RelationList of returned stored element relations, or null for failed store
	 * @throws IOException If low level problem
	 * @throws IllegalAccessException If other low level problem
	 * @throws ClassNotFoundException If the class to store cant be located
	 */
	@ServerMethod
	public static RelationList multiStore(ArrayList<Comparable[]> tuples) throws IOException, IllegalAccessException, ClassNotFoundException {
		   List<Comparable[]> synTuples = Collections.synchronizedList(tuples);
		   Future<?>[] jobs = new Future[synTuples.size()];
		   RelationList returnList = new RelationList();
		   List<Comparable> synReturn = Collections.synchronizedList(returnList);
		   AtomicInteger threadIndex = new AtomicInteger(0);
		   for(int i = 0; i < synTuples.size(); i++) {
		    	jobs[i] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
		    		@Override
		    		public void run() {
		    			Comparable[] dmr = null;
		    			synchronized(synTuples) {
		    				dmr = synTuples.get(threadIndex.getAndIncrement());
		    			}
		    			try {
		    				synchronized(synReturn) {
		    					synReturn.add(store(dmr[0],dmr[1],dmr[2]));
		    				}
						} catch (IllegalAccessException | ClassNotFoundException | IOException | DuplicateKeyException e) {
		    				synchronized(synReturn) {
		    					synReturn.add(null);
		    				}
						}
		    		}
		    	}, multiStoreX);
		   }
		   SynchronizedThreadManager.waitForCompletion(jobs);
		   return returnList;
	}
	
	/**
	 * Perform multiple store on passed List
	 * @param alias The database alias
	 * @param tuples ArrayList of Comparable domain, map, range arrays
	 * @return RelationList of returned stored element relations, or null for failed store
	 * @throws IOException If low level problem
	 * @throws IllegalAccessException If other low level problem
	 * @throws ClassNotFoundException If the class to store cant be located
	 */
	@ServerMethod
	public static RelationList multiStore(Alias alias, ArrayList<Comparable[]> tuples) throws IOException, IllegalAccessException, ClassNotFoundException {
		   List<Comparable[]> synTuples = Collections.synchronizedList(tuples);
		   Future<?>[] jobs = new Future[synTuples.size()];
		   RelationList returnList = new RelationList();
		   List<Comparable> synReturn = Collections.synchronizedList(returnList);
		   AtomicInteger threadIndex = new AtomicInteger(0);
		   for(int i = 0; i < synTuples.size(); i++) {
		    	jobs[i] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
		    		@Override
		    		public void run() {
		    			Comparable[] dmr = null;
		    			synchronized(synTuples) {
		    				dmr = synTuples.get(threadIndex.getAndIncrement());
		    			}
		    			try {
		    				synchronized(synReturn) {
		    					synReturn.add(store(alias, dmr[0],dmr[1],dmr[2]));
		    				}
						} catch (IllegalAccessException | ClassNotFoundException | IOException | DuplicateKeyException e) {
		    				synchronized(synReturn) {
		    					synReturn.add(null);
		    				}
						}
		    		}
		    	}, multiStoreX);
		   }
		   SynchronizedThreadManager.waitForCompletion(jobs);
		   return returnList;
	}
	/**
	 * Invoke threads to store primary key and each index in parallel by calling back to RelatrixKVJson.storekv.
	 * @param identity
	 * @param pk
	 * @throws IOException
	 */
	public static void storeParallel(Relation identity, PrimaryKeySet pk) throws IOException {
		AtomicInteger semaphore = new AtomicInteger();
		final IOException writeException = new IOException();
		Future<?>[] jobs = new Future[6];
		synchronized(mutex) {
			jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0)
							RelatrixKVJson.store(pk, identity.getIdentity());
						if( DEBUG  )
							System.out.println("RelatrixJson.store stored primary:"+pk);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				} // run
			},storeX); // spin 
			// Start threads to store remaining indexes now that we have our primary set up
			jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapDomainRange(identity);
							RelatrixKVJson.store(dmr, identity.getIdentity());	
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						semaphore.getAndIncrement();
						writeException.initCause(e);
						//throw new RuntimeException(e);
					}
				} // run
			},storeX); // spin 
			jobs[2] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new DomainRangeMap(identity);
							RelatrixKVJson.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			jobs[3] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapRangeDomain(identity);
							RelatrixKVJson.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			jobs[4] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {  
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeDomainMap(identity);
							RelatrixKVJson.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			jobs[5] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeMapDomain(identity);
							RelatrixKVJson.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedThreadManager.waitForCompletion(jobs);
		}
		if(DEBUG)
			System.out.println(identity);
		if(semaphore.get() > 0)
			throw writeException;
	}
	
	public static void storeParallel(Alias alias, Relation identity, PrimaryKeySet pk) throws IOException {
		AtomicInteger semaphore = new AtomicInteger();
		final IOException writeException = new IOException();
		Future<?>[] jobs = new Future[6];
		synchronized(mutex) {
			jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0)
							RelatrixKVJson.store(alias, pk, identity.getIdentity());
						if( DEBUG  )
							System.out.println("RelatrixJson.store stored primary:"+pk);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				} // run
			},storeX); // spin 
			// Start threads to store remaining indexes now that we have our primary set up
			jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapDomainRange(identity);
							RelatrixKVJson.store(alias, dmr, identity.getIdentity());	
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						semaphore.getAndIncrement();
						writeException.initCause(e);
						//throw new RuntimeException(e);
					}
				} // run
			},storeX); // spin 
			jobs[2] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new DomainRangeMap(identity);
							RelatrixKVJson.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			jobs[3] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapRangeDomain(identity);
							RelatrixKVJson.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			jobs[4] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {  
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeDomainMap(identity);
							RelatrixKVJson.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			jobs[5] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeMapDomain(identity);
							RelatrixKVJson.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixJson.store stored index:"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedThreadManager.waitForCompletion(jobs);
		}
		if(DEBUG)
			System.out.println(identity);
		if(semaphore.get() > 0)
			throw writeException;
	}
	@ServerMethod
	public static void storekv(Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException {
		RelatrixKVJson.store(key, value);
	}
	@ServerMethod
	public static void storekv(Alias alias, Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException, NoSuchElementException {
		RelatrixKVJson.store(alias, key, value);
	}
	@ServerMethod
	public static Object removekv(Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException {
		return RelatrixKVJson.remove(c);
	}
	@ServerMethod
	public static Object removekv(Alias alias, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		return RelatrixKVJson.remove(alias,c);
	}
	/**
	 * Delete all relationships that this object participates in
	 * @param c The Comparable key to remove
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static void remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);// Remove main entry, which is possibly Relation
		DBKey primaryKey = (DBKey) RelatrixKVJson.remove(c);
		// remove DBKey table
		RelatrixKVJson.remove(primaryKey);
		// Remove primary key if AbstractRelation
		if(c instanceof AbstractRelation) {
			Relation dmr = (Relation)c;
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey());
			RelatrixKVJson.remove(pks);
		}
		List<DBKey> removed = new ArrayList<DBKey>(); //Collections.synchronizedList(new ArrayList<DBKey>());
		try {
			int index = -1;
			DBKey item = primaryKey;
			while(index < removed.size()) {
				removeSearch(item, removed);
				++index;
				if(index < removed.size())
					item = removed.get(index);
			}
			removeParallel(removed);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove exiting remove for key:"+c);
	}

	/**
	 * Delete all relationships that this object participates in
	 * @param alias the database alias
	 * @param c the Comparable key
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchElementException if the alias is not found
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static void remove(Alias alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);
		// Remove main entry, which is possibly Relation
		DBKey primaryKey = (DBKey) RelatrixKVJson.remove(alias, c);
		// remove DBKey table
		RelatrixKVJson.remove(alias, primaryKey);
		// Remove primary key if AbstractRelation
		if(c instanceof AbstractRelation) {
			Relation dmr = (Relation)c;
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias);
			RelatrixKVJson.remove(alias, pks);
		}
		List<DBKey> removed = new ArrayList<DBKey>(); //Collections.synchronizedList(new ArrayList<DBKey>());
		try {
			int index = -1;
			DBKey item = primaryKey;
			while(index < removed.size()) {
				removeSearch(alias, item, removed);
				++index;
				if(index < removed.size())
					item = removed.get(index);
			}
			removeParallel(alias, removed);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove exiting remove for key:"+c);
	}

	/**
	 * 
	 * @param transactionId
	 * @param c
	 * @param deleted
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws DuplicateKeyException
	 */
	private static void removeSearch(DBKey c, List<DBKey> deleted) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, DuplicateKeyException {
		Relation dmr = new Relation(true, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorJson(dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIteratorJson(mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIteratorJson(rmd, rmd_return); //findSet("*","*",c);
		sequentialSearch(itd, itm, itr, deleted);
	}
	/**
	 * 
	 * @param alias
	 * @param c
	 * @param deleted
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws DuplicateKeyException
	 */
	private static void removeSearch(Alias alias, DBKey c, List<DBKey> deleted) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, DuplicateKeyException {
		Relation dmr = new Relation(true, alias, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, alias, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, alias, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorJson(alias, dmr, dmr_return); //findSet(alias, transactionId, c,"*","*");
		Iterator<?> itm = new RelatrixIteratorJson(alias, mdr, mdr_return); //findSet(alias, transactionId, "*",c,"*");
		Iterator<?> itr = new RelatrixIteratorJson(alias, rmd, rmd_return); //findSet(alias, transactionId, "*","*",c);
		sequentialSearch(itd, itm, itr, deleted);
	}
	
	/**
	 * Search the domain, map, and range for each AbstractRelation for relations containing iterator elements
	 * @param itd
	 * @param itm
	 * @param itr
	 * @param deleted
	 */
	private static void sequentialSearch(Iterator<?> itd, Iterator<?> itm, Iterator<?> itr, List<DBKey> deleted) {
		//long tim1 = System.nanoTime();
		try {
			while(itd.hasNext()) {
				Result o = (Result) itd.next();
				if(!deleted.contains(((AbstractRelation)o.get(0)).getIdentity())) {
					deleted.add(((AbstractRelation)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		try {
			while(itm.hasNext()) {
				Result o = (Result) itm.next();
				if(!deleted.contains(((AbstractRelation)o.get(0)).getIdentity())) {
					deleted.add(((AbstractRelation)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		try {
			while(itr.hasNext()) {
				Result o = (Result) itr.next();
				if(!deleted.contains(((AbstractRelation)o.get(0)).getIdentity())) {
					deleted.add(((AbstractRelation)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		//System.out.println("sequentialSearch elapsed:"+(System.nanoTime()-tim1)+" nanos.");
	}
	

	/**
	 * Internal parallel delete
	 * @param removed List of DBKeys to remove
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws IOException
	 * @throws DuplicateKeyException
	 */
	private static void removeParallel(List<DBKey> removed) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		for(DBKey dbk : removed) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove iterated perm 1 "+dbk);
			Relation dmr = (Relation) RelatrixKVJson.remove(dbk); // dbkey table
			RelatrixKVJson.remove(dmr); // instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey());
			RelatrixKVJson.remove(pks);
			// indexes
			DomainRangeMap drm = new DomainRangeMap(dmr);
			MapDomainRange mdr = new MapDomainRange(dmr);
			MapRangeDomain mrd = new MapRangeDomain(dmr);
			RangeDomainMap rdm = new RangeDomainMap(dmr);
			RangeMapDomain rmd = new RangeMapDomain(dmr);
			Future<?>[] jobs = new Future[5];
			jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[2] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[3] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[4] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(rmd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedThreadManager.waitForCompletion(jobs);
		}
	}
	/**
	 * 
	 * @param alias
	 * @param removed
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws IOException
	 * @throws DuplicateKeyException
	 */
	private static void removeParallel(Alias alias, List<DBKey> removed) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		for(DBKey dbk : removed) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove iterated perm 1 "+dbk);
			Relation dmr = (Relation) RelatrixKVJson.remove(alias, dbk); // dbkey
			RelatrixKVJson.remove(alias, dmr); //instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias);
			RelatrixKVJson.remove(alias, pks);
			dmr.setAlias(alias);
			DomainRangeMap drm = new DomainRangeMap(dmr);
			MapDomainRange mdr = new MapDomainRange(dmr);
			MapRangeDomain mrd = new MapRangeDomain(dmr);
			RangeDomainMap rdm = new RangeDomainMap(dmr);
			RangeMapDomain rmd = new RangeMapDomain(dmr);
			Future<?>[] jobs = new Future[5];
			jobs[0] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(alias, drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[1] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(alias, mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[2] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(alias, mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[3] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(alias, rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			jobs[4] = SynchronizedThreadManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVJson.remove(alias, rmd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedThreadManager.waitForCompletion(jobs);
		}
	}
	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @throws IllegalAccessException 
	 * @throws DuplicateKeyException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static void remove(Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, ClassNotFoundException, DuplicateKeyException {
		Relation dmr = new Relation(d,m,null);
		remove(dmr);
	}

	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @throws IllegalAccessException 
	 * @throws DuplicateKeyException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static void remove(Alias alias, Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, NoSuchElementException, ClassNotFoundException, DuplicateKeyException {
		Relation dmr = new Relation(alias,d,m,null);
		remove(alias, dmr);
	}

	/**
	 * Return a resolved list of all components of relationships that this object participates in.
	 * If we supply a tuple, resolves the tuple from the 2 element array in the tuple element 0
	 * @param c The key to locate for initial retrieval, or Tuple to provide initial relation
	 * @return The list of elements related to c
	 * @throws IOException low-level access problem
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static List<Comparable> findSet(Object c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.findSet prepping to find:"+c);
		List<Comparable> located = new RelationList(); //Collections.synchronizedList(new ArrayList<DBKey>());
		List<DBKey> dbkeys = new ArrayList<DBKey>();
		DBKey dbk = null;
		if(!(c instanceof AbstractRelation)) {
			if(c instanceof Tuple) {
				if(((Tuple)c).getRelation() != null) {
					located.add(((Tuple)c).getRelation());
					relatedTupleSearch(((Tuple)c).getRelation().getIdentity(), dbkeys);
					keysToInstances(dbkeys, located);
					return located;
				} else {
					ArrayList<Comparable[]> tuples = ((Tuple)c).getTuples();
					Comparable[] tuple = tuples.get(0);
					PrimaryKeySet pk = PrimaryKeySet.locate(tuple[0], tuple[1]);
					if(pk.getIdentity() != null) {
						Object cx = get(pk.getIdentity());
						if(cx != null) {
							((AbstractRelation)cx).setIdentity(pk.getIdentity());
							located.add((Comparable) cx);
						}
						relatedTupleSearch(pk.getIdentity(), dbkeys);
						keysToInstances(dbkeys, located);
						return located;
					}
				}
			} else {
				dbk = (DBKey) get((Comparable) c);
				if(dbk == null)
					return located;
			}
		} else {
			dbk = ((AbstractRelation)c).getIdentity();
			dbkeys.add(dbk);
		}
		relatedSearch(dbk, dbkeys);
		int index = 0;
		while(index < dbkeys.size()) {
			relatedSearch(dbkeys.get(index), dbkeys);
			++index;
		}
		keysToInstances(dbkeys, located);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.findSet exiting");
		return located;
	}
	/**
	 * Find the related elements
	 * @param transactionId
	 * @param c
	 * @param dbkeys 
	 * @param deleted
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws DuplicateKeyException
	 */
	private static void relatedSearch(DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorJson(dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIteratorJson(mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIteratorJson(rmd, rmd_return); //findSet("*","*",c);
		sequentialMorphismSearch(itd, dbkeys);
		sequentialMorphismSearch(itm, dbkeys);
		sequentialMorphismSearch(itr, dbkeys);
	}
	
	private static void relatedTupleSearch(DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		short dmr_return[] = new short[]{-1,0,2,2};
		Iterator<?> itd = new RelatrixIteratorJson(dmr, dmr_return); //findSet(c,"*","*");
		sequentialMorphismSearch(itd, dbkeys);
	}
	
	private static void keysToInstances(List<DBKey> dbkeys, List<Comparable> instances) throws IllegalAccessException, IOException {
		// should have unique list of dbkeys
		if(DEBUG) {
			System.out.println("Keys to Instances Size:"+dbkeys.size());
			int i = 1;
			for(DBKey dbks : dbkeys) {
				System.out.println((i++)+".)"+get(dbks));
			}
			System.out.println("==========");
		}
		for(DBKey dbks : dbkeys) {
			//AbstractRelation.resolve((Comparable) get(dbks), located);
			Object cx = get(dbks);
			if(cx instanceof AbstractRelation) {
				((AbstractRelation)cx).setIdentity(dbks);
			}
			instances.add((Comparable) cx);
		}
	}
	/**
	 * Return a resolved list of all components of relationships that this object participates in
	 * @param c The key to locate for initial retrieval, or Tuple to provide initial relation
	 * @return The list of elements related to c
	 * @throws IOException low-level access problem
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static List<Comparable> findSet(Alias alias, Object c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.findSet prepping to find:"+c);
		List<Comparable> located = new RelationList(); //Collections.synchronizedList(new ArrayList<DBKey>());
		List<DBKey> dbkeys = new ArrayList<DBKey>();
		DBKey dbk = null;
		if(!(c instanceof AbstractRelation)) {
			if(c instanceof Tuple) {
				if(((Tuple)c).getRelation() != null) {
					located.add(((Tuple)c).getRelation());
					relatedTupleSearch(alias, ((Tuple)c).getRelation().getIdentity(), dbkeys);
					keysToInstances(dbkeys, located);
					return located;
				} else {
					ArrayList<Comparable[]> tuples = ((Tuple)c).getTuples();
					Comparable[] tuple = tuples.get(0);
					PrimaryKeySet pk = PrimaryKeySet.locate(alias, tuple[0], tuple[1]);
					if(pk.getIdentity() != null) {
						Object cx = get(alias, pk.getIdentity());
						if(cx != null) {
							((AbstractRelation)cx).setIdentity(pk.getIdentity());
							((AbstractRelation)cx).setAlias(alias);
							located.add((Comparable) cx);
						}
						relatedTupleSearch(alias, pk.getIdentity(), dbkeys);
						keysToInstances(alias, dbkeys, located);
						return located;
					}
				}
			} else {
				dbk = (DBKey) get(alias, (Comparable)c);
				if(dbk == null)
					return located;
			}
		} else {
			dbk = ((AbstractRelation)c).getIdentity();
			dbkeys.add(dbk);
		}
		relatedSearch(alias, dbk, dbkeys);
		int index = 0;
		while(index < dbkeys.size()) {
			relatedSearch(alias, dbkeys.get(index), dbkeys);
			++index;
		}
		// should have unique list of dbkeys
		keysToInstances(alias, dbkeys, located);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.findSet exiting");
		return located;
	}
	/**
	 * Find the related elements
	 * @param transactionId
	 * @param c
	 * @param dbkeys 
	 * @param deleted
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws DuplicateKeyException
	 */
	private static void relatedSearch(Alias alias, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, alias, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, alias, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, alias, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorJson(alias, dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIteratorJson(alias, mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIteratorJson(alias, rmd, rmd_return); //findSet("*","*",c);
		sequentialMorphismSearch(itd, dbkeys);
		sequentialMorphismSearch(itm, dbkeys);
		sequentialMorphismSearch(itr, dbkeys);
	}
	
	private static void relatedTupleSearch(Alias alias, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, alias, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		short dmr_return[] = new short[]{-1,0,2,2};
		Iterator<?> itd = new RelatrixIteratorJson(alias, dmr, dmr_return); //findSet(c,"*","*");
		sequentialMorphismSearch(itd, dbkeys);
	}
	
	private static void keysToInstances(Alias alias, List<DBKey> dbkeys, List<Comparable> instances) throws IllegalAccessException, IOException {
		// should have unique list of dbkeys
		if(DEBUG) {
			System.out.println("Keys to Instances Size:"+dbkeys.size());
			int i = 1;
			for(DBKey dbks : dbkeys) {
				System.out.println((i++)+".)"+get(alias, dbks));
			}
			System.out.println("==========");
		}
		for(DBKey dbks : dbkeys) {
			//AbstractRelation.resolve((Comparable) get(dbks), located);
			Object cx = get(alias, dbks);
			if(cx instanceof AbstractRelation) {
				((AbstractRelation)cx).setIdentity(dbks);
				((AbstractRelation)cx).setAlias(alias);
			}
			instances.add((Comparable) cx);
		}
	}
	/**
	 * Search the domain, map, and range for each AbstractRelation for relations containing iterator elements
	 * @param itd
	 * @param itm
	 * @param itr
	 * @param dbkeys 
	 * @param deleted
	 */
	protected static void sequentialMorphismSearch(Iterator<?> itd, List<DBKey> dbkeys) {
		//long tim1 = System.nanoTime();
		try {
			while(itd.hasNext()) {
				Result o = (Result) itd.next();
				if(!dbkeys.contains(((AbstractRelation)o.get(0)).getIdentity())) {
					dbkeys.add(((AbstractRelation)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		//System.out.println("sequentialSearch elapsed:"+(System.nanoTime()-tim1)+" nanos.");
	}
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters are objects. Semantically,
	 * the other set-based retrievals make no sense without at least one object.
	 * The returned {@link Result} is always of depth n=1 identity relationship.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type Relation. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param darg Object for domain of relationship
	 * @param marg Object for the map of relationship
	 * @param rarg Object for the range of the relationship
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator of {@link Result}
	 */
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Json(darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Json(darg, marg, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Json(dop, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Json(dop, mop, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Json(dop, mop, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Json(darg, mop, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Json(darg, mop, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Json(dop, marg, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Json(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Json(darg, marg, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Json(dop, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Json(dop, mop, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Json(dop, mop, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Json(darg, mop, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Json(darg, mop, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias,Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Json(dop, marg, rop);
		return ifact.createIterator(alias);
	}
	/**
	 * Perform parallel findSet with list of domains. Result set contains copies of original domain for each returned
	 * Result. The user must match original domain set to returned Result(0) set.
	 * @param d List of domain Objects
	 * @param m map operator
	 * @param r range operator
	 * @return List of Results with original set included in Result(0) for reference
	 */
	@ServerMethod
	public static List<Result> findSetParallel(List<Object> d, Character m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < d.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedThreadManager.getInstance().submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(d.get(taskId), m, r);
						while(it.hasNext()) {
							Result r3;
							Result r2 = (Result) it.next();
							Comparable[] c1 = r2.toArray();
							switch(c1.length) {
								case 1:
									r3 = new Result2();
									r3.set(0,(Comparable)d.get(taskId));
									r3.set(1,c1[0]);
									break;
								case 2:
									r3 = new Result3();
									r3.set(0,(Comparable)d.get(taskId));
									r3.set(1,c1[0]);
									r3.set(2,c1[1]);
									break;
								default:
									throw new RuntimeException("Invalid array length");
							}
							res.add(r3);
						}
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return res;
				}
			},searchX));
		}
		// Collect results
		List<Result> results = new ArrayList<>();
		for (Future<Object> future : futures) {
			List<Result> res;
			try {
				res = (List<Result>) future.get();
				results.addAll(res); // Blocking call to get the result
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * Perform parallel findSet with list of maps. Result set contains copies of original map for each returned
	 * Result. The user must match original map set to returned Result(0) set.
	 * @param d domain operator
	 * @param m List of maps
	 * @param r range operator
	 * @return List of Results with original set included in Result(0) for reference
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Character d, List<Object> m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < m.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedThreadManager.getInstance().submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(d, m.get(taskId), r);
						while(it.hasNext()) {
							Result r3;
							Result r2 = (Result) it.next();
							Comparable[] c1 = r2.toArray();
							switch(c1.length) {
							case 1:
								r3 = new Result2();
								r3.set(0,(Comparable)m.get(taskId));
								r3.set(1,c1[0]);
								break;
							case 2:
								r3 = new Result3();
								r3.set(0,(Comparable)m.get(taskId));
								r3.set(1,c1[0]);
								r3.set(2,c1[1]);
								break;
							default:
								throw new RuntimeException("Invalid array length");
							}
							res.add(r3);
						}
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return res;
				}
			},searchX));
		}
		// Collect results
		List<Result> results = new ArrayList<>();
		for (Future<Object> future : futures) {
			List<Result> res;
			try {
				res = (List<Result>) future.get();
				results.addAll(res); // Blocking call to get the result
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * Perform parallel findSet with list of ranges. Result set contains copies of original range for each returned
	 * Result. The user must match original range set to returned Result(0) set.
	 * @param d domain operator
	 * @param m map operator
	 * @param r List of ranges
	 * @return List of Results with original set included in Result(0) for reference
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Character d, Character m, List<Object> r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < r.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedThreadManager.getInstance().submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(d, m, r.get(taskId));
						while(it.hasNext()) {
							Result r3;
							Result r2 = (Result) it.next();
							Comparable[] c1 = r2.toArray();
							switch(c1.length) {
							case 1:
								r3 = new Result2();
								r3.set(0,(Comparable)r.get(taskId));
								r3.set(1,c1[0]);
								break;
							case 2:
								r3 = new Result3();
								r3.set(0,(Comparable)r.get(taskId));
								r3.set(1,c1[0]);
								r3.set(2,c1[1]);
								break;
							default:
								throw new RuntimeException("Invalid array length");
							}
							res.add(r3);
						}
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return res;
				}
			},searchX));
		}
		// Collect results
		List<Result> results = new ArrayList<>();
		for (Future<Object> future : futures) {
			List<Result> res;
			try {
				res = (List<Result>) future.get();
				results.addAll(res); // Blocking call to get the result
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * Perform parallel findSet with list of domains. Result set contains copies of original domain for each returned
	 * Result. The user must match original domain set to returned Result(0) set.
	 * @param alias alias
	 * @param d List of domain Objects
	 * @param m map operator
	 * @param r range operator
	 * @return List of Results with original set included in Result(0) for reference
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Alias alias, List<Object> d, Character m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < d.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedThreadManager.getInstance().submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(alias, d.get(taskId), m, r);
						while(it.hasNext()) {
							Result r3;
							Result r2 = (Result) it.next();
							Comparable[] c1 = r2.toArray();
							switch(c1.length) {
							case 1:
								r3 = new Result2();
								r3.set(0,(Comparable)d.get(taskId));
								r3.set(1,c1[0]);
								break;
							case 2:
								r3 = new Result3();
								r3.set(0,(Comparable)d.get(taskId));
								r3.set(1,c1[0]);
								r3.set(2,c1[1]);
								break;
							default:
								throw new RuntimeException("Invalid array length");
							}
							res.add(r3);
						}
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return res;
				}
			},searchX));
		}
		// Collect results
		List<Result> results = new ArrayList<>();
		for (Future<Object> future : futures) {
			List<Result> res;
			try {
				res = (List<Result>) future.get();
				results.addAll(res); // Blocking call to get the result
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * Perform parallel findSet with list of maps. Result set contains copies of original map for each returned
	 * Result. The user must match original map set to returned Result(0) set.
	 * @param alias alias
	 * @param d domain operator
	 * @param m List of maps
	 * @param r range operator
	 * @return List of Results with original set included in Result(0) for reference
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Alias alias, Character d, List<Object> m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < m.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedThreadManager.getInstance().submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(alias, d, m.get(taskId), r);
						while(it.hasNext()) {
							Result r3;
							Result r2 = (Result) it.next();
							Comparable[] c1 = r2.toArray();
							switch(c1.length) {
							case 1:
								r3 = new Result2();
								r3.set(0,(Comparable)m.get(taskId));
								r3.set(1,c1[0]);
								break;
							case 2:
								r3 = new Result3();
								r3.set(0,(Comparable)m.get(taskId));
								r3.set(1,c1[0]);
								r3.set(2,c1[1]);
								break;
							default:
								throw new RuntimeException("Invalid array length");
							}
							res.add(r3);
						}
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return res;
				}
			},searchX));
		}
		// Collect results
		List<Result> results = new ArrayList<>();
		for (Future<Object> future : futures) {
			List<Result> res;
			try {
				res = (List<Result>) future.get();
				results.addAll(res); // Blocking call to get the result
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * Perform parallel findSet with list of ranges. Result set contains copies of original range for each returned
	 * Result. The user must match original range set to returned Result(0) set.
	 * @param alias alias
	 * @param d domain operator
	 * @param m map operator
	 * @param r List of ranges
	 * @return List of Results with original set included in Result(0) for reference
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Alias alias, Character d, Character m, List<Object> r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < r.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedThreadManager.getInstance().submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(alias, d, m, r.get(taskId));
						while(it.hasNext()) {
							Result r3;
							Result r2 = (Result) it.next();
							Comparable[] c1 = r2.toArray();
							switch(c1.length) {
							case 1:
								r3 = new Result2();
								r3.set(0,(Comparable)r.get(taskId));
								r3.set(1,c1[0]);
								break;
							case 2:
								r3 = new Result3();
								r3.set(0,(Comparable)r.get(taskId));
								r3.set(1,c1[0]);
								r3.set(2,c1[1]);
								break;
							default:
								throw new RuntimeException("Invalid array length");
							}
							res.add(r3);
						}
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return res;
				}
			},searchX));
		}
		// Collect results
		List<Result> results = new ArrayList<>();
		for (Future<Object> future : futures) {
			List<Result> res;
			try {
				res = (List<Result>) future.get();
				results.addAll(res); // Blocking call to get the result
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters are objects. Semantically,
	 * the other set-based retrievals make no sense without at least one object.
	 * The returned {@link Result} is always of depth n=1 identity relationship.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type Relation. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param darg Object for domain of relationship
	 * @param marg Object for the map of relationship
	 * @param rarg Object for the range of the relationship
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator of {@link Result}
	 */
	@ServerMethod
	public static Stream<?> findStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Json(darg, marg, rop);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Json(dop, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Json(dop, mop, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Json(dop, mop, rop);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Json(darg, mop, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Json(darg, mop, rop);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Json(dop, marg, rop);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Json(darg, marg, rop);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Json(dop, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Json(dop, mop, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Json(dop, mop, rop);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Json(darg, mop, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Json(darg, mop, rop);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias,Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Json(dop, marg, rop);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}

	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators.
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Json(darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Json(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Json(dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Json(dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Json(darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Json(darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Json(darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Json(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Json(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Json(dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Json(dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Json(darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Json(darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Json(darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators.
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Json(dop, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Json(dop, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Json(dop, marg, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Json(darg, mop, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Json(darg, mop, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Json(darg, marg, rop, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Json(dop, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Json(dop, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Json(dop, marg, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Json(darg, mop, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Json(darg, mop, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Json(darg, marg, rop, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}

	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Json(darg, marg, rarg);
		return ifact.createIterator();
	}
	/**
	 * 
	 * @param dop
	 * @param mop
	 * @param rop
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Json(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Json(dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Json(dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Json(darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Json(darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Json(darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Json(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Json(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Json(dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Json(dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Json(darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Json(darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Json(darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}

	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Json(dop, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Json(dop, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Json(dop, marg, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Json(darg, mop, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Json(darg, mop, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Json(darg, marg, rop, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Json(dop, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Json(dop, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Json(dop, marg, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Json(darg, mop, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Json(darg, mop, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Json(darg, marg, rop, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	/**
	 * 
	 * @param darg
	 * @param marg
	 * @param rarg
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Json(darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Json(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	

	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator());
	}
	
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Json(darg, marg, rarg);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Json(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Json(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Json(dop, marg, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Json(dop, marg, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Json(darg, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Json(darg, mop, rarg, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Json(darg, marg, rop, arg1, arg2);
		return new RelatrixStreamJson(ifact.createIterator(alias));
	}

	/**
	 * This method returns the first Relation instance having the lowest valued key value of the index classes.
	 * @return the Relation morphism having the lowest valued key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object first() throws IOException
	{
		try {
			Relation dmr = (Relation) RelatrixKVJson.firstKey(Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVJson.firstValue(Relation.class);
			dmr.setIdentity(dbkey);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static Object first(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			Relation dmr = (Relation) RelatrixKVJson.firstKey(alias,Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVJson.firstValue(alias,Relation.class);
			dmr.setIdentity(dbkey);
			dmr.setAlias(alias);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first Relation
	 * instance having the lowest valued key value.
	 * @return the class having the lowest valued key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object first(Class clazz) throws IOException
	{
		Object o = null;
		try {
			o = RelatrixKVJson.firstKey(clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVJson.firstValue(clazz);
				((AbstractRelation)o).setIdentity(dbkey);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	@ServerMethod
	public static Object first(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		Object o = null;
		try {
			o = RelatrixKVJson.firstKey(alias,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVJson.firstValue(alias,clazz);
				((AbstractRelation)o).setIdentity(dbkey);
				((AbstractRelation)o).setAlias(alias);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first Relation
	 * instance having the lowest valued key value of the index classes.
	 * @return the Relation morphism having the lowest valued key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object firstKey() throws IOException
	{
		return first();
	}
	@ServerMethod
	public static Object firstKey(Alias alias) throws IOException, NoSuchElementException
	{
		return first(alias);
	}
	/**
	 * return lowest valued key.
	 * @param clazz the class to retrieve
	 * @return the The key/value with lowest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object firstKey(Class clazz) throws IOException, IllegalAccessException
	{
		return first(clazz);
	}
	@ServerMethod
	public static Object firstKey(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return first(alias, clazz);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the lowest valued key value of the index classes.
	 * @return the {@link DBKey} having the lowest valued key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object firstValue() throws IOException
	{
		try {
			return RelatrixKVJson.firstValue(Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the value of the first instance of {@link AbstractRelation} {@link Relation} which will be a {@link DBKey}
	 * @param alias the database alias to retrieve the instance value
	 * @return the first value
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	@ServerMethod
	public static Object firstValue(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVJson.firstValue(alias, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the last valued key.
	 * @return the Relation morphism having the last key value with resolved identity.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object last() throws IOException
	{
		try {
			Relation dmr = (Relation) RelatrixKVJson.lastKey(Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVJson.lastValue(Relation.class);
			dmr.setIdentity(dbkey);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the last instance of {@link AbstractRelation} {@link Relation}
	 * @param alias
	 * @return the last Relation instance, with identity and alias resolved
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	@ServerMethod
	public static Object last(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			Relation dmr = (Relation) RelatrixKVJson.lastKey(alias,Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVJson.lastValue(alias,Relation.class);
			dmr.setIdentity(dbkey);
			dmr.setAlias(alias);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the highest valued key.
	 * @param clazz the class target
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object last(Class clazz) throws IOException
	{
		Object o = null;
		try {
			o = RelatrixKVJson.lastKey(clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVJson.lastValue(clazz);
				((AbstractRelation)o).setIdentity(dbkey);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	/**
	 * Get the last instance of the given class and alias
	 * @param alias
	 * @param clazz
	 * @return the instance
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	@ServerMethod
	public static Object last(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		Object o = null;
		try {
			o = RelatrixKVJson.lastKey(alias,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVJson.lastValue(alias,clazz);
				((AbstractRelation)o).setIdentity(dbkey);
				((AbstractRelation)o).setAlias(alias);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the highest valued key.
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastKey() throws IOException
	{
		return last();
	}
	@ServerMethod
	public static Object lastKey(Alias alias) throws IOException, NoSuchElementException
	{
		return last(alias);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the value of the highest valued key.
	 * @return the Relation morphism having the value of highest key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastValue() throws IOException
	{
		try {
			return RelatrixKVJson.lastValue(Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static Object lastValue(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVJson.lastValue(alias, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the highest valued key.
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastKey(Class clazz) throws IOException
	{
		return last(clazz);
	}
	@ServerMethod
	public static Object lastKey(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		return last(alias, clazz);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last Relation
	 * instance having the value of the highest valued key.
	 * @return the Relation morphism having the value of highest key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastValue(Class clazz) throws IOException
	{
		try {
			return RelatrixKVJson.lastValue(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static Object lastValue(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVJson.lastValue(alias, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationships, Which are occurrences {@link Relation} instances.
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 */
	@ServerMethod
	public static long size() throws IOException
	{
		try {
			return RelatrixKVJson.size(Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static long size(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVJson.size(alias, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationships, Which are occurrences {@link Relation} instances.
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 */
	@ServerMethod
	public static long size(Class c) throws IOException
	{
		try {
			return RelatrixKVJson.size(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static long size(Alias alias, Class c) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVJson.size(alias, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns whether the passed Relation
	 * instance exists in the data.
	 * @return true if the passed DomainMapRAnge exists.
	 * @throws IOException
	 */
	@ServerMethod
	public static boolean contains(Comparable obj) throws IOException
	{
		try {
			return RelatrixKVJson.contains(obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static boolean contains(Alias alias, Comparable obj) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVJson.contains(alias, obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the new DBkey.
	 * @return
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static DBKey getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID uuid = UUID.randomUUID();
		DBKey nkey = new DBKey(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
		if(DEBUG)
			System.out.printf("RelatrixJSon.getNewKey Returning NewKey=%s%n", nkey.toString());
		return nkey;
	}

	/**
	 * The lowest key value object
	 * @param clazz the class to retrieve
	 * @return The first value of the class with given key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object firstValue(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKVJson.firstValue(clazz);
	}
	@ServerMethod
	public static Object firstValue(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVJson.firstValue(alias, clazz);
	}
	/**
	 * Return the value for the key.
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object get(Comparable key) throws IOException, IllegalAccessException
	{
		return RelatrixKVJson.get(key);
	}
	@ServerMethod
	public static Object get(Alias alias, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVJson.get(alias, key);
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
		return RelatrixKVJson.get(key);
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
		return RelatrixKVJson.get(alias,key);
	}
	/**
	 * Return the keyset for the given class
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> keySet(Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixKeysetIteratorJson(clazz);
	}
	@ServerMethod
	public static Iterator<?> keySet(Alias alias, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixKeysetIteratorJson(alias, clazz);
	}
	@ServerMethod
	public static Iterator<?> entrySet(Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixEntrysetIteratorJson(clazz);
	}
	@ServerMethod
	public static Iterator<?> entrySet(Alias alias, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixEntrysetIteratorJson(alias, clazz);
	}
	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> entrySetStream(Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixStreamJson(new RelatrixEntrysetIteratorJson(clazz));
	}
	@ServerMethod
	public static Stream<?> entrySetStream(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixStreamJson(new RelatrixEntrysetIteratorJson(alias,clazz));
	}

	/**
	 * Generate the recursively resolved list of relationships in the given AbstractRelation. If none of the components
	 * of the relationship are themselves relationships, the original set of related objects in the tuple is returned as a list.
	 * @param morphism the target for resolution
	 * @return the recursively resolved list of relationships depth first from domain to range
	 */
	@ServerMethod
	public static List<Comparable> resolve(Comparable morphism) {
		List<Comparable> res = new RelationList();
		AbstractRelation.resolve(morphism, res);
		return res;
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


}
