package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import java.util.stream.Stream;
import java.util.Iterator;
import com.neocoretechs.relatrix.DomainMapRange;


public abstract class RelatrixClientTransactionInterfaceImpl implements RelatrixClientTransactionInterface {

	public abstract Object sendCommand(RelatrixTransactionStatement s) throws Exception;
	@Override
	public Stream entrySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySetStream", alias, transactionId, clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySetStream", transactionId, clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public DatabaseCatalog getByPath(String path, boolean create) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByPath", null, path, create);
		try {
			return (DatabaseCatalog)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void loadClassFromPath(String path) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("loadClassFromPath", null, path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAliasToPath(Alias alias) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAliasToPath", alias, null);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getDatabasePath(DatabaseCatalog arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getDatabasePath", null, arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void loadClassFromJar(String jar) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("loadClassFromJar", null, jar);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DatabaseCatalog getByAlias(Alias alias) throws java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByAlias", alias, null);
		try {
			return (DatabaseCatalog)sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findStream(TransactionId transactionId, Object arg2, Object arg3, Object arg4) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", transactionId, arg2, arg3, arg4);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", alias, transactionId, darg, marg, rarg);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("checkpoint", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("checkpoint", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", alias, transactionId, darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId transactionId, Object arg2, Object arg3, Object arg4, Object... arg5) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", transactionId, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId transactionId, Object arg2, Object arg3, Object arg4, Object... arg5) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", transactionId, arg2, arg3, arg4, arg5);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", alias, transactionId, darg, marg, rarg, endarg);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", alias, transactionId, darg, marg, rarg, endarg);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId transactionId, Object arg2, Object arg3, Object arg4, Object... arg5) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", transactionId, arg2, arg3, arg4, arg5);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId transactionId, Object arg2, Object arg3, Object arg4, Object... arg5) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", transactionId, arg2, arg3, arg4, arg5);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", alias, transactionId, darg, marg, rarg, endarg);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", alias, transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId transactionId, Object arg2, Object arg3, Object arg4, Object... arg5) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", transactionId, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", alias, transactionId, darg, rarg, marg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", alias, transactionId, darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId transactionId, Object arg2, Object arg3, Object arg4, Object... arg5) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", transactionId, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removekv", transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removekv", alias, transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId transactionId, Object arg2, Object arg3, Object arg4) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", transactionId, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", alias, transactionId, darg, marg, rarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAliases", null, new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void removeAlias(Alias alias) throws java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removeAlias", alias, null);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void setAlias(Alias alias, String path) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setAlias", alias, null, path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTableSpace", null, new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void setWildcard(char arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setWildcard", null, arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void setTuple(char arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setTuple", null, arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public String getAlias(Alias alias) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAlias", alias, null);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void setTablespace(String path) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setTablespace", null, path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollback", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollback", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void endTransaction(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("endTransaction", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTransactionId", null, new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollbackToCheckpoint", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollbackToCheckpoint", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias alias, TransactionId transactionId, Comparable index) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByIndex", alias, transactionId, index);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(TransactionId transactionId, Comparable index) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByIndex", transactionId, index);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("commit", transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("commit", alias, transactionId);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", alias, transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId transactionId, Class clazz) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("keySet", transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("keySet", alias, transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySet", transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySet", alias, transactionId, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", alias, transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", alias, transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", transactionId, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", alias, transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", transactionId);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("contains", alias, transactionId, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("contains", transactionId, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias alias, TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", alias, transactionId);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId transactionId) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", transactionId);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", alias, transactionId, clazz);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId transactionId, Class clazz) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", transactionId, clazz);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(TransactionId transactionId, Comparable key, Object value) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", transactionId, key, value);
		try {
			return (DomainMapRange) sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}

	@Override
	public DomainMapRange store(Alias alias, TransactionId transactionId, Comparable key, Object value) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", alias, transactionId, key, value);
		try {
			return (DomainMapRange) sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(Alias alias, TransactionId transactionId, Comparable darg, Comparable marg, Comparable rarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", alias, transactionId, darg, marg, rarg);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(TransactionId transactionId, Comparable darg, Comparable marg, Comparable rarg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", transactionId, darg, marg, rarg);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("get", alias, transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("get", transactionId, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", transactionId, key);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", alias, transactionId, key);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias alias, TransactionId transactionId, Comparable darg, Comparable marg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", alias, transactionId, darg, marg);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId transactionId, Comparable darg, Comparable marg) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", transactionId, darg, marg);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
}

