package com.neocoretechs.relatrix.client;

import java.util.UUID;

/**
 * Interface that allows a client to be assigned to a remote service
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024,2026
 */
public interface ClientInterface {
	public UUID getSession();
	public String getRemoteNode();
	public int getRemotePort();
}
