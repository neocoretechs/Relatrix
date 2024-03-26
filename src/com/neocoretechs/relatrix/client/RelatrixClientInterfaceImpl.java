package com.neocoretechs.relatrix.client;

import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.RelatrixIndex;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.relatrix.DomainMapRange;


public abstract class RelatrixClientInterfaceImpl implements RelatrixClientInterface{

	public abstract Object sendCommand(RelatrixStatement s);
	@Override
	public void loadClassFromPath(String arg1,String arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromPath", arg1, arg2);
		sendCommand(s);
	}
	@Override
	public DatabaseCatalog getByAlias(String arg1) throws java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("getByAlias", arg1);
		return (DatabaseCatalog)sendCommand(s);
	}
	@Override
	public void loadClassFromJar(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("loadClassFromJar", arg1);
		sendCommand(s);
	}
	@Override
	public DatabaseCatalog getByPath(String arg1,boolean arg2) {
		RelatrixStatement s = new RelatrixStatement("getByPath", arg1, arg2);
		return (DatabaseCatalog)sendCommand(s);
	}
	@Override
	public String getAliasToPath(String arg1) {
		RelatrixStatement s = new RelatrixStatement("getAliasToPath", arg1);
		return (String)sendCommand(s);
	}
	@Override
	public String getDatabasePath(DatabaseCatalog arg1) {
		RelatrixStatement s = new RelatrixStatement("getDatabasePath", arg1);
		return (String)sendCommand(s);
	}
	@Override
	public RelatrixIndex getNewKey() throws java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getNewKey",new Object[]{});
		return (RelatrixIndex)sendCommand(s);
	}
	@Override
	public Iterator findHeadSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findHeadSetAlias", arg1, arg2, arg3, arg4, arg5);
		return (Iterator)sendCommand(s);
	}
	@Override
	public String[][] getAliases() {
		RelatrixStatement s = new RelatrixStatement("getAliases",new Object[]{});
		return (String[][])sendCommand(s);
	}
	@Override
	public Iterator findHeadSet(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Stream findStream(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return (Stream)sendCommand(s);
	}
	@Override
	public Stream findStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return (Stream)sendCommand(s);
	}
	@Override
	public Iterator findTailSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findTailSetAlias", arg1, arg2, arg3, arg4, arg5);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Stream findTailStream(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		return (Stream)sendCommand(s);
	}
	@Override
	public Iterator findSubSet(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Iterator findTailSet(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		return (Iterator)sendCommand(s);
	}
	@Override
	public void removeAlias(String arg1) throws java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("removeAlias", arg1);
		sendCommand(s);
	}
	@Override
	public Iterator findSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Iterator findSet(String arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Stream findSubStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findSubStreamAlias", arg1, arg2, arg3, arg4, arg5);
		return (Stream)sendCommand(s);
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2);
		sendCommand(s);
	}
	@Override
	public void storekv(String arg1,Comparable arg2,Object arg3) throws java.io.IOException,java.lang.IllegalAccessException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2, arg3);
		sendCommand(s);
	}
	@Override
	public Stream findSubStream(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		return (Stream)sendCommand(s);
	}
	@Override
	public Iterator findSubSetAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findSubSetAlias", arg1, arg2, arg3, arg4, arg5);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Stream findHeadStream(Object arg1,Object arg2,Object arg3,Object[] arg4) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		return (Stream)sendCommand(s);
	}
	@Override
	public String getTableSpace() {
		RelatrixStatement s = new RelatrixStatement("getTableSpace",new Object[]{});
		return (String)sendCommand(s);
	}
	@Override
	public void setTuple(char arg1) {
		RelatrixStatement s = new RelatrixStatement("setTuple", arg1);
		sendCommand(s);
	}
	@Override
	public void setTablespace(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setTablespace", arg1);
		sendCommand(s);
	}
	@Override
	public String getAlias(String arg1) {
		RelatrixStatement s = new RelatrixStatement("getAlias", arg1);
		return (String)sendCommand(s);
	}
	@Override
	public void setWildcard(char arg1) {
		RelatrixStatement s = new RelatrixStatement("setWildcard", arg1);
		sendCommand(s);
	}
	@Override
	public void setAlias(String arg1,String arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setAlias", arg1, arg2);
		sendCommand(s);
	}
	@Override
	public Stream entrySetStream(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1);
		return (Stream)sendCommand(s);
	}
	@Override
	public Stream entrySetStream(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2);
		return (Stream)sendCommand(s);
	}
	@Override
	public Object lastValue() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue",new Object[]{});
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastValue(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastValue(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastValue(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Stream findHeadStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findHeadStreamAlias", arg1, arg2, arg3, arg4, arg5);
		return (Stream)sendCommand(s);
	}
	@Override
	public void removePackageFromRepository(String arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removePackageFromRepository", arg1);
		sendCommand(s);
	}
	@Override
	public Stream findTailStreamAlias(String arg1,Object arg2,Object arg3,Object arg4,Object[] arg5) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("findTailStreamAlias", arg1, arg2, arg3, arg4, arg5);
		return (Stream)sendCommand(s);
	}
	@Override
	public Object getByIndex(DBKey arg1) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException {
		RelatrixStatement s = new RelatrixStatement("getByIndex", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastKey() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey",new Object[]{});
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastKey(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastKey(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object lastKey(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstKey() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey",new Object[]{});
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstKey(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstKey(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstKey(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstValue(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstValue() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue",new Object[]{});
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstValue(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object firstValue(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Iterator keySet(String arg1,Class arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2);
		return (Iterator)sendCommand(s);
	}
	@Override
	public Iterator keySet(Class arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1);
		return (Iterator)sendCommand(s);
	}
	@Override
	public List resolve(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("resolve", arg1);
		return (List)sendCommand(s);
	}
	@Override
	public Object first(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object first(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object first() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first",new Object[]{});
		return (Object)sendCommand(s);
	}
	@Override
	public Object first(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("first", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object last(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public Object last(String arg1,Class arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("last", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object last() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last",new Object[]{});
		return (Object)sendCommand(s);
	}
	@Override
	public Object last(Class arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public boolean contains(String arg1,Comparable arg2) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2);
		return (boolean)sendCommand(s);
	}
	@Override
	public boolean contains(Comparable arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1);
		return (boolean)sendCommand(s);
	}
	@Override
	public long size() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size",new Object[]{});
		return (long)sendCommand(s);
	}
	@Override
	public long size(String arg1) throws java.io.IOException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("size", arg1);
		return (long)sendCommand(s);
	}
	@Override
	public DomainMapRange store(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.util.NoSuchElementException,java.lang.ClassNotFoundException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3, arg4);
		return (DomainMapRange)sendCommand(s);
	}
	@Override
	public DomainMapRange store(Comparable arg1,Comparable arg2,Comparable arg3) throws java.lang.IllegalAccessException,java.io.IOException,com.neocoretechs.relatrix.DuplicateKeyException,java.lang.ClassNotFoundException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3);
		return (DomainMapRange)sendCommand(s);
	}
	@Override
	public Object get(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2);
		return (Object)sendCommand(s);
	}
	@Override
	public Object get(Comparable arg1) throws java.io.IOException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("get", arg1);
		return (Object)sendCommand(s);
	}
	@Override
	public void remove(String arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException,java.lang.IllegalAccessException,java.util.NoSuchElementException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3, arg4);
		sendCommand(s);
	}
	@Override
	public void remove(String arg1,Comparable arg2) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.util.NoSuchElementException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		sendCommand(s);
	}
	@Override
	public void remove(Comparable arg1) throws java.io.IOException,java.lang.IllegalArgumentException,java.lang.ClassNotFoundException,java.lang.IllegalAccessException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1);
		sendCommand(s);
	}
	@Override
	public void remove(Comparable arg1,Comparable arg2,Comparable arg3) throws java.io.IOException,java.lang.IllegalAccessException,java.lang.ClassNotFoundException,com.neocoretechs.relatrix.DuplicateKeyException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3);
		sendCommand(s);
	}

}

