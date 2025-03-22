package com.neocoretechs.relatrix.client;

import java.util.concurrent.CountDownLatch;

/**
 * Maintains the barriers and latches to facilitate waits for completion of remote operations on the servers.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RemoteCompletionInterface extends RemoteRequestInterface {
		public CountDownLatch getCountDownLatch();
		public void setCountDownLatch(CountDownLatch cdl);
		public void setLongReturn(long val);
		public void setObjectReturn(Object o);
		public void process() throws Exception;
}
