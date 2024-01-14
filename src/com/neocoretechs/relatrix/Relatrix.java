package com.neocoretechs.relatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public final class Relatrix {
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
	 */
	public static synchronized DomainMapRange store(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		Morphism dmr = new DomainMapRange(d,m,r,true); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted.
		((DomainMapRange)dmr).setUniqueKey(true);
		if(RelatrixKV.contains(dmr)) {
			throw new DuplicateKeyException("dmr:"+dmr);
		}
		((DomainMapRange)dmr).setUniqueKey(false);
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		dmr = new DomainMapRange(d,m,r);
		Morphism identity = dmr;
		DomainRangeMap drm = new DomainRangeMap(d,m,r,dmr.getKeys());
		indexClasses[1] = drm.getClass();
		MapDomainRange mdr = new MapDomainRange(d,m,r,dmr.getKeys());
		indexClasses[2] = mdr.getClass();
		MapRangeDomain mrd = new MapRangeDomain(d,m,r,dmr.getKeys());
		indexClasses[3] = mrd.getClass();
		RangeDomainMap rdm = new RangeDomainMap(d,m,r,dmr.getKeys());
		indexClasses[4] = rdm.getClass();
		RangeMapDomain rmd = new RangeMapDomain(d,m,r,dmr.getKeys());
		indexClasses[5] = rmd.getClass();
		DBKey dbKey = null;
		// this gives our DMR a key, and places it in the IndexInstanceTable pervue for commit
		indexClasses[0] = null; // remove dmr from our commit lineup
		try {
			dbKey = DBKey.newKey(IndexResolver.getIndexInstanceTable(),dmr); // this stores our new relation, DBKey and instance
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} // Use primary key DBKey as value for index keys
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing drm:"+drm);
		RelatrixKV.store(drm, dbKey);
	
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing mdr:"+mdr);
		RelatrixKV.store(mdr, dbKey);
	
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing mrd:"+mrd);
		RelatrixKV.store(mrd, dbKey);

		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing rdm:"+rdm);
		RelatrixKV.store(rdm, dbKey);
	
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing rmd:"+rmd);
		RelatrixKV.store(rmd, dbKey);
	
		return (DomainMapRange) identity;
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
	 */
	public static synchronized DomainMapRange store(String alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		Morphism dmr = new DomainMapRange(d,m,r,true); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted.
		((DomainMapRange)dmr).setUniqueKey(true);
		if(RelatrixKV.contains(dmr)) {
			throw new DuplicateKeyException("dmr:"+dmr);
		}
		((DomainMapRange)dmr).setUniqueKey(false);
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		dmr = new DomainMapRange(d,m,r);
		Morphism identity = dmr;
		DomainRangeMap drm = new DomainRangeMap(d,m,r,dmr.getKeys());
		indexClasses[1] = drm.getClass();
		MapDomainRange mdr = new MapDomainRange(d,m,r,dmr.getKeys());
		indexClasses[2] = mdr.getClass();
		MapRangeDomain mrd = new MapRangeDomain(d,m,r,dmr.getKeys());
		indexClasses[3] = mrd.getClass();
		RangeDomainMap rdm = new RangeDomainMap(d,m,r,dmr.getKeys());
		indexClasses[4] = rdm.getClass();
		RangeMapDomain rmd = new RangeMapDomain(d,m,r,dmr.getKeys());
		indexClasses[5] = rmd.getClass();
		DBKey dbKey = null;
		// this gives our DMR a key, and places it in the IndexInstanceTable pervue for commit
		indexClasses[0] = null; // remove dmr from our commit lineup
		try {
			dbKey = DBKey.newKey(alias, IndexResolver.getIndexInstanceTable(), dmr); // this stores our new relation, DBKey and instance
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} // Use primary key DBKey as value for index keys
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing drm:"+drm);
		RelatrixKV.store(alias, drm, dbKey);
	
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing mdr:"+mdr);
		RelatrixKV.store(alias, mdr, dbKey);
	
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing mrd:"+mrd);
		RelatrixKV.store(alias, mrd, dbKey);

		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing rdm:"+rdm);
		RelatrixKV.store(alias, rdm, dbKey);
	
		if( DEBUG  )
			System.out.println("Relatrix.transactionalStore storing rmd:"+rmd);
		RelatrixKV.store(alias, rmd, dbKey);
	
		return (DomainMapRange) identity;
	}

	public static void storekv(Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException {
		RelatrixKV.store(key, value);
	}

	public static void storekv(String alias, Comparable key, Object value) throws IOException, IllegalAccessException, DuplicateKeyException, NoSuchElementException {
		RelatrixKV.store(alias, key, value);
	}
	/**
	 * Delete all relationships that this object participates in
	 * @param c The Comparable key to remove
	 * @throws IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static synchronized void remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);
		removeRecursive(c);
		try {
			DBKey dbKey = IndexResolver.getIndexInstanceTable().getByInstance(c);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove prepping to remove DBKey:"+dbKey);
			if(dbKey != null) {
			// Should delete instance and DbKey
				IndexResolver.getIndexInstanceTable().delete(dbKey);
			} else {
				// failsafe delete, if we dont find the key for whatever reason, proceed to remove the instance directly if possible
				RelatrixKV.remove(c);
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
	 */
	public static synchronized void remove(String alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("Relatrix.remove prepping to remove:"+c);
		removeRecursive(alias, c);
		try {
			DBKey dbKey = IndexResolver.getIndexInstanceTable().getByInstance(alias, c);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove prepping to remove DBKey:"+dbKey);
			if(dbKey != null) {
				// Should delete instance and DbKey
				IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
			} else {
				// failsafe delete, if we dont find the key for whatever reason, proceed to remove the instance directly if possible
				RelatrixKV.remove(alias, c);
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
	 */
	private static synchronized void removeRecursive(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		ArrayList<Morphism> m = new ArrayList<Morphism>();
		try {
			Iterator<?> it = findSet(c,"*","*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE)
					System.out.println("Relatrix.remove iterated perm 1 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // We can get this exception if the class types differ in domain
		try {
			Iterator<?> it = findSet("*",c,"*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove iterated perm 2 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // we can get this exception if map class types differ
		try {
			Iterator<?> it = findSet("*","*",c);
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove iterated perm 3 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) { 
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace(); 
		} // we can get this exception if range class types differ
		// Process our array of candidates
		for(Morphism mo : m) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove removing:"+mo);
			remove(mo.getDomain(), mo.getMap(), mo.getRange());
			// if this morphism participates in any relationship. remove that relationship recursively
			removeRecursive(mo);
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
	 */
	private static synchronized void removeRecursive(String alias, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		ArrayList<Morphism> m = new ArrayList<Morphism>();
		try {
			Iterator<?> it = findSet(alias,c,"*","*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE)
					System.out.println("Relatrix.remove iterated perm 1 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // We can get this exception if the class types differ in domain
		try {
			Iterator<?> it = findSet(alias,"*",c,"*");
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove iterated perm 2 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) {
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace();
		} // we can get this exception if map class types differ
		try {
			Iterator<?> it = findSet(alias,"*","*",c);
			while(it.hasNext()) {
				Comparable[] o = (Comparable[]) it.next();
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove iterated perm 3 "+o[0]+" of type "+o[0].getClass().getName());
				m.add((Morphism) o[0]); 
			}
		} catch(RuntimeException re) { 
			if( DEBUG || DEBUGREMOVE)
				re.printStackTrace(); 
		} // we can get this exception if range class types differ
		// Process our array of candidates
		for(Morphism mo : m) {
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove removing:"+mo);
			remove(alias, mo.getDomain(), mo.getMap(), mo.getRange());
			// if this morphism participates in any relationship. remove that relationship recursively
			removeRecursive(alias, mo);
		}
	}
	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @param r
	 * @throws IllegalAccessException 
	 */
	public static synchronized void remove(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException {
		Morphism dmr = new DomainMapRange(d,m,r,true);
		indexClasses[0] = dmr.getClass();
		DomainRangeMap drm = new DomainRangeMap(d,m,r,true);
		indexClasses[1] = drm.getClass();
		MapDomainRange mdr = new MapDomainRange(d,m,r,true);
		indexClasses[2] = mdr.getClass();
		MapRangeDomain mrd = new MapRangeDomain(d,m,r,true);
		indexClasses[3] = mrd.getClass();
		RangeDomainMap rdm = new RangeDomainMap(d,m,r,true);
		indexClasses[4] = rdm.getClass();
		RangeMapDomain rmd = new RangeMapDomain(d,m,r,true);
		indexClasses[5] = rmd.getClass();

		try {
			Object o = RelatrixKV.get(dmr);
			if(o == null) {
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove could not find relationship dmr:"+dmr);
				return;
			}
			//KeyValue kv = (KeyValue)o;
			//DBKey dbKey = (DBKey) kv.getmValue();
			DBKey dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(dbKey);
			o = RelatrixKV.get(drm);
			if(o == null)
				throw new IOException(drm+" not found for delete");
			//kv = (KeyValue)o;
			//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(dbKey);
			o = RelatrixKV.get(mdr);
			if(o == null)
				throw new IOException(mdr+" not found for delete");
			//kv = (KeyValue)o;
			//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(dbKey);
			o = RelatrixKV.get(mrd);
			if(o == null)
				throw new IOException(mrd+" not found for delete");
			//kv = (KeyValue)o;
			//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(dbKey);
			o = RelatrixKV.get(rdm);
			if(o == null)
				throw new IOException(rdm+" not found for delete");
			//kv = (KeyValue)o;
			//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(dbKey);
			o = RelatrixKV.get(rmd);
			if(o == null)
				throw new IOException(rmd+" not found for delete");
			//kv = (KeyValue)o;
			//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(dbKey);
		} catch (ClassNotFoundException | DuplicateKeyException e) {
			throw new IOException(e);
		}

		try {
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing dmr:"+dmr);
			RelatrixKV.remove(dmr);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+drm);
			RelatrixKV.remove(drm);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+mdr);
			RelatrixKV.remove(mdr);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+mrd);
			RelatrixKV.remove(mrd);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+rdm);
			RelatrixKV.remove(rdm);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+rmd);
			RelatrixKV.remove(rmd);
		} catch (IllegalArgumentException | ClassNotFoundException e) {
			throw new IOException(e);
		}

	}

	/**
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @param r
	 * @throws IllegalAccessException 
	 */
	public static synchronized void remove(String alias, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException, NoSuchElementException {
		Morphism dmr = new DomainMapRange(d,m,r,true);
		indexClasses[0] = dmr.getClass();
		DomainRangeMap drm = new DomainRangeMap(d,m,r,true);
		indexClasses[1] = drm.getClass();
		MapDomainRange mdr = new MapDomainRange(d,m,r,true);
		indexClasses[2] = mdr.getClass();
		MapRangeDomain mrd = new MapRangeDomain(d,m,r,true);
		indexClasses[3] = mrd.getClass();
		RangeDomainMap rdm = new RangeDomainMap(d,m,r,true);
		indexClasses[4] = rdm.getClass();
		RangeMapDomain rmd = new RangeMapDomain(d,m,r,true);
		indexClasses[5] = rmd.getClass();

		try {
			Object o = RelatrixKV.get(alias, dmr);
			if(o == null) {
				if( DEBUG || DEBUGREMOVE )
					System.out.println("Relatrix.remove could not find relationship dmr:"+dmr);
				return;
			}
			DBKey dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
			o = RelatrixKV.get(drm);
			if(o == null)
				throw new IOException(drm+" not found for delete");
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
			o = RelatrixKV.get(alias, mdr);
			if(o == null)
				throw new IOException(mdr+" not found for delete");
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
			o = RelatrixKV.get(alias, mrd);
			if(o == null)
				throw new IOException(mrd+" not found for delete");
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
			o = RelatrixKV.get(alias, rdm);
			if(o == null)
				throw new IOException(rdm+" not found for delete");
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
			o = RelatrixKV.get(alias, rmd);
			if(o == null)
				throw new IOException(rmd+" not found for delete");
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable().delete(alias, dbKey);
		} catch (ClassNotFoundException | DuplicateKeyException e) {
			throw new IOException(e);
		}

		try {
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing dmr:"+dmr);
			RelatrixKV.remove(alias, dmr);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+drm);
			RelatrixKV.remove(alias, drm);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+mdr);
			RelatrixKV.remove(alias, mdr);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+mrd);
			RelatrixKV.remove(alias, mrd);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+rdm);
			RelatrixKV.remove(alias, rdm);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove removing "+rmd);
			RelatrixKV.remove(alias, rmd);
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
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param marg Object for the map of relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException the operator is invalid
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
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
	 * The returned Comparable[] array is always of dimension n="# of question marks" or a one element array of a single object.
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
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
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
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}

	public static synchronized Stream<?> findStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
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
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 */
	public static synchronized Iterator<?> findTailSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailSet must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
		return ifact.createIterator();
	}

	public static synchronized Iterator<?> findTailSet(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailSet must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
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
	 * @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
	 * @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
	 * @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
	 * @param parallel Optional true for parallel stream execution
	 * @exception IOException low-level access or problems modifiying schema
	 * @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @exception ClassNotFoundException if the Class of Object is invalid
	 * @throws IllegalAccessException 
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Comparable[]>
	 */
	public static synchronized Stream<?> findTailStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailStream must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}

	public static synchronized Stream<?> findTailStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference
		if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
				(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
				(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
			throw new IllegalArgumentException("At least one argument to findTailStream must contain an object reference");
		IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
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
	 * @param darg Domain of morphism, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg Map of morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg Range or codomain or morphism relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public static synchronized Iterator<?> findHeadSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg);
		return ifact.createIterator();
	}

	public static synchronized Iterator<?> findHeadSet(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg);
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
	 * @param parallel Optional true to execute stream in parallel
	 * @return The Stream from which the data may be retrieved. Follows java.util.Stream interface, return Stream<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public static synchronized Stream<?> findHeadStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}

	public static synchronized Stream<?> findHeadStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
	{
		// check for at least one object reference in our headset factory
		IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg);
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
	 * @param darg The domain of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param marg The map of the relationship to retrieve, a dont-care wildcard "*", a return-object "?", or class
	 * @param rarg The range or codomain of the relationship, a dont-care wildcard "*", a return-object "?", or class
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException The number of arguments to the ending range of findSubSet dont match the number of objects declared for the starting range, or no concrete objects vs wildcards are supplied.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public static synchronized Iterator<?> findSubSet(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
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
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		return ifact.createIterator();
	}

	public static synchronized Iterator<?> findSubSet(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
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
	 */
	public static synchronized Stream<?> findSubStream(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
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
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}

	public static synchronized Stream<?> findSubStream(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException
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
		IteratorFactory ifact = IteratorFactory.createSubsetFactory(darg, marg, rarg, endarg);
		Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(alias), characteristics);
		return (Stream<?>) StreamSupport.stream(spliterator, true);
	}
	/**
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
	 * instance having the lowest valued key value of the index classes.
	 * @return the DomainMapRange morphism having the lowest valued key value.
	 * @throws IOException
	 */
	public static synchronized Object first() throws IOException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.firstKey(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object first(String alias) throws IOException, NoSuchElementException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.firstKey(alias, indexClasses[0]);
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
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.firstKey(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object firstKey(String alias) throws IOException, NoSuchElementException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.firstKey(alias, indexClasses[0]);
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
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.firstValue(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object firstValue(String alias) throws IOException, NoSuchElementException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.firstValue(alias, indexClasses[0]);
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
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.lastKey(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object last(String alias) throws IOException, NoSuchElementException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.lastKey(alias, indexClasses[0]);
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
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.lastKey(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object lastKey(String alias) throws IOException, NoSuchElementException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.lastKey(alias, indexClasses[0]);
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
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.lastValue(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized Object lastValue(String alias) throws IOException, NoSuchElementException
	{
		if( /*transactionTreeSets*/indexClasses[0] == null ) {
			return null;
		}
		try {
			return RelatrixKV.lastValue(alias, indexClasses[0]);
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
	 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the number of DomainMapRange
	 * instances.
	 * @return the number of DomainMapRange morphisms.
	 * @throws IOException
	 */
	public static synchronized long size() throws IOException
	{
		if( indexClasses[0] == null ) {
			return -1;
		}
		try {
			return RelatrixKV.size(indexClasses[0]);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized long size(String alias) throws IOException, NoSuchElementException
	{
		if( indexClasses[0] == null ) {
			return -1;
		}
		try {
			return RelatrixKV.size(alias, indexClasses[0]);
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
		if(indexClasses[0] == null ) {
			return false;
		}
		try {
			return RelatrixKV.contains(obj);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
	}

	public static synchronized boolean contains(String alias, Comparable obj) throws IOException, NoSuchElementException
	{
		if(indexClasses[0] == null ) {
			return false;
		}
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
	public static synchronized UUID getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		UUID nkey = UUID.randomUUID();
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
	public static synchronized Object getByIndex(Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException
	{
		return IndexResolver.getIndexInstanceTable().getByIndex((DBKey) key);
	}

	public static synchronized Object getByIndex(String alias, Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException, NoSuchElementException
	{
		return IndexResolver.getIndexInstanceTable().getByIndex(alias, (DBKey) key);
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
		return ret;
	}



}
