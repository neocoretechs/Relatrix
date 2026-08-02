package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.UUID;

import com.neocoretechs.relatrix.client.RelatrixKVStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.rocksack.Alias;

public abstract class IndexResolverClientInterfaceImpl implements IndexResolverClientInterface {
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG = true;
	
	public abstract Object sendCommand(RelatrixStatementInterface command) throws Exception;
	
	@Override
	public void storekv(Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "storekv", arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1,DBKey arg2) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(DBKey arg1) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "getByIndex", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}

	@Override
	public Object get(Object arg1) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "get", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}

	@Override
	public Object remove(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "removekv", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Object arg1) throws java.io.IOException {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "removekv", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}

	@Override
	public abstract UUID getSession();
	
	@Override
	public abstract String getRemoteNode();
	
	@Override
	public abstract int getRemotePort();

}
