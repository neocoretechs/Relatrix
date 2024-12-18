package com.neocoretechs.relatrix.client;

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

	public void loadClassFromJar(String jar) throws java.io.IOException;

	public String getAliasToPath(Alias alias);

	public RelatrixIndex getNewKey() throws java.io.IOException;

	public Iterator findHeadSet(Alias alias,Object darg,Object marg,Object rarg,Object... endarg) throws java.io.IOException;

	public String[][] getAliases();

	public Iterator findHeadSet(Object darg, Object marg, Object rarg, Object... arg4) throws java.io.IOException;

	public Stream findStream(Alias alias, Object darg, Object marg, Object rarg) throws java.io.IOException;

	public Stream findStream(Object darg, Object marg, Object rarg) throws java.io.IOException;

	public Iterator findTailSet(Alias alias,Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public Stream findTailStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public Iterator findSubSet(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public Iterator findTailSet(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public void removeAlias(Alias alias) throws java.util.NoSuchElementException;

	public Iterator findSet(Object darg, Object marg, Object rarg) throws java.io.IOException;

	public Iterator findSet(Alias alias, Object darg, Object marg, Object rarg) throws java.io.IOException;

	public Stream findSubStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public void storekv(Comparable key, Object value) throws java.io.IOException;

	public void storekv(Alias alias, Comparable key, Object value) throws java.io.IOException;

	public Stream findSubStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public Iterator findSubSet(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public Stream findHeadStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public String getTableSpace();

	public void setTuple(char tp);

	public void setTablespace(String path) throws java.io.IOException;

	public String getAlias(Alias alias);

	public void setWildcard(char wc);

	public void setAlias(Alias alias, String path) throws java.io.IOException;

	public Stream entrySetStream(Class clazz) throws java.io.IOException;

	public Iterator entrySet(Alias alias, Class clazz) throws java.io.IOException;
	
	public Iterator entrySet(Class clazz) throws java.io.IOException;

	public Stream entrySetStream(Alias alias, Class clazz) throws java.io.IOException;

	public Object lastValue() throws java.io.IOException;

	public Object lastValue(Alias alias, Class clazz) throws java.io.IOException;

	public Object lastValue(Class clazz) throws java.io.IOException;

	public Object lastValue(Alias alias) throws java.io.IOException;

	public Stream findHeadStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public void removePackageFromRepository(String pack) throws java.io.IOException;

	public Stream findTailStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException;

	public Object getByIndex(DBKey key) throws java.io.IOException;
	
	public Object getByIndex(Alias alias, DBKey index) throws java.io.IOException;

	public Object lastKey() throws java.io.IOException;

	public Object lastKey(Alias alias) throws java.io.IOException;

	public Object lastKey(Class clazz) throws java.io.IOException;

	public Object lastKey(Alias alias, Class clazz) throws java.io.IOException;

	public Object firstKey() throws java.io.IOException;

	public Object firstKey(Alias alias, Class clazz) throws java.io.IOException;

	public Object firstKey(Class clazz) throws java.io.IOException;

	public Object firstKey(Alias alias) throws java.io.IOException;

	public Object firstValue(Class clazz) throws java.io.IOException;

	public Object firstValue() throws java.io.IOException;

	public Object firstValue(Alias alias, Class clazz) throws java.io.IOException;

	public Object firstValue(Alias alias) throws java.io.IOException;

	public Iterator keySet(Alias alias, Class clazz) throws java.io.IOException;

	public Iterator keySet(Class clazz) throws java.io.IOException;

	public List resolve(Comparable morphism);

	public Object first(Class clazz) throws java.io.IOException;

	public Object first(Alias alias) throws java.io.IOException;

	public Object first() throws java.io.IOException;

	public Object first(Alias alias, Class clazz) throws java.io.IOException;

	public Object last(Alias alias) throws java.io.IOException;

	public Object last(Alias alias, Class clazz) throws java.io.IOException;

	public Object last() throws java.io.IOException;

	public Object last(Class clazz) throws java.io.IOException;

	public boolean contains(Alias alias, Comparable obj) throws java.io.IOException;

	public boolean contains(Comparable obj) throws java.io.IOException;

	public long size() throws java.io.IOException;

	public long size(Alias alias) throws java.io.IOException;
	
	public long size(Class clazz) throws java.io.IOException;

	public long size(Alias alias, Class clazz) throws java.io.IOException;

	public DomainMapRange store(Alias alias, Comparable darg, Comparable marg, Comparable rarg) throws java.io.IOException;

	public DomainMapRange store(Comparable darg, Comparable marg, Comparable rarg) throws java.io.IOException;

	public Object get(Alias alias, Comparable key) throws java.io.IOException;

	public Object get(Comparable key) throws java.io.IOException;

	public void remove(Alias alias, Comparable darg, Comparable marg) throws java.io.IOException;

	public void remove(Alias alias, Comparable arg2) throws java.io.IOException;

	public void remove(Comparable key) throws java.io.IOException;

	public void remove(Comparable darg, Comparable marg) throws java.io.IOException;

	Object removekv(Comparable<?> key) throws IOException;

	Object removekv(Alias alias, Comparable key) throws IOException;


}

