package com.neocoretechs.relatrix.server;

import java.io.File;

import com.neocoretechs.relatrix.RelatrixTransaction;

public class RelatrixLockingTransactionServer {

	public static void main(String[] args) throws Exception{
		RelatrixTransaction.getInstance();
		RelatrixTransaction.setOptimisticConcurrency(false);
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixLockingTransaction tablespace "+System.getProperty("tablespace"));
			new RelatrixTransactionServer(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixLockingTransaction tablespace "+System.getProperty("tablespace"));
				new RelatrixTransactionServer(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixLockingTransactionServer [address] <port>");
			}
		}
		System.out.println(RelatrixTransactionServer.address);
	}

}
