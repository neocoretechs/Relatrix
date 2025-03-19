package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.net.SocketException;

import java.util.Iterator;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;

import com.neocoretechs.rocksack.TransactionId;
/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixKVTransactionServer}
 * Worker threads located on a remote node.<p/>
 * On the client and server the following are present as conventions:<br/>
 * On the client a ServerSocket waits for inbound connection on MASTERPORT after DB spinup message to WORKBOOTPORT<br/>
 * On the client a socket is created to connect to SLAVEPORT and objects are written to it<br/>
 * On the server a socket is created to connect to MASTERPORT and response objects are written to it<br/>
 * On the server a ServerSocket waits on SLAVEPORT and request Object are read from it<p/>
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is on the RelatrixKVTransactionServer. The client contacts the boot time server port, the desired database
 * is opened or the context of an open DB is passed back, and the client is handed the addresses of the master 
 * and slave ports that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 */
public class RelatrixJsonKVClientTransaction extends RelatrixKVClientTransaction {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
	Jsonb jsonb = null;
	byte[] buf = new byte[4096];
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 

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
	public RelatrixJsonKVClientTransaction(String bootNode, String remoteNode, int remotePort)  throws IOException {
		super(bootNode, remoteNode, remotePort);
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
				RemoteResponseInterface iori = jsonb.fromJson(ins,RemoteResponseInterface.class);	
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				Object o = iori.getObjectReturn();
				if( o instanceof Exception ) {
					System.out.println("RelatrixJsonKVClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					o = ((Throwable)o).getCause();
				}
				RelatrixKVStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					ins.close();
					throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
				} else {
					if(o instanceof Iterator)
						((RemoteObjectInterface)o).setClient(this);
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
	public void send(RemoteRequestInterface iori) throws Exception {
		outstandingRequests.put(iori.getSession(), (RelatrixKVStatement) iori);
		String iorij = jsonb.toJson(iori);
		OutputStream os = workerSocket.getOutputStream();
		if(DEBUG)
			System.out.println("Output stream "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
		os.write(iorij.getBytes());
		if(DEBUG)
			System.out.println("writeObject "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
		os.flush();
		if(DEBUG)
			System.out.println(iori+" sent to "+workerSocket);
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
		if(jsonb == null)
			jsonb = JsonbBuilder.create();
		Socket s = new Socket(IPAddress, SLAVEPORT);
		s.setKeepAlive(true);
		s.setReceiveBufferSize(32767);
		s.setSendBufferSize(32767);
		System.out.println("Socket created to "+s+" using "+jsonb);
		CommandPacketInterface cpi = new CommandPacket(bootNode, MASTERPORT);
		String cpij = jsonb.toJson(cpi);
		System.out.println(cpij);
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
		RelatrixKVTransactionStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		i = 0;
		RelatrixJsonKVClientTransaction rc = new RelatrixJsonKVClientTransaction(args[0],args[1],Integer.parseInt(args[2]));
		TransactionId xid = null;
		switch(args.length) {
			case 4:
				/*
				Stream stream = rc.entrySetStream(xid, Class.forName(args[3]));
				stream.forEach(e ->{	
					System.out.println(++i+"="+((Map.Entry) (e)).getKey()+" / "+((Map.Entry) (e)).getValue());
				});
				*/
				xid = rc.getTransactionId();
				Iterator it = rc.entrySet(xid,Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				rc.endTransaction(xid);
				System.exit(0);
			case 5:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4]);
				break;
			case 6:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixKVTransactionStatement(args[3],xid,args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		rc.sendCommand(rs);
		rc.endTransaction(xid);
		rc.close();
	}

}
