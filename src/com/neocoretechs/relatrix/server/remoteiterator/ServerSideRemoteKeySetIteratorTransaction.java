package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * Used by the RelatrixTransactionServer to produce key sets for remote delivery.
 * Created from the {@link RelatrixTransactionStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class ServerSideRemoteKeySetIteratorTransaction extends RemoteIteratorTransaction {
	private static final long serialVersionUID = -8792606536289761224L;
	private static boolean DEBUG = false;
	public static ServerInvokeMethod relatrixKeysetMethods = null; // Keyset iterator methods
	
	public ServerSideRemoteKeySetIteratorTransaction(String session) throws ClassNotFoundException {
		super(session);
		if(relatrixKeysetMethods == null)
			relatrixKeysetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction", 0);
		if(DEBUG)
			System.out.println(this);
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
			//System.out.println(itInst+" class:"+itInst.getClass());
			Object result = relatrixKeysetMethods.invokeMethod(this, itInst);
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
