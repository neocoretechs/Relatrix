package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.RelatrixKVJsonTransaction;
import com.neocoretechs.rocksack.Alias;

import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.iterator.json.FindsetUtilJson;
import com.neocoretechs.relatrix.iterator.json.RelatrixHeadsetIteratorJson;
import com.neocoretechs.relatrix.iterator.json.RelatrixIteratorJson;
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
 * For tuples the Result is relative to the '?' query predicates. <br>
 * Here, the headset is retrieved.<p/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIteratorJson} is dependent on the number of '?' operators in a 'findSet'. For example,
 * if we declare findHeadSet('*','?','*',[object | Class])<br/> we get back a {@link com.neocoretechs.relatrix.Result1} of one element.<br> 
 * For findHeadSet('?',object,'?',[object | Class],[object | Class]) <br>we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br>
 * For each * wildcard or ? return we need a corresponding Class or concrete instance object in the suffix arguments. These objects become the basis
 * for the headset objects returned. As mentioned above, if a Class is specified the entire range of ordered instances is replaced by the ? or *, in the
 * case of a concrete instance, the ordered headset from the beginning to that instance (exclusive) is returned or simply used to order
 * the proceeding element in the suffix as it pertains to the retrieved Morphisms in the case of an * wildcard. A concrete instance
 * in one of the first 3 selectors indicates an exact match is desired.
 * 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2024,2026
 *
 */
public class RelatrixHeadsetIteratorJsonTransaction extends RelatrixHeadsetIteratorJson {
	public static boolean DEBUG = false;
	TransactionId xid;

