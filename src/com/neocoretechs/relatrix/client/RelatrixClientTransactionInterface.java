// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings Mon Mar 17 07:45:21 PDT 2025
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;


import java.util.ArrayList;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.TransactionId;


public interface RelatrixClientTransactionInterface{

	public Stream findStream(TransactionId arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public void rollback(TransactionId arg1) throws java.io.IOException;

	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void commit(TransactionId arg1) throws java.io.IOException;

	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object getByIndex(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException;

	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException;

	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastValue(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object lastValue(TransactionId arg1) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public void removeAlias(Alias arg1) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) throws java.io.IOException;

	public List findSet(TransactionId arg1,Object arg2) throws java.io.IOException;

	public List findSet(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException;

	public RelationList multiStore(TransactionId arg1,ArrayList arg2) throws java.io.IOException;

	public RelationList multiStore(Alias arg1,TransactionId arg2,ArrayList arg3) throws java.io.IOException;

	public void endTransaction(TransactionId arg1) throws java.io.IOException;

	public void setRelativeAlias(Alias arg1) throws java.io.IOException;

	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void checkpoint(TransactionId arg1) throws java.io.IOException;

	public String getAlias(Alias arg1);

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Object removekv(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object removekv(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public TransactionId getTransactionId(long arg1) throws java.io.IOException;

	public TransactionId getTransactionId() throws java.io.IOException;

	public String[][] getAliases();

	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException;

	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public void setTuple(Character arg1);

	public void setWildcard(Character arg1);

	public Object lastKey(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object lastKey(TransactionId arg1) throws java.io.IOException;

	public Object firstKey(TransactionId arg1) throws java.io.IOException;

	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException;

	public String getTableSpace();

	public Object firstValue(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object firstValue(TransactionId arg1) throws java.io.IOException;

	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Relation store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.io.IOException;

	public List store(TransactionId arg1,ArrayList arg2) throws java.io.IOException;

	public List store(Alias arg1,TransactionId arg2,ArrayList arg3) throws java.io.IOException;

	public Relation store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public List resolve(Comparable arg1);

	public Object first(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object first(TransactionId arg1) throws java.io.IOException;

	public Object first(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object first(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException;

	public long size(TransactionId arg1) throws java.io.IOException;

	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public long size(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public long size(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object last(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object last(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object last(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object last(TransactionId arg1) throws java.io.IOException;

	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public void remove(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public void remove(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

}

