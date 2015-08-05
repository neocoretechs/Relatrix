package com.neocoretechs.relatrix.client;
/**
 * This interface expresses the contract with a response message coming from remote server
 * to the RelatrixClient.
 * @author jg
 *
 */
public interface RemoteResponseInterface extends RemoteCompletionInterface {
	public String getSession();
	public long getLongReturn();
	public Object getObjectReturn();
}
