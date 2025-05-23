package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

//import com.neocoretechs.rocksack.SerializedComparator;
//import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.ThreadPoolManager;
/**
 * This class functions as client to the RelatrixKVServer Worker threads located on a remote node.<p/>
 * On the client and server the following are present as conventions:<br/>
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT<br/>
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it<br/>
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it<br/>
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it<p/>
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the RelatrixServer. The client contacts the boot time server port, the desired database
 * is opened or the context of an open DB is passed back, and the client is handed the addresses of the master 
 * and slave ports that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 */
public class RelatrixKVClient extends RelatrixKVClientInterfaceImpl implements ClientInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // remoteNode is ignored and get getLocalHost is used
	public static boolean SHOWDUPEKEYEXCEPTION = false;
	
	private String bootNode, remoteNode;
	private int remotePort;
	
	protected int MASTERPORT = 9376; // master port, accepts connection from remote server
	protected int SLAVEPORT = 9377; // slave port, conects to remote, sends outbound requests to master port of remote
	
	protected InetAddress IPAddress = null; // remote server address
	private InetAddress localIPAddress = null; // local server address

	protected Socket workerSocket = null; // socket assigned to slave port
	protected ServerSocket masterSocket; // master socket connected back to via server
	protected Socket sock; // socker of mastersocket
	//private SocketAddress masterSocketAddress; // address of master
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	protected ConcurrentHashMap<String, RelatrixKVStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixKVStatement>();

	/**
	 * Start a Relatrix client to a remote server. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixKVClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			IPAddress = InetAddress.getByName(remoteNode);
		}
		if( DEBUG ) {
			System.out.println("RelatrixKVClient constructed with remote:"+IPAddress);
		}
		localIPAddress = InetAddress.getByName(bootNode);
		//
		// Wait for master server node to connect back to here for return channel communication
		//
		//masterSocketAddress = new InetSocketAddress(MASTERPORT);
		masterSocket = new ServerSocket(0, 1000, localIPAddress);
		MASTERPORT = masterSocket.getLocalPort();
		if(DEBUG) {
			System.out.printf("%s with arguments bootNode:%s remoteNode:%s remotePort:%d masterSocket:%s MASTERPORT:%d%n", this.getClass().getName(), bootNode, remoteNode, remotePort, masterSocket.toString(), MASTERPORT);
		}
		SLAVEPORT = remotePort;
		// send message to spin connection
		workerSocket = Fopen(bootNode);
		//masterSocket.bind(masterSocketAddress);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		ThreadPoolManager.getInstance().spin(this);
	}
	
	public String getLocalNode() {
		return bootNode;
	}
	
	public String getRemoteNode() {
		return remoteNode;
	}
	
	public int getRemotePort( ) {
		return remotePort;
	}
	/**
	* Set up the socket 
	 */
	@Override
	public void run() {
  	    //SocketChannel sock;
		try {
			sock = masterSocket.accept();
			sock.setKeepAlive(true);
			//sock.setTcpNoDelay(true);
			sock.setSendBufferSize(32767);
			sock.setReceiveBufferSize(32767);
			// At this point we have a connection back from 'slave'
		} catch (IOException e1) {
			System.out.println("RelatrixKVClient server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("RelatrixKVClient got connection "+sock);
  	    }
  	    try {
		  while(shouldRun ) {
				InputStream ins = sock.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(ins);
				RemoteResponseInterface iori = (RemoteResponseInterface) ois.readObject();
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				Object o = iori.getObjectReturn();
				if( o instanceof Throwable ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixKVClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					 o = ((Throwable)o).getCause();
				}
				RelatrixKVStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					ois.close();
					throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
				} else {
					// We have the request after its session round trip, get it from outstanding waiters and signal
					// set it with the response object
					if(o instanceof Iterator)
							((RemoteCompletionInterface)o).process();
					rs.setObjectReturn(o);
					// and signal the latch we have finished
					rs.signalCompletion(o);
				}
		  }
		} catch(Exception e) {
			if(!(e instanceof SocketException)) {
				// we lost the remote master, try to close worker and wait for reconnect
				e.printStackTrace();
				System.out.println(this.getClass().getName()+": receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
			}
		} finally {
			shutdown();
  	    }
  	    synchronized(waitHalt) {
  	    	waitHalt.notifyAll();
  	    }
	}
	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	public void send(RemoteRequestInterface iori) throws Exception {
		if(DEBUG) {
			System.out.println("Attempting to send "+iori+" to "+workerSocket);
			if(workerSocket != null)
				System.out.println("Socket bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
			else
				System.out.println("Socket NULL!");
		}
		outstandingRequests.put(iori.getSession(), (RelatrixKVStatement) iori);
		//if(DEBUG) {
		//	byte[] b = SerializedComparator.serializeObject(iori);
		//	System.out.println("Payload bytes="+b.length+" Put session "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
		//}
		ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
		if(DEBUG)
			System.out.println("Output stream "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
		oos.writeObject(iori);
		if(DEBUG)
			System.out.println("writeObject "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
		oos.flush();
		if(DEBUG)
			System.out.println(iori+" sent to "+workerSocket);
	}
	
	public void close() {
		shouldRun = false;
		try {
			sock.close();
		} catch (IOException e) {}
		sock = null;
		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException ie) {}
		}
		ThreadPoolManager.getInstance().shutdown(); // client threads
	}
	
	protected void shutdown() {
		if( sock != null ) {
			try {
				sock.close();
			} catch (IOException e) {}
		}
		if( workerSocket != null ) {
			try {
				workerSocket.close();
			} catch (IOException e2) {}
			workerSocket = null;
		}
		if( masterSocket != null ) {
			try {
				masterSocket.close();
			} catch (IOException e2) {}
			masterSocket = null;
		}
		shouldRun = false;
	}
	
	/**
	 * Call the remote server method to send a manually constructed command
	 * @param rs The RelatrixKvStatement manually constructed
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	@Override
	public Object sendCommand(RelatrixStatementInterface rs) throws Exception {
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCompletionObject(cdl);
		send(rs);
		cdl.await();
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof Exception)
			throw (Exception)o;
		return o;
	}
	
	//-------------------------------------------------------------------
	// Start of remote command sequence
	//-------------------------------------------------------------------
	
	public String getTablespace() {
		RelatrixKVStatement rs = new RelatrixKVStatement("getTableSpace",(Object[])null);
		try {
			return (String) sendCommand(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return Object of iteration, depends on iterator being used, typically, Map.Entry derived serializable instance of next element
	 */
	public Object next(RelatrixKVStatement rii) throws Exception {
		rii.methodName = "next";
		rii.paramArray = new Object[0];
		return sendCommand(rii);
	}
	
	public boolean hasNext(RelatrixKVStatement rii) throws Exception {
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return (boolean) sendCommand(rii);
	}
	
	public void remove(RelatrixKVStatement rii) throws Exception{
		rii.methodName = "remove";
		rii.paramArray = new Object[]{ rii.getObjectReturn() };
		sendCommand(rii);
	}
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	public void close(RelatrixKVStatement rii) throws Exception {
		rii.methodName = "close";
		rii.paramArray = new Object[0];
		sendCommand(rii);
	}
	
	/**
	 * Open a socket to the remote 
	 * @param fname
	 * @param remote remote database name
	 * @param port remote port
	 * @return
	 * @throws IOException
	 */
	public Socket Fopen(String bootNode) throws IOException {
		// send a remote Fopen request to the node
		// this consists of sending the running WorkBoot a message to start the worker for a particular
		// database on the node we hand down
		//if(workerSocket == null ) {
		//	workerSocketAddress = new InetSocketAddress(IPAddress, SLAVEPORT);
		//	workerSocket = new Socket();
		//	workerSocket.connect(workerSocketAddress);
		//}
		Socket s = new Socket(IPAddress, SLAVEPORT);
		s.setKeepAlive(true);
		s.setReceiveBufferSize(32767);
		s.setSendBufferSize(32767);
		System.out.println("Socket created to "+s);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		CommandPacketInterface cpi = new CommandPacket(bootNode, MASTERPORT);
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
		//os.close();
		//s.close();
		return s;
	}
	
	@Override
	public String toString() {
		return String.format("Key/Value server BootNode:%s RemoteNode:%s RemotePort:%d%n",bootNode, remoteNode, remotePort);
	}
	
	static int i = 0;
	/**
	 * case 4:
	 * <dd>Generic call to server: localaddr, remote addr, port, class
	 * <dd>Displays entry set stream of class from database running on addr and port
	 * <dd>case 5-8:
	 * <dd>Call to server method: localaddr, remote addr, port, server_method <arg1> <arg2> ... 
	 * <dd>Invokes named method on the server at host and port using the given string arguments.<p/>
	 * Note that method must accept the number of string arguments provided, such as loadClassFromJar <jar>
	 * and loadClassFromPath <package> <path> and removePackageFromRepository <package>.<p/>
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//RelatrixKVClient rc = new RelatrixKVClient("localhost","localhost", 9000);
		RelatrixKVStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		//rc.send(rs);
		i = 0;
		RelatrixKVClient rc = new RelatrixKVClient(args[0],args[1],Integer.parseInt(args[2]));

		switch(args.length) {
			case 4:
				Iterator it = (Iterator) rc.entrySet(Class.forName(args[3]));
				new RemoteStream(it).forEach(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				/*
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				*/
				System.exit(0);
			case 5:
				rs = new RelatrixKVStatement(args[3],args[4]);
				break;
			case 6:
				rs = new RelatrixKVStatement(args[3],args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixKVStatement(args[3],args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixKVStatement(args[3],args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		rc.sendCommand(rs);
		rc.close();
	}

	
}
