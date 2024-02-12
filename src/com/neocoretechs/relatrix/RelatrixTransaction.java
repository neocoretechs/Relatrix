package com.neocoretechs.relatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.rocksack.session.TransactionalMap;
import com.neocoretechs.rocksack.session.VolumeManager;

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
* if we declare findHeadSet("*","?","*") we get back a Comparable[] of one element, for findSet("?",object,"?") we
* would get back a Comparable[2] array, with each element of the relationship returned.<br/>
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
    private static final int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED;
	private static final String databaseCatalogProperty = "Relatrix.Catalog";
	private static String databaseCatalog = "/etc/db/";
	private static ConcurrentHashMap<String, UUID> pathToIndex = new ConcurrentHashMap<String,UUID>();
	private static ConcurrentHashMap<UUID, String> indexToPath = new ConcurrentHashMap<UUID,String>();
	
	static {
		if(System.getProperty(databaseCatalogProperty) != null)
			databaseCatalog = System.getProperty(databaseCatalogProperty);
		try {
			setAlias(databaseCatalogProperty, databaseCatalog);
			readDatabaseCatalog();
		} catch (IOException | IllegalAccessException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	private static Class[] indexClasses = new Class[6];//{DomainMapRange.class,DomainRangeMap.class,MapDomainRange.class,
												  //MapRangeDomain.class,RangeDomainMap.class,RangeMapDomain.class};

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
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set tablespace directory for fileset "+path+" to allocate persistent storage.");
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
	public static void setAlias(String alias, String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set alias for tablespace directory using fileset "+path+" to allocate persistent storage.");
		DatabaseManager.setTableSpaceDir(alias, path);
	}
	
	/**
	 * Verify that we are specifying a directory, then set an alias as top level file structure and database name
	 * @param alias
	 * @throws NoSuchElementException if the alias was not ofund
	 */
	public static void removeAlias(String alias) throws NoSuchElementException {
		DatabaseManager.removeAlias(alias);
	}
	
	/**
	 * Will return null if alias does not exist
	 * @param alias
	 * @return
	 */
	public static String getAlias(String alias) {
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
	 * Get a new transaction ID
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static String getTransactionId() throws IllegalAccessException, IOException {
		String xid =  DatabaseManager.getTransactionId();
		return xid;
	}
	
	/**
	 * @param xid
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void endTransaction(String xid) throws IllegalAccessException, IOException, ClassNotFoundException {
		DatabaseManager.endTransaction(xid);
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
	public static synchronized DomainMapRangeTransaction store(String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRangeTransaction identity = new DomainMapRangeTransaction(xid,d,m,r); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted.
		identity.setPrimaryKeyCheck(true);
		if(RelatrixKVTransaction.contains(xid, identity)) {
			identity.setPrimaryKeyCheck(false);
			rollback(xid);
			throw new DuplicateKeyException("dmr:"+identity);
		}
		identity.setPrimaryKeyCheck(false);
		IndexResolver.getIndexInstanceTable().put(xid,identity);
		DomainRangeMapTransaction drm = new DomainRangeMapTransaction(identity);
		indexClasses[1] = drm.getClass();
		IndexResolver.getIndexInstanceTable().put(xid,drm);
		MapDomainRangeTransaction mdr = new MapDomainRangeTransaction(identity);
		indexClasses[2] = mdr.getClass();
		IndexResolver.getIndexInstanceTable().put(xid,mdr);
		MapRangeDomainTransaction mrd = new MapRangeDomainTransaction(identity);
		indexClasses[3] = mrd.getClass();
		IndexResolver.getIndexInstanceTable().put(xid,mrd);
		RangeDomainMapTransaction rdm = new RangeDomainMapTransaction(identity);
		indexClasses[4] = rdm.getClass();
		IndexResolver.getIndexInstanceTable().put(xid,rdm);
		RangeMapDomainTransaction rmd = new RangeMapDomainTransaction(identity);
		indexClasses[5] = rmd.getClass();
		IndexResolver.getIndexInstanceTable().put(xid,rmd);
		// this gives our DMR a key, and places it in the IndexInstanceTable pervue for commit
		indexClasses[0] = null; // remove dmr from our commit lineup
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
	public static synchronized DomainMapRangeTransaction store(String alias, String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		DomainMapRangeTransaction identity = new DomainMapRangeTransaction(alias,xid,d,m,r); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted.
		identity.setPrimaryKeyCheck(true);
		if(RelatrixKVTransaction.contains(alias, xid, identity)) {
			identity.setPrimaryKeyCheck(false);
			rollback(xid);
			throw new DuplicateKeyException("dmr:"+identity);
		}
		identity.setPrimaryKeyCheck(false);
		IndexResolver.getIndexInstanceTable().putAlias(alias,xid,identity);
		DomainRangeMapTransaction drm = new DomainRangeMapTransaction(alias,identity);
		indexClasses[1] = drm.getClass();
		IndexResolver.getIndexInstanceTable().putAlias(alias,xid,drm);
		MapDomainRangeTransaction mdr = new MapDomainRangeTransaction(alias,identity);
		indexClasses[2] = mdr.getClass();
		IndexResolver.getIndexInstanceTable().putAlias(alias,xid,mdr);
		MapRangeDomainTransaction mrd = new MapRangeDomainTransaction(alias,identity);
		indexClasses[3] = mrd.getClass();
		IndexResolver.getIndexInstanceTable().putAlias(alias,xid,mrd);
		RangeDomainMapTransaction rdm = new RangeDomainMapTransaction(alias,identity);
		indexClasses[4] = rdm.getClass();
		IndexResolver.getIndexInstanceTable().putAlias(alias,xid,rdm);
		RangeMapDomainTransaction rmd = new RangeMapDomainTransaction(alias,identity);
		indexClasses[5] = rmd.getClass();
		IndexResolver.getIndexInstanceTable().putAlias(alias,xid,rmd);
		// this gives our DMR a key, and places it in the IndexInstanceTable pervue for commit
		indexClasses[0] = null; // remove dmr from our commit lineup
		return identity;
	}
	/**
	 * Commit the outstanding transaction data in the transaction context.
	 * @param the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void commit(String xid) throws IOException, IllegalAccessException {
		// first commit components of relationships
		IndexResolver.getIndexInstanceTable().commit(xid);
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
	public static synchronized void commit(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		// first commit components of relationships
		IndexResolver.getIndexInstanceTable().commit(xid);
		RelatrixKVTransaction.commit(alias, xid);
	}
	/**
	 * Roll back all outstanding transactions on for each relationship in the transaction context.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void rollback(String xid) throws IOException, IllegalAccessException {
		// first roll back components
		IndexResolver.getIndexInstanceTable().rollback(xid);
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
	public static synchronized void rollback(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		// first roll back components
		IndexResolver.getIndexInstanceTable().rollback(xid);
		RelatrixKVTransaction.rollback(alias, xid);
	}
	/**
	 * Roll back all outstanding transactions on for each relationship in the transaction context to established checkpoint.
	 * @param xid the transaction id
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void rollbackToCheckpoint(String xid) throws IOException, IllegalAccessException {
		// first roll back components
		IndexResolver.getIndexInstanceTable().rollbackToCheckpoint(xid);
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
	public static synchronized void rollbackToCheckpoint(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		// first roll back components
		IndexResolver.getIndexInstanceTable().rollbackToCheckpoint(xid);
		RelatrixKVTransaction.rollbackToCheckpoint(alias, xid);
	}
	/**
	 * Take a check point of our current written relationships in the given transaction context. We can then
	 * issue a 'rollbackToCheckpoint' and remove further written data after this point.
	 * @param xid the transaction id.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void checkpoint(String xid) throws IOException, IllegalAccessException {
		IndexResolver.getIndexInstanceTable().checkpoint(xid);
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
	public static synchronized void checkpoint(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		IndexResolver.getIndexInstanceTable().checkpoint(xid);
		RelatrixKVTransaction.checkpoint(alias, xid);
	}
	/**
	 * Delete all relationships that this object participates in for the current transaction context.
	 * @param xid the transaction id.
	 * @param c The Comparable key
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static synchronized void remove(String xid, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove Id:"+xid+" prepping to remove:"+c);
		removeRecursive(xid, c);
		try {
			DBKey dbKey = IndexResolver.getIndexInstanceTable().getByInstance(xid,c);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove Id:"+xid+" prepping to remove DBKey:"+dbKey);
			if(dbKey != null) {
				// Should delete instance and DbKey
				IndexResolver.getIndexInstanceTable().delete(xid,dbKey);
			} else {
				// failsafe delete, if we dont find the key for whatever reason, proceed to remove the instance directly if possible
				RelatrixKVTransaction.remove(xid, c);
			}
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove Id:"+xid+" exiting remove for key:"+c);
	}
	/**
	 * Iterate through all possible relationships the given element may participates in for this transaction context, then recursively process those
	 * relationships to remove references to those.
	 * @param xid the transaction id
	 * @param c The Comparable key
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	private static synchronized void removeRecursive(String xid, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		ArrayList<Morphism> m = new ArrayList<Morphism>();
		try {
			Iterator<?> it = findSet(xid, c,"*","*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE)
					System.out.println("RelatrixTransaction.remove iterated perm 1 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // We can get this exception if the class types differ in domain
		try {
			Iterator<?> it = findSet(xid, "*",c,"*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove iterated perm 2 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // we can get this exception if map class types differ
		try {
			Iterator<?> it = findSet(xid, "*","*",c);
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove iterated perm 3 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) { 
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace(); 
		} // we can get this exception if range class types differ
		// Process our array of candidates
		for(Morphism mo : m) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("RelatrixTransaction.remove Id: "+xid+" removing:"+mo);
			remove(xid, mo.getDomain(), mo.getMap(), mo.getRange());
			// if this morphism participates in any relationship. remove that relationship recursively
			removeRecursive(xid, mo);
		}
	}
	
	/**
	 * Delete all relationships that this object participates in for this database in this transaction context.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param c the Comparable key
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchElementException if the alias doesnt exist
	 */
	public static synchronized void remove(String alias, String xid, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove Id:"+xid+" prepping to remove:"+c);
		removeRecursive(alias, xid, c);
		try {
			DBKey dbKey = IndexResolver.getIndexInstanceTable().getByInstance(xid, c);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove Id:"+xid+" prepping to remove DBKey:"+dbKey);
			if(dbKey != null) {
				// Should delete instance and DbKey
				IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			} else {
				// failsafe delete, if we dont find the key for whatever reason, proceed to remove the instance directly if possible
				RelatrixKVTransaction.remove(alias, xid, c);
			}
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove Id:"+xid+" exiting remove for key:"+c);
	}
	
	/**
	 * Iterate through all possible relationships the given element may participate in, then recursively process those
	 * relationships to remove references to those.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @param c the Comparable key
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException If the alias doesnt exist
	 */
	private static synchronized void removeRecursive(String alias, String xid, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		ArrayList<Morphism> m = new ArrayList<Morphism>();
		try {
			Iterator<?> it = findSet(alias, xid, c, "*", "*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE)
					System.out.println("RelatrixTransaction.remove iterated perm 1 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // We can get this exception if the class types differ in domain
		try {
			Iterator<?> it = findSet(alias, xid, "*", c, "*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove iterated perm 2 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // we can get this exception if map class types differ
		try {
			Iterator<?> it = findSet(alias, xid, "*","*",c);
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove iterated perm 3 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) { 
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace(); 
		} // we can get this exception if range class types differ
		// Process our array of candidates
		for(Morphism mo : m) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("RelatrixTransaction.remove Id: "+xid+" removing:"+mo);
			remove(alias, xid, mo.getDomain(), mo.getMap(), mo.getRange());
			// if this morphism participates in any relationship. remove that relationship recursively
			removeRecursive(alias, xid, mo);
		}
	}
	
	/**
	 * Delete specific relationship and all relationships that it participates in for this transaction in the default tablespace. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param xid the transaction id
	 * @param d the domain of the relationship as Comparable key
	 * @param m the map of the relationship as Comparable key
	 * @param r the range of the relationship as Comparable key
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void remove(String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException {
		DomainMapRangeTransaction dmr = new DomainMapRangeTransaction(xid,d,m,r);
		indexClasses[0] = dmr.getClass();
		DomainRangeMapTransaction drm = new DomainRangeMapTransaction(xid,dmr);
		indexClasses[1] = drm.getClass();
		MapDomainRangeTransaction mdr = new MapDomainRangeTransaction(xid,d,m,r);
		indexClasses[2] = mdr.getClass();
		MapRangeDomainTransaction mrd = new MapRangeDomainTransaction(xid,d,m,r);
		indexClasses[3] = mrd.getClass();
		RangeDomainMapTransaction rdm = new RangeDomainMapTransaction(xid,d,m,r);
		indexClasses[4] = rdm.getClass();
		RangeMapDomainTransaction rmd = new RangeMapDomainTransaction(xid,d,m,r);
		indexClasses[5] = rmd.getClass();

		try {
			Object o = RelatrixKVTransaction.get(xid, dmr);
			if(o == null) {
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" could not find relationship dmr:"+dmr);
				return;
			}
		//KeyValue kv = (KeyValue)o;
		//DBKey dbKey = (DBKey) kv.getmValue();
			DBKey dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(xid, drm);
			if(o == null)
				throw new IOException(drm+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(xid, mdr);
			if(o == null)
				throw new IOException(mdr+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(xid, mrd);
			if(o == null)
				throw new IOException(mrd+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(xid, rdm);
			if(o == null)
				throw new IOException(rdm+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(xid, rmd);
			if(o == null)
				throw new IOException(rmd+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
		} catch (ClassNotFoundException | DuplicateKeyException e) {
			throw new IOException(e);
		}

		try {
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove Id:"+xid+" removing dmr:"+dmr);
				RelatrixKVTransaction.remove(xid, dmr);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+drm);
				RelatrixKVTransaction.remove(xid, drm);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+mdr);
				RelatrixKVTransaction.remove(xid, mdr);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+mrd);
				RelatrixKVTransaction.remove(xid, mrd);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+rdm);
				RelatrixKVTransaction.remove(xid, rdm);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove removing Id:"+xid+" "+rmd);
				RelatrixKVTransaction.remove(xid, rmd);
		} catch (IllegalArgumentException | ClassNotFoundException e) {
			throw new IOException(e);
		}
	
	}
	
	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param alias database alias
	 * @param xid transaction id
	 * @param d
	 * @param m
	 * @param r
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws NoSuchElementException if alias isnt found
	 */
	public static synchronized void remove(String alias, String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException, NoSuchElementException {
		MorphismTransaction dmr = new DomainMapRangeTransaction(alias,xid,d,m,r);
		indexClasses[0] = dmr.getClass();
		DomainRangeMapTransaction drm = new DomainRangeMapTransaction(alias,xid,d,m,r);
		indexClasses[1] = drm.getClass();
		MapDomainRangeTransaction mdr = new MapDomainRangeTransaction(alias,xid,d,m,r);
		indexClasses[2] = mdr.getClass();
		MapRangeDomainTransaction mrd = new MapRangeDomainTransaction(alias,xid,d,m,r);
		indexClasses[3] = mrd.getClass();
		RangeDomainMapTransaction rdm = new RangeDomainMapTransaction(alias,xid,d,m,r);
		indexClasses[4] = rdm.getClass();
		RangeMapDomainTransaction rmd = new RangeMapDomainTransaction(alias,xid,d,m,r);
		indexClasses[5] = rmd.getClass();

		try {
			Object o = RelatrixKVTransaction.get(alias, xid, dmr);
			if(o == null) {
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" could not find relationship dmr:"+dmr);
				return;
			}
		//KeyValue kv = (KeyValue)o;
		//DBKey dbKey = (DBKey) kv.getmValue();
			DBKey dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(alias, xid, drm);
			if(o == null)
				throw new IOException(drm+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(alias, xid, mdr);
			if(o == null)
				throw new IOException(mdr+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(alias, xid, mrd);
			if(o == null)
				throw new IOException(mrd+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(alias, xid, rdm);
			if(o == null)
				throw new IOException(rdm+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
			o = RelatrixKVTransaction.get(alias, xid, rmd);
			if(o == null)
				throw new IOException(rmd+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(xid, dbKey);
		} catch (ClassNotFoundException | DuplicateKeyException e) {
			throw new IOException(e);
		}

		try {
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove Id:"+xid+" removing dmr:"+dmr);
				RelatrixKVTransaction.remove(alias, xid, dmr);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+drm);
				RelatrixKVTransaction.remove(alias, xid, drm);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+mdr);
				RelatrixKVTransaction.remove(alias, xid, mdr);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+mrd);
				RelatrixKVTransaction.remove(alias, xid, mrd);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("RelatrixTransaction.remove Id:"+xid+" removing "+rdm);
				RelatrixKVTransaction.remove(alias, xid, rdm);
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove removing Id:"+xid+" "+rmd);
				RelatrixKVTransaction.remove(alias, xid, rmd);
		} catch (IllegalArgumentException | ClassNotFoundException e) {
			throw new IOException(e);
		}
	
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned Comparable[] array is always of dimension n="# of question marks" or a one element array of a single object.
	 * In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	 * domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	 * relationships of the 3 objects, of the type DomainMapRange. 
	 * The returned elements(s) constitute identities in the sense of these morphisms satisfying
	 * the requirement to be 'categorical'. In general, all '3 element' arrays returned by the operators are
	 * the mathematical identity, or constitute the unique key in database terms.
	 * @param xid the transaction id
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static synchronized Iterator<?> findSet(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
		return ifact.createIterator();
	}
	
	/**
	 * Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	 * matching the given set of operators and/or objects. Essentially this is the default permutation which
	 * retrieves the equivalent of a tailSet and the parameters can be objects and/or ?,* operators. Semantically,
	 * the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	 * The returned Comparable[] array is always of dimension n="# of question marks" or a one element array of a single object.
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
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @throws NoSuchElementException if the alias doesnt exist
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static synchronized Iterator<?> findSet(String alias, String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
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
	public static synchronized Stream<?> findStream(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	public static synchronized Stream<?> findStream(String alias, String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(alias), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static synchronized Iterator<?> findTailSet(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailSet must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static synchronized Iterator<?> findTailSet(String alias, String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailSet must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Comparable[]>
	 */
	public static synchronized Stream<?> findTailStream(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailStream must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Comparable[]>
	 */
	public static synchronized Stream<?> findTailStream(String alias, String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailStream must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(alias), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return the headset iterator
	 */
	public static synchronized Iterator<?> findHeadSet(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(xid, darg, marg, rarg);
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchelementExcpetion if the alias isnt found
	 * @return The iterator for headset
	 */
	public static synchronized Iterator<?> findHeadSet(String alias, String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(xid, darg, marg, rarg);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return head stream stream
	 */
	public static synchronized Stream<?> findHeadStream(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(xid, darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
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
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchelementException if alias doesnt exist
	 * @return the head stream
	 */
	public static synchronized Stream<?> findHeadStream(String alias, String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(xid, darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(alias), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return subset iterator
	 */
	public static synchronized Iterator<?> findSubSet(String xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findSubSet must contain an object reference");
			int numberObjects = 0;
			if( !darg.equals(OPERATOR_WILDCARD) && !darg.equals(OPERATOR_TUPLE) ) ++numberObjects;
			if( !marg.equals(OPERATOR_WILDCARD) && !marg.equals(OPERATOR_TUPLE) ) ++numberObjects;
			if( !rarg.equals(OPERATOR_WILDCARD) && !rarg.equals(OPERATOR_TUPLE) ) ++numberObjects;
			if( numberObjects != endarg.length)
				throw new IllegalArgumentException("The number of arguments to the ending range of findSubSet must match the number of objects declared for the starting range");
			IteratorFactory ifact = IteratorFactory.createSubsetFactory(xid, darg, marg, rarg, endarg);
			return ifact.createIterator();
	}
	
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return subset iterator
	 */
	public static synchronized Iterator<?> findSubSet(String alias, String xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findSubSet must contain an object reference");
			int numberObjects = 0;
			if( !darg.equals(OPERATOR_WILDCARD) && !darg.equals(OPERATOR_TUPLE) ) ++numberObjects;
			if( !marg.equals(OPERATOR_WILDCARD) && !marg.equals(OPERATOR_TUPLE) ) ++numberObjects;
			if( !rarg.equals(OPERATOR_WILDCARD) && !rarg.equals(OPERATOR_TUPLE) ) ++numberObjects;
			if( numberObjects != endarg.length)
				throw new IllegalArgumentException("The number of arguments to the ending range of findSubSet must match the number of objects declared for the starting range");
			IteratorFactory ifact = IteratorFactory.createSubsetFactory(xid, darg, marg, rarg, endarg);
			return ifact.createIterator(alias);
	}
	
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
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
	 * @return The Stream from which the data may be retrieved. Follows Stream interface, return Stream<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @return The stream for subset in range specified
	 */
	public static synchronized Stream<?> findSubStream(String xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findSubStream must contain an object reference");
		int numberObjects = 0;
		if( !darg.equals(OPERATOR_WILDCARD) && !darg.equals(OPERATOR_TUPLE) ) ++numberObjects;
		if( !marg.equals(OPERATOR_WILDCARD) && !marg.equals(OPERATOR_TUPLE) ) ++numberObjects;
		if( !rarg.equals(OPERATOR_WILDCARD) && !rarg.equals(OPERATOR_TUPLE) ) ++numberObjects;
		if( numberObjects != endarg.length)
			throw new IllegalArgumentException("The number of arguments to the ending range of findSubStream must match the number of objects declared for the starting range");
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(xid, darg, marg, rarg, endarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
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
	 * @return The Stream from which the data may be retrieved. Follows Stream interface, return Stream<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NosuchElementException If the alias doesnt exist
	 * @return The stream for subset in range specified
	 */
	public static synchronized Stream<?> findSubStream(String alias, String xid, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findSubStream must contain an object reference");
		int numberObjects = 0;
		if( !darg.equals(OPERATOR_WILDCARD) && !darg.equals(OPERATOR_TUPLE) ) ++numberObjects;
		if( !marg.equals(OPERATOR_WILDCARD) && !marg.equals(OPERATOR_TUPLE) ) ++numberObjects;
		if( !rarg.equals(OPERATOR_WILDCARD) && !rarg.equals(OPERATOR_TUPLE) ) ++numberObjects;
		if( numberObjects != endarg.length)
			throw new IllegalArgumentException("The number of arguments to the ending range of findSubStream must match the number of objects declared for the starting range");
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(xid, darg, marg, rarg, endarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(alias), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	
	/**
	 * this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object first(String xid) throws IOException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.firstKey(xid, indexClasses[0]);
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
	public static synchronized Object first(String alias, String xid) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.firstKey(alias, xid, indexClasses[0]);
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
	public static synchronized Object firstValue(String xid) throws IOException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.firstValue(xid, indexClasses[0]);
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
	public static synchronized Object firstValue(String alias, String xid) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.firstValue(alias, xid, indexClasses[0]);
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
	public static synchronized Object first(String xid, Class clazz) throws IOException
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
	public static synchronized Object first(String alias, String xid, Class clazz) throws IOException, NoSuchElementException
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
	public static synchronized Object firstValue(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object firstValue(String xid, Class clazz) throws IOException, IllegalAccessException
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
	public static synchronized Object last(String xid) throws IOException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.lastKey(xid, indexClasses[0]);
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
	public static synchronized Object last(String alias, String xid) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.lastKey(alias, xid, indexClasses[0]);
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
	public static synchronized Object lastValue(String xid) throws IOException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.lastValue(xid, indexClasses[0]);
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
	public static synchronized Object lastValue(String alias, String xid) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKVTransaction.lastValue(alias, xid, indexClasses[0]);
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
	public static synchronized Object last(String xid, Class clazz) throws IOException
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
	public static synchronized Object last(String alias, String xid, Class clazz) throws IOException, NoSuchElementException
	{
		try {
			return RelatrixKVTransaction.lastKey(alias, xid, clazz);
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
	public static synchronized Object lastValue(String xid, Class clazz) throws IOException
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
	public static synchronized Object lastValue(String alias, String xid, Class clazz) throws IOException, NoSuchElementException
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
	public static synchronized long size(String xid) throws IOException
	{
		if( indexClasses[0] == null ) {
			return -1;
		}
		try {
			return RelatrixKVTransaction.size(xid, indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	/**
	 * This method returns the number of relationship
	 * instances in the scope of this transaction.
	 * @param alias the database alias
	 * @param xid the transaction id
	 * @throws NoSuchElementException if the alias is not found
	 * @return the number of DomainMapRange relationships.
	 * @throws IOException
	 */
	public static synchronized long size(String alias, String xid) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return -1;
		}
		try {
			return RelatrixKVTransaction.size(alias, xid, indexClasses[0]);
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
	public static synchronized boolean contains(String xid, Comparable obj) throws IOException
	{
		if( indexClasses[0] == null ) {
			return false;
		}
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
	public static synchronized boolean contains(String alias, String xid, Comparable obj) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return false;
		}
		try {
			return RelatrixKVTransaction.contains(alias, xid, obj);
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
	public static synchronized UUID getNewKey() {
		UUID nkey = UUID.randomUUID();
		if(DEBUG)
			System.out.printf("Returning NewKey=%s%n", nkey.toString());
		return nkey;
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
	public static synchronized void store(String xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
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
	public static synchronized Object get(String xid, Comparable key) throws IOException, IllegalAccessException
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
	public static synchronized Object get(String alias, String xid, Comparable key) throws IOException, IllegalAccessException, NoSuchElementException
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
	public static synchronized Object getByIndex(String xid, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return IndexResolver.getIndexInstanceTable().getByIndex(xid, (DBKey) key);
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
	public static synchronized Object getByIndex(String alias, String xid, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException, NoSuchElementException
	{
		return IndexResolver.getIndexInstanceTable().getByIndex(xid, (DBKey) key);
	}
	/**
	 * Return the keyset for the given class
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return the iterator for the keyset
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static synchronized Iterator<?> keySet(String xid, Class clazz) throws IOException, IllegalAccessException
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
	public static synchronized Iterator<?> keySet(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVTransaction.keySet(alias, xid, clazz);
	}
	
	/**
	 * Return the entry set for the given class type
	 * @param xid the transaction id
	 * @param clazz the class to retrieve
	 * @return Stream for entry set
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static synchronized Stream<?> entrySetStream(String xid, Class clazz) throws IOException, IllegalAccessException
	{
		return RelatrixKVTransaction.entrySetStream(xid, clazz);
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
	public static synchronized Stream<?> entrySetStream(String alias, String xid, Class clazz) throws IOException, IllegalAccessException, NoSuchElementException
	{
		return RelatrixKVTransaction.entrySetStream(alias, xid, clazz);
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
		Iterator<?> it = RelatrixKV.entrySet(databaseCatalogProperty, UUID.class);
		while(it.hasNext()) {
			Entry e = (Entry) it.next();
			indexToPath.put((UUID)e.getKey(), (String)e.getValue());
			pathToIndex.put((String)e.getValue(), (UUID)e.getKey());
		}
		RelatrixKV.close(databaseCatalogProperty, UUID.class);
	}

	static void writeDatabaseCatalog() throws IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		Iterator<Map.Entry<UUID, String>> it = indexToPath.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<UUID, String> entry = it.next();
			RelatrixKV.store(databaseCatalogProperty, entry.getKey(), entry.getValue());
		}
		RelatrixKV.close(databaseCatalogProperty, UUID.class);
	}
	/**
	 * Get the UUID for the given tablespace path. If the index does not exist, it will be created based on param
	 * @param path
	 * @param create
	 * @return the UUID of path
	 */
	public static UUID getByPath(String path, boolean create) {
		if(DEBUG)
			System.out.println("IndexManager.get attempt for path:"+path);
		UUID v = pathToIndex.get(path);
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
			if(DEBUG)
				System.out.println("IndexManager.get creating new index for path:"+path);
			v = UUID.randomUUID();
			pathToIndex.put(path, v);
			indexToPath.put(v, path);
			try {
				RelatrixKV.store(databaseCatalogProperty, v, path);
				RelatrixKV.close(databaseCatalogProperty, UUID.class);
			} catch (IllegalAccessException | NoSuchElementException | IOException | DuplicateKeyException e) {
				e.printStackTrace();
			}
		}
		return v;
	}
	/**
	 * Get the path for the given index. If the path does not exist, it will NOT be created.
	 * @param index
	 * @return path from indexToPath
	 */
	public static String getDatabasePath(UUID index) {
		if(DEBUG)
			System.out.println("IndexManager.get attempt for UUID:"+index);
		String v = indexToPath.get(index);
		if(v == null) {
			if(DEBUG)
				System.out.println("IndexManager.get did not find index:"+index);
		}
		return v;
	}
	/**
	 * Get the tablespace path for the given alias
	 * @param alias
	 * @return The path for this alias or null if none
	 */
	public static String getAliasToPath(String alias) {
		if(DEBUG)
			System.out.println("IndexManager.getAliasToPath attempt for alias:"+alias+" will return:"+DatabaseManager.getAliasToPath(alias));
		return DatabaseManager.getAliasToPath(alias);
	}
	/**
	 * Get the index for the given alias. If the index does not exist, it will be created
	 * @param alias
	 * @return The UUID index for the alias
	 * @throws NoSuchElementException If the alias was not found
	 */
	public static UUID getByAlias(String alias) throws NoSuchElementException {
		String path = getAliasToPath(alias);
		if(path == null)
			throw new NoSuchElementException("The alias "+alias+" was not found.");
		if(DEBUG)
			System.out.println("IndexManager.getByAlias attempt for alias:"+alias+" got path:"+path);
		return getByPath(path, true);
	}
	
	/**
	 * Remove the given tablespace path for index.
	 * @param index
	 * @return previous String path of removed UUID index
	 */
	static String removeDatabaseCatalog(UUID index) {
		if(DEBUG)
			System.out.println("VolumeManager.remove for index:"+index);
		String ret = indexToPath.remove(index);
		if(ret != null)
			pathToIndex.remove(ret);
		try {
			RelatrixKV.remove(index);
			RelatrixKV.close(databaseCatalogProperty, UUID.class);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * Remove the index for the given tablespace path.
	 * @param path
	 * @return UUID index of removed path
	 */
	static UUID removeDatabaseCatalog(String path) {
		UUID ret = pathToIndex.remove(path);
		if(DEBUG)
			System.out.println("IndexManager.remove for path:"+path+" will return previous index:"+ret);		
		if(ret != null)
			indexToPath.remove(ret);
		try {
			RelatrixKV.remove(ret);
			RelatrixKV.close(databaseCatalogProperty, UUID.class);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
		setTablespace(args[0]);
		RelatrixTransaction.findStream(args[1], "*", "*", "*").forEach((s) -> {
			System.out.println(s.toString());
		});
	}
 
}
