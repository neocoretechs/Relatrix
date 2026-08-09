// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon May 12 11:37:11 PDT 2025
package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.client.*;
import com.neocoretechs.relatrix.key.DBKey;


public abstract class AsynchRelatrixKVClientInterfaceImpl implements AsynchRelatrixKVClientInterface{

	public abstract CompletableFuture<Object> queueCommand(RelatrixStatementInterface s);
	@Override
	public CompletableFuture<Object> lastValue(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Stream> findHeadMapStream(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMapStream", arg1, arg2);
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
	public CompletableFuture<Stream> findHeadMapStream(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMapStream", arg1);
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
	public CompletableFuture<Stream> keySetStream(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "keySetStream", arg1, arg2);
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
	public CompletableFuture<Stream> keySetStream(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "keySetStream", arg1);
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
	public CompletableFuture<Stream> findSubMapKVStream(Comparable arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMapKVStream", arg1, arg2);
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
	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,Comparable arg2,Comparable arg3) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMapKVStream", arg1, arg2, arg3);
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
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,Comparable arg2,Comparable arg3) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(Comparable arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubMapStream(Alias arg1,Comparable arg2,Comparable arg3) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMapStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findSubMapStream(Comparable arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMapStream", arg1, arg2);
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
	public CompletableFuture<Iterator> findTailMap(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMap(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMap", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String> getAlias(Alias arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMapKV", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findTailMapStream(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMapStream", arg1);
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
	public CompletableFuture<Stream> findTailMapStream(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMapStream", arg1, arg2);
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
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> setAlias(Alias arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "setAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(),"getAliases", new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public CompletableFuture<Object> nearest(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "nearest", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> nearest(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "nearest", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> findSubMap(Alias arg1,Comparable arg2,Comparable arg3) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMap(Comparable arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findSubMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMap", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "entrySetStream", arg1, arg2);
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
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "entrySetStream", arg1);
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
	public CompletableFuture<Iterator> findTailMapKV(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMapKV", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findHeadMapKVStream(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMapKVStream", arg1, arg2);
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
	public CompletableFuture<Stream> findHeadMapKVStream(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findHeadMapKVStream", arg1);
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
	public CompletableFuture<Stream> findTailMapKVStream(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMapKVStream", arg1);
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
	public CompletableFuture<Stream> findTailMapKVStream(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "findTailMapKVStream", arg1, arg2);
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
	public CompletableFuture<Object> lastKey(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> close(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "close", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> close(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "close", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "keySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Boolean> containsValue(Alias arg1,Class arg2,Comparable arg3) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "containsValue", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> containsValue(Class arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "containsValue", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Void> store(Comparable arg1,Object arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "store", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> store(Alias arg1,Comparable arg2,Object arg3) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "entrySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,Class arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Class arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "contains", arg1);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public Object get(Alias arg1,Object arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Object arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "get", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> remove(Comparable arg1) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "remove", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> remove(Alias arg1,Comparable arg2) {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(), "remove", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<DBKey> getNewKey() {
		RelatrixKVStatement s = new RelatrixKVStatement(getSession(),"getNewKey", new Object[]{});
		return queueCommand(s).thenApply(result -> (DBKey) result);
	}
}

