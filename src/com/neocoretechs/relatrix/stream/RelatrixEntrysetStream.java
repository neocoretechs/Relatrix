package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021
 *
 */
public class RelatrixEntrysetStream<T> extends RelatrixStream<T> {
	private static boolean DEBUG = false;
 
	public RelatrixEntrysetStream() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStream(Class c) throws IOException {
    	super(new RelatrixEntrysetIterator(c));
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStream "+stream);
    }
    
    public RelatrixEntrysetStream(Comparable c) throws IOException {
    	this(c.getClass());
    }
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStream(String alias, Class c) throws IOException, NoSuchElementException {
    	super(new RelatrixEntrysetIterator(alias, c));
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStream "+stream);
    }
    
    public RelatrixEntrysetStream(String alias, Comparable c) throws IOException, NoSuchElementException {
    	this(alias, c.getClass());
    }
    
}
