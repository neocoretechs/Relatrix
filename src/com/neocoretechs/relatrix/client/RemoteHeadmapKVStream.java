package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.server.RelatrixKVServer;
/**
 * Used by the Key/Value RelatrixKVServer to produce headmaps for remote delivery
 * @author Groff
 *
 */
public class RemoteHeadmapKVStream extends RelatrixKVStatement implements RemoteObjectInterface{
	private static final long serialVersionUID = -3324485838278832306L;
	public static final String className = "com.neocoretechs.bigsack.stream.HeadSetKVStream";
	public RemoteHeadmapKVStream(String session) {
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
			Object itInst = RelatrixKVServer.sessionToObject.get(getSession());
			if( itInst == null )
				throw new Exception("Requested stream instance does not exist for session "+getSession());
			// invoke the desired method on this concrete server side iterator, let boxing take result
			Object result = RelatrixKVServer.relatrixHeadmapKVStreamMethods.invokeMethod(this, itInst);
			setObjectReturn(result);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

	@Override
	public void close() {
		RelatrixKVServer.sessionToObject.remove(getSession());
	}
}