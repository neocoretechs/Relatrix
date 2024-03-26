package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.DatabaseCatalog;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.RelatrixIndex;
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
		IndexResolver.setRemote((RelatrixClientInterface) this);
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
	
	public Object sendCommand(RelatrixStatementInterface rs) throws DuplicateKeyException, IllegalAccessException, IOException {
		IndexResolver.setRemote((RelatrixClientInterface) this);
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
	
	
	public String getLocalNode() {
		return bootNode;
	}
	
	public String getRemoteNode() {
		return remoteNode;
	}
	
	public int getRemotePort( ) {
		return remotePort;
	}

	
	public void closeDb(Class clazz) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixStatement rs = new RelatrixStatement("close", clazz);
		sendCommand(rs);
	}
	
	public void closeDb(String alias, Class clazz) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixStatement rs = new RelatrixStatement("close", alias, clazz);
		sendCommand(rs);
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
	
	public DomainMapRange store(String alias, Comparable d, Comparable m, Comparable r) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixStatement rs = new RelatrixStatement("store",alias, d, m, r);
		return (DomainMapRange)sendCommand(rs);
	}
	
	public Object store(String alias, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("storekv", alias, k, v);
		return (DomainMapRange)sendCommand(rs);
	}
	
	public Object store(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixStatement rs = new RelatrixStatement("storekv", k, v);
		return sendCommand(rs);
	}
	


	public Object getByIndex(DBKey key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("getByIndex",key);
		try {
			return sendCommand(rs);
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
	
	public RemoteStream findSetStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findStream",alias, darg, marg, rarg);
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
	* @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<REsult>
	*/
	public RemoteTailSetIterator findTailSet(Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixStatement rs = new RelatrixStatement("findTailSet",darg, marg, rarg);
		try {
			return (RemoteTailSetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	public RemoteTailSetIterator findTailSet(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findTailSet",alias, darg, marg, rarg);
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
	
	public RemoteStream findTailSetStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findTailStream",alias, darg, marg, rarg);
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
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Result>
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
	
	public RemoteHeadSetIterator findHeadSet(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findHeadSet",alias, darg, marg, rarg);
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
	
	public RemoteStream findHeadSetStream(String alias, Object darg, Object marg, Object rarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findHeadStream",alias, darg, marg, rarg);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}

	}

	
	public RemoteSubSetIterator findSubSet(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findSubSet",alias, darg, marg, rarg, endarg);
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
	
	public RemoteStream findSubSetStream(String alias, Object darg, Object marg, Object rarg, Object ...endarg) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixStatement rs = new RelatrixStatement("findSubStream",alias, darg, marg, rarg, endarg);
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
	public Result next(Iterator rii) throws NoSuchElementException {
		((RelatrixStatement)rii).methodName = "next";
		((RelatrixStatement)rii).paramArray = new Object[0];
		try {
			return (Result)sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | DuplicateKeyException | IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	public boolean hasNext(Iterator rii) {
		((RelatrixStatement)rii).methodName = "hasNext";
		((RelatrixStatement)rii).paramArray = new Object[0];
		try {
			return (boolean)sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | DuplicateKeyException | IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
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

	@Override
	public RelatrixIndex getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findHeadStream(Object arg1, Object arg2, Object arg3, Object[] arg4)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findSubStreamAlias(String arg1, Object arg2, Object arg3, Object arg4, Object[] arg5)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException,
			NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAliasToPath(String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findSubStream(Object arg1, Object arg2, Object arg3, Object[] arg4)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatabaseCatalog getByPath(String arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatabaseCatalog getByAlias(String arg1) throws NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findSubSet(Object arg1, Object arg2, Object arg3, Object[] arg4)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatabasePath(DatabaseCatalog arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findTailStream(Object arg1, Object arg2, Object arg3, Object[] arg4)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void loadClassFromJar(String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findHeadSetAlias(String arg1, Object arg2, Object arg3, Object arg4, Object[] arg5)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException,
			NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findSubSetAlias(String arg1, Object arg2, Object arg3, Object arg4, Object[] arg5)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException,
			NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findHeadSet(Object arg1, Object arg2, Object arg3, Object[] arg4)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void loadClassFromPath(String arg1, String arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void removeAlias(String arg1) throws NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void setWildcard(char arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlias(String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void setTablespace(String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findTailSetAlias(String arg1, Object arg2, Object arg3, Object arg4, Object[] arg5)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException,
			NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findTailSet(Object arg1, Object arg2, Object arg3, Object[] arg4)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findStream(Object arg1, Object arg2, Object arg3)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findStream(String arg1, Object arg2, Object arg3, Object arg4) throws IOException,
			IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findSet(String arg1, Object arg2, Object arg3, Object arg4) throws IOException,
			IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator findSet(Object arg1, Object arg2, Object arg3)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void storekv(Comparable arg1, Object arg2)
			throws IOException, IllegalAccessException, DuplicateKeyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void storekv(String arg1, Comparable arg2, Object arg3)
			throws IOException, IllegalAccessException, DuplicateKeyException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void setAlias(String arg1, String arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void setTuple(char arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastValue() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastValue(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastValue(Class arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastValue(String arg1, Class arg2) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream entrySetStream(String arg1, Class arg2)
			throws IOException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream entrySetStream(Class arg1) throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findHeadStreamAlias(String arg1, Object arg2, Object arg3, Object arg4, Object[] arg5)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException,
			NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void removePackageFromRepository(String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream findTailStreamAlias(String arg1, Object arg2, Object arg3, Object arg4, Object[] arg5)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException,
			NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastKey(String arg1, Class arg2) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastKey(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastKey() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lastKey(Class arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstKey(Class arg1) throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstKey(String arg1, Class arg2) throws IOException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstKey(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstKey() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstValue(String arg1, Class arg2)
			throws IOException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstValue(Class arg1) throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstValue(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object firstValue() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator keySet(Class arg1) throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator keySet(String arg1, Class arg2) throws IOException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List resolve(Comparable arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object first(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object first() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object first(String arg1, Class arg2) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object first(Class arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object last(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object last(String arg1, Class arg2) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object last() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object last(Class arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String arg1, Comparable arg2) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Comparable arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long size() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long size(String arg1) throws IOException, NoSuchElementException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object get(String arg1, Comparable arg2) throws IOException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(Comparable arg1) throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void remove(String arg1, Comparable arg2, Comparable arg3, Comparable arg4) throws IOException,
			IllegalAccessException, NoSuchElementException, ClassNotFoundException, DuplicateKeyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void remove(String arg1, Comparable arg2) throws IOException, IllegalArgumentException,
			ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void remove(Comparable arg1)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void remove(Comparable arg1, Comparable arg2, Comparable arg3)
			throws IOException, IllegalAccessException, ClassNotFoundException, DuplicateKeyException {
		// TODO Auto-generated method stub
		return null;
	}

}
