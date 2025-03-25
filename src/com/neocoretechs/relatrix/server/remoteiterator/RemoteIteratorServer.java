package com.neocoretechs.relatrix.server.remoteiterator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.server.CommandPacketInterface;
import com.neocoretechs.relatrix.server.TCPServer;
import com.neocoretechs.relatrix.server.TCPWorker;
import com.neocoretechs.relatrix.server.ThreadPoolManager;

public class RemoteIteratorServer extends TCPServer {

	private ConcurrentHashMap<String, TCPWorker> dbToWorker = new ConcurrentHashMap<String, TCPWorker>();
	private boolean DEBUG;
	
	public RemoteIteratorServer(String iteratorClass, InetAddress host, int port) throws IOException, ClassNotFoundException {
		super();
		startServer(host, iteratorClass);
	}
	
	@Override
	public void run() {
		while(!shouldStop) {
			try {
				Socket datasocket = server.accept();
				// disable Nagles algoritm; do not combine small packets into larger ones
				datasocket.setTcpNoDelay(true);
				// wait 1 second before close; close blocks for 1 sec. and data can be sent
				datasocket.setSoLinger(true, 1);
				//
				ObjectInputStream ois = new ObjectInputStream(datasocket.getInputStream());
				CommandPacketInterface o = (CommandPacketInterface) ois.readObject();
				if( DEBUG )
					System.out.println("Iterator Server command received:"+o);
				// if we get a command packet with no statement, assume it to start a new instance

				TCPWorker uworker = dbToWorker.get(o.getRemoteMaster()+":"+o.getMasterPort());
				if( uworker != null ) {
					if(o.getTransport().equals("TCP")) {
						if( uworker.shouldRun )
							uworker.stopWorker();
					}
				}                   
				// Create the worker, it in turn creates a WorkerRequestProcessor
				uworker = new TCPWorker(datasocket, o.getRemoteMaster(), o.getMasterPort());
				dbToWorker.put(o.getRemoteMaster()+":"+o.getMasterPort(), uworker); 
				ThreadPoolManager.getInstance().spin(uworker);

				if( DEBUG ) {
					System.out.println("RelatrixServer starting new worker "+uworker+
							//( rdb != null ? "remote db:"+rdb : "" ) +
							" master port:"+o.getMasterPort());
				}

			} catch(Exception e) {
				System.out.println("Relatrix Server node configuration server socket accept exception "+e);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
}
