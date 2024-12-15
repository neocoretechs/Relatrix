package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;


/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixEntrysetIterator implements Iterator<Comparable> {
	private static boolean DEBUG = false;
	protected Iterator iter;
    protected Comparable buffer = null;
    protected Comparable nextit = null;
    protected boolean needsIter = true;
    protected Alias alias = null;
    
    public RelatrixEntrysetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetIterator(Class c) throws IOException {
    	try {
			iter = RelatrixKV.entrySet(c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof Morphism) {
				((Morphism)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
			}
    	if( DEBUG )
			System.out.printf("%s hasNext=%b needsIter=%b %s%n",this.getClass().getName(),iter.hasNext(),needsIter,buffer);
    	}
    }
    
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetIterator(Alias alias, Class c) throws IOException, NoSuchElementException {
    	this.alias = alias;
    	try {
			iter = RelatrixKV.entrySet(alias, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof Morphism) {
				((Morphism)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
				((Morphism)((Map.Entry)buffer).getKey()).setAlias(alias);
			}
    	if( DEBUG )
    		System.out.printf("%s hasNext=%b needsIter=%b %s%n",this.getClass().getName(),iter.hasNext(),needsIter,buffer);
    	}
    }
    
    public RelatrixEntrysetIterator(Comparable c) throws IOException {
    	this(c.getClass());
    }
    
    public RelatrixEntrysetIterator(Alias alias, Comparable c) throws IOException, NoSuchElementException {
    	this(alias, c.getClass());
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.printf("%s hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
		return needsIter;
	}

	@Override
	public Comparable next() {
		if(buffer == null || needsIter) {
			if( DEBUG ) {
				System.out.printf("%s.next before iter hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
				nextit = (Comparable)iter.next();
				if(((Map.Entry)nextit).getKey() instanceof Morphism) {
					((Morphism)((Map.Entry)nextit).getKey()).setIdentity((DBKey)((Map.Entry)nextit).getValue());
					((Morphism)((Map.Entry)nextit).getKey()).setAlias(alias);
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
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

}
