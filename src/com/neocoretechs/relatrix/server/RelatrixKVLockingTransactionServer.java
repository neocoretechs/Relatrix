package com.neocoretechs.relatrix.server;

import java.io.File;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

public class RelatrixKVLockingTransactionServer {

	public static void main(String[] args) throws Exception {
		RelatrixKVTransaction.getInstance();
		RelatrixKVTransaction.setOptimisticConcurrency(false);
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixKVLockingTransaction default tablespace.");
			new RelatrixKVTransactionServer(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixKVLockingTransaction default tablespace.");
				new RelatrixKVTransactionServer(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVLockingTransactionServer [address] <port>");
			}
		}
		System.out.println(RelatrixKVTransactionServer.address);
	}

}
