package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import java.util.stream.Stream;
import java.util.Iterator;
import com.neocoretechs.relatrix.DomainMapRange;


public interface RelatrixClientTransactionInterface{

	public Stream entrySetStream(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public DatabaseCatalog getByPath(String arg1,boolean arg2);

	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException;

	public String getAliasToPath(String arg1);

	public String getDatabasePath(DatabaseCatalog arg1);

	public void loadClassFromJar(String arg1) throws java.io.IOException;

	public DatabaseCatalog getByAlias(String arg1) throws java.util.NoSuchElementException;

	public Stream findStream(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void checkpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void checkpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator findTailSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object... arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findTailSet(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadStream(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object... arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object... arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findSubStream(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailStream(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailStream(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object... arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue(String arg1,Class arg2) throws java.io.IOException;

	public Object lastValue(String arg1) throws java.io.IOException;

	public Object lastValue(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object lastValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException;

	public Iterator findHeadSet(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findHeadSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object... arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5,Object... arg6) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(String arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Object removekv(String arg1,Comparable arg2) throws java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException;

	public Object removekv(String arg1,String arg2,Comparable arg3) throws java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException;

	public Iterator findSet(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSet(String arg1,String arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public String[][] getAliases();

	public void removeAlias(String arg1) throws java.util.NoSuchElementException;

	public void setAlias(String arg1,String arg2) throws java.io.IOException;

	public String getTableSpace();

	public void setWildcard(char arg1);

	public void setTuple(char arg1);

	public String getAlias(String arg1);

	public void setTablespace(String arg1) throws java.io.IOException;

	public void rollback(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public void rollback(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void endTransaction(String arg1) throws java.lang.IllegalAccessException,java.io.IOException,java.lang.ClassNotFoundException;

	public String getTransactionId() throws java.lang.IllegalAccessException,java.io.IOException;

	public void rollbackToCheckpoint(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public void rollbackToCheckpoint(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object getByIndex(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,java.util.NoSuchElementException;

	public Object getByIndex(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

	public void commit(String arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	public void commit(String arg1,String arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(String arg1) throws java.io.IOException;

	public Object firstValue(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstValue(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator keySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;
	
	public Iterator entrySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(String arg1,String arg2,Class arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object first(String arg1) throws java.io.IOException;

	public Object first(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException;

	public Object first(String arg1,Class arg2) throws java.io.IOException;

	public Object first(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(String arg1,Class arg2) throws java.io.IOException;

	public Object last(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(String arg1) throws java.io.IOException;

	public boolean contains(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.util.NoSuchElementException;

	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException;

	public long size(String arg1,String arg2) throws java.io.IOException,java.util.NoSuchElementException;

	public long size(String arg1, Class arg2) throws java.io.IOException;
	
	public long size(String arg1,String arg2,Class arg3) throws java.io.IOException,java.util.NoSuchElementException;

	public long size(String arg1) throws java.io.IOException;

	public void store(String arg1,Comparable arg2,Object arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException;

	public DomainMapRange store(String arg1,String arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	public DomainMapRange store(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	public Object get(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public void remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void remove(String arg1,String arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void remove(String arg1,String arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void remove(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

}

