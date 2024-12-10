package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified.<p/>
 * Our main representable analog. Instances of this class deliver the set of identity morphisms, or
 * deliver sets of compositions of morphisms representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the subset, or from beginning parameters to the ending parameters of template element, are retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a {@link Result1} of one element. For findSet("?",object,"?") we
 * would get back a {@link Result2}, with each element containing the relationship returned.<br/>
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
    	try {
    		if(templateo.getDomain() != null)
    			RelatrixKVTransaction.findSubMapKVStream(xid,templateo.getDomain(), templatep.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKVTransaction.findSubMapKVStream(xid,templateo.getMap(), templatep.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKVTransaction.findSubMapKVStream(xid,templateo.getRange(), templatep.getRange()).forEach(e -> {
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
				buffer = (Morphism) RelatrixKVTransaction.get(xid, (Comparable<?>) iter.next()); // primary DBKey for Morphism
				buffer.setTransactionId(xid);
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
    	try {
    		if(templateo.getDomain() != null)
    			RelatrixKVTransaction.findSubMapKVStream(alias,xid,templateo.getDomain(), templatep.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKVTransaction.findSubMapKVStream(alias,xid,templateo.getMap(), templatep.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKVTransaction.findSubMapKVStream(alias,xid,templateo.getRange(), templatep.getRange()).forEach(e -> {
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
				buffer = (Morphism) RelatrixKVTransaction.get(alias, xid, (Comparable<?>) iter.next()); // primary DBKey for Morphism
				buffer.setAlias(alias);
				buffer.setTransactionId(xid);
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
	    			if(alias == null) {
	    				nextit = (Morphism) RelatrixKVTransaction.get(xid, (Comparable<?>) iter.next()); // primary DBKey for Morphism
	    			} else {
	    				nextit = (Morphism) RelatrixKVTransaction.get(alias, xid, (Comparable<?>) iter.next()); // primary DBKey for Morphism
	    				nextit.setAlias(alias);
	    			}
    				nextit.setTransactionId(xid);
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
