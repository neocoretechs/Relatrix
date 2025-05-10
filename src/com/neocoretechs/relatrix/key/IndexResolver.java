package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixKVClientInterface;

/**
 * The IndexResolver determines whether the database index instance table resides locally, and an
 * instance of {@link IndexInstanceTable} can be used to resolve database index to object instances, or whether an
 * instance of {@link RemoteIndexInstanceTable} using an implementation of {@link RelatrixKVClientInterface}
 * must be used to resolve remote instances. Implementations of RelatrixClientInterface will not include
 * transaction context information as it functions in a global context and is manipulated outside of transactions.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public class IndexResolver {
	public static boolean DEBUG = false;
	static IndexInstanceTableInterface instanceTable = null;
	/**
	 * REturn the instance table; the table that translates indexes to object instances
	 * @return
	 * @throws IOException
	 */
	public static IndexInstanceTableInterface getIndexInstanceTable() throws IOException {
		if(DEBUG)
			System.out.println("Returning instance table:"+instanceTable);
		return instanceTable;
	}
	
	/**
	 * Determine if the instance of this class will be operating on a local or remote resolver table.
	 * By calling this, local is set to true, by default, it is also true. A new IndexInstanceTable is constructed with default ctor.
	 */
	public static void setLocal() {
		instanceTable = new IndexInstanceTable();
		if(DEBUG)
			System.out.println("IndexResolver setLocal instance table:"+instanceTable);
	}

	/**
	 * Set the remote client to resolve the remote indexes.
	 * @param remoteClient Implementations of ClientInterface may include transaction context information.
	 * @throws IOException if low level problem 
	 */
	public static void setRemote(ClientInterface remoteClient) throws IOException {
		instanceTable = new RemoteIndexInstanceTable(remoteClient);
		if(DEBUG)
			System.out.println("IndexResolver setRemote instance table:"+instanceTable);
	}
	
	/**
	 * Set the remote transaction client to resolve the remote indexes.
	 * @param remoteClient Implementations of ClientInterface may include transaction context information.
	 * @throws IOException if low level problem
	 */
	public static void setRemoteTransaction(ClientInterface remoteClient) throws IOException {
		if(!(remoteClient instanceof ClientTransactionInterface))
			throw new IOException("Remote Client not instance of ClientTransactionInterface:"+remoteClient.getClass());
		instanceTable = new RemoteIndexInstanceTable(remoteClient);
		if(DEBUG)
			System.out.println("IndexResolver setRemote instance table:"+instanceTable);	
	}

}
