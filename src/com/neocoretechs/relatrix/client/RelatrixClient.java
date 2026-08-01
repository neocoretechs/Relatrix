package com.neocoretechs.relatrix.client;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClient;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;

/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
 * that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.<p/>
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2026
 */
public class RelatrixClient extends RelatrixClientInterfaceImpl {
	private static final boolean DEBUG = true;
	private Object mutex = new Object();
	private AsynchRelatrixClient asynchClient;

	/**
	 * Start a Relatrix client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode The remote Node
	 * @param remotePort The remote Port
	 * @throws IOException if connect fail
	 */
	public RelatrixClient(String remoteNode, int remotePort)  throws IOException {
		asynchClient = new AsynchRelatrixClient(remoteNode, remotePort);
	}
	public RelatrixClient(String remoteNode, int remotePort, IndexResolver resolver)  throws IOException {
		asynchClient = new AsynchRelatrixClient(remoteNode, remotePort, resolver);
	}
	@Override
	public UUID getSession() {
		return asynchClient.getSession();
	}
	
	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		if(DEBUG)
			System.out.printf("%s.sendCommand statement=%s%n", this.getClass().getName(), s);
		CompletableFuture<Object> cf = asynchClient.queueCommand(s);
		return cf.get(30, TimeUnit.SECONDS);
	}
	@Override
	public String getRemoteNode() {
		return asynchClient.getRemoteNode();
	}
	@Override
	public int getRemotePort() {
		return asynchClient.getRemotePort();
	}
	public ParallelExecutionContext getContext() {
		return asynchClient.getContext();
	}

	static int i = 0;
	/**
	 * Generic call to server remote addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixClient rc = new RelatrixClient(args[0],Integer.parseInt(args[1]));
		RelatrixStatement rs = null;
		switch(args.length) {
		case 4:
			Iterator it = rc.entrySet(Class.forName(args[2]));
			it.forEachRemaining(e ->{	
				System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
			});
			System.exit(0);
		case 5:
			rs = new RelatrixStatement(null,args[2], args[3]);
			break;
		case 6:
			rs = new RelatrixStatement(null,args[2],args[3], args[4]);
			break;
		case 7:
			rs = new RelatrixStatement(null,args[2],args[3],args[4], args[5]);
			break;
		case 8:
			rs = new RelatrixStatement(null,args[2],args[3],args[4],args[5], args[6]);
			break;
		default:
			System.out.println("Cant process argument list of length:"+args.length);
			return;
		}
		System.out.println(rc.sendCommand(rs));
	}


}
