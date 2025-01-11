package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified.<p/>                                                                                                                                                                                                                                                                                                                                                                      
 *
 * Populate a series of arrays with the partial ordered sets of classes
 * designated in the suffix of the 'findSet' predicate then use the min and max range of those to build a range query into
 * the proper table of Morphisms. Extract the domain, map and range components from each retrieved Morphism
 * and determine their index into each domain, map and range arraylist. Use those indexes to form a key using
 * a {@link com.neocoretechs.relatrix.Result} object. Use that key to order a TreeMap entry with the primary key of the
 * retrieved Morphism. The iterator for the findSet then becomes the ordered TreeMap iterator and the primary key is used to retrieve the original
 * Morphism with all its actual payload objects. Ultimately return Result instance elements in next(), 
 * <p/>
 * For tuples the Result is relative to the '?' query predicates. <br/>
 * Here, the subset is retrieved.<p/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIterator} is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findSubSet("*","?","*",[object | Class],[object]) we get back a {@link com.neocoretechs.relatrix.Result1} of one element. 
 * For findSubSet("?",object,"?",[object | Class],[object],[object | Class],[object]) we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * For each * wildcard or ? return we need a corresponding Class or 2 concrete instance objects in the suffix arguments. These objects become the basis
 * for the subset objects returned. If a Class is specified the entire range of ordered instances is replaced by the ? or *, in the
 * case of a concrete instance, the ordered subset from that instance (inclusive) to the second object (exclusive) is returned or simply used to order
 * the proceeding element in the suffix as it pertains to the retrieved Morphisms in the case of an * wildcard.<p/>
 * The subset requires an additional concrete instance when an object is specified to designate the ending range of the subset operation.
 * When a Class is specified the range is implied to be the beginning object of the poset (partially ordered set) to the end object instance (exclusive).
 * A concrete instance in one of the first 3 selectors indicates an exact match is desired.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class RelatrixSubsetIteratorTransaction extends RelatrixSubsetIterator {
	TransactionId xid;
	private static boolean DEBUG = false;

    public RelatrixSubsetIteratorTransaction() {}
    /**
     * 
     * @param template The template from the original findSubSet containing the proper Morphism instance depending on operator components
     * @param templateo The lower range for searching primary key Morphisms
     * @param templatep The upper range for searching primary key Morphisms
     * @param dmr_return The operator sequence encoded as array
     * @throws IOException
     */
    public RelatrixSubsetIteratorTransaction(TransactionId xid, Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException {
      	if(DEBUG)
    		System.out.printf("%s template:%s templateo:%s templatep:%s dmr_return:%s%n", this.getClass().getName(), template, templateo, templatep, Arrays.toString(dmr_return));
      	this.xid = xid;
    	this.dmr_return = dmr_return;
       	this.base = template;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
      	// if template domain, map, range was null, templateo was set with endarg last key for class,
    	// concrete type otherwise. template domain, map, range null means we are returning values for that element
    	// and a class or concrete type must have been supplied. For class, we would have inserted last key.
    	try {
    		if(template.getDomain() != null) {
    			DBKey dk = (DBKey) RelatrixKVTransaction.get(xid,template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}
    		} else
    			if(templateo.getDomain() != null)
    				RelatrixKVTransaction.findSubMapKVStream(xid, templateo.getDomain(), templatep.getDomain()).forEach(e -> {
    					DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(dkeys.compareTo(dkeyLo) < 0)
    						dkeyLo = dkeys;	
    					if(dkeys.compareTo(dkeyHi) > 0)
    						dkeyHi = dkeys;
    					dkey.add(dkeys);
    				});
    		if(template.getMap() != null) {
    			DBKey mk = (DBKey) RelatrixKVTransaction.get(xid,template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null)
    				RelatrixKVTransaction.findSubMapKVStream(xid, templateo.getMap(), templatep.getMap()).forEach(e -> {
    					DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(mkeys.compareTo(mkeyLo) < 0)
    						mkeyLo = mkeys;	
    					if(mkeys.compareTo(mkeyHi) > 0)
    						mkeyHi = mkeys;
    					mkey.add(mkeys);
    				});

    		if(template.getRange() != null) {
    			DBKey rk = (DBKey) RelatrixKVTransaction.get(xid,template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}
    		} else
    			if(templateo.getRange() != null)
    				RelatrixKVTransaction.findSubMapKVStream(xid, templateo.getRange(), templatep.getRange()).forEach(e -> {
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
		
 		FindsetUtil.getMorphismRangeTransaction(xid, dkeyLo, mkeyLo, rkeyLo, dkeyHi, mkeyHi, rkeyHi, dkey, mkey, rkey, resultSet);
		
 		if(DEBUG) {
			System.out.println(">>Result set size:"+resultSet.size());
    		resultSet.values().iterator().forEachRemaining(e->{
    			try {
					System.out.println(">>"+RelatrixKVTransaction.get(xid,e));
				} catch (IllegalAccessException | IOException e1) {}
    		});
		}
 		
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

    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixSubsetIteratorTransaction hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:");
    }
    /**
     * 
     * @param alias
     * @param template The template from the original findSubSet containing the proper Morphism instance depending on operator components
     * @param templateo The lower range for searching primary key Morphisms
     * @param templatep The upper range for searching primary key Morphisms
     * @param dmr_return The operator sequence encoded as array
     * @throws IOException
     * @throws NoSuchElementException
     */
    public RelatrixSubsetIteratorTransaction(Alias alias, TransactionId xid, Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException, NoSuchElementException {
    	if(DEBUG)
    		System.out.printf("%s template:%s templateo:%s templatep:%s dmr_return:%s%n", this.getClass().getName(), template, templateo, templatep, Arrays.toString(dmr_return));
    	this.alias = alias;
      	this.xid = xid;
    	this.dmr_return = dmr_return;
       	this.base = template;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	// if template domain, map, range was null, templateo was set with endarg last key for class,
    	// concrete type otherwise. template domain, map, range null means we are returning values for that element
    	// and a class or concrete type must have been supplied. For class, we would have inserted last key.
    	try {
    		if(template.getDomain() != null) {
    			DBKey dk = (DBKey) RelatrixKVTransaction.get(alias, xid, template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}    		
    		} else
    			if(templateo.getDomain() != null)
    				RelatrixKVTransaction.findSubMapKVStream(alias, xid, templateo.getDomain(), templatep.getDomain()).forEach(e -> {
    					DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(dkeys.compareTo(dkeyLo) < 0)
    						dkeyLo = dkeys;	
    					if(dkeys.compareTo(dkeyHi) > 0)
    						dkeyHi = dkeys;
    					dkey.add(dkeys);
    				});

    		if(template.getMap() != null) {
    			DBKey mk = (DBKey) RelatrixKVTransaction.get(alias, xid, template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null)
    				RelatrixKVTransaction.findSubMapKVStream(alias, xid, templateo.getMap(), templatep.getMap()).forEach(e -> {
    					DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(mkeys.compareTo(mkeyLo) < 0)
    						mkeyLo = mkeys;	
    					if(mkeys.compareTo(mkeyHi) > 0)
    						mkeyHi = mkeys;
    					mkey.add(mkeys);
    				});

    		if(template.getRange() != null) {
    			DBKey rk = (DBKey) RelatrixKVTransaction.get(alias, xid, template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}    		
    		} else
    			if(templateo.getRange() != null)
    				RelatrixKVTransaction.findSubMapKVStream(alias, xid, templateo.getRange(), templatep.getRange()).forEach(e -> {
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
		
 		FindsetUtil.getMorphismRangeTransaction(alias, xid, dkeyLo, mkeyLo, rkeyLo, dkeyHi, mkeyHi, rkeyHi, dkey, mkey, rkey, resultSet);
		
 		if(DEBUG) {
			System.out.println(">>Result set size:"+resultSet.size());
    		resultSet.values().iterator().forEachRemaining(e->{
    			try {
					System.out.println(">>"+RelatrixKVTransaction.get(alias,xid,e));
				} catch (IllegalAccessException | IOException e1) {}
    		});
		}
 		
    	iter = resultSet.values().iterator();
    	
    	if( iter.hasNext() ) {
    		try {
    			DBKey dbkey = (DBKey) iter.next();
				buffer = (Morphism) RelatrixKVTransaction.get(alias, xid, dbkey); // primary DBKey for Morphism
				buffer.setAlias(alias);
				buffer.setTransactionId(xid);
				buffer.setIdentity(dbkey);
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}

    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
			System.out.println("RelatrixSubsetIteratorTransaction hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:");
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUGITERATION )
			System.out.println("RelatrixSubsetIteratorTransaction.hasNext() "+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		return needsIter;
	}

	@Override
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUGITERATION ) {
	    			System.out.println("RelatrixSubsetIteratorTransaction.next() before iteration hasNext:"+iter.hasNext()+" needsIter:"+needsIter+", buffer:"+buffer+", nextit"+nextit);
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
	
			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUGITERATION ) {
			System.out.println("RelatrixSubsetIteratorTransaction.next() template match after iteration hasNext:"+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
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
