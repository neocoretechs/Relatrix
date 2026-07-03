package com.neocoretechs.relatrix.client;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixKVClient;
import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixKVClientTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * This class functions as client to the {@link com.neocoretechs.relatrix.server.RelatrixKVTransactionServer}
 * Worker threads located on a remote node.<p/>
 * 
 * In the current context, this client node functions as 'master' to the remote 'worker' or 'slave' node
 * which is on the RelatrixKVTransactionServer that correspond to the sockets that the server thread uses to service the traffic
 * from this client. Likewise this client has a master worker thread that handles traffic back from the server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 */
public class RelatrixKVClientTransaction extends RelatrixKVClientTransactionInterfaceImpl {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // true to run in local cluster test mode
	private Object mutex = new Object();
	private AsynchRelatrixKVClientTransaction asynchClient;

	/**
	 * Start a Relatrix client to a remote server.  A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixKVClientTransaction(String remoteNode, int remotePort)  throws IOException {
		RelatrixKVTransaction.getInstance(this);
		asynchClient = new AsynchRelatrixKVClientTransaction(remoteNode, remotePort);
	}
	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		synchronized(mutex) {
		if(DEBUG)
			System.out.printf("%s.sendCommand statement=%s%n", this.getClass().getName(), s);
		CompletableFuture<Object> cf = asynchClient.queueCommand((RelatrixKVTransactionStatementInterface) s);
		//if(DEBUG)
			//System.out.printf("%s.sendCommand returned=%s%n", this.getClass().getName(), cf.get());
		return cf.get();
		}
	}
	@Override
	public void storekv(TransactionId xid, Comparable index, Object instance) throws IOException {
		asynchClient.storekv(xid, index, instance);	
	}
	@Override
	public void storekv(Alias alias, TransactionId xid, Comparable index, Object instance) throws IOException {
		asynchClient.storekv(alias, xid, index, instance);;
	}
	@Override
	public Object getByIndex(TransactionId xid, Comparable index) throws IOException {
		return asynchClient.getByIndex(xid, index);
	}
	@Override
	public Object getByIndex(Alias alias, TransactionId xid, Comparable index) throws IOException {
		return asynchClient.getByIndex(alias, xid, index);
	}
	@Override
	public Object get(TransactionId xid, Comparable instance) throws IOException {
		return asynchClient.get(xid, instance);
	}
	@Override
	public Object get(Alias alias, TransactionId xid, Comparable instance) throws IOException {
		return asynchClient.get(alias, xid, instance);
	}
	@Override
	public void remove(TransactionId xid, Comparable instance) throws IOException {
		asynchClient.remove(xid, instance);
	}
	@Override
	public void remove(Alias alias, TransactionId xid, Comparable instance) throws IOException {
		asynchClient.remove(alias, xid, instance);
	}
	public void close() throws IOException {
		asynchClient.close();
	}
	static int i = 0;
	/**
	 * case 4:
	 * <dd>Generic call to server: remote addr, port, class
	 * <dd>Displays entry set stream of class from database running on addr and port
	 * <dd>case 5-8:
	 * <dd>Call to server method: remote addr, port, server_method <arg1> <arg2> ... 
	 * <dd>Invokes named method on the server at host and port using the given string arguments.<p/>
	 * Note that method must accept the number of string arguments provided, such as loadClassFromJar <jar>
	 * and loadClassFromPath <package> <path> and removePackageFromRepository <package>.<p/>
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RelatrixKVTransactionStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		i = 0;
		RelatrixKVClientTransaction rc = new RelatrixKVClientTransaction(args[0],Integer.parseInt(args[1]));
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
				Iterator it = rc.entrySet(xid,Class.forName(args[2]));
				it.forEachRemaining(e ->{	
					System.out.println(++i+"="+((Map.Entry)(e)).getKey()+" / "+((Map.Entry)(e)).getValue());
				});
				rc.endTransaction(xid);
				System.exit(0);
			case 5:
				rs = new RelatrixKVTransactionStatement(args[2],xid,args[3]);
				break;
			case 6:
				rs = new RelatrixKVTransactionStatement(args[2],args[3],xid,args[4]);
				break;
			case 7:
				rs = new RelatrixKVTransactionStatement(args[2],args[3],xid,args[4],args[5]);
				break;
			case 8:
				rs = new RelatrixKVTransactionStatement(args[2],args[3],xid,args[4],args[5],args[6]);
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
