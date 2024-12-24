// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Relatrix;


public interface RelatrixClientInterface{

	public Stream findTailStream(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException;

	public Stream findTailStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException;

	public void setTuple(char arg1);

	public void loadClassFromJar(String arg1) throws java.io.IOException;

	public String getTableSpace();

	public Object removekv(Comparable arg1) throws java.io.IOException;

	public Object removekv(Alias arg1,Comparable arg2) throws java.io.IOException;

	public String getAlias(Alias arg1);

	public Stream findHeadStream(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException;

	public Stream findHeadStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException;

	public void setAlias(Alias arg1,String arg2) throws java.io.IOException;

	public void setTablespace(String arg1) throws java.io.IOException;

	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException;

	public Object lastValue() throws java.io.IOException;

	public Object lastValue(Alias arg1) throws java.io.IOException;

	public Object lastValue(Class arg1) throws java.io.IOException;

	public void removeAlias(Alias arg1) throws java.io.IOException;

	public DBKey getNewKey() throws java.io.IOException;

	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException;

	public Iterator findSubSet(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException;

	public Iterator findSubSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException;

	public Iterator findSet(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public Iterator findSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException;

	public Stream findSubStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException;

	public Stream findSubStream(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException;

	public Object lastKey() throws java.io.IOException;

	public Object lastKey(Alias arg1) throws java.io.IOException;

	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException;

	public Object lastKey(Class arg1) throws java.io.IOException;

	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException;

	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException;

	public Object getByIndex(DBKey arg1) throws java.io.IOException;
	
	public Object getByIndex(Alias alias, DBKey index) throws java.io.IOException;

	public Iterator findTailSet(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException;

	public Iterator findTailSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException;

	public void setWildcard(char arg1);

	public Stream findStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException;

	public Stream findStream(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException;

	public String[][] getAliases();

	public Iterator findHeadSet(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException;

	public Iterator findHeadSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException;

	public Stream entrySetStream(Class arg1) throws java.io.IOException;

	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException;

	public void removePackageFromRepository(String arg1) throws java.io.IOException;

	public Object firstKey(Alias arg1) throws java.io.IOException;

	public Object firstKey() throws java.io.IOException;

	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException;

	public Object firstKey(Class arg1) throws java.io.IOException;

	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException;

	public Object firstValue() throws java.io.IOException;

	public Object firstValue(Class arg1) throws java.io.IOException;

	public Object firstValue(Alias arg1) throws java.io.IOException;

	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException;

	public Iterator keySet(Class arg1) throws java.io.IOException;

	public DomainMapRange store(Comparable arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public DomainMapRange store(Alias arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException;

	public List resolve(Comparable arg1);

	public Object first() throws java.io.IOException;

	public Object first(Alias arg1) throws java.io.IOException;

	public Object first(Class arg1) throws java.io.IOException;

	public Object first(Alias arg1,Class arg2) throws java.io.IOException;

	public Relatrix getInstance();

	public Iterator entrySet(Class arg1) throws java.io.IOException;

	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException;

	public long size(Class arg1) throws java.io.IOException;

	public long size() throws java.io.IOException;

	public long size(Alias arg1) throws java.io.IOException;

	public long size(Alias arg1,Class arg2) throws java.io.IOException;

	public Object last() throws java.io.IOException;

	public Object last(Alias arg1) throws java.io.IOException;

	public Object last(Alias arg1,Class arg2) throws java.io.IOException;

	public Object last(Class arg1) throws java.io.IOException;

	public boolean contains(Alias arg1,Comparable arg2) throws java.io.IOException;

	public boolean contains(Comparable arg1) throws java.io.IOException;

	public Object get(Alias arg1,Comparable arg2) throws java.io.IOException;

	public Object get(Comparable arg1) throws java.io.IOException;

	public void remove(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException;

	public void remove(Comparable arg1,Comparable arg2) throws java.io.IOException;

	public void remove(Alias arg1,Comparable arg2) throws java.io.IOException;

	public void remove(Comparable arg1) throws java.io.IOException;


}

