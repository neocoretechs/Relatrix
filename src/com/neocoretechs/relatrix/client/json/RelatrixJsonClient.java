package com.neocoretechs.relatrix.client.json;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.ClientNonTransactionInterface;
import com.neocoretechs.relatrix.client.ClientTransactionInterface;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.ThreadPoolManager;
import com.neocoretechs.rocksack.Alias;

/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
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
 * The client thread initiates with a CommandPacketInterface.<p/>
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixJsonClient extends RelatrixJsonClientInterfaceImpl implements ClientNonTransactionInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	private boolean SHOWDUPEKEYEXCEPTION = true;
	
	private String bootNode, remoteNode;
	private int remotePort;
	
	protected int MASTERPORT = 9880; // master port, accepts connection from remote server
	protected int SLAVEPORT = 9881; // slave port, conects to remote, sends outbound requests to master port of remote
	
	protected InetAddress IPAddress = null; // remote server address
	private InetAddress localIPAddress = null; // local server address

	protected Socket workerSocket = null; // socket assigned to slave port
	protected ServerSocket masterSocket; // master socket connected back to via server
	protected Socket sock; // socket of mastersocket
	//private SocketAddress masterSocketAddress; // address of master
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	protected ConcurrentHashMap<String, RelatrixJsonStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixJsonStatement>();

	/**
	 * Start a Relatrix client to a remote server. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode Name of local master socket to coonect back to
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixJsonClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		IndexResolver.setRemote(this);
		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			IPAddress = InetAddress.getByName(remoteNode);
		}
		if( DEBUG ) {
			System.out.println("RelatrixClient constructed with remote:"+IPAddress);
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

	
	public Object sendCommand(RelatrixStatementInterface rs) throws Exception {
		IndexResolver.setRemote((RelatrixClientInterface) this);
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
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The next iterated object or null
	 */
	public Object next(RelatrixJsonStatement rii) throws Exception {
		rii.methodName = "next";
		rii.paramArray = new Object[0];
		return sendCommand(rii);
	}
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The boolean result of hasNext on server
	 */	
	public boolean hasNext(RelatrixJsonStatement rii) throws Exception {
		rii.methodName = "hasNext";
		rii.paramArray = new Object[0];
		return (boolean) sendCommand(rii);
	}
	
	public void close() {
		shouldRun = false;
		try {
			if(sock != null)
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

	
	public void closeDb(Class clazz) throws Exception {
		RelatrixJsonStatement rs = new RelatrixJsonStatement("close", clazz);
		sendCommand(rs);
	}
	
	public void closeDb(String alias, Class clazz) throws Exception {
		RelatrixJsonStatement rs = new RelatrixJsonStatement("close", alias, clazz);
		sendCommand(rs);
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
			System.out.println("RelatrixJsonClient server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("RelatrixJsonClient got connection "+sock);
  	    }
  	    try {
		  while(shouldRun ) {
				InputStream ins = sock.getInputStream();
				if(DEBUG)
					System.out.println("RelatrixJsonClient "+sock+" bound:"+sock.isBound()+" closed:"+sock.isClosed()+" connected:"+sock.isConnected()+" input shut:"+sock.isInputShutdown()+" output shut:"+sock.isOutputShutdown());
				//ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//while(true) {
				//  int n = ins.read(buf);
				//  if( n < 0 ) break;
				//  baos.write(buf,0,n);
				//}
				BufferedReader in = new BufferedReader(new InputStreamReader(ins));
				JSONObject jobj = new JSONObject(in.readLine());
				RemoteResponseInterface iori = (RemoteResponseInterface) jobj.toObject();//, RemoteResponseInterface.class);
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				Object o = iori.getObjectReturn();
				if( DEBUG )
					 System.out.println("FROM Remote, returned object from response:"+o+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				if( o instanceof Throwable ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixJsonClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					 o = ((Throwable)o).getCause();
				} else {
		    		Class<?> returnClass = Class.forName(iori.getReturnClass());
  	    			if(returnClass != o.getClass()) {
 	    				// if exception was thrown, returnClass should be throwable
  	    				if(Throwable.class.isAssignableFrom(returnClass)) {
  	    					try {
  	    						Constructor co = returnClass.getConstructor(o.getClass());
  	    						o = co.newInstance(o);
  	    						throw new Exception((String)((Throwable)o).getMessage());
  	    					} catch(Exception oe) {
  	    						System.out.println("RelatrixJsonClient: ******** REMOTE EXCEPTION ******** "+oe);
  	    						o = oe;
  	    					}
  	    				} else {
  	    					// class mismatch of non Throwable variety, we my have a hashmap of values
  	    					if(o instanceof HashMap) {
  	    						JSONObject jo = (JSONObject) JSONObject.wrap(o);
  	    						o = jo.toObject(returnClass);
  	    					}
  	    				}
  	    			}
  	    		}
				RelatrixStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					in.close();
					ins.close();
					throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
				} else {
					if(o instanceof Iterator)
						((RemoteCompletionInterface)o).process();
					// We have the request after its session round trip, get it from outstanding waiters and signal
					// set it with the response object
					rs.setObjectReturn(o);
					// and signal the latch we have finished
					rs.signalCompletion(o);
				}
		  }
		} catch(Exception e) {
			//if(!(e instanceof SocketException)) {
				// we lost the remote master, try to close worker and wait for reconnect
				e.printStackTrace();
				System.out.println(this.getClass().getName()+": receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
			//}
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
		outstandingRequests.put(iori.getSession(), (RelatrixJsonStatement) iori);
		String iorij = JSONObject.toJson(iori);
		OutputStream os = workerSocket.getOutputStream();
		os.write(iorij.getBytes());
		os.flush();
	}

	/**
	 * Open a socket to the remote worker located at IPAddress and SLAVEPORT using {@link CommandPacket} bootNode and MASTERPORT
	 * @param bootNode local MASTER node name to connect back to
	 * @return Opened socket
	 * @throws IOException
	 */
	public Socket Fopen(String bootNode) throws IOException {
		Socket s = new Socket(IPAddress, SLAVEPORT);
		s.setKeepAlive(true);
		s.setReceiveBufferSize(32767);
		s.setSendBufferSize(32767);
		System.out.println("Socket created to "+s);
		CommandPacketInterface cpi = new CommandPacket(bootNode, MASTERPORT);
		String cpij = JSONObject.toJson(cpi);
		OutputStream os = s.getOutputStream();
		os.write(cpij.getBytes());
		os.flush();
		return s;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	static int i = 0;
	/**
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixJsonClient rc = new RelatrixJsonClient(args[0],args[1],Integer.parseInt(args[2]));
		RelatrixStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);
			case 5:
				rs = new RelatrixJsonStatement(args[3],args[4]);
				break;
			case 6:
				rs = new RelatrixJsonStatement(args[3],args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixJsonStatement(args[3],args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixJsonStatement(args[3],args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		System.out.println(rc.sendCommand(rs));
		//rc.send(rs);
		rc.close();
	}


	@Override
	public List findSetParallel(Alias arg1, Character arg2, List arg3, Character arg4) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List findSetParallel(Alias arg1, List arg2, Character arg3, Character arg4) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List findSetParallel(Alias arg1, Character arg2, Character arg3, List arg4) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List findSetParallel(Character arg1, Character arg2, List arg3) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List findSetParallel(Character arg1, List arg2, Character arg3) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List findSetParallel(List arg1, Character arg2, Character arg3) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
