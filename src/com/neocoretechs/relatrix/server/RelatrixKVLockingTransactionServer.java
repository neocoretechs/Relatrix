package com.neocoretechs.relatrix.server;

import java.io.File;

import com.neocoretechs.relatrix.RelatrixKVTransaction;

public class RelatrixKVLockingTransactionServer {

	public static void main(String[] args) throws Exception {
		RelatrixKVTransaction.getInstance();
		RelatrixKVTransaction.setOptimisticConcurrency(false);
		if(args.length == 3) {
		    String db = (new File(args[0])).toPath().getParent().toString() + File.separator +
		        		(new File(args[0]).getName());
		    System.out.println("Bringing up Relatrix tablespace:"+db);
		    RelatrixKVTransaction.setTablespace(db);
		    new RelatrixKVTransactionServer(args[1], Integer.parseInt(args[2]));
		} else {
			if( args.length == 2) {
			    System.out.println("Bringing up Relatrix default tablespace.");
				new RelatrixKVTransactionServer(args[0], Integer.parseInt(args[1]));
			} else {
				if(args.length == 1) {
					System.out.println("Bringing up Relatrix default tablespace.");
					new RelatrixKVTransactionServer(Integer.parseInt(args[0]));
				} else {
					System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixKVLockingTransactionServer [/path/to/database/databasename] [address] <port>");
				}
			}
		}

	}

}
