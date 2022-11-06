package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.relatrix.client.RelatrixClientInterface;
/**
 * The IndexResolver determines whether the database index instance table resides locally, and an
 * instance of {@link IndexInstanceTable} can be used to resolve database index to object instances, or whether an
 * instance of {@link RemoteIndexInstanceTable} using an implementation of {@link RelatrixClientInterface}
 * must be used to resolve remote instances. Implementations of RelatrixClientInterface may include
 * transaction context information.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public class IndexResolver {
	static IndexInstanceTableInterface instanceTable = null;
	static boolean local = true;
	static boolean transaction = false;
	static RelatrixClientInterface remoteIndexInstanceTable;
	
	public static IndexInstanceTableInterface getIndexInstanceTable() throws IOException {
		if(instanceTable == null) {
			if(local) {
				if(transaction) {
					instanceTable = new IndexInstanceTableTransaction();
				} else {
					instanceTable = new IndexInstanceTable();
				}
			} else {
				instanceTable = new RemoteIndexInstanceTable(remoteIndexInstanceTable);
			}
		}
		return instanceTable;
	}
	
	/**
	 * Determine if the instance of this class will be operating on a local or remote resolver table.
	 * By calling this, local is set to true, by default, it is also true.
	 */
	public static void setLocal() {
		local = true;
	}
	
	/**
	 * Determine if the instance of this class will be operating in a transaction context.
	 * By default transaction is set to false.
	 */
	public static void setTransaction(boolean xaction) {
		transaction = xaction;
	}
	
	/**
	 * Set the remote client to resolve the remote indexes. If transaction is true, instance of {@link RelatrixClientInterface}
	 * must be transactional.
	 * @param remoteClient Implementations of RelatrixClientInterface may include transaction context information.
	 */
	public static void setRemote(RelatrixClientInterface remoteClient) {
		local = false;
		remoteIndexInstanceTable = remoteClient;
	}

}
