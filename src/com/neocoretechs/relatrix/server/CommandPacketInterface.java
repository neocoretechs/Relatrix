package com.neocoretechs.relatrix.server;

import java.io.Serializable;


/**
 * Command packet interface bound for WorkBoot nodes to activate threads
 * to operate on a specific port. RemoteMaster is from the perspective of the server,
 * which is sent the packet with the master and slave ports of the client.
 * controller
 * @author Groff Copyright (C) NeoCoreTechs 4/2000, 5/2020
 *
 */
public interface CommandPacketInterface extends Serializable {
	public int getMasterPort();
	public void setMasterPort(int port);
	public String getTransport();
	public void setTransport(String transport);
	public String getRemoteMaster();
	public void setRemoteMaster(String remoteMaster);

}
