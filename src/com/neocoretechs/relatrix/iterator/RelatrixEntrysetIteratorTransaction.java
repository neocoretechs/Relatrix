package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.util.Map;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;



/**
 * Implementation of the standard Iterator interface which operates on K/V keys
 * to set the lower bound of the correct range search for the properly ordered set of  subclasses;
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020
 *
 */
public class RelatrixEntrysetIteratorTransaction extends RelatrixEntrysetIterator {
	private static boolean DEBUG = false;
	private TransactionId xid = null;
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param dmr_return
     * @throws IOException 
     */
    public RelatrixEntrysetIteratorTransaction(TransactionId xid, Class c) throws IOException {
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
    
    public RelatrixEntrysetIteratorTransaction(Alias alias, TransactionId xid, Class c) throws IOException {
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
		return buffer;
	}
 
}
