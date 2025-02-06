package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.rocksack.Alias;

/**
 *                                                                                                                                                                                                                                                                                                                                                                         * Instances of this class deliver the set of identity {@link Morphism}s, or
 * Post order the morphisms, which are stored in key order instead of the order of its instances. Find elements strictly less than 'to' target.
 * Populate a series of arrays with the partial ordered sets of classes
 * designated in the suffix of the 'findSet' predicate, then use the min and max range of those to build a range query into
 * the proper table of Morphisms. Extract the domain, map and range components from each retrieved Morphism
 * and determine their index into each domain, map and range arraylist. Use those indexes to form a key using
 * a {@link com.neocoretechs.relatrix.Result} object. Use that key to order a TreeMap entry with the primary key of the
 * retrieved Morphism. The iterator for the findSet then becomes the ordered TreeMap iterator and the primary key is used to retrieve the original
 * Morphism with all its actual payload objects. Ultimately return Result instance elements in next().<p/>
 * Start by retrieving the instances in their natural order based on the suffix of the findSet predicate and build 3 tables of keys
 * that we can relate back to the morphisms. While we do this, gather the low and high key ranges of the potential eligible morphisms.
 * At conclusion, we have a low and high range of potential morphism keys formed from the keys of the instance ordering that we relate back to the morphisms.<p/>
 * These 2 tables and key ranges represent the independent headset orders. At the conclusion of all this, call the 
 * FindSetUtil.getMorphismRange to attempt to match the actual morphisms to the acquired keys using the ranges we obtained and indexing into the
 * 3 tables we formed. In this way, storing only the keys as intermediate elements, we can post-order the morphisms as desired.
 * <p/>
 * For tuples the Result is relative to the '?' query predicates. <br/>
 * Here, the headset is retrieved.<p/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIterator} is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*",[object | Class]) we get back a {@link com.neocoretechs.relatrix.Result1} of one element. 
 * For findHeadSet("?",object,"?",[object | Class],[object | Class]) we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * For each * wildcard or ? return we need a corresponding Class or concrete instance object in the suffix arguments. These objects become the basis
 * for the headset objects returned. As mentioned above, if a Class is specified the entire range of ordered instances is replaced by the ? or *, in the
 * case of a concrete instance, the ordered headset from the beginning to that instance (exclusive) is returned or simply used to order
 * the proceeding element in the suffix as it pertains to the retrieved Morphisms in the case of an * wildcard.<p/>
 * When replacing one of the first 3 selectors with a concrete instance, we perform an exact match on that field. 
 * 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2024
 */
public class RelatrixHeadsetIterator implements Iterator<Result> {
	public static boolean DEBUG = false;
	public static boolean DEBUGITERATION = false;
	protected Alias alias = null;
	protected Iterator<?> iter;
    protected Morphism buffer = null;
    protected Morphism nextit = null;
    protected Morphism base;
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = true;
	protected Morphism template;
    protected boolean identity = false;
    
    protected ArrayList<DBKey> dkey = new ArrayList<DBKey>();
    protected ArrayList<DBKey> mkey = new ArrayList<DBKey>();
    protected ArrayList<DBKey> rkey = new ArrayList<DBKey>();
    
    protected TreeMap<Result,DBKey> resultSet = new TreeMap<Result,DBKey>();
    
    protected DBKey dkeyLo = DBKey.fullDBKey;
    protected DBKey dkeyHi = DBKey.nullDBKey;
    protected DBKey mkeyLo = DBKey.fullDBKey;
    protected DBKey mkeyHi = DBKey.nullDBKey;
    protected DBKey rkeyLo = DBKey.fullDBKey;
    protected DBKey rkeyHi = DBKey.nullDBKey;
   
