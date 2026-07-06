package com.neocoretechs.relatrix.client.json;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.neocoretechs.relatrix.RelatrixKVJson;

import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixKVClient;

/**
 * This class functions as client to the RelatrixServer Worker threads located on a remote node.
 * that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * The client thread initiates with a CommandPacketInterface.<p/>
 *
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2026
 */
public class RelatrixKVClientJson extends RelatrixKVClientInterfaceJsonImpl {
	private static final boolean DEBUG = true;
	public static final boolean TEST = false; // true to run in local cluster test mode
	public static boolean SHOWDUPEKEYEXCEPTION = true;
	AsynchRelatrixKVClient asynchClient;

	/**
	 * Start a Relatrix client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode The remote Node
	 * @param remotePort The remote Port
	 * @throws IOException if connect fail
	 */
	public RelatrixKVClientJson(String remoteNode, int remotePort)  throws IOException {
		asynchClient = new AsynchRelatrixKVClient(remoteNode, remotePort);
	}

	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		CompletableFuture<Object> cf = asynchClient.queueCommand(s);
		return cf.get();
	}
	
	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The next iterated object or null
	 */
	public Object next(RelatrixStatementInterface rii) throws Exception {
		rii.setMethodName("next");
		rii.setParamArray(new Object[0]);
		return sendCommand(rii);
	}

	/**
	 * Called for the various 'findSet' methods.
	 * The original request is preserved according to session GUID and upon return of
	 * object the value is transferred
	 * @param rii RelatrixStatement
	 * @return The boolean result of hasNext on server
	 */	
	public boolean hasNext(RelatrixStatementInterface rii) throws Exception {
		rii.setMethodName("hasNext");
		rii.setParamArray(new Object[0]);
		return (boolean) sendCommand(rii);
	}

	public void close(RelatrixStatementInterface rii) throws Exception {
		rii.setMethodName("next");
		rii.setParamArray(new Object[0]);
		sendCommand(rii);
	}
	
	static int i = 0;
	/**
	 * Generic call to server localaddr, remotes addr, port, method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixKVClientJson rc = new RelatrixKVClientJson(args[1],Integer.parseInt(args[2]));
		RelatrixKVStatementJson rs = null;
		switch(args.length) {
		case 4:
			Iterator it = rc.entrySet(Class.forName(args[3]));
			it.forEachRemaining(e ->{	
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
		System.out.println(rc.sendCommand(rs));
		//rc.send(rs);
		rc.close(rs);
	}

}
