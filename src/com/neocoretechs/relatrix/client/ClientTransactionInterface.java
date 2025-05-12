package com.neocoretechs.relatrix.client;

import java.io.IOException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Marker interface that allows a client to be assigned to a remote service
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public interface ClientTransactionInterface extends ClientInterface{
	
	public TransactionId getTransactionId() throws IOException;

	public void storekv(TransactionId transactionId, Comparable index, Object instance) throws IOException;

	public void storekv(Alias alias, TransactionId transactionId, Comparable instance, Object index) throws IOException;

	public Object get(TransactionId transactionId, Comparable instance) throws IOException;
	
	public Object get(Alias alias, TransactionId transactionId, Comparable instance) throws IOException;
	
	public Object getByIndex(Alias alias, TransactionId transactionId, Comparable index) throws IOException;
	
	public Object getByIndex(TransactionId transactionId, Comparable index) throws IOException;

}
