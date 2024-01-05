package com.neocoretechs.relatrix.key;

import java.io.IOException;

import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;

/**
 * The IndexInstanceTable is actually a combination of 2 K/V tables that allow retrieval of
 * indexed instances via an integer index, for the instance, and the instance, for the reverse
 * lookup of the Integer index. We use the DBKey wrapper class to carry the integer index inside the Morphism.
 * which also adds validation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2023
 *
 */
public final class RemoteIndexInstanceTableAlias implements IndexInstanceTableInterface {
	public static boolean DEBUG = false;
	String alias = null;
	private RelatrixClientInterface rc = null;
	private RelatrixClientTransactionInterface rcx = null;
	private String transactionId;
	
	public RemoteIndexInstanceTableAlias(String alias, RelatrixClientInterface rc) throws IOException {
		this.alias = alias;
		this.rc = rc;
	}	
	
	public RemoteIndexInstanceTableAlias(String alias, String xid, RelatrixClientTransactionInterface rc) throws IOException {
		this.alias = alias;
		this.transactionId = xid;
		this.rcx = rc;
	}	
	/**
	 * Put the key to the proper tables
	 * @param index The DBKey index
	 * @param instance the Comparable instance payload
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void put(DBKey index, Comparable instance) throws IllegalAccessException, IOException, ClassNotFoundException {
			if(DEBUG)
				System.out.printf("%s.put index=%s instance=%s%n", index.getClass().getName(), index, instance);
			// instance index not valid, key not fully formed, we may have to add instance value to table and index it
			if(instance == null) {
				// instance is null, no instance in DBkey, keys are not valid, nothing to put.
				throw new IllegalAccessException("DBKey is in an invalid state: no valid instance, no valid index.");
			}
			try {
				if(rc != null) {
					rc.store(alias, index, instance);
				} else {
					if(rcx != null) {
						rcx.storekv(alias, transactionId, index, instance);
					} else {
						throw new IOException("RelatrixClient is null");
					}
				}
			} catch(DuplicateKeyException dke) {
				throw new IOException(String.format("DBKey to Instance table duplicate key:%s encountered for instance:%s. Existing entry=%s/%s%n",index,instance,((KeyValue)RelatrixKV.get(index)).getmKey(),((KeyValue)RelatrixKV.get(index)).getmValue()));
			}

	}
	
	@Override
	public void delete(DBKey index) throws IllegalAccessException, IOException, DuplicateKeyException, ClassNotFoundException {
			Comparable instance = null;
			instance = (Comparable) getByIndex(index);
			if(instance != null) {
				if(rc != null) {
					rc.remove(alias, instance);
				} else {
					if(rcx != null) {
						rcx.remove(alias, transactionId, instance);
					} else {
						throw new IOException("RelatrixClient is null");
					}
				}
			}
			if(rc != null) {
				rc.remove(alias, index);
			} else {
				if(rcx != null) {
					rcx.remove(alias, transactionId, index);
				} else {
					throw new IOException("RelatrixClient is null");
				}
			}
	}
	
	@Override
	public void commit() throws IOException {
		if(rcx != null) {
			rcx.commit(alias, transactionId);
		} else {
			throw new IOException("RelatrixClient is null");
		}
	}
	
	@Override
	public void rollback() throws IOException {
		if(rcx != null) {
			rcx.rollback(alias, transactionId);
		} else {
			throw new IOException("RelatrixClient is null");
		}
	}
	
	@Override
	public void checkpoint() throws IllegalAccessException, IOException {
		if(rcx != null) {
			rcx.checkpoint(alias, transactionId);
		} else {
			throw new IOException("RelatrixClient is null");
		}
	}
	
	@Override
	public void rollbackToCheckpoint() throws IOException, IllegalAccessException {
		if(rcx != null) {
			rcx.rollbackToCheckpoint(alias, transactionId);
		} else {
			throw new IOException("RelatrixClient is null");
		}	
	}

	/**
	 * Get the instance by using the InstanceIndex contained in the passed DBKey
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object getByIndex(DBKey index) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(rc != null) {
			return rc.getByIndex(alias, index);
		} else {	
			if(rcx != null) {
				return rcx.getByIndex(alias, transactionId, index);
			} else { 
				throw new IOException("RelatrixClient is null");
			}
		}
	}
	/**
	 * Get the Integer index of the instance by retrieving the InstanceIndex using the instance present in the passed object
	 * @param instance the DBKey containing the instance
	 * @return The Integer index contained in the retrieved InstanceIndex
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public DBKey getByInstance(Object instance) throws IllegalAccessException, IOException, ClassNotFoundException {
		if(rc != null) {
			return (DBKey)rc.get(alias, (Comparable) instance);
		} else {
			if(rcx != null) {
				return (DBKey)rcx.get(alias, transactionId, (Comparable) instance);
			} else { 
				throw new IOException("RelatrixClient is null");
			}
		}
	}

	@Override
	public void setTransactionId(String xid) {
		this.transactionId = xid;
	}

	@Override
	public DBKey getNewDBKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		if(rc != null) {
			return new DBKey(rc.getNewKey());
		} else {
			if(rcx != null) {
				return new DBKey(rcx.getNewKey(transactionId));
			} else { 
				throw new IOException("RelatrixClient is null");
			}
		}
	}

}
