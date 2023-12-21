package com.neocoretechs.relatrix.tooling;

import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;

public class RollbackAllServerTransactions {
	public static void main(String[] args) throws Exception {
		RelatrixKVClientTransaction rkvc = new RelatrixKVClientTransaction(args[0], args[1], Integer.parseInt(args[2]));
		Object[] states = rkvc.getTransactionState();
		for(Object s : states) {
			System.out.println(s);
		}
		System.out.println("Preparing to rollback all transactions, proceed?");
		System.in.read();
		rkvc.rollbackOutstandingTransactions();
		rkvc.close();
		System.exit(0);
	}
}
