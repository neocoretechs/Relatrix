package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.rocksack.TransactionId;

/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findStream retrieval using the client to a remote {@link com.neocoretechs.relatrix.server.RelatrixTransactionServer}.
 * NOTES:
 * program arguments are local_node remote_node remote_port_for_database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public class TransactionBatteryRelatrix {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 2000;
	public static String DATABASE;

	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		RelatrixClientTransaction session = null;
		DATABASE = argv[0];		
		session = new RelatrixClientTransaction(argv[0], argv[1], Integer.parseInt(argv[2]) );
		TransactionId xid = session.getTransactionId();
		System.out.println("Test battery got trans Id:"+xid);
		//Relatrix.setTablespaceDirectory(argv[0]);
		battery0(session, xid);
		//battery1(session, xid);
		session.commit(xid);
		session.endTransaction(xid);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(1);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery0(RelatrixClientTransaction rct, TransactionId xid) throws Exception {
		System.out.println("Battery0 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int i = min;
		//for(; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			rct.store(xid, fkey, "Has unit", new Long(i));
			++recs;
		//}
		System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	/**
	 * Loads up on keys, we have same domain with multiple maps to test unique keys in multiple domains with same map
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(RelatrixClientTransaction rct, TransactionId xid) throws Exception {
		System.out.println("Battery1 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		Iterator<?> it = (Iterator<?>) rct.findSet(xid, "*", "*", "*");
		ArrayList<Comparable> ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//Relatrix.transactionalStore(c[0],"has identity",c[0]);
			ar.add(c[0]);
		}
		for(Comparable c: ar) {
			System.out.println("About to store functor:"+c);
			rct.store(xid, c,"has identity",c);
		}
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs);
	}
	public static void battery2(RelatrixClientTransaction rct, TransactionId xid) throws Exception {
		System.out.println("Battery2 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
				Stream rs =  rct.findStream(xid, fkey, "Has unit", new Long(i));
				if(rs.count() != 1)
					System.out.println("Stream mismatch, should be 1 but is:"+rs.count());
				Optional<?> o = rs.findFirst();
				if(o.isPresent()) {
					rs = rct.findStream(xid,  o.get(), "*", o.get());
					Optional<?> p = rs.findFirst();
					if(p.isPresent()) {
						Comparable[] c = (Comparable[]) p.get();
						DomainMapRange d = (DomainMapRange) c[0];
						if(!d.getDomain().equals(fkey))
							System.out.println("Domain identity doesnt match "+fkey);
						if(!d.getMap().equals("has identity"))
							System.out.println("Map identity doesnt match 'has identity'");
						if(!d.getRange().equals(i))
							System.out.println("Range identity doesnt match "+i);
					} else
						System.out.println("Failed to find identity for "+o.get());	
				} else
					System.out.println("Failed to find identity for "+fkey);
				++recs;
		}
		System.out.println("BATTERY2 verification SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs);
	}

}
