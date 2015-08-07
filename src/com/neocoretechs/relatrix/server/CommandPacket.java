package com.neocoretechs.relatrix.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandPacket implements CommandPacketInterface {
	private static final long serialVersionUID = 1L;
	String fname;
	InetAddress bootNode;
	int MASTERPORT, SLAVEPORT;
	String transport = "TCP";
	String remoteDirectory;
	
	public CommandPacket(InetAddress bootNode, String fname, String remoteDir, int masterport, int slaveport) {
		this.bootNode = bootNode;
		this.fname = fname;
		remoteDirectory = remoteDir;
		MASTERPORT = masterport;
		SLAVEPORT = slaveport;
	}
	@Override
	public String getDatabase() {
		return fname;
	}
	@Override
	public void setDatabase(String database) {
		fname = database;
	}
	@Override
	public String getMasterPort() {
		return (String.valueOf(MASTERPORT));
	}
	@Override
	public String getSlavePort() {
		return (String.valueOf(SLAVEPORT));
	}
	@Override
	public void setMasterPort(String port) {
		MASTERPORT = Integer.valueOf(port);
	}
	@Override
	public void setSlavePort(String port) {
		SLAVEPORT = Integer.valueOf(port);
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
			return bootNode.getHostAddress();
	}
	
	@Override
	public void setRemoteMaster(String remoteMaster) {
		try {
			bootNode = InetAddress.getByName(remoteMaster);
		} catch (UnknownHostException e) {
			System.out.println("Remote host unknown:"+remoteMaster);
		}
	}
	@Override
	public String getRemoteDirectory() {
			return remoteDirectory;
	}
	
	@Override
	public void setRemoteDirectory(String remoteDir) {
		remoteDirectory = remoteDir;
	}
	@Override
	public String toString() { return "CommandPacket boot:"+bootNode+" DB:"+fname+" trans:"+transport+" ports:"+MASTERPORT+","+SLAVEPORT; }
	
}
