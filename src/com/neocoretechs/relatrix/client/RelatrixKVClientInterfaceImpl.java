package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;
import java.util.Iterator;


public abstract class RelatrixKVClientInterfaceImpl implements RelatrixKVClientInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastValue", arg1, arg2);
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
	public Object lastValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream entrySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySetStream", arg1);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySetStream", arg1, arg2);
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
	public Object nearest(String arg1,Comparable arg2) throws java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("nearest", arg1, arg2);
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
	public Object nearest(Comparable arg1) throws java.lang.IllegalAccessException,java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("nearest", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("loadClassFromPath", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMap", arg1, arg2);
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
	public Iterator findSubMap(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMap", arg1, arg2, arg3);
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
	public Iterator findHeadMap(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMap", arg1);
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
	public Iterator findHeadMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMap", arg1, arg2);
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
	public Iterator findTailMap(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMap", arg1);
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
	public Iterator findTailMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMap", arg1, arg2);
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
	public Stream findTailMapStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapStream", arg1);
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
	public Stream findTailMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapStream", arg1, arg2);
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
	public Stream keySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("keySetStream", arg1, arg2);
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
	public Stream keySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("keySetStream", arg1);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator findSubMapKV(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapKV", arg1, arg2, arg3);
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
	public Iterator findSubMapKV(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapKV", arg1, arg2);
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
	public Iterator findHeadMapKV(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapKV", arg1);
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
	public Iterator findHeadMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapKV", arg1, arg2);
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
	public Stream findSubMapStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapStream", arg1, arg2, arg3);
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
	public Stream findSubMapStream(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapStream", arg1, arg2);
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
	public Iterator findTailMapKV(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapKV", arg1);
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
	public Iterator findTailMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapKV", arg1, arg2);
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
	public Stream findHeadMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapStream", arg1, arg2);
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
	public Stream findHeadMapStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapStream", arg1);
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
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("loadClassFromJar", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapKVStream", arg1, arg2);
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
	public Stream findSubMapKVStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapKVStream", arg1, arg2, arg3);
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
	public String getAlias(String arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixKVStatement s = new RelatrixKVStatement("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void removeAlias(String arg1) throws java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void setAlias(String arg1,String arg2) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("setAlias", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTablespace(String arg1) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("setTablespace", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixKVStatement s = new RelatrixKVStatement("getTableSpace",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadMapKVStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapKVStream", arg1);
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
	public Stream findHeadMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapKVStream", arg1, arg2);
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
	public Stream findTailMapKVStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapKVStream", arg1);
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
	public Stream findTailMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapKVStream", arg1, arg2);
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
	public void removePackageFromRepository(String arg1) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("removePackageFromRepository", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastKey", arg1, arg2);
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
	public Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("firstKey", arg1, arg2);
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
	public Object firstKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("firstValue", arg1, arg2);
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
	public Object firstValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean containsValue(Class arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("containsValue", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean containsValue(String arg1,Class arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("containsValue", arg1, arg2, arg3);
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
	public Iterator keySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("keySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("keySet", arg1, arg2);
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
	public void close(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("close", arg1, arg2);
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
	public void close(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("close", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator entrySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Iterator entrySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySet", arg1, arg2);
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
	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean contains(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("contains", arg1);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public long size(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public long size(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("size", arg1, arg2);
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
	public void store(Comparable arg1,Object arg2) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixKVStatement s = new RelatrixKVStatement("store", arg1, arg2);
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
	public void store(String arg1,Comparable arg2,Object arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("store", arg1, arg2, arg3);
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
	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("get", arg1, arg2);
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
	public Object get(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("get", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Object remove(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("remove", arg1);
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
	public Object remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("remove", arg1, arg2);
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

