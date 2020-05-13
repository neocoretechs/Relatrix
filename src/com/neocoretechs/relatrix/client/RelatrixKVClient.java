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
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.bigsack.io.ThreadPoolManager;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;
/**
 * This class functions as client to the RelatrixKVServer Worker threads located on a remote node.
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
 * In general for a cluster configuration the remote directory is 
 * 'Database path + 'tablespace'+ tablespace# + tablename' where tablename is 'DBname+class+'.'+tablespace#'
 * so if your remote db path is /home/relatrix/AMI as passed to the server then its translation is:
 *  /home/relatrix/tablespace0/AMIcom.yourpack.yourclass.0
 * for the remote node 'AMI0', for others replace all '0' with '1' etc for other tablespaces.
 * @author jg
 * Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixKVClient implements Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	private int MASTERPORT = 9376; // master port, accepts connection from remote server
	private int SLAVEPORT = 9377; // slave port, conects to remote, sends outbound requests to master port of remote
	
	private InetAddress IPAddress = null; // remote server address

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
		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			IPAddress = InetAddress.getByName(remoteNode);
		}
		if( DEBUG ) {
			System.out.println("RelatrixClient constructed with remote:"+IPAddress);
		}
		//
		// Wait for master server node to connect back to here for return channel communication
		//
		//masterSocketAddress = new InetSocketAddress(MASTERPORT);
		masterSocket = new ServerSocket(0);
		MASTERPORT = masterSocket.getLocalPort();
		SLAVEPORT = remotePort;
		// send message to spin connetion
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
		} catch(Exception e) {
			// we lost the remote, try to close worker and wait for reconnect
			//System.out.println("RelatrixClient: receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
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
			outstandingRequests.put(iori.getSession(), (RelatrixKVStatement) iori);
			ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
			oos.writeObject(iori);
			oos.flush();
		} catch (SocketException e) {
				System.out.println("Exception setting up socket to remote KV host:"+IPAddress+" port "+SLAVEPORT+" "+e);
		} catch (IOException e) {
				System.out.println("KV Socket send error "+e+" to address "+IPAddress+" on port "+SLAVEPORT);
		}
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
	 * Call the remote server method to store an object.
	 * @param k The Comparable representing the key relationship
	 * @param v The value
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @return 
	 */
	public Object store(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixKVStatement rs = new RelatrixKVStatement("store",k,v);
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
	public Object transactionalStore(Comparable k, Object v) throws IllegalAccessException, IOException, DuplicateKeyException {
		RelatrixKVStatement rs = new RelatrixKVStatement("transactionalStore",k, v);
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
	/**
	 * Commit the outstanding indicies to their transactional data.
	 * @throws IOException
	 */
	public void transactionCommit(Class clazz) throws IOException {
		RelatrixKVStatement rs = new RelatrixKVStatement("transactionCommit",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		Object o = rs.getObjectReturn();		
		outstandingRequests.remove(rs.getSession());
		if(o != null && o instanceof IOException)
			throw (IOException)o;
		else
			if(o instanceof Exception)
				throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
	}
	/**
	 * Roll back all outstanding transactions on the indicies
	 * @throws IOException
	 */
	public void transactionRollback(Class clazz) throws IOException {
		RelatrixKVStatement rs = new RelatrixKVStatement("transactionRollback",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		Object o = rs.getObjectReturn();		
		outstandingRequests.remove(rs.getSession());
		if(o != null && o instanceof IOException)
			throw (IOException)o;
		else
			if(o instanceof Exception)
				throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
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
	public void transactionCheckpoint(Class clazz) throws IOException, IllegalAccessException {
		RelatrixKVStatement rs = new RelatrixKVStatement("transactionCheckpoint",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		outstandingRequests.remove(rs.getSession());
		Object o = rs.getObjectReturn();		
		if(o != null) {
			if(o instanceof IOException)
				throw (IOException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof Exception)
						throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		}
	}
	/**
	* recursively delete all relationships that this object participates in
	* @exception IOException low-level access or problems modifiying schema
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	*/
	public Object remove(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("remove",key);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
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

	public Object firstValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("firstValue",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
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
	
	public Object lastValue(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("lastValue",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
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
	
	public Comparable firstKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("firstKey",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (Comparable) o;
	}
	
	public Comparable lastKey(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("lastKey",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (Comparable) o;
	}

	public long size(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("size",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (long) o;
	}
	
	public boolean contains(Comparable key) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("contains",key);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (boolean) o;
	}
	
	public boolean containsValue(Object value) throws IOException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("containsValue",value);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (boolean) o;
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
	public RemoteTailmapIterator findTailMap(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMap",key);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteTailmapIterator)o;
	}

	public RemoteEntrysetIterator entrySet(Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("entrySet",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteEntrysetIterator)o;


	}
	
	public RemoteKeysetIterator keySet(Class clazz) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("keySet",clazz);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteKeysetIterator)o;


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
	public RemoteTailmapKVIterator findTailMapKV(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("findTailMapKV",key);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		//IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteTailmapKVIterator)o;

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
	public RemoteHeadmapIterator findHeadMap(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMap",key);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteHeadmapIterator)o;

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
	public RemoteHeadmapKVIterator findHeadMapKV(Comparable key) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("findHeadMapKV",key);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteHeadmapKVIterator)o;

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
	public RemoteSubmapIterator findSubMap(Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMap",key1, key2);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteSubmapIterator)o;

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
	public RemoteSubmapKVIterator findSubMapKV(Comparable key1, Comparable key2) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
	{
		RelatrixKVStatement rs = new RelatrixKVStatement("findSubMapKV",key1, key2);
		CountDownLatch cdl = new CountDownLatch(1);
		rs.setCountDownLatch(cdl);
		send(rs);
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}
		Object o = rs.getObjectReturn();
		outstandingRequests.remove(rs.getSession());
		if(o instanceof IllegalArgumentException)
			throw (IllegalArgumentException)o;
		else
			if(o instanceof ClassNotFoundException)
				throw (ClassNotFoundException)o;
			else
				if(o instanceof IllegalAccessException)
					throw (IllegalAccessException)o;
				else
					if(o instanceof IOException)
						throw (IOException)o;
					else
						if(o instanceof Exception)
							throw new IOException("Repackaged remote exception pertaining to "+(((Exception)o).getMessage()));
		return (RemoteSubmapKVIterator)o;

	}
	/**
	 * Call the remote iterator from the various 'findSet' methods and return the result.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii
	 * @return
	 */
	public Comparable next(RemoteObjectInterface rii) throws NoSuchElementException {
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
		return (Comparable)o;

	}
	
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
	
	public static void main(String[] args) throws Exception {
		RelatrixKVClient rc = new RelatrixKVClient("localhost","localhost", 9000);
		RelatrixKVStatement rs = new RelatrixKVStatement("toString",(Object[])null);
		rc.send(rs);
	}
	
}
