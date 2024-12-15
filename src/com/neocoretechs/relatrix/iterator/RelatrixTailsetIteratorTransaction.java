package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 *                                                                                                                                                                                                                                                                                                                                                                         * Instances of this class deliver the set of identity {@link Morphism}s, or
 * Mathematically, based on Category Theory: deliver sets of compositions of {@link Morphism}s 
 * representing new group homomorphisms as functors.<p/>
 * More plainly, programmatically, populate a series of arrays with the partial ordered sets of classes
 * designated in the suffix of the 'findSet' predicate then use the min and max range of those to build a range query into
 * the proper table of Morphisms. Extract the domain, map and range components from each retrieved Morphism
 * and determine their index into each domain, map and range arraylist. Use those indexes to form a key using
 * a {@link com.neocoretechs.relatrix.Result} object. Use that key to order a TreeMap entry with the primary key of the
 * retrieved Morphism. The iterator for the findSet then becomes the ordered TreeMap iterator and the primary key is used to retrieve the original
 * Morphism with all its actual payload objects. Ultimately return Result instance elements in next(), 
 * <p/>
 * For tuples the Result is relative to the '?' query predicates. <br/>
 * Here, the tailset is retrieved.<p/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIterator} is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findTailSet("*","?","*",[object | Class]) we get back a {@link com.neocoretechs.relatrix.Result1} of one element. 
 * For findTailSet("?",object,"?",[object | Class],[object | Class]) we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * For each * wildcard or ? return we need a corresponding Class or concrete instance object in the suffix arguments. These objects become the basis
 * for the tailset objects returned. If a Class is specified the entire range of ordered instances is replaced by the ? or *, in the
 * case of a concrete instance, the ordered tailset from that instance (inclusive) to the end is returned or simply used to order
 * the proceeding element in the suffix as it pertains to the retrieved Morphisms in the case of an * wildcard.
 * 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2024
 *
 */
public class RelatrixTailsetIteratorTransaction extends RelatrixTailsetIterator {
	public static boolean DEBUG = false;
	TransactionId xid;
    
