package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Iterator;

import com.neocoretechs.rocksack.TransactionId;

/**
 * Fulfills the Iterator interface contract and acts as a proxy to the client.
 * This calls the {@link RelatrixClientTransaction} next or hasNext to invoke the proper server side iterator
 * then get the result of the call on the server and return it to the client side.<p/>
 * On the server we will be working with an actual iterator or {@link RelatrixIteratorTransaction}.
 * One of the ServerInvokeMethods statically declared on the {@link RelatrixTransactionServer} or {@link RelatrixKVTransactionServer} <p/>
 * On the server side this gets created then linked to a session object via the String constructor then returned
 * to the client where the {@link RelatrixTransactionStatement} contains the linked session ID.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class RemoteIteratorTransaction extends RelatrixTransactionStatement implements RemoteObjectInterface, Serializable, Iterator {
	private static final long serialVersionUID = 4422613369716655753L;
	private transient RelatrixClientTransaction relatrixClient;
	
	public RemoteIteratorTransaction(String session) {
		super(session);
	}
	/**
	 * Client side creation where we link the client transport
	 * @param relatrixClient
	 */
	public RemoteIteratorTransaction(RelatrixClientTransaction relatrixClient) {
		super();
		this.relatrixClient = relatrixClient;
	}
	
	public void setClient(ClientInterface client) {
		this.relatrixClient = (RelatrixClientTransaction) client;
	}
	
	/**
	 * Fulfills the Iterator interface contract and acts as a proxy to call the {@link RelatrixClientTransaction} hasNext method
	 * with transaction Id and 'this' instance.
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
	 * Fulfills the Iterator interface contract and acts as a proxy to call the {@link RelatrixClientTransaction} next method
	 * with transaction Id and 'this' instance.
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
