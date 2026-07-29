// auto generated from com.neocoretechs.relatrix.server.GenerateClientBindings Tue Jun 23 17:16:04 PDT 2026
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;

public abstract class RelatrixKVClientInterfaceJsonImpl implements RelatrixKVClientInterfaceJson{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public String[][] getAliases() {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null,"getAliases", new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "nearest", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "storekv", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMapStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMapKV", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMapKVStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMapStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMap", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMapKV", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMap", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findHeadMapKVStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "entrySetStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "keySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "keySetStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Class arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "containsValue", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "keySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "close", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "entrySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "contains", arg1);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "store", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "get", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(DBKey arg1) throws IOException
	{
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "getByIndex", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1, DBKey arg2) throws IOException
	{
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Class arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Object arg1) throws java.io.IOException {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson(null, "remove", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

