package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ExecutionContextHolder;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.server.ServerMethod;

/**
 * Implementation of the standard Iterator interface which operates on {@link com.neocoretechs.relatrix.AbstractRelation}s formed into a template
 * to set the lower bound of the correct range search for the properly ordered set of AbstractRelation subclasses;
 * If its an identity morphism (instance of AbstractRelation) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link com.neocoretechs.relatrix.Result} in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p>
 * findSet('*','*','*') = {@link Result1} containing identity of instance Relation <br>
 * findSet('*','*',object) =  {@link Result1} identity of RangeDomainMap where 'object' is range <br>
 * findSet('*',object,object) = {@link Result1} identity of MapRangeDomain matching the 2 concrete objects <br>
 * findSet(object,object,object) = {@link Result1} identity of Relation matching 3 objects <br>
 * etc.
 * <p>
 * findHeadSet works in the same fashion but returns elements strictly less than the target element. <p>
 * A special case is the subset, where the number of returned elements includes the target range object(s).<br>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017,2026
 *
 */
public class RelatrixIterator implements Iterator<Result> {
	private static boolean DEBUG = false;
	protected Iterator<?> iter;
    protected AbstractRelation buffer = null;
    protected AbstractRelation nextit = null;
    protected AbstractRelation base;
    protected short dmr_return[] = new short[4];
    protected Alias alias = null;

    protected boolean needsIter = true;
    protected boolean identity = false;
    protected IndexResolver indexResolver;
    
