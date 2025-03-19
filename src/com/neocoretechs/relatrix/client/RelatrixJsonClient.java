package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.server.CommandPacket;
import com.neocoretechs.relatrix.server.CommandPacketInterface;

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
 * The client thread initiates with a CommandPacketInterface.<p/>
 * The special case is the {@link RemoteIterator}, which is a proxy to the 'next' and 'hasNext' methods here,
 * such that we can deliver the RemoteIterator and treat it as an abstract Iterator to simply call next and hasNext on the
 * Iterator interface. 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixJsonClient extends RelatrixClient {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static boolean SHOWDUPEKEYEXCEPTION = true;
	
	Jsonb jsonb = JsonbBuilder.create();
	byte[] buf = new byte[4096];
	
	private volatile boolean shouldRun = true; // master service thread control
	private Object waitHalt = new Object(); 
	
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
	public RelatrixJsonClient(String bootNode, String remoteNode, int remotePort)  throws IOException {
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
			System.out.println("RelatrixJsonClient server socket accept failed with "+e1);
			shutdown();
			return;
		}
  	    if( DEBUG ) {
  	    	 System.out.println("RelatrixJsonClient got connection "+sock);
  	    }
  	    try {
		  while(shouldRun ) {
				InputStream ins = sock.getInputStream();
				if(DEBUG)
					System.out.println("RelatrixJsonClient "+sock+" bound:"+sock.isBound()+" closed:"+sock.isClosed()+" connected:"+sock.isConnected()+" input shut:"+sock.isInputShutdown()+" output shut:"+sock.isOutputShutdown());
				//ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//while(true) {
				//  int n = ins.read(buf);
				//  if( n < 0 ) break;
				//  baos.write(buf,0,n);
				//}
				RemoteResponseInterface iori = jsonb.fromJson(ins,RemoteResponseInterface.class);	
				// get the original request from the stored table
				if( DEBUG )
					 System.out.println("FROM Remote, response:"+iori+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				Object o = iori.getObjectReturn();
				if( DEBUG )
					 System.out.println("FROM Remote, returned object from response:"+o+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
				if( o instanceof Exception ) {
					if( !(((Throwable)o).getCause() instanceof DuplicateKeyException) || SHOWDUPEKEYEXCEPTION )
						System.out.println("RelatrixJsonClient: ******** REMOTE EXCEPTION ******** "+((Throwable)o).getCause());
					 o = ((Throwable)o).getCause();
				}
				RelatrixStatement rs = outstandingRequests.get(iori.getSession());
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
		} catch(Exception e) {
			//if(!(e instanceof SocketException)) {
				// we lost the remote master, try to close worker and wait for reconnect
				e.printStackTrace();
				System.out.println(this.getClass().getName()+": receive IO error "+e+" Address:"+IPAddress+" master port:"+MASTERPORT+" slave:"+SLAVEPORT);
			//}
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
		outstandingRequests.put(iori.getSession(), (RelatrixStatement) iori);
		String iorij = jsonb.toJson(iori);
		OutputStream os = workerSocket.getOutputStream();
		os.write(iorij.getBytes());
		os.flush();
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
		String cpij = jsonb.toJson(cpi);
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
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixJsonClient rc = new RelatrixJsonClient(args[0],args[1],Integer.parseInt(args[2]));
		RelatrixStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(Class.forName(args[3]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);
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
