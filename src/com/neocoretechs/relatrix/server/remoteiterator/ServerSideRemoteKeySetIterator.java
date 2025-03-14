package com.neocoretechs.relatrix.server.remoteiterator;
import com.neocoretechs.relatrix.client.RemoteIterator;
import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * Used by the RelatrixServer to produce key sets for remote delivery.
 * Created from the {@link RelatrixStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class ServerSideRemoteKeySetIterator extends RemoteIterator {
	private static final long serialVersionUID = -7760511563553719651L;

	public ServerSideRemoteKeySetIterator(String session) {
		super(session);
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
			//System.out.println(itInst+" class:"+itInst.getClass());
			Object result = RelatrixServer.relatrixKeysetMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
