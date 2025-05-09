package com.neocoretechs.relatrix.client.asynch;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.RelatrixClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatement;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.CircularBlockingDeque;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.ThreadPoolManager;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixTransactionServer} 
 * Worker threads located on a remote node. It carries the transaction identifier to maintain transaction context.
 * On the client and server the following are present as conventions:<br/>
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT<br/>
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it<br/>
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it<br/>
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it<p/>
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is the {@link RelatrixTransactionServer}. The client contacts the boot time server port, the desired database
 * is opened or the context of an open DB is passed back, and the client is handed the addresses of the master 
 * and slave ports that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.<p/>
 *
 * In a transaction context, we must obtain a transaction Id from the server for the lifecycle of the transaction.<p/>
 * The transaction Id may outlive the session, as the session is transitory for communication purposes.
 * The {@link RelatrixTransactionStatement} contains the transaction Id.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class AsynchRelatrixClientTransaction extends AsynchRelatrixClientTransactionInterfaceImpl implements AsynchRelatrixClientTransactionInterface, AsynchClientTransactionInterface,Runnable {
	private static final boolean DEBUG = true;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static final int REQUEST_QUEUE = 1024;
	
	protected CircularBlockingDeque<RelatrixTransactionStatementInterface> queuedRequests = new CircularBlockingDeque<RelatrixTransactionStatementInterface>(REQUEST_QUEUE);
	private String bootNode, remoteNode;
	private int remotePort;
	
	protected int MASTERPORT = 9876; // master port, accepts connection from remote server
	protected int SLAVEPORT = 9877; // slave port, conects to remote, sends outbound requests to master port of remote
	
	protected InetAddress IPAddress = null; // remote server address
	private InetAddress localIPAddress = null; // local server address

	protected Socket workerSocket = null; // socket assigned to slave port
	protected ServerSocket masterSocket; // master socket connected back to via server
	protected Socket sock; // socket of mastersocket
	//private SocketAddress masterSocketAddress; // address of master
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 

	public AsynchRelatrixClientTransaction() { }
	
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
	public AsynchRelatrixClientTransaction(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		IndexResolver.setRemoteTransaction((AsynchRelatrixClientTransactionInterface) this);
		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			IPAddress = InetAddress.getByName(remoteNode);
		}
		if( DEBUG ) {
			System.out.println("AsynchRelatrixClientTransaction constructed with remote:"+IPAddress);
		}
		localIPAddress = InetAddress.getByName(bootNode);
		//
 		// Wait for master server node to connect back to here for return channel communication
		//
		//masterSocketAddress = new InetSocketAddress(MASTERPORT);
		masterSocket = new ServerSocket(0, 1000, localIPAddress);
		MASTERPORT = masterSocket.getLocalPort();
		SLAVEPORT = remotePort;
		// send message to spin connection
		workerSocket = Fopen(bootNode);
		//masterSocket.bind(masterSocketAddress);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		ThreadPoolManager.getInstance().spin(this);
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
			System.out.println("AsynchRelatrixClientTransaction server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("AsynchRelatrixClientTransaction got connection "+sock);
  	    }
  	    try {
  	    	while(shouldRun ) {
  	    		RelatrixTransactionStatementInterface rs = queuedRequests.takeFirst();
  	    		CompletableFuture<Object> cf = (CompletableFuture<Object>) rs.getCompletionObject();
  	    		ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
  	    		oos.writeObject(rs);
  	    		oos.flush();
  	    		InputStream ins = sock.getInputStream();
  	    		ObjectInputStream ois = new ObjectInputStream(ins);
  	    		RemoteResponseInterface iori = (RemoteResponseInterface) ois.readObject();
  	    		// get the original request from the stored table
  	    		if( DEBUG )
  	    			System.out.println("Asynch FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
  	    		Object o = iori.getObjectReturn();
  	    		if( o instanceof Throwable ) {
  	    			System.out.println("AsynchRelatrixClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
  	    			o = ((Throwable)o).getCause();
  	    			cf.completeExceptionally((Throwable) o);
  	    		} else {
  	    			if(o instanceof Iterator)
  	    				((RemoteCompletionInterface)o).process();
  		    		cf.complete(o);
  	    		}
  	    		// We have the request after its session round trip, get it from outstanding waiters and signal
  	    		// set it with the response object
  	    		rs.setObjectReturn(o);
  	    		// and signal the latch we have finished
  	    		rs.signalCompletion(o);
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
	 * Queue a command to the blocking deque. Its a circular deque, so once capacity is reach, oldest requests are overwritten
	 */
	@Override
	public CompletableFuture<Object> queueCommand(RelatrixTransactionStatementInterface rs) {
		CompletableFuture<Object> cf = new CompletableFuture<>();
		rs.setCompletionObject(cf);
		queuedRequests.addLast(rs);
		return cf;
	}

	/**
	 * Open a socket to the remote worker located at IPAddress and SLAVEPORT using {@link CommandPacket} bootNode and MASTERPORT
	 * @param bootNode local MASTER node name to connect back to
	 * @return Open Socket
	 * @throws IOException
	 */

	public Socket Fopen(String bootNode) throws IOException {
		Socket s = new Socket(IPAddress, SLAVEPORT);
		s.setKeepAlive(true);
		s.setReceiveBufferSize(32767);
		s.setSendBufferSize(32767);
		if(DEBUG)
			System.out.println(this.getClass().getName()+" Socket created to "+s);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		CommandPacketInterface cpi = new CommandPacket(bootNode, MASTERPORT);
		os.writeObject(cpi);
		os.flush();
		return s;
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
	 * Called from the {@link RemoteIteratorTransaction} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param xid Transaction Id
	 * @param rii RelatrixTransactionStatement
	 * @return The next iterated object or null
	 */
	public CompletableFuture<Object> next(RelatrixTransactionStatement rii) throws Exception {
		rii.methodName = "next";
		rii.paramArray = new Object[0];
		return queueCommand(rii);
	}
	/**
	 * Called from the {@link RemoteIteratorTransaction} for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param xid Transaction Id
	 * @param rii RelatrixTransactionStatement
	 * @return The boolean result of hasNext on server
	 */	
	public CompletableFuture<Object> hasNext(RelatrixTransactionStatement rii) throws Exception {
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return queueCommand(rii);
	}

	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	public void close(RelatrixTransactionStatement rii) throws Exception {
		rii.methodName = "close";
		rii.paramArray = new Object[0];
		queueCommand(rii);
	}
	
	@Override
	public String toString() {
		return String.format("%s BootNode:%s RemoteNode:%s RemotePort:%d input socket:%s output socket%s%n",this.getClass().getName(), remoteNode, remotePort, sock, workerSocket);
	}

	static int i = 0;
	/**
	 * Generic call to server localaddr, remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args local node, remote server, remote server port, className for entrySet or (method, argument, argument, argument...) 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AsynchRelatrixClientTransaction rc = new AsynchRelatrixClientTransaction(args[0],args[1],Integer.parseInt(args[2]));
		TransactionId xid = rc.getTransactionId();
		RelatrixTransactionStatement rs = null;
		switch(args.length) {
			case 4:
				System.out.println("queueing..");
				CompletableFuture<Iterator> cit = rc.entrySet(xid,Class.forName(args[3]));
				long tim = System.nanoTime();
				Iterator it = cit.get();
				System.out.println("Iterator return from future took:"+(System.nanoTime()-tim)+"ns.");
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);				
				break;
			case 5:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4]);
				break;
			case 6:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixTransactionStatement(args[3],xid,args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		System.out.println("queueing "+rs);
		CompletableFuture<?> cf = rc.queueCommand(rs);
		System.out.println("Command queued...");
		long tim = System.nanoTime();
		System.out.println("Return from future:"+cf.get()+" took:"+(System.nanoTime()-tim)+"ns.");
		rc.endTransaction(xid);
		rc.close();
	}

}
