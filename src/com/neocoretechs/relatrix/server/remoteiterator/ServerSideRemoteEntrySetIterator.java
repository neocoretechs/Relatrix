package com.neocoretechs.relatrix.server.remoteiterator;
import java.io.IOException;

import java.net.InetAddress;

import com.neocoretechs.relatrix.client.RemoteIterator;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * Used by the RelatrixServer to produce entry sets for remote delivery.
 * Created from the {@link RelatrixStatement} process method and setObjectReturn is then called to place it in the return.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public class ServerSideRemoteEntrySetIterator extends RemoteIterator {
	private static boolean DEBUG;
	private static final long serialVersionUID = 1L;
	public static ServerInvokeMethod relatrixEntrysetMethods = null; // Entryset iterator methods
	
	public ServerSideRemoteEntrySetIterator(String session, InetAddress host) throws IOException, ClassNotFoundException {
		super(session);
		if(relatrixEntrysetMethods == null)
			relatrixEntrysetMethods = new ServerInvokeMethod("com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator", 0);
	}
	
	public ServerSideRemoteEntrySetIterator(String session) {
		super(session);
	}

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
			Object result = relatrixEntrysetMethods.invokeMethod(this, itInst);
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
