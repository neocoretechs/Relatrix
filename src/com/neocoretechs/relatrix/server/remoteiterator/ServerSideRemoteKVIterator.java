package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteKVIterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;

public class ServerSideRemoteKVIterator extends RemoteKVIterator {
	private static final long serialVersionUID = -7415412359358145958L;

	public ServerSideRemoteKVIterator(String session) {
		super(session);
	}
	
	@Override
	public void process() throws Exception {
		if( this.methodName.equals("close") ) {
			RelatrixKVServer.sessionToObject.remove(getSession());
		} else {
			// Get the iterator linked to this session
			Object itInst = RelatrixKVServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			//System.out.println(itInst+" class:"+itInst.getClass());
			Object result = RelatrixKVServer.relatrixIteratorMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
