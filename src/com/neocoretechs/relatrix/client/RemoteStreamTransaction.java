package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.stream.StreamHelper;

/**
 * Used by the RelatrixServer and RelatrixKVServer to produce and consume streams for remote delivery and retrieval.<p/>
 * There is no persistent contract here and no need to implement RemoteObjectInterface for a 'close' operation nor
 * extend RelatrixStatement for a 'process' operation since the entire payload is built here for delivery in one operation.<p/>
 * Unlike an iterator, a stream is atomic and requires no further calls to the server. Indeed, it must be so to follow the stream paradigm.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RemoteStreamTransaction<T> extends RemoteStream<T> {
	private static boolean DEBUG = false;
	String xid;

	public RemoteStreamTransaction(RemoteIteratorTransaction it) {
		stream = new StreamHelper<T>(it);
	}
	
	public String getTransactionId() { return xid; }
	
}
