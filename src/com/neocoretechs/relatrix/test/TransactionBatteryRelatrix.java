package com.neocoretechs.relatrix.test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.relatrix.DomainMapRangeTransaction;
import com.neocoretechs.relatrix.DuplicateKeyException;

import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.relatrix.client.RemoteStream;

/**
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the battery1 testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * NOTES:
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * A database unique to this test module should be used.
 * @author jg C 2017
 *
 */
public class TransactionBatteryRelatrix {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 2000;
	public static String DATABASE;
	public static int DATABASE_PORT = 9030;

	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		RelatrixClientTransaction session = null;
		DATABASE = argv[0];
		session = new RelatrixClientTransaction(DATABASE, DATABASE, DATABASE_PORT);
		String xid = session.getTransactionId();
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
	public static void battery0(RelatrixClientTransaction rct, String xid) throws Exception {
		System.out.println("Battery0 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int i = min;
		//for(; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				rct.store(xid, fkey, "Has unit", new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		//}
		System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	/**
	 * Loads up on keys, we have same domain with multiple maps to test unique keys in multiple domains with same map
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(RelatrixClientTransaction rct, String xid) throws Exception {
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
	public static void battery2(RelatrixClientTransaction rct, String xid) throws Exception {
		System.out.println("Battery2 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
				RemoteStream rs = rct.findSetStream(xid, fkey, "Has unit", new Long(i));
				if(rs.of().count() != 1)
					System.out.println("Stream mismatch, should be 1 but is:"+rs.of().count());
				Optional<?> o = rs.of().findFirst();
				if(o.isPresent()) {
					rs = rct.findSetStream(xid,  o.get(), "*", o.get());
					Optional<?> p = rs.of().findFirst();
					if(p.isPresent()) {
						Comparable[] c = (Comparable[]) p.get();
						DomainMapRangeTransaction d = (DomainMapRangeTransaction) c[0];
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
