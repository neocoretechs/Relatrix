// auto generated from com.neocoretechs.relatrix.server.GenerateClientBindings Fri Jun 19 11:22:34 PDT 2026
package com.neocoretechs.relatrix.client.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import com.neocoretechs.rocksack.Alias;
import java.util.ArrayList;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;


public abstract class RelatrixClientInterfaceJsonImpl implements RelatrixClientInterfaceJson{

	public abstract Object sendCommand(RelatrixStatementInterface s) throws Exception;
	@Override
	public Stream findTailStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Character arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Object arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Object arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findTailStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Character arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Object arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Object arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findTailSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Character arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Object arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findHeadStream(Object arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Character arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Object arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Object arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findHeadSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSetParallel(Alias arg1,Character arg2,List arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Alias arg1,Character arg2,Character arg3,List arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Alias arg1,List arg2,Character arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Character arg1,Character arg2,List arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(Character arg1,List arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public List findSetParallel(List arg1,Character arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public void setRelativeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setRelativeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setWildcard(Character arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setWildcard", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public void removeAlias(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "removeAlias", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String getTableSpace() {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"getTableSpace", new Object[]{});
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Stream entrySetStream(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySetStream", arg1);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream entrySetStream(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySetStream", arg1, arg2);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void setTuple(Character arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setTuple", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
		}
	}
	@Override
	public Stream findStream(Alias arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Object arg1,Character arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Object arg1,Character arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Character arg1,Character arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Character arg1,Character arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Object arg1,Object arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Character arg1,Object arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Character arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Stream findStream(Alias arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		try {
			return new RemoteStream((Iterator)sendCommand(s));
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelationList multiStore(ArrayList arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "multiStore", arg1);
		try {
			return (RelationList)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public RelationList multiStore(Alias arg1,ArrayList arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "multiStore", arg1, arg2);
		try {
			return (RelationList)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "storekv", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "storekv", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public DBKey getNewKey() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"getNewKey", new Object[]{});
		try {
			return (DBKey)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Object arg1,Object arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Character arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List findSet(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Character arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Object arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Object arg2,Object arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Character arg2,Character arg3,Character arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Character arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Alias arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Object arg1,Character arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Character arg1,Character arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Object arg1,Character arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Character arg1,Character arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSet(Character arg1,Object arg2,Character arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "removekv", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object removekv(Comparable arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "removekv", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"lastValue", new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastValue(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Character arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Character arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Object arg2,Character arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator findSubSet(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public String[][] getAliases() {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"getAliases", new Object[]{});
		try {
			return (String[][])sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public String getAlias(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "getAlias", arg1);
		try {
			return (String)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object lastKey(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object lastKey() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"lastKey", new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(DBKey arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "getByIndex", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object getByIndex(Alias arg1,DBKey arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "getByIndex", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstKey", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"firstKey", new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstKey(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstKey", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"firstValue", new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstValue", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object firstValue(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstValue", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "keySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator keySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "keySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List resolve(Comparable arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "resolve", arg1);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			return null;
		}
	}
	@Override
	public Object first(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "first", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"first", new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object first(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "first", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySet", arg1);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Iterator entrySet(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySet", arg1, arg2);
		try {
			return (Iterator)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "last", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "last", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object last() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"last", new Object[]{});
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Comparable arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "contains", arg1);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public boolean contains(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "contains", arg1, arg2);
		try {
			return (boolean)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List store(Alias arg1,ArrayList arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Object arg1,Object arg2,Object arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2, arg3);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Alias arg1,Object arg2,Object arg3,Object arg4) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public List store(ArrayList arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1);
		try {
			return (List)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Comparable arg1, Comparable arg2, Comparable arg3) throws IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2, arg3);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Relation store(Alias arg1, Comparable arg2, Comparable arg3, Comparable arg4) throws IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		try {
			return (Relation)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Comparable arg1) throws IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Comparable arg1, Comparable arg2) throws IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}

	}
	@Override
	public Object get(Alias arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "get", arg1, arg2);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object get(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "get", arg1);
		try {
			return (Object)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size() throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(null,"size", new Object[]{});
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Class arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1,Class arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "size", arg1, arg2);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public long size(Alias arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "size", arg1);
		try {
			return (long)sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,Comparable arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public void remove(Alias arg1,Comparable arg2,Comparable arg3) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2, arg3);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	
	@Override
	public void remove(Object arg1,Object arg2) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2);
		try {
			sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
	@Override
	public Object remove(Object arg1) throws java.io.IOException {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1);
		try {
			return sendCommand(s);
		} catch(Exception e) {
			throw new java.io.IOException(e);
		}
	}
}

