package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * Used by RelatrixTransactionServer to produce tailsets for remote delivery in a transaction context.
 * Created from the {@link RelatrixTransactionStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff (C) NeoCoreTechs 2021,2022
 *
 */
public class ServerSideRemoteTailSetIteratorTransaction extends RemoteIteratorTransaction {
	private static final long serialVersionUID = -7652502684740120088L;
	public static ServerInvokeMethod relatrixTailsetMethods = null; // FindTailset iterator methods
	
	public ServerSideRemoteTailSetIteratorTransaction(String session) throws ClassNotFoundException {
		super(session);
		if(relatrixTailsetMethods == null)
			relatrixTailsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction", 0);
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
			Object result = relatrixTailsetMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" "+super.toString();
	}

}
