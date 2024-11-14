package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.rocksack.Alias;
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
public class RelatrixTailsetIterator implements Iterator<Result> {
	public static boolean DEBUG = false;
	public static boolean DEBUGITERATION = false;
	protected Alias alias = null;
	protected Iterator iter;
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
    
    public RelatrixTailsetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template the findset operators and/or concrete object instances from findSet call
     * @param templateo the endargs lower bound original findset call
     * @param dmr_return findSet operator order and tuple return control
     * @throws IOException 
     */
    public RelatrixTailsetIterator(Morphism template, Morphism templateo, short[] dmr_return) throws IOException {
       	if(DEBUG)
    		System.out.printf("%s template:%s templateo:%s dmr_return:%s%n", this.getClass().getName(), template, templateo, Arrays.toString(dmr_return));
    	this.dmr_return = dmr_return;
       	this.base = template;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
    		if(templateo.getDomain() != null)
    			RelatrixKV.findTailMapKVStream(templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKV.findTailMapKVStream(templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKV.findTailMapKVStream(templateo.getRange()).forEach(e -> {
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
		FindsetUtil.getMorphismRange(xdmr, ydmr, dkey, mkey, rkey, resultSet);
		if(DEBUG)
			System.out.println("Result set size:"+resultSet.size());
    	iter = resultSet.values().iterator();
    	if( iter.hasNext() ) {
    		try {
				buffer = (Morphism) RelatrixKV.get((Comparable<?>) iter.next()); // primary DBKey for Morphism
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
			System.out.println("RelatrixTailsetIterator hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:"+base);
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
    public RelatrixTailsetIterator(Alias alias, Morphism template, Morphism templateo, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.alias = alias;
      	if(DEBUG)
    		System.out.printf("%s alias:%s template:%s templateo:%s dmr_return:%s%n", this.getClass().getName(), alias, template, templateo, Arrays.toString(dmr_return));
    	this.base = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
      	try {
    		if(templateo.getDomain() != null)
    			RelatrixKV.findTailMapKVStream(alias,templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKV.findTailMapKVStream(alias,templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKV.findTailMapKVStream(alias,templateo.getRange()).forEach(e -> {
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
		FindsetUtil.getMorphismRange(alias, xdmr, ydmr, dkey, mkey, rkey, resultSet);
		if(DEBUG)
			System.out.println("Result set size:"+resultSet.size());
    	iter = resultSet.values().iterator();
    	if( iter.hasNext() ) {
    		try {
				buffer = (Morphism) RelatrixKV.get(alias, (Comparable<?>) iter.next()); // primary DBKey for Morphism
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
			System.out.println("RelatrixTailsetIterator hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:"+base);
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUGITERATION )
			System.out.println("RelatrixTailsetIterator.hasNext() "+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		return needsIter;
	}

	@Override
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUGITERATION ) {
	    			System.out.println("RelatrixTailsetIterator.next() before iteration hasNext:"+iter.hasNext()+" needsIter:"+needsIter+", buffer:"+buffer+", nextit"+nextit);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
	    		try {
	    			if(alias == null)
	    				nextit = (Morphism) RelatrixKV.get((Comparable<?>) iter.next()); // primary DBKey for Morphism
	    			else
	    				nextit = (Morphism) RelatrixKV.get(alias,(Comparable<?>) iter.next()); // primary DBKey for Morphism
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
			System.out.println("RelatrixIterator.next() template match after iteration hasNext:"+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
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
