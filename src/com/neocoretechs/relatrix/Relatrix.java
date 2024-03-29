package com.neocoretechs.relatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.relatrix.key.RelatrixIndex;

import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.stream.RelatrixStream;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.session.DatabaseManager;

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
* Some of this work is based on a DBMS described by Alfonso F. Cardenas and Dennis McLeod (1990). Research Foundations 
* in Object-Oriented and Semantic Database Systems. Prentice Hall.
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism and the works of
* Mac Lane<p/>
* @author Jonathan Groff (C) NeoCoreTechs 1997,2013,2014,2015,2020,2021
*/
public final class Relatrix {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = true;
	private static boolean TRACE = true;
	
	public static char OPERATOR_WILDCARD_CHAR = '*';
	public static char OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
    private static final int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED;
	static final String databaseCatalogProperty = "Relatrix.Catalog";
	static String databaseCatalog = "/etc/db/";
	private static ConcurrentHashMap<String, DatabaseCatalog> pathToIndex = new ConcurrentHashMap<String,DatabaseCatalog>();
	private static ConcurrentHashMap<DatabaseCatalog, String> indexToPath = new ConcurrentHashMap<DatabaseCatalog,String>();
	
	private static SynchronizedFixedThreadPoolManager sftpm;
	public static final String storeX = "STOREX";
	public static final String storeI = "STOREI";
	public static final String deleteX = "DELETEX";
	public static final String deleteI = "DELETEI";
	
