package com.neocoretechs.relatrix.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.neocoretechs.bigsack.io.ThreadPoolManager;
import com.neocoretechs.bigsack.io.request.IoResponseInterface;
import com.neocoretechs.bigsack.io.request.cluster.CompletionLatchInterface;

/**
 * @author jg
 * Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class TCPWorker implements Runnable, DistributedWorkerResponseInterface {
	private static final boolean DEBUG = false;
	boolean shouldRun = true;
	public int MASTERPORT = 9876;
	public int SLAVEPORT = 9876;
	private String remoteMaster = "AMIMASTER";
    //private byte[] sendData;
	private InetAddress IPAddress = null;
	//private ServerSocketChannel workerSocketChannel;
	private SocketAddress workerSocketAddress;
	//private SocketChannel masterSocketChannel;
	private SocketAddress masterSocketAddress;
	
	private ServerSocket workerSocket;
	private Socket masterSocket;
	
	private WorkerRequestProcessor workerRequestProcessor;
	// ByteBuffer for NIO socket read/write, currently broken under arm
	//private ByteBuffer b = ByteBuffer.allocate(LogToFile.DEFAULT_LOG_BUFFER_SIZE);
	private static boolean TEST = true;
	
    public TCPWorker(String dbname, String remoteMaster, int masterPort, int slavePort) throws IOException {
    	if( remoteMaster != null )
    		this.remoteMaster = remoteMaster;
    	MASTERPORT= masterPort;
    	SLAVEPORT = slavePort;
		try {
			if(TEST ) {
				IPAddress = InetAddress.getLocalHost();
			} else {
				IPAddress = InetAddress.getByName(remoteMaster);
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException("Bad remote master address:"+remoteMaster);
		}
	
		masterSocketAddress = new InetSocketAddress(IPAddress, MASTERPORT);
		masterSocket = new Socket();
		masterSocket.connect(masterSocketAddress);
		masterSocket.setKeepAlive(true);
		//masterSocket.setTcpNoDelay(true);
		masterSocket.setReceiveBufferSize(32767);
		masterSocket.setSendBufferSize(32767);
		// start listening on the required worker port
		workerSocketAddress = new InetSocketAddress(SLAVEPORT);
		workerSocket = new ServerSocket();
		workerSocket.bind(workerSocketAddress);
		// spin the request processor thread for the worker
		workerRequestProcessor = new WorkerRequestProcessor(this);
		ThreadPoolManager.getInstance().spin(workerRequestProcessor);
		if( DEBUG ) {
			System.out.println("Worker on port "+SLAVEPORT+" with master "+MASTERPORT+" database:"+dbname+
					" address:"+IPAddress);
		}
	}
    
	
	/**
	 * Queue a request on this worker, the request is assumed to be on this tablespace
	 * Instead of queuing to a running thread request queue, queue this for outbound message
	 * The type is IOResponseInterface and contains the Id and the payload
	 * back to master
	 * @param irf
	 */
	public void queueResponse(IoResponseInterface irf) {
	
		if( DEBUG ) {
			System.out.println("Adding response "+irf+" to outbound from worker to "+IPAddress+" port:"+MASTERPORT);
		}
		try {
			// connect to the master and establish persistent connect
			//sendData = GlobalDBIO.getObjectAsBytes(irf);
			//ByteBuffer srcs = ByteBuffer.wrap(sendData);
			//masterSocketChannel.write(srcs);
			OutputStream os = masterSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(irf);
			oos.flush();
		} catch (SocketException e) {
				System.out.println("Exception setting up socket to remote master port "+MASTERPORT+" on local port "+SLAVEPORT+" "+e);
				throw new RuntimeException(e);
		} catch (IOException e) {
				System.out.println("Socket send error "+e+" to address "+IPAddress+" on port "+MASTERPORT);
				throw new RuntimeException(e);
		}
	}
	/**
     * Spin the worker, get the tablespace from the cmdl param
     * @param args
     * @throws Exception
     */
	public static void main(String args[]) throws Exception {
		if( args.length < 4 ) {
			System.out.println("Usage: java com.neocoretechs.relatrix.server.TCPWorker [database] [remote master] [master port] [slave port]");
		}
		// Use mmap mode 0
		ThreadPoolManager.getInstance().spin(new TCPWorker(
				args[0], // database
				args[1], // remote master node
				Integer.valueOf(args[2]) , // master port
				Integer.valueOf(args[3]))); //worker port
	}
	
	@Override
	public void run() {
		//SocketChannel s = null;
		Socket s = null;
		try {
			/*
			s = workerSocketChannel.accept();
			s.configureBlocking(true);
			s.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			s.setOption(StandardSocketOptions.TCP_NODELAY, true);
			s.setOption(StandardSocketOptions.SO_SNDBUF, 32767);
			s.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
			*/
			s = workerSocket.accept();
			s.setKeepAlive(true);
			//s.setTcpNoDelay(true);
			s.setSendBufferSize(32767);
			s.setReceiveBufferSize(32767);
		} catch (IOException e) {
			System.out.println("TCPWorker socket accept exception "+e+" on port "+SLAVEPORT);
			return;
		}
		while(shouldRun) {
			try {
				//s.read(b);
				// extract the serialized request
				//final CompletionLatchInterface iori = (CompletionLatchInterface)GlobalDBIO.deserializeObject(b);
				//b.clear();
				InputStream ins = s.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(ins);
				CompletionLatchInterface iori = (CompletionLatchInterface)ois.readObject();
				if( DEBUG ) {
					System.out.println("TCPWorker FROM REMOTE on port:"+SLAVEPORT+" "+iori);
				}
				// put the received request on the processing stack
				workerRequestProcessor.getQueue().put(iori);
			} catch(IOException ioe) {
				System.out.println("TCPWorker receive exception "+ioe+" on port "+SLAVEPORT);
				break;
			} catch (InterruptedException e) {
				// the condition here is that the blocking request queue was waiting on a 'put' since the
				// queue was at maximum capacity, and a the ExecutorService requested a shutdown during that
				// time, we should bail form the thread and exit
			    // quit the processing thread
			    break;
			} catch (ClassNotFoundException e) {
				System.out.println("TCPWorker class not found on deserialization"+e+" on port "+SLAVEPORT);
				break;
			} 
		}
		// thread has been stopped by WorkBoot or by error
		try {
			s.close();
			/*
			if( masterSocketChannel.isOpen() ) masterSocketChannel.close();
			if( workerSocketChannel.isOpen() ) workerSocketChannel.close();
			*/
			if(!masterSocket.isClosed()) masterSocket.close();
			if(!workerSocket.isClosed()) workerSocket.close();
			workerRequestProcessor.stop();
		} catch (IOException e) {}
	}


	public String getMasterPort() {
		return String.valueOf(MASTERPORT);
	}


	public String getSlavePort() {
		return String.valueOf(SLAVEPORT);
	}

	public void stopWorker() {
		// thread has been stopped by executor
		shouldRun = false;
	}
}
