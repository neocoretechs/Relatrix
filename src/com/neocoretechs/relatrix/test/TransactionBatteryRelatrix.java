package com.neocoretechs.relatrix.test;


import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.bigsack.keyvaluepages.KeyValue;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DuplicateKeyException;

import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;

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

	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		 //System.out.println("Analysis of all");
		Relatrix.setTablespaceDirectory(argv[0]);
		battery0(argv);
		battery1(argv);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(1);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery0(String[] argv) throws Exception {
		System.out.println("Battery0 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relatrix.transactionalStore(fkey, "Has unit", new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		Relatrix.transactionCommit();
		 System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	/**
	 * Loads up on keys, we have same domain with multiple maps to test unique keys in multiple domains with same map
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		Iterator<?> it = Relatrix.findSet("*", "*", "*");
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
			if(((KeyValue) (RelatrixKV.get(c))).getmValue() == null)
				continue;
			Relatrix.transactionalStore(c,"has identity",c);
		}
		Relatrix.transactionCommit();
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs);
	}
	

}
