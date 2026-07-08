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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.parallel.SynchronizedThreadManager;

/**
 * Server-side socket accept connection handler
 */
public class ConnectionHandler {
	private static boolean DEBUG = true;
	protected SocketChannel channel;
	private Object mutexWrite = new Object();
	private Object mutexRead = new Object();
	public Object readMx = new Object();
	public Object writeMx = new Object();
	private static int QUEUESIZE = 32;
	public BlockingQueue<Object> readQueue;
	public BlockingQueue<Object> writeQueue;
	// readiness latch to avoid sending before reader is ready
	private final CountDownLatch readerReady = new CountDownLatch(1);
	// poison pill to stop loops cleanly
	private static final Object POISON = new Object();
	// writer/reader threads state
	protected final Reader reader;
	protected final Writer writer;

	public ConnectionHandler() {
		this.readQueue = new ArrayBlockingQueue<>(QUEUESIZE, true);
		this.writeQueue = new ArrayBlockingQueue<>(QUEUESIZE, true);
		this.reader = new Reader(this);
		this.writer = new Writer(this);
	}

	public ConnectionHandler(SocketChannel ch) throws IOException {
		this.channel = ch;
		ch.configureBlocking(true);
		ch.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		ch.setOption(StandardSocketOptions.SO_RCVBUF, 32767);
		ch.setOption(StandardSocketOptions.SO_SNDBUF, 32767);
		ch.setOption(StandardSocketOptions.TCP_NODELAY, true);
		this.readQueue = new ArrayBlockingQueue<Object>(QUEUESIZE, true);
		this.writeQueue = new ArrayBlockingQueue<Object>(QUEUESIZE, true);
		this.reader = new Reader(this);
		this.writer = new Writer(this);
		SynchronizedThreadManager.getInstance().spin(reader);
		SynchronizedThreadManager.getInstance().spin(writer);
		if(DEBUG)
			System.out.printf("%s channel:%s%n",this.getClass().getName(), ch);	
	}

	// Single-threaded writer or synchronized
	public void sendObject(Object obj) throws IOException {
		if(DEBUG)
			System.out.printf("%s send object %s channel:%s%n",this.getClass().getName(), obj, this.toString());
		// Wait for reader to be ready on this connection (avoid race where peer hasn't started reader)
		try {
			boolean ready = readerReady.await(5, TimeUnit.SECONDS);
			if (!ready) {
				throw new IOException("Peer reader not ready for channel " + this.toString());
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted waiting for peer reader", ie);
		}
		//sendObjectFramed(obj);
		//writeQueue.add(obj);
		// Offer with timeout to avoid unbounded queue growth; caller can handle IOException
		try {
			boolean offered = writeQueue.offer(obj, 5, TimeUnit.SECONDS);
			if (!offered) {
				throw new IOException("Write queue full for channel " + this.toString());
			}
			if (DEBUG)
				System.out.printf("%s queued object %s channel:%s%n", this.getClass().getName(), obj, this.toString());
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted while queuing object", ie);
		}
		if(DEBUG)
			System.out.printf("%s queued object %s channel:%s%n",this.getClass().getName(), obj, this.toString());
	}

	public Object readObject() throws IOException, ClassNotFoundException {
		if(DEBUG)
			System.out.printf("%s attempt to recieve object channel:%s%n",this.getClass().getName(), this.toString());
		//Object o = receiveObjectFramed();
		Object o = null;
	    try {
            o = readQueue.take();
            // If poison pill, treat as closed
            if (o == POISON) return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while waiting for read", e);
        }
		if(DEBUG)
			System.out.printf("%s recieved object %s channel:%s%n",this.getClass().getName(),o,this.toString());
		return o;
	}

	public void close() {
		if(DEBUG)
			System.out.printf("%s CLOSING CHANNEL:%s%n",this.getClass().getName(), this.toString());
		shutdown();
		try {
			if (channel != null && channel.isOpen()) 
				channel.close();
		} catch (IOException ignored) {
		}
	}
	/**
	 * Graceful shutdown: stop loops and insert poison pill to unblock queues.
	 */
	public void shutdown() {
		// stop reader/writer loops
		reader.shouldRun = false;
		writer.shouldRun = false;
		// insert poison to unblock readers/writers
		writeQueue.offer(POISON);
		readQueue.offer(POISON);
		// close channel will be done by caller/close()
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


	/***********************
	 * Reader inner class
	 ***********************/
	public class Reader implements Runnable {
		ConnectionHandler in;
		public volatile boolean shouldRun = true;
		public volatile boolean isRunning = false;

		public Reader(ConnectionHandler in) {
			this.in = in;
		}

		public void run() {
			isRunning = true;
			// mark reader ready before entering loop so peer can wait on readerReady
			readerReady.countDown();
			try {
				while (shouldRun) {
					try {
						Object obj = receiveObjectFramed();
						if (obj == null) {
							// remote closed cleanly
							if (DEBUG) System.out.printf("Reader: remote closed for %s%n", in.toString());
							break;
						}
						// put into readQueue (offer with timeout to avoid blocking forever)
						boolean offered = in.readQueue.offer(obj, 5, TimeUnit.SECONDS);
						if (!offered) {
							// queue full: log and fail outstanding or drop per policy
							System.err.printf("Reader: readQueue full for %s, dropping message%n", in.toString());
						}
						synchronized (readMx) {
							readMx.notifyAll();
						}
					} catch (ClassNotFoundException | IOException e) {
						// fatal for this connection: log, fail outstanding, and break
						System.err.printf("Reader exception on %s: %s%n", in.toString(), e);
						//failOutstandingRequests(e);
						break;
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						System.err.printf("Reader interrupted on %s%n", in.toString());
						break;
					}
				}
			} catch (Throwable t) {
				// top-level catch to avoid silent thread death
				System.err.printf("Reader top-level failure on %s: %s%n", in.toString(), t);
				//failOutstandingRequests(t);
			} finally {
				// ensure shutdown and notify writer
				shouldRun = false;
				writer.shouldRun = false;
				writeQueue.offer(POISON);
				isRunning = false;
				try {
					if (channel != null && channel.isOpen()) channel.close();
				} catch (IOException ignored) {}
			}
		}
	}

	/***********************
	 * Writer inner class
	 ***********************/
	public class Writer implements Runnable {
		ConnectionHandler out;
		public volatile boolean shouldRun = true;
		public volatile boolean isRunning = false;

		public Writer(ConnectionHandler out) {
			this.out = out;
		}

		public void run() {
			isRunning = true;
			try {
				// Wait until reader is ready before sending first message (avoid race)
				try {
					readerReady.await(5, TimeUnit.SECONDS);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}

				while (shouldRun) {
					try {
						Object o = writeQueue.take();
						if (o == POISON) break;
						// send framed; sendObjectFramed is synchronized on mutexWrite
						sendObjectFramed(o);
						synchronized (writeMx) {
							writeMx.notifyAll();
						}
					} catch (IOException ioe) {
						System.err.printf("Writer IOException on %s: %s%n", out.toString(), ioe);
						//failOutstandingRequests(ioe);
						break;
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						System.err.printf("Writer interrupted on %s%n", out.toString());
						break;
					}
				}
			} catch (Throwable t) {
				System.err.printf("Writer top-level failure on %s: %s%n", out.toString(), t);
				//failOutstandingRequests(t);
			} finally {
				shouldRun = false;
				reader.shouldRun = false;
				readQueue.offer(POISON);
				isRunning = false;
				try {
					if (channel != null && channel.isOpen()) channel.close();
				} catch (IOException ignored) {}
			}
		}
	}
}



