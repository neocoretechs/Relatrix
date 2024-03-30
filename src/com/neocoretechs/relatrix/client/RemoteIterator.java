package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Iterator;

import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * This has to get called from the client to invoke the proper server side iterator
 * then get the result of the call from the server and return it to the client side call.
 * On the server we will be working with an actual iterator or {@link RelatrixIterator}.
 * One of the ServerInvokeMethods statically declared on the {@link RelatrixServer} or {@link  RelatrixKVServer} <p/>
 * On the server side this gets created then linked to a session object via the String constructor then returned
 * to the client where the RelatrixStatement contains the linked session ID.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class RemoteIterator extends RelatrixStatement implements RemoteObjectInterface, Serializable, Iterator {
	private static final long serialVersionUID = 4422613369716655753L;
	private transient RelatrixClient relatrixClient;
	/**
	 * Client side creation where we link the client transport
	 * @param relatrixClient
	 */
	public RemoteIterator(RelatrixClient relatrixClient) {
		this.relatrixClient = relatrixClient;
	}
	/**
	 * Invoked on server where we create this and pass it back to the client linking the server side session id
	 * @param session
	 */
	public RemoteIterator(String session) {
		super(session);
	}
	
	public void setClient(RelatrixClient client) {
		this.relatrixClient = client;
	}
	
	@Override
	public boolean hasNext() {
		return relatrixClient.hasNext(this);
	}

	@Override
	public Object next() {
		return relatrixClient.next(this);
	}

	@Override
	public void close() {
		relatrixClient.close();	
	}

}
