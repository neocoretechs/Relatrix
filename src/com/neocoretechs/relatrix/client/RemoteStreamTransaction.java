package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Used by the RelatrixServer and RelatrixKVServer to produce and consume streams for remote delivery and retrieval.<p/>
 * There is no persistent contract here and no need to implement RemoteObjectInterface for a 'close' operation nor
 * extend RelatrixStatement for a 'process' operation since the entire payload is built here for delivery in one operation.<p/>
 * Unlike an iterator, a stream is atomic and requires no further calls to the server. Indeed, it must be so to follow the stream paradigm.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RemoteStreamTransaction extends RemoteStream implements Serializable {
	private static final long serialVersionUID = 3064585530528835745L;
	private static boolean DEBUG = false;
	String xid;
	/**
	 * 
	 * @param result instance of stream to build collection that is serializable to return to client for
	 * construction of client side stream
	 */
	public RemoteStreamTransaction(String xid, Stream result) {
		super(result);
		this.xid = xid;
	}

	
	public String getTransactionId() { return xid; }
	
}
