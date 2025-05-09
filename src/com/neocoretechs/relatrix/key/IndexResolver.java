package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixKVClientInterface;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClientTransactionInterface;

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
	
	public static IndexInstanceTableInterface getIndexInstanceTable() throws IOException {
		if(DEBUG)
			System.out.println("Returning instance table:"+instanceTable);
		return instanceTable;
	}
	
	/**
	 * Determine if the instance of this class will be operating on a local or remote resolver table.
	 * By calling this, local is set to true, by default, it is also true.
	 */
	public static void setLocal() {
		instanceTable = new IndexInstanceTable();
		if(DEBUG)
			System.out.println("IndexResolver setLocal instance table:"+instanceTable);
	}

	/**
	 * Set the remote client to resolve the remote indexes. If transaction is true, instance of {@link RelatrixClientInterface}
	 * must be transactional.
	 * @param remoteClient Implementations of RelatrixClientInterface may include transaction context information.
	 * @throws IOException 
	 */
	public static void setRemote(ClientInterface remoteClient) throws IOException {
		instanceTable = new RemoteIndexInstanceTable(remoteClient);
		if(DEBUG)
			System.out.println("IndexResolver setRemote instance table:"+instanceTable);
	}

	public static void setRemoteTransaction(ClientInterface clientInterface) {
		// TODO Auto-generated method stub
		
	}


}
