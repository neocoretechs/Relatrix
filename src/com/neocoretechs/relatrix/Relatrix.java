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

import com.neocoretechs.relatrix.iterator.FindHeadSetMode0;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode1;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode2;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode3;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode4;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode5;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode6;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode7;
import com.neocoretechs.relatrix.iterator.FindSetMode0;
import com.neocoretechs.relatrix.iterator.FindSetMode1;
import com.neocoretechs.relatrix.iterator.FindSetMode2;
import com.neocoretechs.relatrix.iterator.FindSetMode3;
import com.neocoretechs.relatrix.iterator.FindSetMode4;
import com.neocoretechs.relatrix.iterator.FindSetMode5;
import com.neocoretechs.relatrix.iterator.FindSetMode6;
import com.neocoretechs.relatrix.iterator.FindSetMode7;
import com.neocoretechs.relatrix.iterator.FindSubSetMode0;
import com.neocoretechs.relatrix.iterator.FindSubSetMode1;
import com.neocoretechs.relatrix.iterator.FindSubSetMode2;
import com.neocoretechs.relatrix.iterator.FindSubSetMode3;
import com.neocoretechs.relatrix.iterator.FindSubSetMode4;
import com.neocoretechs.relatrix.iterator.FindSubSetMode5;
import com.neocoretechs.relatrix.iterator.FindSubSetMode6;
import com.neocoretechs.relatrix.iterator.FindSubSetMode7;
import com.neocoretechs.relatrix.iterator.FindTailSetMode0;
import com.neocoretechs.relatrix.iterator.FindTailSetMode1;
import com.neocoretechs.relatrix.iterator.FindTailSetMode2;
import com.neocoretechs.relatrix.iterator.FindTailSetMode3;
import com.neocoretechs.relatrix.iterator.FindTailSetMode4;
import com.neocoretechs.relatrix.iterator.FindTailSetMode5;
import com.neocoretechs.relatrix.iterator.FindTailSetMode6;
import com.neocoretechs.relatrix.iterator.FindTailSetMode7;
import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.iterator.RelatrixKeysetIterator;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.relatrix.stream.RelatrixStream;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.type.Tuple;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;


/**
* Top-level class that imparts behavior to the {@link AbstractRelation} subclasses which contain references for domain, map, range.<p/>
* The lynch pin is the AbstractRelation and its subclasses indexed
* by the 6 permutations of the domain,map,and range so we can retrieve instances in all
* the potential sort orders.
* The compareTo and fullCompareTo of AbstractRelation provide the comparison methods to drive the processes.
* For retrieval, a partial template is constructed of the proper AbstractRelation subclass which puts the three elements
* in the proper sort order. To retrieve the proper AbstractRelation subclass, partially construct a morphism template to
* order the result set. The retrieval operators allow us to form the partially ordered result sets that are returned.<p/>
* The critical concept about retrieving relationships is to remember that the number of elements from each passed
* stream element or iteration of a Stream or Iterator is dependent on the number of "?" operators in a 'findSet'. For example,
* if we declare findHeadSet("*","?","*") we get back a {@link Result} of one element, for findSet("?",object,"?") we
* would get back a {@link Result2}, with each element of the relationship returned.<br/>
* If we findHeadStream("*","?","*") we return a stream where one  {@link Result} element can be mapped, reduced, consumed, etc.<br/>
* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
* the requirement to be 'categorical'.<p/>
* In general, all Streams or '3 element' arrays returned by the operators are
* the mathematical identity. To follow Categorical rules, the unique key in database terms are the first 2 elements, the domain and map,
* since conceptually a AbstractRelation is a domain acted upon by the map function yielding the range.<p/>
* A given domain run through a 'map function' always yields the same range, 
* as any function that processes an element yields one consistent result.<p/>
* The morphism components are indexed by a {@link com.neocoretechs.relatrix.key.DBKey} that contains a reference to the instance
* within that database as a (UUID). <p/> 
* Some of this work is based on a DBMS described by Alfonso F. Cardenas and Dennis McLeod (1990). Research Foundations 
* in Object-Oriented and Semantic Database Systems. Prentice Hall.
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism and the works of
* Mac Lane<p/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021
*/
public final class Relatrix {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = false;
	private static boolean TRACE = true;
	
	public static Character OPERATOR_WILDCARD_CHAR = '*';
	public static Character OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
  
