package com.neocoretechs.relatrix.server;

import java.io.IOException;
import java.util.Random;

import org.rocksdb.RocksDBException;
import org.rocksdb.Status;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Series of error handlers to retry commit on optimistic transaction database exception during commit phase.
 * Uses exponential backoff during retry of commit for various clients of transactional database sessions.
 *  
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2020
 */
public class ErrorUtil {
	static int maxRetries = 5;
	static int initialDelay = 100; // 100 ms
	static int maxDelay = 30000; // 30 seconds
	static double backoffFactor = 2.0;
	static Random random = new Random();
	
	public static void handleCommitBusyRetry(RelatrixClientTransaction session, TransactionId xid) throws IOException, InterruptedException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				break; // Commit successful, exit loop
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
	}
	public static void handleCommitBusyRetry(RelatrixKVClientTransaction session, TransactionId xid) throws IOException, InterruptedException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				break; // Commit successful, exit loop
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
	}
	public static void handleCommitBusyRetry(RelatrixKVTransaction session, TransactionId xid) throws IOException, InterruptedException, IllegalAccessException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				break; // Commit successful, exit loop
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
	}
	public static void handleCommitBusyRetry(RelatrixTransaction session, TransactionId xid) throws IOException, InterruptedException, IllegalAccessException {
		Status s = null;
		for (int retry = 0; retry < maxRetries; retry++) {
			try {
				// Perform transactional operations
				session.commit(xid);
				break; // Commit successful, exit loop
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
	}
}
