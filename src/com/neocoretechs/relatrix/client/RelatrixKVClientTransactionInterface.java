// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings Sat Mar 01 15:03:00 PST 2025
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;


public interface RelatrixKVClientTransactionInterface{

	public void setRelativeAlias(Alias arg1) throws java.io.IOException;

	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Stream findSubMapStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public Stream findSubMapStream(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findTailMapKV(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findTailMapKV(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Stream findTailMapStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Stream findTailMapStream(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findHeadMapKV(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findHeadMapKV(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void endTransaction(TransactionId arg1) throws java.io.IOException;

	public void checkpoint(TransactionId arg1) throws java.io.IOException;

	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void rollback(TransactionId arg1) throws java.io.IOException;

	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Stream findSubMapKVStream(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Stream findSubMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public Iterator findTailMap(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findTailMap(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Stream keySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Stream keySetStream(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Iterator findHeadMap(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findHeadMap(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void commit(TransactionId arg1) throws java.io.IOException;

	public TransactionId getTransactionId() throws java.io.IOException;

	public TransactionId getTransactionId(long arg1) throws java.io.IOException;

	public String[][] getAliases();

	public Stream findHeadMapStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Stream findHeadMapStream(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findSubMap(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findSubMap(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public void removeAlias(Alias arg1) throws java.io.IOException;

	public Object nearest(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Object nearest(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findSubMapKV(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public Iterator findSubMapKV(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public String getAlias(Alias arg1);

	public void rollbackAllTransactions();

	public Stream findHeadMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Stream findHeadMapKVStream(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Stream findTailMapKVStream(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Stream findTailMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException;

	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void rollbackTransaction(TransactionId arg1) throws java.io.IOException;

	public Object[] getTransactionState();

	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public void close(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public void close(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public boolean containsValue(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException;

	public boolean containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException;

	public void store(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException;

	public void store(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException;

	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public long size(TransactionId arg1,Class arg2) throws java.io.IOException;

	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public boolean contains(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) throws java.io.IOException;

	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public boolean contains(TransactionId arg1,Class arg2,Comparable arg3) throws java.io.IOException;

	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object get(TransactionId arg1,Class arg2,Comparable arg3) throws java.io.IOException;

	public Object get(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) throws java.io.IOException;

	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object remove(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

}

