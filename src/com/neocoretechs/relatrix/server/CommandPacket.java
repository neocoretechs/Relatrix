package com.neocoretechs.relatrix.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * Remote master is from the perspective of the server, as it calls back to the 
 * source of the sent ComandPacket to the bootNode
 * @author jg 2020
 *
 */
public class CommandPacket implements CommandPacketInterface {
	private static final long serialVersionUID = 8579879510521146873L;
	String bootNode;
	int MASTERPORT;
	String transport = "TCP";
	
	public CommandPacket(String bootNode, int masterport) {
		this.bootNode = bootNode;
		MASTERPORT = masterport;
	}
	@Override
	public int getMasterPort() {
		return MASTERPORT;
	}

	@Override
	public void setMasterPort(int port) {
		MASTERPORT = port;
	}

	@Override
	public String getTransport() {
		return transport;
	}
	@Override
	public void setTransport(String transport) {
		this.transport = transport;
	}
	@Override
	public String getRemoteMaster() {
			return bootNode;
	}
	
	@Override
	public void setRemoteMaster(String remoteMaster) {
			bootNode = remoteMaster;
	}

	@Override
	public String toString() { return "CommandPacket boot:"+bootNode+" trans:"+transport+" ports:"+MASTERPORT; }
	
}
