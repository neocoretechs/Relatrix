package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.relatrix.client.RelatrixClientInterface;

public class IndexResolver {
	static IndexInstanceTableInterface instanceTable = null;
	
	public static IndexInstanceTableInterface getIndexInstanceTable() {
		if(instanceTable == null) {
			instanceTable = new IndexInstanceTable();
		}
		return instanceTable;
	}

	public static IndexInstanceTableInterface getIndexInstanceTable(RelatrixClientInterface remoteIndexInstanceTable) throws IOException {
		if(instanceTable == null) {
			instanceTable = new RemoteIndexInstanceTable(remoteIndexInstanceTable);
		}
		return instanceTable;
	}

}
