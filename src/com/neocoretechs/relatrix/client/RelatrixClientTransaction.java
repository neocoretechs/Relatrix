package com.neocoretechs.relatrix.client;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.TransactionId;

import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixClientTransaction;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;


/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixTransactionServer} 
 * Worker threads located on a remote node. It carries the transaction identifier to maintain transaction context.
 *
 * In a transaction context, we must obtain a transaction Id from the server for the lifecycle of the transaction.<p/>
 * The transaction Id may outlive the session, as the session is transitory for communication purposes.
 * The {@link RelatrixTransactionStatement} contains the transaction Id.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class RelatrixClientTransaction extends RelatrixClientTransactionInterfaceImpl {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	private Object mutex = new Object();
	private AsynchRelatrixClientTransaction asynchClient;
	private String remoteNode;
	private int remotePort;
	
	public RelatrixClientTransaction() { }
	
	/**
	 * Start a Relatrix client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixClientTransaction(String remoteNode, int remotePort)  throws IOException {
		super();
		this.remoteNode = remoteNode;
		this.remotePort = remotePort;
		asynchClient = new AsynchRelatrixClientTransaction(remoteNode, remotePort);
	}
	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		synchronized(mutex) {
		if(DEBUG)
			System.out.printf("%s.sendCommand statement=%s%n", this.getClass().getName(), s);
		CompletableFuture<Object> cf = asynchClient.queueCommand((RelatrixTransactionStatementInterface) s);
		//if(DEBUG)
			//System.out.printf("%s.sendCommand returned=%s%n", this.getClass().getName(), cf.get());
		return cf.get();
		}
	}
	
	@Override
	public UUID getSession() {
		return asynchClient.getSession();
	}
	
	@Override
	public String getRemoteNode() {
		return remoteNode;
	}
	@Override
	public int getRemotePort() {
		return remotePort;
	}
	/**
	 * Set the client {@link RemoteIteratorClient} to a previously established remote iterator server.
	 * The purpose of this is to prevent a new connection to a remote server inside a loop. The existing
	 * client will be re-used rather than creating a new client connection to the remote iterator server.
	 * This is critical for large queries that contain nested queries as exhaustion of remote connections
	 * can occur otherwise.
	 * @param it The previously established iterator
	 */
	public void setIterator(Iterator<?> it) {
		asynchClient.setIterator(((RemoteIteratorClient)it));
	}
	/**
	 * Get the {@link RemoteStream} from the Stream, then the {@link RemoteIteratorClient} from the RemoteStream, then the {@link AsynchRelatrixClientTransaction} from the RemoteIteratorClient.
	 * The purpose of this is to prevent a new connection to a remote server inside a loop. The existing
	 * client will be re-used rather than creating a new client connection to the remote server.
	 * This is critical for large queries that contain nested queries as exhaustion of remote connections
	 * can occur otherwise.
	 * @param st The previously established stream
	 */
	public void setStream(Stream st) {
		asynchClient.setIterator((((RemoteStream)st).getClient()));//.getClient().getIterator());
	}	
	static int i = 0;
	/**
	 * Generic call to server remote addr, port, server method, arg1 to method, arg2 to method...
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixClientTransaction rc = new RelatrixClientTransaction(args[0],Integer.parseInt(args[1]));
		TransactionId xid = rc.getTransactionId();
		RelatrixTransactionStatement rs = null;
		switch(args.length) {
			case 4:
				Iterator it = rc.entrySet(xid,Class.forName(args[2]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				System.exit(0);				
				break;
			case 5:
				rs = new RelatrixTransactionStatement(rc.getSession(),args[2],args[3],xid,args[4]);
				break;
			case 6:
				rs = new RelatrixTransactionStatement(rc.getSession(),args[2],args[3],xid,args[4]);
				break;
			case 7:
				rs = new RelatrixTransactionStatement(rc.getSession(),args[2],args[3],xid,args[4],args[5]);
				break;
			case 8:
				rs = new RelatrixTransactionStatement(rc.getSession(),args[2],args[3],xid,args[4],args[5],args[6]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		System.out.println(rc.sendCommand(rs));
		rc.endTransaction(xid);
	}

}
