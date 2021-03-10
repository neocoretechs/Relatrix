package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * Used for Relatrix category theoretic and set oriented processing to produce tailStream and tailStream functors
 * @author Jonathan Groff (C) NeoCoreTechs 2020,2021
 *
 */
public class RemoteTailsetStream extends RelatrixStatement implements RemoteObjectInterface {
	private static final long serialVersionUID = -322257696363301665L;
	public static final String className = "com.neocoretechs.relatrix.stream.RelatrixStream";
	public RemoteTailsetStream(String session) {
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
			Object result = RelatrixServer.relatrixTailstreamMethods.invokeMethod(this, itInst);
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
