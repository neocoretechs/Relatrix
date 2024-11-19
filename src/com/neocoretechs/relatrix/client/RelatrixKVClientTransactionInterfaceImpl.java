package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

public abstract class RelatrixKVClientTransactionInterfaceImpl implements RelatrixKVClientTransactionInterface {

	public abstract Object sendCommand(RelatrixKVTransactionStatement s) throws Exception;
	
	@Override
	public Object lastValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastValue", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastValue", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("nearest", transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("nearest", alias, transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKV", transactionId, from, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKV", alias, transactionId, from, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKV", alias, transactionId, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKV", transactionId, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMap", alias, transactionId, from, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMap", transactionId, from, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapStream", alias, transactionId, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapStream", transactionId, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKV", transactionId, from);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator  findTailMapKV(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKV", alias, transactionId, from);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromJar(String jar) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("loadClassFromJar", jar);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMap", alias, transactionId, from);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMap", transactionId, from);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromPath(String clazz,String path) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("loadClassFromPath", clazz, path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMap", alias, transactionId, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMap", transactionId, to);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySetStream", transactionId, clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySetStream", alias, transactionId, clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKVStream", alias, transactionId, from, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKVStream", transactionId, from, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapStream", alias, transactionId, from);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapStream", transactionId, from);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapStream", alias, transactionId, from, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapStream", transactionId, from, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias alias) throws java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("removeAlias", alias);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public String getAlias(Alias alias) {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getAlias", alias);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getTableSpace", new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void endTransaction(TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("endTransaction", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getAliases", new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getTransactionId", new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias alias, String path) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("setAlias", alias, path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTablespace(String path) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("setTablespace", path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollback", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollback", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("checkpoint", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("checkpoint", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKVStream", alias, transactionId, from);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(TransactionId transactionId, Comparable from) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKVStream", transactionId, from);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackAllTransactions() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackAllTransactions", new Object[]{});
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Object[] getTransactionState() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getTransactionState", new Object[]{});
		try {
			return (Object[])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKVStream", alias, transactionId, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(TransactionId transactionId, Comparable to) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKVStream", transactionId, to);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackToCheckpoint", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackToCheckpoint", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void removePackageFromRepository(String pack) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("removePackageFromRepository", pack);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackTransaction(TransactionId transactionId) {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackTransaction", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream entrySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySetStream", transactionId, clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySetStream", alias, transactionId, clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastKey", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastKey", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("commit", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId transactionId) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("commit", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstKey", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstKey", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstValue", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstValue", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(TransactionId transactionId, Class clazz, Object key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("containsValue", transactionId, clazz, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias alias, TransactionId transactionId, Class clazz, Object key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("containsValue", alias, transactionId, clazz, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySet", alias, transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySet", transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("close", alias, transactionId, clazz);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("close", transactionId, clazz);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId xid,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySet", xid, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySet", alias, transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", alias, transactionId, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", transactionId, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", transactionId, clazz, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias alias, TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", alias, transactionId, clazz, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("size", alias, transactionId, clazz);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("size", transactionId, clazz);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias alias, TransactionId transactionId, Comparable key, Object value) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("store", alias, transactionId, key, value);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(TransactionId transactionId, Comparable key, Object value) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("store", transactionId, key, value);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", transactionId, clazz, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias alias, TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", alias, transactionId, clazz, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", alias, transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("remove", transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("remove", alias, transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
}

