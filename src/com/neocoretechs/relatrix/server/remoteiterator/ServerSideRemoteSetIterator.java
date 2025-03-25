package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteIterator;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
/**
 * Used to produce RelatrixServer triplesets for remote delivery.
 * Created from the {@link RelatrixStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff (C) NeoCoreTechs 2024
 *
 */
public class ServerSideRemoteSetIterator extends RemoteIterator {
	private static final long serialVersionUID = -7652502684740120087L;
	public static ServerInvokeMethod relatrixSetMethods = null; // FindSet iterator methods
	
	public ServerSideRemoteSetIterator(String session) throws ClassNotFoundException {
		super(session);
		if(relatrixSetMethods == null)
			relatrixSetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixIterator", 0);
	}

	@Override
	public void process() throws Exception {
		if( this.methodName.equals("close") ) {
			RelatrixServer.sessionToObject.remove(getSession());
		} else {
			// Get the iterator linked to this session
			Object itInst = RelatrixServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = relatrixSetMethods.invokeMethod(this, itInst);
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
