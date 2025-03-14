package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.rocksack.Alias;

/**
 * Implementation of the standard Iterator interface which operates on keys.
 * We have to perform an entrySet on the KV to retrieve the DBKey in case we have to
 * set the identity of a relation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2025
 *
 */
public class RelatrixKeysetIterator implements Iterator<Comparable> {
	private static boolean DEBUG = false;
	protected Iterator<?> iter;
    protected Comparable buffer = null;
    protected Comparable nextit = null;
    protected boolean needsIter = true;
    protected Alias alias = null;
    
    public RelatrixKeysetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param c the Class to retrieve
     * @throws IOException 
     */
    public RelatrixKeysetIterator(Class c) throws IOException {
    	try {
			iter = RelatrixKV.entrySet(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof AbstractRelation) {
				((AbstractRelation)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
			}
    	if( DEBUG )
			System.out.printf("%s hasNext=%b needsIter=%b %s%n",this.getClass().getName(),iter.hasNext(),needsIter,buffer);
    	}
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param alias the database alias
     * @param c The class we are retrieving
     * @throws IOException 
     */
    public RelatrixKeysetIterator(Alias alias, Class c) throws IOException {
    	this.alias = alias;
    	try {
			iter = RelatrixKV.entrySet(alias, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof AbstractRelation) {
				((AbstractRelation)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
				((AbstractRelation)((Map.Entry)buffer).getKey()).setAlias(alias);
			}
    	if( DEBUG )
    		System.out.printf("%s hasNext=%b needsIter=%b %s%n",this.getClass().getName(),iter.hasNext(),needsIter,buffer);
    	}
    }
    
    public RelatrixKeysetIterator(Comparable c) throws IOException {
    	this(c.getClass());
    }
    
    public RelatrixKeysetIterator(Alias alias, Comparable c) throws IOException {
    	this(alias, c.getClass());
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
		return (Comparable) ((Map.Entry)buffer).getKey();
	}

	@Override
	@ServerMethod
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

}
