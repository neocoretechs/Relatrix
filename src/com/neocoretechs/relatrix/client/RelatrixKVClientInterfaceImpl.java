package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public abstract class RelatrixKVClientInterfaceImpl implements RelatrixKVClientInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	
	@Override
	public Object lastValue(Alias alias, Class clazz)throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastValue", alias, clazz);
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
	public Object lastValue(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastValue", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream entrySetStream(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySetStream", clazz);
		try {
			return (Stream)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public Stream entrySetStream(Alias alias, Class clazz)throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySetStream", alias, clazz);
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
	public Object nearest(Alias alias, Comparable key) throws java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("nearest", alias, key);
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
	public Object nearest(Comparable key) throws java.lang.IllegalAccessException,java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("nearest", key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.lang.IllegalAccessException)
				throw new java.lang.IllegalAccessException(e.getMessage());
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromPath(String clazz,String path) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("loadClassFromPath", clazz, path);
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
	public Iterator findSubMap(Alias alias, Comparable from, Comparable to)  throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMap", alias, from, to);
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
	public Iterator findHeadMap(Alias alias, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMap", alias, to);
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
	public Iterator findTailMap(Alias alias, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMap", alias, from);
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
	public Stream findTailMapStream(Alias alias, Comparable from)  throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapStream", alias, from);
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
	public Stream keySetStream(Alias alias, Class clazz)throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("keySetStream", alias, clazz);
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
	public Iterator findSubMapKV(Alias alias, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapKV", alias, from, to);
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
	public Iterator findHeadMapKV(Alias alias, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapKV", alias, to);
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
	public Stream findSubMapStream(Alias alias, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapStream", alias, from, to);
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
	public Iterator findTailMapKV(Alias alias, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapKV", alias, from);
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
	public Stream findHeadMapStream(Alias alias, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapStream", alias, to);
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
	public Stream findSubMapKVStream(Alias alias, Comparable from, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findSubMapKVStream", alias, from, to);
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
	public String getAlias(Alias alias) {
		RelatrixKVStatement s = new RelatrixKVStatement("getAlias", alias);
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
	public void removeAlias(Alias alias)  throws java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("removeAlias", alias);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public void setAlias(Alias alias, String path) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement("setAlias", alias, path);
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
	public Stream findHeadMapKVStream(Alias alias, Comparable to) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findHeadMapKVStream", alias, to);
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
	public Stream findTailMapKVStream(Alias alias, Comparable from) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("findTailMapKVStream", alias, from);
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
	public Object lastKey(Alias alias, Class clazz)  throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("lastKey", alias, clazz);
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
	public Object firstKey(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("firstKey", alias, clazz);
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
	public Object firstValue(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("firstValue", alias, clazz);
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
	public boolean containsValue(Class clazz, Object key) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("containsValue", clazz, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean containsValue(Alias alias, Class clazz, Object key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("containsValue", alias, clazz, key);
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
	public Iterator keySet(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("keySet", alias, clazz);
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
	public void close(Alias alias, Class clazz)  throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("close", alias, clazz);
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
	public Iterator entrySet(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("entrySet", alias, clazz);
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
	public boolean contains(Alias alias, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("contains", alias, key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
			throw new java.lang.IllegalAccessException(e.getMessage());
		}
	}
	@Override
	public boolean contains(Alias alias, Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("contains", alias, clazz, key);
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
	public boolean contains(Class clazz, Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixKVStatement s = new RelatrixKVStatement("contains", clazz, arg1);
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
	public long size(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("size", alias, clazz);
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
	public void store(Alias alias, Comparable key, Object value) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("store", alias, key, value);
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
	public Object get(Alias alias, Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("get", alias, clazz, key);
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
	public Object get(Alias alias, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("get", alias, key);
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
	public Object get(Class clazz, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("get", clazz, key);
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
	public Object remove(Alias alias, Comparable key)  throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixKVStatement s = new RelatrixKVStatement("remove", alias, key);
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

