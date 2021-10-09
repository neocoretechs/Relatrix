package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.relatrix.client.RelatrixClientInterface;

public class IndexResolver {
	static IndexInstanceTableInterface instanceTable = null;
	static boolean local = true;
	static RelatrixClientInterface remoteIndexInstanceTable;
	
	public static IndexInstanceTableInterface getIndexInstanceTable() throws IOException {
		if(instanceTable == null) {
			if(local)
				instanceTable = new IndexInstanceTable();
			else
				instanceTable = new RemoteIndexInstanceTable(remoteIndexInstanceTable);
		}
		return instanceTable;
	}

	public static void setLocal() {
		local = true;
	}
	
	public static void setRemote(RelatrixClientInterface remoteClient) {
		local = false;
		remoteIndexInstanceTable = remoteClient;
	}

}
