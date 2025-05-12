// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon May 12 12:57:28 PDT 2025
package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.*;


public interface AsynchRelatrixKVClientTransactionInterface extends com.neocoretechs.relatrix.client.ClientTransactionInterface{

	public CompletableFuture<Object[]> getTransactionState();

	public CompletableFuture<Stream> findTailMapKVStream(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Stream> findTailMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Stream> findHeadMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Stream> findHeadMapKVStream(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Void> rollbackToCheckpoint(TransactionId arg1);

	public CompletableFuture<Void> rollbackToCheckpoint(Alias arg1,TransactionId arg2);

	public CompletableFuture<Void> rollbackTransaction(TransactionId arg1);

	public CompletableFuture<Void> rollbackAllTransactions();

	public CompletableFuture<Void> rollback(Alias arg1,TransactionId arg2);

	public CompletableFuture<Void> rollback(TransactionId arg1);

	public CompletableFuture<Object> nearest(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Object> nearest(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Iterator> findTailMap(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Iterator> findTailMap(TransactionId arg1,Comparable arg2);

	public CompletableFuture<String[][]> getAliases();

	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2);

	public CompletableFuture<Void> setRelativeAlias(Alias arg1);

	public CompletableFuture<Stream> keySetStream(TransactionId arg1,Class arg2);

	public CompletableFuture<Stream> keySetStream(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Stream> findSubMapStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4);

	public CompletableFuture<Stream> findSubMapStream(TransactionId arg1,Comparable arg2,Comparable arg3);

	public TransactionId getTransactionId();

	public TransactionId getTransactionId(long arg1);

	public CompletableFuture<Iterator> findHeadMap(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Iterator> findHeadMap(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4);

	public CompletableFuture<Stream> findSubMapKVStream(TransactionId arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Stream> findTailMapStream(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Stream> findTailMapStream(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Void> endTransaction(TransactionId arg1);

	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4);

	public CompletableFuture<Iterator> findSubMapKV(TransactionId arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Stream> entrySetStream(TransactionId arg1,Class arg2);

	public CompletableFuture<Iterator> findSubMap(TransactionId arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Iterator> findSubMap(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4);

	public CompletableFuture<Iterator> findHeadMapKV(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Void> checkpoint(Alias arg1,TransactionId arg2);

	public CompletableFuture<Void> checkpoint(TransactionId arg1);

	public CompletableFuture<Stream> findHeadMapStream(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Stream> findHeadMapStream(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<String> getAlias(Alias arg1);

	public CompletableFuture<Iterator> findTailMapKV(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Void> commit(Alias arg1,TransactionId arg2);

	public CompletableFuture<Void> commit(TransactionId arg1);

	public CompletableFuture<Void> removeAlias(Alias arg1);

	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Void> close(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Void> close(TransactionId arg1,Class arg2);

	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2);

	public CompletableFuture<Boolean> containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4);

	public CompletableFuture<Boolean> containsValue(TransactionId arg1,Class arg2,Object arg3);

	public CompletableFuture<Void> store(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4);

	public CompletableFuture<Void> store(TransactionId arg1,Comparable arg2,Object arg3);

	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2);

	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Long> size(TransactionId arg1,Class arg2);

	public CompletableFuture<Boolean> contains(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Boolean> contains(TransactionId arg1,Class arg2,Comparable arg3);

	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4);

	public Object get(TransactionId arg1,Class arg2,Comparable arg3);

	public Object get(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4);

	public Object get(Alias arg1,TransactionId arg2,Comparable arg3);

	public Object get(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Object> remove(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Object> remove(TransactionId arg1,Comparable arg2);

}

