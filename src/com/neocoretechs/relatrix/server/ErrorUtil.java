package com.neocoretechs.relatrix.server;

import java.io.IOException;
import java.util.Random;

import org.rocksdb.RocksDBException;
import org.rocksdb.Status;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;

/**
 * Series of error handlers to retry commit on optimistic transaction database exception during commit phase.
 * Uses exponential backoff during retry of commit for various clients of transactional database sessions.<p>
 * Also handles sanitized errors from servers to clients.
 *  
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020,2026
 */
public class ErrorUtil {
	static int maxRetries = 60;
	static int initialDelay = 10; // 10 ms
	static int maxDelay = 60000; // 60 seconds
	static double backoffFactor = 2.0;
	static Random random = new Random();
	/**
	 * Handle the response from the transaction server while attempting to commit. Use exponential backoff to wait
	 * successively increasing times, up to maximum for the given maximum for responses of Status.Code.Busy or
	 * Status.Code.TryAgain in a RocksDBException on commit
	 * @param session The client transaction session
	 * @param xid The transaction id
	 * @throws IOException If any exception other than those described above occurs, or if max retries is exceeded.
	 * @throws InterruptedException If interrupted during thread sleep
	 */
	public static void handleCommitBusyRetry(RelatrixClientTransaction session, TransactionId xid) throws IOException, InterruptedException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				return; // Commit successful, exit
			} catch (IOException e) {
				Throwable c = e.getCause();
				if(c instanceof IOException) {
					c = c.getCause();
					if(c instanceof RocksDBException) {
						s = ((RocksDBException)c).getStatus();
						if (s.getCode() == Status.Code.Busy || s.getCode() == Status.Code.TryAgain) {
							int delay = (int) Math.min(maxDelay, initialDelay * Math.pow(backoffFactor, retry));
							int jitter = random.nextInt((int) (delay * 0.1)); // 10% jitter
							delay += jitter;
							System.out.println("Commit retry in:"+delay+" ms.");
							Thread.sleep(delay);
						} else {
							throw e; // Rethrow other exceptions
						}
					} else {
						throw e;
					}
				} else {
					throw e;
				}
			}
		}
		throw new IOException("Commit failed after "+maxRetries+" giving up.");
	}
	/**
	 * Handle the response from the transaction server while attempting to commit. Use exponential backoff to wait
	 * successively increasing times, up to maximum for the given maximum for responses of Status.Code.Busy or
	 * Status.Code.TryAgain in a RocksDBException on commit
	 * @param session The KV client transaction session
	 * @param xid The transaction id
	 * @throws IOException If any exception other than those described above occurs, or if max retries is exceeded.
	 * @throws InterruptedException If interrupted during thread sleep
	 */
	public static void handleCommitBusyRetry(RelatrixKVClientTransaction session, TransactionId xid) throws IOException, InterruptedException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				return; // Commit successful, exit
			} catch (IOException e) {
				Throwable c = e.getCause();
				if(c instanceof IOException) {
					c = c.getCause();
					if(c instanceof RocksDBException) {
						s = ((RocksDBException)c).getStatus();
						if (s.getCode() == Status.Code.Busy || s.getCode() == Status.Code.TryAgain) {
							int delay = (int) Math.min(maxDelay, initialDelay * Math.pow(backoffFactor, retry));
							int jitter = random.nextInt((int) (delay * 0.1)); // 10% jitter
							delay += jitter;
							System.out.println("Commit retry in:"+delay+" ms.");
							Thread.sleep(delay);
						} else {
							throw e; // Rethrow other exceptions
						}
					} else {
						throw e;
					}
				} else {
					throw e;
				}
			}
		}
		throw new IOException("Commit failed after "+maxRetries+" giving up.");
	}
	/**
	 * Handle the response from the transaction server while attempting to commit. Use exponential backoff to wait
	 * successively increasing times, up to maximum for the given maximum for responses of Status.Code.Busy or
	 * Status.Code.TryAgain in a RocksDBException on commit
	 * @param session The embedded KV transaction session
	 * @param xid The transaction id
	 * @throws IOException If any exception other than those described above occurs, or if max retries is exceeded.
	 * @throws InterruptedException If interrupted during thread sleep
	 */
	public static void handleCommitBusyRetry(RelatrixKVTransaction session, TransactionId xid) throws IOException, InterruptedException, IllegalAccessException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				return; // Commit successful, exit
			} catch (IOException e) {
				Throwable c = e.getCause();
				if(c instanceof IOException) {
					c = c.getCause();
					if(c instanceof RocksDBException) {
						s = ((RocksDBException)c).getStatus();
						if (s.getCode() == Status.Code.Busy || s.getCode() == Status.Code.TryAgain) {
							int delay = (int) Math.min(maxDelay, initialDelay * Math.pow(backoffFactor, retry));
							int jitter = random.nextInt((int) (delay * 0.1)); // 10% jitter
							delay += jitter;
							System.out.println("Commit retry in:"+delay+" ms.");
							Thread.sleep(delay);
						} else {
							throw e; // Rethrow other exceptions
						}
					} else {
						throw e;
					}
				} else {
					throw e;
				}
			}
		}
		throw new IOException("Commit failed after "+maxRetries+" giving up.");
	}
	/**
	 * Handle the response from the transaction server while attempting to commit. Use exponential backoff to wait
	 * successively increasing times, up to maximum for the given maximum for responses of Status.Code.Busy or
	 * Status.Code.TryAgain in a RocksDBException on commit
	 * @param session The embedded transaction session
	 * @param xid The transaction id
	 * @throws IOException If any exception other than those described above occurs, or if max retries is exceeded.
	 * @throws InterruptedException If interrupted during thread sleep
	 */
	public static void handleCommitBusyRetry(RelatrixTransaction session, TransactionId xid) throws IOException, InterruptedException, IllegalAccessException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				return; // Commit successful, exit
			} catch (IOException e) {
				Throwable c = e.getCause();
				if(c instanceof IOException) {
					c = c.getCause();
					if(c instanceof RocksDBException) {
						s = ((RocksDBException)c).getStatus();
						if (s.getCode() == Status.Code.Busy || s.getCode() == Status.Code.TryAgain) {
							int delay = (int) Math.min(maxDelay, initialDelay * Math.pow(backoffFactor, retry));
							int jitter = random.nextInt((int) (delay * 0.1)); // 10% jitter
							delay += jitter;
							System.out.println("Commit retry in:"+delay+" ms.");
							Thread.sleep(delay);
						} else {
							throw e; // Rethrow other exceptions
						}
					} else {
						throw e;
					}
				} else {
					throw e;
				}
			}
		}
		throw new IOException("Commit failed after "+maxRetries+" giving up.");
	}
	
	private static String safeMessage(String msg) {
	    if (msg == null) return "unexpected error";
	    String cleaned = msg.replaceAll("[\\r\\n]+", " ");
	    cleaned = cleaned.replaceAll("(/[^\\s]{20,})", "[redacted]");
	    cleaned = cleaned.replaceAll("\\s{2,}", " ").trim();
	    return cleaned.length() > 200 ? cleaned.substring(0, 200) + "…" : cleaned;
	}

	private static String mapToErrorCode(Throwable t) {
	    if (t == null) return "SERVER_ERROR";
	    // Example mappings, adapt to your domain
	    if (t instanceof IllegalArgumentException) return "BAD_REQUEST";
	    if (t instanceof java.nio.channels.ClosedChannelException) return "CONNECTION_CLOSED";
	    if (t.getClass().getSimpleName().contains("Index")) return "INDEX_ERROR";
	    return "SERVER_ERROR";
	}
	
	public static Throwable formatError(Throwable t, String correlationId) {
	    String errorCode = mapToErrorCode(t);
	    String message = safeMessage(t == null ? null : t.getMessage());
	    String causeClass = t == null ? "Unknown" : t.getClass().getSimpleName();
	    String serverTime = java.time.Instant.now().toString();
	    StringBuilder sb = new StringBuilder();
	    sb.append("errorCode:");
	    sb.append(errorCode);
	    sb.append(" cause:");
	    sb.append(safeMessage(t.getCause() == null ? null : t.getCause().toString()));
	    sb.append("\r\n");
	    sb.append("message:");
	    sb.append(message);
	    sb.append("\r\n");
	    sb.append("correlationId:");
	    sb.append(correlationId);
	    sb.append(" serverTime");
	    sb.append(serverTime);
	    sb.append(" causeClass");
	    sb.append(causeClass);
	    sb.append("\r\n");
	    return new Throwable(sb.toString());
	}

}
