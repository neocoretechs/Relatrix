package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteKVIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Used by the Key/Value RelatrixKVTRansactionServer to produce entry sets for remote delivery.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class RemoteEntrySetKVIteratorTransaction extends RemoteKVIteratorTransaction {
	private static boolean DEBUG = false;
	private static final long serialVersionUID = 1206621317830948409L;
	public RemoteEntrySetKVIteratorTransaction(TransactionId xid, String session) {
		super(xid,session);
		paramArray = new Object[0];
		setSession(session);
		this.xid = xid;
		if(DEBUG)
			System.out.println(this);
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
			//System.out.println(itInst+" class:"+itInst.getClass());
			Object result = RelatrixKVTransactionServer.relatrixEntrysetMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}


}
