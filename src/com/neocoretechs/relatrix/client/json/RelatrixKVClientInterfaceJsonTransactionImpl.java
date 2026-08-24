// auto generated from com.neocoretechs.relatrix.server.GenerateJsonClientBindings Mon Jul 06 14:35:32 PDT 2026
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;


public abstract class RelatrixKVClientInterfaceJsonTransactionImpl implements RelatrixKVClientInterfaceJsonTransaction{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public String getAlias(Alias arg1) {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(null,"getAliases", new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMap", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "nearest", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "setAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackAllTransactions() {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(null,"rollbackAllTransactions", new Object[]{});
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Iterator findTailMapKV(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMapKV", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findTailMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object[] getTransactionState() {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(null,"getTransactionState", new Object[]{});
		try {
			return (Object[])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMapKVStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "findSubMapStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(null,"getTransactionId", new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId(long arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "getTransactionId", arg1);
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "keySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "keySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "rollbackTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "storekv", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "containsValue", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "close", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "contains", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "get", arg1, arg2, arg3, arg4);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson(getSession(), "remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1, TransactionId arg2, Comparable arg3, Comparable arg4)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findSubMapStream(TransactionId arg1, Comparable arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findTailMapKV(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findTailMapKV(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findTailMapStream(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findTailMapStream(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findHeadMapKV(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findSubMapKVStream(TransactionId arg1, Comparable arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1, TransactionId arg2, Comparable arg3, Comparable arg4) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findTailMap(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findTailMap(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findHeadMap(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findHeadMap(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findHeadMapStream(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findHeadMapStream(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findSubMap(TransactionId arg1, Comparable arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findSubMap(Alias arg1, TransactionId arg2, Comparable arg3, Comparable arg4) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object nearest(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object nearest(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findSubMapKV(Alias arg1, TransactionId arg2, Comparable arg3, Comparable arg4) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator findSubMapKV(TransactionId arg1, Comparable arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findHeadMapKVStream(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findTailMapKVStream(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void store(TransactionId arg1, Comparable arg2, Object arg3) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void store(Alias arg1, TransactionId arg2, Comparable arg3, Object arg4) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean contains(Alias arg1, TransactionId arg2, Class arg3, Comparable arg4) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(TransactionId arg1, Class arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object get(TransactionId arg1, Class arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object get(Alias arg1, TransactionId arg2, Class arg3, Comparable arg4) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object get(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object get(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void remove(TransactionId arg1, Comparable arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(Alias arg1, TransactionId arg2, Comparable arg3) throws IOException {
		// TODO Auto-generated method stub
		
	}

}


