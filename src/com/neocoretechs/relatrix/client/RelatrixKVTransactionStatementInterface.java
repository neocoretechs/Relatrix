package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.rocksack.TransactionId;
/**
 * The interface allows the transport of method calls to the server {@link RelatrixKVTransactionServer} using a transaction context.
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) NeoCoreTechs 2022
 *
 */
public interface RelatrixKVTransactionStatementInterface extends RemoteRequestInterface, RemoteResponseInterface {
	public TransactionId getTransactionId();
}
