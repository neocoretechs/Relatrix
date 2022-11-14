package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;


/**
* Mode 2 find returns a headSet in map, domain, range order. The map value is matched against the constructor
* value. 
* Find the set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],* <br/>
* *,[object],? <br/>
* ?,[object],? <br/>
* ?,[object],* <br/>
* *,[TemplateClass],* <br/>
* *,[TemplateClass],? <br/>
* ?,[TemplateClass],? <br/>
* ?,[TemplateClass],* <br/>
*  <p/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode2Transaction extends FindHeadSetStreamMode2 {
	String xid;
    public FindHeadSetStreamMode2Transaction(String xid, char dop, Object marg, char rop) { 	
    	super(dop, marg, rop);
    	this.xid = xid;
    }
    /**
     * @return Stream for the set, each stream return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		return new RelatrixHeadsetStreamTransaction(xid, tdmr, dmr_return);
	}
}
