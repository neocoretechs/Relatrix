package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;

import com.neocoretechs.relatrix.server.RelatrixKVServer;
/**
 * Used by the RelatrixKVServer to produce and consume streams for remote delivery and retrieval.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RemoteKVStream extends RelatrixKVStatement implements RemoteObjectInterface{
	private static boolean DEBUG = true;
	private static final long serialVersionUID = 1206621317830948409L;
	// This classname is just a generic placeholder, notice in 'process' we dont actually call anything using it
	// it is just here to conform to the interface contract and indicate that SackStream is
	// the superclass of all Relatrix streams.
	public static final String className = "com.neocoretechs.bigsack.stream.SackStream";
	public RemoteKVStream(String session) {
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
			Object[] retArray = ((Stream)itInst).toArray();
			if(DEBUG)
				System.out.printf("Setting return object:%s length:%d%n", (retArray != null ? retArray : "NULL"), (retArray != null ? retArray.length : 0));
			setObjectReturn(retArray);
		}
		// notify latch waiters
		getCountDownLatch().countDown();
	}

	public Stream<?> of() {
		return Stream.of((Comparable[])getObjectReturn());
	}
	
	@Override
	public void close() {
		RelatrixKVServer.sessionToObject.remove(getSession());
	}
}
