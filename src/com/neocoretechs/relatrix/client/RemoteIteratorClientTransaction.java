package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.io.Serializable;

import java.util.Iterator;
import java.util.Map;

import com.neocoretechs.rocksack.TransactionId;

/**
 * Manages remote iterators via client that is serialized to remote transaction servers and returned as payload.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class RemoteIteratorClientTransaction extends RemoteIteratorClient implements Runnable, RelatrixTransactionStatementInterface, Serializable, Iterator {
	private static final long serialVersionUID = 1L;
	public static final boolean DEBUG = false;
	public static final boolean LOCALTEST = false; // use localhost as remote node
	public static final boolean TEST = false; // timing
	private long tim;
	
	private TransactionId transactionId;

	/**
	 * Start a client to a remote server. A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param bootNode Name of local master socket
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RemoteIteratorClientTransaction(TransactionId transactionId, String remoteNode, int remotePort)  throws IOException {
		super(remoteNode, remotePort);
		this.transactionId = transactionId;
	}
	
	public RemoteIteratorClientTransaction() {
		super();
	}
	
	@Override
	public String toString() {
		return String.format("%s transaction Id:%s%n",super.toString(),this.transactionId);
	}

	@Override
	public TransactionId getTransactionId() {
		return transactionId;
	}
	
	/**
	 * Generic call to server localaddr, remote addr, port, server method, arg1 to method, arg2 to method...
	 * java RemoteIteratorClientTransaction VOLVATRON VOLVATRON 9010 java.lang.String
	 * @param args
	 * @throws Exception
	 */
	static int i = 0;
	public static void main(String[] args) throws Exception {
		RelatrixClientTransaction rc = new RelatrixClientTransaction(args[1],Integer.parseInt(args[2]));
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
		rc.close(rs);
	}
}
