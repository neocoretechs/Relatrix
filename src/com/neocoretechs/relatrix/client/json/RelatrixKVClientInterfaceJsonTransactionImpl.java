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
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMap", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("nearest", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackAllTransactions() {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("rollbackAllTransactions",new Object[]{});
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Iterator findTailMapKV(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMapKV", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findTailMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object[] getTransactionState() {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("getTransactionState",new Object[]{});
		try {
			return (Object[])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMapKVStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("findSubMapStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("getTransactionId",new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId(long arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("getTransactionId", arg1);
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("keySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("keySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("rollbackTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("storekv", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("containsValue", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("close", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("contains", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,TransactionId arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("store", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(TransactionId arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("get", arg1, arg2, arg3, arg4);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("remove", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixKVTransactionStatementJson s = new RelatrixKVTransactionStatementJson("remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