    public RelatrixTailsetIteratorTransaction() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param templateo 
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixTailsetIteratorTransaction(TransactionId xid, Morphism template, Morphism templateo, short[] dmr_return) throws IOException {
       	this.xid = xid;
    	if(DEBUG)
    		System.out.printf("%s %s %s %s%n", this.getClass().getName(), xid, template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	buffer.setTransactionId(xid);
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
      	try {
    		if(templateo.getDomain() != null)
    			RelatrixKVTransaction.findTailMapKVStream(xid,templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKVTransaction.findTailMapKVStream(xid,templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKVTransaction.findTailMapKVStream(xid,templateo.getRange()).forEach(e -> {
    				DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(rkeys.compareTo(rkeyLo) < 0)
    					rkeyLo = rkeys;	
    				if(rkeys.compareTo(rkeyHi) > 0)
    					rkeyHi = rkeys;  				
    				rkey.add(rkeys);
    			});
    		
    		if(DEBUG)
    			System.out.printf("Keys: %d,%d,%d, ranges: lod:%s, hid:%s, lom:%s, him:%s, lor:%s, hir:%s%n",dkey.size(),mkey.size(),rkey.size(),dkeyLo,dkeyHi,mkeyLo,mkeyHi,rkeyLo,rkeyHi);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	// clone original template and fill in lo and hi values to select Morphism subset
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) template.clone(); // concrete instance in range
			ydmr = (Morphism) template.clone();
		} catch (CloneNotSupportedException e) {}
		if(xdmr.getDomain() == null) {
			xdmr.setDomainKey(dkeyLo);
			ydmr.setDomainKey(dkeyHi);
		}
		if(xdmr.getMap() == null) {
			xdmr.setMapKey(mkeyLo);
			ydmr.setMapKey(mkeyHi);
		}
		if(xdmr.getRange() == null) {
			xdmr.setRangeKey(rkeyLo);
			ydmr.setRangeKey(rkeyHi);
		}
		FindsetUtil.getMorphismRangeTransaction(xid, xdmr, ydmr, dkey, mkey, rkey, resultSet);
		if(DEBUG)
			System.out.println("Result set size:"+resultSet.size());
    	iter = resultSet.values().iterator();
    	if( iter.hasNext() ) {
    		try {
    			DBKey dbkey = (DBKey) iter.next();
				buffer = (Morphism) RelatrixKVTransaction.get(xid, dbkey); // primary DBKey for Morphism
				buffer.setTransactionId(xid);
				buffer.setIdentity(dbkey);
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			if( !RelatrixIterator.templateMatches(base, buffer, dmr_return) ) {
				buffer = null;
				needsIter = false;
			}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixTailsetIteratorTransaction xid:"+xid+" "+super.toString());
    }
    
    public RelatrixTailsetIteratorTransaction(Alias alias, TransactionId xid, Morphism template, Morphism templateo, short[] dmr_return) throws IOException, NoSuchElementException {
      	this.xid = xid;
    	this.alias = alias;
     	if(DEBUG)
    		System.out.printf("%s %s %s %s %s%n", this.getClass().getName(), alias, xid, template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	buffer.setTransactionId(xid);
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
     	try {
    		if(templateo.getDomain() != null)
    			RelatrixKVTransaction.findTailMapKVStream(alias,xid,templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKVTransaction.findTailMapKVStream(alias,xid,templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKVTransaction.findTailMapKVStream(alias,xid,templateo.getRange()).forEach(e -> {
    				DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(rkeys.compareTo(rkeyLo) < 0)
    					rkeyLo = rkeys;	
    				if(rkeys.compareTo(rkeyHi) > 0)
    					rkeyHi = rkeys;  				
    				rkey.add(rkeys);
    			});
    		
    		if(DEBUG)
    			System.out.printf("Keys: %d,%d,%d, ranges: lod:%s, hid:%s, lom:%s, him:%s, lor:%s, hir:%s%n",dkey.size(),mkey.size(),rkey.size(),dkeyLo,dkeyHi,mkeyLo,mkeyHi,rkeyLo,rkeyHi);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    	// clone original template and fill in lo and hi values to select Morphism subset
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) template.clone(); // concrete instance in range
			ydmr = (Morphism) template.clone();
		} catch (CloneNotSupportedException e) {}
		if(xdmr.getDomain() == null) {
			xdmr.setDomainKey(dkeyLo);
			ydmr.setDomainKey(dkeyHi);
		}
		if(xdmr.getMap() == null) {
			xdmr.setMapKey(mkeyLo);
			ydmr.setMapKey(mkeyHi);
		}
		if(xdmr.getRange() == null) {
			xdmr.setRangeKey(rkeyLo);
			ydmr.setRangeKey(rkeyHi);
		}
		FindsetUtil.getMorphismRangeTransaction(alias, xid, xdmr, ydmr, dkey, mkey, rkey, resultSet);
		if(DEBUG)
			System.out.println("Result set size:"+resultSet.size());
    	iter = resultSet.values().iterator();
    	if( iter.hasNext() ) {
    		try {
    			DBKey dbkey = (DBKey) iter.next();
				buffer = (Morphism) RelatrixKVTransaction.get(alias, xid, dbkey); // primary DBKey for Morphism
				buffer.setTransactionId(xid);
				buffer.setAlias(alias);
				buffer.setIdentity(dbkey);
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			if( !RelatrixIterator.templateMatches(base, buffer, dmr_return) ) {
				buffer = null;
				needsIter = false;
			}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixTailsetIteratorTransaction xid:"+xid+" "+super.toString());  
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUGITERATION )
			System.out.println("RelatrixTailsetIteratorTransaction.hasNext() xid:"+xid+" "+super.toString());
		return needsIter;	
	}

	@Override
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUGITERATION ) {
	    			System.out.println("RelatrixTailsetIteratorTransaction.next() before iteration xid:"+xid+" "+super.toString());
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
	    		try {
	    			DBKey dbkey = (DBKey) iter.next();
	    			if(alias == null) {
	    				nextit = (Morphism) RelatrixKVTransaction.get(xid, dbkey); // primary DBKey for Morphism
	    			} else {
	    				nextit = (Morphism) RelatrixKVTransaction.get(alias, xid, dbkey); // primary DBKey for Morphism
	    				nextit.setAlias(alias);
	    			}
    				nextit.setTransactionId(xid);
    				nextit.setIdentity(dbkey);
				} catch (IllegalAccessException | IOException e) {
					throw new RuntimeException(e);
				}
				if( !RelatrixIterator.templateMatches(base, nextit, dmr_return) ) {
					nextit = null;
					needsIter = false;
				}
			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUGITERATION ) {
			System.out.println("RelatrixIteratorTransaction.next() template match after iteration xid:"+xid+" "+super.toString());
		}
		return FindsetUtil.iterateDmr(buffer, identity, dmr_return);
		
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

}
