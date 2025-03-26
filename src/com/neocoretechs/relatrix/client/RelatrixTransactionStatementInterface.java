package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.TransactionId;

/**
 * Defines the contract for remote calls to the Relatrix server. Ensured a transactionId is available
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RelatrixTransactionStatementInterface extends RemoteRequestInterface, RemoteResponseInterface {
	public TransactionId getTransactionId();
}
