package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
/**
 * Used by the category theoretic Relatrix server to produce headsets for remote delivery.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class RemoteHeadSetIteratorTransaction extends RelatrixTransactionStatement implements RemoteObjectInterface {
	private static final long serialVersionUID = -7652502684740120088L;
	public RemoteHeadSetIteratorTransaction(String xid, String session) {
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
			Object itInst = RelatrixTransactionServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixTransactionServer.relatrixHeadsetMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

	@Override
	public void close() {
		RelatrixTransactionServer.sessionToObject.remove(getSession());
	}
}
