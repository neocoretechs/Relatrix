package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;
import java.util.Iterator;
import com.neocoretechs.relatrix.DomainMapRange;


public abstract class RelatrixClientTransactionInterfaceImpl implements RelatrixClientTransactionInterface{

	public abstract Object sendCommand(RelatrixTransactionStatement s) throws Exception;
	@Override
	public Stream entrySetStream(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySetStream", arg1, arg2, arg3);
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
	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySetStream", arg1, arg2);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("loadClassFromJar", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("loadClassFromPath", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object[] arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public Iterator findHeadSet(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
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
	public Stream findHeadStream(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
	public Stream findHeadStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object[] arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public Stream findSubStream(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
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
	public Stream findSubStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object[] arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public Iterator findSet(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
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
	public Iterator findSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
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
	public Stream findTailStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object[] arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public Stream findTailStream(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
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
	public Iterator findSubSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object[] arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public Iterator findSubSet(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
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
	public Iterator findTailSet(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
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
	public Iterator findTailSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object[] arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public Stream findStream(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public Stream findStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public Object lastValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object lastValue(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object lastValue(String arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("checkpoint", arg1, arg2);
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
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void setAlias(String arg1,String arg2) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setAlias", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(String arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void rollback(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollback", arg1, arg2);
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
	public String[][] getAliases() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAliases","",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void setTablespace(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setTablespace", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(String arg1) throws java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTableSpace","",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void endTransaction(String arg1) throws java.lang.IllegalAccessException,java.io.IOException,java.lang.ClassNotFoundException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.ClassNotFoundException(e.getMessage());
		}
	}
	@Override
	public String getTransactionId() throws java.lang.IllegalAccessException,java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTransactionId","",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAlias(String arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void setTuple(char arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setTuple","", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void setWildcard(char arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setWildcard","", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void rollbackToCheckpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void rollbackToCheckpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollbackToCheckpoint", arg1, arg2);
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
	public Object getByIndex(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByIndex", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object getByIndex(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.lang.ClassNotFoundException(e.getMessage());
		}
	}
	@Override
	public void commit(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("commit", arg1, arg2);
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
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object firstValue(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object firstValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1, arg2, arg3);
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
	public Object firstValue(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator keySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("keySet", arg1, arg2, arg3);
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
	public Object first(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object first(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object first(String arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(String arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public Object last(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public boolean contains(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public long size(String arg1) throws java.io.IOException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(String arg1,Comparable arg2,Object arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2, arg3);
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
	public DomainMapRange store(String arg1,String arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2, arg3, arg4, arg5);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof com.neocoretechs.relatrix.DuplicateKeyException)
				throw new com.neocoretechs.relatrix.DuplicateKeyException();
			throw new java.lang.ClassNotFoundException(e.getMessage());
		}
	}
	@Override
	public DomainMapRange store(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2, arg3, arg4);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof com.neocoretechs.relatrix.DuplicateKeyException)
				throw new com.neocoretechs.relatrix.DuplicateKeyException();
			throw new java.lang.ClassNotFoundException(e.getMessage());
		}
	}
	@Override
	public Object get(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("get", arg1, arg2, arg3);
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
	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public void remove(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.lang.ClassNotFoundException(e.getMessage());
		}
	}
	@Override
	public void remove(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.util.NoSuchElementException)
				throw new java.util.NoSuchElementException(e.getMessage());
			throw new com.neocoretechs.relatrix.DuplicateKeyException();
		}
	}
	@Override
	public void remove(String arg1,String arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2, arg3, arg4, arg5);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			if(e instanceof java.util.NoSuchElementException)
				throw new java.util.NoSuchElementException(e.getMessage());
			if(e instanceof java.lang.IllegalArgumentException)
				throw new java.lang.IllegalArgumentException(e);
			if(e instanceof java.lang.ClassNotFoundException)
				throw new java.lang.ClassNotFoundException(e.getMessage());
			throw new com.neocoretechs.relatrix.DuplicateKeyException();
		}
	}
	@Override
	public void remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2);
		try {
			sendCommand(s);
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
}

