// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon May 12 11:37:11 PDT 2025
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


public interface AsynchRelatrixKVClientInterface extends com.neocoretechs.relatrix.client.ClientInterface{

	public CompletableFuture<Object> lastValue(Class arg1);

	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2);

	public CompletableFuture<Stream> findHeadMapStream(Alias arg1,Comparable arg2);

	public CompletableFuture<Stream> findHeadMapStream(Comparable arg1);

	public CompletableFuture<Stream> keySetStream(Alias arg1,Class arg2);

	public CompletableFuture<Stream> keySetStream(Class arg1);

	public CompletableFuture<Stream> findSubMapKVStream(Comparable arg1,Comparable arg2);

	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Void> removeAlias(Alias arg1);

	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Iterator> findSubMapKV(Comparable arg1,Comparable arg2);

	public CompletableFuture<Stream> findSubMapStream(Alias arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Stream> findSubMapStream(Comparable arg1,Comparable arg2);

	public CompletableFuture<Iterator> findTailMap(Alias arg1,Comparable arg2);

	public CompletableFuture<Iterator> findTailMap(Comparable arg1);

	public CompletableFuture<String> getAlias(Alias arg1);

	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,Comparable arg2);

	public CompletableFuture<Iterator> findHeadMapKV(Comparable arg1);

	public CompletableFuture<Stream> findTailMapStream(Comparable arg1);

	public CompletableFuture<Stream> findTailMapStream(Alias arg1,Comparable arg2);

	public CompletableFuture<Void> setRelativeAlias(Alias arg1);

	public CompletableFuture<String[][]> getAliases();

	public CompletableFuture<Object> nearest(Comparable arg1);

	public CompletableFuture<Object> nearest(Alias arg1,Comparable arg2);

	public CompletableFuture<Iterator> findSubMap(Alias arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Iterator> findSubMap(Comparable arg1,Comparable arg2);

	public CompletableFuture<Iterator> findHeadMap(Comparable arg1);

	public CompletableFuture<Iterator> findHeadMap(Alias arg1,Comparable arg2);

	public CompletableFuture<Stream> entrySetStream(Alias arg1,Class arg2);

	public CompletableFuture<Stream> entrySetStream(Class arg1);

	public CompletableFuture<Iterator> findTailMapKV(Comparable arg1);

	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,Comparable arg2);

	public CompletableFuture<Stream> findHeadMapKVStream(Alias arg1,Comparable arg2);

	public CompletableFuture<Stream> findHeadMapKVStream(Comparable arg1);

	public CompletableFuture<Stream> findTailMapKVStream(Comparable arg1);

	public CompletableFuture<Stream> findTailMapKVStream(Alias arg1,Comparable arg2);

	public CompletableFuture<Object> lastKey(Alias arg1,Class arg2);

	public CompletableFuture<Object> lastKey(Class arg1);

	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2);

	public CompletableFuture<Object> firstKey(Class arg1);

	public CompletableFuture<Object> firstValue(Class arg1);

	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2);

	public CompletableFuture<Void> close(Alias arg1,Class arg2);

	public CompletableFuture<Void> close(Class arg1);

	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2);

	public CompletableFuture<Iterator> keySet(Class arg1);

	public CompletableFuture<Boolean> containsValue(Alias arg1,Class arg2,Comparable arg3);

	public CompletableFuture<Boolean> containsValue(Class arg1,Comparable arg2);

	public CompletableFuture<Void> store(Comparable arg1,Object arg2);

	public CompletableFuture<Void> store(Alias arg1,Comparable arg2,Object arg3);

	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2);

	public CompletableFuture<Iterator> entrySet(Class arg1);

	public CompletableFuture<Long> size(Alias arg1,Class arg2);

	public CompletableFuture<Long> size(Class arg1);

	public CompletableFuture<Boolean> contains(Comparable arg1);

	public CompletableFuture<Boolean> contains(Alias arg1,Comparable arg2);

	public Object get(Alias arg1,Comparable arg2);

	public Object get(Comparable arg1);

	public CompletableFuture<Object> remove(Comparable arg1);

	public CompletableFuture<Object> remove(Alias arg1,Comparable arg2);

}