	private static SynchronizedFixedThreadPoolManager sftpm;
	public static final String storeX = "STOREXTX";
	public static final String storeI = "STOREITX";
	public static final String deleteX = "DELETEXTX";
	public static final String searchX = "SEARCHXTX";
	
	public static final int numMultiStoreThreads = 16;
	public static final String multiStoreX = "MULTISTOREX";
	
	static {
		sftpm = SynchronizedFixedThreadPoolManager.getInstance();
		sftpm.init(6, 6, new String[] {storeX});
		sftpm.init(5, 5, new String[] {deleteX});
		sftpm.init(2, 2, new String[] {storeI});
		sftpm.init(16, 16, new String[] {searchX});
		sftpm.init(numMultiStoreThreads, numMultiStoreThreads, new String[] {multiStoreX});
	}
	
	private static Object mutex = new Object();
	
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private Relatrix() {
	}
	// 2.) volatile instance
	private static volatile Relatrix instance = null;
	// 3.) lock class, assign instance if null
	public static Relatrix getInstance() {
		synchronized(Relatrix.class) {
			if(instance == null) {
				instance = new Relatrix();
				IndexResolver.setLocal();
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
		RelatrixKV.setTablespace(path);
	}
	
	@ServerMethod
	public static String getTableSpace() {
		return RelatrixKV.getTableSpace();
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
		RelatrixKV.setAlias(alias, RelatrixKV.getTableSpace()+alias.getAlias());
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @param path
	 * @throws IOException
	 */
	public static void setAlias(Alias alias, String path) throws IOException {
		RelatrixKV.setAlias(alias, path);
	}
	/**
	 * Get the tablespace path for this alias. Will return null if alias does not exist
	 * @param alias
	 * @return The tablespace path for the given alias returned as a String.
	 */
	@ServerMethod
	public static String getAlias(Alias alias) {
		return RelatrixKV.getAlias(alias);
	}
	
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	@ServerMethod
	public static String[][] getAliases() {
		return RelatrixKV.getAliases();
	}
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	@ServerMethod
	public static void removeAlias(Alias alias) throws NoSuchElementException {
		RelatrixKV.removeAlias(alias);
	}
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The Relation of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Relation store(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		Relation identity = new Relation(); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		PrimaryKeySet pk = PrimaryKeySet.locate(d, m);
		if(pk.getIdentity() == null) {
			identity.setDomainKey(pk.getDomainKey());
			identity.setMapKey(pk.getMapKey());
			identity.setDomainResolved(d);
			identity.setMapResolved(m);
			DBKey rKey = AbstractRelation.checkMorphism(r);
			if(rKey == null)
				identity.setRange(r);
			else {
				identity.setRangeKey(rKey);
				identity.setRangeResolved(r);
			}
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(identity));
		} else
			throw new DuplicateKeyException("Relationship primary key ["+d+"->"+m+"] already exists.");
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		if( DEBUG  )
			System.out.println("Relatrix.store stored :"+identity);
		// store the primary, but not in the DBKey table
		storeParallel(identity, pk);
		if(DEBUG)
			System.out.println(identity);
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
			System.out.println("Relatrix.store stored :"+identity);
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
		    	jobs[i] = SynchronizedFixedThreadPoolManager.submit(new Runnable() {
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
		   SynchronizedFixedThreadPoolManager.waitForCompletion(jobs);
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
		    	jobs[i] = SynchronizedFixedThreadPoolManager.submit(new Runnable() {
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
		   SynchronizedFixedThreadPoolManager.waitForCompletion(jobs);
		   return returnList;
	}
	
	public static void storeParallel(Relation identity, PrimaryKeySet pk) throws IOException {
		AtomicInteger semaphore = new AtomicInteger();
		final IOException writeException = new IOException();
		synchronized(mutex) {
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0)
							RelatrixKV.store(pk, identity.getIdentity());
						if( DEBUG  )
							System.out.println("RelatrixTransaction.store stored primary:"+pk);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				} // run
			},storeX); // spin 
			// Start threads to store remaining indexes now that we have our primary set up
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapDomainRange(identity);
							RelatrixKV.store(dmr, identity.getIdentity());	
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						semaphore.getAndIncrement();
						writeException.initCause(e);
						//throw new RuntimeException(e);
					}
				} // run
			},storeX); // spin 
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new DomainRangeMap(identity);
							RelatrixKV.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapRangeDomain(identity);
							RelatrixKV.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {  
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeDomainMap(identity);
							RelatrixKV.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeMapDomain(identity);
							RelatrixKV.store(dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeX);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(DEBUG)
			System.out.println(identity);
		if(semaphore.get() > 0)
			throw writeException;
	}
	
