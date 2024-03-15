package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
/**
 * Our main representable analog. Instances of this class deliver the set of identity {@link Morphism}s, or
 * deliver sets of compositions of {@link Morphism}s representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned {@link com.neocoretechs.relatrix.Result} elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the tailset is retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findTailSet("*","?","*") we get back a {@link com.neocoretechs.relatrix.Result1} of one element. For findTailSet("?",object,"?") we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2024
 *
 */
public class RelatrixTailsetIterator implements Iterator<Result> {
	public static boolean DEBUG = false;
	protected String alias = null;
	protected Iterator iter1, iter2;//, iter3;
	protected Morphism buffer = new DomainMapRange();
	protected Morphism template;
 
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = false; // for retrieved DMR buffer
    protected boolean needsIter1 = false; // for domain
    protected boolean needsIter2 = false; //
    protected boolean identity = false;

    protected boolean resultReturn = false;
    protected Result returnedResult = null;
    
    protected ArrayList<DBKey> dkey = new ArrayList<DBKey>();
    protected ArrayList<DBKey> mkey = new ArrayList<DBKey>();
 
    protected int primaryKeyd = 0;
    protected int primaryKeym = 0;
    
    protected long maxReturnKeys = 0L;
    protected long keysReturned = 0L;
    
    
    public RelatrixTailsetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param templateo 
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixTailsetIterator(Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException {
    	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
    		Iterator it = RelatrixKV.findSubMap(templateo, templatep);// subset of partial and full keys of Morphism subclass
    		while(it.hasNext()) {
    			Morphism m = (Morphism) it.next();
    			if(dmr_return[1] == 0) {
    				if(!dkey.contains(m.getDomainKey()) && m.domainKeyEquals(templateo))
    					dkey.add(m.getDomainKey());
    			} else {
    				if(!dkey.contains(m.getDomainKey()))
    					dkey.add(m.getDomainKey());
    			}
    			if(dmr_return[2] == 0) {
    				if(!mkey.contains(m.getMapKey()) && m.mapKeyEquals(templateo))
    					mkey.add(m.getMapKey());
    			} else {
    				if(!mkey.contains(m.getMapKey()))
    					mkey.add(m.getMapKey());
    			}
        		++maxReturnKeys; // maximum possible key unique primary key combinations
    			//if(DEBUG)
    			//System.out.println("Adding keys:"+m.getDomainKey()+", "+m.getMapKey());
    		}
    		if(DEBUG)
    			System.out.println("Keys:"+dkey.size()+", "+mkey.size());
    		if(dmr_return[1] != 0) {
    			iter1 = RelatrixKV.findTailMapKV(template.getDomain());
    			needsIter1 = true;
    		} else {
    			buffer.setDomain(template.getDomain());
    		}
    		if(dmr_return[2] != 0) {
    			iter2 = RelatrixKV.findTailMapKV(template.getMap());
    			needsIter2 = true;
    		} else {
    			buffer.setMap(template.getMap());
    		}
    		if(dmr_return[3] == 0)
    			buffer.setRange(template.getRange());
    		if(DEBUG)
    			System.out.println(this.getClass().getName()+" "+iter1+" "+iter2+/*" "+iter3+*/" "+template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
    public RelatrixTailsetIterator(String alias, Morphism template, Morphism templateo, Morphism templatep, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.alias = alias;
     	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
    		Iterator it = RelatrixKV.findSubMap(alias, templateo, templatep);// subset of partial and full keys of Morphism subclass
    		while(it.hasNext()) {
    			Morphism m = (Morphism) it.next();
    			if(dmr_return[1] == 0) {
    				if(!dkey.contains(m.getDomainKey()) && m.domainKeyEquals(templateo))
    					dkey.add(m.getDomainKey());
    			} else {
    				if(!dkey.contains(m.getDomainKey()))
    					dkey.add(m.getDomainKey());
    			}
    			if(dmr_return[2] == 0) {
    				if(!mkey.contains(m.getMapKey()) && m.mapKeyEquals(templateo))
    					mkey.add(m.getMapKey());
    			} else {
    				if(!mkey.contains(m.getMapKey()))
    					mkey.add(m.getMapKey());
    			}
           		++maxReturnKeys; // maximum possible key unique primary key combinations
    			//if(DEBUG)
    			//System.out.println("Adding keys:"+m.getDomainKey()+", "+m.getMapKey());
    		}
    		if(DEBUG)
    			System.out.println("Keys:"+dkey.size()+", "+mkey.size());
    		if(dmr_return[1] != 0) {
    			iter1 = RelatrixKV.findTailMapKV(alias, template.getDomain());
    			needsIter1 = true;
    		} else {
    			buffer.setDomain(alias, template.getDomain());
    		}
    		if(dmr_return[2] != 0) {
    			iter2 = RelatrixKV.findTailMapKV(alias, template.getMap());
    			needsIter2 = true;
    		} else {
    			buffer.setMap(alias, template.getMap());
    		}
    		if(dmr_return[3] == 0)
    			buffer.setRange(alias, template.getRange());
    		if(DEBUG)
    			System.out.println(this.getClass().getName()+" "+iter1+" "+iter2+/*" "+iter3+*/" "+template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
	@Override
	public boolean hasNext() {
		if(keysReturned >= maxReturnKeys)
			return false;
		if(!resultReturn) {
			returnedResult = next();
			resultReturn = true;
		}
		return (returnedResult != null);
	}

	@Override
	public Result next() {
		if(alias != null)
			return nextAlias();
		return nextGeneric();
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove not supported for this iterator");	
	}
	
	private Result nextGeneric() {
		DBKey pk = null;
		if(DEBUG)
			System.out.println("NextGeneric");
		// from previous hasNext test
		if(resultReturn) {
			resultReturn = false;
			return returnedResult;
		}
		while(true) {
			pk = null;
			//buffer = (Morphism)iter.next();
			if( needsIter1) {
				if(iter1.hasNext()) {
					Map.Entry me = (Entry) iter1.next();
					if((primaryKeyd = dkey.indexOf(me.getValue())) == -1) {
						//if(DEBUG)
							//System.out.println("Didnt find domain "+me.getKey()+", "+me.getValue());
						continue;
					}
					//target.setDomain((Comparable<?>)me.getKey());
					buffer.setDomainKey((DBKey) me.getValue());
					buffer.setDomainResolved((Comparable<?>)me.getKey());
					if(DEBUG)
						System.out.println("NextGeneric set domain:"+buffer);
					needsIter1 = false;
					//
				} else {
					if(DEBUG)
						System.out.println("NextGeneric iter1 return null");
					return null;
				}
			}
			if(needsIter2) {
				if(iter2.hasNext()) {
					Map.Entry me = (Entry) iter2.next();
					if((primaryKeym = mkey.indexOf(me.getValue())) == -1) {
						//if(DEBUG)
							//System.out.println("Didnt find map "+me.getKey()+", "+me.getValue());
						continue;
					}
					//target.setMap((Comparable<?>) me.getKey());
					buffer.setMapKey((DBKey) me.getValue());
					buffer.setMapResolved((Comparable<?>)me.getKey());
					if(DEBUG)
						System.out.println("NextGeneric set map:"+buffer);
					needsIter2 = true;
				} else {
					if(iter1 != null)
						needsIter1 = true;
					needsIter2 = true;
					try {
						iter2 = RelatrixKV.findTailMapKV(template.getMap());
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					if(DEBUG)
						System.out.println("NextGeneric iter2 continue after reset iter2 iter3");
					continue;
				}
			} else {
				if(dmr_return[2] == 0 && iter1 != null)
					needsIter1 = true;
			}
			PrimaryKeySet pks = new PrimaryKeySet(buffer);
			try {
				pk = (DBKey) RelatrixKV.get(pks);
				// did not find primary key of domain,map, continue to next iteration of components
				if(pk == null) {
					if(DEBUG)
						System.out.println("Primary key lookup fail for "+buffer+", continue");
					continue;
				}
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			if(DEBUG)
				System.out.println("Target primary key:"+pk);
			try {
				buffer = (Morphism) RelatrixKV.get(pk);
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			} // lookup into DBKey tablespace with pk as key returning Morphism
			// this should not fail with null, we already did a lookup earlier
			if(DEBUG)
				System.out.println("Lookup result for buffer:"+buffer);
			// if range concrete and doesnt match retrieval, continue
			if(dmr_return[3] == 0 && !template.rangeKeyEquals(buffer))
				continue;
			break;
		} // while true
		try {
			return iterateDmr();
		} catch (IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Result nextAlias() {
		DBKey pk = null;
		if(DEBUG)
			System.out.println("NextAlias");
		// from previous hasNext test
		if(resultReturn) {
			resultReturn = false;
			return returnedResult;
		}
		while(true) {
			pk = null;
			if( needsIter1) {
				if(iter1.hasNext()) {
					Map.Entry me = (Entry) iter1.next();
					if((primaryKeyd = dkey.indexOf(me.getValue())) == -1) {
						continue;
					}
					buffer.setDomainKey((DBKey) me.getValue());
					buffer.setDomainResolved((Comparable<?>)me.getKey());
					if(DEBUG)
						System.out.println("NextAlias set domain:"+buffer);
					needsIter1 = false;
					//
				} else {
					if(DEBUG)
						System.out.println("NextAlias iter1 return null");
					return null;
				}
			}
			if(needsIter2) {
				if(iter2.hasNext()) {
					Map.Entry me = (Entry) iter2.next();
					if((primaryKeym = mkey.indexOf(me.getValue())) == -1) {
						//if(DEBUG)
							//System.out.println("Didnt find map "+me.getKey()+", "+me.getValue());
						continue;
					}
					buffer.setMapKey((DBKey) me.getValue());
					buffer.setMapResolved((Comparable<?>)me.getKey());
					if(DEBUG)
						System.out.println("NextAlias set map:"+buffer);
					needsIter2 = true;
				} else {
					if(iter1 != null)
						needsIter1 = true;
					needsIter2 = true;
					try {
						iter2 = RelatrixKV.findTailMapKV(alias, template.getMap());
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					if(DEBUG)
						System.out.println("NextAlias iter2 continue after reset iter2 iter3");
					continue;
				}
			} else {
				if(dmr_return[2] == 0 && iter1 != null)
					needsIter1 = true;
			}
			PrimaryKeySet pks = new PrimaryKeySet(buffer);
			try {
				pk = (DBKey) RelatrixKV.get(alias,pks);
				// did not find primary key of domain,map, continue to next iteration of components
				if(pk == null) {
					if(DEBUG)
						System.out.println("NextAlias Primary key lookup fail for "+buffer+", continue");
					continue;
				}
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			if(DEBUG)
				System.out.println("NextAlias Target primary key:"+pk);
			try {
				buffer = (Morphism) RelatrixKV.get(alias,pk); // get the main entry from the dbkey
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			} // lookup into DBKey tablespace with pk as key returning Morphism
			// this should not fail with null, we already did a lookup earlier
			if(DEBUG)
				System.out.println("NextAlias Lookup result for buffer:"+buffer);
			// if range concrete and doesnt match retrieval, continue
			if(dmr_return[3] == 0 && !template.rangeKeyEquals(buffer))
				continue;
			break;
		} // while true
		try {
			return iterateDmr();
		} catch (IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
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
				System.out.println("RelatrixTailSetIterator iterateDmr returning identity tuples:"+tuples);
	    	return tuples;
	    }
	    dmr_return[0] = 0;
	    for(int i = 0; i < tuples.length(); i++)
	    	tuples.set(i, buffer.iterate_dmr(dmr_return));
		if(DEBUG)
			System.out.println("RelatrixTailSetIterator iterateDmr returning tuples:"+tuples);
		return tuples;
	}
}
