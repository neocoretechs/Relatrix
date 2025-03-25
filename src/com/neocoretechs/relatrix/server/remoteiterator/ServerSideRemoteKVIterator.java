package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteKVIterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

public class ServerSideRemoteKVIterator extends RemoteKVIterator {
	private static final long serialVersionUID = -7415412359358145958L;
	public static ServerInvokeMethod relatrixIteratorMethods = null;
	
	public ServerSideRemoteKVIterator(String session) throws ClassNotFoundException {
		super(session);
		if(relatrixIteratorMethods == null) 
			relatrixIteratorMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.IteratorWrapper", 0);
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
			Object result = relatrixIteratorMethods.invokeMethod(this, itInst);
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
