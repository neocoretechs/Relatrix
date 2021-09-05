package com.neocoretechs.relatrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
//import java.util.Stack;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neocoretechs.bigsack.keyvaluepages.KeyValue;
//import com.neocoretechs.bigsack.keyvaluepages.KeySearchResult;
//import com.neocoretechs.bigsack.keyvaluepages.TraversalStackElement;
//import com.neocoretechs.bigsack.session.BigSackAdapter;
//import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;


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
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism
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
		/*BigSackAdapter*/RelatrixKV.setTablespaceDirectory(path);
	}
	
	public static synchronized String getTableSpaceDirectory() {
		return /*BigSackAdapter.getTableSpaceDir();*/RelatrixKV.getTableSpaceDirectory();
	}
	/**
	 * We cant reasonably check the validity. Set the path to the remote directory that contains the
	 * BigSack tablespaces that comprise our database on remote nodes.
	 * @param path
	 * @throws IOException
	 */
	public static synchronized void setRemoteDirectory(String path) {
		//BigSackAdapter.setRemoteDir(path);
		RelatrixKV.setRemoteDirectory(path);
	}
	
	public static synchronized String getRemoteDirectory() {
		return RelatrixKV.getRemoteDirectory();//BigSackAdapter.getRemoteDir();
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
public static synchronized DomainMapRange transactionalStore(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IllegalAccessException, IOException, DuplicateKeyException {
	if( d == null || m == null || r == null)
		throw new IllegalAccessException("Neither domain, map, nor range may be null when storing a morphism");
	Morphism dmr = new DomainMapRange(d,m,r);
	// check for domain/map match
	// Enforce categorical structure; domain->map function uniquely determines range.
	// If the search winds up at the key or the key is empty or the domain->map exists, the key
	// cannot be inserted.
	//transactionTreeSets[0] = BigSackAdapter.getBigSackTransactionalTreeSet(dmr);
	//indexClasses[0] = dmr.getClass(); indexInstanceTable takes over dmr
	((DomainMapRange)dmr).setUniqueKey(true);
	//Stack stack = new Stack();
	//KeySearchResult tsr = transactionTreeSets[0].locate(dmr, stack);
	Object o = RelatrixKV.get(dmr);
	((DomainMapRange)dmr).setUniqueKey(false);
	if( DEBUG )
		System.out.println("Relatrix.store Tree Search Result: "+o);//tsr);
	if(o != null) { //tsr.atKey) {
		transactionRollback();
		throw new DuplicateKeyException("dmr:"+o);//tsr);
	}
	
	Morphism identity = dmr;
	DomainRangeMap drm = new DomainRangeMap(d,m,r,dmr.getKeys());
	//transactionTreeSets[1] = BigSackAdapter.getBigSackTransactionalTreeSet(drm);
	indexClasses[1] = drm.getClass();
	MapDomainRange mdr = new MapDomainRange(d,m,r,dmr.getKeys());
	//transactionTreeSets[2] = BigSackAdapter.getBigSackTransactionalTreeSet(mdr);
	indexClasses[2] = mdr.getClass();
	MapRangeDomain mrd = new MapRangeDomain(d,m,r,dmr.getKeys());
	//transactionTreeSets[3] = BigSackAdapter.getBigSackTransactionalTreeSet(mrd);
	indexClasses[3] = mrd.getClass();
	RangeDomainMap rdm = new RangeDomainMap(d,m,r,dmr.getKeys());
	//transactionTreeSets[4] = BigSackAdapter.getBigSackTransactionalTreeSet(rdm);
	indexClasses[4] = rdm.getClass();
	RangeMapDomain rmd = new RangeMapDomain(d,m,r,dmr.getKeys());
	//transactionTreeSets[5] = BigSackAdapter.getBigSackTransactionalTreeSet(rmd);
	indexClasses[5] = rmd.getClass();
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing dmr:"+dmr);
	//transactionTreeSets[0].put(dmr);
	DBKey dbKey = null;
	try {
		dbKey = DBKey.newKey(dmr);
	} catch (ClassNotFoundException e) {
		throw new IOException(e);
	} // store primary key, then use it as value for index keys
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing drm:"+drm);
	//transactionTreeSets[1].put(drm);
	RelatrixKV.transactionalStore(drm,  dbKey);
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing mdr:"+mdr);
	//transactionTreeSets[2].put(mdr);
	RelatrixKV.transactionalStore(mdr,  dbKey);
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing mrd:"+mrd);
	//transactionTreeSets[3].put(mrd);
	RelatrixKV.transactionalStore(mrd,  dbKey);

	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing rdm:"+rdm);
	//transactionTreeSets[4].put(rdm);
	RelatrixKV.transactionalStore(rdm,  dbKey);
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing rmd:"+rmd);
	//transactionTreeSets[5].put(rmd);
	RelatrixKV.transactionalStore(rmd,  dbKey);
	
	return (DomainMapRange) identity;
}
/**
 * Commit the outstanding transaction data in each active transactional treeset.
 * @throws IOException
 */
public static synchronized void transactionCommit() throws IOException {
	IndexInstanceTable.commit();
	for(int i = 0; i < indexClasses.length/*transactionTreeSets.length*/; i++) {
		long startTime = System.currentTimeMillis();
		//transactionTreeSets[i].Commit();
		if(indexClasses[i] != null) {
			if( DEBUG || TRACE )
				System.out.println("Committing "+/*transactionTreeSets*/indexClasses[i]);		
			RelatrixKV.transactionCommit(indexClasses[i]);
			if( DEBUG || TRACE )
				System.out.println("Committed "+/*transactionTreeSets*/indexClasses[i]/*.getDBName()*/ + " in " + (System.currentTimeMillis() - startTime) + "ms.");		
			/*transactionTreeSets*/indexClasses[i] = null;
		}
	}
}
/**
 * Roll back all outstanding transactions on the indicies
 * @throws IOException
 */
public static synchronized void transactionRollback() throws IOException {
	IndexInstanceTable.rollback();
	for(int i = 0; i < /*transactionTreeSets*/indexClasses.length; i++) {
		if(/*transactionTreeSets*/indexClasses[i] != null) {
			/*transactionTreeSets*/RelatrixKV.transactionRollback(indexClasses[i]);//transactionTreeSets[i].Rollback();
			/*transactionTreeSets*/indexClasses[i] = null;
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
public static synchronized void transactionCheckpoint() throws IOException, IllegalAccessException {
	IndexInstanceTable.checkpoint();
	for(int i = 0; i < /*transactionTreeSets*/indexClasses.length; i++) {
		/*transactionTreeSets[i].Checkpoint();*/
		if(indexClasses[1] != null)
			RelatrixKV.transactionCheckpoint(indexClasses[i]);
	}
}
/**
* Delete all relationships that this object participates in
* @exception IOException low-level access or problems modifiying schema
 * @throws IllegalAccessException 
 * @throws ClassNotFoundException 
 * @throws IllegalArgumentException 
*/
public static synchronized void remove(Comparable<?> c) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	if( DEBUG || DEBUGREMOVE )
		System.out.println("Relatrix.remove prepping to remove:"+c);
	ArrayList<Morphism> m = new ArrayList<Morphism>();
	try {
		Iterator<?> it = findSet(c,"*","*");
		while(it.hasNext()) {
			Comparable[] o = (Comparable[]) it.next();
			if( DEBUG || DEBUGREMOVE)
				System.out.println("Relatrix.remove iterated perm 1 "+o[0]+" of type "+o[0].getClass().getName());
			m.add((Morphism) o[0]); 
		}
	} catch(RuntimeException re) { /*re.printStackTrace();*/} // We can get this exception if the class types differ in domain
	try {
		Iterator<?> it = findSet("*",c,"*");
		while(it.hasNext()) {
			Comparable[] o = (Comparable[]) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove iterated perm 2 "+o[0]+" of type "+o[0].getClass().getName());
			m.add((Morphism) o[0]); 
		}
	} catch(RuntimeException re) {/*re.printStackTrace();*/} // we can get this exception if map class types differ
	try {
		Iterator<?> it = findSet("*","*",c);
		while(it.hasNext()) {
			Comparable[] o = (Comparable[]) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove iterated perm 3 "+o[0]+" of type "+o[0].getClass().getName());
			m.add((Morphism) o[0]); 
		}
	} catch(RuntimeException re) { /*re.printStackTrace();*/ } // we can get this exception if range class types differ
	// Process our array of candidates
	for(Morphism mo : m) {
		if( DEBUG || DEBUGREMOVE)
			System.out.println("Relatrix.remove removing"+mo);
		remove(mo.getDomain(), mo.getMap(), mo.getRange());
	}
	if( DEBUG || DEBUGREMOVE )
		System.out.println("Relatrix.remove exiting remove for key:"+c+" should have removed "+m.size());
}
/**
 * Delete specific relationship and all relationships that it participates in
 * @param d
 * @param m
 * @param r
 * @throws IllegalAccessException 
 */
public static synchronized void remove(Comparable<?> d, Comparable<?> m, Comparable<?> r) throws IOException, IllegalAccessException {
	Morphism dmr = new DomainMapRange(d,m,r);
	Object o = RelatrixKV.get(dmr);
	if(o == null)
		return;
	KeyValue kv = (KeyValue)o;
	DBKey dbKey = (DBKey) kv.getmValue();
	try {
		IndexInstanceTable.delete(dbKey);
	} catch (ClassNotFoundException | DuplicateKeyException e) {
		throw new IOException(e);
	}
	indexClasses[0] = null;
	DomainRangeMap drm = new DomainRangeMap(d,m,r);
	indexClasses[1] = drm.getClass();
	MapDomainRange mdr = new MapDomainRange(d,m,r);
	indexClasses[2] = mdr.getClass();
	MapRangeDomain mrd = new MapRangeDomain(d,m,r);
	indexClasses[3] = mrd.getClass();
	RangeDomainMap rdm = new RangeDomainMap(d,m,r);
	indexClasses[4] = rdm.getClass();
	RangeMapDomain rmd = new RangeMapDomain(d,m,r);
	indexClasses[5] = rmd.getClass();
	if( DEBUG || DEBUGREMOVE )
		System.out.println("Relatrix.remove removed dmr:"+dmr);
	try {
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
public static synchronized Iterator<?> findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
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
public static synchronized Stream<?> findStream(Object darg, Object marg, Object rarg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
    Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
    return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
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
public static synchronized Stream<?> findTailStream(Object darg, Object marg, Object rarg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference
	if( (darg.equals(OPERATOR_WILDCARD) || darg.equals(OPERATOR_TUPLE)) && 
		(marg.equals(OPERATOR_WILDCARD) || marg.equals(OPERATOR_TUPLE)) &&
		(rarg.equals(OPERATOR_WILDCARD) || rarg.equals(OPERATOR_TUPLE))) 
		throw new IllegalArgumentException("At least one argument to findTailStream must contain an object reference");
	IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
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
public static synchronized Stream<?> findHeadStream(Object darg, Object marg, Object rarg, boolean... parallel) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	// check for at least one object reference in our headset factory
	IteratorFactory ifact = IteratorFactory.createHeadsetFactory(darg, marg, rarg);
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, (parallel.length == 0 ? false : parallel[0]));
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
public static synchronized Stream<?> findSubStream(Object darg, Object marg, Object rarg, boolean parallel, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
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
	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(ifact.createIterator(), characteristics);
	return (Stream<?>) StreamSupport.stream(spliterator, parallel);
}
/**
 * If the desire is to step outside the database and category theoretic realm and use the instances more as a basic Set, this method returns the first DomainMapRange
 * instance having the lowest valued key.
 * @return the DomainMapRange morphism having the lowest key value.
 * @throws IOException
 */
public static synchronized Object first() throws IOException
{
	if( /*transactionTreeSets*/indexClasses[0] == null ) {
		return null;
	}
	/*
	Stack stack = new Stack();
	TraversalStackElement tse = new TraversalStackElement(null,0,0);
	return transactionTreeSets[0].first(tse, stack);
	*/
	try {
		return RelatrixKV.firstKey(indexClasses[0]);
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
	/*
	Stack stack = new Stack();
	TraversalStackElement tse = new TraversalStackElement(null,0,0);
	return transactionTreeSets[0].last(tse, stack);
	*/
	try {
		return RelatrixKV.lastKey(indexClasses[0]);
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
	if( /*transactionTreeSets*/indexClasses[0] == null ) {
		return -1;
	}
	//return transactionTreeSets[0].size();
	try {
		return RelatrixKV.size(indexClasses[0]);
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
	if( /*transactionTreeSets*/indexClasses[0] == null ) {
		return false;
	}
	//return transactionTreeSets[0].contains(obj);
	try {
		return RelatrixKV.contains(obj);
	} catch (IllegalAccessException e) {
		throw new IOException(e);
	}
}

public static void main(String[] args) throws Exception {
	setTablespaceDirectory(args[0]);
	Relatrix.findStream("*", "*", "*", false).forEach((s) -> {
		System.out.println(s.toString());
	});
}
}

