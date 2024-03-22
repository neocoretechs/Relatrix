package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
/**
 * Our main representable analog. Instances of this class deliver the set of identity {@link Morphism}s, or
 * deliver sets of compositions of {@link Morphism}s representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link com.neocoretechs.relatrix.Result} elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the headset is retrieved.<p/>
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a {@link RelatrixIterator} is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a {@link com.neocoretechs.relatrix.Result1} of one element. For findSet("?",object,"?") we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class RelatrixHeadsetIterator implements Iterator<Result> {
	public static boolean DEBUG = true;
	protected String alias = null;
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
   
    protected long maxReturnKeys = 0L;
    protected long keysReturned = 0L;
    
    
    public RelatrixHeadsetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template the findset operators and/or concrete object instances from findSet call
     * @param templateo the endargs lower bound original findset call
     * @param templatep the endargs upper bound (if any) from original findset else null
     * @param dmr_return findSet operator order and tuple return control
     * @throws IOException 
     */
    public RelatrixHeadsetIterator(Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException {
    	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.dmr_return = dmr_return;
       	this.base = template;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
    		if(templateo.getDomain() != null)
    			RelatrixKV.findHeadMapKVStream(templateo.getDomain()).forEach(e -> {
    				DBKey dkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(dkeys.compareTo(dkeyLo) < 0)
    					dkeyLo = dkeys;	
    				if(dkeys.compareTo(dkeyHi) > 0)
    					dkeyHi = dkeys;
    				dkey.add(dkeys);
    			});
    		if(templateo.getMap() != null)
    			RelatrixKV.findHeadMapKVStream(templateo.getMap()).forEach(e -> {
    				DBKey mkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
       				if(mkeys.compareTo(mkeyLo) < 0)
    					mkeyLo = mkeys;	
    				if(mkeys.compareTo(mkeyHi) > 0)
    					mkeyHi = mkeys;
    				mkey.add(mkeys);
    			});
    		if(templateo.getRange() != null)
    			RelatrixKV.findHeadMapKVStream(templateo.getRange()).forEach(e -> {
    				DBKey rkeys = ((Map.Entry<Comparable,DBKey>)e).getValue();
    				if(rkeys.compareTo(rkeyLo) < 0)
    					rkeyLo = rkeys;	
    				if(rkeys.compareTo(rkeyHi) > 0)
    					rkeyHi = rkeys;  				
    				rkey.add(rkeys);
    			});
    		
    		if(DEBUG)
    			System.out.println("Keys:"+dkey.size()+", "+mkey.size()+", "+rkey.size());
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
    	try {
    		// stream of DBKeys in Morphism relation, and primary key to said Morphism
    		RelatrixKV.findSubMapKVStream(xdmr, ydmr).forEach(e ->{
    			Map.Entry<Morphism,DBKey> m = (Map.Entry<Morphism,DBKey>)e;
    			Result3 r = new Result3();
    			boolean insert = true;
    			r.set(0,0);
    			if(dkey.size() > 0) {
    				// does our Morphism domain key exist in headSet of designated headset domain objects, if any?
    				int insd = dkey.indexOf(m.getKey().getDomainKey());
    				// no, this Morphism is not eligible
    				if(insd == -1)
    					insert = false;
    				else
    					// yes, set result index 0 to sort position of domain headset list key
    					r.set(0,insd);
    			}
    			r.set(1,0);
    			if(mkey.size() > 0 && insert) { // should we check map, and is this Morphism still eligible?
    				int insm = mkey.indexOf(m.getKey().getMapKey());
    				if(insm == -1)
    					insert = false;
    				else
    					r.set(1,insm);
    			}
    			r.set(2,0);
    			if(rkey.size() > 0 && insert) {
    				int insr = rkey.indexOf(m.getKey().getRangeKey());
    				if(insr == -1)
    					insert = false;
    				else
    					r.set(2,insr);
    			}
    			// now we have whether we should insert the primary key DBKey for this Morphism and a Result3 with ordering indexes
    			// if we skipped any indexes in result3, they should be 0
    			if(insert)
    				resultSet.put(r, m.getValue());
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
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
			System.out.println("RelatrixHeadsetIterator hasNext:"+iter.hasNext()+" needsIter:"+needsIter+" buffer:"+buffer+" template:"+base);
    }
    
    public RelatrixHeadsetIterator(String alias, Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.alias = alias;
     	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.buffer = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
  
    }
    
	@Override
	public boolean hasNext() {
		if( DEBUG )
			System.out.println("RelatrixHeadsetIterator.hasNext() "+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		return needsIter;
	}

	@Override
	public Result next() {
		try {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
	    			System.out.println("RelatrixHeadsetIterator.next() before iteration hasNext::"+iter.hasNext()+" needsIter:"+needsIter+", buffer:"+buffer+", nextit"+nextit);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
	    		try {
					nextit = (Morphism) RelatrixKV.get((Comparable<?>) iter.next()); // primary DBKey for Morphism
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
		if( DEBUG ) {
			System.out.println("RelatrixIterator.next() template match after iteration hasNext:"+iter.hasNext()+", needsIter:"+needsIter+", buffer:"+buffer+", nextit:"+nextit);
		}
		return iterateDmr();
		
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	

	/**
	 * iterate_dmr - return proper domain, map, or range
	 * based on dmr_return values.  In dmr_return, value 0
	 * is iterator for ?,*.  1-3 BOOLean for d,m,r return yes/no
	 * @return the next location to retrieve or null, the only time its null is when we exhaust the buffered tuples
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	private Result iterateDmr() throws IllegalAccessException, IOException
	{
		++keysReturned;
	    Result tuples = RelatrixIterator.getReturnTuples(dmr_return);
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples.set(0, buffer);
	    	if(DEBUG)
				System.out.println("RelatrixHeadSetIterator iterateDmr returning identity tuples:"+tuples);
	    	return tuples;
	    }
	    dmr_return[0] = 0;
	    for(int i = 0; i < tuples.length(); i++)
	    	tuples.set(i, buffer.iterate_dmr(dmr_return));
		if(DEBUG)
			System.out.println("RelatrixHeadSetIterator iterateDmr returning tuples:"+tuples);
		return tuples;
	}
}
