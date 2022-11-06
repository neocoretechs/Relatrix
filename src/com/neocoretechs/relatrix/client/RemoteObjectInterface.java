package com.neocoretechs.relatrix.client;
/**
 * This interface represents an intermediary contract between a remote object such as
 * those iterators issued by 'findSet' and actions on the client. The only real difference
 * is the close operation which is necessary to dispose of the remote object on the server.
 * We get object with persistent remote references returned in a RelatrixStatement that calls a 
 * findSet permutation that return an iterator and perhaps other incarnations later.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015
 */
public interface RemoteObjectInterface {
	/**
	 * Signal a call to the server to remove this object from memory
	 */
	public void close();


}
