package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteKVIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Used by RelatrixKVTransactionServer to produce key/value tailmaps for remote delivery.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2020,2022
 *
 */
public class RemoteTailMapKVIteratorTransaction extends RemoteKVIteratorTransaction {
	private static final long serialVersionUID = -7652502684740120087L;
	public RemoteTailMapKVIteratorTransaction(TransactionId xid, String session) {
		super(xid,session);
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
			Object result = RelatrixKVTransactionServer.relatrixTailmapKVMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
