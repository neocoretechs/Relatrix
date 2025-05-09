package com.neocoretechs.relatrix.client.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

//import com.neocoretechs.rocksack.SerializedComparator;
//import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RelatrixKVStatement;
import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;

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
public class RelatrixJsonKVClient extends RelatrixKVClient {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // remoteNode is ignored and get getLocalHost is used
	public static boolean SHOWDUPEKEYEXCEPTION = false;
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
	protected ConcurrentHashMap<String, RelatrixJsonKVStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixJsonKVStatement>();

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
	public RelatrixJsonKVClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
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
			System.out.println("RelatrixJsonKVClient server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("RelatrixJsonKVClient got connection "+sock);
  	    }
  	    try {
		  while(shouldRun ) {
				InputStream ins = sock.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(ins));
				JSONObject jobj = new JSONObject(in.readLine());
				RelatrixJsonKVStatement iori = (RelatrixJsonKVStatement) jobj.toObject();//,RelatrixKVTransactionStatement.class);	
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				Object o = iori.getObjectReturn();
				if( o instanceof Throwable ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixJsonKVClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
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
  	    						System.out.println("RelatrixJsonKVClient: ******** REMOTE EXCEPTION ******** "+oe);
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
				RelatrixJsonKVStatement rs = outstandingRequests.get(iori.getSession());
				if( rs == null ) {
					in.close();
					ins.close();
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
	@Override
	public void send(RemoteRequestInterface iori) throws Exception {
		if(DEBUG) {
			System.out.println("Attempting to send "+iori+" to "+workerSocket);
			if(workerSocket != null)
				System.out.println("Socket bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
			else
				System.out.println("Socket NULL!");
		}
		outstandingRequests.put(iori.getSession(), (RelatrixJsonKVStatement) iori);
		//if(DEBUG) {
		//	byte[] b = SerializedComparator.serializeObject(iori);
		//	System.out.println("Payload bytes="+b.length+" Put session "+iori+" to "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
		//}
		String iorij = JSONObject.toJson(iori);
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
		RelatrixJsonKVStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		//rc.send(rs);
		i = 0;
		RelatrixJsonKVClient rc = new RelatrixJsonKVClient(args[0],args[1],Integer.parseInt(args[2]));

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
				rs = new RelatrixJsonKVStatement(args[3],args[4]);
				break;
			case 6:
				rs = new RelatrixJsonKVStatement(args[3],args[4],args[5]);
				break;
			case 7:
				rs = new RelatrixJsonKVStatement(args[3],args[4],args[5],args[6]);
				break;
			case 8:
				rs = new RelatrixJsonKVStatement(args[3],args[4],args[5],args[6],args[7]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		rc.sendCommand(rs);
		rc.close();
	}

	
}
