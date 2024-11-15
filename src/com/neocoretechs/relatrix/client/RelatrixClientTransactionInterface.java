package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import java.util.stream.Stream;
import java.io.IOException;
import java.util.Iterator;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DuplicateKeyException;


public interface RelatrixClientTransactionInterface{

	public Stream entrySetStream(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream entrySetStream(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public DatabaseCatalog getByPath(String path, boolean create);

	public void loadClassFromPath(String arg2) throws java.io.IOException;

	public String getAliasToPath(Alias alias);

	public String getDatabasePath(DatabaseCatalog arg1);

	public void loadClassFromJar(String jar) throws java.io.IOException;

	public DatabaseCatalog getByAlias(Alias alias) throws java.util.NoSuchElementException;

	public Stream findStream(TransactionId transactionId,Object darg,Object marg,Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void checkpoint(Alias alias,TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void checkpoint(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator findTailSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator findTailSet(TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endrarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadStream(TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endrarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findHeadStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Stream findSubStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findSubStream(TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endrarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailStream(TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endrarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Stream findTailStream(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object lastValue(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object lastValue(TransactionId transactionId) throws java.io.IOException;

	public Object lastValue(Alias alias, TransactionId transactionId) throws java.io.IOException,java.util.NoSuchElementException;

	public Object lastValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Iterator findHeadSet(TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endrarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findHeadSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSubSet(TransactionId transactionId, Object darg, Object marg, Object rarg, Object... endrarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Object removekv(TransactionId transactionId, Comparable arg2) throws java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException;

	public Object removekv(Alias alias, TransactionId transactionId, Comparable arg3) throws java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException,java.util.NoSuchElementException;

	public Iterator findSet(TransactionId transactionId, Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public Iterator findSet(Alias alias, TransactionId transactionId, Object darg, Object marg, Object rarg) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public String[][] getAliases();

	public void removeAlias(Alias alias) throws java.util.NoSuchElementException;

	public void setAlias(Alias alias, String path) throws java.io.IOException;

	public String getTableSpace();

	public void setWildcard(char wc);

	public void setTuple(char tc);

	public String getAlias(Alias alias);

	public void setTablespace(String path) throws java.io.IOException;

	public void rollback(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public void rollback(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public void endTransaction(TransactionId transactionId) throws java.lang.IllegalAccessException,java.io.IOException,java.lang.ClassNotFoundException;

	public TransactionId getTransactionId() throws java.lang.IllegalAccessException,java.io.IOException;

	public void rollbackToCheckpoint(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public void rollbackToCheckpoint(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object getByIndex(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,java.util.NoSuchElementException;

	public Object getByIndex(TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

	public void commit(TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException;

	public void commit(Alias alias, TransactionId transactionId) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object firstValue(TransactionId transactionId) throws java.io.IOException;

	public Object firstValue(Alias alias, TransactionId transactionId) throws java.io.IOException,java.util.NoSuchElementException;

	public Object firstValue(TransactionId transactionId,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Object firstValue(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Iterator keySet(TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator keySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;
	
	public Iterator entrySet(TransactionId transactionId,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException;

	public Iterator entrySet(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object first(TransactionId transactionId) throws java.io.IOException;

	public Object first(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Object first(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object first(Alias alias, TransactionId transactionId) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(TransactionId transactionId, Class clazz) throws java.io.IOException;

	public Object last(Alias alias, TransactionId transactionId) throws java.io.IOException,java.util.NoSuchElementException;

	public Object last(TransactionId transactionId) throws java.io.IOException;

	public boolean contains(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.util.NoSuchElementException;

	public boolean contains(TransactionId transactionId, Comparable key) throws java.io.IOException;

	public long size(Alias alias, TransactionId transactionId) throws java.io.IOException,java.util.NoSuchElementException;

	public long size(TransactionId transactionId, Class clazz) throws java.io.IOException;
	
	public long size(Alias alias, TransactionId transactionId, Class clazz) throws java.io.IOException,java.util.NoSuchElementException;

	public long size(TransactionId transactionId) throws java.io.IOException;

	public DomainMapRange store(TransactionId transactionId, Comparable key, Object value) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException;

	public DomainMapRange store(Alias alias, TransactionId transactionId, Comparable darg, Comparable marg, Comparable rarg) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	public DomainMapRange store(TransactionId transactionId, Comparable darg, Comparable marg, Comparable rarg) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException;

	public Object get(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException;

	public Object get(TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalAccessException;

	public void remove(TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException;

	public void remove(Alias alias, TransactionId transactionId, Comparable key) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void remove(Alias alias, TransactionId transactionId, Comparable darg, Comparable marg) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException;

	public void remove(TransactionId transactionId, Comparable darg, Comparable marg) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException;

	public DomainMapRange store(Alias alias, TransactionId transactionId, Comparable key, Object value) throws IllegalAccessException, IOException, DuplicateKeyException;

}

