package com.neocoretechs.relatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.rocksack.session.RockSackAdapter;
import com.neocoretechs.rocksack.session.TransactionalMap;

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
	public static synchronized void setTablespaceDirectory(String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set tablespace directory for fileset "+path+" to allocate persistent storage.");
		RelatrixKVTransaction.setTablespaceDirectory(path);
	}
	
	public static synchronized String getTableSpaceDirectory() {
		return RelatrixKVTransaction.getTableSpaceDirectory();
	}

	public static String getTransactionId() throws IllegalAccessException, IOException {
		String xid =  RockSackAdapter.getRockSackTransactionId();
		IndexResolver.setIndexInstanceTable(xid);
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
		RockSackAdapter.removeRockSackTransaction(xid);
		IndexResolver.remove(xid);
	}
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * This is a transactional store in the context of a previously initiated transaction.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation.
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 */
	public static synchronized DomainMapRangeTransaction store(String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException {
		if( d == null || m == null || r == null)
			throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
		MorphismTransaction dmr = new DomainMapRangeTransaction(d,m,r,true); // form it as template for duplicate key search
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted.
		((DomainMapRangeTransaction)dmr).setUniqueKey(true);
		if(RelatrixKVTransaction.contains(xid, dmr)) {
			rollback(xid);
			throw new DuplicateKeyException("dmr:"+dmr);
		}
		((DomainMapRangeTransaction)dmr).setUniqueKey(false);
		// re-create it, now that we know its valid, in a form that stores the components with DBKeys
		// and maintains the classes stores in IndexInstanceTable for future commit.
		dmr = new DomainMapRangeTransaction(d,m,r);
		MorphismTransaction identity = dmr;
		DomainRangeMapTransaction drm = new DomainRangeMapTransaction(d,m,r,dmr.getKeys());
		indexClasses[1] = drm.getClass();
		MapDomainRangeTransaction mdr = new MapDomainRangeTransaction(d,m,r,dmr.getKeys());
		indexClasses[2] = mdr.getClass();
		MapRangeDomainTransaction mrd = new MapRangeDomainTransaction(d,m,r,dmr.getKeys());
		indexClasses[3] = mrd.getClass();
		RangeDomainMapTransaction rdm = new RangeDomainMapTransaction(d,m,r,dmr.getKeys());
		indexClasses[4] = rdm.getClass();
		RangeMapDomainTransaction rmd = new RangeMapDomainTransaction(d,m,r,dmr.getKeys());
		indexClasses[5] = rmd.getClass();
		DBKey dbKey = null;
		// this gives our DMR a key, and places it in the IndexInstanceTable pervue for commit
		indexClasses[0] = null; // remove dmr from our commit lineup
		try {
			dbKey = DBKey.newKey(IndexResolver.getIndexInstanceTable(xid),dmr); // this stores our new relation, DBKey and instance
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} // Use primary key DBKey as value for index keys
		if( DEBUG  )
			System.out.println("RelatrixTransaction.transactionalStore Id:"+xid+" storing drm:"+drm);
		RelatrixKVTransaction.store(xid, drm, dbKey);
	
		if( DEBUG  )
			System.out.println("RelatrixTransaction.transactionalStore Id:"+xid+" storing mdr:"+mdr);
		RelatrixKVTransaction.store(xid, mdr, dbKey);
	
		if( DEBUG  )
			System.out.println("RelatrixTransaction.transactionalStore Id:"+xid+" storing mrd:"+mrd);
		RelatrixKVTransaction.store(xid, mrd, dbKey);

		if( DEBUG  )
			System.out.println("RelatrixTransaction.transactionalStore Id:"+xid+" storing rdm:"+rdm);
		RelatrixKVTransaction.store(xid, rdm, dbKey);
	
		if( DEBUG  )
			System.out.println("RelatrixTransaction.transactionalStore Id:"+xid+" storing rmd:"+rmd);
		RelatrixKVTransaction.store(xid, rmd, dbKey);
	
		return (DomainMapRangeTransaction) identity;
	}
	/**
	 * Commit the outstanding transaction data in each active transactional treeset.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void commit(String xid) throws IOException, IllegalAccessException {
		// first commit components of relationships
		IndexResolver.getIndexInstanceTable(xid).commit();
		// now commit main relationship and index classes
		for(int i = 0; i < indexClasses.length; i++) {
			long startTime = System.currentTimeMillis();
			if(indexClasses[i] != null) {
				if( DEBUG || TRACE )
					System.out.println("Committing "+indexClasses[i]+" with transaction:"+xid);		
				RelatrixKVTransaction.commit(xid, indexClasses[i]);
				if( DEBUG || TRACE )
					System.out.println("Committed "+indexClasses[i] + " with transaction " + xid + " in " + (System.currentTimeMillis() - startTime) + "ms.");		
				indexClasses[i] = null;
			}
		}
	}
	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void rollback(String xid) throws IOException, IllegalAccessException {
		// first roll back components
		IndexResolver.getIndexInstanceTable(xid).rollback();
		// Now roll back relationships
		for(int i = 0; i < indexClasses.length; i++) {
			if(indexClasses[i] != null) {
				RelatrixKVTransaction.rollback(xid, indexClasses[i]);
				indexClasses[i] = null;
			}
		}
	}
	/**
	 * Take a check point of our current indicies. What this means is that we are
	 * going to write a log record such that if we crash will will restore the logs from that point forward.
	 * We have to have confidence that we are doing this at a legitimate point, so this should only be called if things are well
	 * and processing is proceeding normally. Its a way to say "start from here and go forward in time 
	 * if we crash, to restore the data to its state up to that point", hence check, point...
	 * If we are loading lots of data and we want to partially confirm it as part of the database, we do this.
	 * It does not perform a 'commit' because if we chose to do so we could start a roll forward recovery and restore
	 * even the old data before the checkpoint.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public static synchronized void checkpoint(String xid) throws IOException, IllegalAccessException {
		IndexResolver.getIndexInstanceTable(xid).checkpoint();
		for(int i = 0; i < indexClasses.length; i++) {
			if(indexClasses[i] != null)
				RelatrixKVTransaction.checkpoint(xid, indexClasses[i]);
		}
	}
	/**
	 * Delete all relationships that this object participates in
	 * @exception IOException low-level access or problems modifiying schema
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static synchronized void remove(String xid, Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		if( DEBUG || DEBUGREMOVE )
			System.out.println("RelatrixTransaction.remove Id:"+xid+" prepping to remove:"+c);
		removeRecursive(xid, c);
		try {
			DBKey dbKey = IndexResolver.getIndexInstanceTable(xid).getByInstance(c);
			if( DEBUG || DEBUGREMOVE )
				System.out.println("RelatrixTransaction.remove Id:"+xid+" prepping to remove DBKey:"+dbKey);
			if(dbKey != null) {
				// Should delete instance and DbKey
				IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
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
	 * Iterate through all possible relationships the given element may participate in, then recursively process those
	 * relationships to remove references to those.
	 * @param c
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
	 * Delete specific relationship and all relationships that it participates in. Some redundancy built in to
	 * the removal process to ensure all keys are removed regardless of existence of proper DBKey.
	 * @param d
	 * @param m
	 * @param r
	 * @throws IllegalAccessException 
	 */
	public static synchronized void remove(String xid, Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException {
		MorphismTransaction dmr = new DomainMapRangeTransaction(d,m,r,true);
		indexClasses[0] = dmr.getClass();
		DomainRangeMapTransaction drm = new DomainRangeMapTransaction(d,m,r,true);
		indexClasses[1] = drm.getClass();
		MapDomainRangeTransaction mdr = new MapDomainRangeTransaction(d,m,r,true);
		indexClasses[2] = mdr.getClass();
		MapRangeDomainTransaction mrd = new MapRangeDomainTransaction(d,m,r,true);
		indexClasses[3] = mrd.getClass();
		RangeDomainMapTransaction rdm = new RangeDomainMapTransaction(d,m,r,true);
		indexClasses[4] = rdm.getClass();
		RangeMapDomainTransaction rmd = new RangeMapDomainTransaction(d,m,r,true);
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
			IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
			o = RelatrixKVTransaction.get(xid, drm);
			if(o == null)
				throw new IOException(drm+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
			o = RelatrixKVTransaction.get(xid, mdr);
			if(o == null)
				throw new IOException(mdr+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
			o = RelatrixKVTransaction.get(xid, mrd);
			if(o == null)
				throw new IOException(mrd+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
			o = RelatrixKVTransaction.get(xid, rdm);
			if(o == null)
				throw new IOException(rdm+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
			o = RelatrixKVTransaction.get(xid, rmd);
			if(o == null)
				throw new IOException(rmd+" not found for delete Id:"+xid);
		//kv = (KeyValue)o;
		//dbKey = (DBKey) kv.getmValue();
			dbKey = (DBKey)o;
			IndexResolver.getIndexInstanceTable(xid).delete(dbKey);
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
	public static synchronized Iterator<?> findSet(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		IteratorFactory ifact = IteratorFactory.createFactory(xid, darg, marg, rarg);
		return ifact.createIterator();
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
	public static synchronized Stream<?> findStream(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
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
* @param darg Object for domain of relationship, a dont-care wildcard "*", a return-object "?", or class template
* @param marg Object for the map of relationship , a dont-care wildcard "*", a return-object "?", or a class template
* @param rarg Object for the range of the relationship, a dont-care wildcard "*", a return-object "?", or a class template
* @exception IOException low-level access or problems modifiying schema
* @exception IllegalArgumentException At least one argument must be a valid object reference instead of a wildcard * or ?
* @exception ClassNotFoundException if the Class of Object is invalid
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
public static synchronized Stream<?> findHeadStream(String xid, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference in our headset factory
	IteratorFactory ifact = IteratorFactory.createHeadsetFactory(xid, darg, marg, rarg);
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
 * instance having the lowest valued key value of the index classes.
 * @return the DomainMapRange morphism having the lowest valued key value.
 * @throws IOException
 */
public static synchronized Object firstKey(String xid) throws IOException
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
 * instance having the lowest valued key value of the index classes.
 * @return the DomainMapRange morphism having the lowest valued key value.
 * @throws IOException
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
 * instance having the lowest valued key value.
 * @return the DomainMapRange morphism having the lowest valued key value.
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
 * instance having the highest valued key.
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
 * instance having the highest valued key.
 * @return the DomainMapRange morphism having the highest key value.
 * @throws IOException
 */
public static synchronized Object lastKey(String xid) throws IOException
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
 * instance having the value of the highest valued key.
 * @return the DomainMapRange morphism having the value of highest key.
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
 * instance having the highest valued key.
 * @return the DomainMapRange morphism having the highest key value.
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the last DomainMapRange
 * instance having the highest valued key.
 * @return the DomainMapRange morphism having the highest key value.
 * @throws IOException
 */
public static synchronized Object lastKey(String xid, Class clazz) throws IOException
{
	try {
		return RelatrixKVTransaction.lastKey(xid, clazz);
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
public static synchronized Object lastValue(String xid, Class clazz) throws IOException
{
	try {
		return RelatrixKVTransaction.lastValue(xid, clazz);
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
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns whether the passed DomainMapRange
 * instance exists in the data.
 * @return true if the passed DomainMapRAnge exists.
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
 * Store our permutations of the key/value
 * This is a transactional store in the context of a previously initiated transaction.
 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
 * to prevent a recovery on the next operation.
 * @param key of comparable
 * @param value
 * @throws IllegalAccessException
 * @throws IOException
 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
 */
public static synchronized void store(String xid, Comparable<?> key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException {
	RelatrixKVTransaction.store(xid, key,  value);
}
/**
 * Commit the outstanding transaction data in each active transactional treeset.
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized void commit(String xid, Class clazz) throws IOException, IllegalAccessException {
	RelatrixKVTransaction.commit(xid, clazz);
}
/**
 * Roll back all outstanding transactions on the given class, overlap with K/V functionality
 * @throws IOException
 * @throws IllegalAccessException 
 */
public static synchronized void rollback(String xid, Class clazz) throws IOException, IllegalAccessException {
	RelatrixKVTransaction.rollback(xid, clazz);
}
/**
 * Take a check point of our current indicies. What this means is that we are
 * going to write a log record such that if we crash will will restore the logs from that point forward.
 * We have to have confidence that we are doing this at a legitimate point, so this should only be called if things are well
 * and processing is proceeding normally. Its a way to say "start from here and go forward in time 
 * if we crash, to restore the data to its state up to that point", hence check, point...
 * If we are loading lots of data and we want to partially confirm it as part of the database, we do this.
 * It does not perform a 'commit' because if we chose to do so we could start a roll forward recovery and restore
 * even the old data before the checkpoint.
 * @param clazz The class for which the map has been created.
 * @throws IOException
 * @throws IllegalAccessException 
 */
 public static synchronized void checkpoint(String xid, Class clazz) throws IOException, IllegalAccessException {
	RelatrixKVTransaction.checkpoint(xid, clazz);
 }
 /**
  * return lowest valued key.
  * @param clazz the class to retrieve
  * @return the The key/value with lowest key value.
  * @throws IOException
  * @throws IllegalAccessException 
  */
 public static synchronized Object firstKey(String xid, Class clazz) throws IOException, IllegalAccessException
 {
	 return RelatrixKVTransaction.firstKey(xid, clazz);
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
  * Return the value for the key.
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
  * Return the Object pointed to by the DBKey. this is to support remote iterators.
  * @param key the key to retrieve
  * @return The instance by DBKey
  * @throws IOException
  * @throws IllegalAccessException 
  * @throws ClassNotFoundException 
  */
 public static synchronized Object getByIndex(String xid,Comparable key) throws IOException, IllegalAccessException, ClassNotFoundException
 {
	 return IndexResolver.getIndexInstanceTable(xid).getByIndex((DBKey) key);
 }
 /**
  * Return the keyset for the given class
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
  * Return the entry set for the given class type
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

 public static void main(String[] args) throws Exception {
	setTablespaceDirectory(args[0]);
	RelatrixTransaction.findStream(args[1], "*", "*", "*").forEach((s) -> {
		System.out.println(s.toString());
	});
 }
 
}
