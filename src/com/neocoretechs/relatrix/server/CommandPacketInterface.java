package com.neocoretechs.relatrix.server;

import java.io.Serializable;

import com.neocoretechs.relatrix.client.RelatrixStatement;
/**
 * Command packet interface bound for WorkBoot nodes to activate threads
 * to operate on a specific port, tablespace, and database, all determined by master node
 * controller
 * @author jg
 *
 */
public interface CommandPacketInterface extends Serializable {
	public String getDatabase();
	public void setDatabase(String database);
	public String getMasterPort();
	public String getSlavePort();
	public void setMasterPort(String port);
	public void setSlavePort(String port);
	public String getTransport();
	public void setTransport(String transport);
	public String getRemoteMaster();
	public void setRemoteMaster(String remoteMaster);
	
	// RelatrixStatement contains linkage to Relatrix method to be called
	public RelatrixStatement getExecutableStatement();
	public void setExecutableStatement(RelatrixStatement rs);
}