    public RelatrixHeadsetIteratorJsonTransaction() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param templateo 
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadsetIteratorJsonTransaction(TransactionId xid, AbstractRelation template, AbstractRelation templateo, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	if(DEBUG)
    		System.out.printf("%s %s %s %s%n", this.getClass().getName(), xid, template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIteratorJson.isIdentity(this.dmr_return);
    	// if template domain, map, range was null, templateo was set with endarg last key for class,
    	// concrete type otherwise. template domain, map, range null means we are returning values for that element
    	// and a class or concrete type must have been supplied. For class, we would have inserted last key.
    	try {
    		if(template.getDomain() != null) {
       			DBKey dk = (DBKey) RelatrixKVJsonTransaction.get(xid,template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}
    		} else
    			if(templateo.getDomain() != null) {
    				/*RelatrixKVJsonTransaction.findHeadMapKVStream(xid,templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    				});
    				*/
    				ConcurrentLinkedQueue<DBKey> q = RelatrixKVJsonTransaction.findHeadMapKVStream(xid, templateo.getDomain())
    	    			    .map(e -> ((Map.Entry<Comparable,DBKey>) e).getValue())
    	    			    .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    	    				dkey.addAll(q); // single-threaded merge
    	    				// compute lo/hi
    	    				if(DEBUG)
    	    					if(dkey.isEmpty())
    	    						System.out.printf("%s domain headmap keyrange yielded empty set for:%s%n", this.getClass().getName(), RelatrixKVJson.getData(templateo.getDomain()));
    	    				for (DBKey k : q) {
    	    					if (k.compareTo(dkeyLo) < 0) dkeyLo = k;
    	    					if (k.compareTo(dkeyHi) > 0) dkeyHi = k;
    	    				}
    			}
    		if(template.getMap() != null) {
       			DBKey mk = (DBKey) RelatrixKVJsonTransaction.get(xid,template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null) {
    				/*RelatrixKVJsonTransaction.findHeadMapKVStream(xid,templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});*/
    				ConcurrentLinkedQueue<DBKey> q = RelatrixKVJsonTransaction.findHeadMapKVStream(xid, templateo.getMap())
      					.map(e -> ((Map.Entry<Comparable,DBKey>) e).getValue())
    			    	.collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
      					mkey.addAll(q); // single-threaded merge
      					if(DEBUG)
        					if(mkey.isEmpty())
        						System.out.printf("%s map headmap keyrange yielded empty set for:%s%n", this.getClass().getName(), RelatrixKVJson.getData(templateo.getMap()));
    					// compute lo/hi
    					for (DBKey k : q) {
    						if (k.compareTo(mkeyLo) < 0) mkeyLo = k;
    						if (k.compareTo(mkeyHi) > 0) mkeyHi = k;
    					}
    			}
    		if(template.getRange() != null) {
     			DBKey rk = (DBKey) RelatrixKVJsonTransaction.get(xid,template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}
    		} else
    			if(templateo.getRange() != null) {
    				/*RelatrixKVJsonTransaction.findHeadMapKVStream(xid,templateo.getRange()).forEach(e -> {
    				DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(rkeys.compareTo(rkeyLo) < 0)
    					rkeyLo = rkeys;	
    				if(rkeys.compareTo(rkeyHi) > 0)
    					rkeyHi = rkeys;  				
    				rkey.add(rkeys);
    			});*/
    				ConcurrentLinkedQueue<DBKey> q = RelatrixKVJsonTransaction.findHeadMapKVStream(xid, templateo.getRange())
      						.map(e -> ((Map.Entry<Comparable,DBKey>) e).getValue())
    			    	.collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
      					rkey.addAll(q); // single-threaded merge
    					if(DEBUG)
        					if(rkey.isEmpty())
        						System.out.printf("%s range headmap keyrange yielded empty set for:%s%n", this.getClass().getName(), RelatrixKVJson.getData(templateo.getRange()));
    					// compute lo/hi
    					for (DBKey k : q) {
    						if (k.compareTo(rkeyLo) < 0) rkeyLo = k;
    						if (k.compareTo(rkeyHi) > 0) rkeyHi = k;
    					}
    			}
    	} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
    		throw new IOException(e);
    	}

 		if(DEBUG)
			System.out.printf("Keys: %d,%d,%d, ranges: lo:%s%s%s, hi:%s%s%s%n",dkey.size(),mkey.size(),rkey.size(),dkeyLo,mkeyLo,rkeyLo,dkeyHi,mkeyHi,rkeyHi);
		
 		FindsetUtilJson.getMorphismRangeTransaction(xid, dkeyLo, mkeyLo, rkeyLo, dkeyHi, mkeyHi, rkeyHi, dkey, mkey, rkey, resultSet);
		
 		if(DEBUG) {
			System.out.println(">>Result set size:"+resultSet.size());
    		resultSet.values().iterator().forEachRemaining(e->{
    			try {
					System.out.println(">>"+RelatrixKVJsonTransaction.get(xid,e));
				} catch (IllegalAccessException | IOException e1) {}
    		});
		}
 		
    	iter = resultSet.values().iterator();
    	if( iter.hasNext() ) {
    		try {
    			DBKey dbkey = (DBKey) iter.next();
				buffer = (AbstractRelation) RelatrixKVJsonTransaction.get(xid, dbkey); // primary DBKey for AbstractRelation
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
			System.out.println("RelatrixHeadsetIteratorJsonTransaction hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:"+base);
    }
    
    public RelatrixHeadsetIteratorJsonTransaction(Alias alias, TransactionId xid, AbstractRelation template, AbstractRelation templateo, short[] dmr_return) throws IOException {
    	this.xid = xid;
    	this.alias = alias;
     	if(DEBUG)
    		System.out.printf("%s %s %s %s %s%n", this.getClass().getName(), alias, xid, template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIteratorJson.isIdentity(this.dmr_return);
       	// if template domain, map, range was null, templateo was set with endarg last key for class,
    	// concrete type otherwise. template domain, map, range null means we are returning values for that element
    	// and a class or concrete type must have been supplied. For class, we would have inserted last key.
    	try {
    		if(template.getDomain() != null) {
    			DBKey dk = (DBKey) RelatrixKVJsonTransaction.get(alias,xid,template.getDomain());
    			if(dk != null) {
    				dkey.add(dk);
    				dkeyLo = dk;
    				dkeyHi = dk;
    			}
    		} else
    			if(templateo.getDomain() != null) {
    				/*RelatrixKVJsonTransaction.findHeadMapKVStream(alias,xid,templateo.getDomain()).forEach(e -> {
    					DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(dkeys.compareTo(dkeyLo) < 0)
    						dkeyLo = dkeys;	
    					if(dkeys.compareTo(dkeyHi) > 0)
    						dkeyHi = dkeys;
    					dkey.add(dkeys);
    				});*/
    				ConcurrentLinkedQueue<DBKey> q = RelatrixKVJsonTransaction.findHeadMapKVStream(alias, xid, templateo.getDomain())
    	    			    .map(e -> ((Map.Entry<Comparable,DBKey>) e).getValue())
    	    			    .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    	    				dkey.addAll(q); // single-threaded merge
    	    				// compute lo/hi
    	    				if(DEBUG)
    	    					if(dkey.isEmpty())
    	    						System.out.printf("%s domain headmap keyrange yielded empty set for:%s%n", this.getClass().getName(), RelatrixKVJson.getData(templateo.getDomain()));
    	    				for (DBKey k : q) {
    	    					if (k.compareTo(dkeyLo) < 0) dkeyLo = k;
    	    					if (k.compareTo(dkeyHi) > 0) dkeyHi = k;
    	    				}
    			}
    		if(template.getMap() != null) {
    			DBKey mk = (DBKey) RelatrixKVJsonTransaction.get(alias,xid,template.getMap());
    			if(mk != null) {
    				mkey.add(mk);
    				mkeyLo = mk;
    				mkeyHi = mk;
    			}
    		} else
    			if(templateo.getMap() != null) {
    				/*RelatrixKVJsonTransaction.findHeadMapKVStream(alias,xid,templateo.getMap()).forEach(e -> {
    					DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(mkeys.compareTo(mkeyLo) < 0)
    						mkeyLo = mkeys;	
    					if(mkeys.compareTo(mkeyHi) > 0)
    						mkeyHi = mkeys;
    					mkey.add(mkeys);
    				});*/
     				ConcurrentLinkedQueue<DBKey> q = RelatrixKVJsonTransaction.findHeadMapKVStream(alias, xid, templateo.getMap())
          					.map(e -> ((Map.Entry<Comparable,DBKey>) e).getValue())
        			    	.collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
          					mkey.addAll(q); // single-threaded merge
          					if(DEBUG)
            					if(mkey.isEmpty())
            						System.out.printf("%s map headmap keyrange yielded empty set for:%s%n", this.getClass().getName(), RelatrixKVJson.getData(templateo.getMap()));
        					// compute lo/hi
        					for (DBKey k : q) {
        						if (k.compareTo(mkeyLo) < 0) mkeyLo = k;
        						if (k.compareTo(mkeyHi) > 0) mkeyHi = k;
        					}
    			}
    		if(template.getRange() != null) {
    			DBKey rk = (DBKey) RelatrixKVJsonTransaction.get(alias,xid,template.getRange());
    			if(rk != null) {
    				rkey.add(rk);
    				rkeyLo = rk;
    				rkeyHi = rk;
    			}
    		} else
    			if(templateo.getRange() != null) {
    				/*RelatrixKVJsonTransaction.findHeadMapKVStream(alias,xid,templateo.getRange()).forEach(e -> {
    					DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    					if(rkeys.compareTo(rkeyLo) < 0)
    						rkeyLo = rkeys;	
    					if(rkeys.compareTo(rkeyHi) > 0)
    						rkeyHi = rkeys;  				
    					rkey.add(rkeys);
    				});*/
    				ConcurrentLinkedQueue<DBKey> q = RelatrixKVJsonTransaction.findHeadMapKVStream(alias, xid, templateo.getRange())
      						.map(e -> ((Map.Entry<Comparable,DBKey>) e).getValue())
    			    	.collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
      					rkey.addAll(q); // single-threaded merge
    					if(DEBUG)
        					if(rkey.isEmpty())
        						System.out.printf("%s range headmap keyrange yielded empty set for:%s%n", this.getClass().getName(), RelatrixKVJson.getData(templateo.getRange()));
    					// compute lo/hi
    					for (DBKey k : q) {
    						if (k.compareTo(rkeyLo) < 0) rkeyLo = k;
    						if (k.compareTo(rkeyHi) > 0) rkeyHi = k;
    					}
    			}
    	} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
    		throw new IOException(e);
    	}

 		if(DEBUG)
			System.out.printf("Keys: %d,%d,%d, ranges: lo:%s%s%s, hi:%s%s%s%n",dkey.size(),mkey.size(),rkey.size(),dkeyLo,mkeyLo,rkeyLo,dkeyHi,mkeyHi,rkeyHi);
		
 		FindsetUtilJson.getMorphismRangeTransaction(alias, xid, dkeyLo, mkeyLo, rkeyLo, dkeyHi, mkeyHi, rkeyHi, dkey, mkey, rkey, resultSet);
		
 		if(DEBUG) {
			System.out.println(">>Result set size:"+resultSet.size());
    		resultSet.values().iterator().forEachRemaining(e->{
    			try {
					System.out.println(">>"+RelatrixKVJsonTransaction.get(alias,xid,e));
				} catch (IllegalAccessException | IOException e1) {}
    		});
		}
 		
    	iter = resultSet.values().iterator();
    	
    	if( iter.hasNext() ) {
    		try {
    			DBKey dbkey = (DBKey) iter.next();
				buffer = (AbstractRelation) RelatrixKVJsonTransaction.get(alias, xid, dbkey); // primary DBKey for AbstractRelation
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
	    				nextit = (AbstractRelation) RelatrixKVJsonTransaction.get(xid, dbkey ); // primary DBKey for AbstractRelation
	    			} else {
	    				nextit = (AbstractRelation) RelatrixKVJsonTransaction.get(alias, xid, dbkey); // primary DBKey for AbstractRelation
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
		return FindsetUtilJson.iterateDmr(buffer, identity, dmr_return);
		
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
