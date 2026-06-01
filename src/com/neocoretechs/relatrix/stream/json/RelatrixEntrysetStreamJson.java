package com.neocoretechs.relatrix.stream.json;

import java.io.IOException;

import java.util.NoSuchElementException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.iterator.json.RelatrixEntrysetIteratorJson;

/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2021
 *
 */
public class RelatrixEntrysetStreamJson<T> extends RelatrixStreamJson<T> {
	private static boolean DEBUG = false;
 
	public RelatrixEntrysetStreamJson() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStreamJson(Class c) throws IOException {
    	super(new RelatrixEntrysetIteratorJson(c));
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStream "+stream);
    }
    
    public RelatrixEntrysetStreamJson(Comparable c) throws IOException {
    	this(c.getClass());
    }
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetStreamJson(Alias alias, Class c) throws IOException, NoSuchElementException {
    	super(new RelatrixEntrysetIteratorJson(alias, c));
    	if( DEBUG )
			System.out.println("RelatrixEntrysetStream "+stream);
    }
    
    public RelatrixEntrysetStreamJson(Alias alias, Comparable c) throws IOException, NoSuchElementException {
    	this(alias, c.getClass());
    }
    
}
