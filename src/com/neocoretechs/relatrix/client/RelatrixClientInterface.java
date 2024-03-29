package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.RelatrixIndex;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.relatrix.DomainMapRange;


public interface RelatrixClientInterface{

	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException;

	public DatabaseCatalog getByAlias(String arg1) throws java.util.NoSuchElementException;

	public void loadClassFromJar(String arg1) throws java.io.IOException;

	public DatabaseCatalog getByPath(String arg1,boolean arg2);

	public String getAliasToPath(String arg1);

	public String getDatabasePath(DatabaseCatalog arg1);

	public RelatrixIndex getNewKey() throws java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException;

	public Iterator findHeadSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public String[][] getAliases();

	public Iterator findHeadSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findStream(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void removeAlias(String arg1) throws java.util.NoSuchElementException;

	public Iterator findSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSet(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void storekv(String arg1,Comparable arg2,Object arg3) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException;

	public Stream findSubStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public String getTableSpace();

	public void setTuple(char arg1);

	public void setTablespace(String arg1) throws java.io.IOException;

	public String getAlias(String arg1);

	public void setWildcard(char arg1);

	public void setAlias(String arg1,String arg2) throws java.io.IOException;

	public Stream entrySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue() throws java.io.IOException;

	public Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object lastValue(Class arg1) throws java.io.IOException;

	public Object lastValue(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public Stream findHeadStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void removePackageFromRepository(String arg1) throws java.io.IOException;

	public Stream findTailStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object getByIndex(DBKey arg1) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

	public Object lastKey() throws java.io.IOException;

	public Object lastKey(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public Object lastKey(Class arg1) throws java.io.IOException;

	public Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object firstKey() throws java.io.IOException;

	public Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstKey(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public Object firstValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstValue() throws java.io.IOException;

	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public List resolve(Comparable arg1);

	public Object first(Class arg1) throws java.io.IOException;

	public Object first(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public Object first() throws java.io.IOException;

	public Object first(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last() throws java.io.IOException;

	public Object last(Class arg1) throws java.io.IOException;

	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public boolean contains(Comparable arg1) throws java.io.IOException;

	public long size() throws java.io.IOException;

	public long size(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	public DomainMapRange store(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException,java.lang.ClassNotFoundException;

	public DomainMapRange store(Comparable arg1,Comparable arg2,Comparable arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public void remove(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void remove(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void remove(Comparable arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	Object removekv(Comparable<?> arg1) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException;

	Object removekv(String arg1, Comparable arg2) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, java.util.NoSuchElementException;

}

