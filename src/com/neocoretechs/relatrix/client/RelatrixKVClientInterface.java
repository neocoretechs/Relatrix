package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;
import java.util.Iterator;


public interface RelatrixKVClientInterface{

	public Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream entrySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object nearest(String arg1,Comparable arg2) throws java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException;

	public Object nearest(Comparable arg1) throws java.lang.IllegalAccessException,java.io.IOException;

	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException;

	public Iterator findSubMap(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubMap(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findHeadMap(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findHeadMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findTailMap(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMap(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailMapStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream keySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream keySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator findSubMapKV(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findSubMapKV(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findHeadMapKV(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findHeadMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubMapStream(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMapKV(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailMapKV(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadMapStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadMapStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void loadClassFromJar(String arg1) throws java.io.IOException;

	public Stream findSubMapKVStream(Comparable arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findSubMapKVStream(String arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public String getAlias(String arg1);

	public String[][] getAliases();

	public void removeAlias(String arg1) throws java.util.NoSuchElementException;

	public void setAlias(String arg1,String arg2) throws java.io.IOException;

	public void setTablespace(String arg1) throws java.io.IOException;

	public String getTableSpace();

	public Stream findHeadMapKVStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailMapKVStream(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailMapKVStream(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void removePackageFromRepository(String arg1) throws java.io.IOException;

	public Object lastKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean containsValue(Class arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean containsValue(String arg1,Class arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void close(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void close(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public boolean contains(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public long size(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public long size(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void store(Comparable arg1,Object arg2) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void store(String arg1,Comparable arg2,Object arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException;

	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object remove(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Object remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

}

