package com.neocoretechs.relatrix.client.iterator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;

import com.neocoretechs.relatrix.client.ClientInterface;

/**
 * Contract for remote iterators via client. Remote iterators are server processors with Iterator methods.
 * This class conforms to the same pattern as client bindings to main Relatrix servers.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public interface RemoteIteratorInterface extends ClientInterface, Iterator, Serializable {
	@Override
	public Object next();
	@Override
	public boolean hasNext();
	public void close();
	public void shutdown();
	@Override
	public String getRemoteNode();
	@Override
	public int getRemotePort();
	@Override
	public UUID getSession();
}
