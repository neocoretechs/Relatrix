package com.neocoretechs.relatrix.server.json;

import com.neocoretechs.relatrix.RelatrixJsonTransaction;

public class RelatrixLockingTransactionServerJson {

	public static void main(String[] args) throws Exception{
		RelatrixJsonTransaction.getInstance();
		RelatrixJsonTransaction.setOptimisticConcurrency(false);
		if( args.length == 2) {
			System.out.println("Bringing up RelatrixLockingJsonTransaction tablespace "+System.getProperty("tablespace"));
			new RelatrixTransactionServerJson(args[0], Integer.parseInt(args[1]));
		} else {
			if(args.length == 1) {
				System.out.println("Bringing up RelatrixLockingJsonTransaction tablespace "+System.getProperty("tablespace"));
				new RelatrixTransactionServerJson(Integer.parseInt(args[0]));
			} else {
				System.out.println("usage: java com.neocoretechs.relatrix.server.RelatrixLockingTransactionServerJson [address] <port>");
			}
		}
		System.out.println(RelatrixTransactionServerJson.address);
	}

}
