package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Iterator;

import com.neocoretechs.relatrix.iterator.RelatrixIterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * Fulfills the Iterator interface contract and acts as a proxy to the client.
 * This calls the {@link RelatrixClient} next or hasNext to invoke the proper server side iterator
 * then get the result of the call on the server and return it to the client side.<p/>
 * On the server we will be working with an actual iterator or {@link RelatrixIterator}.
 * One of the ServerInvokeMethods statically declared on the {@link RelatrixServer} or {@link  RelatrixKVServer} <p/>
 * On the server side this gets created then linked to a session object via the String constructor then returned
 * to the client where the {@link RelatrixStatement} contains the linked session ID.
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
		super();
		this.relatrixClient = relatrixClient;
	}
	/**
	 * Invoked on server where we create this and pass it back to the client linking the server side session id
	 * @param session
	 */
	public RemoteIterator(String session) {
		super(session);
	}
	
	public void setClient(ClientInterface client) {
		this.relatrixClient = (RelatrixClient) client;
	}
	/**
	 * Fulfills the Iterator interface contract and acts as a proxy to call the {@link RelatrixClient} hasNext method
	 * with 'this' instance.
	 * @return the boolean result of server side call
	 */	
	@Override
	public boolean hasNext() {
		try {
			return relatrixClient.hasNext(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Fulfills the Iterator interface contract and acts as a proxy to call the {@link RelatrixClient} next method
	 * with 'this' instance.
	 * @return the next iterated object or null
	 */
	@Override
	public Object next() {
		try {
			return relatrixClient.next(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



}
