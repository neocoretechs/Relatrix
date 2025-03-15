package com.neocoretechs.relatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.iterator.FindHeadSetMode0Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode1Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode2Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode3Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode4Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode5Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode6Transaction;
import com.neocoretechs.relatrix.iterator.FindHeadSetMode7Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode0Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode1Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode2Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode3Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode4Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode5Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode6Transaction;
import com.neocoretechs.relatrix.iterator.FindSetMode7Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode0Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode1Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode2Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode3Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode4Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode5Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode6Transaction;
import com.neocoretechs.relatrix.iterator.FindSubSetMode7Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode0Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode1Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode2Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode3Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode4Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode5Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode6Transaction;
import com.neocoretechs.relatrix.iterator.FindTailSetMode7Transaction;
import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.relatrix.stream.RelatrixStream;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Top-level class that imparts behavior to the AbstractRelation subclasses which contain references for domain, map, range.<p/>
* The lynch pin is the AbstractRelation and its subclasses indexed
* in the 6 permutations of the domain,map,and range so we can retrieve instances in all
* the potential sort orders.<b/>
* The compareTo and fullCompareTo of AbstractRelation provide the comparison methods to drive the processes.
* For retrieval, a partial template is constructed of the proper AbstractRelation subclass which puts the three elements
* in the proper sort order. To retrieve the proper AbstractRelation subclass, partially construct a morphism template to
* order the result set. The retrieval operators allow us to form the partially ordered result sets that are returned.<p/>
* The critical concept about retrieving relationships is to remember that the number of elements from each passed
* stream element or iteration of a Stream or Iterator is dependent on the number of '?' operators in a 'findSet'. For example,
* if we declare findHeadSet('*','?','*') we get back a {@link Result} of one element, for findSet('?',object,'?') we
* would get back a {@link Result2} array, with each element of the relationship returned.<br/>
* If we findHeadStream('*','?','*') we return a stream where one Comparable array element can be mapped, reduced, consumed, etc.<br/>
* In the special case of the all wildcard specification: findSet('*','*','*'), which will return all elements of the
* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
* the requirement to be 'categorical'.<p/>
* In general, all Streams or '3 element' arrays returned by the operators are
* the mathematical identity. To follow Categorical rules, the unique key in database terms are the first 2 elements, the domain and map,
* since conceptually a AbstractRelation is a domain acted upon by the map function yielding the range.<p/>
* A given domain run through a 'map function' always yields the same range, 
* as any function that processes an element yields one consistent result.<p/>
* Some of this work is based on a DBMS described by Alfonso F. Cardenas and Dennis McLeod (1990). Research Foundations 
* in Object-Oriented and Semantic Database Systems. Prentice Hall.
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism and the works of
* Mac Lane<p/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021
*/
public final class RelatrixTransaction {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = false;
	
	public static Character OPERATOR_WILDCARD_CHAR = '*';
	public static Character OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	
	private static SynchronizedFixedThreadPoolManager sftpm;
	public static final String storeXTransaction = "STOREXTX";
	public static final String storeITransaction = "STOREITX";
	public static final String deleteXTransaction = "DELETEXTX";
	public static final String searchXTransaction = "SEARCHXTX";
	
	static {
		sftpm = SynchronizedFixedThreadPoolManager.getInstance();
		sftpm.init(6, 6, new String[] {storeXTransaction});
		sftpm.init(5, 5, new String[] {deleteXTransaction});
		sftpm.init(2, 2, new String[] {storeITransaction});
		sftpm.init(3, 3, new String[] {searchXTransaction});
	}
	
	private static Object mutex = new Object();
	
