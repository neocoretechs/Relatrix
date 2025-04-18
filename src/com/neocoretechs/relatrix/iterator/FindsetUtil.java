package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.key.DBKey;


/**
 * Helper routines to be used with headset, subset, tailset to populate a TreeMap with DBKeys ordered by indexes in 
 * three arraylists designated dkey, mkey and rkey for domain key, map key and range key, from a range of Morphisms.<p/>
 * The Morphisms are designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
 * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
 * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap. The TreeMap then
 * becomes the basis for the iterator or stream that delivers the results.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class FindsetUtil {
	private static boolean DEBUG = false;
	private static boolean DEBUGITERATION = false;
	
	/**
	 * populate the TreeMap with DBKeys ordered by indexes in 
	 * three arraylists designated dkey, mkey and rkey for domain key, map key and range key, from a range of Morphisms.<p/>
	 * The Morphisms are passed in the m0 parameter. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap. Process
	 * the m0 AbstractRelation to create the entry. The TreeMap then
	 * becomes the basis for the iterator or stream that delivers the results.
	 * @param m0 The iterated AbstractRelation to process against the three DBKey index arrays
	 * @param dkey The domain key array of instance ordered DBKeys
	 * @param mkey The map key array of instance ordered DBKeys
	 * @param rkey The range key array of instance ordered DBKeys
	 * @param resultSet The treemap to be populated with Result3 post-ordering indexes
	 */
    private static void createResultSet(AbstractRelation m0, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) {
			Result3 r = new Result3();
			boolean insert = false;
			int insd = -1;
			int insm = -1;
			int insr = -1;
			if(dkey.size() > 0) {
				// does our AbstractRelation domain key exist in headSet of designated headset domain objects, if any?
				insd = dkey.indexOf(m0.getDomainKey());
				// no, this AbstractRelation is not eligible
				if(insd != -1) {
					insert = true;
					// yes, set result index 0 to sort position of domain headset list key
					r.set(0,insd);
				}
			}
			if(mkey.size() > 0) { // should we check map, and is this AbstractRelation still eligible?
				if(insert) {
					insm = mkey.indexOf(m0.getMapKey());
					if(insm != -1) {
						r.set(1,insm);
					} else {
						insert = false;
					}
				}
			} else {
				insert = false;
			}
			if(rkey.size() > 0) {
				if(insert) {
					insr = rkey.indexOf(m0.getRangeKey());
					if(insr != -1) {
						r.set(2,insr);
					} else {
						insert = false;
					}
				}
			} else {
				insert = false;
			}
			// now we have whether we should insert the primary key DBKey for this AbstractRelation and a Result3 with ordering indexes
			// if we skipped any indexes in result3, they should be 0
			if(insert) {
				synchronized(resultSet) {
					resultSet.put(r, m0.getIdentity());
				}
			}
			//if(DEBUG)
			//	System.out.printf("FindSetUtil.createResultSet %d %d %d %s %b%n",insd,insm,insr,r,insert);
    }
    
    /**
     * Populate the TreeMap with the Relation morphisms in the range of the DBKey low and hi ranges provided.
     * If we find the 3 morphism keys in the arrays of domain, map, and range keys we built, they are eligible for the final post-order set.
	 * <p/>
	 * The low range AbstractRelation template is formed from the 3 low keys. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap as
	 * each morphism in range is streamed to the createResultSet method.
     * @param dkeyLo
     * @param mkeyLo
     * @param rkeyLo
     * @param dkeyHi
     * @param mkeyHi
     * @param rkeyHi
     * @param dkey
     * @param mkey
     * @param rkey
     * @param resultSet
     * @throws IOException
     */
    public static void getMorphismRange(DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		if(DEBUG) {
    			System.out.println("getMorphismRange tailMap from:"+dkeyLo+" "+mkeyLo+" "+rkeyLo+" to:"+dkeyHi+" "+mkeyHi+" "+rkeyHi);
    			//"\r\nDomain Array:\r\n"+Arrays.toString(dkey.toArray())+"\r\nMap array\r\n"+Arrays.toString(mkey.toArray())+"\r\nRange array\r\n"+Arrays.toString(rkey.toArray()));
    			/*
    			System.out.println("Domain array:");
    			for(DBKey d: dkey) {
    				System.out.println(RelatrixKV.get(d));
    			}
    			System.out.println("Map array:");
    			for(DBKey m: mkey) {
    				System.out.println(RelatrixKV.get(m));
    			}
    			System.out.println("Range array:");
    			for(DBKey r: rkey) {
    				System.out.println(RelatrixKV.get(r));
    			}
    			*/
    		}
    		AbstractRelation xdmr = (AbstractRelation) new Relation(true, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		// stream of DBKeys in AbstractRelation relation
    		RelatrixKV.findTailMapKVStream(xdmr)/*RelatrixKV.entrySetStream(xdmr.getClass())*/.forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
    			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
    			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
			throw new IOException(e);
		}
    }
     
    /**
     * Populate the TreeMap with the Relation morphisms in the range of the DBKey low and hi ranges provided.
     * If we find the 3 morphism keys in the arrays of domain, map, and range keys we built, they are eligible for the final post-order set.
	 * <p/>
	 * The low range AbstractRelation template is formed from the 3 low keys. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap as
	 * each morphism in range is streamed to the createResultSet method.
     * @param alias
     * @param dkeyLo
     * @param mkeyLo
     * @param rkeyLo
     * @param dkeyHi
     * @param mkeyHi
     * @param rkeyHi
     * @param dkey
     * @param mkey
     * @param rkey
     * @param resultSet
     * @throws IOException
     */
    public static void getMorphismRange(Alias alias, DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in AbstractRelation relation
       		AbstractRelation xdmr = (AbstractRelation) new Relation(true, alias, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		RelatrixKV.findTailMapKVStream(alias,xdmr).forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
     			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setAlias(alias);
       			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
      			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | NoSuchElementException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap with the Relation morphisms in the range of the DBKey low and hi ranges provided.
     * If we find the 3 morphism keys in the arrays of domain, map, and range keys we built, they are eligible for the final post-order set.
	 * <p/>
	 * The low range AbstractRelation template is formed from the 3 low keys. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap as
	 * each morphism in range is streamed to the createResultSet method.
     * @param xid
     * @param dkeyLo
     * @param mkeyLo
     * @param rkeyLo
     * @param dkeyHi
     * @param mkeyHi
     * @param rkeyHi
     * @param dkey
     * @param mkey
     * @param rkey
     * @param resultSet
     * @throws IOException
     */
    public static void getMorphismRangeTransaction(TransactionId xid, DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in AbstractRelation relation
       		AbstractRelation xdmr = (AbstractRelation) new Relation(true, xid, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		RelatrixKVTransaction.findTailMapKVStream(xid,xdmr).forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
      			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setTransactionId(xid);
       			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
     			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap param with DBKeys ordered by indexes in dkey, mkey and rkey from the range of Morphisms
     * designated by xdmr bound . The order is created by using the
     * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
     * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap.
     * @param alias the database alias
     * @param xid the transaction id
     * @param xdmr bound for AbstractRelation search
     * @param dkey ArrayList of domain keys in order based on endargs from findSet
     * @param mkey ArrayList of map key in order based on endargs from findSet
     * @param rkey ArrayList of range keys in order based on endargs from findSet
     * @param resultSet TreeMap to be populated with AbstractRelation primary key DBKeys ordered by Result3 of indexOf in dkey, mkey, and rkey arrays
     * @throws IOException
     */
    public static void getMorphismRangeTransaction(Alias alias, TransactionId xid, DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in AbstractRelation relation
       		AbstractRelation xdmr = (AbstractRelation) new Relation(true, alias, xid, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		RelatrixKVTransaction.findTailMapKVStream(alias,xid,xdmr).forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
      			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setAlias(alias);
    			m0.setTransactionId(xid);
       			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
     			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | NoSuchElementException e) {
			throw new IOException(e);
		}
    }
       
	/**
	 * iterate_dmr - return proper domain, map, or range
	 * based on dmr_return values.  In dmr_return, value 0
	 * is iterator for ?,*.  1-3 BOOLean for d,m,r return yes/no
	 * @return the next location to retrieve or null, the only time its null is when we exhaust the buffered tuples
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	public static Result iterateDmr(AbstractRelation buffer, boolean identity, short[] dmr_return) throws IllegalAccessException, IOException {
	    Result tuples = RelatrixIterator.getReturnTuples(dmr_return);
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples.set(0, buffer);
	    	if(DEBUGITERATION)
				System.out.println("RelatrixHeadSetIterator iterateDmr returning identity tuples:"+tuples);
	    	return tuples;
	    }
	    dmr_return[0] = 0;
	    for(int i = 0; i < tuples.length(); i++)
	    	tuples.set(i, buffer.iterate_dmr(dmr_return));
		if(DEBUGITERATION)
			System.out.println("RelatrixHeadSetIterator iterateDmr returning tuples:"+tuples);
		return tuples;
	}
}
