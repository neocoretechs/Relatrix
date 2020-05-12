package com.neocoretechs.relatrix.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
/**
 * Maintains the barriers and latches to facilitate waits for completion of remote operations on the servers
 * @author Groff
 *
 */
public interface RemoteCompletionInterface extends RemoteRequestInterface {
		public CountDownLatch getCountDownLatch();
		public void setCountDownLatch(CountDownLatch cdl);
		public CyclicBarrier getCyclicBarrier();
		public void setCyclicBarrier(CyclicBarrier cb);
		public void setLongReturn(long val);
		public void setObjectReturn(Object o);
		public void process() throws Exception;
}
