// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.relatrix.RelatrixKV;


public abstract class RelatrixKVClientInterfaceImpl implements RelatrixKVClientInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
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
	public String[][] getAliases() {
		RelatrixStatement s = new RelatrixStatement("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findTailMapKV(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKV", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMapKV(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapStream", arg1);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapStream(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
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
	public Object lastValue(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySetStream", arg1);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream keySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySetStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
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
	public Iterator findSubMapKV(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKV", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMapKV(Comparable arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
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
	public Stream findSubMapStream(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapStream(Comparable arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Comparable arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubMap(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMap", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKV", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMapKV(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKV", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapStream(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapStream", arg1);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailMap(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMap", arg1);
		try {
			return (Iterator)sendCommand(s);
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
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromJar", arg1);
		try {
			sendCommand(s);
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
	public String getAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object nearest(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("nearest", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object nearest(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("nearest", arg1);
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
	public Stream findSubMapKVStream(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKVStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubMapKVStream(Comparable arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMap", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadMap(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMap", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
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
	public Stream findTailMapKVStream(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKVStream", arg1);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailMapKVStream(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailMapKVStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKVStream", arg1);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadMapKVStream(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadMapKVStream", arg1, arg2);
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
	public Object firstKey(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
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
	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
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
	public BufferedMap getMap(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getMap", arg1);
		try {
			return (BufferedMap)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public BufferedMap getMap(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getMap", arg1, arg2);
		try {
			return (BufferedMap)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public BufferedMap getMap(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getMap", arg1, arg2);
		try {
			return (BufferedMap)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public BufferedMap getMap(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getMap", arg1);
		try {
			return (BufferedMap)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("close", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void close(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("close", arg1, arg2);
		try {
			sendCommand(s);
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
	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Alias arg1,Class arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("containsValue", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean containsValue(Class arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("containsValue", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Comparable arg1,Object arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void store(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelatrixKV getInstance() {
		RelatrixStatement s = new RelatrixStatement("getInstance",new Object[]{});
		try {
			return (RelatrixKV)sendCommand(s);
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
	public long size(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
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
	public Object remove(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

