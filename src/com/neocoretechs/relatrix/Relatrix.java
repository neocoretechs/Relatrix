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
import java.util.stream.Stream;

import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.stream.RelatrixStream;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.rocksack.Alias;

/**
* Top-level class that imparts behavior to the {@link Morphism} subclasses which contain references for domain, map, range.<p/>
* The lynch pin is the Morphism and its subclasses indexed
* by the 6 permutations of the domain,map,and range so we can retrieve instances in all
* the potential sort orders.
* The compareTo and fullCompareTo of Morphism provide the comparison methods to drive the processes.
* For retrieval, a partial template is constructed of the proper Morphism subclass which puts the three elements
* in the proper sort order. To retrieve the proper Morphism subclass, partially construct a morphism template to
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
* since conceptually a Morphism is a domain acted upon by the map function yielding the range.<p/>
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
	
	public static char OPERATOR_WILDCARD_CHAR = '*';
	public static char OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
  
	private static SynchronizedFixedThreadPoolManager sftpm;
	public static final String storeX = "STOREXTX";
	public static final String storeI = "STOREITX";
	public static final String deleteX = "DELETEXTX";
	public static final String searchX = "SEARCHXTX";
	
	static {
		sftpm = SynchronizedFixedThreadPoolManager.getInstance();
		sftpm.init(6, 6, new String[] {storeX});
		sftpm.init(5, 5, new String[] {deleteX});
		sftpm.init(2, 2, new String[] {storeI});
		sftpm.init(3, 3, new String[] {searchX});
	}
	
	
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
			}
		}
		return instance;
	}	
	

	/**
	* Calling these methods allows the user to substitute their own
	* symbology for the usual Findset semantics. If you absolutely
	* need to store values confusing to the standard findset *,? semantics.
	* */
	public static void setWildcard(char wc) {
		OPERATOR_WILDCARD_CHAR = wc;
		OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	}
	public static void setTuple(char tp) {
		OPERATOR_TUPLE_CHAR = tp;
		OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	}
	/**
	 * Verify that we are specifying a directory, then set that as top level file structure and database name
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespace(String path) throws IOException {
		RelatrixKV.setTablespace(path);
	}
	
	public static String getTableSpace() {
		return RelatrixKV.getTableSpace();
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
	public static String getAlias(Alias alias) {
		return RelatrixKV.getAlias(alias);
	}
	
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	public static String[][] getAliases() {
		return RelatrixKV.getAliases();
	}
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
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
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	public static DomainMapRange store(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRange identity = new DomainMapRange(); // form it as template for duplicate key search
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
			identity.setRange(r);
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(identity));
		} else
			throw new DuplicateKeyException("Relationship ["+d+"->"+r+"] already exists.");
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		if( DEBUG  )
			System.out.println("Relatrix.store stored :"+identity);
		// store the primary, but not in the DBKey table
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKV.store(pk,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored primary key :"+pk);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			} // run
		},storeX); // spin 
		// Start threads to store remaining indexes now that we have our primary set up
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapDomainRange(identity);
					RelatrixKV.store(dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			} // run
		},storeX); // spin 
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new DomainRangeMap(identity);
					RelatrixKV.store(dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapRangeDomain(identity);
					RelatrixKV.store(dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {  
				try {
					Morphism dmr = new RangeDomainMap(identity);
					RelatrixKV.store(dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					Morphism dmr = new RangeMapDomain(identity);
					RelatrixKV.store(dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeX);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	public static DomainMapRange store(Alias alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRange identity = new DomainMapRange(); // form it as template for duplicate key search
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
			identity.setRange(alias, r);
			// newKey will call into DBKey.newKey with proper transactionId and alias
			// and then call proper indexInstanceTable.put(instance) to place the DBKey/instance instance/DBKey
			// and return the new DBKey reference
			identity.setIdentity(identity.newKey(identity));
		} else
			throw new DuplicateKeyException("Relationship ["+d+"->"+r+"] already exists.");
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		if( DEBUG  )
			System.out.println("Relatrix.store stored :"+identity);
		// store the primary, but not in the DBKey table
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					RelatrixKV.store(alias, pk, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored primary key :"+pk);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			} // run
		},storeX); // spin 
		// Start threads to store remaining indexes now that we have our primary set up
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapDomainRange(alias,identity);
					RelatrixKV.store(alias,dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			} // run
		},storeX); // spin 
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new DomainRangeMap(alias,identity);
					RelatrixKV.store(alias,dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapRangeDomain(alias,identity);
					RelatrixKV.store(alias,dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {  
				try {
					Morphism dmr = new RangeDomainMap(alias,identity);
					RelatrixKV.store(alias,dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					Morphism dmr = new RangeMapDomain(alias,identity);
					RelatrixKV.store(alias,dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeX);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeX);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return identity;
	}

	public static void storekv(Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException {
		RelatrixKV.store(key, value);
	}

	public static void storekv(Alias alias, Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException, NoSuchElementException {
		RelatrixKV.store(alias, key, value);
	}
	public static Object removekv(Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException {
		return RelatrixKV.remove(c);
	}
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
	public static void remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);// Remove main entry, which is possibly DomainMapRange
		DBKey primaryKey = (DBKey) RelatrixKV.remove(c);
		// remove DBKey table
		RelatrixKV.remove(primaryKey);
		// Remove primary key if Morphism
		if(c instanceof Morphism) {
			DomainMapRange dmr = (DomainMapRange)c;
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
	public static void remove(Alias alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);
		// Remove main entry, which is possibly DomainMapRange
		DBKey primaryKey = (DBKey) RelatrixKV.remove(alias, c);
		// remove DBKey table
		RelatrixKV.remove(alias, primaryKey);
		// Remove primary key if Morphism
		if(c instanceof Morphism) {
			DomainMapRange dmr = (DomainMapRange)c;
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
		DomainMapRange dmr = new DomainMapRange(true, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
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
		DomainMapRange dmr = new DomainMapRange(true, alias, null, c, null, DBKey.nullDBKey, null, DBKey.nullDBKey);
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
	 * Search the domain, map, and range for each Morphism for relations containing iterator elements
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
				if(!deleted.contains(((Morphism)o.get(0)).getIdentity())) {
					deleted.add(((Morphism)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		try {
			while(itm.hasNext()) {
				Result o = (Result) itm.next();
				if(!deleted.contains(((Morphism)o.get(0)).getIdentity())) {
					deleted.add(((Morphism)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		try {
			while(itr.hasNext()) {
				Result o = (Result) itr.next();
				if(!deleted.contains(((Morphism)o.get(0)).getIdentity())) {
					deleted.add(((Morphism)o.get(0)).getIdentity());
				}
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		//System.out.println("sequentialSearch elapsed:"+(System.nanoTime()-tim1)+" nanos.");
	}
	
	/**
	 * Appears slower for this application
	 * @param itd
	 * @param itm
	 * @param itr
	 * @param deleted
	 */
	private static void parallelSearch(Iterator<?> itd, Iterator<?> itm, Iterator<?> itr, List<DBKey> deleted) {
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					while(itd.hasNext()) {
						Result o = (Result) itd.next();
						if(!deleted.contains(((Morphism)o.get(0)).getIdentity())) {
							deleted.add(((Morphism)o.get(0)).getIdentity());
						}
					}
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
			}
		},searchX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					while(itm.hasNext()) {
						Result o = (Result) itm.next();
						if(!deleted.contains(((Morphism)o.get(0)).getIdentity())) {
							deleted.add(((Morphism)o.get(0)).getIdentity());
						}
					}
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
			}
		},searchX);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					while(itr.hasNext()) {
						Result o = (Result) itr.next();
						if(!deleted.contains(((Morphism)o.get(0)).getIdentity())) {
							deleted.add(((Morphism)o.get(0)).getIdentity());
						}
					}
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
			}
		},searchX);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(searchX);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	private static void removeParallel(List<DBKey> removed) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		for(DBKey dbk : removed) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove iterated perm 1 "+dbk);
			DomainMapRange dmr = (DomainMapRange) RelatrixKV.remove(dbk); // dbkey table
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
			DomainMapRange dmr = (DomainMapRange) RelatrixKV.remove(alias, dbk); // dbkey
			RelatrixKV.remove(alias, dmr); //instance
			PrimaryKeySet pks = new PrimaryKeySet(dmr.getDomainKey(),dmr.getMapKey(), alias);
			RelatrixKV.remove(alias, pks);
			dmr.setAlias(alias);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);
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
	public static void remove(Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, ClassNotFoundException, DuplicateKeyException {
		DomainMapRange dmr = new DomainMapRange(d,m,null);
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
	public static void remove(Alias alias, Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, NoSuchElementException, ClassNotFoundException, DuplicateKeyException {
		DomainMapRange dmr = new DomainMapRange(alias,d,m,null);
		remove(alias, dmr);
	}

	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned {@link Result} is always of depth n="# of question marks" or hierarchy of objects.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type DomainMapRange. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
		return ifact.createIterator();
	}
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned Result is always of hierarchy n="# of question marks" or a one element class hierarchy {@link Result} of a single object.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type DomainMapRange. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static Iterator<?> findSet(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
		return ifact.createIterator(alias);
	}
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned Stream is always of dimension n="# of question marks" or a one element array of a single object.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type DomainMapRange. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * 
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param parallel Optional argument to invoke parallel stream
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream pipeline with the retrieved elements
	 */
	public static Stream<?> findStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}

	public static Stream<?> findStream(Alias alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
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
	public static Iterator<?> findTailSet(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator();
	}

	public static Iterator<?> findTailSet(Alias alias, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactory(darg, marg, rarg, endarg);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Result>
	 */
	public static Stream<?> findTailStream(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}

	public static Stream<?> findTailStream(Alias alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactory(darg, marg, rarg, endarg);
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
	public static Iterator<?> findHeadSet(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator();
	}

	public static Iterator<?> findHeadSet(Alias alias, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(alias, darg, marg, rarg, endarg);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public static Stream<?> findHeadStream(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}

	public static Stream<?> findHeadStream(Alias alias, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three.
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * arguments to the ending point of the associated variable number of parameters, which must match the number of objects
	 * passed in the first three arguments. If a passed argument in the first 3 parameters is neither "*" (wildcard)
	 * or "?" (return the object from the retrieved tuple morphism) then it is presumed to be an object.
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * Semantically, this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * Since this is a subset operation, the additional constraint is applied that the ending declaration of the subset retrieval
	 * must match the number of concrete objects vs wildcards in the first part of the declaration.
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public static Iterator<?> findSubSet(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator();
	}
	/**
	 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param alias
	 * @param darg
	 * @param marg
	 * @param rarg
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return Iterator for the subSet
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	public static Iterator<?> findSubSet(Alias alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator(alias);
	}
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three.
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * arguments to the ending point of the associated variable number of parameters, which must match the number of objects
	 * passed in the first three arguments. If a passed argument in the first 3 parameters is neither "*" (wildcard)
	 * or "?" (return the object from the retrieved tuple morphism) then it is presumed to be an object.
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * Semantically, this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * Since this is a subset operation, the additional constraint is applied that the ending declaration of the subset retrieval
	 * must match the number of concrete objects vs wildcards in the first part of the declaration.
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The Stream from which the data may be retrieved. Follows Stream interface, return Stream<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public static Stream<?> findSubStream(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}
	/**
	 * Provides a persistent collection stream of keys 'from' element inclusive, 'to' element exclusive of the keys specified<p/>
	 * @param alias
	 * @param darg
	 * @param marg
	 * @param rarg
	 * @param endarg variable length set of parameters qualifying the non-concrete (wildcard or return-object) parameters. Either of Class or instance type.
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException
	 */
	public static Stream<?> findSubStream(Alias alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator(alias));	
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static Object first() throws IOException
	{
		try {
			DomainMapRange dmr = (DomainMapRange) RelatrixKV.firstKey(DomainMapRange.class);
			DBKey dbkey = (DBKey) RelatrixKV.firstValue(DomainMapRange.class);
			dmr.setIdentity(dbkey);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static Object first(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			DomainMapRange dmr = (DomainMapRange) RelatrixKV.firstKey(alias,DomainMapRange.class);
			DBKey dbkey = (DBKey) RelatrixKV.firstValue(alias,DomainMapRange.class);
			dmr.setIdentity(dbkey);
			dmr.setAlias(alias);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
	 * instance having the lowest valued key value.
	 * @return the class having the lowest valued key value.
	 * @throws IOException
	 */
	public static Object first(Class clazz) throws IOException
	{
		Object o = null;
		try {
			o = RelatrixKV.firstKey(clazz);
			if(o instanceof Morphism) {
				DBKey dbkey = (DBKey) RelatrixKV.firstValue(clazz);
				((Morphism)o).setIdentity(dbkey);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}

	public static Object first(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		Object o = null;
		try {
			o = RelatrixKV.firstKey(alias,clazz);
			if(o instanceof Morphism) {
				DBKey dbkey = (DBKey) RelatrixKV.firstValue(alias,clazz);
				((Morphism)o).setIdentity(dbkey);
				((Morphism)o).setAlias(alias);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static Object firstKey() throws IOException
	{
		return first();
	}

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
	public static Object firstKey(Class clazz) throws IOException, IllegalAccessException
	{
		return first(clazz);
	}

	public static Object firstKey(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return first(alias, clazz);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the {@link DBKey} having the lowest valued key value.
	 * @throws IOException
	 */
	public static Object firstValue() throws IOException
	{
		try {
			return RelatrixKV.firstValue(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the value of the first instance of {@link Morphism} {@link DomainMapRange} which will be a {@link DBKey}
	 * @param alias the database alias to retrieve the instance value
	 * @return the first value
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	public static Object firstValue(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.firstValue(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the last valued key.
	 * @return the DomainMapRange morphism having the last key value with resolved identity.
	 * @throws IOException
	 */
	public static Object last() throws IOException
	{
		try {
			DomainMapRange dmr = (DomainMapRange) RelatrixKV.lastKey(DomainMapRange.class);
			DBKey dbkey = (DBKey) RelatrixKV.lastValue(DomainMapRange.class);
			dmr.setIdentity(dbkey);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the last instance of {@link Morphism} {@link DomainMapRange}
	 * @param alias
	 * @return the last DomainMapRange instance, with identity and alias resolved
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	public static Object last(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			DomainMapRange dmr = (DomainMapRange) RelatrixKV.lastKey(alias,DomainMapRange.class);
			DBKey dbkey = (DBKey) RelatrixKV.lastValue(alias,DomainMapRange.class);
			dmr.setIdentity(dbkey);
			dmr.setAlias(alias);
			return dmr;
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @param clazz the class target
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 */
	public static Object last(Class clazz) throws IOException
	{
		Object o = null;
		try {
			o = RelatrixKV.lastKey(clazz);
			if(o instanceof Morphism) {
				DBKey dbkey = (DBKey) RelatrixKV.lastValue(clazz);
				((Morphism)o).setIdentity(dbkey);
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
	public static Object last(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		Object o = null;
		try {
			o = RelatrixKV.lastKey(alias,clazz);
			if(o instanceof Morphism) {
				DBKey dbkey = (DBKey) RelatrixKV.lastValue(alias,clazz);
				((Morphism)o).setIdentity(dbkey);
				((Morphism)o).setAlias(alias);
			}
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		return o;
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 */
	public static Object lastKey() throws IOException
	{
		return last();
	}

	public static Object lastKey(Alias alias) throws IOException, NoSuchElementException
	{
		return last(alias);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the value of the highest valued key.
	 * @return the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 */
	public static Object lastValue() throws IOException
	{
		try {
			return RelatrixKV.lastValue(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static Object lastValue(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastValue(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 */
	public static Object lastKey(Class clazz) throws IOException
	{
		return last(clazz);
	}

	public static Object lastKey(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		return last(alias, clazz);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the value of the highest valued key.
	 * @return the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 */
	public static Object lastValue(Class clazz) throws IOException
	{
		try {
			return RelatrixKV.lastValue(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static Object lastValue(Alias alias, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastValue(alias, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationships, Which are occurrences {@link DomainMapRange} instances.
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 */
	public static long size() throws IOException
	{
		try {
			return RelatrixKV.size(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static long size(Alias alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.size(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationships, Which are occurrences {@link DomainMapRange} instances.
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 */
	public static long size(Class c) throws IOException
	{
		try {
			return RelatrixKV.size(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static long size(Alias alias, Class c) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.size(alias, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns whether the passed DomainMapRange
	 * instance exists in the data.
	 * @return true if the passed DomainMapRAnge exists.
	 * @throws IOException
	 */
	public static boolean contains(Comparable obj) throws IOException
	{
		try {
			return RelatrixKV.contains(obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

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
	public static Object firstValue(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.firstValue(clazz);
	}

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
	public static Object get(Comparable key) throws IOException, IllegalAccessException
	{
		return RelatrixKV.get(key);
	}

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
	public static Iterator<?> keySet(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.keySet(clazz);
	}

	public static Iterator<?> keySet(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.keySet(alias, clazz);
	}
	public static Iterator<?> entrySet(Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixEntrysetIterator(clazz);
	}

	public static Iterator<?> entrySet(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static Stream<?> entrySetStream(Class clazz) throws IOException, IllegalAccessException
	{
		return new RelatrixStream(new RelatrixEntrysetIterator(clazz));
	}

	public static Stream<?> entrySetStream(Alias alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixStream(new RelatrixEntrysetIterator(alias,clazz));
	}

	/**
	 * Generate the recursively resolved list of relationships in the given Morphism. If none of the components
	 * of the relationship are themselves relationships, the original set of related objects in the tuple is returned as a list.
	 * @param morphism the target for resolution
	 * @return the recursively resolved list of relationships depth first from domain to range
	 */
	public static List<Comparable> resolve(Comparable morphism) {
		ArrayList<Comparable> res = new ArrayList<Comparable>();
		Morphism.resolve(morphism, res);
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

	public static void main(String[] args) throws Exception {
		
	}

}
