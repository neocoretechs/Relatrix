// auto generated from com.neocoretechs.relatrix.server.GenerateJsonClientBindings Mon Jul 06 15:15:18 PDT 2026
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import java.util.ArrayList;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;


public abstract class RelatrixClientInterfaceJsonTransactionImpl implements RelatrixClientInterfaceJsonTransaction{

	public abstract Object sendCommand(RelatrixTransactionStatementInterface s) throws Exception;
	@Override
	public String getAlias(Alias arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelationList multiStore(Alias arg1,TransactionId arg2,ArrayList arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "multiStore", arg1, arg2, arg3);
		try {
			return (RelationList)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelationList multiStore(TransactionId arg1,ArrayList arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "multiStore", arg1, arg2);
		try {
			return (RelationList)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTuple(Character arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setTuple", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(null,"getAliases", new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "removekv", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "removekv", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "storekv", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setWildcard(Character arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setWildcard", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSetParallel(TransactionId arg1,List arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(TransactionId arg1,Character arg2,List arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(TransactionId arg1,Character arg2,Character arg3,List arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Alias arg1,TransactionId arg2,Character arg3,Character arg4,List arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4, arg5);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Alias arg1,TransactionId arg2,Character arg3,List arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4, arg5);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Alias arg1,TransactionId arg2,List arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4, arg5);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public TransactionId getTransactionId(long arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getTransactionId", arg1);
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(null,"getTransactionId", new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(null,"getTableSpace", new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getByIndex", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List resolve(Comparable arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "resolve", arg1);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object first(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List store(TransactionId arg1,ArrayList arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List store(Alias arg1,TransactionId arg2,ArrayList arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Alias arg1, TransactionId arg2, Comparable arg3, Comparable arg4, Comparable arg5) throws IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}

	@Override
	public Relation store(TransactionId arg1, Comparable arg2, Comparable arg3, Comparable arg4) throws IOException {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}

}

