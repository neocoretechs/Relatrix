package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Helper routines to be used with headset, subset, tailset to populate a TreeMap with DBKeys ordered by indexes in 
 * three arraylists designated dkey, mkey and rkey for domain key, map key and range key, from a range of Morphisms.<p/>
 * The Morphisms are designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
 * ordered positions in the 3 domain, map and range key arrays based on indexOf each Morphism component
 * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap. The TreeMap then
 * becomes the basis for the iterator or stream that delivers the results.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class FindsetUtil {
	private static boolean DEBUGITERATION = false;
    /**
     * Populate the TreeMap param with DBKeys ordered by indexes in dkey, mkey and rkey from the range of Morphisms
     * designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
     * ordered positions in the 3 domain, map and range key arrays based on indexOf each Morphism component
     * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap.
     * @param xdmr lower bound for Morphism search
     * @param ydmr upper bound for Morphism search
     * @param dkey ArrayList of domain keys in order based on endargs from findSet
     * @param mkey ArrayList of map key in order based on endargs from findSet
     * @param rkey ArrayList of range keys in order based on endargs from findSet
     * @param resultSet TreeMap to be populated with Morphism primary key DBKeys ordered by Result3 of indexOf in dkey, mkey, and rkey arrays
     * @throws IOException
     */
    public static void getMorphismRange(Morphism xdmr, Morphism ydmr, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in Morphism relation, and primary key to said Morphism
    		RelatrixKV.findTailMapKVStream(xdmr).forEach(e ->{
    			Map.Entry<Morphism,DBKey> m = (Map.Entry<Morphism,DBKey>)e;
    			Morphism m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			if(m0.compareTo(ydmr) > 0)
    				return;
    			Result3 r = new Result3();
    			boolean insert = true;
    			r.set(0,0);
    			if(dkey.size() > 0) {
    				// does our Morphism domain key exist in headSet of designated headset domain objects, if any?
    				int insd = dkey.indexOf(m0.getDomainKey());
    				// no, this Morphism is not eligible
    				if(insd == -1)
    					insert = false;
    				else
    					// yes, set result index 0 to sort position of domain headset list key
    					r.set(0,insd);
    			}
    			r.set(1,0);
    			if(mkey.size() > 0 && insert) { // should we check map, and is this Morphism still eligible?
    				int insm = mkey.indexOf(m0.getMapKey());
    				if(insm == -1)
    					insert = false;
    				else
    					r.set(1,insm);
    			}
    			r.set(2,0);
    			if(rkey.size() > 0 && insert) {
    				int insr = rkey.indexOf(m0.getRangeKey());
    				if(insr == -1)
    					insert = false;
    				else
    					r.set(2,insr);
    			}
    			// now we have whether we should insert the primary key DBKey for this Morphism and a Result3 with ordering indexes
    			// if we skipped any indexes in result3, they should be 0
    			if(insert) {
    				synchronized(resultSet) {
    					resultSet.put(r, m.getValue());
    				}
    			}
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap param with DBKeys ordered by indexes in dkey, mkey and rkey from the range of Morphisms
     * designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
     * ordered positions in the 3 domain, map and range key arrays based on indexOf each Morphism component
     * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap.
     * @param alias the database alias
     * @param xdmr lower bound for Morphism search
     * @param ydmr upper bound for Morphism search
     * @param dkey ArrayList of domain keys in order based on endargs from findSet
     * @param mkey ArrayList of map key in order based on endargs from findSet
     * @param rkey ArrayList of range keys in order based on endargs from findSet
     * @param resultSet TreeMap to be populated with Morphism primary key DBKeys ordered by Result3 of indexOf in dkey, mkey, and rkey arrays
     * @throws IOException
     */
    public static void getMorphismRange(Alias alias, Morphism xdmr, Morphism ydmr, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in Morphism relation, and primary key to said Morphism
    		RelatrixKV.findTailMapKVStream(alias,xdmr).forEach(e ->{
    			Map.Entry<Morphism,DBKey> m = (Map.Entry<Morphism,DBKey>)e;
     			Morphism m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setAlias(alias);
    			if(m0.compareTo(ydmr) > 0)
    				return;
    			Result3 r = new Result3();
    			boolean insert = true;
    			r.set(0,0);
    			if(dkey.size() > 0) {
    				// does our Morphism domain key exist in headSet of designated headset domain objects, if any?
    				int insd = dkey.indexOf(m0.getDomainKey());
    				// no, this Morphism is not eligible
    				if(insd == -1)
    					insert = false;
    				else
    					// yes, set result index 0 to sort position of domain headset list key
    					r.set(0,insd);
    			}
    			r.set(1,0);
    			if(mkey.size() > 0 && insert) { // should we check map, and is this Morphism still eligible?
    				int insm = mkey.indexOf(m0.getMapKey());
    				if(insm == -1)
    					insert = false;
    				else
    					r.set(1,insm);
    			}
    			r.set(2,0);
    			if(rkey.size() > 0 && insert) {
    				int insr = rkey.indexOf(m0.getRangeKey());
    				if(insr == -1)
    					insert = false;
    				else
    					r.set(2,insr);
    			}
    			// now we have whether we should insert the primary key DBKey for this Morphism and a Result3 with ordering indexes
    			// if we skipped any indexes in result3, they should be 0
    			if(insert) {
    				synchronized(resultSet) {
    					resultSet.put(r, m.getValue());
    				}
    			}
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | NoSuchElementException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap param with DBKeys ordered by indexes in dkey, mkey and rkey from the range of Morphisms
     * designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
     * ordered positions in the 3 domain, map and range key arrays based on indexOf each Morphism component
     * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap.
     * @param xid the transaction id
     * @param xdmr lower bound for Morphism search
     * @param ydmr upper bound for Morphism search
     * @param dkey ArrayList of domain keys in order based on endargs from findSet
     * @param mkey ArrayList of map key in order based on endargs from findSet
     * @param rkey ArrayList of range keys in order based on endargs from findSet
     * @param resultSet TreeMap to be populated with Morphism primary key DBKeys ordered by Result3 of indexOf in dkey, mkey, and rkey arrays
     * @throws IOException
     */
    public static void getMorphismRangeTransaction(TransactionId xid, Morphism xdmr, Morphism ydmr, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in Morphism relation, and primary key to said Morphism
    		RelatrixKVTransaction.findTailMapKVStream(xid,xdmr).forEach(e ->{
    			Map.Entry<Morphism,DBKey> m = (Map.Entry<Morphism,DBKey>)e;
      			Morphism m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setTransactionId(xid);
    			if(m0.compareTo(ydmr) > 0)
    				return;
    			Result3 r = new Result3();
    			boolean insert = true;
    			r.set(0,0);
    			if(dkey.size() > 0) {
    				// does our Morphism domain key exist in headSet of designated headset domain objects, if any?
    				int insd = dkey.indexOf(m0.getDomainKey());
    				// no, this Morphism is not eligible
    				if(insd == -1)
    					insert = false;
    				else
    					// yes, set result index 0 to sort position of domain headset list key
    					r.set(0,insd);
    			}
    			r.set(1,0);
    			if(mkey.size() > 0 && insert) { // should we check map, and is this Morphism still eligible?
    				int insm = mkey.indexOf(m0.getMapKey());
    				if(insm == -1)
    					insert = false;
    				else
    					r.set(1,insm);
    			}
    			r.set(2,0);
    			if(rkey.size() > 0 && insert) {
    				int insr = rkey.indexOf(m0.getRangeKey());
    				if(insr == -1)
    					insert = false;
    				else
    					r.set(2,insr);
    			}
    			// now we have whether we should insert the primary key DBKey for this Morphism and a Result3 with ordering indexes
    			// if we skipped any indexes in result3, they should be 0
    			if(insert) {
    				synchronized(resultSet) {
    					resultSet.put(r, m.getValue());
    				}
    			}
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap param with DBKeys ordered by indexes in dkey, mkey and rkey from the range of Morphisms
     * designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
     * ordered positions in the 3 domain, map and range key arrays based on indexOf each Morphism component
     * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap.
     * @param alias the database alias
     * @param xid the transaction id
     * @param xdmr lower bound for Morphism search
     * @param ydmr upper bound for Morphism search
     * @param dkey ArrayList of domain keys in order based on endargs from findSet
     * @param mkey ArrayList of map key in order based on endargs from findSet
     * @param rkey ArrayList of range keys in order based on endargs from findSet
     * @param resultSet TreeMap to be populated with Morphism primary key DBKeys ordered by Result3 of indexOf in dkey, mkey, and rkey arrays
     * @throws IOException
     */
    public static void getMorphismRangeTransaction(Alias alias, TransactionId xid, Morphism xdmr, Morphism ydmr, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in Morphism relation, and primary key to said Morphism
    		RelatrixKVTransaction.findTailMapKVStream(alias,xid,xdmr).forEach(e ->{
    			Map.Entry<Morphism,DBKey> m = (Map.Entry<Morphism,DBKey>)e;
      			Morphism m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setAlias(alias);
    			m0.setTransactionId(xid);
    			if(m0.compareTo(ydmr) > 0)
    				return;
    			Result3 r = new Result3();
    			boolean insert = true;
    			r.set(0,0);
    			if(dkey.size() > 0) {
    				// does our Morphism domain key exist in headSet of designated headset domain objects, if any?
    				int insd = dkey.indexOf(m0.getDomainKey());
    				// no, this Morphism is not eligible
    				if(insd == -1)
    					insert = false;
    				else
    					// yes, set result index 0 to sort position of domain headset list key
    					r.set(0,insd);
    			}
    			r.set(1,0);
    			if(mkey.size() > 0 && insert) { // should we check map, and is this Morphism still eligible?
    				int insm = mkey.indexOf(m0.getMapKey());
    				if(insm == -1)
    					insert = false;
    				else
    					r.set(1,insm);
    			}
    			r.set(2,0);
    			if(rkey.size() > 0 && insert) {
    				int insr = rkey.indexOf(m0.getRangeKey());
    				if(insr == -1)
    					insert = false;
    				else
    					r.set(2,insr);
    			}
    			// now we have whether we should insert the primary key DBKey for this Morphism and a Result3 with ordering indexes
    			// if we skipped any indexes in result3, they should be 0
    			if(insert) {
    				synchronized(resultSet) {
    					resultSet.put(r, m.getValue());
    				}
    			}
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
	public static Result iterateDmr(Morphism buffer, boolean identity, short[] dmr_return) throws IllegalAccessException, IOException {
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
