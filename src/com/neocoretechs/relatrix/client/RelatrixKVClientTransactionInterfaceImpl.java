// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.session.TransactionalMap;
import com.neocoretechs.relatrix.RelatrixKVTransaction;


public abstract class RelatrixKVClientTransactionInterfaceImpl implements RelatrixKVClientTransactionInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias arg1,String arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setAlias", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromJar", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromPath", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixStatement s = new RelatrixStatement("getTableSpace",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void setTablespace(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setTablespace", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getTransactionId",new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixStatement s = new RelatrixStatement("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySetStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMap", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKVStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("nearest", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKV", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackAllTransactions() {
		RelatrixStatement s = new RelatrixStatement("rollbackAllTransactions",new Object[]{});
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollbackTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removePackageFromRepository(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removePackageFromRepository", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object[] getTransactionState() {
		RelatrixStatement s = new RelatrixStatement("getTransactionState",new Object[]{});
		try {
			return (Object[])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionalMap getMap(Alias arg1,Class arg2,TransactionId arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getMap", arg1, arg2, arg3);
		try {
			return (TransactionalMap)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionalMap getMap(Class arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getMap", arg1, arg2);
		try {
			return (TransactionalMap)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("close", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("containsValue", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(TransactionId arg1,Class arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelatrixKVTransaction getInstance() {
		RelatrixStatement s = new RelatrixStatement("getInstance",new Object[]{});
		try {
			return (RelatrixKVTransaction)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2, arg3, arg4);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