	public static void storeParallel(Alias alias, Relation identity, PrimaryKeySet pk) throws IOException {
		AtomicInteger semaphore = new AtomicInteger();
		final IOException writeException = new IOException();
		synchronized(mutex) {
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0)
							RelatrixKV.store(alias, pk, identity.getIdentity());
						if( DEBUG  )
							System.out.println("RelatrixTransaction.store stored primary:"+pk);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				} // run
			},storeX); // spin 
			// Start threads to store remaining indexes now that we have our primary set up
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapDomainRange(identity);
							RelatrixKV.store(alias, dmr, identity.getIdentity());	
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						semaphore.getAndIncrement();
						writeException.initCause(e);
						//throw new RuntimeException(e);
					}
				} // run
			},storeX); // spin 
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new DomainRangeMap(identity);
							RelatrixKV.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapRangeDomain(identity);
							RelatrixKV.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {  
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeDomainMap(identity);
							RelatrixKV.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeMapDomain(identity);
							RelatrixKV.store(alias, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("Relatrix.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeX);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeX);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(DEBUG)
			System.out.println(identity);
		if(semaphore.get() > 0)
			throw writeException;
	}
	@ServerMethod
	public static void storekv(Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException {
		RelatrixKV.store(key, value);
	}
	@ServerMethod
	public static void storekv(Alias alias, Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException, NoSuchElementException {
		RelatrixKV.store(alias, key, value);
	}
	@ServerMethod
	public static Object removekv(Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException {
		return RelatrixKV.remove(c);
	}
	@ServerMethod
	public static Object removekv(Alias alias, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		return RelatrixKV.remove(alias,c);
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
		DBKey primaryKey = (DBKey) RelatrixKV.remove(c);
		// remove DBKey table
		RelatrixKV.remove(primaryKey);
		// Remove primary key if AbstractRelation
		if(c instanceof AbstractRelation) {
			Relation dmr = (Relation)c;
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey());
			RelatrixKV.remove(pks);
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
		DBKey primaryKey = (DBKey) RelatrixKV.remove(alias, c);
		// remove DBKey table
		RelatrixKV.remove(alias, primaryKey);
		// Remove primary key if AbstractRelation
		if(c instanceof AbstractRelation) {
			Relation dmr = (Relation)c;
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias);
			RelatrixKV.remove(alias, pks);
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
		Iterator<?> itd = new RelatrixIterator(dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIterator(mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIterator(rmd, rmd_return); //findSet("*","*",c);
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
		Iterator<?> itd = new RelatrixIterator(alias, dmr, dmr_return); //findSet(alias, transactionId, c,"*","*");
		Iterator<?> itm = new RelatrixIterator(alias, mdr, mdr_return); //findSet(alias, transactionId, "*",c,"*");
		Iterator<?> itr = new RelatrixIterator(alias, rmd, rmd_return); //findSet(alias, transactionId, "*","*",c);
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
	 * Perform parallel findSet with list of domains
	 * @param d List of domain Objects
	 * @param m map operator
	 * @param r range operator
	 * @return List of Results
	 */
	@ServerMethod
	public static List<Result> findSetParallel(List<Object> d, Character m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < d.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedFixedThreadPoolManager.submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(d.get(taskId), m, r);
						while(it.hasNext()) {
							res.add((Result) it.next());
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
	 * Perform parallel findSet with list of maps
	 * @param d domain operator
	 * @param m List of maps
	 * @param r range operator
	 * @return List of Results
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Character d, List<Object> m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < m.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedFixedThreadPoolManager.submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(m.get(taskId), m, r);
						while(it.hasNext()) {
							res.add((Result) it.next());
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
	 * Perform parallel findSet with list of ranges
	 * @param d domain operator
	 * @param m map operator
	 * @param r List of ranges
	 * @return List of Results
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Character d, Character m, List<Object> r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < r.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedFixedThreadPoolManager.submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(r.get(taskId), m, r);
						while(it.hasNext()) {
							res.add((Result) it.next());
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
	 * Perform parallel findSet with list of domains
	 * @param alias alias
	 * @param d List of domain Objects
	 * @param m map operator
	 * @param r range operator
	 * @return List of Results
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Alias alias, List<Object> d, Character m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < d.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedFixedThreadPoolManager.submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(alias, d.get(taskId), m, r);
						while(it.hasNext()) {
							res.add((Result) it.next());
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
	 * Perform parallel findSet with list of maps
	 * @param alias alias
	 * @param d domain operator
	 * @param m List of maps
	 * @param r range operator
	 * @return List of Results
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Alias alias, Character d, List<Object> m, Character r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < m.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedFixedThreadPoolManager.submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(alias, m.get(taskId), m, r);
						while(it.hasNext()) {
							res.add((Result) it.next());
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
	 * Perform parallel findSet with list of ranges
	 * @param alias alias
	 * @param d domain operator
	 * @param m map operator
	 * @param r List of ranges
	 * @return List of Results
	 */
	@ServerMethod
	public static List<Result> findSetParallel(Alias alias, Character d, Character m, List<Object> r) {
		List<Future<Object>> futures = new ArrayList<>();
		for(int i = 0; i < r.size(); i++) {
			final int taskId = i;
			futures.add( SynchronizedFixedThreadPoolManager.submit(new Callable<Object>() {
				@Override
				public List<Result> call() {
					List<Result> res = new ArrayList<Result>();
					try {
						Iterator<?> it = findSet(alias, r.get(taskId), m, r);
						while(it.hasNext()) {
							res.add((Result) it.next());
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
			Relation dmr = (Relation) RelatrixKV.remove(dbk); // dbkey table
			RelatrixKV.remove(dmr); // instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey());
			RelatrixKV.remove( pks);
			// indexes
			DomainRangeMap drm = new DomainRangeMap(dmr);
			MapDomainRange mdr = new MapDomainRange(dmr);
			MapRangeDomain mrd = new MapRangeDomain(dmr);
			RangeDomainMap rdm = new RangeDomainMap(dmr);
			RangeMapDomain rmd = new RangeMapDomain(dmr);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(rmd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(deleteX);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
			Relation dmr = (Relation) RelatrixKV.remove(alias, dbk); // dbkey
			RelatrixKV.remove(alias, dmr); //instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias);
			RelatrixKV.remove(alias, pks);
			dmr.setAlias(alias);
			DomainRangeMap drm = new DomainRangeMap(dmr);
			MapDomainRange mdr = new MapDomainRange(dmr);
			MapRangeDomain mrd = new MapRangeDomain(dmr);
			RangeDomainMap rdm = new RangeDomainMap(dmr);
			RangeMapDomain rmd = new RangeMapDomain(dmr);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias, drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias, mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias, mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias, rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias, rmd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(deleteX);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		Iterator<?> itd = new RelatrixIterator(dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIterator(mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIterator(rmd, rmd_return); //findSet("*","*",c);
		sequentialMorphismSearch(itd, dbkeys);
		sequentialMorphismSearch(itm, dbkeys);
		sequentialMorphismSearch(itr, dbkeys);
	}
	
	private static void relatedTupleSearch(DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		short dmr_return[] = new short[]{-1,0,2,2};
		Iterator<?> itd = new RelatrixIterator(dmr, dmr_return); //findSet(c,"*","*");
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
		Iterator<?> itd = new RelatrixIterator(alias, dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIterator(alias, mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIterator(alias, rmd, rmd_return); //findSet("*","*",c);
		sequentialMorphismSearch(itd, dbkeys);
		sequentialMorphismSearch(itm, dbkeys);
		sequentialMorphismSearch(itr, dbkeys);
	}
	
	private static void relatedTupleSearch(Alias alias, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, alias, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		short dmr_return[] = new short[]{-1,0,2,2};
		Iterator<?> itd = new RelatrixIterator(alias, dmr, dmr_return); //findSet(c,"*","*");
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
		IteratorFactory ifact = new FindSetMode7(darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6(darg, marg, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3(dop, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1(dop, mop, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0(dop, mop, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5(darg, mop, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4(darg, mop, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2(dop, marg, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6(darg, marg, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3(dop, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1(dop, mop, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0(dop, mop, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5(darg, mop, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4(darg, mop, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias,Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2(dop, marg, rop);
		return ifact.createIterator(alias);
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
		IteratorFactory ifact = new FindSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6(darg, marg, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3(dop, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1(dop, mop, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0(dop, mop, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5(darg, mop, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4(darg, mop, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2(dop, marg, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6(darg, marg, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3(dop, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1(dop, mop, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0(dop, mop, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5(darg, mop, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4(darg, mop, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias,Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2(dop, marg, rop);
		return new RelatrixStream(ifact.createIterator(alias));
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
		IteratorFactory ifact = new FindTailSetMode7(darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2(dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3(dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4(darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5(darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6(darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2(dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3(dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4(darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5(darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6(darg, marg, rop, arg1);
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
		IteratorFactory ifact = new FindTailSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1(dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2(dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3(dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4(darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5(darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6(darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1(dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2(dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3(dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4(darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5(darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6(darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
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
		IteratorFactory ifact = new FindHeadSetMode7(darg, marg, rarg);
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
		IteratorFactory ifact = new FindHeadSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2(dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3(dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4(darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5(darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6(darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2(dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3(dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4(darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5(darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6(darg, marg, rop, arg1);
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
		IteratorFactory ifact = new FindHeadSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1(dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2(dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3(dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4(darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5(darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6(darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1(dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2(dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3(dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4(darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5(darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6(darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
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
		IteratorFactory ifact = new FindSubSetMode7(darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	

	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0(dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1(dop, mop, rarg, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2(dop, marg, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3(dop, marg, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4(darg, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5(darg, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6(darg, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
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
			Relation dmr = (Relation) RelatrixKV.firstKey(Relation.class);
			DBKey dbkey = (DBKey) RelatrixKV.firstValue(Relation.class);
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
			Relation dmr = (Relation) RelatrixKV.firstKey(alias,Relation.class);
			DBKey dbkey = (DBKey) RelatrixKV.firstValue(alias,Relation.class);
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
			o = RelatrixKV.firstKey(clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKV.firstValue(clazz);
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
			o = RelatrixKV.firstKey(alias,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKV.firstValue(alias,clazz);
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
			return RelatrixKV.firstValue(Relation.class);
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
			return RelatrixKV.firstValue(alias, Relation.class);
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
			Relation dmr = (Relation) RelatrixKV.lastKey(Relation.class);
			DBKey dbkey = (DBKey) RelatrixKV.lastValue(Relation.class);
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
			Relation dmr = (Relation) RelatrixKV.lastKey(alias,Relation.class);
			DBKey dbkey = (DBKey) RelatrixKV.lastValue(alias,Relation.class);
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
			o = RelatrixKV.lastKey(clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKV.lastValue(clazz);
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
			o = RelatrixKV.lastKey(alias,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKV.lastValue(alias,clazz);
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
			return RelatrixKV.lastValue(Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static Object lastValue(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastValue(alias, Relation.class);
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
			return RelatrixKV.lastValue(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static Object lastValue(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastValue(alias, clazz);
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
			return RelatrixKV.size(Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static long size(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.size(alias, Relation.class);
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
			return RelatrixKV.size(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static long size(Alias alias, Class c) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.size(alias, c);
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
			return RelatrixKV.contains(obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static boolean contains(Alias alias, Comparable obj) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.contains(alias, obj);
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
			System.out.printf("Returning NewKey=%s%n", nkey.toString());
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
		return RelatrixKV.firstValue(clazz);
	}
	@ServerMethod
	public static Object firstValue(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.firstValue(alias, clazz);
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
		return RelatrixKV.get(key);
	}
	@ServerMethod
	public static Object get(Alias alias, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.get(alias, key);
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
		return RelatrixKV.get(key);
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
		return RelatrixKV.get(alias,key);
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
		return new RelatrixKeysetIterator(clazz);
	}
	@ServerMethod
	public static Iterator<?> keySet(Alias alias, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixKeysetIterator(alias, clazz);
	}
	@ServerMethod
	public static Iterator<?> entrySet(Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixEntrysetIterator(clazz);
	}
	@ServerMethod
	public static Iterator<?> entrySet(Alias alias, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixEntrysetIterator(alias, clazz);
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
		return new RelatrixStream(new RelatrixEntrysetIterator(clazz));
	}
	@ServerMethod
	public static Stream<?> entrySetStream(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixStream(new RelatrixEntrysetIterator(alias,clazz));
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