	// Multithreaded double check Singleton setups:
	// 1.) privatized constructor; no other class can call
	private RelatrixTransaction() {
	}
	// 2.) volatile instance
	private static volatile RelatrixTransaction instance = null;
	// 3.) lock class, assign instance if null
	public static RelatrixTransaction getInstance() {
		synchronized(RelatrixTransaction.class) {
			if(instance == null) {
				instance = new RelatrixTransaction();
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
	
	public static void setOptimisticConcurrency(boolean optimistic) {
		RelatrixKVTransaction.setOptimisticConcurrency(optimistic);
	}
	
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespace(String path) throws IOException {
		getInstance();
		RelatrixKVTransaction.setTablespace(path);
	}
	
	/**
	 * Get the default tablespace directory
	 * @return the path/dbname of current default tablespace
	 */
	@ServerMethod	
	public static String getTableSpace() {
		return RelatrixKVTransaction.getTableSpace();
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
		RelatrixKV.setAlias(alias, getTableSpace()+alias.getAlias());
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @param path
	 * @throws IOException
	 */
	public static void setAlias(Alias alias, String path) throws IOException {
		RelatrixKVTransaction.setAlias(alias, path);
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	@ServerMethod
	public static void removeAlias(Alias alias) throws NoSuchElementException {
		RelatrixKVTransaction.removeAlias(alias);
	}
	
	/**
	 * Get the tablespace path for this alias. Will return null if alias does not exist.
	 * @param alias
	 * @return The tablespace path of this alias as a String
	 */
	@ServerMethod
	public static String getAlias(Alias alias) {
		return RelatrixKVTransaction.getAlias(alias);
	}
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	@ServerMethod
	public static String[][] getAliases() {
		return RelatrixKVTransaction.getAliases();
	}
	
	/**
	 * Get a new transaction ID
	 * @return the TransactionId instance
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static TransactionId getTransactionId() throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionId xid =  RelatrixKVTransaction.getTransactionId();
		return xid;
	}
	/**
	 * Get a new transaction ID
	 * @param the lock timeout
	 * @return the TransactionId instance as LockingTransactionId
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static TransactionId getTransactionId(long timeout) throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionId xid =  RelatrixKVTransaction.getTransactionId(timeout);
		return xid;
	}
	/**
	 * @param xid
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static void endTransaction(TransactionId xid) throws IllegalAccessException, IOException, ClassNotFoundException {
		RelatrixKVTransaction.endTransaction(xid);
	}
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * This is a transactional store in the context of a previously initiated transaction, for the default tablespace.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation.
	 * @param xid the transaction id
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The Relation of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Relation store(TransactionId xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		Relation identity = new Relation(); // form it as template for duplicate key search
		identity.setTransactionId(xid);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		PrimaryKeySet pk = PrimaryKeySet.locate(xid, d, m);
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
		storeParallel(xid, identity, pk);
		return identity;
	}
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * This is a transactional store in the context of a previously initiated transaction, for a specific database alias.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The Relation of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Relation store(Alias alias, TransactionId xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		Relation identity = new Relation(); // form it as template for duplicate key search
		identity.setAlias(alias);
		identity.setTransactionId(xid);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		PrimaryKeySet pk = PrimaryKeySet.locate(alias, xid, d, m);
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
			identity.setIdentity(identity.newKey(identity));
		} else
			throw new DuplicateKeyException("Relationship primary key ["+d+"->"+m+"] already exists.");
		storeParallel(alias, xid, identity, pk);
		return identity;
	}
	
	/**
	 * Designed to interoperate with {@link Tuple}<p>
	 * Store the set of prepared tuples. Expects the first tuple to have d, m, r. The remaining tuples
	 * have m, r and the relation of the first tuple will be used as domain. If any duplicate keys occur, a null will be 
	 * returned in the array position of the returned tuple.
	 * @param xid the transaction Id 
	 * @param tuples the set of prepared tuples, domain, map, range, for first tuple, map range for remaining
	 * @return the set of stored tuples with identity key set and null for duplicates
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@ServerMethod
	public static List store(TransactionId xid, ArrayList<Comparable[]> tuples) throws IOException, IllegalAccessException, ClassNotFoundException {
		List<Comparable> identities = new RelationList();
		Comparable[] tuple = tuples.get(0);
		Relation identity = new Relation();
		identity.setTransactionId(xid);
		PrimaryKeySet pk = PrimaryKeySet.locate(xid, tuple[0], tuple[1]);
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
			storeParallel(xid, identity, pk);
		} else {
			identity.setIdentity(pk.getIdentity());
		}
		identities.add(identity);
		if(DEBUG)
			System.out.println("Tuple size:"+tuples.size());
		for(int i = 1; i < tuples.size(); i++) {
			tuple = tuples.get(i);
			if(DEBUG)
				System.out.println(Arrays.toString(tuple));
			try {
				identities.add(store(xid, identity, tuple[0], tuple[1]));
			} catch(DuplicateKeyException dke) {
				if(DEBUG)
					System.out.println("Duplicate key returned for tuple store:"+dke);
			}
		}
		if(DEBUG) {
			for(Comparable r: identities) {
				System.out.println(r);
			}
			System.out.println("-----");
		}
		return identities;
	}
	/**
	 * Designed to interoperate with {@link Tuple}<p>
	 * Store the set of prepared tuples. Expectes the first tuple to have d, m, r. The remaining tuples
	 * have m, r and the relation of the first tuple will be used as domain. If any duplicate keys occur, a null will be 
	 * returned in the array position of the returned tuple.
	 * @param alias database alias
	 * @param xid the transaction Id 
	 * @param tuples the set of prepared tuples, domain, map, range, for first tuple, map range for remaining
	 * @return the set of stored tuples with identity key set and null for duplicates
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@ServerMethod
	public static List store(Alias alias, TransactionId xid, ArrayList<Comparable[]> tuples) throws IOException, IllegalAccessException, ClassNotFoundException {
		List<Comparable> identities = new RelationList();
		Comparable[] tuple = tuples.get(0);
		Relation identity = new Relation();
		identity.setAlias(alias);
		identity.setTransactionId(xid);
		PrimaryKeySet pk = PrimaryKeySet.locate(alias, xid, tuple[0], tuple[1]);
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
			storeParallel(alias, xid, identity, pk);
		} else {
			identity.setIdentity(pk.getIdentity());
		}
		identities.add(identity);
		for(int i = 1; i < tuples.size(); i++) {
			tuple = tuples.get(i);
			try {
				identities.add(store(alias, xid, identity, tuple[0], tuple[1]));
			} catch(DuplicateKeyException dke) {}
		}
		return identities;
	}
	
	public static void storeParallel(TransactionId xid, Relation identity, PrimaryKeySet pk) throws IOException {
		AtomicInteger semaphore = new AtomicInteger();
		final IOException writeException = new IOException();
		synchronized(mutex) {
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0)
							RelatrixKVTransaction.store(xid, pk, identity.getIdentity());
						if( DEBUG  )
							System.out.println("RelatrixTransaction.store stored primary:"+pk);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				} // run
			},storeXTransaction); // spin 
			// Start threads to store remaining indexes now that we have our primary set up
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapDomainRange(identity);
							RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());	
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						semaphore.getAndIncrement();
						writeException.initCause(e);
						//throw new RuntimeException(e);
					}
				} // run
			},storeXTransaction); // spin 
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new DomainRangeMap(identity);
							RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapRangeDomain(identity);
							RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {  
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeDomainMap(identity);
							RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeMapDomain(identity);
							RelatrixKVTransaction.store(xid, dmr,identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeXTransaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(DEBUG)
			System.out.println(identity);
		if(semaphore.get() > 0)
			throw writeException;
	}
	
	public static void storeParallel(Alias alias, TransactionId xid, Relation identity, PrimaryKeySet pk) throws IOException {
		AtomicInteger semaphore = new AtomicInteger();
		final IOException writeException = new IOException();
		synchronized(mutex) {
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0)
							RelatrixKVTransaction.store(alias, xid, pk, identity.getIdentity());
						if( DEBUG  )
							System.out.println("RelatrixTransaction.store stored primary:"+pk);
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				} // run
			},storeXTransaction); // spin 
			// Start threads to store remaining indexes now that we have our primary set up
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapDomainRange(identity);
							RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());	
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						semaphore.getAndIncrement();
						writeException.initCause(e);
						//throw new RuntimeException(e);
					}
				} // run
			},storeXTransaction); // spin 
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new DomainRangeMap(identity);
							RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new MapRangeDomain(identity);
							RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {  
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeDomainMap(identity);
							RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						if(semaphore.get() == 0) {
							AbstractRelation dmr = new RangeMapDomain(identity);
							RelatrixKVTransaction.store(alias, xid, dmr,identity.getIdentity());
							if( DEBUG  )
								System.out.println("RelatrixTransaction.store stored :"+dmr);
						}
					} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
						//throw new RuntimeException(e);
						semaphore.getAndIncrement();
						writeException.initCause(e);
					}
				}
			},storeXTransaction);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeXTransaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(DEBUG)
			System.out.println(identity);
		if(semaphore.get() > 0)
			throw writeException;
	}
	/**
	 * Commit the outstanding transaction data in the transaction context.
	 * @param the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void commit(TransactionId xid) throws IOException, IllegalAccessException {
		RelatrixKVTransaction.commit(xid);
	}
	/**
	 * Commit the outstanding transaction data in given transaction for the stated database alias.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the database alias doesnt exist.
	 */
	@ServerMethod
	public static void commit(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.commit(alias, xid);
	}
	/**
	 * Roll back all outstanding transactions on for each relationship in the transaction context.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void rollback(TransactionId xid) throws IOException, IllegalAccessException {
		RelatrixKVTransaction.rollback(xid);
	}
	
	/**
	 * Roll back all outstanding transactions for each relationship in the transaction context for the given database alias.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias doesnt exist 
	 */
	@ServerMethod
	public static void rollback(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.rollback(alias, xid);
	}
	/**
	 * Roll back all outstanding transactions on for each relationship in the transaction context to established checkpoint.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void rollbackToCheckpoint(TransactionId xid) throws IOException, IllegalAccessException {
		RelatrixKVTransaction.rollbackToCheckpoint(xid);
	}
	
	/**
	 * Roll back all outstanding transactions for each relationship in the transaction context for the given database alias to established checkpoint.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias doesnt exist 
	 */
	@ServerMethod
	public static void rollbackToCheckpoint(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.rollbackToCheckpoint(alias, xid);
	}
	/**
	 * Take a check point of our current written relationships in the given transaction context. We can then
	 * issue a 'rollbackToCheckpoint' and remove further written data after this point.
	 * @param xid the transaction id.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static void checkpoint(TransactionId xid) throws IOException, IllegalAccessException {
		RelatrixKVTransaction.checkpoint(xid);
	}
	
	/**
	 * Take a check point of our current written relationships in the given transaction context for a given database alias. We can then
	 * issue a 'rollbackToCheckpoint' and remove further written data after this point for this database.
	 * @param alis the database alias
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias doesnt exist.
	 */
	@ServerMethod
	public static void checkpoint(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.checkpoint(alias, xid);
	}
	@ServerMethod
	public static Object removekv(TransactionId xid, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException {
		return RelatrixKVTransaction.remove(xid,c);
	}
	@ServerMethod
	public static Object removekv(Alias alias, TransactionId xid, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		return RelatrixKVTransaction.remove(alias,xid, c);
	}
	/**
	 * Delete all relationships that this object participates in
	 * @param transactionId
	 * @param c The Comparable key to remove
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static void remove(TransactionId transactionId, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove prepping to remove:"+c);		// Remove main entry, which is possibly Relation
		DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(transactionId, c);
		// remove DBKey table
		RelatrixKVTransaction.remove(transactionId, primaryKey);
		// Remove primary key if AbstractRelation
		if(c instanceof AbstractRelation) {
			Relation dmr = (Relation)c;
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), transactionId);
			RelatrixKVTransaction.remove(transactionId, pks);
		}
		List<DBKey> removed = new ArrayList<DBKey>();//Collections.synchronizedList(new ArrayList<DBKey>()); slower parallel search
		try {
			int index = -1;
			DBKey item = primaryKey;
			while(index < removed.size()) {
				removeSearch(transactionId, item, removed);
				++index;
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RemoveSearch index"+index+" size:"+removed.size());
				if(index < removed.size())
					item = removed.get(index);
			}
			removeParallel(transactionId, removed);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove exiting remove for key:"+c);
	}

	/**
	 * Delete all relationships that this object participates in
	 * @param alias the database alias
	 * @param transactionId
	 * @param c the Comparable key
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchElementException if the alias is not found
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static void remove(Alias alias, TransactionId transactionId, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove prepping to remove:"+c);
		// Remove main entry, which is possibly Relation
		DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(alias, transactionId, c);
		// remove DBKey table
		RelatrixKVTransaction.remove(alias, transactionId, primaryKey);
		// Remove primary key if AbstractRelation
		if(c instanceof AbstractRelation) {
			Relation dmr = (Relation)c;
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias, transactionId);
			RelatrixKVTransaction.remove(alias, transactionId, pks);
		}
		List<DBKey> removed = new ArrayList<DBKey>();//Collections.synchronizedList(new ArrayList<DBKey>());
		try {
			int index = -1;
			DBKey item = primaryKey;
			while(index < removed.size()) {
				removeSearch(alias, transactionId, item, removed);
				++index;
				if(DEBUG || DEBUGREMOVE)
					System.out.println("RemoveSearch index"+index+" size:"+removed.size());
				if(index < removed.size())
					item = removed.get(index);
			}
			removeParallel(alias, transactionId, removed);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove exiting remove for key:"+c);
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
	private static void removeSearch(TransactionId transactionId, DBKey c, List<DBKey> deleted) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, DuplicateKeyException {
		Relation dmr = new Relation(true, transactionId, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, transactionId, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, transactionId, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorTransaction(transactionId, dmr, dmr_return); //findSet(transactionId, c,'*','*');
		Iterator<?> itm = new RelatrixIteratorTransaction(transactionId, mdr, mdr_return); //findSet(transactionId, '*',c,'*');
		Iterator<?> itr = new RelatrixIteratorTransaction(transactionId, rmd, rmd_return); //findSet(transactionId, '*','*',c);
		sequentialSearch(itd, itm, itr, deleted);
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RemoveSearch Exit:"+c+" deleted size="+deleted.size());
	}
	/**
	 * 
	 * @param alias
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
	private static void removeSearch(Alias alias, TransactionId transactionId, DBKey c, List<DBKey> deleted) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, DuplicateKeyException {
		Relation dmr = new Relation(true, alias, transactionId, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, alias, transactionId, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, alias, transactionId, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorTransaction(alias, transactionId, dmr, dmr_return); //findSet(alias, transactionId, c,'*','*');
		Iterator<?> itm = new RelatrixIteratorTransaction(alias, transactionId, mdr, mdr_return); //findSet(alias, transactionId, '*',c,'*');
		Iterator<?> itr = new RelatrixIteratorTransaction(alias, transactionId, rmd, rmd_return); //findSet(alias, transactionId, '*','*',c);
		sequentialSearch(itd, itm, itr, deleted);
	}

	/**
	 * 
	 * @param transactionId
	 * @param removed
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws IOException
	 * @throws DuplicateKeyException
	 */
	private static void removeParallel(TransactionId transactionId, List<DBKey> removed) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		for(DBKey dbk : removed) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("RelatrixTransaction.remove iterated perm 1 "+dbk);
			Relation dmr = (Relation) RelatrixKVTransaction.remove(transactionId, dbk); // dbkey
			RelatrixKVTransaction.remove(transactionId, dmr); // instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), transactionId);
			RelatrixKVTransaction.remove(transactionId, pks);
			dmr.setTransactionId(transactionId);
			DomainRangeMap drm = new DomainRangeMap(dmr);
			drm.setTransactionId(transactionId);
			MapDomainRange mdr = new MapDomainRange(dmr);
			mdr.setTransactionId(transactionId);
			MapRangeDomain mrd = new MapRangeDomain(dmr);
			mrd.setTransactionId(transactionId);
			RangeDomainMap rdm = new RangeDomainMap(dmr);
			rdm.setTransactionId(transactionId);
			RangeMapDomain rmd = new RangeMapDomain(dmr);
			rmd.setTransactionId(transactionId);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(transactionId, drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(transactionId, mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(transactionId, mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(transactionId, rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(transactionId, rmd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(deleteXTransaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @param alias
	 * @param transactionId
	 * @param removed
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 * @throws IOException
	 * @throws DuplicateKeyException
	 */
	private static void removeParallel(Alias alias, TransactionId transactionId, List<DBKey> removed) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		for(DBKey dbk : removed) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("RelatrixTransaction.remove iterated perm 1 "+dbk);
			Relation dmr = (Relation) RelatrixKVTransaction.remove(alias, transactionId, dbk); // dbkey
			RelatrixKVTransaction.remove(alias, transactionId, dmr); // instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias, transactionId);
			RelatrixKVTransaction.remove(alias, transactionId, pks);
			dmr.setTransactionId(transactionId);
			dmr.setAlias(alias);
			DomainRangeMap drm = new DomainRangeMap(dmr);
			drm.setTransactionId(transactionId);
			MapDomainRange mdr = new MapDomainRange(dmr);
			mdr.setTransactionId(transactionId);
			MapRangeDomain mrd = new MapRangeDomain(dmr);
			mrd.setTransactionId(transactionId);
			RangeDomainMap rdm = new RangeDomainMap(dmr);
			rdm.setTransactionId(transactionId);
			RangeMapDomain rmd = new RangeMapDomain(dmr);
			rmd.setTransactionId(transactionId);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(alias, transactionId, drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(alias, transactionId, mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(alias, transactionId, mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(alias, transactionId, rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKVTransaction.remove(alias, transactionId, rmd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteXTransaction);
			try {
				SynchronizedFixedThreadPoolManager.waitForGroupToFinish(deleteXTransaction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * This appears to run 2x slower than a sequential search for some reason
	 * @param itd
	 * @param itm
	 * @param itr
	 * @param deleted
	 */
	private static void parallelSearch(Iterator<?> itd, Iterator<?> itm, Iterator<?> itr, List<DBKey> deleted) {
		long tim1 = System.nanoTime();
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
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
			}
		},searchXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
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
			}
		},searchXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
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
			}
		},searchXTransaction);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(searchXTransaction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("parallelsearch elapsed:"+(System.nanoTime()-tim1)+" nanos.");
	}
	/**
	 * 
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
	 * Delete specific relationship and all relationships that it participates in for this transaction in the default tablespace. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param xid the transaction id
	 * @param d the domain of the relationship as Comparable key
	 * @param m the map of the relationship as Comparable key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws DuplicateKeyException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static void remove(TransactionId xid, Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, ClassNotFoundException {
		remove(xid, new Relation(null, xid, d, m, null));
	}
	
	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param alias database alias
	 * @param xid transaction id
	 * @param d
	 * @param m
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if alias isnt found
	 * @throws DuplicateKeyException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	@ServerMethod
	public static void remove(Alias alias, TransactionId xid, Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, NoSuchElementException, IllegalArgumentException, ClassNotFoundException, DuplicateKeyException {
		remove(alias, xid, d, m);
	}
	/**
	 * Return a resolved list of all components of relationships that this object participates in.
	 * If we supply a tuple, resolves the tuple from the 2 element array in the tuple element 0
	 * @param c The Comparable key to locate for initial retrieval, or Tuple supplying initial relation
	 * @return The list of elements related to c
	 * @throws IOException low-level access problem
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static List<Comparable> findSet(TransactionId xid, Object c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.findSet prepping to find:"+c);
		List<Comparable> located = new RelationList(); //Collections.synchronizedList(new ArrayList<DBKey>());
		List<DBKey> dbkeys = new ArrayList<DBKey>();
		DBKey dbk = null;
		if(!(c instanceof AbstractRelation)) {
			if(c instanceof Tuple) {
				if(((Tuple)c).getRelation() != null) {
					located.add(((Tuple)c).getRelation());
					relatedTupleSearch(xid, ((Tuple)c).getRelation().getIdentity(), dbkeys);
					keysToInstances(xid, dbkeys, located);
					return located;
				} else {
					ArrayList<Comparable[]> tuples = ((Tuple)c).getTuples();
					Comparable[] tuple = tuples.get(0);
					PrimaryKeySet pk = PrimaryKeySet.locate(xid, tuple[0], tuple[1]);
					if(pk.getIdentity() != null) {
						Object cx = get(xid, pk.getIdentity());
						if(cx != null) {
							((AbstractRelation)cx).setIdentity(pk.getIdentity());
							((AbstractRelation)cx).setTransactionId(xid);
							located.add((Comparable) cx);
						}
						relatedTupleSearch(xid, pk.getIdentity(), dbkeys);
						keysToInstances(xid, dbkeys, located);
						return located;
					}
				}
			} else {
				dbk = (DBKey) get(xid, (Comparable)c);
				if(dbk == null)
					return located;
			}
		} else {
			dbk = ((AbstractRelation)c).getIdentity();
			dbkeys.add(dbk);
		}
		relatedSearch(xid, dbk, dbkeys);
		int index = 0;
		while(index < dbkeys.size()) {
			relatedSearch(xid, dbkeys.get(index), dbkeys);
			++index;
		}
		keysToInstances(xid, dbkeys, located);
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
	private static void relatedSearch(TransactionId xid, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, xid, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, xid, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, xid, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorTransaction(xid, dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIteratorTransaction(xid, mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIteratorTransaction(xid, rmd, rmd_return); //findSet("*","*",c);
		Relatrix.sequentialMorphismSearch(itd, dbkeys);
		Relatrix.sequentialMorphismSearch(itm, dbkeys);
		Relatrix.sequentialMorphismSearch(itr, dbkeys);
	}
	
	private static void relatedTupleSearch(TransactionId xid, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, xid, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		short dmr_return[] = new short[]{-1,0,2,2};
		Iterator<?> itd = new RelatrixIteratorTransaction(xid, dmr, dmr_return); //findSet(c,"*","*");
		Relatrix.sequentialMorphismSearch(itd, dbkeys);
	}
	
	private static void keysToInstances(TransactionId xid, List<DBKey> dbkeys, List<Comparable> instances) throws IllegalAccessException, IOException {
		// should have unique list of dbkeys
		if(DEBUG) {
			System.out.println("Keys to Instances Size:"+dbkeys.size());
			int i = 1;
			for(DBKey dbks : dbkeys) {
				System.out.println((i++)+".)"+get(xid, dbks));
			}
			System.out.println("==========");
		}
		for(DBKey dbks : dbkeys) {
			//AbstractRelation.resolve((Comparable) get(xid, dbks), located);
			Object cx = get(xid, dbks);
			if(cx instanceof AbstractRelation) {
				((AbstractRelation)cx).setIdentity(dbks);
				((AbstractRelation)cx).setTransactionId(xid);
			}
			instances.add((Comparable) cx);
		}
	}
	/**
	 * Return a resolved list of all components of relationships that this object participates in
	 * @param c The Comparable key to locate for initial retrieval
	 * @return The list of elements related to c
	 * @throws IOException low-level access problem
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws DuplicateKeyException 
	 */
	@ServerMethod
	public static List<Comparable> findSet(Alias alias, TransactionId xid, Object c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.findSet prepping to find:"+c);
		List<Comparable> located = new RelationList(); //Collections.synchronizedList(new ArrayList<DBKey>());
		List<DBKey> dbkeys = new ArrayList<DBKey>();
		DBKey dbk = null;
		if(!(c instanceof AbstractRelation)) {
			if(c instanceof Tuple) {
				if(((Tuple)c).getRelation() != null) {
					located.add(((Tuple)c).getRelation());
					relatedTupleSearch(alias, xid, ((Tuple)c).getRelation().getIdentity(), dbkeys);
					keysToInstances(alias, xid, dbkeys, located);
					return located;
				} else {
					ArrayList<Comparable[]> tuples = ((Tuple)c).getTuples();
					Comparable[] tuple = tuples.get(0);
					PrimaryKeySet pk = PrimaryKeySet.locate(alias, xid, tuple[0], tuple[1]);
					if(pk.getIdentity() != null) {
						Object cx = get(alias, xid, pk.getIdentity());
						if(cx != null) {
							((AbstractRelation)cx).setIdentity(pk.getIdentity());
							((AbstractRelation)cx).setAlias(alias);
							((AbstractRelation)cx).setTransactionId(xid);
							located.add((Comparable) cx);
						}
						relatedTupleSearch(alias, xid, pk.getIdentity(), dbkeys);
						keysToInstances(alias, xid, dbkeys, located);
						return located;
					}
				}
			} else {
				dbk = (DBKey) get(alias, xid, (Comparable)c);
				if(dbk == null)
					return located;
			}
		} else {
			dbk = ((AbstractRelation)c).getIdentity();
			dbkeys.add(dbk);
		}
		relatedSearch(alias, xid, dbk, dbkeys);
		int index = 0;
		while(index < dbkeys.size()) {
			relatedSearch(alias, xid, dbkeys.get(index), dbkeys);
			++index;
		}
		// should have unique list of dbkeys
		keysToInstances(alias, xid, dbkeys, located);
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
	private static void relatedSearch(Alias alias, TransactionId xid, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, alias, xid, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		MapDomainRange mdr = new MapDomainRange(true, alias, xid, null, DBKey.nullDBKey, null, c, null, DBKey.nullDBKey);
		RangeMapDomain rmd = new RangeMapDomain(true, alias, xid, null, DBKey.nullDBKey, null, DBKey.nullDBKey, null, c);
		short dmr_return[] = new short[]{-1,0,2,2};
		short mdr_return[] = new short[]{-1,2,0,2};
		short rmd_return[] = new short[]{-1,2,2,0};
		Iterator<?> itd = new RelatrixIteratorTransaction(alias, xid, dmr, dmr_return); //findSet(c,"*","*");
		Iterator<?> itm = new RelatrixIteratorTransaction(alias, xid, mdr, mdr_return); //findSet("*",c,"*");
		Iterator<?> itr = new RelatrixIteratorTransaction(alias, xid, rmd, rmd_return); //findSet("*","*",c);
		Relatrix.sequentialMorphismSearch(itd, dbkeys);
		Relatrix.sequentialMorphismSearch(itm, dbkeys);
		Relatrix.sequentialMorphismSearch(itr, dbkeys);
	}
	
	private static void relatedTupleSearch(Alias alias, TransactionId xid, DBKey c, List<DBKey> dbkeys) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		Relation dmr = new Relation(true, alias, xid, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
		short dmr_return[] = new short[]{-1,0,2,2};
		Iterator<?> itd = new RelatrixIteratorTransaction(alias, xid, dmr, dmr_return); //findSet(c,"*","*");
		Relatrix.sequentialMorphismSearch(itd, dbkeys);
	}
	
	private static void keysToInstances(Alias alias, TransactionId xid, List<DBKey> dbkeys, List<Comparable> instances) throws IllegalAccessException, IOException {
		// should have unique list of dbkeys
		if(DEBUG) {
			System.out.println("Keys to Instances Size:"+dbkeys.size());
			int i = 1;
			for(DBKey dbks : dbkeys) {
				System.out.println((i++)+".)"+get(alias, xid, dbks));
			}
			System.out.println("==========");
		}
		for(DBKey dbks : dbkeys) {
			//AbstractRelation.resolve((Comparable) get(alias, xid, dbks), located);
			Object cx = get(alias, xid, dbks);
			if(cx instanceof AbstractRelation) {
				((AbstractRelation)cx).setIdentity(dbks);
				((AbstractRelation)cx).setAlias(alias);
				((AbstractRelation)cx).setTransactionId(xid);
			}
			instances.add((Comparable) cx);
		}
	}
	
	/**
	 * Generate the recursively resolved list of relationships in the given AbstractRelation. If none of the components
	 * of the relationship are themselves relationships, the original set of related objects in the tuple is returned as a list.
	 * @param morphism the target for resolution
	 * @return the recursively resolved list of relationships depth first from domain to range
	 */
	@ServerMethod
	public static List<Comparable> resolve(Comparable morphism) {
		RelationList res = new RelationList();
		AbstractRelation.resolve(morphism, res);
		return res;
	}
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters are objects. Semantically,
	 * the other set-based retrievals make no sense without at least one object.
	 * The returned {@link Result} is always of depth n=1 identity relationship.
	 * In the special case of the all wildcard specification: findSet('*','*','*'), which will return all elements of the
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
	public static Iterator<?> findSet(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Transaction(xid, darg, marg, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Transaction(xid, dop, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Transaction(xid, dop, mop, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Transaction(xid, dop, mop, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Transaction(xid, darg, mop, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Transaction(xid, darg, mop, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(TransactionId xid, Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Transaction(xid, dop, marg, rop);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Transaction(xid, darg, marg, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Transaction(xid, dop, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Transaction(xid, dop, mop, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Transaction(xid, dop, mop, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Transaction(xid, darg, mop, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Transaction(xid, darg, mop, rop);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSet(Alias alias, TransactionId xid, Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Transaction(xid, dop, marg, rop);
		return ifact.createIterator(alias);
	}

	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters are objects. Semantically,
	 * the other set-based retrievals make no sense without at least one object.
	 * The returned {@link Result} is always of depth n=1 identity relationship.
	 * In the special case of the all wildcard specification: findSet('*','*','*'), which will return all elements of the
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
	public static Stream<?> findStream(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Transaction(xid, darg, marg, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Transaction(xid, dop, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Transaction(xid, dop, mop, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Transaction(xid, dop, mop, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Transaction(xid, darg, mop, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Transaction(xid, darg, mop, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(TransactionId xid, Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Transaction(xid, dop, marg, rop);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Object darg, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode6Transaction(xid, darg, marg, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode3Transaction(xid, dop, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode1Transaction(xid, dop, mop, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode0Transaction(xid, dop, mop, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode5Transaction(xid, darg, mop, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Object darg, Character mop, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode4Transaction(xid, darg, mop, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findStream(Alias alias, TransactionId xid, Character dop, Object marg, Character rop) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = new FindSetMode2Transaction(xid, dop, marg, rop);
		return new RelatrixStream(ifact.createIterator(alias));
	}

	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators.
	 * @param darg Object for domain of relationship, a dont-care wildcard '*', a return-object '?', or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard '*', a return-object '?', or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard '*', a return-object '?', or a class template
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Transaction(xid, darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findTailSet(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Transaction(xid, darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}

	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators.
	 * @param darg Object for domain of relationship, a dont-care wildcard '*', a return-object '?', or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard '*', a return-object '?', or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard '*', a return-object '?', or a class template
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	/**
	 * Return the stream of Results strictly less than arg1 and arg2 matching range
	 * @param xid Transaction Id
	 * @param dop Domain wildcard or return tuple
	 * @param mop Map wildcard or return tuple
	 * @param rarg Range object to match
	 * @param arg1 Qualifier for domain wildcard or return tuple Class or instance greater than result
	 * @param arg2 Qualifier for map wildcard or return tuple Class or instance greater than result
	 * @return The Stream of retrieved Result items
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Transaction(xid, darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias,TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindTailSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findTailStream(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact =  new FindTailSetMode6Transaction(xid, darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}


	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param darg Domain of morphism, a dont-care wildcard '*', a return-object '?', or class
	 * @param marg Map of morphism relationship, a dont-care wildcard '*', a return-object '?', or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard '*', a return-object '?', or class
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Transaction(xid, darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findHeadSet(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Transaction(xid, darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}

	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param darg Domain of morphism, a dont-care wildcard '*', a return-object '?', or class
	 * @param marg Map of morphism relationship, a dont-care wildcard '*', a return-object '?', or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard '*', a return-object '?', or class
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Transaction(xid, darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findHeadStream(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindHeadSetMode6Transaction(xid, darg, marg, rop, arg1);
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
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(TransactionId xid, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1, arg2);
		return ifact.createIterator();
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Transaction(xid, darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3, arg4);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1, arg2);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1);
		return ifact.createIterator(alias);
	}
	@ServerMethod
	public static Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1, arg2);
		return ifact.createIterator(alias);
	}

	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator());
	}
	@ServerMethod
	public static Stream<?> findSubStream(TransactionId xid, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator());
	}
	
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode7Transaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode0Transaction(xid, dop, mop, rop, arg1, arg2, arg3, arg4, arg5, arg6);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Character mop, Object rarg, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode1Transaction(xid, dop, mop, rarg, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Object marg, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode2Transaction(xid, dop, marg, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Character dop, Object marg, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode3Transaction(xid, dop, marg, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Character mop, Character rop, Object arg1, Object arg2, Object arg3, Object arg4) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode4Transaction(xid, darg, mop, rop, arg1, arg2, arg3, arg4);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Character mop, Object rarg, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode5Transaction(xid, darg, mop, rarg, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	@ServerMethod
	public static Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Object marg, Character rop, Object arg1, Object arg2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = new FindSubSetMode6Transaction(xid, darg, marg, rop, arg1, arg2);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	

	/**
	 * this method returns the first Relation
	 * instance having the lowest valued key value of the index classes.
	 * @return the Relation morphism having the lowest valued key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object firstKey(TransactionId xid) throws IOException
	{
		return first(xid);
	}
	
	/**
	 * This method returns the first Relation
	 * instance having the lowest valued key.
	 * @param xid the transaction id
	 * @param clazz the class of the the target instances
	 * @return the Relation morphism first key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object firstKey(TransactionId xid, Class clazz) throws IOException
	{
		return first(xid, clazz);
	}
	/**
	 * this method returns the first Relation
	 * instance having the lowest valued key value of the index classes
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	@ServerMethod
	public static Object firstKey(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		return first(alias, xid, clazz);
	}
	/**
	 * this method returns the first Relation
	 * instance having the lowest valued key value of the index classes.
	 * @return the Relation morphism having the lowest valued key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object first(TransactionId xid) throws IOException
	{
		try {
			Relation dmr = (Relation) RelatrixKVTransaction.firstKey(xid,Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVTransaction.firstValue(xid,Relation.class);
			dmr.setIdentity(dbkey);
			dmr.setTransactionId(xid);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * this method returns the first Relation
	 * instance having the lowest valued key value of the index classes.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @return the Relation morphism having the lowest valued key value.
	 * @throws NoSuchElementException if the alias doesnt exist
	 * @throws IOException
	 */
	@ServerMethod
	public static Object first(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			Relation dmr = (Relation) RelatrixKVTransaction.firstKey(alias,xid, Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVTransaction.firstValue(alias,xid,Relation.class);
			dmr.setIdentity(dbkey);
			dmr.setAlias(alias);
			dmr.setTransactionId(xid);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * This method returns the first DBKey of Relation
	 * instance having the lowest valued key value of the index classes.
	 * @param xid transaction id
	 * @return the Relation morphism having the lowest valued key value.	
	 * @throws IOException																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						* @throws IOException
	 */
	@ServerMethod
	public static Object firstValue(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.firstValue(xid, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the first DBKey Relation
	 * instance having the lowest valued key value of the index classes.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @return the Relation morphism having the lowest valued key.	
	 * @throws IOException																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																					* @throws IOException
	 */
	@ServerMethod
	public static Object firstValue(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.firstValue(alias, xid, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the first Relation
	 * instance having the lowest valued key.
	 * @param xid the transaction id
	 * @param clazz the class of the the target instances
	 * @return the Relation morphism first key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object first(TransactionId xid, Class clazz) throws IOException
	{
		Object o = null;
		try {
			o = RelatrixKVTransaction.firstKey(xid,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVTransaction.firstValue(xid,clazz);
				((AbstractRelation)o).setIdentity(dbkey);
				((AbstractRelation)o).setTransactionId(xid);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	
	/**
	 * This method returns the first class
	 * instance having the lowest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class of the the target instances
	 * @return the class instance first key.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	@ServerMethod
	public static Object first(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		Object o = null;
		try {
			o = RelatrixKVTransaction.firstKey(alias,xid,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVTransaction.firstValue(alias,xid,clazz);
				((AbstractRelation)o).setIdentity(dbkey);
				((AbstractRelation)o).setAlias(alias);
				((AbstractRelation)o).setTransactionId(xid);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	
	/**
	 * The lowest key value object
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return The first value of the class with given key
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias is not found
	 */
	@ServerMethod
	public static Object firstValue(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVTransaction.firstValue(alias, xid, clazz);
	}
	
	/**
	 * The lowest key value object
	 * @param clazz the class to retrieve
	 * @return The first value of the class with given key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object firstValue(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKVTransaction.firstValue(xid, clazz);
	}
	
	/**
	 * This method returns the last Relation
	 * instance having the highest valued key.
	 * @param xid the transaction id
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object last(TransactionId xid) throws IOException
	{
		try {
			Relation dmr = (Relation) RelatrixKVTransaction.lastKey(xid,Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVTransaction.lastValue(xid,Relation.class);
			dmr.setIdentity(dbkey);
			dmr.setTransactionId(xid);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last Relation
	 * instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	@ServerMethod
	public static  Object last(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			Relation dmr = (Relation) RelatrixKVTransaction.lastKey(alias,xid, Relation.class);
			DBKey dbkey = (DBKey) RelatrixKVTransaction.lastValue(alias,xid,Relation.class);
			dmr.setIdentity(dbkey);
			dmr.setAlias(alias);
			dmr.setTransactionId(xid);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * this method returns the last target class
	 * instance having the highest valued key.
	 * @param xid the transaction id
	 * @param clazz the class of the target
	 * @return the target class having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object last(TransactionId xid, Class clazz) throws IOException
	{
		Object o = null;
		try {
			o = RelatrixKVTransaction.lastKey(xid,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVTransaction.lastValue(xid,clazz);
				((AbstractRelation)o).setIdentity(dbkey);
				((AbstractRelation)o).setTransactionId(xid);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	
	/**
	 * this method returns the last class instance
	 * instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class of the target
	 * @return the class instance having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object last(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		Object o = null;
		try {
			o = RelatrixKVTransaction.lastKey(alias,xid,clazz);
			if(o instanceof AbstractRelation) {
				DBKey dbkey = (DBKey) RelatrixKVTransaction.lastValue(alias,xid,clazz);
				((AbstractRelation)o).setIdentity(dbkey);
				((AbstractRelation)o).setAlias(alias);
				((AbstractRelation)o).setTransactionId(xid);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}

	/**
	 * This method returns the last Relation
	 * instance having the highest valued key.
	 * @param xid the transaction id
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastKey(TransactionId xid) throws IOException
	{
		return last(xid);
	}
	
	/**
	 * This method returns the last Relation
	 * instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @return the Relation morphism having the highest key value.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	@ServerMethod
	public static Object lastKey(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		return last(alias, xid);
	}
	
	/**
	 * this method returns the last target class
	 * instance having the highest valued key.
	 * @param xid the transaction id
	 * @param clazz the class of the target
	 * @return the target class having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastKey(TransactionId xid, Class clazz) throws IOException
	{
		return last(xid,clazz);
	}
	
	/**
	 * this method returns the last class instance
	 * instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class of the target
	 * @return the class instance having the highest key value.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastKey(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		return last(alias, xid, clazz);
	}
	
	/**
	 * This method returns the last DBKey of Relation
	 * instance having the value of the highest valued key.
	 * @param xid the transaction id
	 * @return the DBKey of the Relation morphism having the value of highest key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastValue(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastValue(xid, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last DBKey of Relation
	 * instance having the value of the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @return the DBKey of the Relation morphism having the value of highest key.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	@ServerMethod
	public static Object lastValue(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastValue(alias, xid, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * This method returns the last value for the given class
	 * instance having the value of the highest valued key.
	 * @return the Relation morphism having the value of highest key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastValue(TransactionId xid, Class clazz) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastValue(xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last value for the given class
	 * instance having the value of the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class table to query
	 * @return the instance having the value of highest key.
	 * @throws IOException
	 */
	@ServerMethod
	public static Object lastValue(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastValue(alias, xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationship
	 * instances in the scope of this transaction.
	 * @param xid the transaction id
	 * @return the number of Relation morphisms.
	 * @throws IOException
	 */
	@ServerMethod
	public static long size(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.size(xid, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationships in the scope of this transaction.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws NoSuchElementException if the alias is not found
	 * @return the number of Relation relationships.
	 * @throws IOException
	 */
	@ServerMethod
	public static long size(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.size(alias, xid, Relation.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static long size(TransactionId xid, Class c) throws IOException
	{
		try {
			return RelatrixKVTransaction.size(xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	@ServerMethod
	public static long size(Alias alias, TransactionId xid, Class c) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.size(alias, xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns whether the passed
	 * instance exists in the scope of this transaction.
	 * @param xid the transaction id
	 * @param obj the instance to locate based on class and value of object instance based on Comparable compareTo method
	 * @return true if the instance exists.
	 * @throws IOException
	 */
	@ServerMethod
	public static boolean contains(TransactionId xid, Comparable obj) throws IOException
	{
		try {
			return RelatrixKVTransaction.contains(xid, obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns whether the passed
	 * instance exists in the scope of this transaction for this database.
	 * @param xid the transaction id
	 * @param obj the instance to locate based on class and value of object instance based on Comparable compareTo method
	 * @return true if the instance exists.
	 * @throws IOException
	 * @throws NoSuchElementException of the alias doesnt exist
	 */
	@ServerMethod
	public static boolean contains(Alias alias, TransactionId xid, Comparable obj) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.contains(alias, xid, obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Store our permutations of the key/value within the scope of this transaction. In other words, this
	 * instance will not be visible outside this transaction scope until 'commit'. It can also be rolled back
	 * based on the transaction id.
	 * @param xid the transaction id
	 * @param key of comparable whose order is determined by Comparable interface contract via value of compareTo method
	 * @param value the value payload
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static void storekv(Alias alias, TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixKVTransaction.store(alias, xid, key, value);
	}

	/**
	 * Store our permutations of the key/value within the scope of this transaction. In other words, this
	 * instance will not be visible outside this transaction scope until 'commit'. It can also be rolled back
	 * based on the transaction id.
	 * @param xid the transaction id
	 * @param key of comparable whose order is determined by Comparable interface contract via value of compareTo method
	 * @param value the value payload
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@ServerMethod
	public static void storekv(TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixKVTransaction.store(xid, key, value);
	}

	/**
	 * Return the value for the key.
	 * @param xid the transaction id
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	@ServerMethod
	public static Object get(TransactionId xid, Comparable key) throws IOException, IllegalAccessException
	{
		return RelatrixKVTransaction.get(xid, key);
	}
	
	/**
	 * Return the value for the key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param key the key to retrieve
	 * @return The value for the key.
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias isnt found 
	 */
	@ServerMethod
	public static Object get(Alias alias, TransactionId xid, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVTransaction.get(alias, xid, key);
	}
	/**
	 * Return the Object pointed to by the DBKey. this is to support remote iterators.
	 * @param xid the transaction id
	 * @param key the key to retrieve
	 * @return The instance by DBKey
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 */
	@ServerMethod
	public static Object getByIndex(TransactionId xid, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return RelatrixKVTransaction.get(xid, (DBKey) key);
	}
	/**
	 * Return the Object pointed to by the DBKey. this is to support remote iterators.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param key the key to retrieve
	 * @return The instance by DBKey
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException
	 * @throws NoSuchElementException if alias is not found 
	 */
	@ServerMethod
	public static Object getByIndex(Alias alias, TransactionId xid, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException, NoSuchElementException
	{
		return RelatrixKVTransaction.get(xid, (DBKey) key);
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
	public static Iterator<?> keySet(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixKeysetIteratorTransaction(xid, clazz);
	}
	
	/**
	 * Return the keyset for the given class
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> keySet(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixKeysetIteratorTransaction(alias, xid, clazz);
	}
	
	@ServerMethod
	public static Iterator<?> entrySet(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixEntrysetIteratorTransaction(xid, clazz);
	}
	
	/**
	 * Return the entryset for the given class
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the iterator for the entryset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Iterator<?> entrySet(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixEntrysetIteratorTransaction(alias, xid, clazz);
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
	public static Stream<?> entrySetStream(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixStream(new RelatrixEntrysetIteratorTransaction(xid, clazz));
	}
	
	/**
	 * Return the entry set for the given class type
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@ServerMethod
	public static Stream<?> entrySetStream(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixStream(new RelatrixEntrysetIteratorTransaction(alias, xid, clazz));
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
	
	public static void main(String[] args) throws Exception {
		setTablespace(args[0]);
		RelatrixTransaction.findStream(new TransactionId(args[1]), '*', '*', '*').forEach((s) -> {
			System.out.println(s.toString());
		});
	}
 
}
