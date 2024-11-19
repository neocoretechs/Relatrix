package com.neocoretechs.relatrix.client;

import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

public interface RelatrixKVClientTransactionInterface {

	public Object lastValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object lastValue(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object nearest(TransactionId transactionId, Comparable key) throws java.io.IOException;

	public Object nearest(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException;

	public Iterator findSubMapKV(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException;

	public Iterator findSubMapKV(Alias alias, TransactionId transactionId, Comparable from,Comparable to) throws java.io.IOException;

	public Iterator findHeadMapKV(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Iterator findHeadMapKV(TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Iterator findSubMap(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException;

	public Iterator findSubMap(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException;

	public Stream findHeadMapStream(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Stream findHeadMapStream(TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Iterator findTailMapKV(TransactionId transactionId, Comparable from) throws java.io.IOException;

	public Iterator findTailMapKV(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException;

	public void loadClassFromJar(String jar) throws java.io.IOException;

	public Iterator findTailMap(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException;

	public Iterator findTailMap(TransactionId transactionId, Comparable from) throws java.io.IOException;

	public void loadClassFromPath(String clazz, String path) throws java.io.IOException;

	public Iterator findHeadMap(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Iterator findHeadMap(TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Stream keySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Stream keySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Stream findSubMapKVStream(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException;

	public Stream findSubMapKVStream(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException;

	public Stream findTailMapStream(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException;

	public Stream findTailMapStream(TransactionId transactionId, Comparable from) throws java.io.IOException;

	public Stream findSubMapStream(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException;

	public Stream findSubMapStream(TransactionId transactionId,Comparable from, Comparable to) throws java.io.IOException;

	public void removeAlias(Alias alias) throws java.util.NoSuchElementException;

	public String getAlias(Alias alias);

	public String getTableSpace();

	public void endTransaction(TransactionId transactionId) throws java.io.IOException;

	public String[][] getAliases();

	public TransactionId getTransactionId() throws java.io.IOException;

	public void setAlias(Alias alias, String path) throws java.io.IOException;

	public void setTablespace(String path) throws java.io.IOException;

	public void rollback(Alias alias, TransactionId transactionId) throws java.io.IOException;

	public void rollback(TransactionId transactionId) throws java.io.IOException;

	public void checkpoint(Alias alias, TransactionId transactionId) throws java.io.IOException;

	public void checkpoint(TransactionId transactionId) throws java.io.IOException;

	public Stream findTailMapKVStream(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException;

	public Stream findTailMapKVStream(TransactionId transactionId, Comparable from) throws java.io.IOException;

	public void rollbackAllTransactions();

	public Object[] getTransactionState();

	public Stream findHeadMapKVStream(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException;

	public Stream findHeadMapKVStream(TransactionId transactionId, Comparable to) throws java.io.IOException;

	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws java.io.IOException;

	public void rollbackToCheckpoint(TransactionId transactionId) throws java.io.IOException;

	public void removePackageFromRepository(String pack) throws java.io.IOException;

	public void rollbackTransaction(TransactionId transactionId);

	public Stream entrySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Stream entrySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object lastKey(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object lastKey(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public void commit(Alias alias, TransactionId transactionId) throws java.io.IOException;

	public void commit(TransactionId transactionId) throws java.io.IOException;

	public Object firstKey(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object firstKey(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object firstValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object firstValue(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public boolean containsValue(TransactionId transactionId, Class clazz, Object key) throws java.io.IOException;

	public boolean containsValue(Alias alias, TransactionId transactionId, Class clazz, Object key) throws java.io.IOException;

	public Iterator keySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Iterator keySet(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public void close(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public void close(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Iterator entrySet(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Iterator entrySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public boolean contains(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException;

	public boolean contains(TransactionId transactionId, Comparable key) throws java.io.IOException;

	public boolean contains(TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException;

	public boolean contains(Alias alias, TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException;

	public long size(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException;

	public long size(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public void store(Alias alias, TransactionId transactionId, Comparable key, Object value) throws java.io.IOException;

	public void store(TransactionId transactionId, Comparable key, Object value) throws java.io.IOException;

	public Object get(TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException;

	public Object get(TransactionId transactionId, Comparable key) throws java.io.IOException;

	public Object get(Alias alias, TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException;

	public Object get(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException;

	public Object remove(TransactionId transactionId, Comparable key) throws java.io.IOException;

	public Object remove(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException;

}

