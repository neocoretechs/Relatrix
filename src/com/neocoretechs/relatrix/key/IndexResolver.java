package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
/**
 * The IndexResolver determines whether the database index instance table resides locally, and an
 * instance of {@link IndexInstanceTable} can be used to resolve database index to object instances, or whether an
 * instance of {@link RemoteIndexInstanceTable} using an implementation of {@link RelatrixClientInterface}
 * must be used to resolve remote instances. Implementations of RelatrixClientInterface will not include
 * transaction context information as it functions in a global context and is manipulated outside of transactions.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2021,2022
 *
 */
public class IndexResolver {
	public static boolean DEBUG = false;
	static IndexInstanceTableInterface instanceTable = null;
	static boolean local = true;
	static RelatrixClientInterface remoteIndexInstanceTable;
	static ConcurrentHashMap<String,IndexInstanceTableInterface> indexInstanceTableTransaction = new ConcurrentHashMap<String,IndexInstanceTableInterface>();
	static String currentTransactionId = null;
	
	public static IndexInstanceTableInterface getIndexInstanceTable() throws IOException {
		if(instanceTable == null) {
			if(local) {
				instanceTable = new IndexInstanceTable();
			} else {
				instanceTable = new RemoteIndexInstanceTable(remoteIndexInstanceTable);
			}
		}
		return instanceTable;
	}
	
	public static synchronized IndexInstanceTableInterface getIndexInstanceTable(String xid) throws IOException {
		if(DEBUG)
			System.out.println("IndexResolver.getIndexInstanceTable for XId:"+xid+" from table sized:"+indexInstanceTableTransaction.size());
		if(xid == null) {
			new Exception().printStackTrace();
			throw new IOException("Transaction Id null");
		}
		IndexInstanceTableInterface iTable = indexInstanceTableTransaction.get(xid);
		if(local) {
				if(iTable == null) {
					iTable = new IndexInstanceTable(xid);
					indexInstanceTableTransaction.put(xid,iTable);
				}
		} else {
			if(iTable == null)
				throw new IOException("Must call 'setRemote' in 'IndexResolver' with remote client for transaction:"+xid);
		}
		return iTable;
	}
	
	public static synchronized IndexInstanceTableInterface getCurrentIndexInstanceTable() throws IOException {
		return getIndexInstanceTable(currentTransactionId);
	}
	/**
	 * If we are operating in a local transaction context, such as a server, ensure we have an index resolution table for DBKeys
	 * for this transaction id. If non-local, do nothing as setRemote should be handling things.
	 * This should be called before processing a RelatrixTransactionStatement or variant.
	 * @param xid
	 * @throws IOException
	 */
	public static synchronized void setIndexInstanceTable(String xid) throws IOException {
		if(local) {
			if(xid == null)
				throw new IOException("Transaction Id null");
			IndexInstanceTableInterface iTable = indexInstanceTableTransaction.get(xid);
			if(iTable == null) {
				iTable = new IndexInstanceTable(xid);
				indexInstanceTableTransaction.put(xid,iTable);
			}
		}
	}
	
	public static synchronized void remove(String xid) {
		indexInstanceTableTransaction.remove(xid);
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

	public static synchronized void setRemote(String xid, RelatrixClientTransactionInterface remoteClient) throws IOException {
		local = false;
		if(!indexInstanceTableTransaction.containsKey(xid))
			indexInstanceTableTransaction.put(xid, new RemoteIndexInstanceTable(xid, remoteClient));
	}
	
	public static synchronized void setCurrentTransactionId(String xid) {
		currentTransactionId = xid;
	}
	public static synchronized String getCurrentTransactionId() {
		return currentTransactionId;
	}

}
