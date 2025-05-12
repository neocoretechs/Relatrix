// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon May 12 12:57:28 PDT 2025
package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.*;


public abstract class AsynchRelatrixKVClientTransactionInterfaceImpl implements AsynchRelatrixKVClientTransactionInterface{

	public abstract CompletableFuture<Object> queueCommand(com.neocoretechs.relatrix.client.RelatrixKVTransactionStatementInterface s);
	@Override
	public CompletableFuture<Object[]> getTransactionState() {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("getTransactionState",new Object[]{});
		return queueCommand(s).thenApply(result -> (Object[]) result);
	}
	@Override
	public CompletableFuture<Stream> findTailMapKVStream(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMapKVStream", arg1, arg2);
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
	public CompletableFuture<Stream> findTailMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMapKVStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findHeadMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMapKVStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findHeadMapKVStream(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMapKVStream", arg1, arg2);
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
	public CompletableFuture<Void> rollbackToCheckpoint(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("rollbackToCheckpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackToCheckpoint(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("rollbackToCheckpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackTransaction(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("rollbackTransaction", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackAllTransactions() {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("rollbackAllTransactions",new Object[]{});
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollback(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("rollback", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollback(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("rollback", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Object> nearest(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("nearest", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> nearest(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("nearest", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> findTailMap(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMap(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("getAliases",new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("lastValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> keySetStream(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("keySetStream", arg1, arg2);
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
	public CompletableFuture<Stream> keySetStream(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("keySetStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findSubMapStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMapStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findSubMapStream(TransactionId arg1,Comparable arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMapStream", arg1, arg2, arg3);
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
	public TransactionId getTransactionId() {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("getTransactionId",new Object[]{});
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public TransactionId getTransactionId(long arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("getTransactionId", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMapKVStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findSubMapKVStream(TransactionId arg1,Comparable arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMapKVStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> findTailMapStream(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMapStream", arg1, arg2);
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
	public CompletableFuture<Stream> findTailMapStream(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMapStream", arg1, arg2, arg3);
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
	public CompletableFuture<Void> endTransaction(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("endTransaction", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMapKV", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(TransactionId arg1,Comparable arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("entrySetStream", arg1, arg2, arg3);
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
	public CompletableFuture<Stream> entrySetStream(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("entrySetStream", arg1, arg2);
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
	public CompletableFuture<Iterator> findSubMap(TransactionId arg1,Comparable arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMap(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findSubMap", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> checkpoint(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("checkpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> checkpoint(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("checkpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> findHeadMapStream(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMapStream", arg1, arg2);
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
	public CompletableFuture<Stream> findHeadMapStream(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findHeadMapStream", arg1, arg2, arg3);
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
	public CompletableFuture<String> getAlias(Alias arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Iterator> findTailMapKV(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("findTailMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> commit(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("commit", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> commit(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("commit", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("lastKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("firstKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("firstValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> close(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("close", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> close(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("close", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("keySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Boolean> containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("containsValue", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> containsValue(TransactionId arg1,Class arg2,Object arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("containsValue", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Void> store(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("store", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> store(TransactionId arg1,Comparable arg2,Object arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("entrySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("size", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("contains", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(TransactionId arg1,Class arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("contains", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("contains", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public Object get(TransactionId arg1,Class arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("get", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("get", arg1, arg2, arg3, arg4);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("get", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> remove(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("remove", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> remove(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement("remove", arg1, arg2);
		return queueCommand(s);
	}
}

