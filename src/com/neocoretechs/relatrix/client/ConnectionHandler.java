package com.neocoretechs.relatrix.client;

import java.io.EOFException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.StandardSocketOptions;

import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
/**
 * Server-side socket accept connection handler
 */
public class ConnectionHandler {
	private static boolean DEBUG = true;
	private final SocketChannel channel;
	private final ObjectOutputStream oos;
	private final ObjectInputStream ois;

	public ConnectionHandler(SocketChannel ch) throws IOException {
		this.channel = ch;
		ch.configureBlocking(true);
		ch.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		ch.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
		ch.setOption(StandardSocketOptions.SO_SNDBUF, 32767);
		ch.setOption(StandardSocketOptions.TCP_NODELAY, true);
		if(DEBUG)
			System.out.printf("%s channel:%s%n",this.getClass().getName(), ch);
		// Wrap channel streams; use non-closing wrapper so closing OOS doesn't close channel
		OutputStream os = Channels.newOutputStream(ch);
		InputStream is = Channels.newInputStream(ch);

		NonClosingOutputStream ncOs = new NonClosingOutputStream(os);
		this.oos = new ObjectOutputStream(ncOs); // writes header once
		this.oos.flush();
		this.ois = new ObjectInputStream(is);    // read side: create once
	}

	// Single-threaded writer or synchronized
	public synchronized void sendObject(Object obj) throws IOException {
		if(DEBUG)
			System.out.printf("%s send object %s channel:%s%n",this.getClass().getName(), obj, this.toString());
		oos.writeObject(obj);
		oos.flush();
		oos.reset(); // avoid cache growth if sending many objects
	}

	public Object readObject() {	
		try {
			return ois.readObject(); // blocks until full object
		} catch (Exception e) {
			e.printStackTrace();
			close();
			throw new RuntimeException(e);
		}
	}
	public void close() {
		// close resources and channel when connection ends
		try { ois.close(); } catch (IOException ignored) {}
		try { oos.close(); } catch (IOException ignored) {} // won't close channel
		try { channel.close(); } catch (IOException ignored) {}
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

class NonClosingOutputStream extends FilterOutputStream {
	public NonClosingOutputStream(OutputStream out) { super(out); }
	@Override
	public void close() throws IOException {
		// flush but do not close the underlying stream/channel
		try { super.flush(); } catch (IOException ignored) {}
		// do not call super.close();
	}
}


