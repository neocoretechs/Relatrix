package com.neocoretechs.relatrix.client.json;

import java.io.IOException;

import java.lang.reflect.Constructor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.SocketChannel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.ClientInterface;
import com.neocoretechs.relatrix.client.RelatrixKVClientInterfaceImpl;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteStream;

import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

import com.neocoretechs.relatrix.server.json.RelatrixJsonServer;


/**
 * This class functions as client to the RelatrixKVServer Worker threads located on a remote node.<p/>
 * the sockets that the server thread uses to service the traffic
 * from this client.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 */
public class RelatrixJsonKVClient extends RelatrixKVClientInterfaceImpl implements ClientInterface, Runnable {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // remoteNode is ignored and get getLocalHost is used
	public static boolean SHOWDUPEKEYEXCEPTION = false;
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	private String bootNode, remoteNode;
	private int remotePort;
	
	protected int MASTERPORT = 9376; // master port, accepts connection from remote server
	protected int SLAVEPORT = 9377; // slave port, conects to remote, sends outbound requests to master port of remote
	
	protected InetAddress IPAddress = null; // remote server address

	protected SocketChannel workerSocket = null; // socket assigned to slave port
	
	protected ConcurrentHashMap<String, RelatrixJsonKVStatement> outstandingRequests = new ConcurrentHashMap<String,RelatrixJsonKVStatement>();

	/**
	 * Start a Relatrix client to a remote server.
	 * @param bootNode
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixJsonKVClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
		this.bootNode = bootNode;
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		if( TEST ) {
			IPAddress = InetAddress.getLocalHost();
		} else {
			IPAddress = InetAddress.getByName(remoteNode);
		}
		if( DEBUG ) {
			System.out.println(this.getClass().getName()+" constructed with remote:"+IPAddress);
		}
		//
		// Wait for master server node to connect back to here for return channel communication
		//
		SLAVEPORT = remotePort;
		// send message to spin connection
		//workerSocket = RelatrixServer.Fopen(bootNode, MASTERPORT, IPAddress, SLAVEPORT);
		workerSocket = SocketChannel.open(new InetSocketAddress(IPAddress, SLAVEPORT));
		// spin up 'this' to receive connection request from remote server 'slave' to our 'master'
		SynchronizedThreadManager.getInstance().spin(this);
	}
	
	@Override
	public void run() {
  	    try {
		  while(shouldRun ) {
			String s = new String(RelatrixJsonServer.readUntil(workerSocket, (byte)'\n'));
			JSONObject jobj = new JSONObject(s);
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
	 * @param s
	 */
	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		if(DEBUG) {
			System.out.println("Attempting to send "+s+" to "+workerSocket);
			if(workerSocket != null)
				System.out.println("Channel connected:"+workerSocket.isConnected());
			else
				System.out.println("Channel NULL!");
		}
		outstandingRequests.put(s.getSession(), (RelatrixJsonKVStatement) s);
		String iorij = JSONObject.toJson(s);
		RelatrixJsonServer.writeLineBlocking(workerSocket, iorij, null);
		if(DEBUG)
			System.out.println(iorij+" sent to "+workerSocket);
		return iorij;
	}
	
	public void close() {
		shouldRun = false;
		synchronized(waitHalt) {
			try {
				waitHalt.wait();
			} catch (InterruptedException ie) {}
		}
		SynchronizedThreadManager.getInstance().shutdown(); // client threads
	}
	
	protected void shutdown() {
		if( workerSocket != null ) {
			try {
				workerSocket.close();
			} catch (IOException e) {}
		}
		shouldRun = false;
	}
	
	@Override
	public String toString() {
		return String.format("%s RemoteNode:%s RemotePort:%d output socket%s%n",this.getClass().getName(), remoteNode, remotePort, workerSocket);
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
