// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings
package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.DomainMapRange;


public abstract class RelatrixClientTransactionInterfaceImpl implements RelatrixClientTransactionInterface{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public void rollbackToCheckpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollbackToCheckpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollbackToCheckpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollbackToCheckpoint", arg1, arg2);
		try {
			sendCommand(s);
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
	public String getAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,char arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,char arg2,char arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,char arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,char arg2,char arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,char arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,char arg2,Object arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(TransactionId arg1,Object arg2,Object arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,char arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,char arg2,Object arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,char arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,char arg2,char arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,char arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,char arg2,char arg3,char arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
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
	public void rollback(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollback", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void rollback(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("rollback", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removekv", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("removekv", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
	public void endTransaction(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("endTransaction", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((RemoteIterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,char arg4,char arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,char arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,char arg4,char arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,char arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,char arg2,Object arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,char arg2,char arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,char arg2,char arg3,char arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,Object arg3,char arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,char arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(TransactionId arg1,Object arg2,char arg3,char arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public void checkpoint(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("checkpoint", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void checkpoint(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("checkpoint", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("commit", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("commit", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("setRelativeAlias", arg1);
		try {
			sendCommand(s);
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
	public TransactionId getTransactionId() throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getTransactionId",new Object[]{});
		try {
			return (TransactionId)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("getByIndex", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3, arg4, arg5);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DomainMapRange store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3, arg4);
		try {
			return (DomainMapRange)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2, arg3);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,TransactionId arg2,Class arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId arg1,Class arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,TransactionId arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(TransactionId arg1) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2, arg3);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2, arg3);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3, arg4);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

