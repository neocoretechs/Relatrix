package com.neocoretechs.relatrix;

public interface RelatrixClientInterface{

	RelatrixIndex getNewKey() throws java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException;

	Stream findHeadStream(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Stream findSubStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	String getAliasToPath(String arg1);

	Stream findSubStream(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	DatabaseCatalog getByPath(String arg1,boolean arg2);

	DatabaseCatalog getByAlias(String arg1) throws java.util.NoSuchElementException;

	Iterator findSubSet(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	String getDatabasePath(DatabaseCatalog arg1);

	Stream findTailStream(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Void loadClassFromJar(String arg1) throws java.io.IOException;

	Iterator findHeadSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Iterator findSubSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Iterator findHeadSet(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Void loadClassFromPath(String arg1,String arg2) throws java.io.IOException;

	String getTableSpace();

	Void removeAlias(String arg1) throws java.util.NoSuchElementException;

	Void setWildcard(char arg1);

	String getAlias(String arg1);

	Void setTablespace(String arg1) throws java.io.IOException;

	Iterator findTailSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Iterator findTailSet(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Stream findStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Stream findStream(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Iterator findSet(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Iterator findSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Void storekv(Comparable arg1,Object arg2) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException;

	Void storekv(String arg1,Comparable arg2,Object arg3) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException;

	Void setAlias(String arg1,String arg2) throws java.io.IOException;

	Void setTuple(char arg1);

	String[][] getAliases();

	Object lastValue() throws java.io.IOException;

	Object lastValue(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	Object lastValue(Class arg1) throws java.io.IOException;

	Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Stream entrySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	Stream findHeadStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Void removePackageFromRepository(String arg1) throws java.io.IOException;

	Stream findTailStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Object getByIndex(DBKey arg1) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

	Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	Object lastKey(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	Object lastKey() throws java.io.IOException;

	Object lastKey(Class arg1) throws java.io.IOException;

	Object firstKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Object firstKey(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	Object firstKey() throws java.io.IOException;

	Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Object firstValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	Object firstValue(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	Object firstValue() throws java.io.IOException;

	Iterator keySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	List resolve(Comparable arg1);

	Object first(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	Object first() throws java.io.IOException;

	Object first(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	Object first(Class arg1) throws java.io.IOException;

	Object last(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	Object last(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException;

	Object last() throws java.io.IOException;

	Object last(Class arg1) throws java.io.IOException;

	boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.util.NoSuchElementException;

	boolean contains(Comparable arg1) throws java.io.IOException;

	long size() throws java.io.IOException;

	long size(String arg1) throws java.io.IOException,java.util.NoSuchElementException;

	DomainMapRange store(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException,java.lang.ClassNotFoundException;

	DomainMapRange store(Comparable arg1,Comparable arg2,Comparable arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Object get(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException;

	Void remove(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	Void remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	Void remove(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	Void remove(Comparable arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	Void main(String[] arg1) throws java.lang.Exception;

}

