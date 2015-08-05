package com.neocoretechs.relatrix;
import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BufferedTreeMap;
import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.typedlambda.TemplateClass;

/**
* Wrapper for structural (relationship) morphism identity objects and the representable operators that retrieve them<dd>
* Utilizes the DMRStruc subclasses which contain reference for domain, map, range
* The lynch pin is the DMRStruc and its subclasses, which store and retrieve the identity morphisms indexed
* in all possible permutations of the domain,map,and range of the morphism, the identities. Conceptually, these wrappers
* swim in 2 oceans: The storage ocean and the retrieval sea. For storage, the compareTo and fullCompareTo of DMRStruc
* needs to account for differing classes and values. For retrieval, a partial template is constructed to retrieve the
* proper DMRStruc subclass and partially construct a morphism, a so-called 'representable' to partially
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
* @author jg, Groff (C) NeoCoreTechs 1997, 2013,2014,2015
*/
public final class Relatrix {
	public static char OPERATOR_WILDCARD_CHAR = '*';
	public static char OPERATOR_TUPLE_CHAR = '?';
	public static String OPERATOR_WILDCARD = String.valueOf(OPERATOR_WILDCARD_CHAR);
	public static String OPERATOR_TUPLE = String.valueOf(OPERATOR_TUPLE_CHAR);
	
	private static TransactionalTreeSet[] transactionTreeSets = new TransactionalTreeSet[6];
	private static boolean DEBUG = false;
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
	 * Verify that we are specifying a dir
	 * @param path
	 * @throws IOException
	 */
	public static void setTablespaceDirectory(String path) throws IOException {
		File p = new File(path);
		if(!new File(p.getParent()).isDirectory())
			throw new IOException("Cannot set tablespace directory for fileset "+path+" to allocate persistent storage.");
		BigSackAdapter.setTableSpaceDir(path);
	}
	
	public static String getTableSpaceDirectory() {
		return BigSackAdapter.getTableSpaceDir();
	}
	/**
	 * We cant reasonably check the validity. Set the path to the remote directory that contains the
	 * BigSack tablespaces that comprise our database.
	 * @param path
	 * @throws IOException
	 */
	public static void setRemoteDirectory(String path) {
		BigSackAdapter.setRemoteDir(path);
	}
	
