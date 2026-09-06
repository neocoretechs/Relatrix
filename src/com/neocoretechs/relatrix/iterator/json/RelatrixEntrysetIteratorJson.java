package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.relatrix.server.ServerMethod;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys in a Json context
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2026
 *
 */
public class RelatrixEntrysetIteratorJson implements Iterator<Comparable> {
	private static boolean DEBUG = false;
	protected Iterator iter;
    protected Comparable buffer = null;
    protected Comparable nextit = null;
    protected boolean needsIter = true;
    protected Alias alias = null;
    protected IndexResolver indexResolver;
    
    public RelatrixEntrysetIteratorJson() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param ctx TODO
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetIteratorJson(Class c, ParallelExecutionContext ctx) throws IOException {
    	this.indexResolver = ctx.resolver();
    	try {
			iter = RelatrixKVJson.entrySet(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof AbstractRelation) {
				((AbstractRelation)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
				((AbstractRelation)((Map.Entry)buffer).getKey()).setResolver(indexResolver);
			}
    	if( DEBUG )
			System.out.printf("%s hasNext=%b needsIter=%b %s%n",this.getClass().getName(),iter.hasNext(),needsIter,buffer);
    	}
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param alias the database alias
     * @param c The class we are retrieving
     * @param ctx TODO
     * @throws IOException for low level fail
     */
    public RelatrixEntrysetIteratorJson(Alias alias, Class c, ParallelExecutionContext ctx) throws IOException {
    	this.alias = alias;
    	this.indexResolver = ctx.resolver();
    	try {
			iter = RelatrixKVJson.entrySet(alias, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof AbstractRelation) {
				((AbstractRelation)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
				((AbstractRelation)((Map.Entry)buffer).getKey()).setAlias(alias);
				((AbstractRelation)((Map.Entry)buffer).getKey()).setResolver(indexResolver);
			}
    	if( DEBUG )
    		System.out.printf("%s hasNext=%b needsIter=%b %s%n",this.getClass().getName(),iter.hasNext(),needsIter,buffer);
    	}
    }
    
    
	@Override
	@ServerMethod
	public boolean hasNext() {
		if( DEBUG )
			System.out.printf("%s hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
		return needsIter;
	}

	@Override
	@ServerMethod
	public Comparable next() {
		if(buffer == null || needsIter) {
			if( DEBUG ) {
				System.out.printf("%s.next before iter hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
				nextit = (Comparable)iter.next();
				if(((Map.Entry)nextit).getKey() instanceof AbstractRelation) {
					((AbstractRelation)((Map.Entry)nextit).getKey()).setIdentity((DBKey)((Map.Entry)nextit).getValue());
					((AbstractRelation)((Map.Entry)nextit).getKey()).setAlias(alias);
					((AbstractRelation)((Map.Entry)nextit).getKey()).setResolver(indexResolver);
				}
			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUG ) {
			System.out.printf("%s after iter hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
		}
		return buffer;
	}

	@Override
	@ServerMethod
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

}
