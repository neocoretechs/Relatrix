package com.neocoretechs.relatrix.server.json;

import java.io.EOFException;
import java.io.IOException;

import java.net.SocketException;
import java.nio.channels.SocketChannel;

import org.json.JSONObject;

import com.neocoretechs.relatrix.client.RelatrixKVTransactionStatement;
import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;
import com.neocoretechs.relatrix.server.TCPWorker;

public class TCPJsonKVTransactionWorker extends TCPWorker {
	private static boolean DEBUG = false;

	public TCPJsonKVTransactionWorker(SocketChannel datasocket) throws IOException {
		super(datasocket);
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
			System.out.println("Adding response "+irf+" to outbound from "+this.getClass().getName()+" to "+workerSocket);
		}
		try {
			// Write response to master for forwarding to client
			String jirf = JSONObject.toJson(irf);
			if(DEBUG)
				System.out.println("Sending "+jirf+" to "+workerSocket);
			RelatrixJsonServer.writeLineBlocking(workerSocket, jirf, null);
		} catch (SocketException e) {
				//System.out.println("Exception setting up socket to remote master port "+MASTERPORT+e);
				//throw new RuntimeException(e);
		} catch (IOException e) {
				System.out.println("Channel send error "+e+" to address "+workerSocket);
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
				if(DEBUG)
					System.out.println(this.getClass().getName()+" InputStream "+" connected:"+workerSocket.isConnected());
				String sobj = new String(RelatrixJsonServer.readUntil(workerSocket, (byte)'\n'));
				JSONObject jobj = new JSONObject(sobj);
				RelatrixKVTransactionStatement iori = (RelatrixKVTransactionStatement) jobj.toObject();//(,RelatrixKVTransactionStatement.class);	
				if( DEBUG ) {
					System.out.println(this.getClass().getName()+" FROM REMOTE on port:"+workerSocket+" "+iori);
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
				System.out.println(this.getClass().getName()+" closing:"+workerSocket);
			}
			shouldRun = false;
			try {
				workerSocket.close();
			} catch (IOException e) {}
			synchronized(waitHalt) {
				waitHalt.notify();
			}
		}
	}

}
