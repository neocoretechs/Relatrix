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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

//import com.neocoretechs.rocksack.SerializedComparator;
//import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
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
public class RelatrixKVClient implements Runnable, RelatrixClientInterface {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static boolean SHOWDUPEKEYEXCEPTION = false;
	
	private String bootNode, remoteNode;
	private int remotePort;
	
	private int MASTERPORT = 9376; // master port, accepts connection from remote server
	private int SLAVEPORT = 9377; // slave port, conects to remote, sends outbound requests to master port of remote
	
	private InetAddress IPAddress = null; // remote server address
	private InetAddress localIPAddress = null; // local server address

	private Socket workerSocket = null; // socket assigned to slave port
	private SocketAddress workerSocketAddress; //address of slave
	private ServerSocket masterSocket; // master socket connected back to via server
	private Socket sock; // socker of mastersocket
	//private SocketAddress masterSocketAddress; // address of master
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	private ConcurrentHashMap<String, RelatrixKVStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixKVStatement>();

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
		IndexResolver.setRemote(this);
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
				if( o instanceof Exception ) {
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
			if(DEBUG)
				System.out.println("Attempting to send "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
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
		} catch (SocketException e) {
				System.out.println("Exception setting up socket to remote KV host:"+IPAddress+" port "+SLAVEPORT+" "+e);
		} catch (IOException e) {
				System.out.println("KV Socket send error "+e+" to address "+IPAddress+" on port "+SLAVEPORT);
		}
	}
	
	@Override
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
	
	/**
	 * Call the remote server method to send a manually constructed command
	 * @param rs The RelatrixKvStatement manually constructed
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	@Override
	public Object sendCommand(RelatrixStatementInterface rs) throws IllegalAccessException, IOException, DuplicateKeyException {
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
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
	
	//-------------------------------------------------------------------
	// Start of remote command sequence
	//-------------------------------------------------------------------
	
	public String getTablespace() {
		RelatrixKVStatement rs = new RelatrixKVStatement("getTableSpace",(Object[])null);
		try {
			return (String) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String setTablespace(String path) {
		RelatrixKVStatement rs = new RelatrixKVStatement("setTableSpace",path);
		try {
			return (String) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getAlias(String alias) {
		RelatrixKVStatement rs = new RelatrixKVStatement("getAlias",alias);
		try {
			return (String) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[][] getAliases() {
		RelatrixKVStatement rs = new RelatrixKVStatement("getAliases",(Object[])null);
		try {
			return (String[][]) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object setAlias(String alias, String path) {
		RelatrixKVStatement rs = new RelatrixKVStatement("setAlias",alias, path);
		try {
			return sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object removeAlias(String alias) {
		RelatrixKVStatement rs = new RelatrixKVStatement("removeAlias",alias);
		try {
			return sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Call the remote server method to store an object.
	 * @param k The Comparable representing the key relationship
	 * @param v The value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	@Override
	public Object store(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixKVStatement rs = new RelatrixKVStatement("store",k,v);
		return sendCommand(rs);
	}
	
	/**
	 * Call the remote server method to store an object.
	 * @param alias The database alias
	 * @param k The Comparable representing the key relationship
	 * @param v The value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws DuplicateKeyException if the storage of a duplicate key was attempted
	 * @throws NoSuchElementException if the alias was not found
	 * @return 
	 */
	@Override
	public Object store(String alias, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("store",alias,k,v);
		return sendCommand(rs);
	}
	
