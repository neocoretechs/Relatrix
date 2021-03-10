package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * Used by the category theoretic Relatrix server to produce headstreams for remote delivery
 * @author Jonathan Groff (C) NeoCoreTechs 2020,2021
 *
 */
public class RemoteHeadsetStream extends RelatrixStatement implements RemoteObjectInterface {
	private static final long serialVersionUID = 6619765118651744159L;
	public static final String className = "com.neocoretechs.relatrix.stream.RelatrixHeadsetStream";
	public RemoteHeadsetStream(String session) {
		super();
		paramArray = new Object[0];
		setSession(session);
	}
	/* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getClassName()
	 */
	@Override
	public String getClassName() { return className; }
	
	@Override
	public void process() throws Exception {
		if( this.methodName.equals("close") ) {
			close();
		} else {
			// Get the stream linked to this session
			Object itInst = RelatrixServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested stream instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixServer.relatrixHeadstreamMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

	@Override
	public void close() {
		RelatrixServer.sessionToObject.remove(getSession());
	}
}
