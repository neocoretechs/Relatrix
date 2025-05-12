// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings Mon May 12 10:12:54 PDT 2025
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
import com.neocoretechs.relatrix.key.DBKey;
import java.util.ArrayList;
import com.neocoretechs.relatrix.type.RelationList;
import com.neocoretechs.relatrix.Relation;


public abstract class AsynchRelatrixClientInterfaceImpl implements AsynchRelatrixClientInterface{

	public abstract CompletableFuture<Object> queueCommand(RelatrixStatementInterface s);
	@Override
	public CompletableFuture<Stream> findStream(Object arg1,Character arg2,Character arg3) {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findStream(Alias arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<String[][]> getAliases() {
		RelatrixStatement s = new RelatrixStatement("getAliases",new Object[]{});
		return queueCommand(s).thenApply(result -> (String[][]) result);
	}
	@Override
	public CompletableFuture<DBKey> getNewKey() {
		RelatrixStatement s = new RelatrixStatement("getNewKey",new Object[]{});
		return queueCommand(s).thenApply(result -> (DBKey) result);
	}
	@Override
	public CompletableFuture<Object> removekv(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("removekv", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> removekv(Alias arg1,Comparable arg2) {
		RelatrixStatement s = new RelatrixStatement("removekv", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findTailSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findTailSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findSubStream(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findSubStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9,Object arg10) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8,Object arg9) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSubSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6,Object arg7,Object arg8) {
		RelatrixStatement s = new RelatrixStatement("findSubSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<String> getAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("getAlias", arg1);
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Void> removeAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("removeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> setTuple(Character arg1) {
		RelatrixStatement s = new RelatrixStatement("setTuple", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Stream> findHeadStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findHeadStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public Object getByIndex(Alias arg1,DBKey arg2) {
		RelatrixStatement s = new RelatrixStatement("getByIndex", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object getByIndex(DBKey arg1) {
		RelatrixStatement s = new RelatrixStatement("getByIndex", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Object> lastValue(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue() {
		RelatrixStatement s = new RelatrixStatement("lastValue",new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastValue(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("lastValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Stream> findTailStream(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Stream> findTailStream(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("findTailStream", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Object arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Object arg3,Character arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Character arg3,Character arg4,Object arg5,Object arg6,Object arg7) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Object arg2,Character arg3,Character arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Alias arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Character arg2,Character arg3,Object arg4,Object arg5,Object arg6) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5, arg6);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Character arg2,Object arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Object arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Character arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Character arg2,Character arg3,Object arg4,Object arg5) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4, arg5);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findHeadSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findHeadSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Void> setWildcard(Character arg1) {
		RelatrixStatement s = new RelatrixStatement("setWildcard", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<RelationList> multiStore(Alias arg1,ArrayList arg2) {
		RelatrixStatement s = new RelatrixStatement("multiStore", arg1, arg2);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<RelationList> multiStore(ArrayList arg1) {
		RelatrixStatement s = new RelatrixStatement("multiStore", arg1);
		return queueCommand(s).thenApply(result -> (RelationList) result);
	}
	@Override
	public CompletableFuture<String> getTableSpace() {
		RelatrixStatement s = new RelatrixStatement("getTableSpace",new Object[]{});
		return queueCommand(s).thenApply(result -> (String) result);
	}
	@Override
	public CompletableFuture<Void> setRelativeAlias(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("setRelativeAlias", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Object arg2,Character arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Character arg2,Character arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Character arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Character arg3,Character arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Character arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Object arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Object arg3,Character arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Object arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Character arg2,Object arg3,Character arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> findSet(Alias arg1,Object arg2) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> findSet(Object arg1) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Character arg2,Character arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Character arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Character arg1,Object arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Object arg1,Object arg2,Character arg3) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Character arg3,Character arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> findSet(Alias arg1,Object arg2,Character arg3,Object arg4) {
		RelatrixStatement s = new RelatrixStatement("findSet", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public void storekv(Alias arg1,Comparable arg2,Object arg3) {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2, arg3);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public void storekv(Comparable arg1,Object arg2) {
		RelatrixStatement s = new RelatrixStatement("storekv", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Stream> entrySetStream(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
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
		RelatrixStatement s = new RelatrixStatement("entrySetStream", arg1, arg2);
		return queueCommand(s).thenApply(result -> {
	        try {
	            return (Stream)(new RemoteStream((Iterator) result));
	        } catch (Exception e) {
	            throw new CompletionException(e);
	        }
	    }).exceptionally(ex -> {
	        // Handle the exception, e.g., return an empty stream or throw a custom exception
	        throw new RuntimeException(ex);
	    });

	}
	@Override
	public CompletableFuture<Object> lastKey(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey() {
		RelatrixStatement s = new RelatrixStatement("lastKey",new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> lastKey(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("lastKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey() {
		RelatrixStatement s = new RelatrixStatement("firstKey",new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstKey(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("firstKey", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue() {
		RelatrixStatement s = new RelatrixStatement("firstValue",new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> firstValue(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("firstValue", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> keySet(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> keySet(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("keySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<List> store(Alias arg1,ArrayList arg2) {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<List> store(ArrayList arg1) {
		RelatrixStatement s = new RelatrixStatement("store", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Relation> store(Comparable arg1,Comparable arg2,Comparable arg3) {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<Relation> store(Alias arg1,Comparable arg2,Comparable arg3,Comparable arg4) {
		RelatrixStatement s = new RelatrixStatement("store", arg1, arg2, arg3, arg4);
		return queueCommand(s).thenApply(result -> (Relation) result);
	}
	@Override
	public CompletableFuture<List> resolve(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("resolve", arg1);
		return queueCommand(s).thenApply(result -> (List) result);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("first", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first() {
		RelatrixStatement s = new RelatrixStatement("first",new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> first(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("first", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Iterator> entrySet(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Iterator> entrySet(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("entrySet", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Iterator) result);

	}
	@Override
	public CompletableFuture<Long> size() {
		RelatrixStatement s = new RelatrixStatement("size",new Object[]{});
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("size", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Long> size(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("size", arg1);
		return queueCommand(s).thenApply(result -> (Long) result);
	}
	@Override
	public CompletableFuture<Object> last(Class arg1) {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last() {
		RelatrixStatement s = new RelatrixStatement("last",new Object[]{});
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Alias arg1) {
		RelatrixStatement s = new RelatrixStatement("last", arg1);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Object> last(Alias arg1,Class arg2) {
		RelatrixStatement s = new RelatrixStatement("last", arg1, arg2);
		return queueCommand(s);
	}
	@Override
	public CompletableFuture<Boolean> contains(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("contains", arg1);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public CompletableFuture<Boolean> contains(Alias arg1,Comparable arg2) {
		RelatrixStatement s = new RelatrixStatement("contains", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Boolean) result);
	}
	@Override
	public Object get(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("get", arg1);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public Object get(Alias arg1,Comparable arg2) {
		RelatrixStatement s = new RelatrixStatement("get", arg1, arg2);
		CompletableFuture<Object> cf = queueCommand(s);
          try {
                    return cf.get();
          } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
          }
	}
	@Override
	public CompletableFuture<Void> remove(Alias arg1,Comparable arg2) {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> remove(Comparable arg1) {
		RelatrixStatement s = new RelatrixStatement("remove", arg1);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> remove(Alias arg1,Comparable arg2,Comparable arg3) {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2, arg3);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
	@Override
	public CompletableFuture<Void> remove(Comparable arg1,Comparable arg2) {
		RelatrixStatement s = new RelatrixStatement("remove", arg1, arg2);
		return queueCommand(s).thenApply(result -> (Void) result);

	}
}

