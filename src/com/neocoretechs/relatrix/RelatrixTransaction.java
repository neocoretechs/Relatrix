package com.neocoretechs.relatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.SynchronizedFixedThreadPoolManager;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.stream.RelatrixStream;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
* Top-level class that imparts behavior to the Morphism subclasses which contain references for domain, map, range.<p/>
* The lynch pin is the Morphism and its subclasses indexed
* in the 6 permutations of the domain,map,and range so we can retrieve instances in all
* the potential sort orders.<b/>
* The compareTo and fullCompareTo of Morphism provide the comparison methods to drive the processes.
* For retrieval, a partial template is constructed of the proper Morphism subclass which puts the three elements
* in the proper sort order. To retrieve the proper Morphism subclass, partially construct a morphism template to
* order the result set. The retrieval operators allow us to form the partially ordered result sets that are returned.<p/>
* The critical concept about retrieving relationships is to remember that the number of elements from each passed
* stream element or iteration of a Stream or Iterator is dependent on the number of "?" operators in a 'findSet'. For example,
* if we declare findHeadSet("*","?","*") we get back a {@link Result} of one element, for findSet("?",object,"?") we
* would get back a {@link Result2} array, with each element of the relationship returned.<br/>
* If we findHeadStream("*","?","*") we return a stream where one Comparable array element can be mapped, reduced, consumed, etc.<br/>
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
public final class RelatrixTransaction {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = true;
	private static boolean TRACE = true;
	
	public static char OPERATOR_WILDCARD_CHAR = '*';
	public static char OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	
	private static SynchronizedFixedThreadPoolManager sftpm;
	public static final String storeXTransaction = "STOREXTX";
	public static final String storeITransaction = "STOREITX";
	public static final String deleteXTransaction = "DELETEXTX";
	public static final String deleteITransaction = "DELETEITX";
	
	static {
		sftpm = SynchronizedFixedThreadPoolManager.getInstance();
		sftpm.init(5, 5, new String[] {storeXTransaction,deleteXTransaction});
		sftpm.init(2, 2, new String[] {storeITransaction,deleteITransaction});
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
		RelatrixKVTransaction.setTablespace(path);
	}
	
