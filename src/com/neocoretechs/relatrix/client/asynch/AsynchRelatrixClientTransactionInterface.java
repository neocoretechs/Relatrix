// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon May 12 10:34:56 PDT 2025
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
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;


public interface AsynchRelatrixClientTransactionInterface extends com.neocoretechs.relatrix.client.ClientTransactionInterface{

	public CompletableFuture<String[][]> getAliases();

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2);

	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> lastValue(TransactionId arg1);

	public TransactionId getTransactionId();

	public TransactionId getTransactionId(long arg1);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Void> checkpoint(TransactionId arg1);

	public CompletableFuture<Void> checkpoint(Alias arg1,TransactionId arg2);

	public CompletableFuture<Void> endTransaction(TransactionId arg1);

	public CompletableFuture<Void> removeAlias(Alias arg1);

	public CompletableFuture<Void> rollback(TransactionId arg1);

	public CompletableFuture<Void> rollback(Alias arg1,TransactionId arg2);

	public void storekv(TransactionId arg1,Comparable arg2,Object arg3);

	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4);

	public CompletableFuture<RelationList> multiStore(TransactionId arg1,ArrayList arg2);

	public CompletableFuture<RelationList> multiStore(Alias arg1,TransactionId arg2,ArrayList arg3);

	public Object getByIndex(TransactionId arg1,Comparable arg2);

	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Object arg3,Character arg4);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Character arg3,Character arg4);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5);

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Object arg3,Character arg4);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Character arg3,Object arg4);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Character arg3,Character arg4);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<List> findSet(TransactionId arg1,Object arg2);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<List> findSet(Alias arg1,TransactionId arg2,Object arg3);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4);

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Void> commit(Alias arg1,TransactionId arg2);

	public CompletableFuture<Void> commit(TransactionId arg1);

	public CompletableFuture<String> getTableSpace();

	public CompletableFuture<Void> setWildcard(Character arg1);

	public CompletableFuture<Void> setRelativeAlias(Alias arg1);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7);

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6);

	public CompletableFuture<Void> setTuple(Character arg1);

	public CompletableFuture<String> getAlias(Alias arg1);

	public CompletableFuture<Object> removekv(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Object> removekv(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Void> rollbackToCheckpoint(TransactionId arg1);

	public CompletableFuture<Void> rollbackToCheckpoint(Alias arg1,TransactionId arg2);

	public CompletableFuture<Stream> entrySetStream(TransactionId arg1,Class arg2);

	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2);

	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> lastKey(TransactionId arg1);

	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> firstKey(TransactionId arg1);

	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2);

	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> firstValue(TransactionId arg1);

	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2);

	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2);

	public CompletableFuture<Relation> store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5);

	public CompletableFuture<List> store(TransactionId arg1,ArrayList arg2);

	public CompletableFuture<List> store(Alias arg1,TransactionId arg2,ArrayList arg3);

	public CompletableFuture<Relation> store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4);

	public CompletableFuture<List> resolve(Comparable arg1);

	public CompletableFuture<Object> first(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> first(TransactionId arg1);

	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2);

	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2);

	public CompletableFuture<Long> size(TransactionId arg1);

	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2);

	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Long> size(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2,Class arg3);

	public CompletableFuture<Object> last(TransactionId arg1,Class arg2);

	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2);

	public CompletableFuture<Object> last(TransactionId arg1);

	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Boolean> contains(TransactionId arg1,Comparable arg2);

	public Object get(TransactionId arg1,Comparable arg2);

	public Object get(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Void> remove(Alias arg1,TransactionId arg2,Comparable arg3);

	public CompletableFuture<Void> remove(TransactionId arg1,Comparable arg2);

	public CompletableFuture<Void> remove(TransactionId arg1,Comparable arg2,Comparable arg3);

	public CompletableFuture<Void> remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4);

}

