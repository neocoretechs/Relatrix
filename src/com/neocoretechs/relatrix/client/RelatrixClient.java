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
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.bigsack.io.ThreadPoolManager;
import com.neocoretechs.relatrix.DomainMapRange;
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
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private int MASTERPORT = 9876; // temp master port, accepts connection from remote server
	private int SLAVEPORT = 9877; // temp slave port, sends outbound requests to master port of remote
	
	private InetAddress IPAddress = null;

	private Socket workerSocket = null; // socket assigned to slave port
	private SocketAddress workerSocketAddress; //address of slave
	private ServerSocket masterSocket; // master socket connected back to via server
	private SocketAddress masterSocketAddress; // address of master

	private String DBName; // database remote name
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	private ConcurrentHashMap<String, RelatrixStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixStatement>();

	
	/**
	 * Start a relatrix client. Contact the boot time portion of server and queue a CommandPacket to open the desired
	 * database and get back the master and slave ports of the remote server. The main client thread then
	 * contacts the server master port, and the remote slave port contacts the master of the client. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param dbName The name of the remote DB in full qualified path form
	 * @param remote The remote database name for cluster server
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
		Fopen(dbName, null, false);
		//
		// Wait for master server node to connect back to here for return channel communication
		//
		masterSocketAddress = new InetSocketAddress(MASTERPORT);
		masterSocket = new ServerSocket();
		masterSocket.bind(masterSocketAddress);
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		ThreadPoolManager.getInstance().spin(this);
	}
	
	public RelatrixClient(String dbName, String remote, String bootNode, int bootPort)  throws IOException {
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
		Fopen(dbName, remote, false);
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
  	    Socket sock = null;
		try {
			sock = masterSocket.accept();
			sock.setKeepAlive(true);
			//sock.setTcpNoDelay(true);
			sock.setSendBufferSize(32767);
			sock.setReceiveBufferSize(32767);
			// At this point we have a connection back from 'slave'
		} catch (IOException e1) {
			System.out.println("RelatrixClient server socket accept failed with "+e1);
			if( sock != null ) {
				try {
					sock.close();
				} catch (IOException e) {}
			}
			try {
					workerSocket.close();
			} catch (IOException e2) {}
			try {
					masterSocket.close();
			} catch (IOException e2) {}
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
				 } else {
					 RelatrixStatement rs = outstandingRequests.get(iori.getSession());
					 if( rs == null ) {
						 System.out.println("REQUEST/RESPONSE MISMATCH, statement:"+iori);
					 } else {
						 // We have the request after its session round trip, get it from outstanding waiters and signal
						 // set it with the response object
						 rs.setObjectReturn(o);
						 // and signal the latch we have finished
						 rs.getCountDownLatch().countDown();
					 }
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
						Fopen(null, null, false);
						break;
					} catch (IOException e2) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {} // every 3 seconds
					}
				}
				// reached the WorkBoot to restart, set up accept
				try {
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
	      }	// shouldRun
		// requested shutdown from close()
		if( sock != null ) {
			try {
				sock.close();
			} catch (IOException e) {}
		}
		try {
				workerSocket.close();
		} catch (IOException e2) {}
		try {
				masterSocket.close();
		} catch (IOException e2) {}
		synchronized(waitHalt) {
				waitHalt.notify();
		}
	}
	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	public void send(RemoteRequestInterface iori) {
		try {
			if(workerSocket == null ) {
				workerSocketAddress = new InetSocketAddress(IPAddress, SLAVEPORT);
				workerSocket = new Socket();
				workerSocket.connect(workerSocketAddress);
				workerSocket.setKeepAlive(true);
				//workerSocket.setTcpNoDelay(true);
				workerSocket.setReceiveBufferSize(32767);
				workerSocket.setSendBufferSize(32767);
			}
			outstandingRequests.put(iori.getSession(), (RelatrixStatement) iori);
			ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
			oos.writeObject(iori);
			oos.flush();
		} catch (SocketException e) {
				System.out.println("Exception setting up socket to remote host:"+IPAddress+" port "+SLAVEPORT+" "+e);
		} catch (IOException e) {
				System.out.println("Socket send error "+e+" to address "+IPAddress+" on port "+SLAVEPORT);
		}
	}
	
	public void close() {
		synchronized(waitHalt) {
			try {
				shouldRun = false;
				masterSocket.close();
				waitHalt.wait();
			} catch (InterruptedException | IOException e) {}
		}
	}
	/**
	 * Verify that we are specifying a dir
	 * @param path
	 * @throws IOException
	 */
	public void setTablespaceDirectory(String path) throws IOException {
		RelatrixStatement rs = new RelatrixStatement("setTablespaceDirectory",path);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
	}
	
	/**
	 * We cant reasonably check the validity. Set the path to the remote directory that contains the
	 * BigSack tablespaces that comprise our database.
	 * @param path
	 * @throws IOException
	 */
	public void setRemoteDirectory(String path) {
		RelatrixStatement rs = new RelatrixStatement("setRemoteDirectory",path);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
	}
	

	/**
	 * Call the remote server method to store a morphism.
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * This is a standalone store in an atomic transparent transaction. Disallowed in transaction mode.
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity morphism relationship element - The DomainMapRange of stored object composed of d,m,r
	 */
	public DomainMapRange store(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException {
		RelatrixStatement rs = new RelatrixStatement("store",d, m, r);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
		return (DomainMapRange) rs.getObjectReturn();
	
	}
	/**
	 * Store our permutations of the identity morphism d,m,r each to its own index via tables of specific classes.
	 * This is a transactional store in the context of a previously initiated transaction.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation
	 * @param d The Comparable representing the domain object for this morphism relationship.
	 * @param m The Comparable representing the map object for this morphism relationship.
	 * @param r The Comparable representing the range or codomain object for this morphism relationship.
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 */
	public DomainMapRange transactionalStore(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException {
		RelatrixStatement rs = new RelatrixStatement("transactionalStore",d, m, r);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
		return (DomainMapRange) rs.getObjectReturn();
	}
	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	public void transactionCommit() throws IOException {
		RelatrixStatement rs = new RelatrixStatement("transactionCommit",new Object[0]);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
	}
	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	public void transactionRollback() throws IOException {
		RelatrixStatement rs = new RelatrixStatement("transactionRollback",new Object[0]);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
	}
	/**
	 * Take a check point of our current indicies. What this means is that we are
	 * going to write a log record such that if we crash will will restore the logs from that point forward.
	 * We have to have confidence that we are doing this at a legitimate point, so this should only be called if things are well
	 * and processing is proceeding normally. Its a way to say "start from here and go forward in time 
	 * if we crash, to restore the data to its state up to that point", hence check, point...
	 * If we are loading lots of data and we want to partially confirm it as part of the database, we do this.
	 * It does not perform a 'commit' because if we chose to do so we could start a roll forward recovery and restore
	 * even the old data before the checkpoint.
	 * @throws IOException
	 * @throws IllegalAccessException 
	 */
	public void transactionCheckpoint() throws IOException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("transactionCheckpoint",new Object[0]);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
	}
	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	*/
	public void remove(Comparable c) throws IOException
	{
		throw new RuntimeException("Not implemented yet");
	}
	/**
	 * Delete specific relationship and all relationships that it participates in
	 * @param d
	 * @param m
	 * @param r
	 */
	public void remove(Comparable d, Comparable m, Comparable r) {
		throw new RuntimeException("Not implemented yet");	
	}


	/**
	* Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	* matching the given set of operators and/or objects. Essentially this is the default permutation which
	* retrieves the equivalent of a tailSet and the parameters can be objects and/or operators. Semantically,
	* the other set-based retrievals make no sense without at least one object so in those methods that check is performed.
	* In support of the typed lambda calculus, When presented with 3 objects, the options are to return an identity composed of those 3 or
	* a set composed of identity elements matching the class of the template(s) in the argument(s)
	* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
	* In the special case of the all wildcard specification: findSet("*","*","*"), which will return all elements of the
	* domain->map->range relationships, or the case of findSet(object,object,object), which return one element matching the
	* relationships of the 3 objects, the returned elements(s) constitute identities in the sense of these morphisms satisfying
	* the requirement to be 'categorical'. In general, all 3 element arraysw return by the Cat->set representable operators are
	* the mathematical identity, or constitute the unique key in database terms.
	* @param darg Object for domain of relationship or a class template
	* @param marg Object for the map of relationship or a class template
	* @param rarg Object for the range of the relationship or a class template
	* @exception IOException low-level access or problems modifiying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	*/
	public RemoteTailsetIterator findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		return findTailSet(darg, marg, rarg);

	}

	/**
	* Retrieve from the targeted relationship those elements from the relationship to the end of relationships
	* matching the given set of operators and/or objects.
	* The parameters can be objects and/or operators. Semantically,
	* this set-based retrieval makes no sense without at least one object to supply a value to
	* work against, so in this method that check is performed.
	* In support of the typed lambda calculus, When presented with 3 objects, the options are to return a
	* a set composed of elements matching the class of the template(s) in the argument(s)
	* Legal permutations are [object],[object],[object] [TemplateClass],[TemplateClass],[TemplateClass]
	* @param darg Object for domain of relationship or a class template
	* @param marg Object for the map of relationship or a class template
	* @param rarg Object for the range of the relationship or a class template
	* @exception IOException low-level access or problems modifiying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	*/
	public RemoteTailsetIterator findTailSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixStatement rs = new RelatrixStatement("findSet",darg, marg, rarg);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
		return (RemoteTailsetIterator) rs.getObjectReturn();

	}

	/**
	 * Retrieve the given set of relationships from the start of the elements matching the operators and/or objects
	 * passed, to the given relationship, should the relationship contain an object as at least one of its components.
	 * Semantically,this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed.
	 * @param darg Domain of morphism
	 * @param marg Map of morphism relationship
	 * @param rarg Range or codomain or morphism relationship
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public RemoteHeadsetIterator findHeadSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixStatement rs = new RelatrixStatement("findHeadSet",darg, marg, rarg);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
		return (RemoteHeadsetIterator) rs.getObjectReturn();

	}
	/**
	 * Retrieve the subset of the given set of arguments from the point of the relationship of the first three
	 * arguments to the ending point of the associated variable number of parameters, which must match the number of objects
	 * passed in the first three arguments. If a passed argument in the first 3 parameters is neither "*" (wildcard)
	 * or "?" (return the object from the retrieved tuple morphism) then it is presumed to be an object.
	 * Semantically, this set-based retrieval makes no sense without at least one object to supply a value to
	 * work against, so in this method that check is performed.
	 * @param darg The domain of the relationship to retrieve
	 * @param marg The map of the relationship to retrieve
	 * @param rarg The range or codomain of the relationship
	 * @param endarg The variable arguments specifying the ending point of the relationship, must match number of actual objects in first 3 args
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public RemoteSubsetIterator findSubSet(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixStatement rs = new RelatrixStatement("findSubSet",darg, marg, rarg, endarg);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
		return (RemoteSubsetIterator) rs.getObjectReturn();

	}
	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return
	 */
	public Comparable[] next(RemoteObjectInterface rii) {
		((RelatrixStatement)rii).methodName = "next";
		((RelatrixStatement)rii).paramArray = new Object[0];
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		return (Comparable[])((RelatrixStatement)rii).getObjectReturn();

	}
	
	public boolean hasNext(RemoteObjectInterface rii) {
		((RelatrixStatement)rii).methodName = "hasNext";
		((RelatrixStatement)rii).paramArray = new Object[0];
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		return (boolean)((RelatrixStatement)rii).getObjectReturn();	
	}
	
	public void remove(RemoteObjectInterface rii) {
		((RelatrixStatement)rii).methodName = "remove";
		((RelatrixStatement)rii).paramArray = new Object[]{ ((RelatrixStatement)rii).getObjectReturn() };
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
	}
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	public void close(RemoteObjectInterface rii) {
		((RelatrixStatement)rii).methodName = "close";
		((RelatrixStatement)rii).paramArray = new Object[0];
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		outstandingRequests.remove(((RelatrixStatement)rii).getSession());
	}
	/**
	 * Open a socket to the remote worker located at 'remoteWorker' with the tablespace appended
	 * so each node is named [remoteWorker]0 [remoteWorker]1 etc
	 * @param fname
	 * @param create
	 * @return
	 * @throws IOException
	 */
	public boolean Fopen(String fname, String remote, boolean create) throws IOException {
		// send a remote Fopen request to the node
		// this consists of sending the running WorkBoot a message to start the worker for a particular
		// database on the node we hand down
		Socket s = new Socket(IPAddress, RelatrixServer.WORKBOOTPORT);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		CommandPacketInterface cpi = new CommandPacket(IPAddress, fname, remote, MASTERPORT, SLAVEPORT);
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
		RelatrixClient rc = new RelatrixClient("C:/Users/jg/Relatrix/AMI","/home/odroid/Relatrix", "devbox", 9000);
		RelatrixStatement rs = new RelatrixStatement("toString",(Object[])null);
		rc.send(rs);
	}
	
}
