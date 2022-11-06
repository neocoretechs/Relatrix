package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * Used to produce tailsets for remote delivery.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RemoteTailSetIterator extends RelatrixStatement implements RemoteObjectInterface{
	private static final long serialVersionUID = -7652502684740120087L;
	public static final String className = "com.neocoretechs.relatrix.iterator.RelatrixIterator";
	public RemoteTailSetIterator(String session) {
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
			// Get the iterator linked to this session
			Object itInst = RelatrixServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixServer.relatrixTailsetMethods.invokeMethod(this, itInst);
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
