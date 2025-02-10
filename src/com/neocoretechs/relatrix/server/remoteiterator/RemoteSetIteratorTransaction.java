package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Used to produce {@link RelatrixTransactionServer} triplesets for remote delivery.
 * Created from the {@link com.neocoretechs.relatrix.client.RelatrixTransactionStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff (C) NeoCoreTechs 2024
 *
 */
public class RemoteSetIteratorTransaction extends RemoteIteratorTransaction {
	private static final long serialVersionUID = -7652502684740120087L;
	public RemoteSetIteratorTransaction(TransactionId xid, String session) {
		super(xid,session);
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
			Object result = RelatrixTransactionServer.relatrixSetMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
