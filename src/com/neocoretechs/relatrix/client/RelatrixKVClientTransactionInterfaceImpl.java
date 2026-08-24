// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings Sat Mar 01 15:03:00 PST 2025
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

public abstract class RelatrixKVClientTransactionInterfaceImpl implements RelatrixKVClientTransactionInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "setAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMapStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMapKVStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "keySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "keySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(),"getTransactionId", new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId(long arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "getTransactionId", arg1);
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(),"getAliases", new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMap", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "nearest", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMapKV", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void rollbackAllTransactions() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(),"rollbackAllTransactions", new Object[]{});
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findHeadMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "findTailMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "rollbackTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object[] getTransactionState() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(),"getTransactionState", new Object[]{});
		try {
			return (Object[])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "close", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "containsValue", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "store", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2, arg3, arg4);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}

	@Override
	public void remove(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement(getSession(), "remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

