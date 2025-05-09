package com.neocoretechs.relatrix.client;

/**
 * Maintains the barriers and latches to facilitate waits for completion of remote operations on the servers.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2022
 *
 */
public interface RemoteCompletionInterface extends RemoteRequestInterface {
		public Object getCompletionObject();
		public void setCompletionObject(Object cdl);
		public void signalCompletion(Object o);
		public void setObjectReturn(Object o);
		public void process() throws Exception;
}
