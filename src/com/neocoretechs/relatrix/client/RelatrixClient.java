package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.arieslogger.core.impl.LogToFile;
import com.neocoretechs.bigsack.io.pooled.Datablock;
import com.neocoretechs.bigsack.io.pooled.GlobalDBIO;
import com.neocoretechs.bigsack.io.request.IoRequestInterface;
import com.neocoretechs.bigsack.io.request.IoResponseInterface;
import com.neocoretechs.bigsack.io.request.cluster.CompletionLatchInterface;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
 * In general the remote directory is 
 * 'Database path + 'tablespace'+ tablespace# + tablename' where tablename is 'DBname+class+'.'+tablespace#'
 * so if your remote db path is /home/relatrix/AMI as passed to the server then its translation is:
 *  /home/relatrix/tablespace0/AMIcom.yourpack.yourclass.0
 * for the remote node 'AMI0', for others replace all '0' with '1' etc for other tablespaces.
 * @author jg
 * Copyright (C) NeoCoreTechs 2014,2015
 */
public class RelatrixClient implements Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private int MASTERPORT = 9876;
	private int SLAVEPORT = 9876;
	private int WORKBOOTPORT = 8000;
	private static String remoteWorker = "AMI";
	private InetAddress IPAddress = null;

	private SocketChannel workerSocketChannel = null;
	private Socket workerSocket = null;
	
	private SocketAddress workerSocketAddress;
	
	private ServerSocketChannel masterSocketChannel;
	private ServerSocket masterSocket;
	
	private SocketAddress masterSocketAddress;
	ByteBuffer b = ByteBuffer.allocate(LogToFile.DEFAULT_LOG_BUFFER_SIZE);

	private String DBName;
	private int tablespace;
	private String remoteDBName = null; // if not null, alternate database name for remote worker nodes with specific directory
	
	private boolean shouldRun = true;
	
	private ConcurrentHashMap<Integer, IoRequestInterface> requestContext;
	
	/**
	 * Start a master cluster node. The database, tablespace, and listener port are assigned
	 * by the respective IO manager. The request queue and mapping from request id to original request hashmap
	 * are passed again by the respective IO manager. These masters are one-to-one tablespace and database and worker
	 * on the remote node. The masters all run on the main cluster node.
	 * @param dbName
	 * @param tablespace
	 * @param port
	 * @param requestQueue
	 * @param requestContext
	 * @throws IOException
	 */
	public RelatrixClient(String dbName, int tablespace, int masterPort, int slavePort, String bootNode, int bootPort, ConcurrentHashMap<Integer, IoRequestInterface> requestContext)  throws IOException {
		this.DBName = dbName;
		this.tablespace = tablespace;
		this.MASTERPORT = masterPort;
		this.SLAVEPORT = slavePort;
		this.requestContext = requestContext;
		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			if( bootNode != null ) {
				IPAddress = InetAddress.getByName(bootNode);
				WORKBOOTPORT = bootPort;
			} else {
				IPAddress = InetAddress.getByName(remoteWorker+String.valueOf(tablespace));
			}
		}
		if( DEBUG ) {
			System.out.println("TCPMaster constructed with "+DBName+" for tablspace "+tablespace+
								" master port:"+masterPort+" slave:"+slavePort+" to contact WorkBoot "+IPAddress);
		}
		masterSocketAddress = new InetSocketAddress(MASTERPORT);
		//masterSocketChannel = ServerSocketChannel.open();
		//masterSocketChannel.configureBlocking(true);
		//masterSocketChannel.bind(masterSocketAddress);	
		masterSocket = new ServerSocket();
		masterSocket.bind(masterSocketAddress);
	}
	/**
	 * Specify an alternate remote DB name and directory for the current database.
	 * Primary usage is for nodes with OSs different from the master
	 * @param dbName
	 * @param remoteDBName
	 * @param tablespace
	 * @param masterPort
	 * @param slavePort
	 * @param requestContext
	 * @throws IOException
	 */
	public RelatrixClient(String dbName, String remoteDBName, int tablespace, int masterPort, int slavePort, String bootNode, int bootPort,  ConcurrentHashMap<Integer, IoRequestInterface> requestContext)  throws IOException {
		this(dbName, tablespace, masterPort, slavePort, bootNode, bootPort, requestContext);
		this.remoteDBName = remoteDBName;
		if( DEBUG )
			System.out.println("TCPMaster constructed with "+dbName+" using remote DB:"+remoteDBName+" tablespace:"+tablespace+" master:"+masterPort+" slave:"+slavePort);
	}
		
	/**
	 * Set the prefix name of the remote worker node that this master communicates with
	 * This name plus the tablespace identifies each individual worker node
	 * In test mode, the local host is used for workers and master
	 * @param rname
	 */
	public void setRemoteWorkerName(String rname) {
		remoteWorker = rname;
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
			//sock = masterSocketChannel.accept();
			//sock.configureBlocking(true);
			//sock.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			//sock.setOption(StandardSocketOptions.TCP_NODELAY, true);
			//sock.setOption(StandardSocketOptions.SO_SNDBUF, 32767);
			//sock.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
			sock = masterSocket.accept();
			sock.setKeepAlive(true);
			//sock.setTcpNoDelay(true);
			sock.setSendBufferSize(32767);
			sock.setReceiveBufferSize(32767);
		} catch (IOException e1) {
			System.out.println("TCPMaster server socket accept failed with "+e1);
			return;
		}
  	     if( DEBUG ) {
  	    	 System.out.println("TCPMaster got connection "+sock);
  	     }
		while(shouldRun ) {
			try {
				//sock.read(b);
				//IoResponseInterface iori = (IoResponseInterface) GlobalDBIO.deserializeObject(b);
				//b.clear();
				InputStream ins = sock.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(ins);
				IoResponseInterface iori = (IoResponseInterface) ois.readObject();
				 // get the original request from the stored table
				 IoRequestInterface ior = requestContext.get(iori.getUUID());
				 if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				 //
				 // If we detect a request that has not correspondence in the table of requests issued
				 // then the request is a duplicate of some sort of corruption has occurred. If in debug, log, dump
				 // table of current requests, and ignore
				 //
				 if( DEBUG ) {
					 System.out.println("Extracting latch from original request:"+ior);
					 if( ior == null ) {
						 Set<Entry<Integer, IoRequestInterface>> e = requestContext.entrySet();
						 System.out.println("TCPMaster ******* INBOUND REQUEST DOES NOT VERIFY *******\r\nDump context table, size:"+requestContext.size());
						 Iterator<Entry<Integer, IoRequestInterface>> ei = e.iterator();
						 while(ei.hasNext()) {
							 Entry<Integer, IoRequestInterface> ein = ei.next();
							 System.out.println("Request #: "+ein.getKey()+" val:"+ein.getValue());
						 }
						 break;
					 }   	 
				 }
				 // set the return values in the original request to our values from remote workers
				 ((CompletionLatchInterface)ior).setLongReturn(iori.getLongReturn());
				 Object o = iori.getObjectReturn();
				 if( o instanceof Exception ) {
					 System.out.println("TCPMaster: ******** REMOTE EXCEPTION ******** "+o);
				 }
				 ((CompletionLatchInterface)ior).setObjectReturn(o);
				 if( DEBUG ) {
					 System.out.println("TCPMaster ready to count down latch with "+ior);
				 }
				 // now add to any latches awaiting
				 CountDownLatch cdl = ((CompletionLatchInterface)ior).getCountDownLatch();
				 cdl.countDown();
			} catch (SocketException e) {
					System.out.println("TCPMaster receive socket error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
					break;
			} catch (IOException e) {
				// we lost the remote, try to close worker and wait for reconnect
				System.out.println("TCPMaster receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				/*
				if(workerSocketChannel != null ) {
						try {
							workerSocketChannel.close();
						} catch (IOException e1) {}
						workerSocketChannel = null;
				}
				// re-establish master slave connect
				if(masterSocketChannel.isOpen())
					try {
						masterSocketChannel.close();
					} catch (IOException e2) {}
				try {
					masterSocketChannel = ServerSocketChannel.open();
					masterSocketChannel.configureBlocking(true);
					masterSocketChannel.bind(masterSocketAddress);
				} catch (IOException e3) {
					System.out.println("TCPMaster server socket RETRY channel open failed with "+e3+" THIS NODE IST KAPUT!");
					return;
				}
				*/
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
					System.out.println("TCPMaster standard server socket RETRY channel open failed with "+e3+" THIS NODE IST KAPUT!");
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
						System.out.println("TCPMaster server socket RETRY accept failed with "+e1+" THIS NODE IST KAPUT!");
						return;
				}
			  	if( DEBUG ) {
			  		System.out.println("TCPMaster got RE-connection "+sock);
			  	}
			} catch (ClassNotFoundException e1) {
				System.out.println("TCPMaster class not found for deserialization "+e1+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				break;
			}
	      }	
	}
	/**
	 * Send request to remote worker
	 * @param iori
	 */
	public void send(IoRequestInterface iori) {
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
				System.out.println("Exception setting up socket to remote worker:"+IPAddress+" port "+SLAVEPORT+" "+e);
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
		// database and tablespace and the node we hand down
		Socket s = new Socket(IPAddress, WORKBOOTPORT);
		OutputStream os = s.getOutputStream();
		CommandPacketInterface cpi = new CommandPacketInterface() {
			private static final long serialVersionUID = 1L;
			String fname;
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
			}
			@Override
			public void setSlavePort(String port) {
			}
			@Override
			public String getTransport() {
				return "TCP";
			}
			@Override
			public void setTransport(String transport) {		
			}
			@Override
			public String getRemoteMaster() {
				try {
					return InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					throw new RuntimeException(e);
				}
			}
			@Override
			public void setRemoteMaster(String remoteMaster) {
			}

			@Override
			public RelatrixStatement getExecutableStatement() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public void setExecutableStatement(RelatrixStatement rs) {
				// TODO Auto-generated method stub
				
			}
		};
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
		os.write(GlobalDBIO.getObjectAsBytes(cpi));
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
	
}
