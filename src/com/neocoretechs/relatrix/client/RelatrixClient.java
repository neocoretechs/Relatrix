package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.neocoretechs.bigsack.io.ThreadPoolManager;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.RelatrixServer;
/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
 * On the client and server the following are present as conventions:
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the RelatrixServer. The client contacts the boot time server port, the desired database
 * is opened or the context of an open DB is passed back, and the client is handed the addresses of the master 
 * and slave ports that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.
 * In general the remote directory is 
 * 'Database path + 'tablespace'+ tablespace# + tablename' where tablename is 'DBname+class+'.'+tablespace#'
 * so if your remote db path is /home/relatrix/AMI as passed to the server then its translation is:
 *  /home/relatrix/tablespace0/AMIcom.yourpack.yourclass.0
 * for the remote node 'AMI0', for others replace all '0' with '1' etc for other tablespaces.
 * @author jg
 * Copyright (C) NeoCoreTechs 2014,2015
 */
public class RelatrixClient implements Runnable {
	private static final boolean DEBUG = true;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private int MASTERPORT = 9876; // temp master port, accepts connection from remote server
	private int SLAVEPORT = 9877; // temp slave port, sends outbound requests to master port of remote
	
	private InetAddress IPAddress = null;

	private Socket workerSocket = null; // socket assigned to slave port
	private SocketAddress workerSocketAddress; //address of slave
	private ServerSocket masterSocket; // master socket connected back to via server
	private SocketAddress masterSocketAddress; // address of master

	private String DBName; // database remote name
	
	private boolean shouldRun = true; // master service thread control
	