	static {
		if(System.getProperty(databaseCatalogProperty) != null)
			databaseCatalog = System.getProperty(databaseCatalogProperty);
		try {
			setAlias(databaseCatalogProperty, databaseCatalog);
			readDatabaseCatalog();
		} catch (IOException | IllegalAccessException | NoSuchElementException e) {
			e.printStackTrace();
		}
		sftpm = SynchronizedFixedThreadPoolManager.getInstance();
		sftpm.init(5, 5, new String[] {storeX,deleteX});
		sftpm.init(2, 2, new String[] {storeI,deleteI});
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
	public static synchronized void setTablespace(String path) throws IOException {
		RelatrixKV.setTablespace(path);
	}
	
	public static synchronized String getTableSpace() {
		return RelatrixKV.getTableSpace();
	}

	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @param path
	 * @throws IOException
	 */
	public static void setAlias(String alias, String path) throws IOException {
		RelatrixKV.setAlias(alias, path);
	}
	
	/**
	 * Will return null if alias does not exist
	 * @param alias
	 * @return
	 */
	public static String getAlias(String alias) {
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
	public static void removeAlias(String alias) throws NoSuchElementException {
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
	public static synchronized DomainMapRange store(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");

		DomainMapRange identity = new DomainMapRange(); // form it as template for duplicate key search
		identity.setDomain(d);
		identity.setMap(m);
		PrimaryKeySet primary = new PrimaryKeySet(identity);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		DBKey dbkey = primary.store();
		identity.setRange(r); // form it as template for duplicate key search
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		identity.setDBKey(dbkey);
		IndexResolver.getIndexInstanceTable().put(dbkey, identity);
		if( DEBUG  )
			System.out.println("Relatrix.store stored :"+identity);
		// Start threads to store remaining indexes now that we have our primary set up
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapDomainRange(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(dmr,identity.getDBKey());
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
	public static synchronized DomainMapRange store(String alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRange identity = new DomainMapRange(); // form it as template for duplicate key search
		identity.setDomain(alias,d);
		identity.setMap(alias,m);
		identity.setAlias(alias);
		PrimaryKeySet primary = new PrimaryKeySet(identity);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		DBKey dbkey = primary.store();
		identity.setRange(alias,r); // form it as template for duplicate key search
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		identity.setDBKey(dbkey);
		IndexResolver.getIndexInstanceTable().putAlias(alias, dbkey, identity);
		if( DEBUG  )
			System.out.println("Relatrix.store stored :"+identity);
		// Start threads to store remaining indexes now that we have our primary set up
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapDomainRange(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(alias,dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(alias,dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(alias,dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(alias,dmr,identity.getDBKey());
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
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKV.store(alias,dmr,identity.getDBKey());
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

	public static void storekv(String alias, Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException, NoSuchElementException {
		RelatrixKV.store(alias, key, value);
	}
	public static synchronized Object removekv(Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException {
		return RelatrixKV.remove(c);
	}
	public static synchronized Object removekv(String alias, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
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
	public static synchronized void remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);
		try {
			removeRecursive(c);
			if(c instanceof DomainMapRange) {
				PrimaryKeySet primary = new PrimaryKeySet((DomainMapRange) c);
				DomainRangeMap drm = new DomainRangeMap((DomainMapRange) c);
				MapDomainRange mdr = new MapDomainRange((DomainMapRange) c);
				MapRangeDomain mrd = new MapRangeDomain((DomainMapRange) c);
				RangeDomainMap rdm = new RangeDomainMap((DomainMapRange) c);
				RangeMapDomain rmd = new RangeMapDomain((DomainMapRange) c);
				
				DBKey primaryKey = (DBKey) RelatrixKV.remove(primary);
				IndexResolver.getIndexInstanceTable().delete(primaryKey);
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
			} else {
				IndexResolver.getIndexInstanceTable().deleteInstance(c);
			}
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
	public static synchronized void remove(String alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);
		try {
			removeRecursive(alias,c);
			if(c instanceof DomainMapRange) {
				PrimaryKeySet primary = new PrimaryKeySet((DomainMapRange) c);
				DomainRangeMap drm = new DomainRangeMap(alias,(DomainMapRange) c);
				MapDomainRange mdr = new MapDomainRange(alias,(DomainMapRange) c);
				MapRangeDomain mrd = new MapRangeDomain(alias,(DomainMapRange) c);
				RangeDomainMap rdm = new RangeDomainMap(alias,(DomainMapRange) c);
				RangeMapDomain rmd = new RangeMapDomain(alias,(DomainMapRange) c);

				DBKey primaryKey = (DBKey) RelatrixKV.remove(primary);
				IndexResolver.getIndexInstanceTable().delete(alias, primaryKey);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {    
						try {
							RelatrixKV.remove(alias,drm);
						} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
							throw new RuntimeException(e);
						}
					}
				},deleteX);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {    
						try {
							RelatrixKV.remove(alias,mdr);
						} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
							throw new RuntimeException(e);
						}
					}
				},deleteX);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {    
						try {
							RelatrixKV.remove(alias,mrd);
						} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
							throw new RuntimeException(e);
						}
					}
				},deleteX);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {    
						try {
							RelatrixKV.remove(alias,rdm);
						} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
							throw new RuntimeException(e);
						}
					}
				},deleteX);
				SynchronizedFixedThreadPoolManager.spin(new Runnable() {
					@Override
					public void run() {    
						try {
							RelatrixKV.remove(alias,rmd);
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
			} else {
				IndexResolver.getIndexInstanceTable().deleteInstanceAlias(alias,c);
			}
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove exiting remove for key:"+c);
	}

	/**
	 * Iterate through all possible relationships the given element may participate in, then recursively process those
	 * relationships to remove references to those.
	 * @param c The Comparable key
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws DuplicateKeyException 
	 */
	private static synchronized void removeRecursive(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, DuplicateKeyException {
			Iterator<?> it = findSet(c,"*","*");
			while(it.hasNext()) {
				Result o = (Result) it.next();
				if( DEBUG || DEBUGREMOVE)
					System.out.println("Relatrix.remove iterated perm 1 "+o+" of type "+o.get(0).getClass().getName());
				DomainMapRange dmr = (DomainMapRange)o.get(0);
				DomainRangeMap drm = new DomainRangeMap(dmr);
				MapDomainRange mdr = new MapDomainRange(dmr);
				MapRangeDomain mrd = new MapRangeDomain(dmr);
				RangeDomainMap rdm = new RangeDomainMap(dmr);
				RangeMapDomain rmd = new RangeMapDomain(dmr);

				PrimaryKeySet primary = new PrimaryKeySet(dmr);
				DBKey primaryKey = (DBKey) RelatrixKV.remove(primary);
				IndexResolver.getIndexInstanceTable().delete(primaryKey);
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
				removeRecursive(o.get(0)); 
			}
			it = findSet("*",c,"*");
			while(it.hasNext()) {
				Result o = (Result) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove iterated perm 2 "+o.get(0)+" of type "+o.get(0).getClass().getName());
				DomainMapRange dmr = (DomainMapRange)o.get(0);
				DomainRangeMap drm = new DomainRangeMap(dmr);
				MapDomainRange mdr = new MapDomainRange(dmr);
				MapRangeDomain mrd = new MapRangeDomain(dmr);
				RangeDomainMap rdm = new RangeDomainMap(dmr);
				RangeMapDomain rmd = new RangeMapDomain(dmr);

				PrimaryKeySet primary = new PrimaryKeySet(dmr);
				DBKey primaryKey = (DBKey) RelatrixKV.remove(primary); // remove primary key
				IndexResolver.getIndexInstanceTable().delete(primaryKey); // remove DMR index table and instance table via primary key
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
				removeRecursive(o.get(0)); 
			}
			it = findSet("*","*",c);
			while(it.hasNext()) {
				Result o = (Result) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove iterated perm 3 "+o.get(0)+" of type "+o.get(0).getClass().getName());
				DomainMapRange dmr = (DomainMapRange)o.get(0);
				DomainRangeMap drm = new DomainRangeMap(dmr);
				MapDomainRange mdr = new MapDomainRange(dmr);
				MapRangeDomain mrd = new MapRangeDomain(dmr);
				RangeDomainMap rdm = new RangeDomainMap(dmr);
				RangeMapDomain rmd = new RangeMapDomain(dmr);

				PrimaryKeySet primary = new PrimaryKeySet(dmr);
				DBKey primaryKey = (DBKey) RelatrixKV.remove(primary);
				IndexResolver.getIndexInstanceTable().delete(primaryKey);
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
				removeRecursive(o.get(0)); 
			}
	}
	/**
	 * Iterate through all possible relationships the given element may participate in, then recursively process those
	 * relationships to remove references to those.
	 * @param alias the database alias
	 * @param c The Comparable key
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if the alias is not found
	 * @throws DuplicateKeyException 
	 */
	private static synchronized void removeRecursive(String alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, DuplicateKeyException {
		Iterator<?> it = findSet(alias,c,"*","*");
		while(it.hasNext()) {
			Result o = (Result) it.next();
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove iterated perm 1 "+o.get(0)+" of type "+o.get(0).getClass().getName());
			DomainMapRange dmr = (DomainMapRange)o.get(0);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);

			PrimaryKeySet primary = new PrimaryKeySet(dmr);
			DBKey primaryKey = (DBKey) RelatrixKV.remove(alias, primary);
			IndexResolver.getIndexInstanceTable().deleteAlias(alias, primaryKey);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,rmd);
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
			removeRecursive(alias,o.get(0)); 
		}
		it = findSet(alias,"*",c,"*");
		while(it.hasNext()) {
			Result o = (Result) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove iterated perm 2 "+o.get(0)+" of type "+o.get(0).getClass().getName());
			DomainMapRange dmr = (DomainMapRange)o.get(0);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);

			PrimaryKeySet primary = new PrimaryKeySet(dmr);
			DBKey primaryKey = (DBKey) RelatrixKV.remove(alias, primary);
			IndexResolver.getIndexInstanceTable().deleteAlias(alias, primaryKey);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,rmd);
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
			removeRecursive(alias,o.get(0)); 
		}
		it = findSet(alias,"*","*",c);
		while(it.hasNext()) {
			Result o = (Result) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove iterated perm 3 "+o.get(0)+" of type "+o.get(0).getClass().getName());
			DomainMapRange dmr = (DomainMapRange)o.get(0);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);

			PrimaryKeySet primary = new PrimaryKeySet(dmr);
			DBKey primaryKey = (DBKey) RelatrixKV.remove(alias, primary);
			IndexResolver.getIndexInstanceTable().deleteAlias(alias, primaryKey);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,drm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,mdr);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,mrd);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,rdm);
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			},deleteX);
			SynchronizedFixedThreadPoolManager.spin(new Runnable() {
				@Override
				public void run() {    
					try {
						RelatrixKV.remove(alias,rmd);
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
			removeRecursive(alias,o.get(0)); 
		}
	}
	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @param r
	 * @throws IllegalAccessException 
	 * @throws DuplicateKeyException 
	 * @throws ClassNotFoundException 
	 */
	public static synchronized void remove(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException, ClassNotFoundException, DuplicateKeyException {
		DomainMapRange dmr = new DomainMapRange(d,m,r);
		remove(dmr);
	}

	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @param r
	 * @throws IllegalAccessException 
	 * @throws DuplicateKeyException 
	 * @throws ClassNotFoundException 
	 */
	public static synchronized void remove(String alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException, NoSuchElementException, ClassNotFoundException, DuplicateKeyException {
		DomainMapRange dmr = new DomainMapRange(alias,d,m,r);
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
	public static synchronized Iterator<?> findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
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
	public static synchronized Iterator<?> findSet(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
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
	public static synchronized Stream<?> findStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
	}

	public static synchronized Stream<?> findStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Iterator<?> findTailSet(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator();
	}

	public static synchronized Iterator<?> findTailSetAlias(String alias, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Stream<?> findTailStream(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}

	public static synchronized Stream<?> findTailStreamAlias(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Iterator<?> findHeadSet(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator();
	}

	public static synchronized Iterator<?> findHeadSetAlias(String alias, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Stream<?> findHeadStream(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}

	public static synchronized Stream<?> findHeadStreamAlias(String alias, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Iterator<?> findSubSet(Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
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
	public static synchronized Iterator<?> findSubSetAlias(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Stream<?> findSubStream(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
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
	public static synchronized Stream<?> findSubStreamAlias(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object first() throws IOException
	{
		try {
			return RelatrixKV.firstKey(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object first(String alias) throws IOException, NoSuchElementException
	{
	
		try {
			return RelatrixKV.firstKey(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object firstKey() throws IOException
	{

		try {
			return RelatrixKV.firstKey(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object firstKey(String alias) throws IOException, NoSuchElementException
	{

		try {
			return RelatrixKV.firstKey(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object firstValue() throws IOException
	{
	
		try {
			return RelatrixKV.firstValue(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object firstValue(String alias) throws IOException, NoSuchElementException
	{
	
		try {
			return RelatrixKV.firstValue(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
	 * instance having the lowest valued key value.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object first(Class clazz) throws IOException
	{
		try {
			return RelatrixKV.firstKey(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object first(String alias, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.firstKey(alias, clazz);
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
	public static synchronized Object last() throws IOException
	{
		try {
			return RelatrixKV.lastKey(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object last(String alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastKey(alias, DomainMapRange.class);
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
	public static synchronized Object lastKey() throws IOException
	{
		try {
			return RelatrixKV.lastKey(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object lastKey(String alias) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastKey(alias, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the value of the highest valued key.
	 * @return the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 */
	public static synchronized Object lastValue() throws IOException
	{
		try {
			return RelatrixKV.lastValue(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object lastValue(String alias) throws IOException, NoSuchElementException
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
	public static synchronized Object last(Class clazz) throws IOException
	{
		try {
			return RelatrixKV.lastKey(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object last(String alias, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastKey(alias, clazz);
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
	public static synchronized Object lastKey(Class clazz) throws IOException
	{
		try {
			return RelatrixKV.lastKey(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object lastKey(String alias, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKV.lastKey(alias, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
	 * instance having the value of the highest valued key.
	 * @return the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 */
	public static synchronized Object lastValue(Class clazz) throws IOException
	{
		try {
			return RelatrixKV.lastValue(clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object lastValue(String alias, Class clazz) throws IOException, NoSuchElementException
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
	public static synchronized long size() throws IOException
	{
		try {
			return RelatrixKV.size(DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized long size(String alias) throws IOException, NoSuchElementException
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
	public static synchronized long size(Class c) throws IOException
	{
		try {
			return RelatrixKV.size(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized long size(String alias, Class c) throws IOException, NoSuchElementException
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
	public static synchronized boolean contains(Comparable obj) throws IOException
	{
		try {
			return RelatrixKV.contains(obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized boolean contains(String alias, Comparable obj) throws IOException, NoSuchElementException
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
	public static synchronized RelatrixIndex getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID uuid = UUID.randomUUID();
		RelatrixIndex nkey = new RelatrixIndex(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
		if(DEBUG)
			System.out.printf("Returning NewKey=%s%n", nkey.toString());
		return nkey;
	}


	/**
	 * return lowest valued key.
	 * @param clazz the class to retrieve
	 * @return the The key/value with lowest key value.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized Object firstKey(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.firstKey(clazz);
	}

	public static synchronized Object firstKey(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.firstKey(alias, clazz);
	}
	/**
	 * The lowest key value object
	 * @param clazz the class to retrieve
	 * @return The first value of the class with given key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized Object firstValue(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.firstValue(clazz);
	}

	public static synchronized Object firstValue(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object get(Comparable key) throws IOException, IllegalAccessException
	{
		return RelatrixKV.get(key);
	}

	public static synchronized Object get(String alias, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object getByIndex(DBKey key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return IndexResolver.getIndexInstanceTable().getByIndex(key);
	}

	/**
	 * Return the keyset for the given class
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static synchronized Iterator<?> keySet(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.keySet(clazz);
	}

	public static synchronized Iterator<?> keySet(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.keySet(alias, clazz);
	}
	public static synchronized Iterator<?> entrySet(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.entrySet(clazz);
	}

	public static synchronized Iterator<?> entrySet(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.entrySet(alias, clazz);
	}
	/**
	 * Return the entry set for the given class type
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static synchronized Stream<?> entrySetStream(Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKV.entrySetStream(clazz);
	}

	public static synchronized Stream<?> entrySetStream(String alias, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKV.entrySetStream(alias, clazz);
	}

	/**
	 * Generate the recursively resolved list of relationships in the given Morphism. If none of the components
	 * of the relationship are themselves relationships, the original set of related objects in the tuple is returned as a list.
	 * @param morphism the target for resolution
	 * @return the recursively resolved list of relationships depth first from domain to range
	 */
	public static synchronized List<Comparable> resolve(Comparable morphism) {
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
	public static synchronized void loadClassFromPath(String pack, String path) throws IOException {
		Path p = FileSystems.getDefault().getPath(path);
		HandlerClassLoader.setBytesInRepository(pack,p);
	}
	/**
	 * Load the jar file located at jar into the repository
	 * @param jar
	 * @throws IOException
	 */
	public static synchronized void loadClassFromJar(String jar) throws IOException {
		HandlerClassLoader.setBytesInRepositoryFromJar(jar);
	}
	/**
	 * Remove the stated package from the declared package and all subpackages from the bytecode repository
	 * @param pack
	 * @param path
	 * @throws IOException
	 */
	public static synchronized void removePackageFromRepository(String pack) throws IOException {
		HandlerClassLoader.removeBytesInRepository(pack);
	}
	
	static void readDatabaseCatalog() throws IllegalAccessException, NoSuchElementException, IOException {
		if(DEBUG)
			System.out.println("Relatrix.readDatabaseCatalog");
		Iterator<?> it = RelatrixKV.entrySet(databaseCatalogProperty, DatabaseCatalog.class);
		while(it.hasNext()) {
			Entry e = (Entry) it.next();
			indexToPath.put((DatabaseCatalog)e.getKey(), (String)e.getValue());
			pathToIndex.put((String)e.getValue(), (DatabaseCatalog)e.getKey());
			if(DEBUG)
				System.out.println("Relatrix.readDatabaseCatalog indexToPath:"+e.getKey()+" pathToIndex:"+e.getValue());
		}
		if(DEBUG)
			System.out.println("Closing "+databaseCatalogProperty);
		//RelatrixKV.close(databaseCatalogProperty, UUID.class);
		if(DEBUG)
			System.out.println("Closed "+databaseCatalogProperty);
	}

	static void writeDatabaseCatalog() throws IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		Iterator<Map.Entry<DatabaseCatalog, String>> it = indexToPath.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<DatabaseCatalog, String> entry = it.next();
			RelatrixKV.store(databaseCatalogProperty, entry.getKey(), entry.getValue());
		}
	}
	/**
	 * Get the RelatrixIndex for the given tablespace path. If the index does not exist, it will be created based on param
	 * @param path
	 * @param create
	 * @return the RelatrixIndex of path
	 */
	public static DatabaseCatalog getByPath(String path, boolean create) {
		if(DEBUG)
			System.out.println("Relatrix.getByPath attempt for path:"+path+" create:"+create);
		DatabaseCatalog v = pathToIndex.get(path);
		// If we did not find it and another process created it, read catalog
		if(v == null) {
			try {
				readDatabaseCatalog();
				v = pathToIndex.get(path);
			} catch (IllegalAccessException | NoSuchElementException | IOException e) {
				e.printStackTrace();
			}
		}
		if(v == null && create) {
			v = new DatabaseCatalog(UUID.randomUUID());
			if(DEBUG)
				System.out.println("Relatrix.getByPath creating new index for path:"+path+" with catalog:"+v);
			pathToIndex.put(path, v);
			indexToPath.put(v, path);
			try {
				RelatrixKV.store(databaseCatalogProperty, v, path);
			} catch (IllegalAccessException | NoSuchElementException | IOException | DuplicateKeyException e) {
				e.printStackTrace();
			}
		}
		if(DEBUG)
			System.out.println("Relatrix.getByPath returning:"+v+" for path:"+path+" create:"+create);
		return v;
	}
	/**
	 * Get the path for the given index. If the path does not exist, it will NOT be created.
	 * @param index
	 * @return path from indexToPath
	 */
	public static String getDatabasePath(DatabaseCatalog index) {
		if(DEBUG)
			System.out.println("Relatrix.getDatabasePath for catalog:"+index+" will result in:"+indexToPath.get(index));
		return indexToPath.get(index);
	}
	/**
	 * Get the tablespace path for the given alias
	 * @param alias
	 * @return The path for this alias or null if none
	 */
	public static String getAliasToPath(String alias) {
		if(DEBUG)
			System.out.println("Relatrix.getAliasToPath attempt for alias:"+alias+" will return:"+DatabaseManager.getAliasToPath(alias));
		return DatabaseManager.getAliasToPath(alias);
	}
	/**
	 * Get the index for the given alias. If the index does not exist, it will be created
	 * @param alias
	 * @return The {@link DatabaseCatalog} index for the alias
	 * @throws NoSuchElementException If the alias was not found
	 */
	public static DatabaseCatalog getByAlias(String alias) throws NoSuchElementException {
		String path = getAliasToPath(alias);
		if(path == null)
			throw new NoSuchElementException("The alias "+alias+" was not found.");
		if(DEBUG)
			System.out.println("Relatrix.getByAlias attempt for alias:"+alias+" got path:"+path);
		return getByPath(path, true);
	}

	/**
	 * Remove the given tablespace path for index.
	 * @param index
	 * @return previous String path of removed {@link DatabaseCatalog} index
	 */
	static String removeDatabaseCatalog(DatabaseCatalog index) {
		if(DEBUG)
			System.out.println("Relatrix.removeDatabaseCatalog for index:"+index);
		String ret = indexToPath.remove(index);
		if(ret != null)
			pathToIndex.remove(ret);
		try {
			RelatrixKV.remove(index);
			//RelatrixKV.close(databaseCatalogProperty, UUID.class);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * Remove the index for the given tablespace path.
	 * @param path
	 * @return {@link DatabaseCatalog} index of removed path
	 */
	static DatabaseCatalog removeDatabaseCatalog(String path) {
		DatabaseCatalog ret = pathToIndex.remove(path);
		if(DEBUG)
			System.out.println("Relatrix.removeDatabaseCatalog for path:"+path+" will return previous index:"+ret);		
		if(ret != null)
			indexToPath.remove(ret);
		try {
			RelatrixKV.remove(ret);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		indexToPath.entrySet().forEach(System.out::println);
	}

}
