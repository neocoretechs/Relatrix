package com.neocoretechs.relatrix.tooling;

import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
/**
 * Roll back all in progress transaction on the server. Use with caution.
 * Exceptions likely to be thrown server side if active processes. use is mainly
 * for leftover zombie transactions.<p/>
 * According to docs states are:
 * AWAITING_COMMIT AWAITING_PREPARE AWAITING_ROLLBACK COMMITED COMMITTED (?)
 * LOCKS_STOLEN PREPARED ROLLEDBACK STARTED. In practice, it seems to mainly vary between
 * STARTED and COMMITTED. The odd 'COMMITED' state doesnt seem to manifest.
 * @author Jonathan Groff (C) Copyright NeoCoreTechs 2023
 *
 */
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