	/**
	 * Start a relatrix client. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param dbName The name of the remote DB in full qualified path form
	 * @param bootNode The name of the remote server host
	 * @param bootPort Then name of the remote host port on which RelatrixServer is running
	 */
	public RelatrixClient(String dbName, String bootNode, int bootPort)  throws IOException {
		this.DBName = dbName;

		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			if( bootNode != null ) {
				IPAddress = InetAddress.getByName(bootNode);
			} else {
				throw new IOException("Boot node is null for RelatrixClient");
			}
		}
		if( DEBUG ) {
			System.out.println("RelatrixClient constructed with DB:"+DBName+" | WorkBoot:"+IPAddress);
		}
		// send message to spin up DB
		Fopen(dbName, false);
		//
		// Wait for master server node to connect back to here for return channel communication
		//
		masterSocketAddress = new InetSocketAddress(MASTERPORT);
		masterSocket = new ServerSocket();
		masterSocket.bind(masterSocketAddress);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		ThreadPoolManager.getInstance().spin(this);
	}
		

	public void setMasterPort(int port) {
		MASTERPORT = port;
	}
	public void setSlavePort(int port) {
		SLAVEPORT = port;
	}
	
	/**
	 * Look for messages coming back from the workers. Extract the UUID of the returned packet
	 * and get the real request from the ConcurrentHashTable buffer
	 */
	@Override
	public void run() {
  	    //SocketChannel sock;
  	    Socket sock;
		try {
			sock = masterSocket.accept();
			sock.setKeepAlive(true);
			//sock.setTcpNoDelay(true);
			sock.setSendBufferSize(32767);
			sock.setReceiveBufferSize(32767);
			// At this point we have a connection back from 'slave'
		} catch (IOException e1) {
			System.out.println("RelatrixClient server socket accept failed with "+e1);
			return;
		}
  	     if( DEBUG ) {
  	    	 System.out.println("RelatrixClient got connection "+sock);
  	     }
		while(shouldRun ) {
			try {

				InputStream ins = sock.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(ins);
				RemoteResponseInterface iori = (RemoteResponseInterface) ois.readObject();
				 // get the original request from the stored table
				 if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				 Object o = iori.getObjectReturn();
				 if( o instanceof Exception ) {
					 System.out.println("RelatrixClient: ******** REMOTE EXCEPTION ******** "+o);
				 }
			} catch (SocketException e) {
					System.out.println("RelatrixClient: receive socket error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
					break;
			} catch (IOException e) {
				// we lost the remote, try to close worker and wait for reconnect
				System.out.println("RelatrixClient: receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				if(workerSocket != null ) {
					try {
						workerSocket.close();
					} catch (IOException e1) {}
					workerSocket = null;
				}
				// re-establish master slave connect
				if(!masterSocket.isClosed())
					try {
						masterSocket.close();
					} catch (IOException e2) {}
				try {
					masterSocket = new ServerSocket();
					masterSocket.bind(masterSocketAddress);
				} catch (IOException e3) {
					System.out.println("RelatrixClient standard server socket RETRY channel open failed with "+e3+" THIS NODE IST KAPUT!");
					return;
				}
				// We have done everything we can to close all open channels, now try to re-open them
				// Wait in loop contacting WorkBoot until it somehow re-animates, most likely 
				// through human intervention
				while(true) {
					try {
						Fopen(null, false);
						break;
					} catch (IOException e2) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {} // every 3 seconds
					}
				}
				// reached the WorkBoot to restart, set up accept
				try {
					//sock = masterSocketChannel.accept();
					sock = masterSocket.accept();
				} catch (IOException e1) {
						System.out.println("RelatrixClient RETRY accept failed with "+e1+" Remote node can not be reached!");
						return;
				}
			  	if( DEBUG ) {
			  		System.out.println("RelatrixClient got Reconnection "+sock);
			  	}
			} catch (ClassNotFoundException e1) {
				System.out.println("RelatrixClient class not found for deserialization "+e1+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				break;
			}
	      }	
	}
	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	public void send(RemoteRequestInterface iori) {
	    byte[] sendData;
		try {
			/*
			if(workerSocketChannel == null ) {
				workerSocketAddress = new InetSocketAddress(IPAddress, SLAVEPORT);
				workerSocketChannel = SocketChannel.open(workerSocketAddress);
				workerSocketChannel.configureBlocking(true);
				workerSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				workerSocketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
				workerSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 32767);
				workerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
			}
			*/
			if(workerSocket == null ) {
				workerSocketAddress = new InetSocketAddress(IPAddress, SLAVEPORT);
				workerSocket = new Socket();
				workerSocket.connect(workerSocketAddress);
				workerSocket.setKeepAlive(true);
				//workerSocket.setTcpNoDelay(true);
				workerSocket.setReceiveBufferSize(32767);
				workerSocket.setSendBufferSize(32767);
			}
			//sendData = GlobalDBIO.getObjectAsBytes(iori);
			//ByteBuffer srcs = ByteBuffer.wrap(sendData);
			//workerSocketChannel.write(srcs);
			ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
			oos.writeObject(iori);
			oos.flush();
		} catch (SocketException e) {
				System.out.println("Exception setting up socket to remote host:"+IPAddress+" port "+SLAVEPORT+" "+e);
		} catch (IOException e) {
				System.out.println("Socket send error "+e+" to address "+IPAddress+" on port "+SLAVEPORT);
		}
	}
	
	/**
	 * Open a socket to the remote worker located at 'remoteWorker' with the tablespace appended
	 * so each node is named [remoteWorker]0 [remoteWorker]1 etc
	 * @param fname
	 * @param create
	 * @return
	 * @throws IOException
	 */
	public boolean Fopen(String fname, boolean create) throws IOException {
		// send a remote Fopen request to the node
		// this consists of sending the running WorkBoot a message to start the worker for a particular
		// database on the node we hand down
		Socket s = new Socket(IPAddress, RelatrixServer.WORKBOOTPORT);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		CommandPacketInterface cpi = new CommandPacket(IPAddress, fname, MASTERPORT, SLAVEPORT);
		/*
		if( remoteDBName != null )
			cpi.setDatabase(remoteDBName);
		else
			cpi.setDatabase(DBName);
		cpi.setMasterPort(String.valueOf(MASTERPORT));
		cpi.setSlavePort(String.valueOf(SLAVEPORT));
		cpi.setRemoteMaster(InetAddress.getLocalHost().getHostAddress());
		cpi.setTransport("TCP");
		*/
		os.writeObject(cpi);
		os.flush();
		os.close();
		s.close();
		return true;
	}

	public void setSlavePort(String port) {
		SLAVEPORT = Integer.valueOf(port);
	}

	public void setMasterPort(String port) {
		MASTERPORT = Integer.valueOf(port);
		
	}
	
	public static void main(String[] args) throws Exception {
		RelatrixClient rc = new RelatrixClient("C:/Users/jg/Relatrix/AMI", "devbox", 9000);
		RelatrixStatement rs = new RelatrixStatement("tostring",(Object[])null);
		rc.send(rs);
	}
	
}
