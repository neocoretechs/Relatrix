package com.neocoretechs.relatrix.client;

import java.util.Iterator;
import java.util.stream.Stream;


public interface RelatrixKVClientTransactionInterface{

	public Object lastValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object nearest(String arg1,Comparable arg2) throws java.lang.IllegalAccessException,java.io.IOException;

	public Object nearest(String arg1,String arg2,Comparable arg3) throws java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException;

	public Iterator findSubMapKV(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubMapKV(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMapKV(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubMap(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findSubMap(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadMapStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMapKV(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void loadClassFromJar(String arg1) throws java.io.IOException;

	public Iterator findTailMap(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findTailMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException;

	public Iterator findHeadMap(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream keySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream keySetStream(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapKVStream(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapKVStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailMapStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findSubMapStream(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void removeAlias(String arg1) throws java.util.NoSuchElementException;

	public String getAlias(String arg1);

	public String getTableSpace();

	public void endTransaction(String arg1) throws java.io.IOException;

	public String[][] getAliases();

	public String getTransactionId() throws java.lang.IllegalAccessException,java.io.IOException,java.lang.ClassNotFoundException;

	public void setAlias(String arg1,String arg2) throws java.io.IOException;

	public void setTablespace(String arg1) throws java.io.IOException;

	public void rollback(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void rollback(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public void checkpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void checkpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream findTailMapKVStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void rollbackAllTransactions();

	public Object[] getTransactionState();

	public Stream findHeadMapKVStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void rollbackToCheckpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void rollbackToCheckpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public void removePackageFromRepository(String arg1) throws java.io.IOException;

	public void rollbackTransaction(String arg1);

	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream entrySetStream(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastKey(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public void commit(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void commit(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstKey(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean containsValue(String arg1,Class arg2,Object arg3) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean containsValue(String arg1,String arg2,Class arg3,Object arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public void close(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void close(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public boolean contains(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean contains(String arg1,Class arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean contains(String arg1,String arg2,Class arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException;

	public long size(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public long size(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public void store(String arg1,String arg2,Comparable arg3,Object arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException;

	public void store(String arg1,Comparable arg2,Object arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException;

	public Object get(String arg1,Class arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object get(String arg1,String arg2,Class arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Object remove(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

}

