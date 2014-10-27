package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import com.neocoretechs.bigsack.iterator.TailSetIterator;
import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.relatrix.DMRStruc;
/**
 * Our main forgetful functor. Instances of this class deliver the set of identity morphisms, or
 * deliver sets of compositions of morphisms representing new group homomorphisms. More plainly, an array of iterators is returned representing the
 * N return tuple '?' elements of the query. If its an identity functor (instance of DMRStruc) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, that being the identity functors.
 * For tuples the array size is relative to the '?' query predicates. 
 * @author jg
 *
 */
public class RelatrixIterator implements Iterator<Comparable[]> {
	protected TailSetIterator iter;
    protected DMRStruc buffer = null;
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = false;
    protected boolean identity = false;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixIterator(BufferedTreeSet bts, DMRStruc template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	identity = isIdentity(this.dmr_return);
    	iter = (TailSetIterator) bts.tailSet(template);
    }
    
	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public Comparable[] next() {
		if( buffer == null || needsIter) {
			buffer = (DMRStruc)iter.next();
			needsIter = false;
		}
		return iterateDmr();
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
	*/
	private Comparable[] iterateDmr()
	{
		int returnTupleCtr = 0;
	    Comparable[] tuples = new Comparable[getReturnTuples(dmr_return)];
		//System.out.println("IterateDmr "+dmr_return[0]+" "+dmr_return[1]+" "+dmr_return[2]+" "+dmr_return[3]);
	    // no return vals? send back Relate location
	    if( identity ) {
	    	tuples[0] = buffer;
	    	needsIter = true;
	    	return tuples;
	    }
	    dmr_return[0] = 0;
		do {
			dmr_return[0]++;
	        if( dmr_return[dmr_return[0]] != 0)
	        	tuples[returnTupleCtr++] = buffer.returnTupleOrder(dmr_return[0]);
	    } while( dmr_return[0] < 3 );
		needsIter = true;
		return tuples;
	}
	/**
	 * Return the number of tuple elements to be returned from specified query in each iteration
	 * @param dmr_return
	 * @return
	 */
	protected static short getReturnTuples(short[] dmr_return) {
		short cnt = 0;
		if( dmr_return[0] == -1 ||  isIdentity(dmr_return) ) // return all relationship types, 1 tuple special case
			return 1;
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] > 0 )++cnt;
		}
		return cnt;
	}
	
	protected static boolean isIdentity(short[] dmr_return) {
	    return ( dmr_return[0] == (-1) || (dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0));
	}
	

}
