// auto generated from com.neocoretechs.relatrix.server.GenerateJsonAsynchClientBindings Mon Jul 06 19:41:06 PDT 2026
package com.neocoretechs.relatrix.client.asynch.json;

import java.util.Iterator;
import java.util.stream.Stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;

import com.neocoretechs.rocksack.Alias;

import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.client.json.RelatrixKVStatementJson;
import com.neocoretechs.relatrix.key.DBKey;


public abstract class AsynchRelatrixKVClientInterfaceJsonImpl implements AsynchRelatrixKVClientInterfaceJson{

	public abstract CompletableFuture<Object> queueCommand(RelatrixStatementInterface s);
	@Override
	public CompletableFuture<Object> nearest(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("nearest", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> nearest(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("nearest", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<String> getAlias(Alias arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Object> lastValue(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> findSubMap(Object arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMap(Alias arg1,Object arg2,Object arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("getAliases",new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("storekv", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("storekv", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(Object arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,Object arg2,Object arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubMapStream(Alias arg1,Object arg2,Object arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMapStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findSubMapStream(Object arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMapStream", arg1, arg2);
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
	public CompletableFuture<Stream> findHeadMapKVStream(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMapKVStream", arg1);
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
	public CompletableFuture<Stream> findHeadMapKVStream(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMapKVStream", arg1, arg2);
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
	public CompletableFuture<Stream> findHeadMapStream(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMapStream", arg1, arg2);
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
	public CompletableFuture<Stream> findHeadMapStream(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMapStream", arg1);
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
	public CompletableFuture<Iterator> findTailMap(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMap(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMap", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMap", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findTailMapKVStream(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMapKVStream", arg1, arg2);
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
	public CompletableFuture<Stream> findTailMapKVStream(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMapKVStream", arg1);
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
	public CompletableFuture<Stream> findTailMapStream(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMapStream", arg1, arg2);
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
	public CompletableFuture<Stream> findTailMapStream(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMapStream", arg1);
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
	public CompletableFuture<Iterator> findTailMapKV(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMapKV", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findTailMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findHeadMapKV", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,Object arg2,Object arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMapKVStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findSubMapKVStream(Object arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("findSubMapKVStream", arg1, arg2);
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
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> keySetStream(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("keySetStream", arg1);
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
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("keySetStream", arg1, arg2);
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
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("entrySetStream", arg1);
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
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("entrySetStream", arg1, arg2);
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
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public Object getByIndex(DBKey arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("getByIndex", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object getByIndex(Alias arg1,DBKey arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("getByIndex", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Boolean> containsValue(Class arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("containsValue", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> containsValue(Alias arg1,Class arg2,Comparable arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("containsValue", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("keySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> close(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("close", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> close(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("close", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("entrySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Boolean> contains(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("contains", arg1);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Void> store(Object arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("store", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> store(Alias arg1,Object arg2,Object arg3) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public Object get(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("get", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Long> size(Class arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,Class arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public Object remove(Alias arg1,Object arg2) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("remove", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object remove(Object arg1) {
		RelatrixKVStatementJson s = new RelatrixKVStatementJson("remove", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
}

