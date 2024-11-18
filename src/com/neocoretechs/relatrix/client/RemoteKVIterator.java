package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Iterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;
/**
 * This has to get called from the client to invoke the proper server side iterator
 * then get the result of the call from the server and return it to the client side call.
 * On the server we will be working with an actual iterator or {@link RelatrixIterator}.
 * One of the ServerInvokeMethods statically declared on the {@link RelatrixServer} or {@link  RelatrixKVServer} <p/>
 * On the server side this gets created then linked to a session object via the String constructor then returned
 * to the client where the RelatrixKVStatement contains the linked session ID.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class RemoteKVIterator extends RelatrixKVStatement implements RemoteObjectInterface, Serializable, Iterator {
	private static final long serialVersionUID = 4422613369716655753L;
	private transient RelatrixKVClient relatrixKVClient;
	/**
	 * Client side creation where we link the client transport
	 * @param relatrixClient
	 */
	public RemoteKVIterator(RelatrixKVClient relatrixClient) {
		this.relatrixKVClient = relatrixClient;
	}
	/**
	 * Invoked on server where we create this and pass it back to the client linking the server side session id
	 * @param session
	 */
	public RemoteKVIterator(String session) {
		super(session);
	}
	
	public void setClient(RelatrixKVClient client) {
		this.relatrixKVClient = client;
	}
	
	@Override
	public boolean hasNext() {
		try {
			return relatrixKVClient.hasNext(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object next() {
		try {
			return relatrixKVClient.next(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		//TODO:remove iterator, dont close client.
		//relatrixClient.close();	
	}

}
