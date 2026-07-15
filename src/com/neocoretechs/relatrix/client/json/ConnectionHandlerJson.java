package com.neocoretechs.relatrix.client.json;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

import org.json.JSONObject;

import com.neocoretechs.relatrix.client.ConnectionHandler;
import com.neocoretechs.relatrix.client.json.util.Converter;

/**
 * Server-side socket accept connection handler that passes CBOR payloads instead of serialized objects.
 * send and receive take a JSNObject and return a JSONObject.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public class ConnectionHandlerJson extends ConnectionHandler {
	private static boolean DEBUG = true;

	public ConnectionHandlerJson() {
		super();
	}

	public ConnectionHandlerJson(SocketChannel ch) throws IOException {
		super(ch);
	}

	@Override
	/**
	 * @param obj JSONObject
	 */
	public void sendObjectFramed(Object obj) throws IOException {
		Object o = Converter.getMorphicObject((JSONObject) obj);
		byte[] payload = Converter.getMorphicBytes(o);
		ByteBuffer buf = ByteBuffer.allocate(4 + payload.length);
		buf.putInt(payload.length);
		buf.put(payload);
		buf.flip();
		synchronized (mutexWrite) {
			while (buf.hasRemaining()) {
				int written = channel.write(buf);
				if (written == 0) {
					// blocking channel should not return 0 normally; if it does, yield briefly
					Thread.yield();
				}
			}
		}
		/*
		synchronized(mutexWrite) {
			OutputStream os = Channels.newOutputStream(channel);
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(payload.length);
			dos.write(payload);
			dos.flush();
		}*/
	}
	
	@Override
	/**
	 *  Blocking framed receive using Channels.newInputStream(channel) 
	 *  @return JSONObject
	 */
	public Object receiveObjectFramed() throws IOException, ClassNotFoundException {
		byte[] payload;
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
		return Converter.getMorphicObject(payload);
	}

}



