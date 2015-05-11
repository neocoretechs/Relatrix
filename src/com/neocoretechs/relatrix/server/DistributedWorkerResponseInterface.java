package com.neocoretechs.relatrix.server;

import com.neocoretechs.bigsack.io.request.IoResponseInterface;

public interface DistributedWorkerResponseInterface {
	public void queueResponse(IoResponseInterface iori);
	public String getMasterPort();
	public String getSlavePort();
}
