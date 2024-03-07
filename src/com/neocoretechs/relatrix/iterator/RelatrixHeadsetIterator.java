package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
/**
 * Our main representable analog. Instances of this class deliver the set of identity morphisms, or
 * deliver sets of compositions of morphisms representing new group homomorphisms as functors. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity morphism (instance of Morphism) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, that being the identity morphism.
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Here, the headset, or from beginning to the template element, is retrieved.
 * The critical element about retrieving relationships is to remember that the number of elements from each passed
 * iteration of a RelatrixIterator is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSet("*","?","*") we get back a {@link com.neocoretechs.relatrix.Result1} of one element. For findSet("?",object,"?") we
 * would get back a {@link com.neocoretechs.relatrix.Result2}, with each element containing the relationship returned.<br/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class RelatrixHeadsetIterator implements Iterator<Result> {
	private static boolean DEBUG = true;
	protected String alias = null;
	protected Iterator iter1, iter2, iter3;
	protected Morphism target = new DomainMapRange();
	protected Morphism template;
    protected Morphism buffer = null;
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = false; // for retrieved DMR buffer
    protected boolean needsIter1 = true; // for domain
    protected boolean needsIter2 = true; // for map, range ALWAYS needs iterated
    protected boolean identity = false;
    protected boolean returnedIdentity = false;
    
    public RelatrixHeadsetIterator() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixHeadsetIterator(Morphism template, short[] dmr_return) throws IOException {
    	if(DEBUG)
    		System.out.printf("%s %s %s%n", this.getClass().getName(), template, Arrays.toString(dmr_return));
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
			//iter = RelatrixKV.findHeadMap(template);
    		if(dmr_return[1] != 0)
    			iter1 = RelatrixKV.findHeadMap(template.getDomain());
    		else
    			target.setDomain(template.getDomain());
    		if(dmr_return[2] != 0)
    			iter2 = RelatrixKV.findHeadMap(template.getMap());
    		else
    			target.setMap(template.getMap());
    		if(dmr_return[3] != 0)
    			iter3 = RelatrixKV.findHeadMap(template.getRange());
    		else 
    			target.setRange(template.getRange());
    		if(DEBUG)
    			System.out.println(this.getClass().getName()+" "+iter1+" "+iter2+" "+iter3+" "+template);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
    public RelatrixHeadsetIterator(String alias, Morphism template, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.alias = alias;
    	this.template = template;
    	this.dmr_return = dmr_return;
    	identity = RelatrixIterator.isIdentity(this.dmr_return);
    	try {
			//iter = RelatrixKV.findHeadMap(alias, template);
      		if(dmr_return[1] != 0)
      			iter1 = RelatrixKV.findHeadMap(alias,template.getDomain());
      		else
    			target.setDomain(template.getDomain());
    		if(dmr_return[2] != 0)
    			iter2 = RelatrixKV.findHeadMap(alias,template.getMap());
       		else
    			target.setMap(template.getMap());
       		if(dmr_return[3] != 0)
       			iter3 = RelatrixKV.findHeadMap(alias,template.getRange());
      		else 
    			target.setRange(template.getRange());
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    
	@Override
	public boolean hasNext() {
		return iter1.hasNext();
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
		if(DEBUG)
			System.out.println("NextGeneric");
			while(true) {
				//buffer = (Morphism)iter.next();
				if(dmr_return[1] != 0 && needsIter1) {
					if(iter1.hasNext()) {
						target.setDomain((Comparable<?>) iter1.next());
						if(DEBUG)
							System.out.println("NextGeneric set domain:"+target);
						needsIter1 = false;
						//
					} else {
						if(DEBUG)
							System.out.println("NextGeneric iter1 return null");
						return null;
					}
				}
				if(dmr_return[2] != 0 && needsIter2) {
					if(iter2.hasNext()) {
						target.setMap((Comparable<?>) iter2.next());
						if(DEBUG)
							System.out.println("NextGeneric set map:"+target);
						needsIter2 = false;
					} else {
						needsIter1 = true;
						needsIter2 = true;
						try {
							iter2 = RelatrixKV.findHeadMap(template.getMap());
							iter3 = RelatrixKV.findHeadMap(template.getRange());
						} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
							throw new RuntimeException(e);
						}
						if(DEBUG)
							System.out.println("NextGeneric iter2 continue after reset iter2 iter3");
						continue;
					}
				}
				//
				if(dmr_return[3] != 0 && iter3.hasNext()) {
					target.setRange((Comparable<?>) iter3.next());
				} else {
					needsIter2 = true;
					try {
						iter3 = RelatrixKV.findHeadMap(template.getRange());
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
							throw new RuntimeException(e);
					}
					if(DEBUG)
						System.out.println("iter3 reset, continue");
					continue;
				}
				try {
					buffer = (Morphism) RelatrixKV.get(target);
					if(buffer != null) {
						if(DEBUG)
							System.out.println("Found target:"+buffer+", breaking");
						break;
					}
				} catch (IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
				}
			}
		try {
			return iterateDmr();
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Result nextAlias() {
		if( buffer == null || needsIter) {
			//buffer = (Morphism)iter.next();
			if(dmr_return[1] != 0 && needsIter1) {
				if(iter1.hasNext()) {
					target.setDomain(alias,(Comparable<?>) iter1.next());
					needsIter1 = false;
				} else
					return null;
			}
			if(dmr_return[2] != 0 && needsIter2) {
				if(iter2.hasNext()) {
					target.setMap(alias,(Comparable<?>) iter2.next());
					needsIter2 = false;
				} else {
					needsIter1 = true;
					needsIter2 = true;
					try {
						iter2 = RelatrixKV.findHeadMap(alias,template.getMap());
					} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
						throw new RuntimeException(e);
					}
					return nextAlias();
				}		
			}
			if(dmr_return[3] != 0 && iter3.hasNext()) {
				target.setRange(alias,(Comparable<?>) iter3.next());
			} else {
				needsIter2 = true;
				try {
					iter3 = RelatrixKV.findHeadMap(alias,template.getRange());
				} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
					throw new RuntimeException(e);
				}
				return nextAlias();
			}
			try {
				buffer = (Morphism) RelatrixKV.get(alias,target);
				if(buffer == null)
					return nextAlias();
			} catch (IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			return iterateDmr();
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return null;
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
		int returnTupleCtr = 0;
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
