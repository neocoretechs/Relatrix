package com.neocoretechs.relatrix.client;

import java.io.IOException;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Marker interface that allows a client to be assigned to a remote service
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public interface ClientNonTransactionInterface extends ClientInterface{

	void storekv(Comparable index, Object instance) throws IOException;

	void storekv(Alias alias, Comparable index, Object instance) throws IOException;

	Object getByIndex(DBKey index) throws IOException;
	
	Object getByIndex(Alias alias, DBKey index) throws IOException;

	Object get(Comparable instance) throws IOException;

	Object get(Alias alias, Comparable instance) throws IOException;

}
