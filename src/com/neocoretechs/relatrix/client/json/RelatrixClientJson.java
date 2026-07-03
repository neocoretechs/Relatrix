package com.neocoretechs.relatrix.client.json;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClient;

/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
 * that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.<p>
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2026
 */
public class RelatrixClientJson extends AsynchRelatrixClient {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static boolean SHOWDUPEKEYEXCEPTION = true;

	/**
	 * Start a Relatrix client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode The remote Node
	 * @param remotePort The remote Port
	 * @throws IOException if connect fail
	 */
	public RelatrixClientJson(String remoteNode, int remotePort)  throws IOException {
		super(remoteNode, remotePort);
	}

	static int i = 0;
	/**
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixClientJson rc = new RelatrixClientJson(args[1],Integer.parseInt(args[2]));
		RelatrixKVStatementJson rs = null;
		switch(args.length) {
		case 4:
			CompletableFuture<Iterator> it = rc.entrySet(Class.forName(args[3]));
			it.get().forEachRemaining(e ->{	
				System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
			});
			System.exit(0);
		case 5:
			rs = new RelatrixKVStatementJson(args[3],args[4]);
			break;
		case 6:
			rs = new RelatrixKVStatementJson(args[3],args[4],args[5]);
			break;
		case 7:
			rs = new RelatrixKVStatementJson(args[3],args[4],args[5],args[6]);
			break;
		case 8:
			rs = new RelatrixKVStatementJson(args[3],args[4],args[5],args[6],args[7]);
			break;
		default:
			System.out.println("Cant process argument list of length:"+args.length);
			return;
		}
		CompletableFuture<Object> cf = rc.queueCommand(rs);
		System.out.println(cf.get());
		//rc.send(rs);
		rc.close();
	}

}
