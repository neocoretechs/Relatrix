// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Fri Jun 20 08:50:13 PDT 2025
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
import java.util.ArrayList;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;


public abstract class AsynchRelatrixClientTransactionInterfaceImpl implements AsynchRelatrixClientTransactionInterface{

	public abstract CompletableFuture<Object> queueCommand(RelatrixTransactionStatementInterface s);
	@Override
	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByIndex", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object getByIndex(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getByIndex", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("storekv", arg1, arg2, arg3, arg4);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("storekv", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Void> rollback(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollback", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollback(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollback", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public TransactionId getTransactionId() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTransactionId",new Object[]{});
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public TransactionId getTransactionId(long arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTransactionId", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAliases",new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public CompletableFuture<String> getAlias(Alias arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> setWildcard(Character arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setWildcard", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> checkpoint(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("checkpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> checkpoint(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("checkpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> setTuple(Character arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setTuple", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<RelationList> multiStore(TransactionId arg1,ArrayList arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("multiStore", arg1, arg2);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<RelationList> multiStore(Alias arg1,TransactionId arg2,ArrayList arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("multiStore", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String> getTableSpace() {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("getTableSpace",new Object[]{});
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Character arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Stream> findStream(TransactionId arg1,Object arg2,Object arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findStream", arg1, arg2, arg3, arg4);
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
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> findSet(Alias arg1,TransactionId arg2,Object arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSet(TransactionId arg1,Object arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> findSetParallel(TransactionId arg1,List arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(TransactionId arg1,Character arg2,List arg3,Character arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(TransactionId arg1,Character arg2,Character arg3,List arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,TransactionId arg2,List arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSetParallel", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,TransactionId arg2,Character arg3,List arg4,Character arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSetParallel", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,TransactionId arg2,Character arg3,Character arg4,List arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("findSetParallel", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Object> removekv(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removekv", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> removekv(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("removekv", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Void> commit(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("commit", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> commit(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("commit", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> endTransaction(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("endTransaction", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackToCheckpoint(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollbackToCheckpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackToCheckpoint(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("rollbackToCheckpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySetStream", arg1, arg2, arg3);
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
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySetStream", arg1, arg2);
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
	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("lastKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("firstValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("keySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Relation> store(TransactionId arg1,Comparable arg2,Comparable arg3,Comparable arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<Relation> store(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4,Comparable arg5) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<List> store(TransactionId arg1,ArrayList arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> store(Alias arg1,TransactionId arg2,ArrayList arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> resolve(Comparable arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("resolve", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("first", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("entrySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Object> last(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(TransactionId arg1) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("last", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Boolean> contains(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("contains", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("get", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Void> remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> remove(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> remove(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> remove(TransactionId arg1,Comparable arg2,Comparable arg3) {
		RelatrixTransactionStatement s = new RelatrixTransactionStatement("remove", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
}

