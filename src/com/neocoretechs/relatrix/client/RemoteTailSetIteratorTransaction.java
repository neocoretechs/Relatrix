package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
/**
 * Used to produce tailsets for remote delivery in a transaction context.
 * @author Jonathan Groff (C) NeoCoreTechs 2021,2022
 *
 */
public class RemoteTailSetIteratorTransaction extends RelatrixTransactionStatement implements RemoteObjectInterface {
	private static final long serialVersionUID = -7652502684740120088L;
	public RemoteTailSetIteratorTransaction(String session) {
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
			Object result = RelatrixTransactionServer.relatrixTailsetMethods.invokeMethod(this, itInst);
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
