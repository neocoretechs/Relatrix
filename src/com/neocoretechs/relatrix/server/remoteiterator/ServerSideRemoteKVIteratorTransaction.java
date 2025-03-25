package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.TransactionId;
import com.neocoretechs.relatrix.client.RemoteKVIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;


public class ServerSideRemoteKVIteratorTransaction extends RemoteKVIteratorTransaction {
	private static final long serialVersionUID = -1003043201216184312L;
	public static ServerInvokeMethod relatrixIteratorMethods = null;
	
	public ServerSideRemoteKVIteratorTransaction(TransactionId xid, String session) throws ClassNotFoundException {
		super(xid, session);
		if(relatrixIteratorMethods == null)
			relatrixIteratorMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.IteratorWrapper", 0);
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
