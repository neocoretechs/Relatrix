package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Map;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.TransactionId;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;



/**
 * Implementation of the standard Iterator interface which operates on keys.
 * We have to retrieve the entrySet in case we need to set the identity of a Relation
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2025
 *
 */
public class RelatrixKeysetIteratorTransaction extends RelatrixKeysetIterator {
	private static boolean DEBUG = false;
	private TransactionId xid = null;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param xid the transaction Id
     * @param c The Class we are retrieving
     * @throws IOException 
     */
    public RelatrixKeysetIteratorTransaction(TransactionId xid, Class c) throws IOException {
    	this.xid = xid;
    	try {
			iter = RelatrixKVTransaction.entrySet(xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof AbstractRelation) {
				((AbstractRelation)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
				((AbstractRelation)((Map.Entry)buffer).getKey()).setTransactionId(xid);
			}
    	if( DEBUG )
			System.out.printf("%s xid=%s hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),xid,iter.hasNext(),needsIter,nextit,buffer);
    	}
    }
    /**
     * 
     * @param alias The database alias
     * @param xid The transaction Id
     * @param c The class we are retrieving
     * @throws IOException for low level Db fail
     */
    public RelatrixKeysetIteratorTransaction(Alias alias, TransactionId xid, Class c) throws IOException {
    	this.alias = alias;
    	this.xid = xid;
    	try {
			iter = RelatrixKVTransaction.entrySet(alias, xid, c);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
    	if( iter.hasNext() ) {
			buffer = (Comparable) iter.next();
			if(((Map.Entry)buffer).getKey() instanceof AbstractRelation) {
				((AbstractRelation)((Map.Entry)buffer).getKey()).setIdentity((DBKey)((Map.Entry)buffer).getValue());
				((AbstractRelation)((Map.Entry)buffer).getKey()).setAlias(alias);
				((AbstractRelation)((Map.Entry)buffer).getKey()).setTransactionId(xid);
			}
    	if( DEBUG )
			System.out.printf("%s xid=%s hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),xid,iter.hasNext(),needsIter,nextit,buffer);
    	}
    }
    
	public TransactionId getTransactionId() {
		return xid;
	}
	
	@Override
	@ServerMethod
	public Comparable next() {
		if( buffer == null || needsIter) {
			if( DEBUG ) {
				System.out.printf("%s.next before iter hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
			}
			if( nextit != null )
				buffer = nextit;
			
			if( iter.hasNext()) {
				nextit = (Comparable)iter.next();
				if(((Map.Entry)nextit).getKey() instanceof AbstractRelation) {
					((AbstractRelation)((Map.Entry)nextit).getKey()).setIdentity((DBKey)((Map.Entry)nextit).getValue());
					((AbstractRelation)((Map.Entry)nextit).getKey()).setAlias(alias);
					((AbstractRelation)((Map.Entry)nextit).getKey()).setTransactionId(xid);
				}
			} else {
				nextit = null;
				needsIter = false;
			}
		}
		// always return using this with non null buffer
		if( DEBUG ) {
			System.out.printf("%s after iter hasNext=%b needsIter=%b %s %s%n",this.getClass().getName(),iter.hasNext(),needsIter,nextit,buffer);
		}
		return (Comparable) ((Map.Entry)buffer).getKey();
	}

 
}
