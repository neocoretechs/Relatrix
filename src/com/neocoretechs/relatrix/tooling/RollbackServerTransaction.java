package com.neocoretechs.relatrix.tooling;

import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Roll back selected transactiosn on the server. Use with caution.
 * Exceptions likely to be thrown server side if active processes. use is mainly
 * for leftover zombie transactions.<p/>
 * According to docs states are:
 * AWAITING_COMMIT AWAITING_PREPARE AWAITING_ROLLBACK COMMITED COMMITTED (?)
 * LOCKS_STOLEN PREPARED ROLLEDBACK STARTED. In practice, it seems to mainly vary between
 * STARTED and COMMITTED. The odd 'COMMITED' state doesnt seem to manifest.
 * @author Jonathan Groff (C) Copyright NeoCoreTechs 2023
 *
 */
public class RollbackServerTransaction {
	public static void main(String[] args) throws Exception {
		RelatrixKVClientTransaction rkvc = new RelatrixKVClientTransaction(args[0], args[1], Integer.parseInt(args[2]));
		Object[] states = rkvc.getTransactionState();
		int i = 0;
		for(Object s : states) {
			System.out.println(++i+".) "+s);
		}
		if(i == 0) {
			System.out.println("No transactions found.");
			rkvc.close();
			System.exit(0);
		}
		if(i == 1)
			System.out.println("Enter '1' to confirm, then hit return, or <ctrl>-C to terminate.");
		else
			System.out.println("Select transaction to rollback 1.-"+i+".) then hit return, or <ctrl>-C to terminate.");
		int j = 0;
		StringBuilder asc = new StringBuilder(0);
		while(j != 10) {
			j = System.in.read();
			if((char)j >= '0' && (char)j <= '9')
				asc.append((char)j);
		}
		int select = Integer.parseInt(asc.toString());
		TransactionId xid = new TransactionId(((String)states[select-1]).substring(12,48));
		System.out.println("Removing "+select+".) " + xid);
		rkvc.rollbackTransaction(xid);
		rkvc.close();
		System.exit(0);
	}
}