	/**
	 * Get the default tablespace directory
	 * @return the path/dbname of current default tablespace
	 */
	public static String getTableSpace() {
		return RelatrixKVTransaction.getTableSpace();
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
	public static void removeAlias(Alias alias) throws NoSuchElementException {
		RelatrixKVTransaction.removeAlias(alias);
	}
	
	/**
	 * Get the tablespace path for this alias. Will return null if alias does not exist.
	 * @param alias
	 * @return The tablespace path of this alias as a String
	 */
	public static String getAlias(Alias alias) {
		return RelatrixKVTransaction.getAlias(alias);
	}
	/**
	 * 
	 * @return 2d array of aliases to paths. If none 1st dimension is 0.
	 */
	public static String[][] getAliases() {
		return RelatrixKVTransaction.getAliases();
	}
	
	/**
	 * Get a new transaction ID
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static TransactionId getTransactionId() throws IllegalAccessException, IOException, ClassNotFoundException {
		TransactionId xid =  RelatrixKVTransaction.getTransactionId();
		return xid;
	}
	
	/**
	 * @param xid
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
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
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	public static synchronized DomainMapRange store(TransactionId xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRange identity = new DomainMapRange(); // form it as template for duplicate key search
		identity.setTransactionId(xid);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		identity.store(d,m,r);
		// Start threads to store remaining indexes now that we have our primary set up
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapDomainRange(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			} // run
		},storeXTransaction); // spin 
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new DomainRangeMap(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapRangeDomain(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {  
				try {
					Morphism dmr = new RangeDomainMap(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					Morphism dmr = new RangeMapDomain(identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(xid, dmr,identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeXTransaction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 * @throws ClassNotFoundException 
	 */
	public static synchronized DomainMapRange store(Alias alias, TransactionId xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRange identity = new DomainMapRange(); // form it as template for duplicate key search
		identity.setAlias(alias);
		identity.setTransactionId(xid);
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		identity.store(d, m, r);
		// Start threads to store remaining indexes now that we have our primary set up
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapDomainRange(alias,identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			} // run
		},storeXTransaction); // spin 
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new DomainRangeMap(alias,identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {
				try {
					Morphism dmr = new MapRangeDomain(alias,identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {  
				try {
					Morphism dmr = new RangeDomainMap(alias, identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		SynchronizedFixedThreadPoolManager.spin(new Runnable() {
			@Override
			public void run() {    
				try {
					Morphism dmr = new RangeMapDomain(alias, identity);
					//IndexResolver.getIndexInstanceTable().put(dmr);
					RelatrixKVTransaction.store(alias, xid, dmr, identity.getIdentity());
					if( DEBUG  )
						System.out.println("Relatrix.store stored :"+dmr);
				} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
					throw new RuntimeException(e);
				}
			}
		},storeXTransaction);
		try {
			SynchronizedFixedThreadPoolManager.waitForGroupToFinish(storeXTransaction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return identity;
	}
	/**
	 * Commit the outstanding transaction data in the transaction context.
	 * @param the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void commit(TransactionId xid) throws IOException, IllegalAccessException {
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
	public static synchronized void commit(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.commit(alias, xid);
	}
	/**
	 * Roll back all outstanding transactions on for each relationship in the transaction context.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void rollback(TransactionId xid) throws IOException, IllegalAccessException {
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
	public static synchronized void rollback(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.rollback(alias, xid);
	}
	/**
	 * Roll back all outstanding transactions on for each relationship in the transaction context to established checkpoint.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void rollbackToCheckpoint(TransactionId xid) throws IOException, IllegalAccessException {
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
	public static synchronized void rollbackToCheckpoint(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.rollbackToCheckpoint(alias, xid);
	}
	/**
	 * Take a check point of our current written relationships in the given transaction context. We can then
	 * issue a 'rollbackToCheckpoint' and remove further written data after this point.
	 * @param xid the transaction id.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void checkpoint(TransactionId xid) throws IOException, IllegalAccessException {
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
	public static synchronized void checkpoint(Alias alias, TransactionId xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVTransaction.checkpoint(alias, xid);
	}
	
	public static synchronized Object removekv(TransactionId xid, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException {
		return RelatrixKVTransaction.remove(xid,c);
	}
	public static synchronized Object removekv(Alias alias, TransactionId xid, Comparable<?> c) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
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
	public static synchronized void remove(TransactionId transactionId, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove prepping to remove:"+c);
		try {
			removeRecursive(transactionId, c);
			if(c instanceof DomainMapRange) {
				((DomainMapRange)c).setTransactionId(transactionId);
				DomainRangeMap drm = new DomainRangeMap((DomainMapRange) c);
				drm.setTransactionId(transactionId);
				MapDomainRange mdr = new MapDomainRange((DomainMapRange) c);
				mdr.setTransactionId(transactionId);
				MapRangeDomain mrd = new MapRangeDomain((DomainMapRange) c);
				mrd.setTransactionId(transactionId);
				RangeDomainMap rdm = new RangeDomainMap((DomainMapRange) c);
				rdm.setTransactionId(transactionId);
				RangeMapDomain rmd = new RangeMapDomain((DomainMapRange) c);
				rmd.setTransactionId(transactionId);
				
				DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(transactionId, drm);
				IndexResolver.getIndexInstanceTable().delete(transactionId, primaryKey);
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
			} else {
				IndexResolver.getIndexInstanceTable().deleteInstance(transactionId, c);
			}
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
	public static synchronized void remove(Alias alias, TransactionId transactionId, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove prepping to remove:"+c);
		try {
			removeRecursive(alias, transactionId, c);
			if(c instanceof DomainMapRange) {
				((DomainMapRange)c).setTransactionId(transactionId);
				((DomainMapRange)c).setAlias(alias);
				DomainRangeMap drm = new DomainRangeMap(alias,(DomainMapRange) c);
				drm.setTransactionId(transactionId);
				MapDomainRange mdr = new MapDomainRange(alias,(DomainMapRange) c);
				mdr.setTransactionId(transactionId);
				MapRangeDomain mrd = new MapRangeDomain(alias,(DomainMapRange) c);
				mrd.setTransactionId(transactionId);
				RangeDomainMap rdm = new RangeDomainMap(alias,(DomainMapRange) c);
				rdm.setTransactionId(transactionId);
				RangeMapDomain rmd = new RangeMapDomain(alias,(DomainMapRange) c);
				rmd.setTransactionId(transactionId);

				DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(alias, transactionId, c);
				IndexResolver.getIndexInstanceTable().delete(alias, transactionId, primaryKey);
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
			} else {
				IndexResolver.getIndexInstanceTable().deleteInstance(alias, transactionId, c);
			}
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove exiting remove for key:"+c);
	}

	/**
	 * Iterate through all possible relationships the given element may participate in, then recursively process those
	 * relationships to remove references to those.
	 * @param transactionId 
	 * @param c The Comparable key
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws DuplicateKeyException 
	 */
	private static synchronized void removeRecursive(TransactionId transactionId, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, DuplicateKeyException {
			Iterator<?> it = findSet(transactionId, c,"*","*");
			while(it.hasNext()) {
				Result o = (Result) it.next();
				if( DEBUG || DEBUGREMOVE)
					System.out.println("RelatrixTransaction.remove iterated perm 1 "+o+" of type "+o.get(0).getClass().getName());
				DomainMapRange dmr = (DomainMapRange)o.get(0);
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

				DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(transactionId, dmr);
				IndexResolver.getIndexInstanceTable().delete(transactionId, primaryKey);
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
				removeRecursive(transactionId, o.get(0)); 
			}
			it = findSet(transactionId, "*",c,"*");
			while(it.hasNext()) {
				Result o = (Result) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove iterated perm 2 "+o.get(0)+" of type "+o.get(0).getClass().getName());
				DomainMapRange dmr = (DomainMapRange)o.get(0);
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

				DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(transactionId, dmr); // remove primary key
				IndexResolver.getIndexInstanceTable().delete(transactionId, primaryKey); // remove DMR index table and instance table via primary key
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
				removeRecursive(transactionId, o.get(0)); 
			}
			it = findSet(transactionId, "*","*",c);
			while(it.hasNext()) {
				Result o = (Result) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove iterated perm 3 "+o.get(0)+" of type "+o.get(0).getClass().getName());
				DomainMapRange dmr = (DomainMapRange)o.get(0);
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

				DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(transactionId, dmr);
				IndexResolver.getIndexInstanceTable().delete(transactionId, primaryKey);
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
				removeRecursive(transactionId, o.get(0)); 
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
	private static synchronized void removeRecursive(Alias alias, TransactionId transactionId, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException, DuplicateKeyException {
		Iterator<?> it = findSet(alias, transactionId, c,"*","*");
		while(it.hasNext()) {
			Result o = (Result) it.next();
			if( DEBUG || DEBUGREMOVE)
				System.out.println("RelatrixTransaction.remove iterated perm 1 "+o.get(0)+" of type "+o.get(0).getClass().getName());
			DomainMapRange dmr = (DomainMapRange)o.get(0);
			dmr.setTransactionId(transactionId);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			drm.setTransactionId(transactionId);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			mdr.setTransactionId(transactionId);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			mrd.setTransactionId(transactionId);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			rdm.setTransactionId(transactionId);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);
			rmd.setTransactionId(transactionId);

			DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(alias, transactionId, dmr);
			IndexResolver.getIndexInstanceTable().delete(alias, transactionId, primaryKey);
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
			removeRecursive(alias, transactionId, o.get(0)); 
		}
		it = findSet(alias, transactionId, "*",c,"*");
		while(it.hasNext()) {
			Result o = (Result) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove iterated perm 2 "+o.get(0)+" of type "+o.get(0).getClass().getName());
			DomainMapRange dmr = (DomainMapRange)o.get(0);
			dmr.setTransactionId(transactionId);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			drm.setTransactionId(transactionId);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			mdr.setTransactionId(transactionId);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			mrd.setTransactionId(transactionId);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			rdm.setTransactionId(transactionId);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);
			rmd.setTransactionId(transactionId);

			DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(alias, transactionId, dmr);
			IndexResolver.getIndexInstanceTable().delete(alias, transactionId, primaryKey);
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
			removeRecursive(alias, transactionId, o.get(0)); 
		}
		it = findSet(alias, transactionId, "*","*",c);
		while(it.hasNext()) {
			Result o = (Result) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove iterated perm 3 "+o.get(0)+" of type "+o.get(0).getClass().getName());
			DomainMapRange dmr = (DomainMapRange)o.get(0);
			dmr.setTransactionId(transactionId);
			DomainRangeMap drm = new DomainRangeMap(alias,dmr);
			drm.setTransactionId(transactionId);
			MapDomainRange mdr = new MapDomainRange(alias,dmr);
			mdr.setTransactionId(transactionId);
			MapRangeDomain mrd = new MapRangeDomain(alias,dmr);
			mrd.setTransactionId(transactionId);
			RangeDomainMap rdm = new RangeDomainMap(alias,dmr);
			rdm.setTransactionId(transactionId);
			RangeMapDomain rmd = new RangeMapDomain(alias,dmr);
			rmd.setTransactionId(transactionId);

			DBKey primaryKey = (DBKey) RelatrixKVTransaction.remove(alias, transactionId, dmr);
			IndexResolver.getIndexInstanceTable().delete(alias, transactionId, primaryKey);
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
			removeRecursive(alias, transactionId, o.get(0)); 
		}
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
	public static synchronized void remove(TransactionId xid, Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, ClassNotFoundException {
		remove(xid, new DomainMapRange(null, xid, d, m, null));
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
	public static synchronized void remove(Alias alias, TransactionId xid, Comparable<?> d, Comparable<?> m) throws IOException, IllegalAccessException, NoSuchElementException, IllegalArgumentException, ClassNotFoundException, DuplicateKeyException {
		remove(alias, xid, d, m);
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned {@link Result} is always of dimension n="# of question marks" or a one element of a single object.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type DomainMapRange. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param transactionId the transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static synchronized Iterator<?> findSet(TransactionId transactionId, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = IteratorFactory.createFactoryTransaction(transactionId, darg, marg, rarg);
		return ifact.createIterator();
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned Result instance hierarchy is always of depth n="# of question marks" or a hierarchy of a single object.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type DomainMapRange. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' class hierarchy or {@link Result} returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param alias database alias
	 * @param transactionId transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias doesnt exist
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static synchronized Iterator<?> findSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		IteratorFactory ifact = IteratorFactory.createFactoryTransaction(transactionId, darg, marg, rarg);
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
	 * @param xid transaction id
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
	public static synchronized Stream<?> findStream(TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = IteratorFactory.createFactoryTransaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator());
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
	 * @param alias database alias
	 * @param xid transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param parallel Optional argument to invoke parallel stream
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalArgumentException the operator is invalid
	 * @throws ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias doesnt exist
	 * @return The Stream pipeline with the retrieved elements
	 */
	public static synchronized Stream<?> findStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		IteratorFactory ifact = IteratorFactory.createFactoryTransaction(xid, darg, marg, rarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators. Semantically,
	 * this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param xid transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static synchronized Iterator<?> findTailSet(TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return ifact.createIterator();
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators. Semantically,
	 * this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param alias database alias
	 * @param xid transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if alias doesnt exist
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 */
	public static synchronized Iterator<?> findTailSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return ifact.createIterator(alias);
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators. Semantically,
	 * this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param xid transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param parallel Optional true for parallel stream execution
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Result>
	 */
	public static synchronized Stream<?> findTailStream(TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects.
	 * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
	 * The parameters can be objects and/or operators. Semantically,
	 * this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param parallel Optional true for parallel stream execution
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchELementException if the alias isnt found
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Result>
	 */
	public static synchronized Stream<?> findTailStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createTailsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	
	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param xid transaction id
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return the headset iterator
	 */
	public static synchronized Iterator<?> findHeadSet(TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(xid, darg, marg, rarg, endarg);
		return ifact.createIterator();
	}
	
	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param alias datbase alias
	 * @param xid transaction id
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchelementExcpetion if the alias isnt found
	 * @return The iterator for headset
	 */
	public static synchronized Iterator<?> findHeadSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return ifact.createIterator(alias);
	}
	
	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param xid transaction id
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param parallel Optional true to execute stream in parallel
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return head stream stream
	 */
	public static synchronized Stream<?> findHeadStream(TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
	}
	
	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
	 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
	 * @param alias database alias
	 * @param xid transaction id
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param parallel Optional true to execute stream in parallel
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchelementException if alias doesnt exist
	 * @return the head stream
	 */
	public static synchronized Stream<?> findHeadStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg, Object ... endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactoryTransaction(xid, darg, marg, rarg, endarg);
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
	 * @param xid transaction id
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return subset iterator
	 */
	public static synchronized Iterator<?> findSubSet(TransactionId xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
			IteratorFactory ifact = IteratorFactory.createSubsetFactoryTransaction(xid, darg, marg, rarg, endarg);
			return ifact.createIterator();
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
	 * @param xid transaction id
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return subset iterator
	 */
	public static synchronized Iterator<?> findSubSet(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
			IteratorFactory ifact = IteratorFactory.createSubsetFactoryTransaction(xid, darg, marg, rarg, endarg);
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
	 * @param xid transaction id
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param parallel true to execute stream in parallel, false for sequential
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The Stream from which the data may be retrieved. Follows Stream interface, return Stream<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The stream for subset in range specified
	 */
	public static synchronized Stream<?> findSubStream(TransactionId xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createSubsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator());
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
	 * @param alias the database alias
	 * @param xid transaction id
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param parallel true to execute stream in parallel, false for sequential
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The Stream from which the data may be retrieved. Follows Stream interface, return Stream<Result>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NosuchElementException If the alias doesnt exist
	 * @return The stream for subset in range specified
	 */
	public static synchronized Stream<?> findSubStream(Alias alias, TransactionId xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		IteratorFactory ifact = IteratorFactory.createSubsetFactoryTransaction(xid, darg, marg, rarg, endarg);
		return new RelatrixStream(ifact.createIterator(alias));
	}
	
	/**
	 * this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object firstKey(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.firstKey(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the first DomainMapRange
	 * instance having the lowest valued key.
	 * @param xid the transaction id
	 * @param clazz the class of the the target instances
	 * @return the DomainMapRange morphism first key.
	 * @throws IOException
	 */
	public static synchronized Object firstKey(TransactionId xid, Class clazz) throws IOException
	{
		try {
			return RelatrixKVTransaction.firstKey(xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws NoSuchElementException if the alias doesnt exist
	 * @throws IOException
	 */
	public static synchronized Object firstKey(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.firstKey(alias, xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
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
	public static synchronized Object firstKey(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.firstKey(alias, xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object first(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.firstKey(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws NoSuchElementException if the alias doesnt exist
	 * @throws IOException
	 */
	public static synchronized Object first(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.firstKey(alias, xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * This method returns the first DBKey of DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @param xid transaction id
	 * @return the DomainMapRange morphism having the lowest valued key value.	
	 * @throws IOException																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						* @throws IOException
	 */
	public static synchronized Object firstValue(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.firstValue(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the first DBKey DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @param alias the database alias
	 * @param xid transaction id
	 * @return the DomainMapRange morphism having the lowest valued key.	
	 * @throws IOException																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																					* @throws IOException
	 */
	public static synchronized Object firstValue(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.firstValue(alias, xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the first DomainMapRange
	 * instance having the lowest valued key.
	 * @param xid the transaction id
	 * @param clazz the class of the the target instances
	 * @return the DomainMapRange morphism first key.
	 * @throws IOException
	 */
	public static synchronized Object first(TransactionId xid, Class clazz) throws IOException
	{
		try {
			return RelatrixKVTransaction.firstKey(xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
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
	public static synchronized Object first(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.firstKey(alias, xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
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
	public static synchronized Object firstValue(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object firstValue(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKVTransaction.firstValue(xid, clazz);
	}
	
	/**
	 * This method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @param xid the transaction id
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 */
	public static synchronized Object last(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastKey(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	public static synchronized Object last(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastKey(alias, xid, DomainMapRange.class);
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
	public static synchronized Object last(TransactionId xid, Class clazz) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastKey(xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
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
	public static synchronized Object last(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastKey(alias, xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * This method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @param xid the transaction id
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 */
	public static synchronized Object lastKey(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastKey(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last DomainMapRange
	 * instance having the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @return the DomainMapRange morphism having the highest key value.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	public static synchronized Object lastKey(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastKey(alias, xid, DomainMapRange.class);
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
	public static synchronized Object lastKey(TransactionId xid, Class clazz) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastKey(xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
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
	public static synchronized Object lastKey(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastKey(alias, xid, clazz);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last DBKey of DomainMapRange
	 * instance having the value of the highest valued key.
	 * @param xid the transaction id
	 * @return the DBKey of the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 */
	public static synchronized Object lastValue(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.lastValue(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * This method returns the last DBKey of DomainMapRange
	 * instance having the value of the highest valued key.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @return the DBKey of the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	public static synchronized Object lastValue(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastValue(alias, xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	/**
	 * This method returns the last value for the given class
	 * instance having the value of the highest valued key.
	 * @return the DomainMapRange morphism having the value of highest key.
	 * @throws IOException
	 */
	public static synchronized Object lastValue(TransactionId xid, Class clazz) throws IOException
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
	public static synchronized Object lastValue(Alias alias, TransactionId xid, Class clazz) throws IOException, NoSuchElementException
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
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 */
	public static synchronized long size(TransactionId xid) throws IOException
	{
		try {
			return RelatrixKVTransaction.size(xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationships in the scope of this transaction.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws NoSuchElementException if the alias is not found
	 * @return the number of DomainMapRange relationships.
	 * @throws IOException
	 */
	public static synchronized long size(Alias alias, TransactionId xid) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.size(alias, xid, DomainMapRange.class);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	public static synchronized long size(TransactionId xid, Class c) throws IOException
	{
		try {
			return RelatrixKVTransaction.size(xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	public static synchronized long size(Alias alias, TransactionId xid, Class c) throws IOException, NoSuchElementException
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
	public static synchronized boolean contains(TransactionId xid, Comparable obj) throws IOException
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
	public static synchronized boolean contains(Alias alias, TransactionId xid, Comparable obj) throws IOException, NoSuchElementException
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
	public static synchronized void store(TransactionId xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
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
	public static synchronized Object get(TransactionId xid, Comparable key) throws IOException, IllegalAccessException
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
	public static synchronized Object get(Alias alias, TransactionId xid, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object getByIndex(TransactionId xid, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return IndexResolver.getIndexInstanceTable().get(xid, (DBKey) key);
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
	public static synchronized Object getByIndex(Alias alias, TransactionId xid, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException, NoSuchElementException
	{
		return IndexResolver.getIndexInstanceTable().get(xid, (DBKey) key);
	}
	/**
	 * Return the keyset for the given class
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static synchronized Iterator<?> keySet(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKVTransaction.keySet(xid, clazz);
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
	public static synchronized Iterator<?> keySet(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVTransaction.keySet(alias, xid, clazz);
	}
	
	public static synchronized Iterator<?> entrySet(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
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
	public static synchronized Iterator<?> entrySet(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Stream<?> entrySetStream(TransactionId xid, Class clazz) throws IOException, IllegalAccessException
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
	public static synchronized Stream<?> entrySetStream(Alias alias, TransactionId xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return new RelatrixStream(new RelatrixEntrysetIteratorTransaction(alias, xid, clazz));
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
	
	public static void main(String[] args) throws Exception {
		setTablespace(args[0]);
		RelatrixTransaction.findStream(new TransactionId(args[1]), "*", "*", "*").forEach((s) -> {
			System.out.println(s.toString());
		});
	}
 
}
