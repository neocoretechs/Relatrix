package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Marker interface that allows a client to be assigned to a remote service
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public interface AsynchClientTransactionInterface extends ClientTransactionInterface {
	public TransactionId getTransactionId();

	public void storekv(TransactionId transactionId, Comparable index, Object instance);

	public void storekv(Alias alias, TransactionId transactionId, Comparable instance, Object index);

	public CompletableFuture<Object> get(TransactionId transactionId, Comparable instance);
	
	public CompletableFuture<Object> get(Alias alias, TransactionId transactionId, Comparable instance);
	
	public CompletableFuture<Object> getByIndex(Alias alias, TransactionId transactionId, Comparable index);
	
	public CompletableFuture<Object> getByIndex(TransactionId transactionId, Comparable index);

}
