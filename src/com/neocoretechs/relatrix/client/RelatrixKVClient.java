package com.neocoretechs.relatrix.client;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.neocoretechs.relatrix.client.asynch.AsynchRelatrixKVClient;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.rocksack.Alias;

/**
 * This class functions as client to the RelatrixKVServer Worker threads located on a remote node.<p/>
 * 
 * A worker thread that handles traffic back from the server.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2021
 */
public class RelatrixKVClient extends RelatrixKVClientInterfaceImpl {
	private static final boolean DEBUG = false;
	public static final boolean TEST = false; // remoteNode is ignored and get getLocalHost is used
	public static boolean SHOWDUPEKEYEXCEPTION = false;
	private Object mutex = new Object();
	private AsynchRelatrixKVClient asynchClient;

	/**
	 * Start a Relatrix client to a remote server.  A WorkerRequestProcessor
	 * thread is created to handle the processing of payloads and a comm thread handles the bidirectional traffic to server
	 * @param remoteNode
	 * @param remotePort
	 * @throws IOException
	 */
	public RelatrixKVClient(String remoteNode, int remotePort)  throws IOException {
		asynchClient = new AsynchRelatrixKVClient(remoteNode, remotePort);
	}

	@Override
	public Object sendCommand(RelatrixStatementInterface s) throws Exception {
		synchronized(mutex) {
		if(DEBUG)
			System.out.printf("%s.sendCommand statement=%s%n", this.getClass().getName(), s);
		CompletableFuture<Object> cf = asynchClient.queueCommand(s);
		//if(DEBUG)
			//System.out.printf("%s.sendCommand returned=%s%n", this.getClass().getName(), cf.get());
		return cf.get();
		}
	}
	@Override
	public void storekv(Comparable index, Object instance) throws IOException {
		asynchClient.storekv(index, instance);	
	}
	@Override
	public void storekv(Alias alias, Comparable index, Object instance) throws IOException {
		asynchClient.storekv(alias, index, instance);;
	}
	@Override
	public Object getByIndex(DBKey index) throws IOException {
		return asynchClient.getByIndex(index);
	}
	@Override
	public Object getByIndex(Alias alias, DBKey index) throws IOException {
		return asynchClient.getByIndex(alias, index);
	}
	@Override
	public Object get(Object instance) throws IOException {
		return asynchClient.get(instance);
	}
	@Override
	public Object get(Alias alias, Object instance) throws IOException {
		return asynchClient.get(alias, instance);
	}
	@Override
	public Object remove(Object instance) throws IOException {
		return asynchClient.remove(instance);
	}
	@Override
	public Object remove(Alias alias, Object instance) throws IOException {
		return asynchClient.remove(alias, instance);
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
	 * <dd>Call to server method: remote addr, port, server_method arg1, arg2 ... 
	 * <dd>Invokes named method on the server at host and port using the given string arguments.<p/>
	 * Note that method must accept the number of string arguments provided, such as loadClassFromJar jar
	 * and loadClassFromPath package path and removePackageFromRepository package.<p/>
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//RelatrixKVClient rc = new RelatrixKVClient("localhost","localhost", 9000);
		RelatrixKVStatement rs = null;//new RelatrixKVStatement("toString",(Object[])null);
		//rc.send(rs);
		i = 0;
		RelatrixKVClient rc = new RelatrixKVClient(args[0],Integer.parseInt(args[1]));

		switch(args.length) {
			case 4:
				Iterator it = (Iterator) rc.entrySet(Class.forName(args[2]));
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
				rs = new RelatrixKVStatement(args[2],args[3]);
				break;
			case 6:
				rs = new RelatrixKVStatement(args[2],args[3],args[4]);
				break;
			case 7:
				rs = new RelatrixKVStatement(args[2],args[3],args[4],args[5]);
				break;
			case 8:
				rs = new RelatrixKVStatement(args[2],args[3],args[4],args[5],args[6]);
				break;
			default:
				System.out.println("Cant process argument list of length:"+args.length);
				return;
		}
		System.out.println(rc.sendCommand(rs));
		rc.close();
	}

}
