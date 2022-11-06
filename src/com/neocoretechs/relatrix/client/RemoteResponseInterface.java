package com.neocoretechs.relatrix.client;
/**
 * This interface expresses the contract with a response message coming from remote server
 * to the RelatrixClient. the baseline data includes session ID and return object.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2020,2022
 *
 */
public interface RemoteResponseInterface extends RemoteCompletionInterface {
	public String getSession();
	public long getLongReturn();
	public Object getObjectReturn();
}
