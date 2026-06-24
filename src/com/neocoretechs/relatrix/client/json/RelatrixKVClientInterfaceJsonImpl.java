// auto generated from com.neocoretechs.relatrix.server.GenerateClientBindings Tue Jun 23 17:16:04 PDT 2026
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.server.ServerMethod;
import com.neocoretechs.rocksack.Alias;

public abstract class RelatrixKVClientInterfaceJsonImpl implements RelatrixKVClientInterfaceJson{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public String[][] getAliases() {
		RelatrixStatementJson s = new RelatrixStatementJson("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("nearest", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("storekv", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMapStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMapKV", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMapKVStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMapStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMap", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMapKV", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMap", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findHeadMapKVStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMapStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("entrySetStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("keySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("keySetStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Class arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("containsValue", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("keySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("close", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("close", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("entrySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("contains", arg1);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("store", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("get", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(DBKey arg1) throws IOException
	{
		RelatrixStatementJson s = new RelatrixStatementJson("getByIndex", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1, DBKey arg2) throws IOException
	{
		RelatrixStatementJson s = new RelatrixStatementJson("getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson("remove", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

