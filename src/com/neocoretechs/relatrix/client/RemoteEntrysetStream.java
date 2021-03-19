package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;

import com.neocoretechs.relatrix.server.RelatrixKVServer;
/**
 * Used by the Key/Value RelatrixKVServer to produce entry sets for remote delivery
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RemoteEntrySetStream extends RelatrixKVStatement implements RemoteObjectInterface{
	private static final long serialVersionUID = 1206621317830948409L;
	public static final String className = "com.neocoretechs.bigsack.stream.EntrySetStream";
	public RemoteEntrySetStream(String session) {
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
			// invoke the desired method on this concrete server side stream, convert to array
			//System.out.println(itInst+" class:"+itInst.getClass());
			//Object result = RelatrixKVServer.relatrixEntrysetStreamMethods.invokeMethod(this, itInst);
			setObjectReturn(((Stream)itInst).toArray());
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

	@Override
	public void close() {
		RelatrixKVServer.sessionToObject.remove(getSession());
	}
}
