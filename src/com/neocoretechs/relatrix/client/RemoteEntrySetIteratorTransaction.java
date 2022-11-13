package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
/**
 * Used by the Key/Value RelatrixKVServer to produce entry sets for remote delivery.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class RemoteEntrySetIteratorTransaction extends RelatrixKVTransactionStatement implements RemoteObjectInterface {
	private static final long serialVersionUID = 1206621317830948409L;
	public RemoteEntrySetIteratorTransaction(String session) {
		super();
		paramArray = new Object[0];
		setSession(session);
	}
	
	@Override
	public void process() throws Exception {
		if( this.methodName.equals("close") ) {
			close();
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

	@Override
	public void close() {
		RelatrixKVTransactionServer.sessionToObject.remove(getSession());
	}
}