    public RelatrixHeadsetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template the findset operators and/or concrete object instances from findSet call
     * @param templateo the endargs lower bound original findset call
     * @param dmr_return findSet operator order and tuple return control
     * @throws IOException 
     */
    public RelatrixHeadsetIterator(Morphism template, Morphism templateo, short[] dmr_return) throws IOException {
    	if(DEBUG)
    		System.out.printf("%s template:%s templateo:%s dmr_return:%s%n", this.getClass().getName(), template, templateo, Arrays.toString(dmr_return));
    	this.dmr_return = dmr_return;
       	this.base = template;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	// if template domain, map, range was null, templateo was set with endarg last key for class,
    	// concrete type otherwise. template domain, map, range null means we are returning values for that element
    	// and a class or concrete type must have been supplied. For class, we would have inserted last key.
    	try {
    		if(template.getDomain() != null) {
    			// exact match instance
       			DBKey dk = (DBKey) RelatrixKV.get(template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}
    		} else
    			if(templateo.getDomain() != null)
    				RelatrixKV.findHeadMapKVStream(templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(template.getMap() != null) {
    			DBKey mk = (DBKey) RelatrixKV.get(template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null)
    				RelatrixKV.findHeadMapKVStream(templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(template.getRange() != null) {
    			DBKey rk = (DBKey) RelatrixKV.get(template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}
			} else
    			if(templateo.getRange() != null)
    				RelatrixKV.findHeadMapKVStream(templateo.getRange()).forEach(e -> {
    				DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(rkeys.compareTo(rkeyLo) < 0)
    					rkeyLo = rkeys;	
    				if(rkeys.compareTo(rkeyHi) > 0)
    					rkeyHi = rkeys;  				
    				rkey.add(rkeys);
    			});
   
    	} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
    		throw new IOException(e);
    	}

 		if(DEBUG)
			System.out.printf("Keys: %d,%d,%d, ranges: lo:%s%s%s, hi:%s%s%s%n",dkey.size(),mkey.size(),rkey.size(),dkeyLo,mkeyLo,rkeyLo,dkeyHi,mkeyHi,rkeyHi);
		
 		FindsetUtil.getMorphismRange(dkeyLo, mkeyLo, rkeyLo, dkeyHi, mkeyHi, rkeyHi, dkey, mkey, rkey, resultSet);
		
 		if(DEBUG) {
			System.out.println(">>Result set size:"+resultSet.size());
    		resultSet.values().iterator().forEachRemaining(e->{
    			try {
					System.out.println(">>"+RelatrixKV.get(e));
				} catch (IllegalAccessException | IOException e1) {}
    		});
		}
       	iter = resultSet.values().iterator();
    	if( iter.hasNext() ) {
    		try {
       			DBKey dbkey = (DBKey) iter.next();
				buffer = (Morphism) RelatrixKV.get(dbkey); // primary DBKey for Morphism
				buffer.setIdentity(dbkey);
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
	
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println(this.toString());
    }
    /**
     * 
     * @param alias
     * @param template the findset operators and/or concrete object instances from findSet call
     * @param templateo the endargs lower bound original findset call
     * @param dmr_return findSet operator order and tuple return control
     * @param dmr_return
     * @throws IOException
     * @throws NoSuchElementException
     */
    public RelatrixHeadsetIterator(Alias alias, Morphism template, Morphism templateo, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.alias = alias;
      	if(DEBUG)
    		System.out.printf("%s alias:%s template:%s templateo:%s dmr_return:%s%n", this.getClass().getName(), alias, template, templateo, Arrays.toString(dmr_return));
    	this.base = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
    		if(template.getDomain() != null) {
    			DBKey dk = (DBKey) RelatrixKV.get(alias,template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}
    		} else
    			if(templateo.getDomain() != null)
    				RelatrixKV.findHeadMapKVStream(alias,templateo.getDomain()).forEach(e -> {
    					DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(dkeys.compareTo(dkeyLo) < 0)
    						dkeyLo = dkeys;	
    					if(dkeys.compareTo(dkeyHi) > 0)
    						dkeyHi = dkeys;
    					dkey.add(dkeys);
    				});
    		if(template.getMap() != null) {
    			DBKey mk = (DBKey) RelatrixKV.get(alias,template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null)
    				RelatrixKV.findHeadMapKVStream(alias,templateo.getMap()).forEach(e -> {
    					DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(mkeys.compareTo(mkeyLo) < 0)
    						mkeyLo = mkeys;	
    					if(mkeys.compareTo(mkeyHi) > 0)
    						mkeyHi = mkeys;
    					mkey.add(mkeys);
    				});
    		if(template.getRange() != null) {
    			DBKey rk = (DBKey) RelatrixKV.get(alias,template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}
    		} else
    			if(templateo.getRange() != null)
    				RelatrixKV.findHeadMapKVStream(alias,templateo.getRange()).forEach(e -> {
    					DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(rkeys.compareTo(rkeyLo) < 0)
    						rkeyLo = rkeys;	
    					if(rkeys.compareTo(rkeyHi) > 0)
    						rkeyHi = rkeys;  				
    					rkey.add(rkeys);
    				});
    	} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
    		throw new IOException(e);
    	}
    	if(DEBUG)
    		System.out.printf("Keys: %d,%d,%d, ranges: lo:%s%s%s, hi:%s%s%s%n",dkey.size(),mkey.size(),rkey.size(),dkeyLo,mkeyLo,rkeyLo,dkeyHi,mkeyHi,rkeyHi);

    	FindsetUtil.getMorphismRange(alias, dkeyLo, mkeyLo, rkeyLo, dkeyHi, mkeyHi, rkeyHi, dkey, mkey, rkey, resultSet);

    	if(DEBUG) {
    		System.out.println(">>Result set size:"+resultSet.size());
    		resultSet.values().iterator().forEachRemaining(e->{
    			try {
    				System.out.println(">>"+RelatrixKV.get(alias,e));
    			} catch (IllegalAccessException | IOException e1) {}
    		});
    	}
		
    	iter = resultSet.values().iterator();
    	
    	if( iter.hasNext() ) {
    		try {
    			DBKey dbkey = (DBKey) iter.next();
				buffer = (Morphism) RelatrixKV.get(alias, dbkey); // primary DBKey for Morphism
				buffer.setAlias(alias);
				buffer.setIdentity(dbkey);
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}

    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println(this.toString());
    }
    
	@Override
	@ServerMethod
	public boolean hasNext() {
		if( DEBUGITERATION )
			System.out.println(this.toString());
		return needsIter;
	}

	@Override
	@ServerMethod
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUGITERATION ) {
	    			System.out.println("RelatrixHeadsetIterator.next() before iteration:"+this.toString());
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
	    		try {
	    			DBKey dbkey = (DBKey) iter.next();
	    			if(alias == null) {
	    				nextit = (Morphism) RelatrixKV.get(dbkey); // primary DBKey for Morphism
	    			} else {
	    				nextit = (Morphism) RelatrixKV.get(alias, dbkey); // primary DBKey for Morphism
	    				nextit.setAlias(alias);
	    			}
    				nextit.setIdentity(dbkey);
				} catch (IllegalAccessException | IOException e) {
					throw new RuntimeException(e);
				}

			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUGITERATION ) {
			System.out.println("RelatrixIterator.next() template match after iteration:"+this.toString());
		}
		return FindsetUtil.iterateDmr(buffer, identity, dmr_return);
		
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	@ServerMethod
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(" hasNext:");
	    sb.append(iter == null ? "iter NULL" : iter.hasNext());
	    sb.append(" alias:");
	    sb.append(alias);
		sb.append(" needsIter:");
		sb.append(needsIter);
		sb.append(" Identity:");
		sb.append(identity);
		sb.append(" buffer:");
		sb.append(buffer);
		sb.append(" base:");
		sb.append(base);
		sb.append(" nextit:");
		sb.append(nextit);
		sb.append(" dmr_return:");
		sb.append(Arrays.toString(dmr_return));
		return sb.toString();
	}
}
