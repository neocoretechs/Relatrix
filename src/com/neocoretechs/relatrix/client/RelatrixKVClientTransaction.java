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
public class RelatrixKVClientTransaction implements Runnable, RelatrixClientTransactionInterface {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private String bootNode, remoteNode;
	private int remotePort;
	
	private int MASTERPORT = 9376; // master port, accepts connection from remote server
	private int SLAVEPORT = 9377; // slave port, connects to remote, sends outbound requests to master port of remote
	
	private InetAddress IPAddress = null; // remote server address
	private InetAddress localIPAddress = null; // local server address

	private Socket workerSocket = null; // socket assigned to slave port
	private SocketAddress workerSocketAddress; //address of slave
	private ServerSocket masterSocket; // master socket connected back to via server
	private Socket sock; // socket of mastersocket
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
	public RelatrixKVClientTransaction(String bootNode, String remoteNode, int remotePort)  throws IOException {
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
		  if(DEBUG)
			  System.out.printf("%s Exiting run loop shouldRun:%b%n", this.getClass().getName(),shouldRun);
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
			if(((RelatrixKVTransactionStatement)iori).getTransactionId() != null && ((RelatrixKVTransactionStatement)iori).getTransactionId() != "")
				IndexResolver.setCurrentTransactionId(((RelatrixKVTransactionStatement)iori).getTransactionId());
			outstandingRequests.put(iori.getSession(), (RelatrixKVStatement) iori);
			ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
			oos.writeObject(iori);
			oos.flush();
		} catch (SocketException e) {
				System.out.println(this.getClass().getName()+" Exception setting up socket to remote KV host:"+IPAddress+" port "+SLAVEPORT+" "+e);
		} catch (IOException e) {
				System.out.println(this.getClass().getName()+" KV Socket send error "+e+" to address "+IPAddress+" on port "+SLAVEPORT);
		}
	}
	
	@Override
	public void close() {
		if(DEBUG) {
			System.out.println(this.getClass().getName()+" close Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
		}
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
		if(DEBUG) {
			System.out.println(this.getClass().getName()+" shutdown Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
		}
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
	
	@Override
	public String getTransactionId() throws ClassNotFoundException, IllegalAccessException, IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("", "getTransactionId", (Object[])null);
		try {
			String xid = (String) sendCommand(rs);
			IndexResolver.setRemote(xid, this);
			return xid;
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public String endTransaction(String xid) throws ClassNotFoundException, IllegalAccessException, IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "endTransaction", xid);
		try {
			return (String) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public String getTablespace() {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","getTableSpace",(Object[])null);
		try {
			return (String) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setTablespace(String tablespace) {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","setTableSpace",tablespace);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
	}
	
	public String getAlias(String alias) {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","getAlias",alias);
		try {
			return (String) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[][] getAliases() {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","getAliases",(Object[])null);
		try {
			return (String[][]) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object setAlias(String alias, String path) {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","setAlias",alias, path);
		try {
			return sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object removeAlias(String alias) throws NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("removeAlias",alias);
		try {
			return sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[] getTransactionState() {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","getTransactionState",(Object[])null);
		try {
			return (Object[]) sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void rollbackOutstandingTransactions() {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","rollbackAllTransactions",(Object[])null);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
	}
	
	public void rollbackOutstandingTransaction(String uid) {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement("","rollbackTransaction",uid);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Store our k/v
	 * This is a transactional store in the context of a previously initiated transaction.
	 * Here, we can control the transaction explicitly, in fact, we must call commit at the end of processing
	 * to prevent a recovery on the next operation
	 * @param d The Comparable representing the key object for this morphism relationship.
	 * @param m The value object
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return The identity element of the set - The DomainMapRange of stored object composed of d,m,r
	 * @throws DuplicateKeyException 
	 */
	@Override
	public Object store(String xid, Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "store", xid, k, v);
		return sendCommand(rs);
	}
	
	@Override
	public void storekv(String alias, String xid, Comparable index, Object instance) throws IllegalAccessException, IOException, DuplicateKeyException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "store", alias, xid, index, instance);
		sendCommand(rs);
	}
	
	@Override
	public void checkpoint(String alias, String xid, Class clazz) throws IllegalAccessException, IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "checkpoint", alias, xid, clazz);
		try {
			sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}	
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
	@Override
	public void checkpoint(String xid, Class clazz) throws IOException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "checkpoint", xid, clazz);
		try {
			sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void checkpoint(String xid) throws IOException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "checkpoint", xid);
		try {
			sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public void checkpoint(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException  {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "checkpoint", alias, xid);
		try {
			sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public UUID getNewKey(String xid) throws ClassNotFoundException, IllegalAccessException, IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "getNewKey",(Object[])null);
		try {
			return (UUID)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void commit(String xid) throws IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "commit", xid);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException | DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void commit(String alias, String xid) throws IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "commit", alias, xid);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException | DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void commit(String xid, Class clazz) throws IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "commit", xid, clazz);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException | DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void commit(String alias, String xid, Class clazz) throws IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "commit", alias, xid, clazz);
		try {
			sendCommand(rs);
		} catch (DuplicateKeyException | IllegalAccessException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public void rollback(String xid) throws IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "rollback", xid);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException |DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public void rollback(String alias, String xid) throws IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "rollback", alias, xid);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException |DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public void rollback(String xid, Class clazz) throws IOException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "rollback", xid, clazz);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException |DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public void rollback(String alias, String xid, Class next) throws IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "rollback", alias, xid);
		try {
			sendCommand(rs);
		} catch (IllegalAccessException |DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}

	@Override
	public void rollbackToCheckpoint(String xid) throws IOException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "rollbackToCheckpoint", xid);
		try {
			sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}	
	}
	
	@Override
	public void rollbackToCheckpoint(String alias, String xid) throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "rollbackToCheckpoint", alias, xid);
		try {
			sendCommand(rs);
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
	public Object remove(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "remove", xid, key);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object remove(String alias, String xid, Comparable instance) throws IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "remove", alias, xid, instance);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException | IllegalAccessException e) {
			throw new IOException(e);
		}	
	}

	@Override
	public Object firstValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "firstValue", xid, clazz);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object firstValue(String alias, String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "firstValue", xid, clazz);
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
	public Object get(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "get", xid, key);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object get(String alias, String xid, Comparable instance) throws IllegalAccessException, IOException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "get", alias, xid, instance);
		try {
			return sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object getByIndex(String alias, String transactionId, DBKey index) throws IllegalAccessException, IOException, NoSuchElementException {
		return get(alias, transactionId, index);
	}
	
	@Override
	public Object lastValue(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "lastValue", xid, clazz);
		try {
			return sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Object lastValue(String alias, String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "lastValue", alias, xid, clazz);
		try {
			return sendCommand(rs);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable firstKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "firstKey", xid, clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable firstKey(String alias, String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "firstKey", alias, xid, clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public Comparable lastKey(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "lastKey", xid, clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Comparable lastKey(String alias, String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "lastKey", alias, xid, clazz);
		try {
			return (Comparable) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public long size(String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "size", xid, clazz);
		try {
			return (long) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public long size(String alias, String xid, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "size", alias, xid, clazz);
		try {
			return (long) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean contains(String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "contains", xid, key);
		try {
			return (boolean) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean contains(String alias, String xid, Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "contains", alias, xid, key);
		try {
			return (boolean) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean containsValue(String xid, Class keyType, Object value) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "containsValue", xid, keyType, value);
		try {
			return (boolean) sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public boolean containsValue(String alias, String xid, Class keyType, Object value) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "containsValue", alias, xid, keyType, value);
		try {
			return (boolean) sendCommand(rs);
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
	public RemoteTailMapIteratorTransaction findTailMap(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMap", xid, key);
		try {
			return (RemoteTailMapIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	public RemoteTailMapIteratorTransaction findTailMap(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMap", alias, xid, key);
		try {
			return (RemoteTailMapIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findTailMapStream(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMapStream", xid, key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	public RemoteStream findTailMapStream(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMapStream", alias, xid, key);
		try {
			return (RemoteStream)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteEntrySetIteratorTransaction entrySet(String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "entrySet", xid, clazz);
		try {
			return (RemoteEntrySetIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteEntrySetIteratorTransaction entrySet(String alias, String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "entrySet", alias, xid, clazz);
		try {
			return (RemoteEntrySetIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public RemoteStream entrySetStream(String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "entrySetStream", xid, clazz);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public RemoteStream entrySetStream(String alias, String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "entrySetStream", alias, xid, clazz);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	
	@Override
	public RemoteKeySetIteratorTransaction keySet(String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "keySet", xid, clazz);
		try {
			return (RemoteKeySetIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public RemoteKeySetIteratorTransaction keySet(String alias, String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "keySet", alias, xid, clazz);
		try {
			return (RemoteKeySetIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream keySetStream(String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "keySetStream", xid, clazz);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream keySetStream(String alias, String xid, Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "keySetStream", alias, xid, clazz);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
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
	public RemoteTailMapKVIteratorTransaction findTailMapKV(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMapKV",xid, key);
		try {
			return (RemoteTailMapKVIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}

	public RemoteTailMapKVIteratorTransaction findTailMapKV(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMapKV", alias, xid, key);
		try {
			return (RemoteTailMapKVIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findTailMapKVStream(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMapKVStream", xid, key);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findTailMapKVStream(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findTailMapKVStream", alias, xid, key);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
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
	public RemoteHeadMapIteratorTransaction findHeadMap(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMap", xid, key);
		try {
			return (RemoteHeadMapIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteHeadMapIteratorTransaction findHeadMap(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMap", alias, xid, key);
		try {
			return (RemoteHeadMapIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadMapStream(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMapStream", xid, key);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadMapStream(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMapStream", alias, xid, key);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
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
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable[]>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public RemoteHeadMapKVIteratorTransaction findHeadMapKV(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMapKV", xid, key);
		try {
			return (RemoteHeadMapKVIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteHeadMapKVIteratorTransaction findHeadMapKV(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMapKV", alias, xid, key);
		try {
			return (RemoteHeadMapKVIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadMapKVStream(String alias, String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMapKVStream", alias, xid, key);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findHeadMapKVStream(String xid, Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findHeadMapKVStream", xid, key);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
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
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public RemoteSubMapIteratorTransaction findSubMap(String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMap", xid, key1, key2);
		try {
			return (RemoteSubMapIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteSubMapIteratorTransaction findSubMap(String alias, String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMap", alias, xid, key1, key2);
		try {
			return (RemoteSubMapIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubMapStream(String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMapStream", xid, key1, key2);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubMapStream(String alias, String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMapStream", alias, xid, key1, key2);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
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
	 * @return The RemoteRelatrixIterator from which the data may be retrieved. Follows Iterator interface, return Iterator<Comparable>
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	public RemoteSubMapKVIteratorTransaction findSubMapKV(String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMapKV", xid, key1, key2);
		try {
			return (RemoteSubMapKVIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteSubMapKVIteratorTransaction findSubMapKV(String alias, String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMapKV", alias, xid, key1, key2);
		try {
			return (RemoteSubMapKVIteratorTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubMapKVStream(String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMapKVStream", xid, key1, key2);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
		} catch (DuplicateKeyException e) {
			throw new IOException(e);
		}
	}
	
	public RemoteStream findSubMapKVStream(String alias, String xid, Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "findSubMapKVStream", alias, xid, key1, key2);
		try {
			return (RemoteStreamTransaction)sendCommand(rs);
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
	public Object loadClassFromJar(String xid, String jar) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "loadClassFromJar",jar);
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
	public Object loadClassFromPath(String xid, String pack, String path) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "loadClassFromPath", pack, path);
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
	public Object removePackageFromRepository(String xid, String pack) throws IOException, ClassNotFoundException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVTransactionStatement(xid, "removePackageFromRepository", pack);
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
	 * @throws IllegalAccessException 
	 */
	@Override
	public Object next(String xid, RemoteObjectInterface rii)  {
		((RelatrixKVTransactionStatement)rii).xid = xid;
		((RelatrixKVStatement)rii).methodName = "next";
		((RelatrixKVStatement)rii).paramArray = new Object[0];
		try {
			return sendCommand((RelatrixStatementInterface) rii);
		} catch (DuplicateKeyException | IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean hasNext(String xid, RemoteObjectInterface rii) {
		((RelatrixKVTransactionStatement)rii).xid = xid;
		((RelatrixKVStatement)rii).methodName = "hasNext";
		((RelatrixKVStatement)rii).paramArray = new Object[0];
		try {
			return (boolean) sendCommand((RelatrixStatementInterface) rii);
		} catch (DuplicateKeyException | IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void remove(String xid, RemoteObjectInterface rii) throws UnsupportedOperationException, IllegalStateException {
		((RelatrixKVTransactionStatement)rii).xid = xid;
		((RelatrixKVStatement)rii).methodName = "hasNext";
		((RelatrixKVStatement)rii).paramArray = new Object[]{xid,((RelatrixKVTransactionStatement)rii).getObjectReturn()};	
		try {
			sendCommand((RelatrixStatementInterface) rii);
		} catch (IllegalAccessException | IOException | DuplicateKeyException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Issue a close which will merely remove the request resident object here and on the server
	 * @param rii
	 */
	@Override
	public void close(String xid, RemoteObjectInterface rii) {
		((RelatrixKVTransactionStatement)rii).xid = xid;
		((RelatrixKVTransactionStatement)rii).methodName = "close";
		((RelatrixKVTransactionStatement)rii).paramArray = new Object[] {xid};
		CountDownLatch cdl = new CountDownLatch(1);
		((RelatrixKVTransactionStatement) rii).setCountDownLatch(cdl);
		send((RemoteRequestInterface) rii);
		try {
			cdl.await();
		} catch (InterruptedException e) {}
		outstandingRequests.remove(((RelatrixKVTransactionStatement)rii).getSession());
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
		RelatrixKVTransactionStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		//rc.send(rs);
		i = 0;
		RelatrixClientTransactionInterface rc = new RelatrixKVClientTransaction(args[0],args[1],Integer.parseInt(args[2]));
		String xid = "";
		switch(args.length) {
			case 4:
				RemoteStream stream = rc.entrySetStream(xid, Class.forName(args[3]));
				stream.of().forEach(e ->{	
					System.out.println(++i+"="+((Map.Entry) (e)).getKey()+" / "+((Map.Entry) (e)).getValue());
				});
				System.exit(0);
			case 5:
				rs = new RelatrixKVTransactionStatement(xid,args[3],args[4]);
				break;
			case 6:
				rs = new RelatrixKVTransactionStatement(xid,args[3],args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixKVTransactionStatement(xid,args[3],args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixKVTransactionStatement(xid,args[3],args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		rc.sendCommand(rs);
		rc.close();
	}

}
