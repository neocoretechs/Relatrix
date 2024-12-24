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


public abstract class RelatrixClientInterfaceImpl implements RelatrixClientInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public Stream findTailStream(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTuple(char arg1) {
		RelatrixStatement s = new RelatrixStatement("setTuple", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromJar", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
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
	public Object removekv(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removekv", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removekv", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias arg1,String arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setAlias", arg1, arg2);
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
	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
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
	public Object lastValue(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DBKey getNewKey() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getNewKey",new Object[]{});
		try {
			return (DBKey)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
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
	public Iterator findSubSet(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
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
	public Object lastKey(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
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
	public Object getByIndex(Alias alias, DBKey arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getByIndex", alias, arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setWildcard(char arg1) {
		RelatrixStatement s = new RelatrixStatement("setWildcard", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream findStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
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
	public Iterator findHeadSet(Alias arg1,Object arg2,Object arg3,Object arg4,Object... arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Object arg1,Object arg2,Object arg3,Object... arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removePackageFromRepository(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removePackageFromRepository", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
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
	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
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
	public Object firstValue(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(Comparable arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(Alias arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3, arg4);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List resolve(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("resolve", arg1);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
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
	public Object first(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relatrix getInstance() {
		RelatrixStatement s = new RelatrixStatement("getInstance",new Object[]{});
		try {
			return (Relatrix)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator entrySet(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1);
		try {
			return (long)sendCommand(s);
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
	public long size(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
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
	public Object last(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Comparable arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

