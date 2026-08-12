package com.neocoretechs.relatrix.iterator.json.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.iterator.json.FindSetMode3Json;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;
import com.neocoretechs.rocksack.TransactionId;

/**
* Find the set of objects in the relation via the specified predicate. Mode 3 = findSet('*',object,object)
* returns a 1 element {@link com.neocoretechs.relatrix.Result1} with the identity findSet("*",object,object) for all elements matching the
* last 2 objects.
Legal permutations are:<br>
* *,[object],[object] <br>
* *,[TemplateClass],[TemplateClass] <br>
* @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
* 
*/
public class FindSetMode3JsonTransaction extends FindSetMode3Json {
	// mode 3
	TransactionId xid;
    public FindSetMode3JsonTransaction(TransactionId transactionId, char dop, Object marg, Object rarg) { 	
    	super(dop, marg, rarg);
    	this.xid = transactionId;
    }
    
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
	    AbstractRelation dmr = new MapRangeDomain(true, null, xid, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixIterator(dmr, ctx);
	}
	
    @Override
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Iterator<?> createRelatrixIterator(AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException {
	    return new RelatrixIteratorJsonTransaction(xid, tdmr, dmr_return);
	}
    
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator(Alias alias, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
	    AbstractRelation dmr = new MapRangeDomain(true, alias, xid, null, (Comparable)marg, (Comparable)rarg);
	    return createRelatrixIterator(alias, dmr, ctx);
	}
	
    @Override
	/**
	 * Create the specific iterator. Subclass overrides for various set valued functions
	 * @param tdmr
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected Iterator<?> createRelatrixIterator(Alias alias, AbstractRelation tdmr, ParallelExecutionContext ctx) throws IllegalAccessException, IOException, NoSuchElementException {
	    return new RelatrixIteratorJsonTransaction(alias, xid, tdmr, dmr_return);
	}
}
