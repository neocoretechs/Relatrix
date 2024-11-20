package com.neocoretechs.relatrix.client;
/**
 * This interface represents an intermediary contract between a remote object such as
 * those iterators issued by 'findSet' and actions on the client. The only real difference
 * is the close operation which is necessary to dispose of the remote object on the server,
 * and the setClient method which is necessary to set up an instance of a {@link RemoteIterator} as
 * it is passed down to the client.<p/>
 * We get object with persistent remote references returned in a {@link RelatrixStatement} that calls a 
 * findSet permutation that returns an iterator and perhaps other incarnations later.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015
 */
public interface RemoteObjectInterface {
	/**
	 * set the client side transport instance
	 * @param client
	 */
	public void setClient(ClientInterface client);
	/**
	 * Signal a call to the server to remove this object from memory
	 */
	public void close();


}
