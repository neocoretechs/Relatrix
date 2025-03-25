package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * Used by the RelatrixTransactionServer to produce headsets for remote delivery.
 * Created from the {@link RelatrixTransactionStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class ServerSideRemoteHeadSetIteratorTransaction extends RemoteIteratorTransaction {
	private static final long serialVersionUID = -7652502684740120088L;
	public static ServerInvokeMethod relatrixHeadsetMethods = null; // FindHeadset iterator methods
	
	public ServerSideRemoteHeadSetIteratorTransaction(String session) throws ClassNotFoundException {
		super(session);
		if(relatrixHeadsetMethods == null)
			relatrixHeadsetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction", 0);
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
			Object result = relatrixHeadsetMethods.invokeMethod(this, itInst);
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
