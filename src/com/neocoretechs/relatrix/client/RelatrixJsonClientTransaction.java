package com.neocoretechs.relatrix.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import com.neocoretechs.relatrix.TransactionId;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;


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

 * In a transaction context, we must obtain a transaction Id from the server for the lifecycle of the transaction.<p/>
 * The transaction Id may outlive the session, as the session is transitory for communication purposes.
 * The {@link RelatrixTransactionStatement} contains the transaction Id.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixJsonClientTransaction extends RelatrixClientTransaction implements ClientInterface, Runnable {
	private static final boolean DEBUG = true;
	public static final boolean TEST = false; // true to run in local cluster test mode
	
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
	public RelatrixJsonClientTransaction(String bootNode, String remoteNode, int remotePort)  throws IOException {
		super(bootNode, remoteNode, remotePort);
	}

	/**
	* Set up the socket to receive the queued response from TCPJsonTransactionWorker on server
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
			System.out.println("RelatrixJsonClientTransaction server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("RelatrixJsonClientTransaction got connection "+sock);
  	    }
  	    try {
  	    	while(shouldRun) {
  	    		InputStream ins = sock.getInputStream();
  	    		if(DEBUG)
  	    			System.out.println("RelatrixJsonClientTransaction "+sock+" bound:"+sock.isBound()+" closed:"+sock.isClosed()+" connected:"+sock.isConnected()+" input shut:"+sock.isInputShutdown()+" output shut:"+sock.isOutputShutdown());
  	    		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
  	    		String inLine = in.readLine();
  	    		if(DEBUG) {
  	    			System.out.println("RelatrixJsonClientTransaction "+sock+" raw data:"+inLine);
  	    		}
  	    		RelatrixTransactionStatement iori = new Gson().fromJson(inLine,RelatrixTransactionStatement.class);
  	    		// get the original request from the stored table
  	    		if( DEBUG )
  	    			System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);

  	    		Object o = iori.getObjectReturn();
  	    		Class<?> returnClass = Class.forName(iori.getReturnClass());

  	    		// intercept remote stream, returned as a server side remote iterator and session info to communicate with remotely
  	    		// mostly, we just need the session and the fact its a type of stream with encapsulated iterator
  	    		if(o.getClass() == com.google.gson.internal.LinkedTreeMap.class) {
  	    			// try to extract object returned from remote method call encapsulated in returned request
  	    			JsonObject retClassIndex = parse(inLine);
  	    			String retClassName = parseString(retClassIndex,"returnClass");
  	    			JsonObject retObjectData = null;
  	    			boolean parsedJson = false;
  	    			try {
  	    				if(!retClassName.isEmpty()) {
  	    					if(DEBUG)
  	    						System.out.println("Extracted class name:"+retClassName);
  	    					retObjectData = retClassIndex.getAsJsonObject("objectReturn");
  	    					if(!retObjectData.isJsonNull()) {
  	    							if(DEBUG)
  	    								System.out.println("Extracted data:"+retObjectData);
  	    							o = new Gson().fromJson(retObjectData,Class.forName(retClassName));
  	    							parsedJson = true;			
  	    					} else {
  	    						if(DEBUG)
  	    							System.out.println("objectRetrun null");
  	    					}
  	    				} else
  	    					if(DEBUG)
  	    						System.out.println("returnClass empty or null");
  	    			} catch(Exception e) {
  	    				if(DEBUG) {
  	    					System.out.println("Failed to parse extracted data:");
  	    					e.printStackTrace();
  	    				}
  	    				parsedJson = false;
  	    			}
  	    			// if we failed to parse the Json payload, try to get something from the
  	    			// Gson linked map
  	    			if(!parsedJson) {
  	    				LinkedTreeMap map = (com.google.gson.internal.LinkedTreeMap) o;
  	    				if(DEBUG)
  	    					System.out.println("RelatrixJsonClientTransaction return object LinkedTreeMap:"+map);
  	    				o = JsonUtil.jsonMapToObject(returnClass,map);
  	    			}
  	    		}
  	    		if( o instanceof Exception ) {
  	    			System.out.println("RelatrixJsonClientTransaction: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
  	    			o = ((Throwable)o).getCause();
  	    		}
  	    		RelatrixTransactionStatement rs = outstandingRequests.get(iori.getSession());
  	    		if( rs == null ) {
  	    			in.close();
  	    			throw new Exception("REQUEST/RESPONSE MISMATCH, statement:"+iori);
  	    		} else {
  	    			if(DEBUG) {
  	    				System.out.printf("%s run response loop recieved class:%s %s%n", this.getClass().getName(),o.getClass().getName(),o);
  	    			}
  	    			if(o instanceof Iterator)
  	    				((RemoteCompletionInterface)o).process();
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
	 * Starting point to process raw Json text and get JsonObject back
	 * @param jsonLine source text
	 * @return JsonObject processed from input text
	 */
	public JsonObject parse(String jsonLine) {
	    JsonElement jelement = JsonParser.parseString(jsonLine);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    return jobject;
	}
	/** 
	 * @param jobject the map from source text parsed
	 * @param jsonElem the element of the map to get
	 * @return the element as string
	 */
	public String parseString(JsonObject jobject, String jsonElem) {
	    String result = jobject.get(jsonElem).getAsString();
	    return result;
	}
	/** 
	 * @param jobject the map from source text parsed
	 * @param jsonElem the element of the map to get
	 * @return the element as long
	 */
	public long parseLong(JsonObject jobject, String jsonElem) {
	    JsonPrimitive result = jobject.get(jsonElem).getAsJsonPrimitive();
	    return result.getAsLong();
	}
	/** 
	 * @param jobject the map from source text parsed
	 * @param jsonElem the element of the map to get, presumed array of longs
	 * @param arrayElem the element of the array
	 * @return the element as long
	 */
	public long parseArrayLong(JsonObject jobject, String jsonElem, int arrayElem) {
	    JsonArray jarray = jobject.getAsJsonArray(jsonElem);
	    JsonObject jobjectx = jarray.get(arrayElem).getAsJsonObject();
	    JsonPrimitive result = jobjectx.getAsJsonPrimitive();
	    return result.getAsLong();
	}
	
	/**
	 * Send request to remote worker, if workerSocket is null open SLAVEPORT connection to remote master
	 * @param iori
	 */
	@Override
	public void send(RemoteRequestInterface iori) throws Exception {
		outstandingRequests.put(iori.getSession(), (RelatrixTransactionStatement) iori);
		String iorij = new Gson().toJson(iori);
		OutputStream os = workerSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os, true);
		if(DEBUG)
			System.out.println("Sending "+iorij+" to "+workerSocket);
		out.println(iorij);
		if(DEBUG)
			System.out.println("Sent "+iorij+" to "+workerSocket);
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
		String cpij = new Gson().toJson(cpi);
		OutputStream os = s.getOutputStream();
		PrintWriter out = new PrintWriter(os, true);
		out.println(cpij);
		return s;
	}
	
	
	@Override
	public String toString() {
		return super.toString();
	}
	static int i = 0;
	/**
	 * Generic call to server localaddr, remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixJsonClientTransaction rc = new RelatrixJsonClientTransaction(args[0],args[1],Integer.parseInt(args[2]));
		TransactionId xid = rc.getTransactionId();
		RelatrixTransactionStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(xid,Class.forName(args[3]));
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
		System.out.println(rc.sendCommand(rs));
		rc.endTransaction(xid);
		rc.close();
	}



}
