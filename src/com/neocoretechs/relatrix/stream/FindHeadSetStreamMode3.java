package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;

/**
* Find the head set of objects in the relation via the specified predicate. Legal permutations are:<br/>
* *,[object],[object] <br/>
* *,?,[object],[object] <br/>
* *,[TemplateClass],[TemplateClass] <br/>
* *,?,[TemplateClass],[TemplateClass] <br/>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021 
*/
public class FindHeadSetStreamMode3 extends FindSetStreamMode3 {
	Object[] endarg;
    public FindHeadSetStreamMode3(char dop, Object marg, Object rarg, Object ... endarg) { 	
    	super(dop, marg, rarg);
      	if(endarg.length != 1)
    		throw new RuntimeException("Must supply 1 qualifying argument for Headset domain.");
     	this.endarg = endarg;
    }
	/**
	 * Create the specific stream. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Stream<?> createRelatrixStream(Morphism tdmr) throws IllegalAccessException, IOException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain((Comparable) RelatrixKV.lastKey((Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain((Comparable)endarg[0]);
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
	    return new RelatrixHeadsetStream(tdmr, xdmr, ydmr, dmr_return);
	}
	/**
	 * Create the specific stream. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
    @Override
	protected Stream<?> createRelatrixStream(String alias, Morphism tdmr) throws IllegalAccessException, IOException, NoSuchElementException {
		Morphism xdmr = null;
		Morphism ydmr = null;
		try {
			xdmr = (Morphism) tdmr.clone();
			ydmr = (Morphism) tdmr.clone();
		} catch (CloneNotSupportedException e) {}
    	if(tdmr.getDomain() == null) {
			if(endarg[0] instanceof Class) {
				tdmr.setDomain(alias,(Comparable) RelatrixKV.lastKey(alias,(Class)endarg[0]));
				xdmr.setDomainKey(DBKey.nullDBKey); // full range
				ydmr.setDomainKey(DBKey.fullDBKey);
			} else {
				tdmr.setDomain(alias,(Comparable)endarg[0]);
				xdmr.setDomainKey(tdmr.getDomainKey());
				ydmr.setDomainKey(tdmr.getDomainKey());
			}
		} else
			throw new IllegalAccessException("Improper Morphism template.");
    	return new RelatrixHeadsetStream(alias, tdmr, xdmr, ydmr, dmr_return);
	}
}
