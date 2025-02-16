// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings Sun Feb 16 04:20:10 PST 2025
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.Alias;


public interface RelatrixKVClientInterface{

	public Stream findHeadMapKVStream(Comparable arg1) throws java.io.IOException;

	public Stream findHeadMapKVStream(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Stream findTailMapKVStream(Comparable arg1) throws java.io.IOException;

	public Stream findTailMapKVStream(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Stream findHeadMapStream(Comparable arg1) throws java.io.IOException;

	public Stream findHeadMapStream(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findHeadMap(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findHeadMap(Comparable arg1) throws java.io.IOException;

	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException;

	public Object lastValue(Class arg1) throws java.io.IOException;

	public Stream findTailMapStream(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Stream findTailMapStream(Comparable arg1) throws java.io.IOException;

	public Stream keySetStream(Alias arg1,Class arg2) throws java.io.IOException;

	public Stream keySetStream(Class arg1) throws java.io.IOException;

	public Iterator findSubMapKV(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findSubMapKV(Comparable arg1,Comparable arg2) throws java.io.IOException;

	public Stream findSubMapStream(Comparable arg1,Comparable arg2) throws java.io.IOException;

	public Stream findSubMapStream(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findHeadMapKV(Comparable arg1) throws java.io.IOException;

	public Iterator findHeadMapKV(Alias arg1,Comparable arg2) throws java.io.IOException;

	public String[][] getAliases();

	public void removeAlias(Alias arg1) throws java.io.IOException;

	public void setRelativeAlias(Alias arg1) throws java.io.IOException;

	public Object nearest(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Object nearest(Comparable arg1) throws java.io.IOException;

	public Iterator findTailMapKV(Comparable arg1) throws java.io.IOException;

	public Iterator findTailMapKV(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Stream findSubMapKVStream(Comparable arg1,Comparable arg2) throws java.io.IOException;

	public Stream findSubMapKVStream(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findSubMap(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public Iterator findSubMap(Comparable arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findTailMap(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Iterator findTailMap(Comparable arg1) throws java.io.IOException;

	public String getAlias(Alias arg1);

	public Stream entrySetStream(Class arg1) throws java.io.IOException;

	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException;

	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException;

	public Object firstKey(Class arg1) throws java.io.IOException;

	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(Class arg1) throws java.io.IOException;

	public Object firstValue(Class arg1) throws java.io.IOException;

	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException;

	public void close(Class arg1) throws java.io.IOException;

	public void close(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator keySet(Class arg1) throws java.io.IOException;

	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException;

	public boolean containsValue(Class arg1,Comparable arg2) throws java.io.IOException;

	public boolean containsValue(Alias arg1,Class arg2,Comparable arg3) throws java.io.IOException;

	public void store(Comparable arg1,Object arg2) throws java.io.IOException;

	public void store(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException;

	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator entrySet(Class arg1) throws java.io.IOException;

	public long size(Alias arg1,Class arg2) throws java.io.IOException;

	public long size(Class arg1) throws java.io.IOException;

	public boolean contains(Comparable arg1) throws java.io.IOException;

	public boolean contains(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Object get(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Object get(Comparable arg1) throws java.io.IOException;

	public Object remove(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Object remove(Comparable arg1) throws java.io.IOException;

}