	public static String getRemoteDirectory() {
		return BigSackAdapter.getRemoteDir();
	}
/**
 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
 * This is a standalone store in an atomic transparent transaction. Disallowed in transaction mode.
 * @param d The Comparable representing the domain object for this morphism relationship.
 * @param m The Comparable representing the map object for this morphism relationship.
 * @param r The Comparable representing the range or codomain object for this morphism relationship.
 * @throws IllegalAccessException
 * @throws IOException
 * @return The identity morphism relationship element - The DomainMapRange of stored object composed of d,m,r
 */
public static DomainMapRange store(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException {
	if( transactionTreeSets[0] != null )
		throw new IllegalAccessException("Transactions are active, can not initiate monolithic store");
	DMRStruc dmr = new DomainMapRange(d,m,r);
	DMRStruc identity = dmr;
	BufferedTreeSet btm = BigSackAdapter.getBigSackSet(dmr);

	btm.add(dmr);
	dmr = new DomainRangeMap(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	btm.add(dmr);
	dmr = new MapDomainRange(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	btm.add(dmr);
	dmr = new MapRangeDomain(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	btm.add(dmr);
	dmr = new RangeDomainMap(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	btm.add(dmr);
	dmr = new RangeMapDomain(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	btm.add(dmr);
	return (DomainMapRange) identity;
}
/**
 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
 * This is a transactional store in the context of a previously initiated transaction.
 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
 * to prevent a recovery on the next operation
 * @param d The Comparable representing the domain object for this morphism relationship.
 * @param m The Comparable representing the map object for this morphism relationship.
 * @param r The Comparable representing the range or codomain object for this morphism relationship.
 * @throws IllegalAccessException
 * @throws IOException
 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
 */
public static DomainMapRange transactionalStore(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException {

	DMRStruc dmr = new DomainMapRange(d,m,r);
	DMRStruc identity = dmr;
	if( transactionTreeSets[0] == null ) {
		transactionTreeSets[0] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing dmr:"+dmr);
	transactionTreeSets[0].add(dmr);
	
	DomainRangeMap drm = new DomainRangeMap(d,m,r);
	if( transactionTreeSets[1] == null ) {
		transactionTreeSets[1] = BigSackAdapter.getBigSackSetTransaction(drm);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing drm:"+dmr);
	transactionTreeSets[1].add(drm);

	MapDomainRange mdr = new MapDomainRange(d,m,r);
	if( transactionTreeSets[2] == null ) {
		transactionTreeSets[2] = BigSackAdapter.getBigSackSetTransaction(mdr);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing mdr:"+dmr);
	transactionTreeSets[2].add(mdr);

	MapRangeDomain mrd = new MapRangeDomain(d,m,r);
	if( transactionTreeSets[3] == null ) {
		transactionTreeSets[3] = BigSackAdapter.getBigSackSetTransaction(mrd);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing:"+mrd);
	transactionTreeSets[3].add(mrd);

	RangeDomainMap rdm = new RangeDomainMap(d,m,r);
	if( transactionTreeSets[4] == null ) {
		transactionTreeSets[4] = BigSackAdapter.getBigSackSetTransaction(rdm);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing:"+rdm);
	transactionTreeSets[4].add(rdm);

	RangeMapDomain rmd = new RangeMapDomain(d,m,r);
	if( transactionTreeSets[5] == null ) {
		transactionTreeSets[5] = BigSackAdapter.getBigSackSetTransaction(rmd);
	}
	if( DEBUG  )
		System.out.println("Relatrix.transactionalStore storing:"+rmd);
	transactionTreeSets[5].add(rmd);
	
	return (DomainMapRange) identity;
}
/**
 * Commit the outstanding indicies to their transactional data.
 * @throws IOException
 */
public static void transactionCommit() throws IOException {
	if( transactionTreeSets[0] == null ) {
		return;
	}
	for(int i = 0; i < transactionTreeSets.length; i++) {
		long startTime = System.currentTimeMillis();
		System.out.println("Committing treeSet "+transactionTreeSets[i].getDBName());
		transactionTreeSets[i].commit();
		System.out.println("Committed treeSet "+transactionTreeSets[i].getDBName() + " in " + (System.currentTimeMillis() - startTime) + "ms.");		
		transactionTreeSets[i] = null;
	}
}
/**
 * Roll back all outstanding transactions on the indicies
 * @throws IOException
 */
public static void transactionRollback() throws IOException {
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
public static void transactionCheckpoint() throws IOException, IllegalAccessException {
	if( transactionTreeSets[0] == null ) {
		return;
	}
	for(int i = 0; i < transactionTreeSets.length; i++) {
		transactionTreeSets[i].checkpoint();
	}
}
/**
* recursively delete all relationships that this object participates in
* @exception IOException low-level access or problems modifiying schema
*/
public synchronized void remove(Comparable c) throws IOException
{
	throw new RuntimeException("Not implemented yet");
}
/**
 * Delete specific relationship and all relationships that it participates in
 * @param d
 * @param m
 * @param r
 */
public synchronized void remove(Comparable d, Comparable m, Comparable r) {
	throw new RuntimeException("Not implemented yet");	
}
//throw new NonPersistentObjectException("attempt to specify the range of a relationship with a non-persistent object");
//throw new NonPersistentObjectException("attempt to specify the map of a relationship with a non-persistent object");
//throw new NonPersistentObjectException("attempt to specify the domain of a relationship with a non-persistent object");

/**
* Retrieve from the targeted relationship those elements from the relationship to the end of relationships
* matching the given set of operators and/or objects. Essentially this is the default permutation which
* retrieves the equivalent of a tailSet and the parameters can be objects and/or operators. Semantically,
* the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
* In support of the typed lambda calculus, When presented with 3 objects, the options are to return an identity composed of those 3 or
* a set composed of identity elements matching the class of the template(s) in the argument(s)
* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
* the requirement to be 'categorical'. In general, all 3 element arraysw return by the Cat->set representable operators are
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
* work against, so in this method that check is performed.
* In support of the typed lambda calculus, When presented with 3 objects, the options are to return a
* a set composed of elements matching the class of the template(s) in the argument(s)
* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
* @param darg Object for domain of relationship or a class template
* @param marg Object for the map of relationship or a class template
* @param rarg Object for the range of the relationship or a class template
* @exception IOException low-level access or problems modifiying schema
* @exception IllegalArgumentException the operator is invalid
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
 * work against, so in this method that check is performed.
 * @param darg Domain of morphism
 * @param marg Map of morphism relationship
 * @param rarg Range or codomain or morphism relationship
 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
 * @throws IOException
 * @throws IllegalArgumentException
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
 * work against, so in this method that check is performed.
 * @param darg The domain of the relationship to retrieve
 * @param marg The map of the relationship to retrieve
 * @param rarg The range or codomain of the relationship
 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
 * @return The RelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
 * @throws IOException
 * @throws IllegalArgumentException
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

