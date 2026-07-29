// auto generated from com.neocoretechs.relatrix.server.GenerateJsonAsynchClientBindings Mon Jul 06 19:38:49 PDT 2026
package com.neocoretechs.relatrix.client.asynch.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;

import com.neocoretechs.rocksack.Alias;

import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.client.json.RelatrixStatementJson;

import java.util.ArrayList;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;

public abstract class AsynchRelatrixClientInterfaceJsonImpl implements AsynchRelatrixClientInterfaceJson{

	public abstract CompletableFuture<Object> queueCommand(RelatrixStatementInterface s);
	@Override
	public CompletableFuture<String[][]> getAliases() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"getAliases", new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "storekv", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "storekv", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<String> getAlias(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"lastValue", new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<DBKey> getNewKey() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"getNewKey", new Object[]{});
		return queueCommand(s).thenApply(result -> (DBKey) result);
	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Object> removekv(Alias arg1,Comparable arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "removekv", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> removekv(Comparable arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "removekv", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<RelationList> multiStore(ArrayList arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "multiStore", arg1);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<RelationList> multiStore(Alias arg1,ArrayList arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "multiStore", arg1, arg2);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Character arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> findSet(Alias arg1,Object arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSet(Object arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Character arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Object arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Object arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Character arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Character arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Character arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Character arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Object arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Character arg1,Object arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Character arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Character arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Object arg1,Character arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Character arg1,Character arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Character arg1,Character arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Character arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Object arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Object arg1,Object arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Object arg1,Character arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Void> setTuple(Character arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setTuple", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<List> findSetParallel(Character arg1,List arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,Character arg2,List arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,List arg2,Character arg3,Character arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Character arg1,Character arg2,List arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(List arg1,Character arg2,Character arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,Character arg2,Character arg3,List arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Void> setWildcard(Character arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "setWildcard", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySetStream", arg1);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySetStream", arg1, arg2);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<String> getTableSpace() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"getTableSpace", new Object[]{});
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Object> lastKey() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"lastKey", new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public Object getByIndex(DBKey arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "getByIndex", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object getByIndex(Alias arg1,DBKey arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "getByIndex", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> firstKey() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"firstKey", new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"firstValue", new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "keySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> resolve(Comparable arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "resolve", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "first", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "first", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "first", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"first", new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> entrySet(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Object> last(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "last", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "last", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "last", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"last", new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,Object arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Object arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "contains", arg1);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<List> store(Alias arg1,ArrayList arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Relation> store(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<Relation> store(Object arg1,Object arg2,Object arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<List> store(ArrayList arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "store", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public Object get(Alias arg1,Object arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Object arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "get", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Long> size(Class arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,Class arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size() {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(),"size", new Object[]{});
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public Object remove(Alias arg1,Object arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                   return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object remove(Object arg1) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                   return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void remove(Alias arg1,Comparable arg2,Comparable arg3) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void remove(Object arg1,Object arg2) {
		RelatrixStatementJson s = new RelatrixStatementJson(getSession(), "remove", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
}

