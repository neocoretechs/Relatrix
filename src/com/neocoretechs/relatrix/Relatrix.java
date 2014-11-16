package com.neocoretechs.relatrix;
import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

import com.neocoretechs.bigsack.session.BufferedTreeMap;
import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.forgetfulfunctor.TemplateClass;
import com.neocoretechs.relatrix.iterator.IteratorFactory;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;

/**
* Wrapper for structural (relationship) morphism identity objects and the forgetful functor operators that retrieve them<dd>
* Utilizes the DMRStruc subclasses which contain reference for domain, map, range
* The lynch pin is the DMRStruc and its subclasses, which store and retrieve the identity morphisms indexed
* in all possible permutations of the domain,map,and range of the morphism. Conceptually, these wrappers
* swim in 2 oceans: The storage ocean and the retrieval sea. For storage the compareTo and fullCompareTo of DMRStruc
* needs to account for differing classes and values. For retrieval a partial template is constructed to retrieve the
* proper DMRStruc subclass and partially construct a morphism, a so-called 'forgetful functor' to partially
* order the result set, although it may be more of a 'representable' of some sort, perhaps it has no analog. It allows
* us to go from Cat->Set.
* Some of this work is based on a DBMS described by Alfonso F. Cardenas and Dennis McLeod (1990). Research Foundations 
* in Object-Oriented and Semantic Database Systems. Prentice Hall.
* See also Category Theory, Set theory, morphisms, functors, function composition, group homomorphism
* @author Groff (C) NeoCoreTechs 1997, 2013
*/
public class Relatrix {
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
	
	public static void setTablespaceDirectory(String path) throws IOException {
		File p = new File(path);
		if(!p.isDirectory())
			throw new IOException("Cannot access directory "+path+" for tablespace");
		BigSackAdapter.setTableSpaceDir(path);
	}
	
	public static String getTableSpaceDirectory(String path) {
		return BigSackAdapter.getTableSpaceDir();
	}

/**
 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes
 * @param d
 * @param m
 * @param r
 * @throws IllegalAccessException
 * @throws IOException
 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
 */
public static DomainMapRange store(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException {
	if( transactionTreeSets[0] != null )
		throw new IllegalAccessException("Transactions are active, can not initiate monolithic store");
	DMRStruc dmr = new DomainMapRange(d,m,r);
	DMRStruc identity = dmr;
	BufferedTreeSet btm = BigSackAdapter.getBigSackSet(dmr);
	//if(!btm.getDBName().contains("DomainMapRange")) System.out.println("WRONG :"+btm.getDBName());
	
	btm.add(dmr);
	dmr = new DomainRangeMap(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);
	//if(!btm.getDBName().contains("DomainRangeMap")) System.out.println("WRONG :"+btm.getDBName());

	btm.add(dmr);
	dmr = new MapDomainRange(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);
	//if(!btm.getDBName().contains("MapDomainRange")) System.out.println("WRONG :"+btm.getDBName());

	btm.add(dmr);
	dmr = new MapRangeDomain(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);
	//if(!btm.getDBName().contains("MapRangeDomain")) System.out.println("WRONG :"+btm.getDBName());

	btm.add(dmr);
	dmr = new RangeDomainMap(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	//if(!btm.getDBName().contains("RangeDomainMap")) System.out.println("WRONG :"+btm.getDBName());
	btm.add(dmr);
	dmr = new RangeMapDomain(d,m,r);
	btm = BigSackAdapter.getBigSackSet(dmr);

	//if(!btm.getDBName().contains("RangeMapDomain")) System.out.println("WRONG :"+btm.getDBName());
	btm.add(dmr);
	return (DomainMapRange) identity;
}
/**
 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes
 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
 * to prevent a recovery on the next operation
 * @param d
 * @param m
 * @param r
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
	transactionTreeSets[0].add(dmr);
	//if(!transactionTreeSets[0].getDBName().contains("DomainMapRange")) System.out.println("WRONG :"+transactionTreeSets[0].getDBName());
	
	dmr = new DomainRangeMap(d,m,r);
	//btm = BigSackAdapter.getBigSackSet(dmr);
	if( transactionTreeSets[1] == null ) {
		transactionTreeSets[1] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	transactionTreeSets[1].add(dmr);

	dmr = new MapDomainRange(d,m,r);
	//btm = BigSackAdapter.getBigSackSet(dmr);
	if( transactionTreeSets[2] == null ) {
		transactionTreeSets[2] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	transactionTreeSets[2].add(dmr);

	dmr = new MapRangeDomain(d,m,r);
	//btm = BigSackAdapter.getBigSackSet(dmr);
	if( transactionTreeSets[3] == null ) {
		transactionTreeSets[3] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	transactionTreeSets[3].add(dmr);

	dmr = new RangeDomainMap(d,m,r);
	//btm = BigSackAdapter.getBigSackSet(dmr);
	if( transactionTreeSets[4] == null ) {
		transactionTreeSets[4] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	transactionTreeSets[4].add(dmr);

	dmr = new RangeMapDomain(d,m,r);
	//btm = BigSackAdapter.getBigSackSet(dmr);
	if( transactionTreeSets[5] == null ) {
		transactionTreeSets[5] = BigSackAdapter.getBigSackSetTransaction(dmr);
	}
	transactionTreeSets[5].add(dmr);

	return (DomainMapRange) identity;
}
/**
 * Commit the outstanding indicies to their transactional data
 * @throws IOException
 */
public static void transactionCommit() throws IOException {
	if( transactionTreeSets[0] == null ) {
		return;
	}
	for(int i = 0; i < 6; i++) {
		transactionTreeSets[i].commit();
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
	for(int i = 0; i < 6; i++) {
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
	for(int i = 0; i < 6; i++) {
		transactionTreeSets[i].checkpoint();
	}
}
/**
* recursively delete all relationships that this object participates in
* @exception IOException low-level access or problems modifiying schema
*/
public synchronized void remove(Comparable c) throws IOException
{

}
/**
 * Delete specific relationship and all relationships that it participates in
 * @param d
 * @param m
 * @param r
 */
public synchronized void remove(Comparable d, Comparable m, Comparable r) {
	
}
//throw new NonPersistentObjectException("attempt to specify the range of a relationship with a non-persistent object");
//throw new NonPersistentObjectException("attempt to specify the map of a relationship with a non-persistent object");
//throw new NonPersistentObjectException("attempt to specify the domain of a relationship with a non-persistent object");


/**
* When presented with 3 objects, the options are to return an identity composed of those 3 or
* a set composed of identity elements matching the class of the template(s) in the argument(s)
* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
* @param darg Object for domain of relationship or a class template
* @param marg Object for the map of relationship or a class template
* @param rarg Object for the range of the relationship or a class template
* @exception IOException low-level access or problems modifiying schema
* @exception IllegalArgumentException the operator is invalid
* @exception ClassNotFoundException if the Class of Object is invalid
* @throws IllegalAccessException 
* @return The iterator for the set of returned objects
*/
public static synchronized Iterator<?> findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
{
	IteratorFactory ifact = IteratorFactory.createFactory(darg, marg, rarg);
	return ifact.createIterator();
}



}

