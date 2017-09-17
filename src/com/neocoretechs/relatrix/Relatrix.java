package com.neocoretechs.relatrix;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.bigsack.BigSackAdapter;
import com.neocoretechs.bigsack.btree.TreeSearchResult;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.iterator.IteratorFactory;


/**
* Wrapper for structural (relationship) morphism identity objects and the representable operators that retrieve them<dd>
* Utilizes the Morphism subclasses which contain reference for domain, map, range
* The lynch pin is the Morphism and its subclasses, which store and retrieve the identity morphisms indexed
* in all possible permutations of the domain,map,and range of the morphism, the identities. Conceptually, these wrappers
* swim in 2 oceans: The storage ocean and the retrieval sea. For storage, the compareTo and fullCompareTo of Morphism
* needs to account for differing classes and values. For retrieval, a partial template is constructed to retrieve the
* proper Morphism subclass and partially construct a morphism, a so-called 'representable' to partially
* order the result set. The representable operator allows us to go from Cat->Set. Specifically to 'poset'.<br/>
* The critical element about retrieving relationships is to remember that the number of elements from each passed
* iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
* if we declare findHeadSet("*","?","*") we get back a Comparable[] of one element, for findSet("?",object,"?") we
* would get back a Comparable[2] array, with each element of the relationship returned.<br/>
* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
* the requirement to be 'categorical'. In general, all 3 element arrays returned by the Cat->set representable operators are
* the mathematical identity, or constitute the unique key in database terms.
* Some of this work is based on a DBMS described by Alfonso F. Cardenas and Dennis McLeod (1990). Research Foundations 
* in Object-Oriented and Semantic Database Systems. Prentice Hall.
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism
* @author jg, Groff (C) NeoCoreTechs 1997,2013,2014,2015
*/
public final class Relatrix {
	private static boolean DEBUG = false;
	private static boolean DEBUGREMOVE = true;
	private static boolean TRACE = true;
	
