package com.neocoretechs.relatrix.client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.StandardSocketOptions;

import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

import com.neocoretechs.relatrix.RelatrixKVJson;

/**
 * Server-side socket accept connection handler
 */
public class ConnectionHandler {
	private static boolean DEBUG = true;
	protected SocketChannel channel;
	private Object mutexWrite = new Object();
	private Object mutexRead = new Object();
	public ConnectionHandler() {}
	
	public ConnectionHandler(SocketChannel ch) throws IOException {
		this.channel = ch;
		ch.configureBlocking(true);
		ch.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		ch.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
		ch.setOption(StandardSocketOptions.SO_SNDBUF, 32767);
		ch.setOption(StandardSocketOptions.TCP_NODELAY, true);
		if(DEBUG)
			System.out.printf("%s channel:%s%n",this.getClass().getName(), ch);	
	}

	// Single-threaded writer or synchronized
	public void sendObject(Object obj) throws IOException {
		if(DEBUG)
			System.out.printf("%s send object %s channel:%s%n",this.getClass().getName(), obj, this.toString());
		sendObjectFramed(obj);
		if(DEBUG)
			System.out.printf("%s sent object %s channel:%s%n",this.getClass().getName(), obj, this.toString());
	}

	public Object readObject() throws IOException, ClassNotFoundException {	
		return receiveObjectFramed();
	}
	
	public void close() {
		if(DEBUG)
			System.out.printf("%s CLOSING CHANNEL:%s%n",this.getClass().getName(), this.toString());
		try { channel.close(); } catch (IOException ignored) {}
	}
	
	public void sendObjectFramed(Object obj) throws IOException {
	    byte[] payload = RelatrixKVJson.serializeObject(obj);
	    synchronized(mutexWrite) {
	    	OutputStream os = Channels.newOutputStream(channel);
	    	DataOutputStream dos = new DataOutputStream(os);
	    	dos.writeInt(payload.length);
	    	dos.write(payload);
	    	dos.flush();
	    }
	}
	
	/** Blocking framed receive using Channels.newInputStream(channel) */
	public Object receiveObjectFramed() throws IOException, ClassNotFoundException {
		byte[] payload;
		if(DEBUG)
			System.out.printf("%s attempt to recieve object channel:%s%n",this.getClass().getName(), this.toString());
		synchronized(mutexRead) {
			InputStream in = Channels.newInputStream(channel);
			DataInputStream dis = new DataInputStream(in);
			// Read length prefix (big-endian int)
			int len;
			try {
				len = dis.readInt(); // blocks until 4 bytes available or EOF
			} catch (EOFException eof) {
				return null; // remote closed cleanly
			}
			if (len <= 0) 
				throw new IOException("Invalid frame length: " + len);
			payload = new byte[len];
			dis.readFully(payload); // blocks until payload read or throws EOFException
		}
		if(DEBUG)
			System.out.printf("%s recieved object %s len: %s channel:%s%n",this.getClass().getName(),RelatrixKVJson.deserializeObject(payload), String.valueOf(payload.length),this.toString());
	    // Deserialize from the frame bytes (fresh OIS per message)
	    return RelatrixKVJson.deserializeObject(payload);
	}

	@Override
	public String toString() {
		String local = "UNKNOWN";
		String remote = "UNKNOWN";
		try {
			remote = channel.getRemoteAddress().toString();
		} catch (IOException e) {}
		try {
			local = channel.getLocalAddress().toString();
		} catch (IOException e) {}
		return String.format("%s local=%s remote=%s isBlocking=%b isConnected=%b isPending=%b isOpen=%b",this.getClass().getName(), local, remote, channel.isBlocking(), channel.isConnected(), channel.isConnectionPending(), channel.isOpen());
	}
}



