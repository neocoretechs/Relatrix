package com.neocoretechs.relatrix.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * Maintains the barriers and latches to facilitate waits for completion of remote operations on the servers.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RemoteCompletionInterface extends RemoteRequestInterface {
		public CountDownLatch getCompletionObject();
		public void setCompletionObject();
		public void signalCompletion(Object o);
		public void setObjectReturn(Object o);
		public void process() throws Exception;
		public CompletableFuture<Object> getCompletionFuture();
		public void setServerObjectReturn(Object o);
}