	public static char OPERATOR_WILDCARD_CHAR = '*';
	public static char OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	
	private static TransactionalTreeSet[] transactionTreeSets = new TransactionalTreeSet[6];

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
		BigSackAdapter.setTableSpaceDir(path);
	}
	
	public static synchronized String getTableSpaceDirectory() {
		return BigSackAdapter.getTableSpaceDir();
	}
	/**
	 * We cant reasonably check the validity. Set the path to the remote directory that contains the
	 * BigSack tablespaces that comprise our database on remote nodes.
	 * @param path
	 * @throws IOException
	 */
	public static synchronized void setRemoteDirectory(String path) {
		BigSackAdapter.setRemoteDir(path);
	}
	
	public static synchronized String getRemoteDirectory() {
		return BigSackAdapter.getRemoteDir();
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

	Morphism dmr = new DomainMapRange(d,m,r);
	Morphism identity = dmr;
	if( transactionTreeSets[0] == null ) {
		transactionTreeSets[0] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	DomainRangeMap drm = new DomainRangeMap(d,m,r);
	if( transactionTreeSets[1] == null ) {
		transactionTreeSets[1] = BigSackAdapter.getBigSackSetTransaction(drm);
	}
	MapDomainRange mdr = new MapDomainRange(d,m,r);
	if( transactionTreeSets[2] == null ) {
		transactionTreeSets[2] = BigSackAdapter.getBigSackSetTransaction(mdr);
	}
	MapRangeDomain mrd = new MapRangeDomain(d,m,r);
	if( transactionTreeSets[3] == null ) {
		transactionTreeSets[3] = BigSackAdapter.getBigSackSetTransaction(mrd);
	}
	RangeDomainMap rdm = new RangeDomainMap(d,m,r);
	if( transactionTreeSets[4] == null ) {
		transactionTreeSets[4] = BigSackAdapter.getBigSackSetTransaction(rdm);
	}
	RangeMapDomain rmd = new RangeMapDomain(d,m,r);
	if( transactionTreeSets[5] == null ) {
		transactionTreeSets[5] = BigSackAdapter.getBigSackSetTransaction(rmd);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing dmr:"+dmr);
	// check for domain/map match
	// Enforce categorical structure; domain->map function uniquely determines range.
	// If the search winds up at the key or the key is empty or the domain->map exists, the key
	// cannot be inserted.
	((DomainMapRange)dmr).setUniqueKey(true);
	TreeSearchResult tsr = transactionTreeSets[0].locate(dmr);
	((DomainMapRange)dmr).setUniqueKey(false);
	if( DEBUG )
		System.out.println("Relatrix.store Tree Search Result: "+tsr);
	if(tsr.atKey) {
			throw new DuplicateKeyException(d, m);
	}
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing drm:"+dmr);
	transactionTreeSets[0].add(dmr);
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing drm:"+drm);
	transactionTreeSets[1].add(drm);
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing mdr:"+mdr);
	transactionTreeSets[2].add(mdr);

	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing mrd:"+mrd);
	transactionTreeSets[3].add(mrd);

	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing rdm:"+rdm);
	transactionTreeSets[4].add(rdm);
	
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing rmd:"+rmd);
	transactionTreeSets[5].add(rmd);
	
	return (DomainMapRange) identity;
}
/**
 * Commit the outstanding transaction data in each active transactional treeset.
 * @throws IOException
 */
public static synchronized void transactionCommit() throws IOException {
	if( transactionTreeSets[0] == null ) {
		return;
	}
	for(int i = 0; i < transactionTreeSets.length; i++) {
		long startTime = System.currentTimeMillis();
		if( DEBUG || TRACE )
			System.out.println("Committing treeSet "+transactionTreeSets[i].getDBName());
		transactionTreeSets[i].commit();
		if( DEBUG || TRACE )
			System.out.println("Committed treeSet "+transactionTreeSets[i].getDBName() + " in " + (System.currentTimeMillis() - startTime) + "ms.");		
		transactionTreeSets[i] = null;
	}
}
/**
 * Roll back all outstanding transactions on the indicies
 * @throws IOException
 */
public static synchronized void transactionRollback() throws IOException {
	if( transactionTreeSets[0] == null ) {
		return;
	}
	for(int i = 0; i < transactionTreeSets.length; i++) {
		transactionTreeSets[i].rollback();
		transactionTreeSets[i] = null;
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
	if( transactionTreeSets[0] == null ) {
		return;
	}
	for(int i = 0; i < transactionTreeSets.length; i++) {
		transactionTreeSets[i].checkpoint();
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
	} catch(RuntimeException re) { re.printStackTrace();}
	try {
		Iterator<?> it = findSet("*",c,"*");
		while(it.hasNext()) {
			Comparable[] o = (Comparable[]) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove iterated perm 2 "+o[0]+" of type "+o[0].getClass().getName());
			m.add((Morphism) o[0]); 
		}
	} catch(RuntimeException re) {re.printStackTrace();}
	try {
		Iterator<?> it = findSet("*","*",c);
		while(it.hasNext()) {
			Comparable[] o = (Comparable[]) it.next();
			if( DEBUG || DEBUGREMOVE )
				System.out.println("Relatrix.remove iterated perm 3 "+o[0]+" of type "+o[0].getClass().getName());
			m.add((Morphism) o[0]); 
		}
	} catch(RuntimeException re) { re.printStackTrace(); }
	for(Morphism mo : m) {
		if( DEBUG || DEBUGREMOVE)
			System.out.println("Relatrix.remove removing"+mo);
		remove(mo.domain, mo.map, mo.range);
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
	if( transactionTreeSets[0] == null ) {
		transactionTreeSets[0] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	DomainRangeMap drm = new DomainRangeMap(d,m,r);
	if( transactionTreeSets[1] == null ) {
		transactionTreeSets[1] = BigSackAdapter.getBigSackSetTransaction(drm);
	}
	MapDomainRange mdr = new MapDomainRange(d,m,r);
	if( transactionTreeSets[2] == null ) {
		transactionTreeSets[2] = BigSackAdapter.getBigSackSetTransaction(mdr);
	}
	MapRangeDomain mrd = new MapRangeDomain(d,m,r);
	if( transactionTreeSets[3] == null ) {
		transactionTreeSets[3] = BigSackAdapter.getBigSackSetTransaction(mrd);
	}
	RangeDomainMap rdm = new RangeDomainMap(d,m,r);
	if( transactionTreeSets[4] == null ) {
		transactionTreeSets[4] = BigSackAdapter.getBigSackSetTransaction(rdm);
	}
	RangeMapDomain rmd = new RangeMapDomain(d,m,r);
	if( transactionTreeSets[5] == null ) {
		transactionTreeSets[5] = BigSackAdapter.getBigSackSetTransaction(rmd);
	}
	if( DEBUG  )
		System.out.println("Relatrix.remove removing dmr:"+dmr);
	transactionTreeSets[0].remove(dmr);
	
	if( DEBUG  )
		System.out.println("Relatrix.remove removing drm:"+drm);
	transactionTreeSets[1].remove(drm);

	if( DEBUG  )
		System.out.println("Relatrix.remove removing mdr:"+mdr);
	transactionTreeSets[2].remove(mdr);

	if( DEBUG  )
		System.out.println("Relatrix.remove removing mrd:"+mrd);
	transactionTreeSets[3].remove(mrd);

	if( DEBUG  )
		System.out.println("Relatrix.remove removing rdm:"+rdm);
	transactionTreeSets[4].remove(rdm);
	
	if( DEBUG  )
		System.out.println("Relatrix.remove removing rmd:"+rmd);
	transactionTreeSets[5].remove(rmd);
	
}


/**
* Retrieve from the targeted relationship those elements from the relationship to the end of relationships
* matching the given set of operators and/or objects. Essentially this is the default permutation which
* retrieves the equivalent of a tailSet and the parameters can be objects and/or operators. Semantically,
* the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
* The returned Comparable[] array is always of dimension n="# of question marks" or a one element array of a single object.
* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
* relationships of the 3 objects, of the type DomainMapRange. 
* The returned elements(s) constitute identities in the sense of these morphisms satisfying
* the requirement to be 'categorical'. In general, all 3 element arrays return by the Cat->set representable operators are
* the mathematical identity, or constitute the unique key in database terms.
* @param darg Object for domain of relationship or a class template
* @param marg Object for the map of relationship or a class template
* @param rarg Object for the range of the relationship or a class template
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
* matching the given set of operators and/or objects.
* The parameters can be objects and/or operators. Semantically,
* this set-based retrieval makes no sense without at least one object to supply a value to
* work against, so in this method that check is performed. If you are going to anchor a set
* retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
* @param darg Object for domain of relationship or a class template
* @param marg Object for the map of relationship or a class template
* @param rarg Object for the range of the relationship or a class template
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
 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
 * work against, so in this method that check is performed in the createHeadsetFactory method. If you are going to anchor a set
 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
 * @param darg Domain of morphism
 * @param marg Map of morphism relationship
 * @param rarg Range or codomain or morphism relationship
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
 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
 * arguments to the ending point of the associated variable number of parameters, which must match the number of objects
 * passed in the first three arguments. If a passed argument in the first 3 parameters is neither "*" (wildcard)
 * or "?" (return the object from the retrieved tuple morphism) then it is presumed to be an object.
 * Semantically, this set-based retrieval makes no sense without at least one object to supply a value to
 * work against, so in this method that check is performed. If you are going to anchor a set
 * retrieval and declare it a 'head' or 'tail' relative to an object, you need a concrete object to assert that retrieval.
 * Since this is a subset operation, the additional constraint is applied that the ending declaration of the subset retrieval
 * must match the number of concrete objects vs wildcards in the first part of the declaration.
 * @param darg The domain of the relationship to retrieve
 * @param marg The map of the relationship to retrieve
 * @param rarg The range or codomain of the relationship
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

}

