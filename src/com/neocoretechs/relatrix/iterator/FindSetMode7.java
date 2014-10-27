package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.forgetfulfunctor.TemplateClass;

/**
* This winds up serving 2 functions. One is identity functor retrieval if we get 3 objects that are not
* part of TemplateClass retrieval. the second is when one or more params is instanceof TemplateClass. In the second
* instance, the class functions as template for the enclosed class to retrieve objects of that class (or subclass).
* Depending on the subclass of TemplateClass, the argument acts as a wildcard (*) or a tuple (?) for instances of that
* class. Category theory calls these forgetful functors. 
* 
*/
public class FindSetMode7 extends IteratorFactory {
	// mode 7
	Object darg,marg,rarg;
	short[] dmr_return = new short[4];
    public FindSetMode7(Object darg, Object marg, Object rarg) throws IllegalArgumentException, IOException { 	
    	this.darg = darg;
    	this.marg = marg;
    	this.rarg = rarg;
    	dmr_return[1] = 0;
        dmr_return[2] = 0;
        dmr_return[3] = 0;
        if( darg instanceof TemplateClass) {
        	dmr_return[1] = checkOp(((TemplateClass)darg).getRetrievalPredicate());
        }
        if( marg instanceof TemplateClass) {
        	dmr_return[2] = checkOp(((TemplateClass)marg).getRetrievalPredicate());
        }
        if( rarg instanceof TemplateClass) {
        	dmr_return[3] = checkOp(((TemplateClass)rarg).getRetrievalPredicate());
        }
        if( isReturnRelationships(dmr_return) )
        	dmr_return[0] = -1;
    }
    /**
     * @return Iterator for the set, each iterator return is a Comparable array of tuples of arity n=?'s
     */
	@Override
	public Iterator<?> createIterator() throws IllegalAccessException, IOException {
	    DMRStruc dmr = new DomainMapRange((Comparable)darg, (Comparable)marg, (Comparable)rarg);
	    return new RelatrixIterator(BigSackAdapter.getBigSackSet(dmr), dmr, dmr_return);
	}
}
