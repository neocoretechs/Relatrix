package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteKVIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.rocksack.TransactionId;
/**
 * USed by RelatrixKVTransactionServer for remote iterator delivery
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified.<p/>
 * Used by the Key/Value subsystem to produce submaps for remote delivery.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2020,2022
 *
 */
public class RemoteSubMapKVIteratorTransaction extends RemoteKVIteratorTransaction {
	private static final long serialVersionUID = -112309448424952343L;
	public RemoteSubMapKVIteratorTransaction(TransactionId xid, String session) {
		super(xid, session);
	}
	
	@Override
	public void process() throws Exception {
		if( this.methodName.equals("close") ) {
			RelatrixKVTransactionServer.sessionToObject.remove(getSession());
		} else {
			// Get the iterator linked to this session
			Object itInst = RelatrixKVTransactionServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixKVTransactionServer.relatrixSubmapKVMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