	@Override
	public UUID getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException {
		RelatrixKVStatement rs = new RelatrixKVStatement("getNewKey",(Object[])null);
		try {
			return (UUID)sendCommand(rs);
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
	public Object remove(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("remove",key);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object remove(String alias, Comparable instance) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("remove",alias,instance);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object firstValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("firstValue",clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object firstValue(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("firstValue",alias,clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the keyed value
	 * @param key The Comparable key
	 * @return The value for the given key, or null if not found
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	@Override
	public Object get(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("get",key);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object get(String alias, Comparable instance) throws ClassNotFoundException, IllegalAccessException, IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("get",alias,instance);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object getByIndex(String alias, DBKey index) throws IllegalAccessException, IOException, NoSuchElementException, ClassNotFoundException {
		return get(alias,index);
	}

	@Override
	public Object lastValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("lastValue",clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object lastValue(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("lastValue",alias,clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable firstKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("firstKey",clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable firstKey(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("firstKey",alias,clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable lastKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("lastKey",clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable lastKey(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("lastKey",alias,clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public long size(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("size",clazz);
		try {
			return (long) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public long size(String alias, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("size",alias,clazz);
		try {
			return (long) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean contains(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("contains",key);
		try {
			return (boolean) sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean contains(String alias, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("contains",alias,key);
		try {
			return (boolean) sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean containsValue(Class keyType, Object value) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("containsValue",keyType,value);
		try {
			return (boolean) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean containsValue(String alias, Class keyType, Object value) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("containsValue",alias,keyType,value);
		try {
			return (boolean) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	* Provides a persistent collection iterator greater or equal to 'from' element based on RockSack tailset iterator.
	* Returns the KEYS from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteTailMapIterator from which the data may be retrieved. Follows rocksack tailset Iterator interface, return Iterator of Object returning Comparable objects.
	*/
	public RemoteTailMapIterator findTailMap(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMap",key);
		try {
			return (RemoteTailMapIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteTailMapIterator findTailMap(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMap",alias,key);
		try {
			return (RemoteTailMapIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	* Provides a persistent collection stream greater or equal to 'from' element based on RockSack tailset stream.
	* Returns the KEYS from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteStream from which the data may be retrieved. Follows rocksack tailset stream interface, return Stream of Comparable objects.
	* 
	* */
	public RemoteStream findTailMapSteam(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapStream",key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findTailMapSteam(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapStream",alias,key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteEntrySetIterator entrySet(Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("entrySet",clazz);
		try {
			return (RemoteEntrySetIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteEntrySetIterator entrySet(String alias, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("entrySet",alias,clazz);
		try {
			return (RemoteEntrySetIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream entrySetStream(Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("entrySetStream",clazz);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream entrySetStream(String alias, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("entrySetStream",alias,clazz);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteKeySetIterator keySet(Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("keySet",clazz);
		try {
			return (RemoteKeySetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteKeySetIterator keySet(String alias, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("keySet",alias,clazz);
		try {
			return (RemoteKeySetIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream keySetStream(Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("keySetStream",clazz);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream keySetStream(String alias, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("keySetStream",alias,clazz);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	* Provides a persistent collection iterator greater or equal to 'from' element based on RockSack tailsetKV iterator.
	* Returns the com.neocoretechs.rocksack.iterator.KeyValuePair from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteTailMapKVIterator from which the data may be retrieved. Follows rocksack tailsetKV Iterator interface, return Iterator of Object returning KeyValuePair objects.
	*/
	public RemoteTailMapKVIterator findTailMapKV(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapKV",key);
		try {
			return (RemoteTailMapKVIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}

	}
	
	public RemoteTailMapKVIterator findTailMapKV(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapKV",alias,key);
		try {
			return (RemoteTailMapKVIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}

	}
	/**
	* Provides a persistent collection iterator greater or equal to 'from' element based on RockSack tailsetKV stream.
	* Returns the com.neocoretechs.rocksack.iterator.KeyValuePair from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteStream from which the data may be retrieved. Follows rocksack tailsetKV stream interface, return stream of KeyValuePair objects.
	*/
	public RemoteStream findTailMapKVStream(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapKVStream",key);
		try {
			return (RemoteStream) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}

	}
	
	public RemoteStream findTailMapKVStream(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapKVStream",alias,key);
		try {
			return (RemoteStream) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}

	}
	/**
	* Iterator for items of persistent collection strictly less than 'to' element.
	* Returns the KEYS from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteHeadMapIterator from which the data may be retrieved. Follows rocksack headSet Iterator interface, return Iterator of Object returning Comparable objects.
	*/
	public RemoteHeadMapIterator findHeadMap(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMap",key);
		try {
			return (RemoteHeadMapIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteHeadMapIterator findHeadMap(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMap",alias,key);
		try {
			return (RemoteHeadMapIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	* Stream for items of persistent collection strictly less than 'to' element.
	* Returns the KEYS from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteStream from which the data may be retrieved. Follows rocksack headSet stream interface, return stream of Comparable objects.
	*/
	public RemoteStream findHeadMapStream(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapStream",key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadMapStream(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapStream",alias,key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	* Iterator for items of persistent collection strictly less than 'to' element.
	* Returns the com.neocoretechs.rocksack.iterator.KeyValuePair from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteHeadMapKVIterator from which the data may be retrieved. Follows rocksack headSetKV Iterator interface, return Iterator of Object returning KeyValuePair objects.
	*/
	public RemoteHeadMapKVIterator findHeadMapKV(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapKV",key);
		try {
			return (RemoteHeadMapKVIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteHeadMapKVIterator findHeadMapKV(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapKV",alias,key);
		try {
			return (RemoteHeadMapKVIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	/**
	* Stream for items of persistent collection strictly less than 'to' element.
	* Returns the com.neocoretechs.rocksack.iterator.KeyValuePair from a key/value store starting at the given key.
	* @param key Comparable for the starting key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @throws IllegalAccessException 
	* @return The RemoteStream from which the data may be retrieved. Follows rocksack headSetKV stream interface, return stream of KeyValuePair objects.
	*/
	public RemoteStream findHeadMapKVStream(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapKVStream",key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadMapKVStream(String alias, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapKVStream",alias,key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
 	* Provides a persistent collection iterator 'from' element inclusive, 'to' element exclusive
 	* @param key1 Comparable for the starting key of the relationship
 	* @param key2 Comparable for the element before the ending key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteSubMapIterator from which the data may be retrieved. Follows rocksack subSet Iterator interface, return Iterator of Object returning Comparable objects.
	*/
	public RemoteSubMapIterator findSubMap(Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMap",key1,key2);
		try {
			return (RemoteSubMapIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteSubMapIterator findSubMap(String alias, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMap",alias,key1,key2);
		try {
			return (RemoteSubMapIterator)sendCommand(rs);
		} catch ( DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
 	* Provides a persistent collection stream 'from' element inclusive, 'to' element exclusive.
 	* Returns KEYS of key/value pairs.
 	* @param key1 Comparable for the starting key of the relationship
 	* @param key2 Comparable for the element before the ending key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteStream from which the data may be retrieved. Follows rocksack subSetStream interface, return Stream of Comparable objects.
	*/
	public RemoteStream findSubMapStream(Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapStream",key1,key2);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubMapStream(String alias, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapStream",alias,key1,key2);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
 	* Provides a persistent collection Iterator 'from' element inclusive, 'to' element exclusive.
 	* Returns com.neocoretechs.rocksack.iterator.KeyValuePair of key/value pairs.
 	* @param key1 Comparable for the starting key of the relationship
 	* @param key2 Comparable for the element before the ending key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteSubMapKVIterator from which the data may be retrieved. Follows rocksack subSetKVIterator interface, return Iterator of KeyValuePair objects.
	*/
	public RemoteSubMapKVIterator findSubMapKV(Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapKV",key1,key2);
		try {
			return (RemoteSubMapKVIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteSubMapKVIterator findSubMapKV(String alias, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapKV",alias,key1,key2);
		try {
			return (RemoteSubMapKVIterator)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
 	* Provides a persistent collection stream 'from' element inclusive, 'to' element exclusive.
 	* Returns com.neocoretechs.rocksack.iterator.KeyValuePair of key/value pairs.
 	* @param key1 Comparable for the starting key of the relationship
 	* @param key2 Comparable for the element before the ending key of the relationship
	* @exception IOException low-level access or problems modifying schema
	* @exception IllegalArgumentException the operator is invalid
	* @exception ClassNotFoundException if the Class of Object is invalid
	* @exception IllegalAccessException 
	* @return The RemoteStream from which the data may be retrieved. Follows rocksack subSetKVStream interface, return Stream of KeyValuePair objects.
	*/
	public RemoteStream findSubMapKVStream(Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapKVStream",key1,key2);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubMapKVStream(String alias, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapKVStream",alias,key1,key2);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Load a class into the handlerclassloader from remote repository via jar file
	 * @param jar
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public Object loadClassFromJar(String jar) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("loadClassFromJar",jar);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Load a class in to handlerclassloader via package and directory path.
	 * @param pack The package designation
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public Object loadClassFromPath(String pack, String path) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("loadClassFromPath", pack, path);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Remove package in handlerclassloader and from repository.
	 * @param pack The package designation, everything starting with this descriptor will be removed
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public Object removePackageFromRepository(String pack) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("removePackageFromRepository", pack);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return Object of iteration, depends on iterator being used, typically, Map.Entry derived serializable instance of next element
	 */
	public Object next(RemoteObjectInterface rii) throws NoSuchElementException {
		((RelatrixKVStatement)rii).methodName = "next";
		((RelatrixKVStatement)rii).paramArray = new Object[0];
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixKVStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		Object o = ((RelatrixKVStatement)rii).getObjectReturn();
		if(o instanceof NoSuchElementException)
			throw (NoSuchElementException)o;
		return o;

	}
	
	@Override
	public boolean hasNext(RemoteObjectInterface rii) {
		((RelatrixKVStatement)rii).methodName = "hasNext";
		((RelatrixKVStatement)rii).paramArray = new Object[0];
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixKVStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		return (boolean)((RelatrixKVStatement)rii).getObjectReturn();	
	}
	
	@Override
	public void remove(RemoteObjectInterface rii) throws UnsupportedOperationException, IllegalStateException{
		((RelatrixKVStatement)rii).methodName = "remove";
		((RelatrixKVStatement)rii).paramArray = new Object[]{ ((RelatrixKVStatement)rii).getObjectReturn() };
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixKVStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		Object o = ((RelatrixKVStatement)rii).getObjectReturn();
		if( o != null) {
			if( o instanceof UnsupportedOperationException)
				throw (UnsupportedOperationException)o;
			else
				if( o instanceof IllegalStateException)
					throw (IllegalStateException)o;
				else
					if(o instanceof Exception)
						throw new UnsupportedOperationException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		}
	}
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	@Override
	public void close(RemoteObjectInterface rii) {
		((RelatrixKVStatement)rii).methodName = "close";
		((RelatrixKVStatement)rii).paramArray = new Object[0];
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixKVStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		outstandingRequests.remove(((RelatrixStatement)rii).getSession());
	}
	
	/**
	 * Open a socket to the remote worker located at 'remoteWorker' with the tablespace appended
	 * so each node is named [remoteWorker]0 [remoteWorker]1 etc. The fname should be full qualified.
	 * If remote is null, the defaults will all be used, otherwise, database name will be massaged for cluster
	 * @param fname
	 * @param remote remote database name
	 * @param port remote port
	 * @return
	 * @throws IOException
	 */
	@Override
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
		RelatrixClientInterface rc = new RelatrixKVClient(args[0],args[1],Integer.parseInt(args[2]));

		switch(args.length) {
			case 4:
				RemoteStream stream = rc.entrySetStream(Class.forName(args[3]));
				stream.of().forEach(e ->{	
					System.out.println(++i+"="+((Map.Entry) (e)).getKey()+" / "+((Map.Entry) (e)).getValue());
				});
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
