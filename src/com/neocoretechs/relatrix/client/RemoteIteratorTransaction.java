package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Iterator;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.rocksack.TransactionId;
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
public class RemoteIteratorTransaction extends RelatrixTransactionStatement implements RemoteObjectInterface, Serializable, Iterator {
	private static final long serialVersionUID = 4422613369716655753L;
	private transient RelatrixClientTransaction relatrixClient;
	/**
	 * Client side creation where we link the client transport
	 * @param relatrixClient
	 */
	public RemoteIteratorTransaction(RelatrixClientTransaction relatrixClient) {
		super();
		this.relatrixClient = relatrixClient;
	}
	/**
	 * Invoked on server where we create this and pass it back to the client linking the server side session id
	 * @param session
	 */
	public RemoteIteratorTransaction(TransactionId xid, String session) {
		super(xid, session);
	}
	
	public void setClient(ClientInterface client) {
		this.relatrixClient = (RelatrixClientTransaction) client;
	}
	
	@Override
	public boolean hasNext() {
		try {
			return relatrixClient.hasNext(xid,this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object next() {
		try {
			return relatrixClient.next(xid,this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
