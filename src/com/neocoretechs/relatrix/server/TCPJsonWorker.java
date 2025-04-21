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

import org.json.JSONObject;

import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;


public class TCPJsonWorker extends TCPWorker {
	private static boolean DEBUG = true;

	public TCPJsonWorker(Socket datasocket, String remoteMaster, int masterPort) throws IOException {
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
			System.out.println("Adding response "+irf+" to outbound from "+this.getClass().getName()+" to "+IPAddress+" port:"+MASTERPORT);
		}
		try {
			// cant call getObjectReturn from irf or it will unpack transport
			// Write response to master for forwarding to client
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
					System.out.println("TCPJsonWorker InputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				BufferedReader in = new BufferedReader(new InputStreamReader(ins));
				String sobj = in.readLine();
				if(sobj == null)
					continue;
				JSONObject inJson = new JSONObject(sobj);
				if(DEBUG)
					System.out.println("TCPJsonWorker read "+inJson+" from "+workerSocket);
				RelatrixStatement iori = (RelatrixStatement) inJson.toObject();//,RelatrixStatement.class);	
				if( DEBUG ) {
					System.out.println("TCPJsonWorker FROM REMOTE on port:"+workerSocket+" "+iori);
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
				System.out.println("TCPJsonWorker closing:"+workerSocket);
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
