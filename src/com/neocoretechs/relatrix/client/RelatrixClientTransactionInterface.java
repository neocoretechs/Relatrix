// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.DomainMapRange;


public interface RelatrixClientTransactionInterface{

	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException;

	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void setWildcard(char arg1);

	public String getAlias(Alias arg1);

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,char arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,char arg2,char arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,char arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,char arg2,char arg3,char arg4) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,char arg3,char arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,char arg2,Object arg3,char arg4) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5) throws java.io.IOException;

	public List findSet(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public List findSet(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,char arg4) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException;

	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findHeadSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,char arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,char arg2,Object arg3,char arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,char arg3,char arg4) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,char arg2,char arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,char arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,char arg4) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,char arg2,char arg3,char arg4) throws java.io.IOException;

	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5) throws java.io.IOException;

	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public String getTableSpace();

	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void rollback(TransactionId arg1) throws java.io.IOException;

	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastValue(TransactionId arg1) throws java.io.IOException;

	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object lastValue(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object lastKey(TransactionId arg1) throws java.io.IOException;

	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Object removekv(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object removekv(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findHeadStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public String[][] getAliases();

	public void endTransaction(TransactionId arg1) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Stream findTailStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException;

	public Iterator findTailSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException;

	public void removeAlias(Alias arg1) throws java.io.IOException;

	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void checkpoint(TransactionId arg1) throws java.io.IOException;

	public void commit(TransactionId arg1) throws java.io.IOException;

	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public void setRelativeAlias(Alias arg1) throws java.io.IOException;

	public void setTuple(char arg1);

	public TransactionId getTransactionId() throws java.io.IOException;

	public Object getByIndex(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException;

	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException;

	public Object firstKey(TransactionId arg1) throws java.io.IOException;

	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object firstValue(TransactionId arg1) throws java.io.IOException;

	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object firstValue(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException;

	public DomainMapRange store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.io.IOException;

	public DomainMapRange store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public Object first(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object first(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object first(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object first(TransactionId arg1) throws java.io.IOException;

	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public long size(TransactionId arg1) throws java.io.IOException;

	public long size(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public long size(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object last(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException;

	public Object last(TransactionId arg1,Class arg2) throws java.io.IOException;

	public Object last(Alias arg1,TransactionId arg2) throws java.io.IOException;

	public Object last(TransactionId arg1) throws java.io.IOException;

	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException;

	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException;

	public void remove(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public void remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public void remove(TransactionId arg1,Comparable arg2) throws java.io.IOException;

}

