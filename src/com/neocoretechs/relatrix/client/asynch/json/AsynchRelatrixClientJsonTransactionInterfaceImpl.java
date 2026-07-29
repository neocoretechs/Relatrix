// auto generated from com.neocoretechs.relatrix.server.GenerateJsonAsynchClientBindings Mon Jul 06 19:41:36 PDT 2026
package com.neocoretechs.relatrix.client.asynch.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.client.json.RelatrixTransactionStatementJson;

import java.util.ArrayList;

import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;


public abstract class AsynchRelatrixClientJsonTransactionInterfaceImpl implements AsynchRelatrixClientJsonTransactionInterface{

	public abstract CompletableFuture<Object> queueCommand(RelatrixTransactionStatementInterface s);
	@Override
	public CompletableFuture<Stream> findHeadStream(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findHeadStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySetStream", arg1, arg2, arg3);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySetStream", arg1, arg2);
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
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findTailStream(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public CompletableFuture<Stream> findTailStream(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
	public CompletableFuture<Stream> findSubStream(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
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
	public TransactionId getTransactionId(long arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getTransactionId", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public TransactionId getTransactionId() {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(),"getTransactionId", new Object[]{});
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.thenApply(result -> (TransactionId) result).get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackToCheckpoint(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollbackToCheckpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> rollbackToCheckpoint(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollbackToCheckpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<List> findSetParallel(TransactionId arg1,List arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(TransactionId arg1,Character arg2,List arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(TransactionId arg1,Character arg2,Character arg3,List arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,TransactionId arg2,List arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,TransactionId arg2,Character arg3,Character arg4,List arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSetParallel(Alias arg1,TransactionId arg2,Character arg3,List arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSetParallel", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<String> getTableSpace() {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(),"getTableSpace", new Object[]{});
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Void> setWildcard(Character arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setWildcard", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> endTransaction(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "endTransaction", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> findSet(Alias arg1,TransactionId arg2,Object arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSet(TransactionId arg1,Object arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Character arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Object arg2,Object arg3,Character arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(TransactionId arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10,Object arg11) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,TransactionId arg2,Character arg3,Character arg4,Character arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(TransactionId arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String> getAlias(Alias arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public void rollback(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollback", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void rollback(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "rollback", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(TransactionId arg1,Comparable arg2,Object arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "storekv", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(Alias arg1,TransactionId arg2,Comparable arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "storekv", arg1, arg2, arg3, arg4);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> removekv(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "removekv", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> removekv(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "removekv", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<RelationList> multiStore(Alias arg1,TransactionId arg2,ArrayList arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "multiStore", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<RelationList> multiStore(TransactionId arg1,ArrayList arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "multiStore", arg1, arg2);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<Void> setTuple(Character arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "setTuple", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,TransactionId arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4);
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
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "findStream", arg1, arg2, arg3, arg4, arg5);
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
	public CompletableFuture<Void> checkpoint(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "checkpoint", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> checkpoint(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "checkpoint", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(),"getAliases", new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public Object getByIndex(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getByIndex", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object getByIndex(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "getByIndex", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstKey", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "firstValue", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> keySet(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "keySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> resolve(Comparable arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "resolve", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Object> first(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "first", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1, arg2, arg3);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "last", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Boolean> contains(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "contains", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public void commit(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "commit", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void commit(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "commit", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Relation> store(TransactionId arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<List> store(TransactionId arg1,ArrayList arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Relation> store(Alias arg1,TransactionId arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<List> store(Alias arg1,TransactionId arg2,ArrayList arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public Object get(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "get", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Long> size(TransactionId arg1,Class arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(TransactionId arg1) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,TransactionId arg2,Class arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "size", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2,Comparable arg3) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void remove(Alias arg1,TransactionId arg2,Comparable arg3,Comparable arg4) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2, arg3, arg4);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void remove(TransactionId arg1,Comparable arg2) {
		RelatrixTransactionStatementJson s = new RelatrixTransactionStatementJson(getSession(), "remove", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
}

