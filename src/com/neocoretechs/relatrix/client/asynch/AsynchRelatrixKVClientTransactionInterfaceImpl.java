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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(),"getTransactionState", new Object[]{});
		return queueCommand(s).thenApply(result -> (Object[]) result);
	}
	@Override
	public CompletableFuture<Stream> findTailMapKVStream(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMapKVStream", arg1, arg2);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMapKVStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMapKVStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMapKVStream", arg1, arg2);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "rollbackToCheckpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackToCheckpoint(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "rollbackToCheckpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackTransaction(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "rollbackTransaction", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackAllTransactions() {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(),"rollbackAllTransactions", new Object[]{});
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "rollback", arg1, arg2);
		//return queueCommand(s).thenApply(result -> (Void) result);
		CompletableFuture<Object> cf = queueCommand(s);
        try {
                  cf.get();
        } catch (InterruptedException | ExecutionException e) {
                  throw new RuntimeException(e);
        }

	}
	@Override
	public void rollback(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "rollback", arg1);
		//return queueCommand(s).thenApply(result -> (Void) result);
		CompletableFuture<Object> cf = queueCommand(s);
        try {
                  cf.get();
        } catch (InterruptedException | ExecutionException e) {
                  throw new RuntimeException(e);
        }

	}
	@Override
	public CompletableFuture<Object> nearest(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "nearest", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> nearest(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "nearest", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> findTailMap(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMap(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(),"getAliases", new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "lastValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> setAlias(Alias arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "setAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);
	}
	@Override
	public CompletableFuture<Stream> keySetStream(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "keySetStream", arg1, arg2);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "keySetStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMapStream", arg1, arg2, arg3, arg4);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMapStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(),"getTransactionId", new Object[]{});
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public TransactionId getTransactionId(long arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "getTransactionId", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMap(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMap", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubMapKVStream(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMapKVStream", arg1, arg2, arg3, arg4);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMapKVStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMapStream", arg1, arg2);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMapStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "endTransaction", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMapKV", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMapKV(TransactionId arg1,Comparable arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "entrySetStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "entrySetStream", arg1, arg2);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMap", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubMap(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findSubMap", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadMapKV(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> checkpoint(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "checkpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> checkpoint(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "checkpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> findHeadMapStream(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMapStream", arg1, arg2);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findHeadMapStream", arg1, arg2, arg3);
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
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Iterator> findTailMapKV(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMapKV", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailMapKV(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "findTailMapKV", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "commit", arg1, arg2);
		//return queueCommand(s).thenApply(result -> (Void) result);
		CompletableFuture<Object> cf = queueCommand(s);
        try {
                  cf.get();
        } catch (InterruptedException | ExecutionException e) {
                  throw new RuntimeException(e);
        }

	}
	@Override
	public void commit(TransactionId arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "commit", arg1);
		//return queueCommand(s).thenApply(result -> (Void) result);
		CompletableFuture<Object> cf = queueCommand(s);
        try {
                  cf.get();
        } catch (InterruptedException | ExecutionException e) {
                  throw new RuntimeException(e);
        }

	}
	@Override
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "lastKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "firstKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "firstValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> close(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "close", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> close(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "close", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "keySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Boolean> containsValue(Alias arg1,TransactionId arg2,Class arg3,Object arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "containsValue", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> containsValue(TransactionId arg1,Class arg2,Object arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "containsValue", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Void> store(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "store", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> store(TransactionId arg1,Comparable arg2,Object arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "entrySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2,Class arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "size", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(TransactionId arg1,Class arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(TransactionId arg1,Class arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "contains", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public Object get(TransactionId arg1,Class arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Class arg3,Comparable arg4) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2, arg3, arg4);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "remove", arg1, arg2, arg3);
		//return queueCommand(s);
		CompletableFuture<Object> cf = queueCommand(s);
        try {
                  cf.get();
        } catch (InterruptedException | ExecutionException e) {
                  throw new RuntimeException(e);
        }
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2) {
		com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement s = new com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement(getSession(), "remove", arg1, arg2);
		//return queueCommand(s);
		CompletableFuture<Object> cf = queueCommand(s);
        try {
                  cf.get();
        } catch (InterruptedException | ExecutionException e) {
                  throw new RuntimeException(e);
        }
	}
}

