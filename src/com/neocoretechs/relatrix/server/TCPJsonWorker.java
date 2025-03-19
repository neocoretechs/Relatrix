package com.neocoretechs.relatrix.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.neocoretechs.relatrix.client.RemoteCompletionInterface;
import com.neocoretechs.relatrix.client.RemoteResponseInterface;

public class TCPJsonWorker extends TCPWorker {
	Jsonb jsonb = JsonbBuilder.create();
	byte[] buf = new byte[4096];
	private boolean DEBUG = false;

	
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
	public void queueResponse(RemoteResponseInterface irf) {
	
		if( DEBUG ) {
			System.out.println("Adding response "+irf+" to outbound from worker to "+IPAddress+" port:"+MASTERPORT);
		}
		try {
			// Write response to master for forwarding to client
			String jirf = jsonb.toJson(irf);
			OutputStream os = masterSocket.getOutputStream();
			os.write(jirf.getBytes());
			os.flush();
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
				if(DEBUG)
					System.out.println("TCPWorker waiting getInputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				InputStream ins = workerSocket.getInputStream();
				if(DEBUG)
					System.out.println("TCPWorker ObjectInputStream "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				//ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//while(true) {
				//  int n = ins.read(buf);
				//  if( n < 0 ) break;
				//  baos.write(buf,0,n);
				//}
				RemoteCompletionInterface iori = jsonb.fromJson(ins,RemoteCompletionInterface.class);	
				if(DEBUG)
					System.out.println("TCPWorker attempt readObject "+workerSocket+" bound:"+workerSocket.isBound()+" closed:"+workerSocket.isClosed()+" connected:"+workerSocket.isConnected()+" input shut:"+workerSocket.isInputShutdown()+" output shut:"+workerSocket.isOutputShutdown());
				if( DEBUG ) {
					System.out.println("TCPWorker FROM REMOTE on port:"+workerSocket+" "+iori);
				}
				// put the received request on the processing stack
				workerRequestProcessor.getQueue().put(iori);
			}
		// Call to shut down has been received from stopWorker
		} catch (IOException | InterruptedException ie) {
			if(!(ie instanceof SocketException) && !(ie instanceof EOFException)) {
				ie.printStackTrace();
				System.out.println("Remote client disconnect with exception "+ie);
			}
		}
		finally {
			shouldRun = false;
			workerRequestProcessor.stop();
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
