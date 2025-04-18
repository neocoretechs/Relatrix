package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;


/**                                                                                                                                                                                                                                                                                                                                                                      
 * Populate a series of arrays with the partial ordered sets of instances with elements strictly less than 'to' target.
 * Classes designated in the suffix of the 'findSet' predicate use the min and max range of those classes to build a range query into
 * the proper table of Morphisms. Instances designated in the suffix use that concrete instance value.
 * The post-ordering consists of extracting the domain, map and range components from each retrieved AbstractRelation
 * and determine their index into each domain, map and range arraylist. Use those indexes to form a key using
 * a {@link com.neocoretechs.relatrix.Result} object. Use that key to order a TreeMap entry with the primary key of the
 * retrieved AbstractRelation. The iterator for the findSet then becomes the ordered TreeMap iterator and the primary key is used to retrieve the original
 * AbstractRelation with all its actual payload objects. Ultimately return Result instance elements in next(), 
 * <p/>
 * For tuples the Result is relative to the '?' query predicates. <br/>
 * Here, the headset is retrieved.<p/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIterator} is dependent on the number of '?' operators in a 'findSet'. For example,
 * if we declare findHeadSet('*','?','*',[object | Class])<br/> we get back a {@link com.neocoretechs.relatrix.Result1} of one element.<br/> 
 * For findHeadSet('?',object,'?',[object | Class],[object | Class]) <br/>we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * For each * wildcard or ? return we need a corresponding Class or concrete instance object in the suffix arguments. These objects become the basis
 * for the headset objects returned. As mentioned above, if a Class is specified the entire range of ordered instances is replaced by the ? or *, in the
 * case of a concrete instance, the ordered headset from the beginning to that instance (exclusive) is returned or simply used to order
 * the proceeding element in the suffix as it pertains to the retrieved Morphisms in the case of an * wildcard. A concrete instance
 * in one of the first 3 selectors indicates an exact match is desired.
 * 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2024
 *
 */
public class RelatrixHeadsetIteratorTransaction extends RelatrixHeadsetIterator {
	public static boolean DEBUG = false;
	TransactionId xid;

    public RelatrixHeadsetIteratorTransaction() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param templateo 
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadsetIteratorTransaction(TransactionId xid, AbstractRelation template, AbstractRelation templateo, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	if(DEBUG)
    		System.out.printf("%s %s %s %s%n", this.getClass().getName(), xid, template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
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
    				RelatrixKVTransaction.findHeadMapKVStream(xid,templateo.getDomain()).forEach(e -> {
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
    				RelatrixKVTransaction.findHeadMapKVStream(xid,templateo.getMap()).forEach(e -> {
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
    				RelatrixKVTransaction.findHeadMapKVStream(xid,templateo.getRange()).forEach(e -> {
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
				buffer = (AbstractRelation) RelatrixKVTransaction.get(xid, dbkey); // primary DBKey for AbstractRelation
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
			System.out.println("RelatrixHeadsetIteratorTransaction hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:"+base);
    }
    
    public RelatrixHeadsetIteratorTransaction(Alias alias, TransactionId xid, AbstractRelation template, AbstractRelation templateo, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	this.alias = alias;
     	if(DEBUG)
    		System.out.printf("%s %s %s %s %s%n", this.getClass().getName(), alias, xid, template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
       	// if template domain, map, range was null, templateo was set with endarg last key for class,
    	// concrete type otherwise. template domain, map, range null means we are returning values for that element
    	// and a class or concrete type must have been supplied. For class, we would have inserted last key.
    	try {
    		if(template.getDomain() != null) {
    			DBKey dk = (DBKey) RelatrixKVTransaction.get(alias,xid,template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}
    		} else
    			if(templateo.getDomain() != null)
    				RelatrixKVTransaction.findHeadMapKVStream(alias,xid,templateo.getDomain()).forEach(e -> {
    					DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(dkeys.compareTo(dkeyLo) < 0)
    						dkeyLo = dkeys;	
    					if(dkeys.compareTo(dkeyHi) > 0)
    						dkeyHi = dkeys;
    					dkey.add(dkeys);
    				});

    		if(template.getMap() != null) {
    			DBKey mk = (DBKey) RelatrixKVTransaction.get(alias,xid,template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null)
    				RelatrixKVTransaction.findHeadMapKVStream(alias,xid,templateo.getMap()).forEach(e -> {
    					DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(mkeys.compareTo(mkeyLo) < 0)
    						mkeyLo = mkeys;	
    					if(mkeys.compareTo(mkeyHi) > 0)
    						mkeyHi = mkeys;
    					mkey.add(mkeys);
    				});

    		if(template.getRange() != null) {
    			DBKey rk = (DBKey) RelatrixKVTransaction.get(alias,xid,template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}
    		} else
    			if(templateo.getRange() != null)
    				RelatrixKVTransaction.findHeadMapKVStream(alias,xid,templateo.getRange()).forEach(e -> {
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
				buffer = (AbstractRelation) RelatrixKVTransaction.get(alias, xid, dbkey); // primary DBKey for AbstractRelation
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
			System.out.println("RelatrixHeadsetIteratorTransaction hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:"+base);
    }
    
	public TransactionId getTransactionId() {
		return xid;
	}
	
	@Override
	@ServerMethod
	public boolean hasNext() {
		if( DEBUGITERATION )
			System.out.println("RelatrixHeadsetIteratorTransaction.hasNext() "+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		return needsIter;
	}

	@Override
	@ServerMethod
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUGITERATION ) {
	    			System.out.println("RelatrixHeadsetIteratorTransaction.next() before iteration hasNext:"+iter.hasNext()+" needsIter:"+needsIter+", buffer:"+buffer+", nextit"+nextit);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
	    		try {
	    			DBKey dbkey = (DBKey) iter.next();
	    			if(alias == null) {
	    				nextit = (AbstractRelation) RelatrixKVTransaction.get(xid, dbkey ); // primary DBKey for AbstractRelation
	    			} else {
	    				nextit = (AbstractRelation) RelatrixKVTransaction.get(alias, xid, dbkey); // primary DBKey for AbstractRelation
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
			System.out.println("RelatrixIteratorTransaction.next() template match after iteration hasNext:"+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
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
 
}
