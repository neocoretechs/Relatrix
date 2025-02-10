package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Used by RelatrixTransactionServer to produce subsets for remote delivery.
 * Created from the {@link RelatrixTransactionStatement} process method and setObjectReturn is then called to place it in the return.
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified.<p/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2020,2022
 *
 */
public class RemoteSubSetIteratorTransaction extends RemoteIteratorTransaction {
	private static final long serialVersionUID = -7652502684740120087L;
	public RemoteSubSetIteratorTransaction(TransactionId xid, String session) {
		super(xid, session);
	}

	@Override
	public void process() throws Exception {
		if( this.methodName.equals("close") ) {
			RelatrixTransactionServer.sessionToObject.remove(getSession());
		} else {
			// Get the iterator linked to this session
			Object itInst = RelatrixTransactionServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixTransactionServer.relatrixSubsetMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
