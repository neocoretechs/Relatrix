package com.neocoretechs.relatrix.server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.neocoretechs.relatrix.client.RelatrixTransactionStatement;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;


public class TCPJsonTransactionWorker extends TCPWorker {
	private static boolean DEBUG = true;

	public TCPJsonTransactionWorker(Socket datasocket, String remoteMaster, int masterPort) throws IOException {
		super(datasocket, remoteMaster, masterPort);
	}
	/**
	 * Queue a request on this worker,
	 * Instead of queuing to a running thread request queue, queue this for outbound message
	 * The type is RemoteCompletionInterface and contains the Id and the payload
	 * back to master
	 * @param irf
	 */
	@Override
	public void sendResponse(RemoteResponseInterface irf) {
	
		if( DEBUG ) {
			System.out.println("Adding response "+irf+" to outbound from worker to "+IPAddress+" port:"+MASTERPORT);
		}
		try {
			// can't call getObjectReturn or it will unpack transport
			String jirf = JSONObject.toJson(irf);
			if(DEBUG)
				System.out.println("Sending "+jirf+" to "+masterSocket);
			OutputStream os = masterSocket.getOutputStream();
			PrintWriter out = new PrintWriter(os, true);
			out.println(jirf);
		} catch (SocketException e) {
				//System.out.println("Exception setting up socket to remote master port "+MASTERPORT+e);
				//throw new RuntimeException(e);
		} catch (IOException e) {
				System.out.println("Socket send error "+e+" to address "+IPAddress+" on port "+MASTERPORT);
				throw new RuntimeException(e);
		}
	}
	/**
	 * Client (Slave port) sends data to our master in the following loop
	 */
	@Override
	public void run() {
		try {
			while(shouldRun) {
				InputStream ins = workerSocket.getInputStream();
				if(DEBUG)
					System.out.println("TCPJsonTransactionWorker InputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				BufferedReader in = new BufferedReader(new InputStreamReader(ins));
				String sobj = in.readLine();
				if(sobj == null) {
					ins.close();
					break;
				}
				JSONObject jobj = new JSONObject(sobj);
				if(DEBUG) {
					System.out.printf("%s %s%n", this.getClass().getName(),jobj);
				}
				RelatrixTransactionStatement iori = (RelatrixTransactionStatement) jobj.toObject();//,RelatrixTransactionStatement.class);
				if( DEBUG ) {
					System.out.println("TCPJsonTransactionWorker FROM REMOTE on port:"+workerSocket+" "+iori);
					if(iori.getParamArray() != null)
						for(int i = 0; i < iori.getParamArray().length; i++) {
							Object op = iori.getParamArray()[i];
							Class<?> cp = iori.getParams()[i];
							if(op != null) {
								System.out.println(cp+" type:"+op.getClass().getName()+" -- "+op);
								if(op instanceof Map)
									for(Object mp: ((Map)op).entrySet()) {
										Entry emp = (Entry)mp;
										System.out.println("\t"+emp.getKey()+" == "+emp.getValue().getClass().getName()+" -- "+emp.getValue());
									}
							}
						}
				}
				// put the received request on the processing stack
				workerRequestProcessor.getQueue().put((RemoteCompletionInterface) iori);
			}
		// Call to shut down has been received from stopWorker
		} catch (IOException | InterruptedException | InstantiationException | IllegalAccessException ie) {
			if(!(ie instanceof SocketException) && !(ie instanceof EOFException)) {
				ie.printStackTrace();
			}
			System.out.println("Remote client disconnect with exception "+ie);
		}
		finally {
			if( DEBUG ) {
				System.out.println("TCPJsonTransactionWorker closing:"+workerSocket);
			}
			shouldRun = false;
			try {
				workerSocket.close();
			} catch (IOException e) {}
			try {
				masterSocket.close();
			} catch (IOException e) {}
			synchronized(waitHalt) {
				waitHalt.notify();
			}
		}
	}

}
