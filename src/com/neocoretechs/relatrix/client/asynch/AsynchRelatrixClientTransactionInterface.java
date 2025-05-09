// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings Mon Mar 17 07:45:21 PDT 2025
package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.rocksack.TransactionId;

public interface AsynchRelatrixClientTransactionInterface extends AsynchClientTransactionInterface{

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Object arg3,Character arg4) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Character arg3,Object arg4) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Character arg3,Character arg4) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Character arg3,Character arg4) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Object arg3,Character arg4) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Character arg3,Object arg4) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) ;

	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Object> rollback(TransactionId arg1) ;

	public CompletableFuture<Object> rollback(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> commit(TransactionId arg1) ;

	public CompletableFuture<Object> commit(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> getByIndex(TransactionId arg1,Comparable arg2) ;

	public CompletableFuture<Object> getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) ;

	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4);

	public void storekv(TransactionId arg1,Comparable arg2,Object arg3);

	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> lastValue(TransactionId arg1) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) ;

	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Object> removeAlias(Alias arg1) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) ;

	public CompletableFuture<List> findSet(TransactionId arg1,Object arg2) ;

	public CompletableFuture<List> findSet(Alias arg1,TransactionId arg2,Object arg3) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) ;

	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4) ;

	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4) ;

	public CompletableFuture<Object> multiStore(TransactionId arg1,ArrayList arg2) ;

	public CompletableFuture<Object> multiStore(Alias arg1,TransactionId arg2,ArrayList arg3) ;

	public CompletableFuture<Object> endTransaction(TransactionId arg1) ;

	public CompletableFuture<Object> setRelativeAlias(Alias arg1);

	public CompletableFuture<Object> checkpoint(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> checkpoint(TransactionId arg1) ;

	public CompletableFuture<Object> getAlias(Alias arg1);

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) ;

	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) ;

	public CompletableFuture<Object> removekv(TransactionId arg1,Comparable arg2) ;

	public CompletableFuture<Object> removekv(Alias arg1,TransactionId arg2,Comparable arg3) ;

	public CompletableFuture<Object> getTransactionId(long arg1) ;

	public TransactionId getTransactionId() ;

	public CompletableFuture<Object> getAliases();

	public CompletableFuture<Object> rollbackToCheckpoint(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> rollbackToCheckpoint(TransactionId arg1) ;

	public CompletableFuture<Stream> entrySetStream(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> setTuple(Character arg1);

	public CompletableFuture<Object> setWildcard(Character arg1);

	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> lastKey(TransactionId arg1) ;

	public CompletableFuture<Object> firstKey(TransactionId arg1) ;

	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> getTableSpace();

	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> firstValue(TransactionId arg1) ;

	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5) ;

	public CompletableFuture<Object> store(TransactionId arg1,ArrayList arg2) ;

	public CompletableFuture<Object> store(Alias arg1,TransactionId arg2,ArrayList arg3) ;

	public CompletableFuture<Object> store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4) ;

	public CompletableFuture<Object> resolve(Comparable arg1);

	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> first(TransactionId arg1) ;

	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> first(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> size(TransactionId arg1) ;

	public CompletableFuture<Object> size(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> size(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> size(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> last(TransactionId arg1,Class arg2) ;

	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2) ;

	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2,Class arg3) ;

	public CompletableFuture<Object> last(TransactionId arg1) ;

	public CompletableFuture<Object> contains(TransactionId arg1,Comparable arg2) ;

	public CompletableFuture<Object> contains(Alias arg1,TransactionId arg2,Comparable arg3) ;

	public CompletableFuture<Object> get(TransactionId arg1,Comparable arg2) ;

	public CompletableFuture<Object> get(Alias arg1,TransactionId arg2,Comparable arg3) ;

	public CompletableFuture<Object> remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) ;

	public CompletableFuture<Object> remove(TransactionId arg1,Comparable arg2,Comparable arg3) ;

	public CompletableFuture<Object> remove(TransactionId arg1,Comparable arg2) ;

	public CompletableFuture<Object> remove(Alias arg1,TransactionId arg2,Comparable arg3) ;

}

