package com.neocoretechs.relatrix.client;

import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;


public interface RelatrixKVClientTransactionInterface{

	public Object lastValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object nearest(TransactionId transactionId, Comparable key) throws java.lang.IllegalAccessException,java.io.IOException;

	public Object nearest(Alias alias, TransactionId transactionId, Comparable key) throws java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException;

	public Iterator findSubMapKV(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubMapKV(Alias alias, TransactionId transactionId, Comparable from,Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMapKV(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMapKV(TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubMap(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findSubMap(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadMapStream(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadMapStream(TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMapKV(TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMapKV(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void loadClassFromJar(String jar) throws java.io.IOException;

	public Iterator findTailMap(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findTailMap(TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void loadClassFromPath(String clazz, String path) throws java.io.IOException;

	public Iterator findHeadMap(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMap(TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream keySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream keySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapKVStream(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapKVStream(TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailMapStream(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailMapStream(TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findSubMapStream(Alias alias, TransactionId transactionId, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapStream(TransactionId transactionId,Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void removeAlias(Alias alias) throws java.util.NoSuchElementException;

	public String getAlias(Alias alias);

	public String getTableSpace();

	public void endTransaction(TransactionId transactionId) throws java.io.IOException;

	public String[][] getAliases();

	public TransactionId getTransactionId() throws java.lang.IllegalAccessException,java.io.IOException,java.lang.ClassNotFoundException;

	public void setAlias(Alias alias, String path) throws java.io.IOException;

	public void setTablespace(String path) throws java.io.IOException;

	public void rollback(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void rollback(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public void checkpoint(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void checkpoint(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream findTailMapKVStream(Alias alias, TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailMapKVStream(TransactionId transactionId, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void rollbackAllTransactions();

	public Object[] getTransactionState();

	public Stream findHeadMapKVStream(Alias alias, TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadMapKVStream(TransactionId transactionId, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void rollbackToCheckpoint(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public void removePackageFromRepository(String pack) throws java.io.IOException;

	public void rollbackTransaction(TransactionId transactionId);

	public Stream entrySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream entrySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastKey(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastKey(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public void commit(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void commit(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstKey(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstKey(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean containsValue(TransactionId transactionId, Class clazz, Object key) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean containsValue(Alias alias, TransactionId transactionId, Class clazz, Object key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public void close(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void close(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public boolean contains(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public boolean contains(TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean contains(TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean contains(Alias alias, TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public long size(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public long size(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public void store(Alias alias, TransactionId transactionId, Comparable key, Object value) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException;

	public void store(TransactionId transactionId, Comparable key, Object value) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException;

	public Object get(TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object get(TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object get(Alias alias, TransactionId transactionId, Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object remove(TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Object remove(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

}

