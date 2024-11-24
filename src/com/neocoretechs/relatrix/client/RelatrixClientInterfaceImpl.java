package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.RelatrixIndex;
import com.neocoretechs.rocksack.Alias;

import java.util.stream.Stream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import com.neocoretechs.relatrix.DomainMapRange;


public abstract class RelatrixClientInterfaceImpl implements RelatrixClientInterface {

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public DatabaseCatalog getByAlias(Alias alias) throws java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("getByAlias", alias);
		try {
			return (DatabaseCatalog)sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public RelatrixIndex getNewKey() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getNewKey",new Object[]{});
		try {
			return (RelatrixIndex)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", alias, darg, marg, rarg, endarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAliasToPath(Alias alias) {
		RelatrixStatement s = new RelatrixStatement("getAliasToPath", alias);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", darg, marg, rarg, endarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", darg, marg, rarg, endarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public DatabaseCatalog getByPath(String path, boolean create) {
		RelatrixStatement s = new RelatrixStatement("getByPath", path, create);
		try {
			return (DatabaseCatalog)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findTailSet(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSetAlias", alias, darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public String getDatabasePath(DatabaseCatalog darg) {
		RelatrixStatement s = new RelatrixStatement("getDatabasePath", darg);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromPath", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSetAlias", alias, darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSetAlias", alias, darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void loadClassFromJar(String jar) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromJar", jar);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Object darg, Object marg ,Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", darg, marg, rarg, endarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", darg, marg, rarg, endarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias alias) throws java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("removeAlias", alias);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixStatement s = new RelatrixStatement("getTableSpace",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findStream(Object darg, Object marg, Object rarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", darg, marg, rarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias alias, Object darg, Object marg, Object rarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", alias, darg, marg, rarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void setWildcard(char wc) {
		RelatrixStatement s = new RelatrixStatement("setWildcard", wc);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Iterator findSet(Object darg, Object marg, Object rarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", darg, marg, rarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias alias, Object darg, Object marg, Object rarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", alias, darg, marg, rarg);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias alias,String path) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setAlias", alias, path);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTablespace(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setTablespace", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTuple(char tc) {
		RelatrixStatement s = new RelatrixStatement("setTuple", tc);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void storekv(Comparable key, Object value) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("storekv", key, value);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias alias, Comparable key, Object value) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("storekv", alias, key, value);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixStatement s = new RelatrixStatement("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getAlias(Alias alias) {
		RelatrixStatement s = new RelatrixStatement("getAlias", alias);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object lastValue() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue",new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", alias);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", alias, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removePackageFromRepository(String pack) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removePackageFromRepository", pack);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStreamAlias", alias, darg, marg, rarg, endarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias alias, Object darg, Object marg, Object rarg, Object... endarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStreamAlias", alias, darg, marg, rarg, endarg);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", clazz);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", alias, clazz);
		try {
			RemoteIterator it = (RemoteIterator) sendCommand(s);
			return (new RemoteStream(it));
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(DBKey arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getByIndex", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey",new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", alias);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", alias, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey",new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", alias, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", alias);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue",new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", alias, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", alias);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias alias,Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", alias, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", alias, clazz);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public List resolve(Comparable morphism) {
		RelatrixStatement s = new RelatrixStatement("resolve", morphism);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object first(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", alias);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first",new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", alias, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", alias);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", alias, clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last",new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", clazz);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias alias, Comparable clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", alias, clazz);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Comparable key) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", key);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size",new Object[]{});
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias alias) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", alias);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size",clazz);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias alias, Class clazz) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", alias, clazz);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(Alias alias, Comparable darg, Comparable marg, Comparable rarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", alias, darg, marg, rarg);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(Comparable darg, Comparable marg, Comparable rarg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", darg, marg, rarg);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias alias, Comparable key) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", alias, key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Comparable key) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", key);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias alias, Comparable darg, Comparable marg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", alias, darg, marg);
		try {
			sendCommand(s);
		} catch(Exception e) {
			if(e instanceof java.io.IOException)
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias alias ,Comparable key) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", alias, key);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Comparable key) throws IOException {
		RelatrixStatement s = new RelatrixStatement("removekv", key);
		try {
			return sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias alias, Comparable key) throws IOException {
		RelatrixStatement s = new RelatrixStatement("removekv", alias, key);
		try {
			return sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Comparable key) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", key);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Comparable darg, Comparable marg) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", darg, marg);
		try {
			sendCommand(s);
		} catch(Exception e) {
				throw new java.io.IOException(e);
		}
	}
}

