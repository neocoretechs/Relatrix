package com.neocoretechs.relatrix.client;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;

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
		public UUID getIteratorId();
		public void setIteratorId(UUID itid);
		public void setClient(RemoteIteratorClient iteratorClient);
}
