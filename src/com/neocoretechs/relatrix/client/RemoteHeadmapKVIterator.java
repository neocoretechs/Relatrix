package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;

public class RemoteHeadmapKVIterator extends RelatrixStatement implements RemoteObjectInterface{
	private static final long serialVersionUID = -3324485838278832306L;
	public static final String className = "com.neocoretechs.relatrix.iterator.RelatrixHeadmapKVIterator";
	public RemoteHeadmapKVIterator(String session) {
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
			Object itInst = RelatrixKVServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested iterator instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixKVServer.relatrixHeadmapKVMethods.invokeMethod(this, itInst);
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
