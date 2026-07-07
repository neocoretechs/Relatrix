// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon Jul 06 19:41:06 PDT 2026
package com.neocoretechs.relatrix.client.asynch.json;

import java.util.Iterator;
import java.util.stream.Stream;

import java.util.concurrent.CompletableFuture;

import com.neocoretechs.rocksack.Alias;

import com.neocoretechs.relatrix.key.DBKey;

public interface AsynchRelatrixKVClientInterfaceJson extends com.neocoretechs.relatrix.client.ClientNonTransactionInterface{

	public CompletableFuture<Object> nearest(Alias arg1,Object arg2);

	public CompletableFuture<Object> nearest(Object arg1);

	public CompletableFuture<String> getAlias(Alias arg1);

	public CompletableFuture<Object> lastValue(Class arg1);

	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2);

	public CompletableFuture<Iterator> findSubMap(Object arg1,Object arg2);

	public CompletableFuture<Iterator> findSubMap(Alias arg1,Object arg2,Object arg3);

	public CompletableFuture<String[][]> getAliases();

	public void storekv(Comparable arg1,Object arg2);

	public void storekv(Alias arg1,Comparable arg2,Object arg3);

	public CompletableFuture<Iterator> findSubMapKV(Object arg1,Object arg2);

	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findSubMapStream(Alias arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findSubMapStream(Object arg1,Object arg2);

	public CompletableFuture<Stream> findHeadMapKVStream(Object arg1);

	public CompletableFuture<Stream> findHeadMapKVStream(Alias arg1,Object arg2);

	public CompletableFuture<Stream> findHeadMapStream(Alias arg1,Object arg2);

	public CompletableFuture<Stream> findHeadMapStream(Object arg1);

	public CompletableFuture<Iterator> findTailMap(Alias arg1,Object arg2);

	public CompletableFuture<Iterator> findTailMap(Object arg1);

	public CompletableFuture<Void> setRelativeAlias(Alias arg1);

	public CompletableFuture<Iterator> findHeadMap(Alias arg1,Object arg2);

	public CompletableFuture<Iterator> findHeadMap(Object arg1);

	public CompletableFuture<Stream> findTailMapKVStream(Alias arg1,Object arg2);

	public CompletableFuture<Stream> findTailMapKVStream(Object arg1);

	public CompletableFuture<Stream> findTailMapStream(Alias arg1,Object arg2);

	public CompletableFuture<Stream> findTailMapStream(Object arg1);

	public CompletableFuture<Iterator> findTailMapKV(Object arg1);

	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,Object arg2);

	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,Object arg2);

	public CompletableFuture<Iterator> findHeadMapKV(Object arg1);

	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findSubMapKVStream(Object arg1,Object arg2);

	public CompletableFuture<Void> removeAlias(Alias arg1);

	public CompletableFuture<Stream> keySetStream(Class arg1);

	public CompletableFuture<Stream> keySetStream(Alias arg1,Class arg2);

	public CompletableFuture<Stream> entrySetStream(Class arg1);

	public CompletableFuture<Stream> entrySetStream(Alias arg1,Class arg2);

	public CompletableFuture<Object> lastKey(Alias arg1,Class arg2);

	public CompletableFuture<Object> lastKey(Class arg1);

	public Object getByIndex(DBKey arg1);

	public Object getByIndex(Alias arg1,DBKey arg2);

	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2);

	public CompletableFuture<Object> firstKey(Class arg1);

	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2);

	public CompletableFuture<Object> firstValue(Class arg1);

	public CompletableFuture<Boolean> containsValue(Class arg1,Object arg2);

	public CompletableFuture<Boolean> containsValue(Alias arg1,Class arg2,Comparable arg3);

	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2);

	public CompletableFuture<Iterator> keySet(Class arg1);

	public CompletableFuture<Void> close(Alias arg1,Class arg2);

	public CompletableFuture<Void> close(Class arg1);

	public CompletableFuture<Iterator> entrySet(Class arg1);

	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2);

	public CompletableFuture<Boolean> contains(Object arg1);

	public CompletableFuture<Boolean> contains(Alias arg1,Object arg2);

	public CompletableFuture<Void> store(Object arg1,Object arg2);

	public CompletableFuture<Void> store(Alias arg1,Object arg2,Object arg3);

	public Object get(Object arg1);

	public Object get(Alias arg1,Object arg2);

	public CompletableFuture<Long> size(Class arg1);

	public CompletableFuture<Long> size(Alias arg1,Class arg2);

	public Object remove(Alias arg1,Object arg2);

	public Object remove(Object arg1);

}

