package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.client.RelatrixClientInterface;
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
	static boolean local = true;
	static RelatrixClientInterface remoteIndexInstanceTable = null;
	
	public static IndexInstanceTableInterface getIndexInstanceTable() throws IOException {
		if(instanceTable == null) {
			if(local) {
					try {
						instanceTable = new IndexInstanceTable();
					} catch (NoSuchElementException e) {
						throw new IOException(e);
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
	 * Set the remote client to resolve the remote indexes. If transaction is true, instance of {@link RelatrixClientInterface}
	 * must be transactional.
	 * @param remoteClient Implementations of RelatrixClientInterface may include transaction context information.
	 */
	public static void setRemote(RelatrixClientInterface remoteClient) {
		local = false;
		remoteIndexInstanceTable = remoteClient;
	}
	

}
