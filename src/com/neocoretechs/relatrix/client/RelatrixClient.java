package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.ThreadPoolManager;
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
 * The client thread initiates with a CommandPacketInterface.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixClient implements Runnable, RelatrixClientInterface {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static boolean SHOWDUPEKEYEXCEPTION = true;
	
	private String bootNode, remoteNode;
	private int remotePort;
	
	private int MASTERPORT = 9876; // master port, accepts connection from remote server
	private int SLAVEPORT = 9877; // slave port, conects to remote, sends outbound requests to master port of remote
	
	private InetAddress IPAddress = null; // remote server address
	private InetAddress localIPAddress = null; // local server address

	private Socket workerSocket = null; // socket assigned to slave port
	private ServerSocket masterSocket; // master socket connected back to via server
	private Socket sock; // socket of mastersocket
	//private SocketAddress masterSocketAddress; // address of master
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	private ConcurrentHashMap<String, RelatrixStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixStatement>();

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
	public RelatrixClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
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
			System.out.println("RelatrixClient server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("RelatrixClient got connection "+sock);
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
				if( o instanceof Exception ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					 o = ((Throwable)o).getCause();
				}
				RelatrixStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					ois.close();
					throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
				} else {
					// We have the request after its session round trip, get it from outstanding waiters and signal
					// set it with the response object
					rs.setObjectReturn(o);
					// and signal the latch we have finished
					rs.getCountDownLatch().countDown();
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
	@Override
	public void send(RemoteRequestInterface iori) {
		try {
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
	
	@Override
	public Object sendCommand(RelatrixStatementInterface rs) throws DuplicateKeyException, IllegalAccessException, IOException {
		IndexResolver.setRemote(this);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof DuplicateKeyException)
			throw (DuplicateKeyException)o;
		else
			if(o instanceof IllegalAccessException)
				throw (IllegalAccessException)o;
			else
				if(o instanceof IOException)
					throw (IOException)o;
				else
				if(o instanceof Exception)
						throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return o;
	}
	
	@Override
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
	
	private void shutdown() {
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
	
	
	@Override
	public String getLocalNode() {
		return bootNode;
	}
	
	@Override
	public String getRemoteNode() {
		return remoteNode;
	}
	
	@Override
	public int getRemotePort( ) {
		return remotePort;
	}
	/**
	 * Get the last good DBKey from the DBKey table, which is the highest numbered last key delivered.
	 * @return The last good key
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	@Override
	public UUID getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		RelatrixStatement rs = new RelatrixStatement("getNewKey",(Object[])null);
		try {
			return (UUID) sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
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
	public DomainMapRange store(Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixStatement rs = new RelatrixStatement("store",d, m, r);
		return (DomainMapRange)sendCommand(rs);
	}

	@Override
	public Comparable firstKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("firstKey",clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object firstValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("firstValue",clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object get(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("get",key);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public Object getByIndex(DBKey key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("getByIndex",key);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Comparable lastKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("lastKey",clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object lastValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("lastValue",clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object store(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixStatement rs = new RelatrixStatement("store", k, v);
		return sendCommand(rs);
	}
	
	@Override
	public RemoteStream entrySetStream(Class<?> clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("entrySetStream",clazz);
		try {
			return (RemoteStream) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public RemoteKeySetIterator keySet(Class<String> clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("keySet",clazz);
		try {
			return (RemoteKeySetIterator) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	* @throws ClassNotFoundException 
	* @throws IllegalAccessException 
	*/
	@Override
	public Object remove(Comparable c) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("remove",c);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Delete specific relationship and all relationships that it participates in
	 * @param d
	 * @param m
	 * @param r
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 */
	public Object remove(Comparable d, Comparable m, Comparable r) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("remove",d,m,r);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
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
	public RemoteTailSetIterator findSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findSet",darg, marg, rarg);
		try {
			return (RemoteTailSetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSetStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findStream",darg, marg, rarg);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
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
	public RemoteTailSetIterator findTailSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findTailSet",darg, marg, rarg);
		try {
			return (RemoteTailSetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	public RemoteStream findTailSetStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findTailStream",darg, marg, rarg);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
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
	public RemoteHeadSetIterator findHeadSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findHeadSet",darg, marg, rarg);
		try {
			return (RemoteHeadSetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadSetStream(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findHeadStream",darg, marg, rarg);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}

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
	public RemoteSubSetIterator findSubSet(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findSubSet",darg, marg, rarg, endarg);
		try {
			return (RemoteSubSetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubSetStream(Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findSubStream",darg, marg, rarg, endarg);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return
	 */
	public Comparable[] next(RemoteObjectInterface rii) throws NoSuchElementException {
		((RelatrixStatement)rii).methodName = "next";
		((RelatrixStatement)rii).paramArray = new Object[0];
		try {
			return (Comparable[])sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | DuplicateKeyException | IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public boolean hasNext(RemoteObjectInterface rii) {
		((RelatrixStatement)rii).methodName = "hasNext";
		((RelatrixStatement)rii).paramArray = new Object[0];
		try {
			return (boolean)sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | DuplicateKeyException | IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	public void remove(RemoteObjectInterface rii) throws UnsupportedOperationException, IllegalStateException {
		((RelatrixStatement)rii).methodName = "remove";
		((RelatrixStatement)rii).paramArray = new Object[]{ ((RelatrixStatement)rii).getObjectReturn() };
		try {
			sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | DuplicateKeyException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	@Override
	public void close(RemoteObjectInterface rii) {
		((RelatrixStatement)rii).methodName = "close";
		((RelatrixStatement)rii).paramArray = new Object[0];
		try {
			sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | DuplicateKeyException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Open a socket to the remote worker located at IPAddress and SLAVEPORT using {@link CommandPacket} bootNode and MASTERPORT
	 * @param bootNode local MASTER node name to connect back to
	 * @return Opened socket
	 * @throws IOException
	 */
	@Override
	public Socket Fopen(String bootNode) throws IOException {
		Socket s = new Socket(IPAddress, SLAVEPORT);
		s.setKeepAlive(true);
		s.setReceiveBufferSize(32767);
		s.setSendBufferSize(32767);
		System.out.println("Socket created to "+s);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		CommandPacketInterface cpi = new CommandPacket(bootNode, MASTERPORT);
		os.writeObject(cpi);
		os.flush();
		return s;
	}
	
	@Override
	public String toString() {
		return String.format("Relatrix client BootNode:%s RemoteNode:%s RemotePort:%d%n",bootNode, remoteNode, remotePort);
	}
	

	@Override
	public DBKey get(String alias, Comparable instance) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("get",alias, instance);
		try {
			return (DBKey) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object remove(String alias, Comparable instance) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("remove",alias, instance);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	/**
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixClient rc = new RelatrixClient(args[0],args[1],Integer.parseInt(args[2]));
		RelatrixStatement rs = null;
		switch(args.length) {
			case 4:
				rs = new RelatrixStatement(args[3]);
				break;
			case 5:
				rs = new RelatrixStatement(args[3],args[4]);
				break;
			case 6:
				rs = new RelatrixStatement(args[3],args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixStatement(args[3],args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixStatement(args[3],args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		System.out.println(rc.sendCommand(rs));
		//rc.send(rs);
		rc.close();
	}
}
