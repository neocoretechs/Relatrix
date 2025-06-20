// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Fri Jun 20 08:37:58 PDT 2025
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
import java.util.ArrayList;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;


public interface AsynchRelatrixClientInterface extends com.neocoretechs.relatrix.client.ClientNonTransactionInterface{

	public CompletableFuture<RelationList> multiStore(ArrayList arg1);

	public CompletableFuture<RelationList> multiStore(Alias arg1,ArrayList arg2);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findTailStream(Object arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findTailStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Object arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findTailStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Character arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findTailStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findHeadStream(Object arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findHeadStream(Object arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findHeadStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Character arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<String> getAlias(Alias arg1);

	public CompletableFuture<List> findSetParallel(Alias arg1,List arg2,Character arg3,Character arg4);

	public CompletableFuture<List> findSetParallel(Alias arg1,Character arg2,List arg3,Character arg4);

	public CompletableFuture<List> findSetParallel(Alias arg1,Character arg2,Character arg3,List arg4);

	public CompletableFuture<List> findSetParallel(List arg1,Character arg2,Character arg3);

	public CompletableFuture<List> findSetParallel(Character arg1,List arg2,Character arg3);

	public CompletableFuture<List> findSetParallel(Character arg1,Character arg2,List arg3);

	public CompletableFuture<Iterator> findSet(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Iterator> findSet(Object arg1,Object arg2,Character arg3);

	public CompletableFuture<List> findSet(Alias arg1,Object arg2);

	public CompletableFuture<List> findSet(Object arg1);

	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Object arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Object arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Character arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Character arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Character arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(Character arg1,Object arg2,Object arg3);

	public CompletableFuture<Iterator> findSet(Character arg1,Character arg2,Object arg3);

	public CompletableFuture<Iterator> findSet(Character arg1,Character arg2,Character arg3);

	public CompletableFuture<Iterator> findSet(Object arg1,Character arg2,Character arg3);

	public CompletableFuture<Iterator> findSet(Character arg1,Object arg2,Character arg3);

	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(Object arg1,Character arg2,Object arg3);

	public CompletableFuture<Void> removeAlias(Alias arg1);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Object arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findHeadSet(Character arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findHeadSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findHeadSet(Object arg1,Object arg2,Character arg3,Object arg4);

	public void storekv(Comparable arg1,Object arg2);

	public void storekv(Alias arg1,Comparable arg2,Object arg3);

	public CompletableFuture<Object> lastValue(Alias arg1);

	public CompletableFuture<Object> lastValue();

	public CompletableFuture<Object> lastValue(Class arg1);

	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2);

	public CompletableFuture<Void> setTuple(Character arg1);

	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findSubStream(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Object arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<String[][]> getAliases();

	public Object getByIndex(Alias arg1,DBKey arg2);

	public Object getByIndex(DBKey arg1);

	public CompletableFuture<Void> setRelativeAlias(Alias arg1);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<String> getTableSpace();

	public CompletableFuture<DBKey> getNewKey();

	public CompletableFuture<Iterator> findTailSet(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Iterator> findTailSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Character arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findTailSet(Object arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Iterator> findTailSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Object arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Object> removekv(Alias arg1,Comparable arg2);

	public CompletableFuture<Object> removekv(Comparable arg1);

	public CompletableFuture<Void> setWildcard(Character arg1);

	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Object arg3,Character arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Object arg3,Character arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Character arg3,Character arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Character arg3,Character arg4);

	public CompletableFuture<Stream> findStream(Character arg1,Character arg2,Object arg3);

	public CompletableFuture<Stream> findStream(Character arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findStream(Object arg1,Object arg2,Object arg3);

	public CompletableFuture<Stream> findStream(Object arg1,Object arg2,Character arg3);

	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findStream(Character arg1,Object arg2,Character arg3);

	public CompletableFuture<Stream> findStream(Character arg1,Character arg2,Character arg3);

	public CompletableFuture<Stream> findStream(Object arg1,Character arg2,Object arg3);

	public CompletableFuture<Stream> findStream(Object arg1,Character arg2,Character arg3);

	public CompletableFuture<Stream> entrySetStream(Alias arg1,Class arg2);

	public CompletableFuture<Stream> entrySetStream(Class arg1);

	public CompletableFuture<Object> lastKey(Alias arg1);

	public CompletableFuture<Object> lastKey(Class arg1);

	public CompletableFuture<Object> lastKey(Alias arg1,Class arg2);

	public CompletableFuture<Object> lastKey();

	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2);

	public CompletableFuture<Object> firstKey(Class arg1);

	public CompletableFuture<Object> firstKey();

	public CompletableFuture<Object> firstKey(Alias arg1);

	public CompletableFuture<Object> firstValue();

	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2);

	public CompletableFuture<Object> firstValue(Alias arg1);

	public CompletableFuture<Object> firstValue(Class arg1);

	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2);

	public CompletableFuture<Iterator> keySet(Class arg1);

	public CompletableFuture<List> store(Alias arg1,ArrayList arg2);

	public CompletableFuture<Relation> store(Comparable arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Relation> store(Alias arg1,Comparable arg2,Comparable arg3,Comparable arg4);

	public CompletableFuture<List> store(ArrayList arg1);

	public CompletableFuture<List> resolve(Comparable arg1);

	public CompletableFuture<Object> first(Class arg1);

	public CompletableFuture<Object> first(Alias arg1,Class arg2);

	public CompletableFuture<Object> first(Alias arg1);

	public CompletableFuture<Object> first();

	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2);

	public CompletableFuture<Iterator> entrySet(Class arg1);

	public CompletableFuture<Long> size();

	public CompletableFuture<Long> size(Alias arg1,Class arg2);

	public CompletableFuture<Long> size(Class arg1);

	public CompletableFuture<Long> size(Alias arg1);

	public CompletableFuture<Object> last(Alias arg1,Class arg2);

	public CompletableFuture<Object> last(Class arg1);

	public CompletableFuture<Object> last(Alias arg1);

	public CompletableFuture<Object> last();

	public CompletableFuture<Boolean> contains(Alias arg1,Comparable arg2);

	public CompletableFuture<Boolean> contains(Comparable arg1);

	public Object get(Comparable arg1);

	public Object get(Alias arg1,Comparable arg2);

	public CompletableFuture<Void> remove(Alias arg1,Comparable arg2);

	public CompletableFuture<Void> remove(Comparable arg1);

	public CompletableFuture<Void> remove(Comparable arg1,Comparable arg2);

	public CompletableFuture<Void> remove(Alias arg1,Comparable arg2,Comparable arg3);

}

