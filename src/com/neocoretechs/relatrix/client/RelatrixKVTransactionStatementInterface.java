package com.neocoretechs.relatrix.client;

import com.neocoretechs.rocksack.TransactionId;

public interface RelatrixKVTransactionStatementInterface extends RemoteRequestInterface, RemoteResponseInterface {
	public TransactionId getTransactionId();
}
