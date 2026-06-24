// auto generated from com.neocoretechs.relatrix.server.GenerateClientBindings Tue Jun 23 17:03:01 PDT 2026
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;


public interface RelatrixKVClientInterfaceJson extends ClientNonTransactionInterface{

	public void removeAlias(Alias arg1) throws java.io.IOException;

	public Stream entrySetStream(Class arg1) throws java.io.IOException;

	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator findTailMapKV(Alias arg1,Object arg2) throws java.io.IOException;

	public Iterator findTailMapKV(Object arg1) throws java.io.IOException;

	public Stream findTailMapStream(Object arg1) throws java.io.IOException;

	public Stream findTailMapStream(Alias arg1,Object arg2) throws java.io.IOException;

	public Stream findHeadMapKVStream(Object arg1) throws java.io.IOException;

	public Stream findHeadMapKVStream(Alias arg1,Object arg2) throws java.io.IOException;

	public Stream findHeadMapStream(Object arg1) throws java.io.IOException;

	public Stream findHeadMapStream(Alias arg1,Object arg2) throws java.io.IOException;

	public Iterator findSubMapKV(Object arg1,Object arg2) throws java.io.IOException;

	public Iterator findSubMapKV(Alias arg1,Object arg2,Object arg3) throws java.io.IOException;

	public void setRelativeAlias(Alias arg1) throws java.io.IOException;

	public Stream findSubMapKVStream(Object arg1,Object arg2) throws java.io.IOException;

	public Stream findSubMapKVStream(Alias arg1,Object arg2,Object arg3) throws java.io.IOException;

	public Stream keySetStream(Alias arg1,Class arg2) throws java.io.IOException;

	public Stream keySetStream(Class arg1) throws java.io.IOException;

	public Iterator findHeadMapKV(Alias arg1,Object arg2) throws java.io.IOException;

	public Iterator findHeadMapKV(Object arg1) throws java.io.IOException;

	public Iterator findHeadMap(Alias arg1,Object arg2) throws java.io.IOException;

	public Iterator findHeadMap(Object arg1) throws java.io.IOException;

	public Iterator findTailMap(Object arg1) throws java.io.IOException;

	public Iterator findTailMap(Alias arg1,Object arg2) throws java.io.IOException;

	public Stream findTailMapKVStream(Object arg1) throws java.io.IOException;

	public Stream findTailMapKVStream(Alias arg1,Object arg2) throws java.io.IOException;

	public Stream findSubMapStream(Object arg1,Object arg2) throws java.io.IOException;

	public Stream findSubMapStream(Alias arg1,Object arg2,Object arg3) throws java.io.IOException;

	public String[][] getAliases();

	public String getAlias(Alias arg1);

	public Object lastValue(Class arg1) throws java.io.IOException;

	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator findSubMap(Alias arg1,Object arg2,Object arg3) throws java.io.IOException;

	public Iterator findSubMap(Object arg1,Object arg2) throws java.io.IOException;

	public Object nearest(Alias arg1,Object arg2) throws java.io.IOException;

	public Object nearest(Object arg1) throws java.io.IOException;

	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException;

	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException;

	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(Class arg1) throws java.io.IOException;

	public Object firstKey(Class arg1) throws java.io.IOException;

	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException;

	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException;

	public Object firstValue(Class arg1) throws java.io.IOException;

	public boolean containsValue(Class arg1,Object arg2) throws java.io.IOException;

	public boolean containsValue(Alias arg1,Class arg2,Comparable arg3) throws java.io.IOException;

	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator keySet(Class arg1) throws java.io.IOException;

	public void close(Class arg1) throws java.io.IOException;

	public void close(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator entrySet(Class arg1) throws java.io.IOException;

	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException;

	public boolean contains(Object arg1) throws java.io.IOException;

	public boolean contains(Alias arg1,Object arg2) throws java.io.IOException;

	public void store(Object arg1,Object arg2) throws java.io.IOException;

	public void store(Alias arg1,Object arg2,Object arg3) throws java.io.IOException;

	public Object get(Object arg1) throws java.io.IOException;

	public Object get(Alias arg1,Object arg2) throws java.io.IOException;
	
	public Object getByIndex(DBKey arg1) throws java.io.IOException;

	public Object getByIndex(Alias arg1,DBKey arg2) throws java.io.IOException;

	public long size(Alias arg1,Class arg2) throws java.io.IOException;

	public long size(Class arg1) throws java.io.IOException;

	public Object remove(Alias arg1,Object arg2) throws java.io.IOException;

	public Object remove(Object arg1) throws java.io.IOException;

}

