package com.neocoretechs.relatrix.client;

import java.util.Iterator;
import java.util.stream.Stream;


public abstract class RelatrixKVClientTransactionInterfaceImpl implements RelatrixKVClientTransactionInterface{

	public abstract Object sendCommand(RelatrixKVTransactionStatement s) throws Exception;
	@Override
	public Object lastValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object nearest(String arg1,Comparable arg2) throws java.lang.IllegalAccessException,java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(String arg1,String arg2,Comparable arg3) throws java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("nearest", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator findSubMapKV(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator findSubMapKV(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKV", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator findHeadMapKV(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator findHeadMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator findSubMap(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMap", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator findSubMap(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream findHeadMapStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findHeadMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator findTailMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator findTailMapKV(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("loadClassFromJar", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator findTailMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("loadClassFromPath", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator findHeadMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream keySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySetStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream keySetStream(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySetStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findSubMapKVStream(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKVStream", arg1, arg2, arg3, arg4);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findSubMapKVStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapKVStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream findTailMapStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findTailMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream findSubMapStream(String arg1,String arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapStream", arg1, arg2, arg3, arg4);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findSubMapStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findSubMapStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void removeAlias(String arg1) throws java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public String getAlias(String arg1) {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getTableSpace","",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void endTransaction(String arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getAliases","",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getTransactionId() throws java.lang.IllegalAccessException,java.io.IOException,java.lang.ClassNotFoundException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getTransactionId","",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.ClassNotFoundException(e.getMessage());
		}
	}
	@Override
	public void setAlias(String arg1,String arg2) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("setAlias", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTablespace(String arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("setTablespace", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void rollback(String arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void checkpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void checkpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream findTailMapKVStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKVStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findTailMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findTailMapKVStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void rollbackAllTransactions() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackAllTransactions","",new Object[]{});
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Object[] getTransactionState() {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("getTransactionState","",new Object[]{});
		try {
			return (Object[])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadMapKVStream(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKVStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Stream findHeadMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("findHeadMapKVStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void rollbackToCheckpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void rollbackToCheckpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void removePackageFromRepository(String arg1) throws java.io.IOException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("removePackageFromRepository", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackTransaction(String arg1) {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("rollbackTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySetStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream entrySetStream(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySetStream", arg1, arg2, arg3);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object lastKey(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void commit(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void commit(String arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object firstKey(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object firstValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean containsValue(String arg1,Class arg2,Object arg3) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean containsValue(String arg1,String arg2,Class arg3,Object arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("containsValue", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator keySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void close(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("close", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void close(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator entrySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator entrySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public boolean contains(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean contains(String arg1,Class arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean contains(String arg1,String arg2,Class arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("contains", arg1, arg2, arg3, arg4);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public long size(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public long size(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void store(String arg1,String arg2,Comparable arg3,Object arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("store", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof com.neocoretechs.relatrix.DuplicateKeyException)
				throw new com.neocoretechs.relatrix.DuplicateKeyException();
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void store(String arg1,Comparable arg2,Object arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new com.neocoretechs.relatrix.DuplicateKeyException();
		}
	}
	@Override
	public Object get(String arg1,Class arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object get(String arg1,String arg2,Class arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", arg1, arg2, arg3, arg4);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object get(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object remove(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVTransactionStatement s = new RelatrixKVTransactionStatement("remove", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
}

