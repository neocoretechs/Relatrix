package com.neocoretechs.relatrix.server.remoteiterator;

import com.neocoretechs.relatrix.client.RemoteKVIterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
/**
 * Used by the Key/Value RelatrixKVServer to produce headmaps for remote delivery.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class RemoteHeadMapIterator extends RemoteKVIterator {
	private static final long serialVersionUID = -6767314283313398274L;
	public RemoteHeadMapIterator(String session) {
		super(session);
		paramArray = new Object[0];
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
			Object result = RelatrixKVServer.relatrixHeadmapMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

}
