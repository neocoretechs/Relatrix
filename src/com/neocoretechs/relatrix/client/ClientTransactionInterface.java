package com.neocoretechs.relatrix.client;

import java.io.IOException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Marker interface that allows a client to be assigned to a remote service
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public interface ClientTransactionInterface extends ClientInterface {
	
	public TransactionId getTransactionId() throws IOException;
	
	public void commit(TransactionId transactionId) throws IOException;
	
	public void rollback(TransactionId transactionId) throws IOException;
	
	public void commit(Alias alias, TransactionId transactionId) throws IOException;
	
	public void rollback(Alias alias, TransactionId transactionId) throws IOException;
	

}
