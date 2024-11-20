package com.neocoretechs.relatrix.client;

import java.util.stream.Stream;

import com.neocoretechs.rocksack.Alias;

import java.util.Iterator;


public interface RelatrixKVClientInterface {

	public Object lastValue(Alias alias, Class clazz) throws java.io.IOException;

	public Object lastValue( Class clazz) throws java.io.IOException;

	public Object nearest( Comparable key) throws java.io.IOException;

	public Object nearest(Alias alias, Comparable key) throws java.io.IOException;

	public Iterator findSubMapKV( Comparable from, Comparable to) throws java.io.IOException;

	public Iterator findSubMapKV(Alias alias, Comparable from,Comparable to) throws java.io.IOException;

	public Iterator findHeadMapKV(Alias alias, Comparable to) throws java.io.IOException;

	public Iterator findHeadMapKV( Comparable to) throws java.io.IOException;

	public Iterator findSubMap(Alias alias, Comparable from, Comparable to) throws java.io.IOException;

	public Iterator findSubMap( Comparable from, Comparable to) throws java.io.IOException;

	public Stream findHeadMapStream(Alias alias, Comparable to) throws java.io.IOException;

	public Stream findHeadMapStream( Comparable to) throws java.io.IOException;

	public Iterator findTailMapKV( Comparable from) throws java.io.IOException;

	public Iterator findTailMapKV(Alias alias, Comparable from) throws java.io.IOException;

	public void loadClassFromJar(String jar) throws java.io.IOException;

	public Iterator findTailMap(Alias alias, Comparable from) throws java.io.IOException;

	public Iterator findTailMap( Comparable from) throws java.io.IOException;

	public void loadClassFromPath(String clazz, String path) throws java.io.IOException;

	public Iterator findHeadMap(Alias alias, Comparable to) throws java.io.IOException;

	public Iterator findHeadMap( Comparable to) throws java.io.IOException;

	public Stream keySetStream( Class clazz) throws java.io.IOException;

	public Stream keySetStream(Alias alias, Class clazz) throws java.io.IOException;

	public Stream findSubMapKVStream(Alias alias, Comparable from, Comparable to) throws java.io.IOException;

	public Stream findSubMapKVStream( Comparable from, Comparable to) throws java.io.IOException;

	public Stream findTailMapStream(Alias alias, Comparable from) throws java.io.IOException;

	public Stream findTailMapStream( Comparable from) throws java.io.IOException;

	public Stream findSubMapStream(Alias alias, Comparable from, Comparable to) throws java.io.IOException;

	public Stream findSubMapStream(Comparable from, Comparable to) throws java.io.IOException;

	public void removeAlias(Alias alias) throws java.util.NoSuchElementException;

	public String getAlias(Alias alias);

	public String getTableSpace();

	public String[][] getAliases();

	public void setAlias(Alias alias, String path) throws java.io.IOException;

	public void setTablespace(String path) throws java.io.IOException;

	public Stream findTailMapKVStream(Alias alias, Comparable from) throws java.io.IOException;

	public Stream findTailMapKVStream( Comparable from) throws java.io.IOException;

	public Stream findHeadMapKVStream(Alias alias, Comparable to) throws java.io.IOException;

	public Stream findHeadMapKVStream( Comparable to) throws java.io.IOException;

	public void removePackageFromRepository(String pack) throws java.io.IOException;

	public Stream entrySetStream( Class clazz) throws java.io.IOException;

	public Stream entrySetStream(Alias alias, Class clazz) throws java.io.IOException;

	public Object lastKey(Alias alias, Class clazz) throws java.io.IOException;

	public Object lastKey( Class clazz) throws java.io.IOException;

	public Object firstKey( Class clazz) throws java.io.IOException;

	public Object firstKey(Alias alias, Class clazz) throws java.io.IOException;

	public Object firstValue(Alias alias, Class clazz) throws java.io.IOException;

	public Object firstValue( Class clazz) throws java.io.IOException;

	public boolean containsValue( Class clazz, Object key) throws java.io.IOException;

	public boolean containsValue(Alias alias, Class clazz, Object key) throws java.io.IOException;

	public Iterator keySet(Alias alias, Class clazz) throws java.io.IOException;

	public Iterator keySet( Class clazz) throws java.io.IOException;

	public void close(Alias alias, Class clazz) throws java.io.IOException;

	public void close( Class clazz) throws java.io.IOException;

	public Iterator entrySet( Class clazz) throws java.io.IOException;

	public Iterator entrySet(Alias alias, Class clazz) throws java.io.IOException;

	public boolean contains(Alias alias, Comparable key) throws java.io.IOException;

	public boolean contains( Comparable key) throws java.io.IOException;

	public boolean contains( Class clazz, Comparable key) throws java.io.IOException;

	public boolean contains(Alias alias, Class clazz, Comparable key) throws java.io.IOException;

	public long size(Alias alias, Class clazz) throws java.io.IOException;

	public long size( Class clazz) throws java.io.IOException;

	public void store(Alias alias, Comparable key, Object value) throws java.io.IOException;

	public void store( Comparable key, Object value) throws java.io.IOException;

	public Object get( Class clazz, Comparable key) throws java.io.IOException;

	public Object get( Comparable key) throws java.io.IOException;

	public Object get(Alias alias, Class clazz, Comparable key) throws java.io.IOException;

	public Object get(Alias alias, Comparable key) throws java.io.IOException;

	public Object remove( Comparable key) throws java.io.IOException;

	public Object remove(Alias alias, Comparable key) throws java.io.IOException;

}

