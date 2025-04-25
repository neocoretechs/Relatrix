// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings Mon Mar 17 07:45:21 PDT 2025
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;


import java.util.ArrayList;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.rocksack.TransactionId;


public abstract class RelatrixJsonClientTransactionInterfaceImpl implements RelatrixClientTransactionInterface{

	public abstract Object sendCommand(RelatrixTransactionStatementInterface s) throws Exception;
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getByIndex", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("storekv", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(TransactionId arg1,Object arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(Alias arg1,TransactionId arg2,Object arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelationList multiStore(TransactionId arg1,ArrayList arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("multiStore", arg1, arg2);
		try {
			return (RelationList)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelationList multiStore(Alias arg1,TransactionId arg2,ArrayList arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("multiStore", arg1, arg2, arg3);
		try {
			return (RelationList)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("removekv", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("removekv", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId(long arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getTransactionId", arg1);
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getTransactionId",new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getAliases",new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTuple(Character arg1) {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("setTuple", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void setWildcard(Character arg1) {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("setWildcard", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("getTableSpace",new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("store", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List store(TransactionId arg1,ArrayList arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("store", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List store(Alias arg1,TransactionId arg2,ArrayList arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("store", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("store", arg1, arg2, arg3, arg4);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List resolve(Comparable arg1) {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("resolve", arg1);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object first(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("first", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("last", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId arg1) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("remove", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixJsonTransactionStatement s = new RelatrixJsonTransactionStatement("remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