    public RelatrixIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
     * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
     * @param ctx The parallel execution context with IndexResolver etc.
     * @throws IOException
     */
    public RelatrixIterator(AbstractRelation template, short[] dmr_return, ParallelExecutionContext ctx) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	this.indexResolver = ctx.resolver();
    	this.base.setResolver(indexResolver);
    	this.identity = isIdentity(this.dmr_return);
    	findTemplate(template);
    }

    protected RelatrixIterator(TransactionId xid, AbstractRelation template, short[] dmr_return, ParallelExecutionContext ctx) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	this.indexResolver = ctx.resolver();
    	this.base.setResolver(indexResolver);
    	this.identity = isIdentity(this.dmr_return);
    	findTemplate(xid, template);
    }

    protected void findTemplate(AbstractRelation template) throws IOException {
    	try {
    		iter = RelatrixKV.findTailMapKV(template);
    		if( iter.hasNext() ) {
    			Map.Entry me = (Entry) iter.next();
    			buffer.setResolver(indexResolver);
    			buffer = (AbstractRelation)me.getKey();
    			buffer.setIdentity((DBKey) me.getValue());
    			if( !templateMatches(base, buffer, dmr_return) ) {
    				buffer = null;
    				needsIter = false;
    			}
    		} else {
    			buffer = null;
    			needsIter = false;
    		}
    		if( DEBUG )
    			System.out.println(this.toString());
    	} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
    		throw new IOException(e);
    	}
    }

    protected void findTemplate(TransactionId xid, AbstractRelation template) throws IOException{  	
    }
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param alias
     * @param template the retrieval template with objects and nulls to fulfill initial retrieval parameters
     * @param dmr_return the retrieval template with operators indicating object, wildcard, tuple return
     * @param ctx TODO
     * @throws IOException
     */
    public RelatrixIterator(Alias alias, AbstractRelation template, short[] dmr_return, ParallelExecutionContext ctx) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	this.indexResolver = ctx.resolver();
    	this.base.setResolver(indexResolver);
    	this.alias = alias;
    	this.identity = isIdentity(this.dmr_return);
    	findTemplate(alias, template);
    }

    public RelatrixIterator(Alias alias, TransactionId xid, AbstractRelation template, short[] dmr_return, ParallelExecutionContext ctx) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	this.indexResolver = ctx.resolver();
    	this.base.setResolver(indexResolver);
    	this.alias = alias;
    	this.identity = isIdentity(this.dmr_return);
    	findTemplate(alias, xid, template);
    }
    protected void findTemplate(Alias alias, AbstractRelation template) throws IOException {
    	try {
    		iter = RelatrixKV.findTailMapKV(alias, template);
    	} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
    		throw new IOException(e);
    	}
    	if( iter.hasNext() ) {
    		Map.Entry me = (Entry) iter.next();
    		buffer = (AbstractRelation)me.getKey();
    		buffer.setResolver(indexResolver);
    		buffer.setIdentity((DBKey)me.getValue());
    		buffer.setAlias(alias);
    		if( !templateMatches(base, buffer, dmr_return) ) {
    			buffer = null;
    			needsIter = false;
    		}
    	} else {
    		buffer = null;
    		needsIter = false;
    	}
    	if( DEBUG )
    		System.out.println(this.toString());
    }

    protected void findTemplate(Alias alias, TransactionId xid, AbstractRelation template) throws IOException {	 
    }

    @Override
    @ServerMethod
    public boolean hasNext() {
    	if( DEBUG )
    		System.out.println(this.toString());
    	return needsIter;
    }

    @Override
    @ServerMethod
    public Result next() {
    	try {
    		if( buffer == null || needsIter) {
    			if( DEBUG ) {
    				System.out.println(this.toString());
    			}
    			if( nextit != null )
    				buffer = nextit;
    			if( iter.hasNext()) {
    				Map.Entry me = (Entry) iter.next();
    				nextit = (AbstractRelation)me.getKey();
    				nextit.setResolver(indexResolver);
    				nextit.setIdentity((DBKey) me.getValue());
    				if(alias != null)
    					nextit.setAlias(alias);
    				if( !templateMatches(base, nextit, dmr_return) ) {
    					nextit = null;
    					needsIter = false;
    				}
    			} else {
    				nextit = null;
    				needsIter = false;
    			}
    		}
    		// always return using this with non null buffer
    		if( DEBUG ) {
    			System.out.println("RelatrixIterator.next() template match after iteration "+this.toString());
    		}
    		return FindsetUtil.setResult(buffer);

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

    /**
     * Checks to see if our dmr_return array has any return tuple ? values.<br>
     * if any element of our dmr_return array is 1, we have return ? tuple present.<p>
     * For each element of the dmr_return array elements 1-3, 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *<br>
     * If the 0 element (the iterator over the array) is -1 or all elements are either 0 (object), or 2 (wildcard)
     * then we say its an identity, and we will return a {@link Result1} on each iteration with a {@link com.neocoretechs.relatrix.Relation} relationship object.
     * @param dmr_return our 4 element array of element 0 counter, and element 1-3 of 0 (object), 1 (? return tuple) or 2 (wildcard)
     * @return true if element 0 is -1, or any element 1-3 is 1. We then consider it an identity {@link com.neocoretechs.relatrix.AbstractRelation}
     */
    public static boolean isIdentity(short[] dmr_return) {
    	if( dmr_return[0] == (-1) ) return true;
    	for(int i = 1; i < 4; i++) {
    		if( dmr_return[i] == 1 ) return false; // 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
    	}
    	return true;
    }
    /**
     * Determine if a range search has produced an element in range, since we deal with headSet, tailSets and subSets we have
     * to check our iterator to keep it in range for concrete object keys.
     * @param template The template {@link com.neocoretechs.relatrix.AbstractRelation} to match with the record
     * @param record The record AbstractRelation matched against the template
     * @param dmr_return For each element of the array, 0 is counter,for elements 1-3, 0 means object, 2 means its a wildcard *
     * @return true if for each template domain, map, range key that is not null, dmr_return 1-3 is 0 for domain, map, range, and template key matches record key
     */
    protected static boolean templateMatches(AbstractRelation template, AbstractRelation record, short[] dmr_return) {
    	if( DEBUG )
    		System.out.println("RelatrixIterator.templateMatches template:"+template+" record:"+record+" dmr_return:"+Arrays.toString(dmr_return));
    	if(template.getDomainKey() != null)
    		if( dmr_return[1] == 0 && template.getDomainKey().compareTo(record.getDomainKey()) != 0 ) return false;
    	if(template.getMapKey() != null)
    		if( dmr_return[2] == 0 && template.getMapKey().compareTo(record.getMapKey()) != 0 ) return false;
    	if(template.getRangeKey() != null)
    		if( dmr_return[3] == 0 && template.getRangeKey().compareTo(record.getRangeKey()) != 0) return false;
    	return true;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(this.getClass().getName());
    	sb.append(" hasNext:");
    	sb.append(iter == null ? "iter NULL" : iter.hasNext());
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
