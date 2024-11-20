package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.RelatrixIndex;
import com.neocoretechs.rocksack.Alias;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.relatrix.DomainMapRange;


public interface RelatrixClientInterface {

	public void loadClassFromPath(String clazz,String pack) throws java.io.IOException;

	public DatabaseCatalog getByAlias(Alias alias) throws java.util.NoSuchElementException;

	public void loadClassFromJar(String jar) throws java.io.IOException;

	public DatabaseCatalog getByPath(String path,boolean create);

	public String getAliasToPath(Alias alias);

	public String getDatabasePath(DatabaseCatalog index);

	public RelatrixIndex getNewKey() throws java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException;

	public Iterator findHeadSet(Alias alias,Object darg,Object marg,Object rarg,Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public String[][] getAliases();

	public Iterator findHeadSet(Object darg, Object marg, Object rarg, Object... arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findStream(Alias alias, Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findStream(Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailSet(Alias alias,Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findTailStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findTailSet(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void removeAlias(Alias alias) throws java.util.NoSuchElementException;

	public Iterator findSet(Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSet(Alias alias, Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void storekv(Comparable key, Object value) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void storekv(Alias alias, Comparable key, Object value) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException;

	public Stream findSubStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findHeadStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public String getTableSpace();

	public void setTuple(char tp);

	public void setTablespace(String path) throws java.io.IOException;

	public String getAlias(Alias alias);

	public void setWildcard(char wc);

	public void setAlias(Alias alias, String path) throws java.io.IOException;

	public Stream entrySetStream(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;
	
	public Iterator entrySet(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Stream entrySetStream(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue() throws java.io.IOException;

	public Object lastValue(Alias alias, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Object lastValue(Class clazz) throws java.io.IOException;

	public Object lastValue(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;

	public Stream findHeadStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void removePackageFromRepository(String pack) throws java.io.IOException;

	public Stream findTailStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object getByIndex(DBKey key) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

	public Object lastKey() throws java.io.IOException;

	public Object lastKey(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;

	public Object lastKey(Class clazz) throws java.io.IOException;

	public Object lastKey(Alias alias, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Object firstKey() throws java.io.IOException;

	public Object firstKey(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstKey(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstKey(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;

	public Object firstValue(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstValue() throws java.io.IOException;

	public Object firstValue(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;

	public Iterator keySet(Alias alias, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public List resolve(Comparable morphism);

	public Object first(Class clazz) throws java.io.IOException;

	public Object first(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;

	public Object first() throws java.io.IOException;

	public Object first(Alias alias, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(Alias alias, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last() throws java.io.IOException;

	public Object last(Class clazz) throws java.io.IOException;

	public boolean contains(Alias alias, Comparable obj) throws java.io.IOException,java.util.NoSuchElementException;

	public boolean contains(Comparable obj) throws java.io.IOException;

	public long size() throws java.io.IOException;

	public long size(Alias alias) throws java.io.IOException,java.util.NoSuchElementException;
	
	public long size(Class clazz) throws java.io.IOException;

	public long size(Alias alias, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public DomainMapRange store(Alias alias, Comparable darg, Comparable marg, Comparable rarg) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException,java.lang.ClassNotFoundException;

	public DomainMapRange store(Comparable darg, Comparable marg, Comparable rarg) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	public Object get(Alias alias, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public void remove(Alias alias, Comparable darg, Comparable marg) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void remove(Alias alias, Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void remove(Comparable key) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void remove(Comparable darg, Comparable marg) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	Object removekv(Comparable<?> key) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException;

	Object removekv(Alias alias, Comparable key) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, IOException, java.util.NoSuchElementException;

}

