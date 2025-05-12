package com.neocoretechs.relatrix.client;

import java.io.IOException;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;

/**
 * Marker interface that allows a client to be assigned to a remote service
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public interface ClientNonTransactionInterface extends ClientInterface{

	public void storekv(Comparable index, Object instance) throws IOException;

	public void storekv(Alias alias, Comparable index, Object instance) throws IOException;

	public Object getByIndex(DBKey index) throws IOException;
	
	public Object getByIndex(Alias alias, DBKey index) throws IOException;

	public Object get(Comparable instance) throws IOException;

	public Object get(Alias alias, Comparable instance) throws IOException;

}
